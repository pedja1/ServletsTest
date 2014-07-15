/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.test.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author pedja
 */
public class MainServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;

    static Logger logger = Logger.getLogger(MainServlet.class);
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        String command = request.getParameter(RequestParam.cmd.toString());
        if(Utility.isStringEmpty(command))
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.command_not_specified.toString(), "You must specify command/action for this request"));
            return;
        }
        
        Command cmd;
        try
        {
            cmd = Command.valueOf(command);
        }
        catch(Exception e)
        {
            cmd = Command.no_command;
        }
        
        switch(cmd)
        {
            case login:
                new LoginController(this, request.getParameterMap()).setResponse(response);
                break;
            case register:
                new RegisterController(this, request.getParameterMap()).setResponse(response);
                break;
            default:
                writer.print(Utility.generalErrorMessage(ResponseCode.command_not_found.toString(), String.format("No such command '%s'", command)));
                break;
        }
        
        /*DBUtility db = new DBUtility(this);
        List<User> users = new ArrayList<>();
        for(int i = 0; i < 1000000; i++)
        {
            User user = new User();
            user.setEmail("email" + i + "@gmail.com");
            user.setFirstName("Name" + i);
            user.setLastname("Last Name" + i);
            user.setPassword("password" + i);
            users.add(user);
        }
        db.insertUsersInTx(users);*/
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "api";
    }// </editor-fold>

}
