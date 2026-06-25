package tourplan;

import java.util.*;

public class MenuSystem {
    public static class Accommodation {
        String name;
        double price;
        String description;
        Accommodation(String name, double price, String description) {
            this.name = name;
            this.price = price;
            this.description = description;
        }
    }

    private static final List<Accommodation> hotelCatalog = Arrays.asList(
        new Accommodation("Budget Inn", 1800.0, "Clean basic stay and shared services"),
        new Accommodation("Royal Residency", 3500.0, "Comfortable mid-range rooms with city view"),
        new Accommodation("Green Valley Resort", 4500.0, "Premium scenic cottages with pool access"),
        new Accommodation("Lake View Hotel", 6000.0, "Luxurious suites with lakefront scenery"),
        new Accommodation("Grand Palace Hotel", 8000.0, "Heritage royal experience with private gardens")
    );
    private static final Set<String> STARTING_CITIES = new HashSet<>(Arrays.asList(
        "Bengaluru", "Chennai", "Delhi", "Hyderabad", "Kolkata", "Mumbai", "Pune"
    ));

    private Scanner scanner;
    private List<Destination> catalog;
    private Graph graph;
    private String toursFile = "tours.json";

    public MenuSystem(List<Destination> catalog, Graph graph) {
        this.scanner = new Scanner(System.in);
        this.catalog = catalog;
        this.graph = graph;
    }
    public void run() {
        while (true) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            System.out.println();
            switch (choice) {
                case "1":
                    createCustomTourPlan();
                    break;
                case "2":
                    viewHistoricalTours();
                    break;
                case "3":
                    tourismAnalyticsMenu();
                    break;
                case "4":
                    manageDestinationsMenu();
                    break;
                case "5":
                    System.out.println("=========================================");
                    System.out.println("  Thank you for using TourPlan! Goodbye. ");
                    System.out.println("=========================================");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select 1-5.");
            }
            System.out.println("\nPress Enter to return to Main Menu...");
            scanner.nextLine();
        }
    }

    private void printMainMenu() {
        System.out.println("\n=========================================");
        System.out.println("         TOURPLAN SMART PLANNER          ");
        System.out.println("=========================================");
        System.out.println("1. Plan New Tour");
        System.out.println("2. View Saved Tours (History)");
        System.out.println("3. Tourism Analytics");
        System.out.println("4. Manage Destinations (CRUD)");
        System.out.println("5. Exit");
        System.out.println("=========================================");
        System.out.print("Select Option (1-5): ");
    }

    // A. User Preference Collection & Walkthrough Simulation
    // A. User Preference Collection & Walkthrough Simulation
    public void runSimulatedWizard() {
        System.out.println("=========================================");
        System.out.println("    SIMULATED TRAVEL WIZARD WALKTHROUGH  ");
        System.out.println("=========================================");
        
        System.out.println("\n[A. COLLECTING TRAVELER PREFERENCES (SIMULATED)]");
        int tourId = 101;
        String tourName = "Simulated Cross-Country Explorer";
        String startingLocation = "Delhi";
        
        System.out.println("Tour ID            : " + tourId);
        System.out.println("Tour Name          : " + tourName);
        System.out.println("Starting Location  : " + startingLocation);
        System.out.println("-----------------------------------------");

        // B & C. Destination Search using Binary Search
        System.out.println("\n[B & C. TARGET DESTINATION SEARCH]");
        String targetDest = "Agra";
        Destination searched = SearchEngine.binarySearch(catalog, targetDest);

        if (searched == null) {
            System.out.println("Target destination not found. Aborting simulation.");
            return;
        }

        // Extra visiting places
        System.out.println("\n[SELECT EXTRA VISITING PLACES]");
        System.out.println("Searching and adding extra stop: \"Jaipur\"");
        Destination extra1 = SearchEngine.binarySearch(catalog, "Jaipur");
        System.out.println("Searching and adding extra stop: \"Amritsar\"");
        Destination extra2 = SearchEngine.binarySearch(catalog, "Amritsar");
        
        List<Destination> extraStops = new ArrayList<>();
        if (extra1 != null) extraStops.add(extra1);
        if (extra2 != null) extraStops.add(extra2);

        // D. Tourism Graph Adjacency List display
        System.out.println("\n[D. TRAVEL CONNECTIONS NETWORK]");
        graph.printGraph();

        // Dijkstra optimization trace
        System.out.println("\n[ITINERARY ROUTE OPTIMIZATION ENGINE (DIJKSTRA)]");
        DijkstraEngine.DijkstraResult route = DijkstraEngine.computeShortestPath(graph, startingLocation, targetDest, true);

        if (route.path.isEmpty()) {
            System.out.println("No path found between nodes. Aborting simulation.");
            return;
        }

        System.out.println("Optimal Routing Path: " + String.join(" -> ", route.path));
        System.out.printf("Total Distance      : %.1f km\n", route.totalDistance);

        // Select available ways to reach destination
        System.out.println("\n[SELECT TRAVEL TRANSPORT OPTION]");
        System.out.printf("Available transportation options to reach target destination (Distance: %.1f km):\n", route.totalDistance);
        System.out.println("1. Bus   (Speed: 40 km/h, Cost: Rs. 5.00/km)");
        System.out.println("2. Train (Speed: 60 km/h, Cost: Rs. 3.00/km)");
        System.out.println("3. Flight (Speed: 500 km/h, Cost: Rs. 15.00/km)");
        System.out.println("Simulated Selection: Option 2 (Train)");
        
        double speed = 60.0;
        double costPerKm = 3.0;
        String transportMode = "Train";
        
        double travelTime = route.totalDistance / speed;
        double transportCost = route.totalDistance * costPerKm;
 
        // Display accommodation details
        System.out.println("\n[DISPLAY ACCOMMODATION RELATED DETAILS]");
        System.out.println("Available Accommodation Options:");
        for (int i = 0; i < hotelCatalog.size(); i++) {
            Accommodation h = hotelCatalog.get(i);
            System.out.printf("  %d. %-20s (Price: Rs. %.2f/night) - %s\n", (i+1), h.name, h.price, h.description);
        }
        System.out.println("Simulated Selection: Option 3 (Green Valley Resort)");
        Accommodation selectedHotel = hotelCatalog.get(2);
 
        // Generate timeline itinerary
        System.out.println("\n[GENERATED ITINERARY TIMELINE]");
        List<String> itinerary = new ArrayList<>();
        double currentHour = 8.0; // Start at 08:00 AM
        
        itinerary.add(String.format("%s - Depart from %s via %s (Travel distance: %.1f km, duration: %.1f hrs)", 
                formatTime(currentHour), startingLocation, transportMode, route.totalDistance, travelTime));
        
        Destination startD = graph.getDestinations().get(startingLocation);
        currentHour += startD.getVisitDuration();
        itinerary.add(String.format("%s - Visit completed at %s (Duration: %.1f hrs)", formatTime(currentHour), startingLocation, startD.getVisitDuration()));
        
        currentHour += travelTime;
        itinerary.add(String.format("%s - Arrive at %s", formatTime(currentHour), targetDest));
        
        double totalCost = transportCost + selectedHotel.price + startD.getEntryCost() + searched.getEntryCost();
        double sumRating = startD.getRating() + searched.getRating();
        
        for (Destination stop : extraStops) {
            itinerary.add(String.format("%s - Visit attraction: %s (Entry cost: Rs. %.2f, Duration: %.1f hrs)", 
                    formatTime(currentHour), stop.getName(), stop.getEntryCost(), stop.getVisitDuration()));
            currentHour += stop.getVisitDuration();
            totalCost += stop.getEntryCost();
            sumRating += stop.getRating();
            
            // Lunch break insert
            if (currentHour >= 12.0 && currentHour < 13.0) {
                itinerary.add(formatTime(currentHour) + " - Lunch Break (30 mins)");
                currentHour += 0.5;
            }
        }
        
        itinerary.add(String.format("%s - Check-in at selected hotel: %s (Price: Rs. %.2f/night)", 
                formatTime(currentHour), selectedHotel.name, selectedHotel.price));
        itinerary.add(String.format("%s - Reach final destination and check-in: %s", formatTime(currentHour), targetDest));
        
        for (String step : itinerary) {
            System.out.println("  " + step);
        }
        
        double avgRating = sumRating / (2 + extraStops.size());
        double totalDuration = currentHour - 8.0;
        double budget = 5000.0;
        
        // Optimization score formula
        double ratingScore = (avgRating / 5.0) * 40.0;
        double timeScore = Math.min(40.0, (totalDuration / 12.0) * 40.0);
        double budgetScore = totalCost <= budget ? (1.0 - (totalCost / budget)) * 20.0 : 0.0;
        double optimizationScore = ratingScore + timeScore + budgetScore;
 
        System.out.println("\n-----------------------------------------");
        System.out.println("SUMMARY STATS:");
        System.out.printf("  Total Distance    : %.1f km\n", route.totalDistance);
        System.out.printf("  Transport Mode    : %s\n", transportMode);
        System.out.printf("  Hotel Booked      : %s (Rs. %.2f/night)\n", selectedHotel.name, selectedHotel.price);
        System.out.printf("  Total Cost        : Rs. %.2f\n", totalCost);
        System.out.printf("  Total Duration    : %.1f hours\n", totalDuration);
        System.out.printf("  Average Rating    : %.1f/5.0\n", avgRating);
        System.out.printf("  Optimization Score: %.1f/100\n", optimizationScore);
        System.out.println("-----------------------------------------");
        // Save simulated tour plan
        Tour simTour = new Tour(
            tourId,
            tourName,
            new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()),
            String.join(" -> ", route.path),
            route.totalDistance,
            budget,
            totalDuration,
            totalCost,
            avgRating,
            itinerary
        );
        try {
            DatabaseManager.saveTour(simTour);
            System.out.println("\n[SAVE SYSTEM] Simulation tour saved to SQLite database!");
        } catch (Exception e) {
            System.err.println("Error saving simulation tour: " + e.getMessage());
        }
    }

    private void createCustomTourPlan() {
        System.out.println("=========================================");
        System.out.println("          CREATE CUSTOM TOUR PLAN        ");
        System.out.println("=========================================");

        // 1. Tour ID and Name inputs
        System.out.print("Enter Tour ID (Integer): ");
        int tourId = readInt();
        System.out.print("Enter Tour Name / Traveler Name: ");
        String tourName = scanner.nextLine().trim();

        // 2. Select starting location
        List<Destination> startLocations = new ArrayList<>();
        List<Destination> holidayDestinations = new ArrayList<>();
        for (Destination d : catalog) {
            if (STARTING_CITIES.contains(d.getName())) {
                startLocations.add(d);
            } else {
                holidayDestinations.add(d);
            }
        }
        startLocations.sort(Comparator.comparing(Destination::getName));
        holidayDestinations.sort(Comparator.comparing(Destination::getName));

        System.out.println("\n=========================================");
        System.out.println("        AVAILABLE TRAVEL LOCATIONS       ");
        System.out.println("=========================================");
        System.out.println("AVAILABLE STARTING LOCATIONS (MAJOR HUBS):");
        for (int i = 0; i < startLocations.size(); i += 3) {
            String col1 = "  * " + startLocations.get(i).getName();
            String col2 = (i + 1 < startLocations.size()) ? "  * " + startLocations.get(i + 1).getName() : "";
            String col3 = (i + 2 < startLocations.size()) ? "  * " + startLocations.get(i + 2).getName() : "";
            System.out.printf("%-25s%-25s%-25s\n", col1, col2, col3);
        }
        
        System.out.println("\nAVAILABLE HOLIDAY DESTINATIONS:");
        for (int i = 0; i < holidayDestinations.size(); i += 3) {
            String col1 = "  * " + holidayDestinations.get(i).getName();
            String col2 = (i + 1 < holidayDestinations.size()) ? "  * " + holidayDestinations.get(i + 1).getName() : "";
            String col3 = (i + 2 < holidayDestinations.size()) ? "  * " + holidayDestinations.get(i + 2).getName() : "";
            System.out.printf("%-25s%-25s%-25s\n", col1, col2, col3);
        }
        System.out.println("=========================================");

        System.out.print("\nEnter Starting Location: ");
        String startLoc = scanner.nextLine().trim();
        if (!STARTING_CITIES.contains(startLoc) || !graph.getDestinations().containsKey(startLoc)) {
            System.out.println("Error: Starting location not found in starting hubs. Returning to menu.");
            return;
        }

        // 3. Search target destination using Binary Search
        System.out.print("\nEnter Target Destination to Search: ");
        String targetName = scanner.nextLine().trim();
        Destination targetDest = SearchEngine.binarySearch(catalog, targetName);
        if (targetDest == null) {
            System.out.println("Error: Target destination not found. Returning to menu.");
            return;
        }
        if (STARTING_CITIES.contains(targetDest.getName())) {
            System.out.println("Error: Target destination must be a holiday destination, not a starting hub. Returning to menu.");
            return;
        }

        // Compute and display distance/route immediately
        System.out.println("\n[ROUTING] Calculating travel route from \"" + startLoc + "\" to \"" + targetDest.getName() + "\"...");
        DijkstraEngine.DijkstraResult routeRes = DijkstraEngine.computeShortestPath(graph, startLoc, targetDest.getName(), true);
        if (routeRes.path.isEmpty()) {
            System.out.println("Error: No route connects these two places in the network.");
            return;
        }
        System.out.println("\n=========================================");
        System.out.println("            ROUTE & DISTANCE INFO        ");
        System.out.println("=========================================");
        System.out.println("Optimal Routing Path Found: " + String.join(" -> ", routeRes.path));
        System.out.printf("Travel Distance: %.1f km\n", routeRes.totalDistance);
        System.out.println("=========================================");

        // 4. Input extra visiting places
        List<Destination> extraStops = new ArrayList<>();
        System.out.println("\nDo you want to add extra visiting attractions along the way?");
        while (true) {
            System.out.print("Enter extra destination name (or type 'done' to finish): ");
            String extraName = scanner.nextLine().trim();
            if (extraName.equalsIgnoreCase("done") || extraName.isEmpty()) {
                break;
            }
            Destination extraDest = SearchEngine.binarySearch(catalog, extraName);
            if (extraDest != null) {
                if (STARTING_CITIES.contains(extraDest.getName())) {
                    System.out.println("Error: Extra visiting places must be holiday destinations, not starting hubs.");
                } else {
                    extraStops.add(extraDest);
                    System.out.println("Added \"" + extraDest.getName() + "\" to your extra visits checklist.");
                }
            } else {
                System.out.println("Attraction not found in database. Please choose another.");
            }
        }

        // Recalculate route to include extra stops if there are any
        if (!extraStops.isEmpty()) {
            List<String> combinedPath = new ArrayList<>();
            double totalDist = 0;
            String current = startLoc;
            combinedPath.add(current);
            boolean possible = true;

            List<String> stopNames = new ArrayList<>();
            for (Destination d : extraStops) {
                stopNames.add(d.getName());
            }
            stopNames.add(targetDest.getName());

            for (String nextStop : stopNames) {
                DijkstraEngine.DijkstraResult segment = DijkstraEngine.computeShortestPath(graph, current, nextStop, false);
                if (segment.path.isEmpty() || segment.totalDistance == Double.POSITIVE_INFINITY) {
                    possible = false;
                    break;
                }
                for (int i = 1; i < segment.path.size(); i++) {
                    combinedPath.add(segment.path.get(i));
                }
                totalDist += segment.totalDistance;
                current = nextStop;
            }

            if (possible) {
                routeRes = new DijkstraEngine.DijkstraResult(combinedPath, totalDist);
                System.out.println("\n=========================================");
                System.out.println("     UPDATED ROUTE (INCLUDING EXTRA STOPS) ");
                System.out.println("=========================================");
                System.out.println("New Routing Path: " + String.join(" -> ", routeRes.path));
                System.out.printf("New Total Distance: %.1f km\n", routeRes.totalDistance);
                System.out.println("=========================================");
            } else {
                System.out.println("\n[Warning] No connected route could be found visiting the extra stops. Keeping original direct route.");
                extraStops.clear();
            }
        }

        // 6. Select travel mode
        System.out.println("\n=========================================");
        System.out.println("    SELECT TRANSPORTATION TO DESTINATION ");
        System.out.println("=========================================");
        System.out.println("1. Bus    (Speed: 40 km/h, Cost: Rs. 5.00/km)");
        System.out.println("2. Train  (Speed: 60 km/h, Cost: Rs. 3.00/km)");
        System.out.println("3. Flight (Speed: 500 km/h, Cost: Rs. 15.00/km)");
        System.out.println("=========================================");
        System.out.print("Select available way to reach destination (1-3): ");
        int tSelect = readInt();
        
        double speed = 40.0;
        double costPerKm = 5.0;
        String transportMode = "Bus";
        
        if (tSelect == 2) {
            speed = 60.0;
            costPerKm = 3.0;
            transportMode = "Train";
        } else if (tSelect == 3) {
            speed = 500.0;
            costPerKm = 15.0;
            transportMode = "Flight";
        }
        
        double travelTime = routeRes.totalDistance / speed;
        double transportCost = routeRes.totalDistance * costPerKm;

        // 7. Choose accommodation
        System.out.println("\n=========================================");
        System.out.println("         SELECT HOTEL ACCOMMODATION      ");
        System.out.println("=========================================");
        for (int i = 0; i < hotelCatalog.size(); i++) {
            Accommodation h = hotelCatalog.get(i);
            System.out.printf("  %d. %-20s (Price: Rs. %.2f/night) - %s\n", (i+1), h.name, h.price, h.description);
        }
        System.out.println("=========================================");
        System.out.print("Select your accommodation option (1-5): ");
        int hSelect = readInt();
        if (hSelect < 1 || hSelect > 5) {
            hSelect = 1; // Default
        }
        Accommodation selectedHotel = hotelCatalog.get(hSelect - 1);
        System.out.println("\nSelected Accommodation: " + selectedHotel.name + " (Rs. " + selectedHotel.price + "/night)");

        // 8. Financials collection
        System.out.print("\nEnter Total Tour Budget (Rs.): ");
        double budget = readDouble();
        System.out.print("Enter Available Time Limit (Hours): ");
        double timeLimit = readDouble();

        // 9. Generate timeline itinerary
        System.out.println("\n=========================================");
        System.out.println("          GENERATED ITINERARY TIMELINE   ");
        System.out.println("=========================================");
        
        List<String> itinerary = new ArrayList<>();
        double currentHour = 8.0; // Start at 08:00 AM
        double totalCost = transportCost + selectedHotel.price;
        double sumRating = 0.0;

        List<Destination> visitSequence = new ArrayList<>();
        Destination startD = graph.getDestinations().get(startLoc);
        visitSequence.add(startD);
        for (Destination d : extraStops) {
            visitSequence.add(d);
        }
        visitSequence.add(targetDest);

        for (Destination d : visitSequence) {
            totalCost += d.getEntryCost();
            sumRating += d.getRating();
        }

        for (int i = 0; i < visitSequence.size(); i++) {
            Destination currentStop = visitSequence.get(i);
            itinerary.add(String.format("%s - Visit attraction: %s (Entry cost: Rs. %.2f, Duration: %.1f hrs)", 
                    formatTime(currentHour), currentStop.getName(), currentStop.getEntryCost(), currentStop.getVisitDuration()));
            currentHour += currentStop.getVisitDuration();

            if (currentHour >= 12.0 && currentHour < 13.0) {
                itinerary.add(formatTime(currentHour) + " - Lunch Break (30 mins)");
                currentHour += 0.5;
            }

            if (i < visitSequence.size() - 1) {
                Destination nextStop = visitSequence.get(i + 1);
                DijkstraEngine.DijkstraResult seg = DijkstraEngine.computeShortestPath(graph, currentStop.getName(), nextStop.getName(), false);
                double segDist = (seg.totalDistance == Double.POSITIVE_INFINITY) ? 0 : seg.totalDistance;
                double segTime = segDist / speed;

                itinerary.add(String.format("%s - Depart from %s to %s via %s (Distance: %.1f km, Duration: %.1f hrs)", 
                        formatTime(currentHour), currentStop.getName(), nextStop.getName(), transportMode, segDist, segTime));
                currentHour += segTime;
            }
        }

        itinerary.add(String.format("%s - Check-in at selected hotel: %s (Price: Rs. %.2f/night)", 
                formatTime(currentHour), selectedHotel.name, selectedHotel.price));
        itinerary.add(String.format("%s - Reach final destination and check-in: %s", formatTime(currentHour), targetDest.getName()));
        
        for (String step : itinerary) {
            System.out.println("  " + step);
        }
        
        double avgRating = sumRating / visitSequence.size();
        double totalDuration = currentHour - 8.0;
        double remaining = budget - totalCost;

        // Optimization score formula
        double ratingScore = (avgRating / 5.0) * 40.0;
        double timeScore = Math.min(40.0, (totalDuration / timeLimit) * 40.0);
        double budgetScore = totalCost <= budget ? (1.0 - (totalCost / budget)) * 20.0 : 0.0;
        double optimizationScore = ratingScore + timeScore + budgetScore;

        System.out.println("=========================================");
        System.out.printf("  Total Distance    : %.1f km\n", routeRes.totalDistance);
        System.out.printf("  Transport Mode    : %s\n", transportMode);
        System.out.printf("  Hotel Booked      : %s (Rs. %.2f/night)\n", selectedHotel.name, selectedHotel.price);
        System.out.printf("  Total Cost        : Rs. %.2f\n", totalCost);
        System.out.printf("  Total Duration    : %.1f hours\n", totalDuration);
        System.out.printf("  Average Rating    : %.1f/5.0\n", avgRating);
        System.out.printf("  Optimization Score: %.1f/100\n", optimizationScore);
        System.out.println("=========================================");

        // Save System prompt
        System.out.print("\nWould you like to save this Tour Plan to SQLite database? (y/n): ");
        String save = scanner.nextLine().trim();
        if (save.equalsIgnoreCase("y") || save.equalsIgnoreCase("yes")) {
            String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date());
            
            Tour customTour = new Tour(
                tourId,
                tourName,
                today,
                String.join(" -> ", routeRes.path),
                routeRes.totalDistance,
                budget,
                totalDuration,
                totalCost,
                avgRating,
                itinerary
            );
            try {
                DatabaseManager.saveTour(customTour);
                System.out.println("Tour Plan successfully saved to SQLite database!");
            } catch (Exception e) {
                System.err.println("Error saving tour plan: " + e.getMessage());
            }
        } else {
            System.out.println("Itinerary not saved.");
        }
    }

    private void viewHistoricalTours() {
        System.out.println("=========================================");
        System.out.println("         HISTORICAL TOUR PLANS           ");
        System.out.println("=========================================");
        List<Tour> saved = DatabaseManager.getAllTours();
        if (saved.isEmpty()) {
            System.out.println("No saved itineraries found in database.");
            return;
        }

        System.out.println("+----+---------------------------+-------------+-------------+--------------+-------------+------------+");
        System.out.println("| ID | Tour Name                 | Distance    | Travel Mode | Cost         | Time Taken  | Date       |");
        System.out.println("+----+---------------------------+-------------+-------------+--------------+-------------+------------+");
        for (Tour t : saved) {
            String mode = "N/A";
            for (String step : t.getItinerary()) {
                if (step.contains("via Flight")) { mode = "Flight"; break; }
                if (step.contains("via Train")) { mode = "Train"; break; }
                if (step.contains("via Bus")) { mode = "Bus"; break; }
            }
            String distStr = String.format("%.1f km", t.getDistance());
            String costStr = String.format("Rs. %.2f", t.getCost());
            String durationStr = String.format("%.1f hrs", t.getDuration());
            
            System.out.printf("| %-2d | %-25s | %-11s | %-11s | %-12s | %-11s | %-10s |\n", 
                    t.getTourId(), t.getTourName(), distStr, mode, costStr, durationStr, t.getDate());
        }
        System.out.println("+----+---------------------------+-------------+-------------+--------------+-------------+------------+");

        System.out.print("\nEnter Tour ID to select (or 0 to return): ");
        int searchId = readInt();
        if (searchId == 0) return;
        
        Tour found = null;
        for (Tour t : saved) {
            if (t.getTourId() == searchId) {
                found = t;
                break;
            }
        }

        if (found != null) {
            while (true) {
                System.out.println("\n=========================================");
                System.out.println("   MANAGE TOUR ID: " + found.getTourId() + " (" + found.getTourName().toUpperCase() + ")");
                System.out.println("=========================================");
                System.out.println("1. View Detailed Itinerary");
                System.out.println("2. Rename Tour / Traveler Name (Update)");
                System.out.println("3. Update Tour Rating (Update)");
                System.out.println("4. Delete Tour Plan (Delete)");
                System.out.println("5. Back to Tour History List");
                System.out.println("=========================================");
                System.out.print("Select Option (1-5): ");
                String action = scanner.nextLine().trim();
                System.out.println();
                if (action.equals("1")) {
                    System.out.println("=========================================");
                    System.out.println("   DETAILED ITINERARY: " + found.getTourName().toUpperCase());
                    System.out.println("=========================================");
                    System.out.println("Date         : " + found.getDate());
                    System.out.println("Travel Route : " + found.getRoute());
                    System.out.printf("Distance     : %.1f km\n", found.getDistance());
                    System.out.printf("Total Cost   : Rs. %.2f / Budget: Rs. %.2f\n", found.getCost(), found.getBudget());
                    System.out.printf("Duration     : %.1f hours\n", found.getDuration());
                    System.out.printf("Rating       : %.1f/5.0\n", found.getRating());
                    System.out.println("Timeline Schedule:");
                    for (String step : found.getItinerary()) {
                        System.out.println("  " + step);
                    }
                    System.out.println("=========================================");
                } else if (action.equals("2")) {
                    System.out.print("Enter New Tour / Traveler Name: ");
                    String newName = scanner.nextLine().trim();
                    if (!newName.isEmpty()) {
                        found.setTourName(newName);
                        try {
                            DatabaseManager.updateTour(found);
                            System.out.println("Tour renamed successfully in database!");
                        } catch (Exception e) {
                            System.out.println("Error updating tour: " + e.getMessage());
                        }
                    }
                } else if (action.equals("3")) {
                    System.out.print("Enter New Tour Rating (0.0 to 5.0): ");
                    double newRating = readDouble();
                    if (newRating >= 0 && newRating <= 5) {
                        found.setRating(newRating);
                        try {
                            DatabaseManager.updateTour(found);
                            System.out.println("Tour rating updated successfully in database!");
                        } catch (Exception e) {
                            System.out.println("Error updating tour: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid rating. Must be between 0.0 and 5.0.");
                    }
                } else if (action.equals("4")) {
                    System.out.print("Are you sure you want to delete this tour plan? (y/n): ");
                    String confirm = scanner.nextLine().trim();
                    if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                        try {
                            DatabaseManager.deleteTour(found.getTourId());
                            System.out.println("Tour plan deleted from database successfully!");
                            return; 
                        } catch (Exception e) {
                            System.out.println("Error deleting tour: " + e.getMessage());
                        }
                    }
                } else if (action.equals("5")) {
                    break;
                } else {
                    System.out.println("Invalid choice. Select 1-5.");
                }
            }
        } else {
            System.out.println("Itinerary not found for ID: " + searchId);
        }
    }
 
    private void exploreAlgorithmsMenu() {
        while (true) {
            System.out.println("\n=================================================");
            System.out.println("        EXPLORE ALGORITHMS & DIAGNOSTICS         ");
            System.out.println("=================================================");
            System.out.println("1. Trees & Balanced Structures (BST, AVL)");
            System.out.println("2. Multiway Trees & Ranges (B-Tree, Segment, Fenwick)");
            System.out.println("3. Graph Connectivity & MST (BFS, DFS, Prim's MST)");
            System.out.println("4. Routing & Scheduling (Dijkstra, Bellman-Ford, Floyd, TopoSort)");
            System.out.println("5. Advanced Data Sorting (Merge, Quick, Heap, Radix)");
            System.out.println("6. Budgets & Activity Planners (Greedy, DP, Knapsack, LIS)");
            System.out.println("7. Return to Main Dashboard");
            System.out.println("=================================================");
            System.out.print("Select Option (1-7): ");
            String choice = scanner.nextLine().trim();
            System.out.println();
            switch (choice) {
                case "1":
                    DsaEngine.runModule1Diagnostics(catalog);
                    break;
                case "2":
                    DsaEngine.runModule2Diagnostics();
                    break;
                case "3":
                    System.out.println("[BFS & DFS Traversals]");
                    System.out.print("Enter starting node name (e.g. Delhi): ");
                    String startNode = scanner.nextLine().trim();
                    if (!graph.getDestinations().containsKey(startNode)) {
                        System.out.println("Error: City not found in network graph.");
                    } else {
                        SearchEngine.bfs(graph, startNode);
                        System.out.println();
                        SearchEngine.dfs(graph, startNode);
                    }
                    DsaEngine.computePrimMST(graph);
                    break;
                case "4":
                    System.out.println("[Dijkstra Shortest Path]");
                    System.out.print("Enter Start Location (e.g. Delhi): ");
                    String from = scanner.nextLine().trim();
                    System.out.print("Enter End Destination (e.g. Mumbai): ");
                    String to = scanner.nextLine().trim();
                    if (!graph.getDestinations().containsKey(from) || !graph.getDestinations().containsKey(to)) {
                        System.out.println("Error: Location(s) not found in network graph.");
                    } else {
                        DijkstraEngine.computeShortestPath(graph, from, to, true);
                    }
                    DsaEngine.computeBellmanFord(graph, "Delhi");
                    DsaEngine.computeFloydWarshall(graph);
                    DsaEngine.computeTopologicalSort();
                    break;
                case "5":
                    DsaEngine.runModule5Diagnostics();
                    break;
                case "6":
                    DsaEngine.runModule6Diagnostics();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid option. Please enter 1-7.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private void tourismAnalyticsMenu() {
        Analytics.displayDashboard(catalog, DatabaseManager.getAllTours());
    }

    private void manageDestinationsMenu() {
        while (true) {
            System.out.println("\n=========================================");
            System.out.println("       MANAGE DESTINATIONS CATALOG       ");
            System.out.println("=========================================");
            System.out.println("1. List All Destinations");
            System.out.println("2. Add New Destination (Create)");
            System.out.println("3. Update Destination (Update)");
            System.out.println("4. Delete Destination (Delete)");
            System.out.println("5. Back to Main Dashboard");
            System.out.println("=========================================");
            System.out.print("Select Option (1-5): ");
            String choice = scanner.nextLine().trim();
            System.out.println();
            switch (choice) {
                case "1":
                    listDestinations();
                    break;
                case "2":
                    addNewDestination();
                    break;
                case "3":
                    updateExistingDestination();
                    break;
                case "4":
                    deleteExistingDestination();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option. Please enter 1-5.");
            }
        }
    }

    private void listDestinations() {
        System.out.println("=== List of All Destinations in DB ===");
        List<Destination> list = DatabaseManager.getAllDestinations();
        if (list.isEmpty()) {
            System.out.println("No destinations in catalog.");
            return;
        }
        list.sort(Comparator.comparing(Destination::getName));
        for (Destination d : list) {
            System.out.printf(" - %-12s | Category: %-10s | Rating: %.1f | Entry Cost: Rs. %.2f | Visit Duration: %.1f hrs | Coord: (%.1f, %.1f)\n",
                d.getName(), d.getCategory(), d.getRating(), d.getEntryCost(), d.getVisitDuration(), d.getX(), d.getY());
        }
    }

    private void addNewDestination() {
        System.out.println("=== Add New Destination ===");
        System.out.print("Enter City/Destination Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) return;

        // Check if exists
        for (Destination d : catalog) {
            if (d.getName().equalsIgnoreCase(name)) {
                System.out.println("Destination \"" + name + "\" already exists in catalog!");
                return;
            }
        }

        System.out.print("Category (e.g. Nature, Heritage, Beach, Adventure): ");
        String category = scanner.nextLine().trim();
        System.out.print("Rating (0.0 to 5.0): ");
        double rating = readDouble();
        System.out.print("Entry Cost (Rs.): ");
        double entryCost = readDouble();
        System.out.print("Average Visit Duration (Hours): ");
        double duration = readDouble();
        System.out.print("X Coordinate (for map/distance): ");
        double x = readDouble();
        System.out.print("Y Coordinate (for map/distance): ");
        double y = readDouble();

        Destination newDest = new Destination(name, category, rating, entryCost, duration, x, y);

        try {
            DatabaseManager.addDestination(newDest);

            System.out.println("\nAdd Travel Connections (edges) for " + name + ":");
            while (true) {
                System.out.print("Enter connected city name (or type 'done' to finish): ");
                String otherCity = scanner.nextLine().trim();
                if (otherCity.equalsIgnoreCase("done") || otherCity.isEmpty()) {
                    break;
                }

                Destination target = null;
                for (Destination d : catalog) {
                    if (d.getName().equalsIgnoreCase(otherCity)) {
                        target = d;
                        break;
                    }
                }

                if (target == null) {
                    System.out.println("City \"" + otherCity + "\" not found in catalog. Please enter an existing city.");
                    continue;
                }

                System.out.print("Enter distance (km) to " + target.getName() + ": ");
                double dist = readDouble();
                if (dist <= 0) {
                    System.out.println("Distance must be positive.");
                    continue;
                }

                DatabaseManager.addConnection(name, target.getName(), dist);
                System.out.println("Connection saved: " + name + " <-> " + target.getName() + " (" + dist + " km)");
            }

            reloadCatalogAndGraph();
            System.out.println("\nDestination \"" + name + "\" successfully created and loaded into catalog and graph!");
        } catch (Exception e) {
            System.out.println("Error adding destination: " + e.getMessage());
        }
    }

    private void updateExistingDestination() {
        System.out.println("=== Update Destination Details ===");
        System.out.print("Enter Destination Name to edit: ");
        String name = scanner.nextLine().trim();
        Destination target = null;
        for (Destination d : catalog) {
            if (d.getName().equalsIgnoreCase(name)) {
                target = d;
                break;
            }
        }

        if (target == null) {
            System.out.println("Destination not found.");
            return;
        }

        System.out.println("Current Category: " + target.getCategory());
        System.out.print("Enter new category (or press Enter to keep current): ");
        String category = scanner.nextLine().trim();
        if (!category.isEmpty()) {
            target.setCategory(category);
        }

        System.out.println("Current Rating: " + target.getRating());
        System.out.print("Enter new rating (or press Enter to keep current): ");
        String ratingInput = scanner.nextLine().trim();
        if (!ratingInput.isEmpty()) {
            try {
                target.setRating(Double.parseDouble(ratingInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Keeping current.");
            }
        }

        System.out.println("Current Entry Cost: Rs. " + target.getEntryCost());
        System.out.print("Enter new entry cost (or press Enter to keep current): ");
        String costInput = scanner.nextLine().trim();
        if (!costInput.isEmpty()) {
            try {
                target.setEntryCost(Double.parseDouble(costInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Keeping current.");
            }
        }

        System.out.println("Current Visit Duration: " + target.getVisitDuration() + " hours");
        System.out.print("Enter new duration (or press Enter to keep current): ");
        String durationInput = scanner.nextLine().trim();
        if (!durationInput.isEmpty()) {
            try {
                target.setVisitDuration(Double.parseDouble(durationInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Keeping current.");
            }
        }

        try {
            DatabaseManager.updateDestination(target);
            System.out.println("Destination details updated in database!");
            reloadCatalogAndGraph();
        } catch (Exception e) {
            System.out.println("Error updating destination: " + e.getMessage());
        }
    }

    private void deleteExistingDestination() {
        System.out.println("=== Delete Destination ===");
        System.out.print("Enter Destination Name to delete: ");
        String name = scanner.nextLine().trim();
        Destination target = null;
        for (Destination d : catalog) {
            if (d.getName().equalsIgnoreCase(name)) {
                target = d;
                break;
            }
        }

        if (target == null) {
            System.out.println("Destination not found.");
            return;
        }

        System.out.print("Are you sure you want to delete \"" + target.getName() + "\"? This will also delete all associated connections. (y/n): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            try {
                DatabaseManager.deleteDestination(target.getName());
                reloadCatalogAndGraph();
                System.out.println("Destination and connections successfully deleted!");
            } catch (Exception e) {
                System.out.println("Error deleting destination: " + e.getMessage());
            }
        }
    }

    private void reloadCatalogAndGraph() {
        List<Destination> freshCatalog = DatabaseManager.getAllDestinations();
        this.catalog.clear();
        this.catalog.addAll(freshCatalog);

        this.graph.getDestinations().clear();
        this.graph.getAdjList().clear();
        for (Destination d : freshCatalog) {
            this.graph.addDestination(d);
        }
        List<DatabaseManager.EdgeData> dbEdges = DatabaseManager.getAllConnections();
        for (DatabaseManager.EdgeData edge : dbEdges) {
            this.graph.addEdge(edge.source, edge.destination, edge.distance);
        }
    }

    // Input Helpers
    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid integer. Please re-enter: ");
            }
        }
    }

    private double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid decimal number. Please re-enter: ");
            }
        }
    }

    private static String formatTime(double hourFraction) {
        int hours = (int) hourFraction;
        int minutes = (int) Math.round((hourFraction - hours) * 60);
        if (minutes == 60) {
            hours++;
            minutes = 0;
        }
        String period = "AM";
        int displayHour = hours % 24;
        if (displayHour >= 12) {
            period = "PM";
        }
        if (displayHour > 12) {
            displayHour -= 12;
        } else if (displayHour == 0) {
            displayHour = 12;
        }
        return String.format("%02d:%02d %s", displayHour, minutes, period);
    }
}
