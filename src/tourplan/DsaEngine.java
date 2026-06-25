package tourplan;

import java.util.*;

public class DsaEngine {

    // =========================================================================
    // MODULE 1 (M1): Trees & Balanced Search Structures
    // =========================================================================
    
    // 1. Binary Search Tree (BST) Index
    public static class BST {
        public static class Node {
            String key;
            Destination val;
            Node left, right;
            Node(String key, Destination val) {
                this.key = key;
                this.val = val;
            }
        }
        private Node root;

        public void insert(String key, Destination val) {
            root = insert(root, key, val);
        }

        private Node insert(Node n, String key, Destination val) {
            if (n == null) return new Node(key, val);
            int cmp = key.compareToIgnoreCase(n.key);
            if (cmp < 0) n.left = insert(n.left, key, val);
            else if (cmp > 0) n.right = insert(n.right, key, val);
            return n;
        }

        public Destination search(String key) {
            return search(root, key);
        }

        private Destination search(Node n, String key) {
            if (n == null) return null;
            int cmp = key.compareToIgnoreCase(n.key);
            System.out.printf("  BST Lookup: Comparing \"%s\" with index node \"%s\"\n", key, n.key);
            if (cmp < 0) return search(n.left, key);
            if (cmp > 0) return search(n.right, key);
            return n.val;
        }

        public List<String> getInOrder() {
            List<String> acc = new ArrayList<>();
            inOrder(acc, root);
            return acc;
        }

        private void inOrder(List<String> acc, Node n) {
            if (n == null) return;
            inOrder(acc, n.left);
            acc.add(n.key);
            inOrder(acc, n.right);
        }
    }

    // 2. AVL Balanced Booking Registry
    public static class AVL {
        public static class Node {
            int bookingId;
            String customerName;
            int height;
            Node left, right;
            Node(int bookingId, String customerName) {
                this.bookingId = bookingId;
                this.customerName = customerName;
                this.height = 1;
            }
        }
        private Node root;

        public int height(Node n) { return n == null ? 0 : n.height; }
        private int getBalance(Node n) { return n == null ? 0 : height(n.left) - height(n.right); }

        private Node rightRotate(Node y) {
            Node x = y.left;
            Node T2 = x.right;
            x.right = y;
            y.left = T2;
            y.height = Math.max(height(y.left), height(y.right)) + 1;
            x.height = Math.max(height(x.left), height(x.right)) + 1;
            System.out.println("  [AVL Rotate] LL Balance condition met: Right rotating node " + y.bookingId);
            return x;
        }

        private Node leftRotate(Node x) {
            Node y = x.right;
            Node T2 = y.left;
            y.left = x;
            x.right = T2;
            x.height = Math.max(height(x.left), height(x.right)) + 1;
            y.height = Math.max(height(y.left), height(y.right)) + 1;
            System.out.println("  [AVL Rotate] RR Balance condition met: Left rotating node " + x.bookingId);
            return y;
        }

        public void insert(int id, String name) {
            root = insert(root, id, name);
        }

        private Node insert(Node node, int id, String name) {
            if (node == null) {
                System.out.printf("  Inserted Booking Registry Node: ID %d (%s)\n", id, name);
                return new Node(id, name);
            }
            if (id < node.bookingId) node.left = insert(node.left, id, name);
            else if (id > node.bookingId) node.right = insert(node.right, id, name);
            else return node;

            node.height = Math.max(height(node.left), height(node.right)) + 1;
            int balance = getBalance(node);

            if (balance > 1 && id < node.left.bookingId) return rightRotate(node);
            if (balance < -1 && id > node.right.bookingId) return leftRotate(node);
            if (balance > 1 && id > node.left.bookingId) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            if (balance < -1 && id < node.right.bookingId) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
            return node;
        }

        public void printPreOrder() {
            preOrder(root);
        }

        private void preOrder(Node n) {
            if (n == null) return;
            System.out.printf("  - ID: %03d | Customer: %-15s | Height: %d | Balance: %d\n", 
                    n.bookingId, n.customerName, n.height, getBalance(n));
            preOrder(n.left);
            preOrder(n.right);
        }
    }

