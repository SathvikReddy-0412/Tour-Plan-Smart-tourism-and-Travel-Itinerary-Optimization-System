# ✈️ Tour-Plan: Smart Tourism & Travel Itinerary Optimization System

A comprehensive Java-based Smart Tourism Planner and Route Optimization application. This project is built as part of the **Data Structures & Algorithms (DSA-2) Project Based Learning** course. It provides core tourism management services such as destination search, route optimization, hotel/accommodation scheduling, and advanced analytical calculations, backed by dynamic data management.

---

## 🛠️ Tech Stack & Dependencies

*   **Language:** Java (JDK 8 or higher)
*   **Database:** PostgreSQL (primary database connection configured via `db.properties`)
*   **Fallback / Library Reference:** SQLite (for standalone database configurations)
*   **External JARs (located in `/lib`):**
    *   `postgresql.jar` - PostgreSQL JDBC Driver
    *   `sqlite-jdbc.jar` - SQLite JDBC Driver
    *   `slf4j-api.jar` & `slf4j-nop.jar` - Logging interfaces

---

## 🚀 Key Features

1.  **📍 Plan New Tour (Traveler Wizard)**
    *   Collects traveler preferences (Name, Starting Location).
    *   Performs **Binary Search** to instantly locate target destinations in the system catalog.
    *   Allows selecting extra intermediate stops.
    *   Uses **Dijkstra's Algorithm** to compute the mathematically shortest and most cost-effective routes.
    *   Provides transit estimates for various transportation modes (Bus, Train, Flight) along with cost calculations.
    *   Allows selecting from a curated accommodation catalog with dynamic budget checks.
2.  **📜 Saved Tours History**
    *   Reads and writes customized tour plans dynamically to `tours.json` using a custom JSON parser.
3.  **📈 Tourism Analytics**
    *   Ranks popular destinations, monitors growth, and tracks visitor statistics.
4.  **⚙️ Destination Management (CRUD)**
    *   Allows administrators to Add, Read, Update, and Delete destination records directly inside the PostgreSQL database.

---

## 🧠 DSA Mapping (DsaEngine)

The core logical engine (`DsaEngine.java`) implements several classic computer science structures and algorithms, organized into academic modules:

### 🌲 Module 1: Trees & Balanced Search Structures
*   **Binary Search Tree (BST) Index:** Indexes destination cities alphabetically for fast lookup.
*   **AVL Tree:** A self-balancing booking registry ensuring $O(\log N)$ search, insertion, and deletion times.

### 📊 Module 2: Multiway Trees & Range Queries
*   **B-Tree Node Split Simulation:** Simulates an Order-3 B-Tree insertion and split conditions.
*   **B+ Tree Sibling Links:** Simulates sequential range queries for hotel prices using leaf node links.
*   **Segment Tree:** Computes range-sum queries for seasonal visitor statistics in $O(\log N)$ time.
*   **Fenwick Tree (Binary Indexed Tree):** Computes prefix sums for tourist frequency analytics.

### 🕸️ Module 3: Graph Spanning
*   **Prim's MST Algorithm:** Computes the Minimum Spanning Tree (MST) length to design optimal interconnected regional tourism networks.

### 🗺️ Module 4: Shortest Paths & Dependency Scheduling
*   **Bellman-Ford Algorithm:** Finds single-source shortest paths while supporting negative weights (used to model promotional travel discounts, e.g., Delhi $\rightarrow$ Agra).
*   **Floyd-Warshall Algorithm:** Computes all-pairs shortest paths represented in a distance matrix.
*   **Topological Sort:** Generates a linear dependency schedule for holiday planning activities (e.g., *Check Guidelines* $\rightarrow$ *Book Flight* $\rightarrow$ *Pack Suitcase*).

### ⚡ Module 5: Advanced Sorting & Ranking
*   **Merge Sort:** Sorts vacation package prices in ascending order.
*   **Quick Sort:** Ranks holiday destinations by popularity scores in descending order.
*   **Heap Sort:** Ranks tourist attraction entrance costs.
*   **Radix Sort:** Organizes reservation/booking IDs by their digit values.

### 🎒 Module 6: Greedy & Dynamic Programming Planners
*   **Activity Selection (Greedy):** Optimizes a tourist's daily schedule by picking the maximum number of non-overlapping activities.
*   **Fractional Knapsack (Greedy):** Maximizes travel value/satisfaction within a strict monetary budget.
*   **0/1 Knapsack (DP):** Finds the optimal subset of premium travel packages within a weight/budget capacity.
*   **Longest Increasing Subsequence (DP):** Identifies quarterly growth trends in seasonal tourist numbers.

---

## 💻 How to Build and Run

### 1. Database Setup
1. Create a PostgreSQL database named `tourplan_db`.
2. Configure your connection credentials in `db.properties`:
   ```properties
   db.url=jdbc:postgresql://localhost:5432/tourplan_db
   db.username=your_postgres_user
   db.password=your_postgres_password
   ```

### 2. Compilation
Compile all Java source files from the project root directory:
```bash
javac -d bin -cp "lib/*" src/tourplan/*.java
```

### 3. Running the App
Run the interactive console application:
```bash
java -cp "bin;lib/*" tourplan.Main
```

### 4. Running the Demonstration Wizard (CLI Walkthrough)
You can launch the program in **Demo Mode** to automatically run the simulated walkthroughs, tree diagnostic scripts, graph calculations, and sorting performance reports without manual console input:
```bash
java -cp "bin;lib/*" tourplan.Main --demo
```
