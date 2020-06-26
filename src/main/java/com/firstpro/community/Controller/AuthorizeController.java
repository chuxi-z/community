package com.firstpro.community.Controller;

import com.firstpro.community.Provider.GithubProvider;
import com.firstpro.community.dto.AccessTokenDTO;
import com.firstpro.community.dto.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@PropertySource({"classpath:application.properties"})
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider  githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(value="code") String code,
                           @RequestParam(name="state") String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("b232b7575e5e6f83d4bc");
        accessTokenDTO.setClient_secret("541dfb5994031965b0c605513eb339e4d650204c");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8080/callback");
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