    public static void runModule1Diagnostics(List<Destination> catalog) {
        System.out.println("\n=========================================");
        System.out.println("   MODULE 1: TREES & BALANCED STRUCTURES ");
        System.out.println("=========================================");
        
        System.out.println("\n[1. BST Index Seeding]");
        BST bst = new BST();
        for (Destination d : catalog) {
            bst.insert(d.getName(), d);
        }
        System.out.println("Alphabetical BST In-order listing of indexed cities:");
        System.out.println("  " + bst.getInOrder());
        
        System.out.println("\n[2. BST Destination Search]");
        String target = "Mumbai";
        Destination found = bst.search(target);
        System.out.println("Search Result: " + (found != null ? "Found " + found.toString() : "Not Found"));

        System.out.println("\n[3. AVL Self-Balancing Booking Registry]");
        AVL avl = new AVL();
        avl.insert(301, "Alice");
        avl.insert(201, "Bob");
        avl.insert(101, "Charlie"); // Triggers right rotation LL
        avl.insert(401, "David");
        avl.insert(501, "Eva");     // Triggers left rotation RR
        
        System.out.println("\nAVL balanced Booking registry preorder trace:");
        avl.printPreOrder();
    }

    // =========================================================================
    // MODULE 2 (M2): Multiway Trees & Range Query Structures
    // =========================================================================

    public static class SegmentTree {
        private int[] tree;
        private int n;
        public SegmentTree(int[] arr) {
            n = arr.length;
            tree = new int[4 * n];
            build(arr, 0, 0, n - 1);
        }
        private void build(int[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
                return;
            }
            int mid = (start + end) / 2;
            build(arr, 2 * node + 1, start, mid);
            build(arr, 2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
        public int query(int l, int r) {
            return query(0, 0, n - 1, l, r);
        }
        private int query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return 0;
            if (l <= start && end <= r) {
                System.out.printf("  Segment Tree node %d covers [%d, %d]: Sum = %d (Fully contained)\n", node, start, end, tree[node]);
                return tree[node];
            }
            int mid = (start + end) / 2;
            int p1 = query(2 * node + 1, start, mid, l, r);
            int p2 = query(2 * node + 2, mid + 1, end, l, r);
            return p1 + p2;
        }
    }

    public static class FenwickTree {
        private int[] tree;
        public FenwickTree(int size) {
            tree = new int[size + 1];
        }
        public void update(int idx, int val) {
            System.out.printf("  Fenwick update: Index %d, Delta %+d\n", idx, val);
            for (; idx < tree.length; idx += idx & -idx) {
                tree[idx] += val;
            }
        }
        public int query(int idx) {
            int sum = 0;
            for (; idx > 0; idx -= idx & -idx) {
                sum += tree[idx];
            }
            return sum;
        }
    }

    public static void runModule2Diagnostics() {
        System.out.println("\n=========================================");
        System.out.println("  MODULE 2: MULTIWAY TREES & RANGE QUERY ");
        System.out.println("=========================================");

        System.out.println("\n[1. B-Tree Node Insertion Split Simulation (Order 3)]");
        System.out.println("Inserting keys into B-Tree index node. Max key capacity = 2.");
        System.out.println("Step 1: Insert [10] -> Node content: [10]");
        System.out.println("Step 2: Insert [20] -> Node content: [10, 20]");
        System.out.println("Step 3: Insert [15] -> Overflow Node [10, 15, 20]!");
        System.out.println("  -> Splitting node: Push median key [15] to parent root.");
        System.out.println("  -> New structure: Root node [15] | Children: [10] and [20]");

        System.out.println("\n[2. B+ Tree Sibling Links Range Price Query Simulation]");
        System.out.println("Search Hotel range: Rs. 3500 to Rs. 6000");
        System.out.println("Step 1: Traverse indexes to locate leaf price node (>= Rs. 3500).");
        System.out.println("Step 2: Access leaf block sibling chains sequentially:");
        System.out.println("  * Leaf node block 1: [3500] (Match! -> Royal Residency: Rs. 3500.00)");
        System.out.println("  * Sibling link -> Block 2: [4500] (Match! -> Green Valley Resort), [6000] (Match! -> Lake View Hotel)");
        System.out.println("  * Sibling link -> Block 3: [8000] (Out of range. Query stops.)");

        System.out.println("\n[3. Segment Tree Seasonal Visits range-sum query]");
        int[] seasonalVisits = {120, 150, 90, 80, 200, 250, 110, 95}; // 8 seasons
        System.out.println("Seasons visits data: " + Arrays.toString(seasonalVisits));
        SegmentTree segTree = new SegmentTree(seasonalVisits);
        System.out.println("Querying total visits from Season indices 1 to 5:");
        int total = segTree.query(1, 5);
        System.out.printf("  Resulting range-sum: %d visits\n", total);

        System.out.println("\n[4. Fenwick Tree cumulative statistics]");
        FenwickTree ft = new FenwickTree(8);
        ft.update(1, 120);
        ft.update(2, 150);
        ft.update(3, 90);
        System.out.printf("Cumulative prefix sum of tourist counts for first 3 seasons: %d\n", ft.query(3));
    }

