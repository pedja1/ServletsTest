/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.servlet;

/**
 *
 * @author pedja
 */
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public class LoginController extends Controller
{

    public LoginController(HttpServlet servlet, Map<String, String[]> requestParams)
    {
        super(servlet, requestParams);
    }

    @Override
    public void setResponse(HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter writer = response.getWriter();
        String email = getParam(RequestParam.email.toString());
        String password = getParam(RequestParam.password.toString());
        if (Utility.isStringEmpty(email))
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.email_not_provided.toString(), "Email is required!"));
            return;
        }
        if (Utility.isStringEmpty(password))
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.password_not_provided.toString(), "Password is required!"));
            return;
        }

        DBUtility dbUtil = new DBUtility(servlet);
        User user = dbUtil.getUser(email, password);
        if (user != null)
        {
            logger.info("User found with details=" + user);

            String sessionKey = SessionGenerator.getInstance().nextSessionId();
            Cookie cookie = new Cookie("auth_key", sessionKey);
            cookie.setMaxAge(Constants.COOKIE_AGE);
            response.addCookie(cookie);
            
            dbUtil.updateUser(user.getId(), "auth_key", sessionKey);
            
            JSONObject jReponse = new JSONObject();
            jReponse.put(JSONKey.status.toString(), 0);
            jReponse.put(JSONKey.user_info.toString(), user.toJSONObject());

            writer.print(jReponse.toString());
        }
        else
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.email_doesnt_exist.toString(), "Email address not found"));
        }
    }
    
    public void saveUsersAuthKey(String authKey)
    {
        
    }

}
