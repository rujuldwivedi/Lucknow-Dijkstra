import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

class TaxiGraph
{
    static class Station
    {
        int id;
        double lat;
        double lon;

        Station(int id, double lat, double lon)
        {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
        }
    }

    static class Comparison
    {
        static double fare1;
        static double time1;
    }

    static class Graph
    {
        int V;
        ArrayList<ArrayList<Edge>> adj;
        Station[] stations;

        Graph(int V, Station[] stations)
        {
            this.V = V;
            this.stations = stations;

            adj = new ArrayList<>(V);

            for (int i = 0; i < V; i++)
            adj.add(new ArrayList<>());
        }

        static class Edge
        {
            int target;
            double fare;
            double time; // in minutes

            Edge(int target, double fare, double time)
            {
                this.target = target;
                this.fare = fare;
                this.time = time;
            }
        }

        void addEdge(int u, int v, double fare, double time)
        {
            adj.get(u).add(new Edge(v, fare, time));
            adj.get(v).add(new Edge(u, fare, time)); // assuming undirected graph
        }

        double[] dijkstra(int src, int dest)
        {
            double[] minFare = new double[V];
            double[] minTime = new double[V];
            boolean[] visited = new boolean[V];

            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) ->
            {
                // Prioritize by fare, then by time if fares are equal
                if (a[1] == b[1]) return Double.compare(minTime[a[0]], minTime[b[0]]);
                return Double.compare(minFare[a[0]], minFare[b[0]]);
            });

            for (int i = 0; i < V; i++)
            {
                minFare[i] = Double.MAX_VALUE; // Initialize with infinity
                minTime[i] = Double.MAX_VALUE; // Initialize with infinity
            }

            minFare[src] = 0;
            minTime[src] = 0;
            pq.add(new int[]{src, 0}); // {station index, fare}

            while (!pq.isEmpty())
            {
                int[] current = pq.poll();
                int u = current[0];

                if (visited[u])
                continue; // Skip if already visited

                visited[u] = true;

                for (Edge edge : adj.get(u))
                {
                    int v = edge.target;
                    double newFare = minFare[u] + edge.fare;
                    double newTime = minTime[u] + edge.time;

                    // Update fare if the new fare is lower
                    if (newFare < minFare[v])
                    {
                        minFare[v] = newFare;
                        minTime[v] = newTime;
                        pq.add(new int[]{v, (int) newFare});
                    }
                }
            }