    // =========================================================================
    // MODULE 3 (M3): Graph Algorithms for Tourism Networks
    // =========================================================================

    static class MstEdge {
        String u, v;
        double w;
        MstEdge(String u, String v, double w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    public static void computePrimMST(Graph graph) {
        System.out.println("\n=========================================");
        System.out.println("      MODULE 3: PRIM'S MST CIRCUIT       ");
        System.out.println("=========================================");
        
        Map<String, List<Graph.Edge>> adjList = graph.getAdjList();
        if (adjList.isEmpty()) {
            System.out.println("No graph vertices found.");
            return;
        }

        String start = "Delhi";
        if (!adjList.containsKey(start)) {
            start = adjList.keySet().iterator().next();
        }

        Set<String> visited = new HashSet<>();
        PriorityQueue<MstEdge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.w));
        
        visited.add(start);
        for (Graph.Edge e : adjList.getOrDefault(start, Collections.emptyList())) {
            pq.add(new MstEdge(start, e.getTargetName(), e.getDistance()));
        }

        double totalDistance = 0;
        List<MstEdge> mstList = new ArrayList<>();

        System.out.printf("Constructing Prim's Spanning Circuit from starting node: %s\n\n", start);
        while (!pq.isEmpty() && visited.size() < adjList.size()) {
            MstEdge next = pq.poll();
            if (visited.contains(next.v)) continue;

            visited.add(next.v);
            mstList.add(next);
            totalDistance += next.w;
            System.out.printf("  Selected: %s <-> %s (Weight: %.1f km)\n", next.u, next.v, next.w);

            for (Graph.Edge e : adjList.getOrDefault(next.v, Collections.emptyList())) {
                if (!visited.contains(e.getTargetName())) {
                    pq.add(new MstEdge(next.v, e.getTargetName(), e.getDistance()));
                }
            }
        }

        System.out.println("\nMST Construction Complete.");
        System.out.printf("Total MST Route Spanning Length: %.1f km\n", totalDistance);
        System.out.println("Algorithm Used  : Prim's algorithm");
        System.out.println("Time Complexity : O(E log V)");
    }

    // =========================================================================
    // MODULE 4 (M4): Shortest Path Optimization
    // =========================================================================

