/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.servlet;

import static com.test.servlet.Controller.logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author pedja
 */
public class DBUtility
{

    Connection conn;

    public DBUtility(HttpServlet servlet)
    {
        this.conn = (Connection) servlet.getServletContext().getAttribute("DBConnection");
    }

    public User getUser(String email, String password) throws ServletException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = conn.prepareStatement("select id, email, first_name, last_name from user where email=? and password=? limit 1");
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs != null && rs.next())
            {
                return getUser(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (rs != null)rs.close();
                if(ps != null)ps.close();
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
        return null;
    }

    public User getUser(String auth_key) throws ServletException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = conn.prepareStatement("select id, email, first_name, last_name from user where auth_key=? limit 1");
            ps.setString(1, auth_key);
            rs = ps.executeQuery();

            if (rs != null && rs.next())
            {
                return getUser(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (rs != null)rs.close();
                if(ps != null)ps.close();
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
        return null;
    }

    private User getUser(ResultSet rs) throws SQLException
    {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastname(rs.getString("last_name"));
        return user;
    }
    
    public boolean insertUser(User user) throws ServletException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = conn.prepareStatement("insert into user(first_name, last_name, email, password) values (?,?,?,?)");
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastname());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.execute();
            return true;
        }
        catch (SQLException e)
        {
            //if we get error user probably exists
            return false;
        }
        finally
        {
            try
            {
                if (rs != null)rs.close();
                if(ps != null)ps.close();
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
    }
    
    public void updateUser(int id, String column, String value) throws ServletException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = conn.prepareStatement(String.format("UPDATE user SET %s = ? WHERE id = ?", column));
            ps.setString(1, value);
            ps.setInt(2, id);
            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (rs != null)rs.close();
                if(ps != null)ps.close();
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
    }

}
