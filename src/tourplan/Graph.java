package tourplan;

import java.util.*;

public class Graph {
    public static class Edge {
        private String targetName;
        private double distance;

        public Edge(String targetName, double distance) {
            this.targetName = targetName;
            this.distance = distance;
        }

        public String getTargetName() {
            return targetName;
        }

        public double getDistance() {
            return distance;
        }
    }

    private Map<String, Destination> destinations;
    private Map<String, List<Edge>> adjList;

    public Graph() {
        this.destinations = new HashMap<>();
        this.adjList = new HashMap<>();
    }

    public void addDestination(Destination dest) {
        destinations.put(dest.getName(), dest);
        adjList.putIfAbsent(dest.getName(), new ArrayList<>());
    }

    public void addEdge(String src, String dest, double distance) {
        if (!destinations.containsKey(src) || !destinations.containsKey(dest)) {
            return;
        }
        adjList.get(src).add(new Edge(dest, distance));
        adjList.get(dest).add(new Edge(src, distance)); // Undirected travel network
    }

    public Map<String, Destination> getDestinations() {
        return destinations;
    }

    public Map<String, List<Edge>> getAdjList() {
        return adjList;
    }

    public void printGraph() {
        System.out.println("\n--- Destination Graph (Adjacency List) ---");
        for (String src : adjList.keySet()) {
            System.out.print("  " + src);
            List<Edge> edges = adjList.get(src);
            if (edges.isEmpty()) {
                System.out.println(" (No connections)");
            } else {
                for (Edge e : edges) {
                    System.out.printf("\n    -> %s (%.1f km)", e.getTargetName(), e.getDistance());
                }
                System.out.println();
            }
        }
    }
}