    public static void computeBellmanFord(Graph graph, String src) {
        System.out.println("\n=========================================");
        System.out.println("        MODULE 4: BELLMAN-FORD PATH      ");
        System.out.println("=========================================");
        System.out.printf("Source node: %s\n", src);

        Map<String, Double> dist = new HashMap<>();
        for (String node : graph.getAdjList().keySet()) {
            dist.put(node, Double.MAX_VALUE);
        }
        dist.put(src, 0.0);

        int n = graph.getAdjList().size();
        for (int i = 1; i <= n - 1; i++) {
            boolean updated = false;
            for (String u : graph.getAdjList().keySet()) {
                for (Graph.Edge e : graph.getAdjList().get(u)) {
                    String v = e.getTargetName();
                    double w = e.getDistance();
                    
                    // Dynamic travel discount: Delhi -> Agra has Rs. 50 cost discount
                    if (u.equals("Delhi") && v.equals("Agra")) {
                        w = -50.0;
                    }

                    if (dist.get(u) != Double.MAX_VALUE && dist.get(u) + w < dist.get(v)) {
                        dist.put(v, dist.get(u) + w);
                        updated = true;
                    }
                }
            }
            if (!updated) break;
        }

        // Check for negative cycle
        boolean cycle = false;
        for (String u : graph.getAdjList().keySet()) {
            for (Graph.Edge e : graph.getAdjList().get(u)) {
                String v = e.getTargetName();
                double w = e.getDistance();
                if (u.equals("Delhi") && v.equals("Agra")) w = -50.0;

                if (dist.get(u) != Double.MAX_VALUE && dist.get(u) + w < dist.get(v)) {
                    cycle = true;
                }
            }
        }

        System.out.println("\nBellman-Ford Shortest Distances (with Delhi->Agra negative discount edge):");
        for (String key : dist.keySet()) {
            double d = dist.get(key);
            System.out.printf("  To %-15s: %s\n", key, d == Double.MAX_VALUE ? "INF" : String.format("%.1f km", d));
        }

        if (cycle) {
            System.out.println("\n  [WARNING] Negative weight cycle detected!");
        } else {
            System.out.println("\nNo negative cycles detected. Relaxation stabilized.");
        }
    }

    public static void computeFloydWarshall(Graph graph) {
        System.out.println("\n=========================================");
        System.out.println("      MODULE 4: FLOYD-WARSHALL MATRIX    ");
        System.out.println("=========================================");
        
        List<String> nodes = new ArrayList<>(graph.getAdjList().keySet());
        Collections.sort(nodes);
        List<String> sub = nodes.subList(0, Math.min(6, nodes.size()));
        int n = sub.size();

        double[][] d = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(d[i], Double.MAX_VALUE);
            d[i][i] = 0;
        }

        for (int i = 0; i < n; i++) {
            String u = sub.get(i);
            for (Graph.Edge e : graph.getAdjList().get(u)) {
                String v = e.getTargetName();
                int idx = sub.indexOf(v);
                if (idx != -1) {
                    d[i][idx] = e.getDistance();
                }
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (d[i][k] != Double.MAX_VALUE && d[k][j] != Double.MAX_VALUE) {
                        d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
                    }
                }
            }
        }

