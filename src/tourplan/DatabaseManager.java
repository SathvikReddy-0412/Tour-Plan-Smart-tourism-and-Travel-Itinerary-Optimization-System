package tourplan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static String dbUrl = "jdbc:postgresql://localhost:5432/tourplan_db";
    private static String dbUser = "postgres";
    private static String dbPassword = "postgres";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found in classpath!");
        }

        // Load db.properties configuration
        java.io.File propFile = new java.io.File("db.properties");
        if (propFile.exists()) {
            java.util.Properties props = new java.util.Properties();
            try (java.io.FileInputStream fis = new java.io.FileInputStream(propFile)) {
                props.load(fis);
                dbUrl = props.getProperty("db.url", dbUrl);
                dbUser = props.getProperty("db.username", dbUser);
                dbPassword = props.getProperty("db.password", dbPassword);
            } catch (java.io.IOException e) {
                System.err.println("Warning: Error loading db.properties: " + e.getMessage());
            }
        } else {
            // Auto-create properties file with default properties if not exists
            try (java.io.FileWriter fw = new java.io.FileWriter(propFile)) {
                fw.write("db.url=" + dbUrl + "\n");
                fw.write("db.username=" + dbUser + "\n");
                fw.write("db.password=" + dbPassword + "\n");
            } catch (java.io.IOException e) {
                System.err.println("Warning: Could not create default db.properties file: " + e.getMessage());
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Create destinations table
            stmt.execute("CREATE TABLE IF NOT EXISTS destinations (" +
                         "name VARCHAR(100) PRIMARY KEY, " +
                         "category VARCHAR(100), " +
                         "rating DOUBLE PRECISION, " +
                         "entry_cost DOUBLE PRECISION, " +
                         "visit_duration DOUBLE PRECISION, " +
                         "x DOUBLE PRECISION, " +
                         "y DOUBLE PRECISION" +
                         ")");

            // Create connections table (graph edges)
            stmt.execute("CREATE TABLE IF NOT EXISTS connections (" +
                         "source VARCHAR(100), " +
                         "destination VARCHAR(100), " +
                         "distance DOUBLE PRECISION, " +
                         "PRIMARY KEY (source, destination), " +
                         "FOREIGN KEY (source) REFERENCES destinations (name) ON DELETE CASCADE, " +
                         "FOREIGN KEY (destination) REFERENCES destinations (name) ON DELETE CASCADE" +
                         ")");

            // Create tours table
            stmt.execute("CREATE TABLE IF NOT EXISTS tours (" +
                         "tour_id INT PRIMARY KEY, " +
                         "tour_name VARCHAR(255), " +
                         "date VARCHAR(100), " +
                         "route TEXT, " +
                         "distance DOUBLE PRECISION, " +
                         "budget DOUBLE PRECISION, " +
                         "duration DOUBLE PRECISION, " +
                         "cost DOUBLE PRECISION, " +
                         "rating DOUBLE PRECISION" +
                         ")");

            // Create tour_itinerary table
            stmt.execute("CREATE TABLE IF NOT EXISTS tour_itinerary (" +
                         "tour_id INT, " +
                         "step_index INT, " +
                         "instruction TEXT, " +
                         "PRIMARY KEY (tour_id, step_index), " +
                         "FOREIGN KEY (tour_id) REFERENCES tours (tour_id) ON DELETE CASCADE" +
                         ")");
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
        }
    }

    public static void seedDestinationsIfEmpty() {
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM destinations")) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return; 
                }
            }

            System.out.println("Seeding default destinations and connections into PostgreSQL database...");
            conn.setAutoCommit(false);

            String insertDest = "INSERT INTO destinations (name, category, rating, entry_cost, visit_duration, x, y) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertDest)) {
                addDestSeed(pstmt, "Agra", "Heritage", 4.8, 100.0, 3.0, 27.2, 78.0);
                addDestSeed(pstmt, "Alleppey", "Nature", 4.7, 0.0, 4.0, 9.5, 76.3);
                addDestSeed(pstmt, "Amritsar", "Spiritual", 4.9, 0.0, 2.0, 31.6, 74.9);
                addDestSeed(pstmt, "Bengaluru", "Nature", 4.5, 50.0, 2.5, 12.9, 77.6);
                addDestSeed(pstmt, "Chennai", "Coastal", 4.4, 20.0, 2.0, 13.0, 80.2);
                addDestSeed(pstmt, "Delhi", "Heritage", 4.7, 80.0, 4.0, 28.6, 77.2);
                addDestSeed(pstmt, "Goa", "Beach", 4.6, 0.0, 5.0, 15.3, 74.1);
                addDestSeed(pstmt, "Hyderabad", "Heritage", 4.6, 50.0, 3.0, 17.4, 78.4);
                addDestSeed(pstmt, "Jaipur", "Heritage", 4.7, 100.0, 3.5, 26.9, 75.8);
                addDestSeed(pstmt, "Kochi", "Coastal", 4.5, 50.0, 2.5, 9.9, 76.2);
                addDestSeed(pstmt, "Kolkata", "Heritage", 4.4, 30.0, 3.0, 22.5, 88.3);
                addDestSeed(pstmt, "Manali", "Adventure", 4.8, 0.0, 6.0, 32.2, 77.2);
                addDestSeed(pstmt, "Mumbai", "Coastal", 4.6, 50.0, 4.0, 19.0, 72.8);
                addDestSeed(pstmt, "Munnar", "Nature", 4.7, 20.0, 3.0, 10.0, 77.0);
                addDestSeed(pstmt, "Mysore", "Heritage", 4.6, 100.0, 2.5, 12.3, 76.6);
                addDestSeed(pstmt, "Ooty", "Nature", 4.6, 30.0, 3.5, 11.4, 76.7);
                addDestSeed(pstmt, "Pondicherry", "Coastal", 4.5, 0.0, 3.0, 11.9, 79.8);
                addDestSeed(pstmt, "Pune", "Heritage", 4.3, 40.0, 2.0, 18.5, 73.8);
                addDestSeed(pstmt, "Shimla", "Nature", 4.5, 0.0, 4.0, 31.1, 77.1);
                addDestSeed(pstmt, "Varanasi", "Spiritual", 4.8, 0.0, 3.0, 25.3, 83.0);
                pstmt.executeBatch();
            }

            String insertEdge = "INSERT INTO connections (source, destination, distance) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertEdge)) {
                addEdgeSeed(pstmt, "Delhi", "Agra", 230.0);
                addEdgeSeed(pstmt, "Delhi", "Jaipur", 270.0);
                addEdgeSeed(pstmt, "Delhi", "Amritsar", 450.0);
                addEdgeSeed(pstmt, "Delhi", "Shimla", 340.0);
                addEdgeSeed(pstmt, "Delhi", "Mumbai", 1400.0);
                addEdgeSeed(pstmt, "Shimla", "Manali", 250.0);
                addEdgeSeed(pstmt, "Jaipur", "Agra", 240.0);
                addEdgeSeed(pstmt, "Jaipur", "Varanasi", 850.0);
                addEdgeSeed(pstmt, "Agra", "Varanasi", 600.0);
                addEdgeSeed(pstmt, "Varanasi", "Kolkata", 680.0);
                addEdgeSeed(pstmt, "Kolkata", "Hyderabad", 1500.0);
                addEdgeSeed(pstmt, "Hyderabad", "Bengaluru", 570.0);
                addEdgeSeed(pstmt, "Hyderabad", "Chennai", 630.0);
                addEdgeSeed(pstmt, "Hyderabad", "Mumbai", 710.0);
                addEdgeSeed(pstmt, "Mumbai", "Pune", 150.0);
                addEdgeSeed(pstmt, "Mumbai", "Goa", 600.0);
                addEdgeSeed(pstmt, "Pune", "Goa", 450.0);
                addEdgeSeed(pstmt, "Goa", "Bengaluru", 560.0);
                addEdgeSeed(pstmt, "Bengaluru", "Chennai", 350.0);
                addEdgeSeed(pstmt, "Bengaluru", "Mysore", 140.0);
                addEdgeSeed(pstmt, "Bengaluru", "Kochi", 550.0);
                addEdgeSeed(pstmt, "Mysore", "Ooty", 125.0);
                addEdgeSeed(pstmt, "Ooty", "Munnar", 240.0);
                addEdgeSeed(pstmt, "Munnar", "Kochi", 120.0);
                addEdgeSeed(pstmt, "Kochi", "Alleppey", 60.0);
                addEdgeSeed(pstmt, "Chennai", "Pondicherry", 150.0);
                pstmt.executeBatch();
            }

            conn.commit();
            System.out.println("Seeding completed successfully.");
        } catch (SQLException e) {
            System.err.println("Error seeding database: " + e.getMessage());
        }
    }

    private static void addDestSeed(PreparedStatement pstmt, String name, String category, double rating, double entryCost, double duration, double x, double y) throws SQLException {
        pstmt.setString(1, name);
        pstmt.setString(2, category);
        pstmt.setDouble(3, rating);
        pstmt.setDouble(4, entryCost);
        pstmt.setDouble(5, duration);
        pstmt.setDouble(6, x);
        pstmt.setDouble(7, y);
        pstmt.addBatch();
    }

    private static void addEdgeSeed(PreparedStatement pstmt, String src, String dest, double dist) throws SQLException {
        pstmt.setString(1, src);
        pstmt.setString(2, dest);
        pstmt.setDouble(3, dist);
        pstmt.addBatch();
    }

    // --- Destination CRUD ---

    public static List<Destination> getAllDestinations() {
        List<Destination> list = new ArrayList<>();
        String sql = "SELECT * FROM destinations";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Destination(
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("rating"),
                    rs.getDouble("entry_cost"),
                    rs.getDouble("visit_duration"),
                    rs.getDouble("x"),
                    rs.getDouble("y")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving destinations: " + e.getMessage());
        }
        return list;
    }

    public static void addDestination(Destination dest) throws SQLException {
        String sql = "INSERT INTO destinations (name, category, rating, entry_cost, visit_duration, x, y) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, dest.getName());
                pstmt.setString(2, dest.getCategory());
                pstmt.setDouble(3, dest.getRating());
                pstmt.setDouble(4, dest.getEntryCost());
                pstmt.setDouble(5, dest.getVisitDuration());
                pstmt.setDouble(6, dest.getX());
                pstmt.setDouble(7, dest.getY());
                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static void updateDestination(Destination dest) throws SQLException {
        String sql = "UPDATE destinations SET category = ?, rating = ?, entry_cost = ?, visit_duration = ?, x = ?, y = ? WHERE name = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, dest.getCategory());
                pstmt.setDouble(2, dest.getRating());
                pstmt.setDouble(3, dest.getEntryCost());
                pstmt.setDouble(4, dest.getVisitDuration());
                pstmt.setDouble(5, dest.getX());
                pstmt.setDouble(6, dest.getY());
                pstmt.setString(7, dest.getName());
                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static void deleteDestination(String name) throws SQLException {
        String sql = "DELETE FROM destinations WHERE name = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // --- Connections (Edges) CRUD ---

    public static void addConnection(String src, String dest, double dist) throws SQLException {
        String sql = "INSERT INTO connections (source, destination, distance) VALUES (?, ?, ?)";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, src);
                pstmt.setString(2, dest);
                pstmt.setDouble(3, dist);
                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static List<EdgeData> getAllConnections() {
        List<EdgeData> list = new ArrayList<>();
        String sql = "SELECT * FROM connections";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new EdgeData(
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getDouble("distance")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving connections: " + e.getMessage());
        }
        return list;
    }

    public static class EdgeData {
        public String source;
        public String destination;
        public double distance;
        public EdgeData(String source, String destination, double distance) {
            this.source = source;
            this.destination = destination;
            this.distance = distance;
        }
    }

    // --- Tour CRUD ---

    public static List<Tour> getAllTours() {
        List<Tour> list = new ArrayList<>();
        String sql = "SELECT * FROM tours ORDER BY tour_id ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("tour_id");
                List<String> itinerary = getItineraryForTour(id);
                list.add(new Tour(
                    id,
                    rs.getString("tour_name"),
                    rs.getString("date"),
                    rs.getString("route"),
                    rs.getDouble("distance"),
                    rs.getDouble("budget"),
                    rs.getDouble("duration"),
                    rs.getDouble("cost"),
                    rs.getDouble("rating"),
                    itinerary
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving tours: " + e.getMessage());
        }
        return list;
    }

    public static Tour getTourById(int tourId) {
        String sql = "SELECT * FROM tours WHERE tour_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tourId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    List<String> itinerary = getItineraryForTour(tourId);
                    return new Tour(
                        tourId,
                        rs.getString("tour_name"),
                        rs.getString("date"),
                        rs.getString("route"),
                        rs.getDouble("distance"),
                        rs.getDouble("budget"),
                        rs.getDouble("duration"),
                        rs.getDouble("cost"),
                        rs.getDouble("rating"),
                        itinerary
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving tour ID " + tourId + ": " + e.getMessage());
        }
        return null;
    }

    private static List<String> getItineraryForTour(int tourId) {
        List<String> itinerary = new ArrayList<>();
        String sql = "SELECT instruction FROM tour_itinerary WHERE tour_id = ? ORDER BY step_index ASC";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tourId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    itinerary.add(rs.getString("instruction"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving itinerary: " + e.getMessage());
        }
        return itinerary;
    }

    public static void saveTour(Tour tour) throws SQLException {
        String sqlDel = "DELETE FROM tours WHERE tour_id = ?";
        String sqlTour = "INSERT INTO tours (tour_id, tour_name, date, route, distance, budget, duration, cost, rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlItin = "INSERT INTO tour_itinerary (tour_id, step_index, instruction) VALUES (?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement pDel = conn.prepareStatement(sqlDel)) {
                    pDel.setInt(1, tour.getTourId());
                    pDel.executeUpdate();
                }

                try (PreparedStatement pstmt = conn.prepareStatement(sqlTour)) {
                    pstmt.setInt(1, tour.getTourId());
                    pstmt.setString(2, tour.getTourName());
                    pstmt.setString(3, tour.getDate());
                    pstmt.setString(4, tour.getRoute());
                    pstmt.setDouble(5, tour.getDistance());
                    pstmt.setDouble(6, tour.getBudget());
                    pstmt.setDouble(7, tour.getDuration());
                    pstmt.setDouble(8, tour.getCost());
                    pstmt.setDouble(9, tour.getRating());
                    pstmt.executeUpdate();
                }

                try (PreparedStatement pstmt = conn.prepareStatement(sqlItin)) {
                    List<String> itin = tour.getItinerary();
                    for (int i = 0; i < itin.size(); i++) {
                        pstmt.setInt(1, tour.getTourId());
                        pstmt.setInt(2, i);
                        pstmt.setString(3, itin.get(i));
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static void updateTour(Tour tour) throws SQLException {
        String sql = "UPDATE tours SET tour_name = ?, rating = ?, budget = ? WHERE tour_id = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tour.getTourName());
                pstmt.setDouble(2, tour.getRating());
                pstmt.setDouble(3, tour.getBudget());
                pstmt.setInt(4, tour.getTourId());
                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static void deleteTour(int tourId) throws SQLException {
        String sql = "DELETE FROM tours WHERE tour_id = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, tourId);
                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
