import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class MetroGraph
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

            Edge(int target)
            {
                this.target = target;
            }
        }

        void addEdge(int u, int v)
        {
            adj.get(u).add(new Edge(v));
            adj.get(v).add(new Edge(u)); // Assuming undirected graph
        }

        double calculateFare(int numberOfStations)
        {
            if (numberOfStations == 1)
            return 10.0;

            if (numberOfStations == 2)
            return 15.0;

            if (numberOfStations >= 3 && numberOfStations <= 6)
            return 20.0;

            if (numberOfStations >= 7 && numberOfStations <= 9)
            return 30.0;

            if (numberOfStations >= 10 && numberOfStations <= 13)
            return 40.0;

            if (numberOfStations >= 14 && numberOfStations <= 17)
            return 50.0;

            return 60.0; // Above 18 stations
        }

        void getMetroInfo(int src, int dest)
        {
            double[] fareAndTime = bfs(src, dest);
            double fare = fareAndTime[0];
            double time = fareAndTime[1];

            System.out.printf("Metro fare from Station %d to Station %d: Rs. %.2f%n", stations[src].id, stations[dest].id, fare);
            System.out.printf("Metro time from Station %d to Station %d: %.2f min%n", stations[src].id, stations[dest].id, time);
        }

        // Updated BFS to find the fare and time between two stations
        public double[] bfs(int src, int dest)
        {
            boolean[] visited = new boolean[V];
            int[] parent = new int[V];

            Queue<Integer> queue = new LinkedList<>();
            queue.offer(src);

            visited[src] = true;
            parent[src] = -1;

            while (!queue.isEmpty())
            {
                int current = queue.poll();

                if (current == dest)
                break;

                for (Edge edge : adj.get(current))
                {
                    if (!visited[edge.target])
                    {
                        visited[edge.target] = true;
                        queue.offer(edge.target);
                        parent[edge.target] = current;
                    }
                }
            }

            if (!visited[dest])
            return new double[] { 0, 0 };

            // Reconstruct path and calculate total time
            int numberOfStations = 0;
            double totalTravelTime = 0;

            int current = dest;

            while (current != src)
            {
                int prev = parent[current];
                totalTravelTime += getTravelTime(prev, current);
                numberOfStations++;
                current = prev;
            }

            double fare = calculateFare(numberOfStations + 1);
            return new double[] { fare, totalTravelTime };
        }

        double getTravelTime(int src, int dest)
        {
            int startTime = getStationTime(src);
            int endTime = getStationTime(dest);

            return Math.abs(endTime - startTime) + 0.25; // Add 15 seconds (0.25 minutes) for stoppage time
        }

        int getStationTime(int index)
        {
            // Time in minutes corresponding to each station based on provided details
            int[] times = {0, 2, 5, 7, 10, 13, 16, 18, 20, 23, 26, 29, 31, 33, 35, 37, 40, 42, 46, 50};
            return times[index];
        }
    }

    public static Graph initializeMetroGraph()
    {
        Station[] stations = new Station[20];
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

        Graph metroGraph = new Graph(20, stations);

        // Adding edges (linear connections)
        for (int i = 0; i < stations.length - 1; i++)
        metroGraph.addEdge(i, i + 1);

        return metroGraph;
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        Graph metroGraph = initializeMetroGraph();

        System.out.print("Enter the starting station (1-20): ");
        int startStation = scanner.nextInt() - 1; // Adjusting for 0-based index
        System.out.print("Enter the destination station (1-20): ");
        int endStation = scanner.nextInt() - 1; // Adjusting for 0-based index

        if (startStation < 0 || startStation >= 20 || endStation < 0 || endStation >= 20)
        System.out.println("Invalid station number. Please enter a number between 1 and 20.");
        
        else
        metroGraph.getMetroInfo(startStation, endStation);

        scanner.close();
    }
}