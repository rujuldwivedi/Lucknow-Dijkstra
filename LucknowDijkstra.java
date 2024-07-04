import java.util.*;
class LucknowDijkstra
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
        ArrayList<ArrayList<Integer>> adj;
        Station[] stations;    
        Graph(int V, Station[] stations)
        {
            this.V = V;
            this.stations = stations;
            adj = new ArrayList<>(V);
            for(int i = 0; i < V; i++)
            adj.add(new ArrayList<>());
        }
        void addEdge(int u, int v)
        {
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        double distance(Station s1, Station s2)
        {
            double R = 6371.0; // Earth's radius in kilometers
            double lat1 = Math.toRadians(s1.lat);
            double lon1 = Math.toRadians(s1.lon);
            double lat2 = Math.toRadians(s2.lat);
            double lon2 = Math.toRadians(s2.lon);    
            double dlon = lon2 - lon1;
            double dlat = lat2 - lat1;
            double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c;
            return distance;
        }
        void shortestPath(int src)
        {
            double[] dist = new double[V];
            Arrays.fill(dist, Double.MAX_VALUE);
            dist[src] = 0.0;    
            PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingDouble(o -> dist[o]));
            pq.add(src);
            while(!pq.isEmpty())
            {
                int u = pq.poll();
                for(int v : adj.get(u))
                {
                    Station s1 = stations[u];
                    Station s2 = stations[v];
                    double weight = distance(s1, s2);
                    if(dist[u] + weight < dist[v])
                    {
                        dist[v] = dist[u] + weight;
                        pq.add(v);
                    }
                }
            }
            // Print shortest paths from source
            System.out.println("Shortest paths from " + stations[src].id + " (" + stations[src].lat + ", " + stations[src].lon + "):");
            for(int i = 0; i < V; i++)
            {
                if(dist[i] != Double.MAX_VALUE)
                System.out.println("To " + stations[i].id + " (" + stations[i].lat + ", " + stations[i].lon + "): " + dist[i] + " km");
            }
            
            // Allow user to input two stations and compute shortest path between them
            Scanner sc = new Scanner(System.in);
            System.out.println("\n=== Shortest Distance Between Two Stations ===");
            System.out.println("Enter the station number for the first station:");
            int station1 = sc.nextInt();
            System.out.println("Enter the station number for the second station:");
            int station2 = sc.nextInt();
            double distance = dist[station2];
            distance = Math.round(distance * 100.0) / 100.0; // Round to 2 decimal places
            System.out.println("Shortest distance from " + stations[station1].id + " (" + stations[station1].lat + ", " + stations[station1].lon + ") to " +
                               stations[station2].id + " (" + stations[station2].lat + ", " + stations[station2].lon + "): " + distance + " km");
            sc.close();
        }
    }
    public static void main(String[] args)
    {
        Station[] stations = new Station[21];
        // Define station coordinates (id, latitude, longitude)
        stations[0] = new Station(1, 26.887856, 80.995842);    // Munshipulia (source)
        stations[1] = new Station(2, 26.872622, 80.990818);    // Indira Nagar
        stations[2] = new Station(3, 26.872162, 80.982272);    // Bhootnath Market
        stations[3] = new Station(4, 26.870861, 80.973554);    // Lekhraj Market
        stations[4] = new Station(5, 26.870631, 80.961702);    // Badhshah Nagar
        stations[5] = new Station(6, 26.870937, 80.945578);    // IT Chauraha
        stations[6] = new Station(7, 26.865501, 80.939566);    // Vishwavidyalay
        stations[7] = new Station(8, 26.8539, 80.9367);        // KD Singh Stadium
        stations[8] = new Station(9, 26.850723, 80.940425);    // Hazrat Ganj
        stations[9] = new Station(10, 26.842376, 80.941155);   // Sachvalaya
        stations[10] = new Station(11, 26.839082, 80.934498);  // Hussein Ganj
        stations[11] = new Station(12, 26.832343, 80.922989);  // Charbagh
        stations[12] = new Station(13, 26.831960, 80.915431);  // Durgapuri
        stations[13] = new Station(14, 26.825143, 80.909849);  // Mawaiya
        stations[14] = new Station(15, 26.818403, 80.907272);  // Alambagh Bus Station
        stations[15] = new Station(16, 26.813960, 80.902462);  // Alambagh
        stations[16] = new Station(17, 26.803044, 80.896311);  // Singar Nagar
        stations[17] = new Station(18, 26.794386, 80.891721);  // Krishna Nagar
        stations[18] = new Station(19, 26.777836, 80.882574);  // Transport Nagar
        stations[19] = new Station(20, 26.771246, 80.878623);  // Amausi
        stations[20] = new Station(21, 26.766150, 80.883561);  // CCS Airport (destination)
        Graph metro = new Graph(21, stations);
        // Adding edges based on proximity (hypothetical)
        metro.addEdge(0, 1);   // Munshipulia to Indira Nagar
        metro.addEdge(1, 2);   // Indira Nagar to Bhootnath Market
        metro.addEdge(2, 3);   // Bhootnath Market to Lekhraj Market
        metro.addEdge(3, 4);   // Lekhraj Market to Badhshah Nagar
        metro.addEdge(4, 5);   // Badhshah Nagar to IT Chauraha
        metro.addEdge(5, 6);   // IT Chauraha to Vishwavidyalay
        metro.addEdge(6, 7);   // Vishwavidyalay to KD Singh Stadium
        metro.addEdge(7, 8);   // KD Singh Stadium to Hazrat Ganj
        metro.addEdge(8, 9);   // Hazrat Ganj to Sachvalaya
        metro.addEdge(9, 10);  // Sachvalaya to Hussein Ganj
        metro.addEdge(10, 11); // Hussein Ganj to Charbagh
        metro.addEdge(11, 12); // Charbagh to Durgapuri
        metro.addEdge(12, 13); // Durgapuri to Mawaiya
        metro.addEdge(13, 14); // Mawaiya to Alambagh Bus Station
        metro.addEdge(14, 15); // Alambagh Bus Station to Alambagh
        metro.addEdge(15, 16); // Alambagh to Singar Nagar
        metro.addEdge(16, 17); // Singar Nagar to Krishna Nagar
        metro.addEdge(17, 18); // Krishna Nagar to Transport Nagar
        metro.addEdge(18, 19); // Transport Nagar to Amausi
        metro.addEdge(19, 20); // Amausi to CCS Airport (destination)
        // Calculate shortest paths from Munshipulia (source station 0)
        int sourceStation = 0;
        metro.shortestPath(sourceStation);
    }
}