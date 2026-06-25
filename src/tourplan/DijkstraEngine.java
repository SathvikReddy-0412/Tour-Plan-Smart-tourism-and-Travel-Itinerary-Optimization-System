package tourplan;

import java.util.*;

public class DijkstraEngine {

    public static class DijkstraResult {
        public List<String> path;
        public double totalDistance;

        public DijkstraResult(List<String> path, double totalDistance) {
            this.path = path;
            this.totalDistance = totalDistance;
        }
    }

    public static DijkstraResult computeShortestPath(Graph graph, String startNode, String endNode, boolean printTrace) {
        Map<String, Destination> destinations = graph.getDestinations();
        Map<String, List<Graph.Edge>> adjList = graph.getAdjList();

        if (!destinations.containsKey(startNode) || !destinations.containsKey(endNode)) {
            return new DijkstraResult(Collections.emptyList(), Double.POSITIVE_INFINITY);
        }

        // Distance table mapping destination name to distance from start
        Map<String, Double> distTable = new HashMap<>();
        Map<String, String> parentTable = new HashMap<>();
        for (String node : destinations.keySet()) {
            distTable.put(node, Double.POSITIVE_INFINITY);
        }
        distTable.put(startNode, 0.0);

        // Custom model for storing queue items
        class PQNode implements Comparable<PQNode> {
            String name;
            double dist;
            PQNode(String name, double dist) {
                this.name = name;
                this.dist = dist;
            }
            @Override
            public int compareTo(PQNode other) {
                return Double.compare(this.dist, other.dist);
            }
            @Override
            public String toString() {
                return String.format("[%s, %.1f]", name, dist);
            }
        }

        PriorityQueue<PQNode> pq = new PriorityQueue<>();
        pq.add(new PQNode(startNode, 0.0));

        Set<String> visited = new LinkedHashSet<>();
        int iteration = 1;

        while (!pq.isEmpty()) {
            PQNode current = pq.poll();
            String u = current.name;

            if (visited.contains(u)) continue;
            visited.add(u);

            if (printTrace) {
                System.out.println("Iteration " + iteration);
                System.out.println("\nVisited:");
                System.out.print("[");
                int idx = 0;
                for (String vNode : visited) {
                    System.out.print(vNode + (idx < visited.size() - 1 ? ", " : ""));
                    idx++;
                }
                System.out.println("]");
                
                System.out.println("\nPriority Queue:");
                List<PQNode> pqList = new ArrayList<>(pq);
                Collections.sort(pqList);
                if (pqList.isEmpty()) {
                    System.out.println("  (Empty)");
                } else {
                    for (PQNode pqn : pqList) {
                        System.out.println("  " + pqn);
                    }
                }
                System.out.println("\nDistance Table:");
                List<String> sortedNames = new ArrayList<>(destinations.keySet());
                Collections.sort(sortedNames);
                for (String node : sortedNames) {
                    double d = distTable.get(node);
                    System.out.printf("  %-25s %s\n", node, (d == Double.POSITIVE_INFINITY ? "INF" : String.format("%.1f", d)));
                }
                System.out.println("-----------------------------------------\n");
            }

            if (u.equals(endNode)) break;

            for (Graph.Edge edge : adjList.getOrDefault(u, Collections.emptyList())) {
                String v = edge.getTargetName();
                double weight = edge.getDistance();
                if (!visited.contains(v)) {
                    double newDist = distTable.get(u) + weight;
                    if (newDist < distTable.get(v)) {
                        distTable.put(v, newDist);
                        parentTable.put(v, u);
                        pq.add(new PQNode(v, newDist));
                    }
                }
            }
            iteration++;
        }

        if (distTable.get(endNode) == Double.POSITIVE_INFINITY) {
            return new DijkstraResult(Collections.emptyList(), Double.POSITIVE_INFINITY);
        }

        // Reconstruct path back to front
        List<String> path = new ArrayList<>();
        String curr = endNode;
        while (curr != null) {
            path.add(0, curr);
            curr = parentTable.get(curr);
        }

        return new DijkstraResult(path, distTable.get(endNode));
    }
}
