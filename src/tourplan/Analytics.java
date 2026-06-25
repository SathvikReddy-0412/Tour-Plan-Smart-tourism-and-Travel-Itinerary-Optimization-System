package tourplan;

import java.util.*;

public class Analytics {
    
    public static void displayDashboard(List<Destination> destinations, List<Tour> savedTours) {
        System.out.println("=================================================");
        System.out.println("            TOURISM ANALYTICS REPORT             ");
        System.out.println("=================================================");
        
        // 1. Most Visited Destinations
        Map<String, Integer> visitCounts = new HashMap<>();
        // Seed some base counts for demonstration based on entry cost and rating
        for (Destination d : destinations) {
            visitCounts.put(d.getName(), (int) (d.getRating() * 200 + (d.getEntryCost() > 0 ? 50 : 150)));
        }
        // Increment based on actual saved tours
        for (Tour tour : savedTours) {
            String[] parts = tour.getRoute().split(" -> ");
            for (String part : parts) {
                String name = part.trim();
                if (visitCounts.containsKey(name)) {
                    visitCounts.put(name, visitCounts.get(name) + 50);
                }
            }
        }
        
        List<Map.Entry<String, Integer>> sortedVisits = new ArrayList<>(visitCounts.entrySet());
        sortedVisits.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        System.out.println("\n[MOST VISITED DESTINATIONS]");
        System.out.println("+-------------------------------+------------------+");
        System.out.println("| Destination                   | Simulated Visits |");
        System.out.println("+-------------------------------+------------------+");
        for (int i = 0; i < Math.min(5, sortedVisits.size()); i++) {
            Map.Entry<String, Integer> entry = sortedVisits.get(i);
            System.out.printf("| %-29s | %-16d |\n", entry.getKey(), entry.getValue());
        }
        System.out.println("+-------------------------------+------------------+");

        // 2. Average stats (Trip distance, average budget, utilization)
        double totalDistance = 0;
        double totalBudget = 0;
        double totalSpent = 0;
        int tourCount = savedTours.size();
        
        for (Tour tour : savedTours) {
            totalDistance += tour.getDistance();
            totalBudget += tour.getBudget();
            totalSpent += tour.getCost();
        }
        
        double avgDistance = tourCount > 0 ? totalDistance / tourCount : 0;
        double avgBudget = tourCount > 0 ? totalBudget / tourCount : 0;
        double avgSpent = tourCount > 0 ? totalSpent / tourCount : 0;
        double budgetUsagePercent = avgBudget > 0 ? (avgSpent / avgBudget) * 100 : 0;
        
        System.out.println("\n[AVERAGE TOUR METRICS]");
        System.out.println("+---------------------------------+-----------------+");
        System.out.println("| Metric                          | Value           |");
        System.out.println("+---------------------------------+-----------------+");
        System.out.printf("| Average Trip Distance           | %-11.2f km   |\n", avgDistance);
        System.out.printf("| Average Budget Allocation       | Rs. %-11.2f |\n", avgBudget);
        System.out.printf("| Average Cost Spent              | Rs. %-11.2f |\n", avgSpent);
        System.out.printf("| Average Budget Utilization      | %-14.1f%% |\n", budgetUsagePercent);
        System.out.println("+---------------------------------+-----------------+");

        // 3. Category Popularity
        Map<String, Integer> categoryCounts = new HashMap<>();
        for (Destination d : destinations) {
            categoryCounts.put(d.getCategory(), categoryCounts.getOrDefault(d.getCategory(), 0) + 1);
        }
        
        System.out.println("\n[CATEGORY POPULARITY IN CATALOG]");
        System.out.println("+----------------------+----------------------------+");
        System.out.println("| Category             | Count of Destinations      |");
        System.out.println("+----------------------+----------------------------+");
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            System.out.printf("| %-20s | %-26d |\n", entry.getKey(), entry.getValue());
        }
        System.out.println("+----------------------+----------------------------+");

        // 4. Top Rated Destinations
        List<Destination> topRated = new ArrayList<>(destinations);
        topRated.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));
        
        System.out.println("\n[TOP RATED DESTINATIONS]");
        System.out.println("+-------------------------------+------------+--------------+");
        System.out.println("| Destination                   | Category   | Rating       |");
        System.out.println("+-------------------------------+------------+--------------+");
        for (int i = 0; i < Math.min(5, topRated.size()); i++) {
            Destination d = topRated.get(i);
            System.out.printf("| %-29s | %-10s | %-12.1f |\n", d.getName(), d.getCategory(), d.getRating());
        }
        System.out.println("+-------------------------------+------------+--------------+");
    }
}
