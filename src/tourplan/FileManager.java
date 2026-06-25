package tourplan;

import java.io.*;
import java.util.*;

public class FileManager {

    public static void saveTours(List<Tour> tours, String filepath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write("[\n");
            for (int i = 0; i < tours.size(); i++) {
                Tour tour = tours.get(i);
                writer.write("  {\n");
                writer.write("    \"tourId\": " + tour.getTourId() + ",\n");
                writer.write("    \"tourName\": \"" + escapeJson(tour.getTourName()) + "\",\n");
                writer.write("    \"date\": \"" + escapeJson(tour.getDate()) + "\",\n");
                writer.write("    \"route\": \"" + escapeJson(tour.getRoute()) + "\",\n");
                writer.write("    \"distance\": " + tour.getDistance() + ",\n");
                writer.write("    \"budget\": " + tour.getBudget() + ",\n");
                writer.write("    \"duration\": " + tour.getDuration() + ",\n");
                writer.write("    \"cost\": " + tour.getCost() + ",\n");
                writer.write("    \"rating\": " + tour.getRating() + ",\n");
                
                // Write itinerary as JSON array
                writer.write("    \"itinerary\": [\n");
                List<String> itin = tour.getItinerary();
                for (int j = 0; j < itin.size(); j++) {
                    writer.write("      \"" + escapeJson(itin.get(j)) + "\"");
                    if (j < itin.size() - 1) {
                        writer.write(",\n");
                    } else {
                        writer.write("\n");
                    }
                }
                writer.write("    ]\n");
                writer.write("  }");
                if (i < tours.size() - 1) {
                    writer.write(",\n");
                } else {
                    writer.write("\n");
                }
            }
            writer.write("]");
        } catch (IOException e) {
            System.err.println("Error saving tours to JSON file: " + e.getMessage());
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static List<Tour> loadTours(String filepath) {
        List<Tour> list = new ArrayList<>();
        File file = new File(filepath);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String content = sb.toString();

            // Simple parser: split by elements between '{' and '}'
            int startIdx = 0;
            while ((startIdx = content.indexOf('{', startIdx)) != -1) {
                int endIdx = -1;
                int bracketCount = 1;
                int tempIdx = startIdx + 1;
                while (tempIdx < content.length() && bracketCount > 0) {
                    char c = content.charAt(tempIdx);
                    if (c == '{') bracketCount++;
                    else if (c == '}') bracketCount--;
                    tempIdx++;
                }
                endIdx = tempIdx - 1;
                if (endIdx == -1 || endIdx >= content.length()) break;

                String block = content.substring(startIdx + 1, endIdx);
                
                int tourId = parseIntSafe(extractValue(block, "tourId"));
                String tourName = extractValue(block, "tourName");
                String date = extractValue(block, "date");
                String route = extractValue(block, "route");
                double distance = parseDoubleSafe(extractValue(block, "distance"));
                double budget = parseDoubleSafe(extractValue(block, "budget"));
                double duration = parseDoubleSafe(extractValue(block, "duration"));
                double cost = parseDoubleSafe(extractValue(block, "cost"));
                double rating = parseDoubleSafe(extractValue(block, "rating"));
                
                // Extract itinerary array
                List<String> itinerary = new ArrayList<>();
                int itinStart = block.indexOf("\"itinerary\":");
                if (itinStart != -1) {
                    int arrStart = block.indexOf('[', itinStart);
                    int arrEnd = block.indexOf(']', arrStart);
                    if (arrStart != -1 && arrEnd != -1) {
                        String arrBlock = block.substring(arrStart + 1, arrEnd);
                        boolean inString = false;
                        boolean escaped = false;
                        StringBuilder currentStr = new StringBuilder();
                        for (int i = 0; i < arrBlock.length(); i++) {
                            char c = arrBlock.charAt(i);
                            if (escaped) {
                                currentStr.append(c);
                                escaped = false;
                            } else if (c == '\\') {
                                escaped = true;
                            } else if (c == '"') {
                                if (inString) {
                                    itinerary.add(currentStr.toString());
                                    currentStr.setLength(0);
                                    inString = false;
                                } else {
                                    inString = true;
                                }
                            } else if (inString) {
                                currentStr.append(c);
                            }
                        }
                    }
                }

                list.add(new Tour(tourId, tourName, date, route, distance, budget, duration, cost, rating, itinerary));
                startIdx = endIdx + 1;
            }
        } catch (IOException e) {
            System.err.println("Error loading tours from JSON file: " + e.getMessage());
        }
        return list;
    }

    private static int parseIntSafe(String val) {
        if (val == null || val.isEmpty()) return 0;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static double parseDoubleSafe(String val) {
        if (val == null || val.isEmpty()) return 0;
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static String extractValue(String block, String key) {
        String pattern = "\"" + key + "\":";
        int idx = block.indexOf(pattern);
        if (idx == -1) return "";
        int start = idx + pattern.length();
        while (start < block.length() && (block.charAt(start) == ' ' || block.charAt(start) == '\n' || block.charAt(start) == '\r' || block.charAt(start) == '\t')) {
            start++;
        }
        if (start >= block.length()) return "";
        if (block.charAt(start) == '"') {
            start++;
            int end = block.indexOf('"', start);
            if (end == -1) return "";
            return block.substring(start, end);
        } else {
            int end = start;
            while (end < block.length() && (Character.isDigit(block.charAt(end)) || block.charAt(end) == '.' || block.charAt(end) == '-')) {
                end++;
            }
            return block.substring(start, end);
        }
    }
}