        // Print Matrix
        System.out.printf("%-15s", "");
        for (String name : sub) {
            System.out.printf("%-12s", name);
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.printf("%-15s", sub.get(i));
            for (int j = 0; j < n; j++) {
                double val = d[i][j];
                System.out.printf("%-12s", val == Double.MAX_VALUE ? "INF" : String.format("%.1f", val));
            }
            System.out.println();
        }
    }

    public static void computeTopologicalSort() {
        System.out.println("\n=========================================");
        System.out.println("      MODULE 4: TOPOLOGICAL SCHEDULING   ");
        System.out.println("=========================================");
        System.out.println("DAG of travel booking dependencies:");
        System.out.println("  Book Flight -> Pack Suitcase -> Reach Terminal -> Board flight");
        System.out.println("  Hotel Reservation -> Pack Suitcase");

        Map<String, List<String>> adj = new HashMap<>();
        adj.put("Check Guidelines", Arrays.asList("Book Flight"));
        adj.put("Book Flight", Arrays.asList("Pack Suitcase"));
        adj.put("Hotel Reservation", Arrays.asList("Pack Suitcase"));
        adj.put("Pack Suitcase", Arrays.asList("Reach Terminal"));
        adj.put("Reach Terminal", Arrays.asList("Board Flight"));
        adj.put("Board Flight", Collections.emptyList());

        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        for (String node : adj.keySet()) {
            if (!visited.contains(node)) {
                topoDFS(node, adj, visited, stack);
            }
        }

        System.out.println("\nLinear Travel Dependencies Schedule:");
        int idx = 1;
        while (!stack.isEmpty()) {
            System.out.printf("  Step %d: %s\n", idx++, stack.pop());
        }
    }

    private static void topoDFS(String node, Map<String, List<String>> adj, Set<String> visited, Stack<String> stack) {
        visited.add(node);
        for (String neighbor : adj.getOrDefault(node, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                topoDFS(neighbor, adj, visited, stack);
            }
        }
        stack.push(node);
    }

    // =========================================================================
    // MODULE 5 (M5): Advanced Sorting & Data Ranking
    // =========================================================================

    public static void runModule5Diagnostics() {
        System.out.println("\n=========================================");
        System.out.println("      MODULE 5: ADVANCED SORTING         ");
        System.out.println("=========================================");

        System.out.println("\n[1. Merge Sort (Sorting Travel Package Prices)]");
        double[] prices = {12500, 3500, 8000, 1800, 6000, 4500};
        System.out.println("Unsorted Prices: " + Arrays.toString(prices));
        mergeSort(prices, 0, prices.length - 1);
        System.out.println("Sorted Prices  : " + Arrays.toString(prices));

        System.out.println("\n[2. Quick Sort (Ranking Holiday Popularity Ratings)]");
        double[] ratings = {4.4, 4.8, 4.3, 4.9, 4.5, 4.7};
        System.out.println("Unsorted Ratings: " + Arrays.toString(ratings));
        quickSort(ratings, 0, ratings.length - 1);
        System.out.println("Sorted Ratings  : " + Arrays.toString(ratings));

        System.out.println("\n[3. Heap Sort (Finding Top attractions ranking)]");
        double[] costs = {100, 0, 50, 80, 120, 20};
        System.out.println("Unsorted Costs: " + Arrays.toString(costs));
        heapSort(costs);
        System.out.println("Sorted Costs  : " + Arrays.toString(costs));

        System.out.println("\n[4. Radix Sort (Sorting Booking Registry IDs)]");
        int[] ids = {205, 911, 432, 105, 78, 600, 89};
        System.out.println("Unsorted IDs: " + Arrays.toString(ids));
        radixSort(ids);
        System.out.println("Sorted IDs  : " + Arrays.toString(ids));
    }

    private static void mergeSort(double[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    private static void merge(double[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        double[] L = new double[n1];
        double[] R = new double[n2];
        System.arraycopy(arr, l, L, 0, n1);
        System.arraycopy(arr, m + 1, R, 0, n2);
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else arr[k++] = R[j++];
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    private static void quickSort(double[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(double[] arr, int low, int high) {
        double pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] >= pivot) { // Descending sort
                i++;
                double temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
           }
       }
       double temp = arr[i + 1];
       arr[i + 1] = arr[high];
       arr[high] = temp;
       return i + 1;
    }

    private static void heapSort(double[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
        for (int i = n - 1; i > 0; i--) {
            double temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
        }
    }

    private static void heapify(double[] arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        if (l < n && arr[l] > arr[largest]) largest = l;
        if (r < n && arr[r] > arr[largest]) largest = r;
        if (largest != i) {
            double swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest);
        }
    }

    private static void radixSort(int[] arr) {
        int max = arr[0];
        for (int val : arr) if (val > max) max = val;
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortForRadix(arr, exp);
        }
    }

    private static void countingSortForRadix(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];
        for (int x : arr) count[(x / exp) % 10]++;
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];
        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }
        System.arraycopy(output, 0, arr, 0, n);
    }

    // =========================================================================
    // MODULE 6 (M6): Greedy & Dynamic Programming
    // =========================================================================

    public static class Activity {
        String name;
        int start, end;
        Activity(String name, int start, int end) {
            this.name = name;
            this.start = start;
            this.end = end;
        }
    }

    public static class Item {
        String name;
        double cost, value;
        Item(String name, double cost, double value) {
            this.name = name;
            this.cost = cost;
            this.value = value;
        }
    }

    public static void runModule6Diagnostics() {
        System.out.println("\n=========================================");
        System.out.println("      MODULE 6: GREEDY & DP PLANNERS     ");
        System.out.println("=========================================");

        System.out.println("\n[1. Activity Selection Daily Attraction Scheduling]");
        List<Activity> acts = Arrays.asList(
            new Activity("Botanical Garden Tour", 10, 12),
            new Activity("Spiritual Temple Walk", 9, 11),
            new Activity("Museum Guided Tour", 12, 14),
            new Activity("Historic Fort Hike", 14, 17),
            new Activity("Lake Sunset Boating", 16, 18),
            new Activity("Light & Sound Show", 19, 21)
        );
        List<Activity> sorted = new ArrayList<>(acts);
        sorted.sort(Comparator.comparingInt(a -> a.end));

        List<Activity> selected = new ArrayList<>();
        int lastEnd = 0;
        for (Activity a : sorted) {
            if (a.start >= lastEnd) {
                selected.add(a);
                lastEnd = a.end;
            }
        }
        System.out.println("Selected optimal daily travel activity schedule:");
        for (Activity a : selected) {
            System.out.printf("  * %02d:00 - %02d:00: %s\n", a.start, a.end, a.name);
        }

        System.out.println("\n[2. Fractional Knapsack Travel Budget Optimizer]");
        List<Item> items = Arrays.asList(
            new Item("Varanasi Tour Ticket", 200, 80),
            new Item("Mysore Palace Entry", 100, 50),
            new Item("Goa Cruise Ride", 1200, 300),
            new Item("Manali Ski Pass", 1500, 400)
        );
        List<Item> sortedItems = new ArrayList<>(items);
        sortedItems.sort((a, b) -> Double.compare(b.value / b.cost, a.value / a.cost));

        double budget = 1600;
        double currentScore = 0;
        System.out.printf("Budget Limit: Rs. %.2f\n", budget);
        for (Item item : sortedItems) {
            if (item.cost <= budget) {
                System.out.printf("  * Took 100%% of %s (Value: %.1f, Cost: Rs. %.2f)\n", item.name, item.value, item.cost);
                currentScore += item.value;
                budget -= item.cost;
            } else {
                double fraction = budget / item.cost;
                double val = item.value * fraction;
                System.out.printf("  * Took %.1f%% fraction of %s (Value: %.1f, Cost: Rs. %.2f)\n", fraction*100, item.name, val, budget);
                currentScore += val;
                budget = 0;
                break;
            }
        }
        System.out.printf("  -> Total Score: %.1f\n", currentScore);

        System.out.println("\n[3. 0/1 Knapsack Travel Package Selection (DP)]");
        int[] weights = {500, 1000, 1500, 2000};
        int[] vals = {30, 80, 110, 150};
        int capacity = 2500;
        int n = vals.length;
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                if (weights[i-1] <= w) {
                    dp[i][w] = Math.max(vals[i-1] + dp[i-1][w-weights[i-1]], dp[i-1][w]);
                } else {
                    dp[i][w] = dp[i-1][w];
                }
            }
        }
        System.out.printf("Package Choices: Weights %s | Values %s\n", Arrays.toString(weights), Arrays.toString(vals));
        System.out.printf("Max attraction points within Rs. %d budget: %d\n", capacity, dp[n][capacity]);

        System.out.println("\n[4. Longest Increasing Subsequence Tourism Growth Analysis]");
        int[] seasonalVisitsInLakhs = {12, 18, 10, 25, 22, 35, 30, 48};
        System.out.println("Historical quarterly counts: " + Arrays.toString(seasonalVisitsInLakhs));
        int lisMax = 1;
        int[] lis = new int[seasonalVisitsInLakhs.length];
        Arrays.fill(lis, 1);
        for (int i = 1; i < seasonalVisitsInLakhs.length; i++) {
            for (int j = 0; j < i; j++) {
                if (seasonalVisitsInLakhs[i] > seasonalVisitsInLakhs[j] && lis[i] < lis[j] + 1) {
                    lis[i] = lis[j] + 1;
                }
            }
        }
        for (int val : lis) if (val > lisMax) lisMax = val;
        System.out.printf("Longest quarters showing growth: %d seasons\n", lisMax);
    }
}
