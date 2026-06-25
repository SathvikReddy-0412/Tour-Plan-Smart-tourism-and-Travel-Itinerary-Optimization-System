package tourplan;

public class Destination {
    private String name;
    private String category;
    private double rating;
    private double entryCost;
    private double visitDuration; // In hours
    private double x;
    private double y;

    public Destination(String name, String category, double rating, double entryCost, double visitDuration, double x, double y) {
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.entryCost = entryCost;
        this.visitDuration = visitDuration;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getEntryCost() {
        return entryCost;
    }

    public void setEntryCost(double entryCost) {
        this.entryCost = entryCost;
    }

    public double getVisitDuration() {
        return visitDuration;
    }

    public void setVisitDuration(double visitDuration) {
        this.visitDuration = visitDuration;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double distanceTo(Destination other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    @Override
    public String toString() {
        return String.format("Destination[Name='%s', Category='%s', Rating=%.1f, Cost=Rs. %.2f, Duration=%.1f hrs, Coord=(%.1f, %.1f)]",
                name, category, rating, entryCost, visitDuration, x, y);
    }
}
