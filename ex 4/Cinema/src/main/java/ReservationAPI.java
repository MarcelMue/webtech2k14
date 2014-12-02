
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Team Yellow
 */
public class ReservationAPI extends HttpServlet {
    String url = "jdbc:mysql://localhost:3306/";
    //TODO: input your root mysql username, password and change the dbName accordingly 
    String dbName = "test";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "root"; 
    String password = "";
    
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
    
        JsonReader jsonReader=Json.createReader(request.getReader());
        JsonObject ReservationObj=jsonReader.readObject();
        jsonReader.close();
    try {   
        //open the Database Connection
        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(url+dbName,userName,password);  
        //insert the reservation data of the json into the database 
        //the posted json has the form :  {"name": "Philipp", "email": "TEst@asdasd", "cinemovie_id": 7, "seats" : [1,2,3,5] }
        Statement updateStatement = conn.createStatement();
        updateStatement.executeUpdate("INSERT INTO reservations VALUES(NULL,'"
                                    + ReservationObj.getString("name")+"','"
                                    + ReservationObj.getString("email")+"',"
                                    + ReservationObj.getString("cinemovie_id")+")");
        //get the seats array of the json
        JsonArray seats=ReservationObj.getJsonArray("seats");
        int k=0;
        //insert the reservated seats
        while(k<seats.size()){
            updateStatement.executeUpdate("INSERT INTO cinemovies_seats VALUES("
                    +ReservationObj.getString("cinemovie_id")+","
                    +seats.getInt(k)+",LAST_INSERT_ID())");
            k++;
        }   
        Statement queryStatement = conn.createStatement();
        ResultSet reservationID=queryStatement.executeQuery("SELECT LAST_INSERT_ID() as id");
        reservationID.next();
        
        out.println("{\"id\": "+reservationID.getInt("id")+"}");
        conn.close();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e){
        out.println(e.toString());
    }
    out.close();
    }
    
}