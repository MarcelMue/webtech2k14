
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Roftimao
 */
public class CinemaMovieAPI extends HttpServlet {
    String url = "jdbc:mysql://localhost:3306/";
    String dbName = "test";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "root"; 
    String password = "";
  /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *//*SELECT cinemas_movies.id, movies.name,GROUP_CONCAT(cinemovies_seats.seat) as seats FROM cinemas_movies
    LEFT join movies on movies.id=cinemas_movies.movie_id  
    LEFT join cinemovies_seats on cinemas_movies.id=cinemovies_seats.cinemovie_id 
    WHERE cinema='Tada' GROUP by cinemas_movies.id
    @Override*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        String requestedCinema= request.getQueryString();
        try {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url+dbName,userName,password);  
            Statement queryStatement = conn.createStatement();
            ResultSet seatsPerShow = queryStatement.executeQuery("SELECT cm.id, m.name,GROUP_CONCAT(cs.seat) as seats FROM cinemas_movies as cm "
                                                        +" LEFT join movies as m on m.id=cm.movie_id  "
                                                        +" LEFT join cinemovies_seats as cs on cm.id=cs.cinemovie_id "
                                                        +" WHERE cinema='"+ requestedCinema+"' GROUP by cm.id");
            
           
            String result="[";
            while(seatsPerShow.next()){
                result+="{\"name\": "+"\""+seatsPerShow.getString("name")+"\", ";
                result+="\"cinemovie_id\": "+seatsPerShow.getInt("id")+", ";
                if(seatsPerShow.getString("seats")!=null){
                     result+="\"seats\": ["+seatsPerShow.getString("seats")+"]}";
                }else{
                     result+="\"seats\": [0]}";
                }
                if(!seatsPerShow.isLast()){
                    result+=" , ";
                }else{
                    result+=" ] ";
                }
            }
            response.setContentType("application/json");
            out.print(result);
            out.flush();
        } catch (Exception e){
              out.println(e.toString());
        }
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
    
   try {
    
        JsonReader jsonReader=Json.createReader(request.getReader());
        JsonArray cinemaArray=jsonReader.readArray();
        jsonReader.close();
    
        int i=0;
        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(url+dbName,userName,password);  
        Statement updateStatement = conn.createStatement();
        Statement queryStatement = conn.createStatement();
        updateStatement.executeUpdate("TRUNCATE TABLE cinemas_movies");
        updateStatement.executeUpdate("TRUNCATE TABLE cinemovies_seats");
        updateStatement.executeUpdate("TRUNCATE TABLE reservations");
        ResultSet knownMovies = queryStatement.executeQuery("SELECT id, name FROM movies");
        Random random = new Random();
        int randomNumber; 

        while(i<cinemaArray.size()){

            randomNumber= random.nextInt(12);
            while(!knownMovies.isLast()&&randomNumber>=0){
                knownMovies.next();
                updateStatement.executeUpdate("INSERT INTO cinemas_movies VALUES (NULL,"+knownMovies.getInt("id")+",'"+cinemaArray.getString(i)+"')");
                randomNumber--;
            }
            knownMovies.beforeFirst();
            i++;
        }

        conn.close();
    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e){
        out.println(e.toString());
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

