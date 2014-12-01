/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marcel
 */
public class Reservation {
    private String name;
    private String email;
    private String cinema;
    private String movie;
    private int[] seats;
    public Reservation(){
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the cinema
     */
    public String getCinema() {
        return cinema;
    }

    /**
     * @param cinema the cinema to set
     */
    public void setCinema(String cinema) {
        this.cinema = cinema;
    }

    /**
     * @return the movie
     */
    public String getMovie() {
        return movie;
    }

    /**
     * @param movie the movie to set
     */
    public void setMovie(String movie) {
        this.movie = movie;
    }

    /**
     * @return the seats
     */
    public int[] getSeats() {
        return seats;
    }

    /**
     * @param seats the seats to set
     */
    public void setSeats(int[] seats) {
        this.seats = seats;
    }
}