            return new double[]{minFare[dest], minTime[dest]}; // Return fare and time
        }

        double getTaxiInfo(int src, int dest)
        {
            double[] result = dijkstra(src, dest);

            if (result[0] == Double.MAX_VALUE)
            {
                System.out.println("No path found from " + stations[src].id + " to " + stations[dest].id);
                return -1; // Return -1 if no path found
            }

            System.out.printf("Taxi fare from %d to %d: Rs. %.2f%n", stations[src].id, stations[dest].id, result[0]);
            return result[0]; // Return fare for storing
        }

        double getTravelTime(int src, int dest)
        {
            double[] result = dijkstra(src, dest);
            if (result[1] == Double.MAX_VALUE)
            return -1; // Return -1 if no path found

            return result[1]; // Return time for storing
        }
    }

    public static Graph initializeTaxiGraph()
    {
        Station[] stations = new Station[21];
        // Define station coordinates (id, latitude, longitude)
        stations[0] = new Station(1, 26.887856, 80.995842); // Munshipulia
        stations[1] = new Station(2, 26.872622, 80.990818); // Indira Nagar
        stations[2] = new Station(3, 26.872162, 80.982272); // Bhootnath Market
        stations[3] = new Station(4, 26.870861, 80.973554); // Lekhraj Market
        stations[4] = new Station(5, 26.870631, 80.961702); // Badshah Nagar
        stations[5] = new Station(6, 26.870937, 80.945578); // IT College
        stations[6] = new Station(7, 26.865501, 80.939566); // Lucknow University
        stations[7] = new Station(8, 26.8539, 80.9367);     // KD Singh Babu Stadium
        stations[8] = new Station(9, 26.850723, 80.940425); // Hazratganj
        stations[9] = new Station(10, 26.842376, 80.941155); // Sachivalaya
        stations[10] = new Station(11, 26.839082, 80.934498); // Husainganj
        stations[11] = new Station(12, 26.832343, 80.922989); // Charbagh
        stations[12] = new Station(13, 26.831960, 80.915431); // Durgapuri
        stations[13] = new Station(14, 26.825143, 80.909849); // Mawaiya
        stations[14] = new Station(15, 26.818403, 80.907272); // Alambagh Bus Station
        stations[15] = new Station(16, 26.813960, 80.902462); // Alambagh
        stations[16] = new Station(17, 26.803044, 80.896311); // Singar Nagar
        stations[17] = new Station(18, 26.794386, 80.891721); // Krishna Nagar
        stations[18] = new Station(19, 26.777836, 80.882574); // Amausi
        stations[19] = new Station(20, 26.771246, 80.878623); // CCS Airport
    
        Graph taxiGraph = new Graph(21, stations);
    
        // Adding edges with fare and time (example values, modify as needed)
        taxiGraph.addEdge(0, 1, 10.0, 3.0); // Example fare and time from Munshipulia to Indira Nagar
        taxiGraph.addEdge(1, 2, 10.0, 5.0); // Example fare and time from Indira Nagar to Bhootnath Market
        taxiGraph.addEdge(2, 3, 5.0, 4.0); // Example fare and time from Bhootnath Market to Lekhraj Market
        taxiGraph.addEdge(3, 4, 5.0, 4.0); // Example fare and time from Lekhraj Market to Badshah Nagar
        taxiGraph.addEdge(4, 5, 10.0, 5.0); // Example fare and time from Badshah Nagar to IT College
        taxiGraph.addEdge(5, 6, 15.0, 3.0); // Example fare and time from IT College to Lucknow University
        taxiGraph.addEdge(6, 7, 10.0, 4.0); // Example fare and time from Lucknow University to KD Singh Babu Stadium
        taxiGraph.addEdge(7, 8, 15.0, 4.0); // Example fare and time from KD Singh Babu Stadium to Hazratganj
        taxiGraph.addEdge(8, 9, 10.0, 4.0); // Example fare and time from Hazratganj to Sachivalaya
        taxiGraph.addEdge(9, 10, 10.0, 4.0); // Example fare and time from Sachivalaya to Husainganj
        taxiGraph.addEdge(10, 11, 15.0, 7.0); // Example fare and time from Husainganj to Charbagh
        taxiGraph.addEdge(11, 12, 10.0, 3.0); // Example fare and time from Charbagh to Durgapuri
        taxiGraph.addEdge(12, 13, 15.0, 6.0); // Example fare and time from Durgapuri to Mawaiya
        taxiGraph.addEdge(13, 14, 20.0, 6.0); // Example fare and time from Mawaiya to Alambagh Bus Station
        taxiGraph.addEdge(14, 15, 10.0, 5.0); // Example fare and time from Alambagh Bus Station to Alambagh
        taxiGraph.addEdge(15, 16, 10.0, 3.0); // Example fare and time from Alambagh to Singar Nagar
        taxiGraph.addEdge(16, 17, 10.0, 3.0); // Example fare and time from Singar Nagar to Krishna Nagar
        taxiGraph.addEdge(17, 18, 30.0, 9.0); // Example fare and time from Krishna Nagar to Amausi
        taxiGraph.addEdge(18, 19, 15.0, 2.0); // Example fare and time from Amausi to CCS Airport
    
        return taxiGraph; // Return the initialized taxi graph
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        
        // Initialize taxi graph
        Graph taxiGraph = initializeTaxiGraph();

        // Input: Get station numbers from the user
        System.out.print("Enter the starting station (1-20): ");
        int startStation = scanner.nextInt() - 1; // Adjusting for 0-based index
        System.out.print("Enter the destination station (1-20): ");
        int endStation = scanner.nextInt() - 1; // Adjusting for 0-based index

        // Validate input
        if (startStation < 0 || startStation >= 20 || endStation < 0 || endStation >= 20)
        {
            System.out.println("Invalid station number. Please enter a number between 1 and 20.");
            scanner.close();
            return;
        }

        // Get taxi fare and time
        double fare = taxiGraph.getTaxiInfo(startStation, endStation);
        double time = taxiGraph.getTravelTime(startStation, endStation);

        // Store fare and time for comparison
        if (fare != -1 && time != -1)
        {
            Comparison.fare1 = fare;
            Comparison.time1 = time;

            System.out.printf("Taxi fare: Rs. %.2f, Time: %.2f min%n", fare, time);
        }

        scanner.close();
    }
}
