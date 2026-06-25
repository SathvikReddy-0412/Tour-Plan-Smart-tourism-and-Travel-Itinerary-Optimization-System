package tourplan;

import java.util.*;

public class SearchEngine {

    // 1. Binary Search on destination database
    public static Destination binarySearch(List<Destination> destinations, String targetName) {
        // Sort destinations alphabetically by name
        List<Destination> sorted = new ArrayList<>(destinations);
        sorted.sort(Comparator.comparing(Destination::getName, String.CASE_INSENSITIVE_ORDER));

        System.out.println("Sorting database for Binary Search...");
        System.out.println("Searching for destination: \"" + targetName + "\"");
        System.out.println("Searching...");

        int low = 0;
        int high = sorted.size() - 1;
        int step = 1;
        Destination result = null;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            Destination midDest = sorted.get(mid);
            int cmp = midDest.getName().compareToIgnoreCase(targetName);

            System.out.printf("Step %d:\n", step);
            System.out.printf("  Range: [Index %d to %d], Mid: %d (\"%s\")\n", low, high, mid, midDest.getName());
            
            if (cmp == 0) {
                result = midDest;
                System.out.println("  -> Target found!");
                break;
            } else if (cmp < 0) {
                System.out.println("  -> Target is alphabetically after Mid. Adjusting low bound.");
                low = mid + 1;
            } else {
                System.out.println("  -> Target is alphabetically before Mid. Adjusting high bound.");
                high = mid - 1;
            }
            step++;
        }

        if (result != null) {
            System.out.println("\nFound Result");
            System.out.println(result);
        } else {
            System.out.println("\nResult Not Found");
        }
        
        System.out.println("\nAlgorithm Used  : Binary Search");
        System.out.println("Time Complexity : O(log N)");
        return result;
    }

    // 2. BFS on graph connections
    public static List<String> bfs(Graph graph, String startNode) {
        Map<String, List<Graph.Edge>> adjList = graph.getAdjList();
        List<String> visitedOrder = new ArrayList<>();
        if (!adjList.containsKey(startNode)) return visitedOrder;

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new LinkedHashSet<>();

        queue.add(startNode);
        visited.add(startNode);

        System.out.println("\n--- Breadth-First Search (BFS) Traversal Trace ---");
        System.out.printf("Start node: %s\n\n", startNode);

        int step = 1;
        while (!queue.isEmpty()) {
            System.out.printf("Step %d:\n", step);
            System.out.println("  Queue contents   : " + queue);
            System.out.println("  Visited set      : " + visited);
            
            String curr = queue.poll();
            visitedOrder.add(curr);
            System.out.printf("  Processing node  : \"%s\"\n", curr);

            List<Graph.Edge> neighbors = adjList.getOrDefault(curr, Collections.emptyList());
            for (Graph.Edge e : neighbors) {
                if (!visited.contains(e.getTargetName())) {
                    visited.add(e.getTargetName());
                    queue.add(e.getTargetName());
                }
            }
            step++;
            System.out.println();
        }

        System.out.println("BFS Traversal Complete.");
        System.out.println("Visited Order: " + visitedOrder);
        System.out.println("Algorithm Used  : BFS (Queue-based)");
        System.out.println("Time Complexity : O(V + E)");
        return visitedOrder;
    }

    // 3. DFS on graph connections
    public static List<String> dfs(Graph graph, String startNode) {
        Map<String, List<Graph.Edge>> adjList = graph.getAdjList();
        List<String> visitedOrder = new ArrayList<>();
        if (!adjList.containsKey(startNode)) return visitedOrder;

        Stack<String> stack = new Stack<>();
        Set<String> visited = new LinkedHashSet<>();

        stack.push(startNode);

        System.out.println("\n--- Depth-First Search (DFS) Traversal Trace ---");
        System.out.printf("Start node: %s\n\n", startNode);

        int step = 1;
        while (!stack.isEmpty()) {
            System.out.printf("Step %d:\n", step);
            System.out.println("  Stack contents   : " + stack);
            System.out.println("  Visited set      : " + visited);
            
            String curr = stack.pop();
            
            if (!visited.contains(curr)) {
                visited.add(curr);
                visitedOrder.add(curr);
                System.out.printf("  Processing node  : \"%s\"\n", curr);
                
                List<Graph.Edge> neighbors = adjList.getOrDefault(curr, Collections.emptyList());
                // Push neighbors in reverse to keep left-to-right ordering
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    String neighbor = neighbors.get(i).getTargetName();
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            } else {
                System.out.printf("  Node \"%s\" already visited. Skipping.\n", curr);
            }
            step++;
            System.out.println();
        }

        System.out.println("DFS Traversal Complete.");
        System.out.println("Visited Order: " + visitedOrder);
        System.out.println("Algorithm Used  : DFS (Stack-based)");
        System.out.println("Time Complexity : O(V + E)");
        return visitedOrder;
    }

    // 4. Greedy Itinerary Construction
    public static List<Destination> greedyOptimize(List<Destination> candidates, double budget, double availableHours) {
        System.out.println("\n--- Greedy Itinerary Optimization Trace ---");
        System.out.printf("Initial Budget: Rs. %.2f | Available Time: %.1f hrs\n\n", budget, availableHours);

        // Greedy strategy: Sort by rating (descending), break tie by entry cost (ascending)
        List<Destination> sorted = new ArrayList<>(candidates);
        sorted.sort((a, b) -> {
            int cmp = Double.compare(b.getRating(), a.getRating());
            if (cmp != 0) return cmp;
            return Double.compare(a.getEntryCost(), b.getEntryCost());
        });

        System.out.println("Sorting candidates by Rating (descending) & Cost (ascending) for Greedy selection:\n");
        for (Destination d : sorted) {
            System.out.printf("  %-25s | Rating: %.1f | Entry Cost: Rs. %-6.2f | Duration: %.1f hrs\n", 
                    d.getName(), d.getRating(), d.getEntryCost(), d.getVisitDuration());
        }
        System.out.println();

        List<Destination> selected = new ArrayList<>();
        double currentBudget = budget;
        double currentTimeLeft = availableHours;
        int step = 1;

        for (Destination dest : sorted) {
            System.out.printf("Step %d: Checking \"%s\"\n", step, dest.getName());
            if (dest.getEntryCost() <= currentBudget && dest.getVisitDuration() <= currentTimeLeft) {
                selected.add(dest);
                currentBudget -= dest.getEntryCost();
                currentTimeLeft -= dest.getVisitDuration();
                System.out.printf("  -> SELECTED. Cost: Rs. %.2f, Duration: %.1f hrs. (Budget remaining: Rs. %.2f, Time remaining: %.1f hrs)\n",
                        dest.getEntryCost(), dest.getVisitDuration(), currentBudget, currentTimeLeft);
            } else {
                System.out.print("  -> SKIPPED. Reason: ");
                if (dest.getEntryCost() > currentBudget) {
                    System.out.printf("Entry cost Rs. %.2f exceeds remaining budget Rs. %.2f\n", dest.getEntryCost(), currentBudget);
                } else {
                    System.out.printf("Visit duration %.1f hrs exceeds remaining time %.1f hrs\n", dest.getVisitDuration(), currentTimeLeft);
                }
            }
            step++;
        }

        System.out.println("\nGreedy Optimization Complete.");
        System.out.println("Selected " + selected.size() + " destinations.");
        System.out.println("Algorithm Used  : Greedy Selection Strategy");
        System.out.println("Time Complexity : O(N log N)");
        return selected;
    }
}
