/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Marcel
 */
public class API extends HttpServlet {

    
    String url = "jdbc:mysql://localhost:3306/";
    String dbName = "cinema";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "root"; 
    String password = "start";



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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    PrintWriter out = response.getWriter();
 
    int[] seats = new int[2];
    seats[0] = 123;
    seats[1] = 21;
    ObjectMapper mapper = new ObjectMapper();
 
    try {
      // display to console
      out.println(mapper.writeValueAsString(seats));
    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    out.close();
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    PrintWriter out = response.getWriter();
 
    ObjectMapper mapper = new ObjectMapper();
    
 
    try {
      // read from file, convert it to user class
      Reservation res = mapper.readValue(request.getReader(), Reservation.class);
      Class.forName(driver).newInstance();
      Connection conn = DriverManager.getConnection(url+dbName,userName,password);  
      Statement st = conn.createStatement();
      st.executeQuery("INSERT INTO cinema.movies VALUES (NULL,"+res.getMovie()+")");
      conn.close();
      //put into DB
      //get id from database
      //return to user
      out.println(res.getSeats()[0]);
    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
        
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e){
        e.printStackTrace();
    }
    out.close();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Cinema API";
    }// </editor-fold>

}
