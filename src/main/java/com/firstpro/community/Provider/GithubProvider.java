package com.firstpro.community.Provider;

import com.alibaba.fastjson.JSON;
import com.firstpro.community.dto.AccessTokenDTO;
import com.firstpro.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
//import org.springframework.web.bind.annotation.RequestBody;

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

//        "https://github.com/login/oauth/access_token?client_id=b232b7575e5e6f83d4bc&client_secret=541dfb5994031965b0c605513eb339e4d650204c&code="+
//                accessTokenDTO.getCode()+
//                "&redirect_uri=http://localhost:8080/callback&state=1"

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token?client_id="+
                        accessTokenDTO.getClient_id()+"&client_secret="+
                        accessTokenDTO.getClient_secret()+"&code="+
                        accessTokenDTO.getCode()+"&redirect_uri="+
                        accessTokenDTO.getRedirect_uri()+"&state="+
                        accessTokenDTO.getState())
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
//            String[] split = str.split("&");
//            String tokensplit = split[0];
            String token = str.split("&")[0].split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ff");
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();
//                .url("https://api.github.com/user?access_token=" + accessToken)
//                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            GithubUser githubUser = JSON.parseObject(str, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ff");
        return null;
    }
}
