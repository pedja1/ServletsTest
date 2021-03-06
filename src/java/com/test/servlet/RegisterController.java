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
import static com.test.servlet.Controller.logger;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import javax.mail.MessagingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class RegisterController extends Controller
{

    public RegisterController(MainServlet servlet, Map<String, String[]> requestParams)
    {
        super(servlet, requestParams);
    }

    @Override
    public void setResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter writer = response.getWriter();
        String email = getParam(RequestParam.email.toString());
        String password = getParam(RequestParam.password.toString());
        String firstName = getParam(RequestParam.first_name.toString());
        String lastName = getParam(RequestParam.last_name.toString());
        String errorMsg = null;
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
        SessionGenerator generator = SessionGenerator.getInstance();
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastname(lastName);
        user.setPassword(password);
        user.setRegistration_key(generator.nextSessionId());
        boolean result = dbUtil.insertUser(user);
        if (result)
        {
            try
            {
                MailUtility.sendVerificationEmail(user);
            }
            catch (MessagingException ex)
            {
                ex.printStackTrace();
            }

            JSONObject jReponse = new JSONObject();
            jReponse.put(JSONKey.status.toString(), 0);
            jReponse.put(JSONKey.message.toString(), "Registration successful. Check your email for instructions on how to verify your account.");

            writer.print(jReponse.toString());
        }
        else
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.email_exists.toString(), "Email address already exists"));
        }
    }

}
