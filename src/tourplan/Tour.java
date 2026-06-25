package tourplan;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private int tourId;
    private String tourName;
    private String date;
    private String route;
    private double distance;
    private double budget;
    private double duration;
    private double cost;
    private double rating;
    private List<String> itinerary;

    public Tour(int tourId, String tourName, String date, String route, double distance, double budget, double duration, double cost, double rating, List<String> itinerary) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.date = date;
        this.route = route;
        this.distance = distance;
        this.budget = budget;
        this.duration = duration;
        this.cost = cost;
        this.rating = rating;
        this.itinerary = itinerary != null ? itinerary : new ArrayList<>();
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<String> itinerary) {
        this.itinerary = itinerary;
    }
}
