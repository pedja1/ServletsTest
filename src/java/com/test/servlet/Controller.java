/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.test.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author pedja
 */
public abstract class Controller
{
    
    private static final long serialVersionUID = 1L;

    static Logger logger = Logger.getLogger(Controller.class);
    
    HttpServlet servlet;
    protected final Map<String, String[]> requestParams = new HashMap<>();

    public Controller(HttpServlet servlet, Map<String, String[]> requestParams)
    {
        this.servlet = servlet;
        if(requestParams != null)
        {
            this.requestParams.putAll(requestParams);
        }
    }
    
    protected String getParam(String key)
    {
        String[] param = requestParams.get(key);
        if(param == null || param.length < 1)
        {
            return null;
        }
        return param[0];
    }
    
    public abstract void setResponse(HttpServletResponse response) throws ServletException, IOException;
}
