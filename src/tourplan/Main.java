package tourplan;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("  Initializing TourPlan Database...       ");
        System.out.println("=========================================");

        // 1. Initialize database and seed if empty
        DatabaseManager.initializeDatabase();
        DatabaseManager.seedDestinationsIfEmpty();

        // 2. Load destinations catalog from database
        List<Destination> catalog = DatabaseManager.getAllDestinations();

        // 3. Build graph connectivity dynamically from database
        Graph graph = new Graph();
        for (Destination d : catalog) {
            graph.addDestination(d);
        }

        List<DatabaseManager.EdgeData> dbEdges = DatabaseManager.getAllConnections();
        for (DatabaseManager.EdgeData edge : dbEdges) {
            graph.addEdge(edge.source, edge.destination, edge.distance);
        }

        System.out.println("Database Initialized Successfully!");
        System.out.println("=========================================");

        // 3. Start Menu System
        MenuSystem menu = new MenuSystem(catalog, graph);
        if (args.length > 0 && (args[0].equalsIgnoreCase("demo") || args[0].equalsIgnoreCase("--demo"))) {
            System.out.println("Demo argument detected. Running non-interactive demonstration wizard...\n");
            menu.runSimulatedWizard();
            System.out.println("\nDemonstration completed. Exiting.");
            System.exit(0);
        }
        menu.run();
    }
}
