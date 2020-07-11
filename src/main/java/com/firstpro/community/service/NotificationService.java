package com.firstpro.community.service;

import com.firstpro.community.dto.NotificationDTO;
import com.firstpro.community.dto.PaginationDTO;
import com.firstpro.community.enums.NotificationEnum;
import com.firstpro.community.enums.NotificationStatusEnum;
import com.firstpro.community.exception.CustomizeErrorCode;
import com.firstpro.community.exception.CustomizeException;
import com.firstpro.community.mapper.NotificationMapper;
import com.firstpro.community.mapper.UserMapper;
import com.firstpro.community.model.Notification;
import com.firstpro.community.model.NotificationExample;
import com.firstpro.community.model.User;
import com.firstpro.community.model.UserExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    @Resource
    private NotificationMapper notificationMapper;

    @Autowired
    @Resource
    private UserMapper userMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
//        Integer totalCount = questionMapper.countByUserId(userId);

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);
        Integer totalPage;
        if(totalCount%size == 0){
            totalPage = totalCount/size;
        }
        else{
            totalPage = totalCount/size + 1;
        }

        if(page < 1){
            page = 1;
        }
        else if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);

        Integer offset = size*(page-1);
//        List<Question> questions = questionMapper.listByUserId(userId, offset, size);

        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);

        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        if (notifications.size() == 0) {
            return paginationDTO;
        }

//        Set<Long> disUserIds = notifications.stream().map(notify -> notify.getNotifier()).collect(Collectors.toSet());
//        List<Long> userIds = new ArrayList<>(disUserIds);
//        UserExample userExample = new UserExample();
//        userExample.createCriteria()
//                .andIdIn(userIds);
//        List<User> users = userMapper.selectByExample(userExample);
//        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(u -> u.getId(), u -> u));


        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));
            notificationDTOs.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOs);
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!notification.getReceiver().equals(user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        //更新已读状态
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
