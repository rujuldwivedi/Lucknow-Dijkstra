import java.util.ArrayList;
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
        Station[] stations;

        Graph(int V, Station[] stations)
        {
            this.V = V;
            this.stations = stations;
        }

        // Method to calculate distance between two points using Haversine formula
        private double calculateDistance(Station a, Station b)
        {
            final int R = 6371; // Radius of the Earth in km

            double latDistance = Math.toRadians(b.lat - a.lat);
            double lonDistance = Math.toRadians(b.lon - a.lon);

            double aHav = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(a.lat)) * Math.cos(Math.toRadians(b.lat))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

            double c = 2 * Math.atan2(Math.sqrt(aHav), Math.sqrt(1 - aHav));

            return R * c; // Distance in km
        }

        double getTaxiInfo(int src, int dest)
        {
            Station sourceStation = stations[src];
            Station destStation = stations[dest];
            double distance = calculateDistance(sourceStation, destStation);

            double fare = distance * 10; // Fare in rupees (10 rupees/km)
            double time = (distance / 40) * 60; // Time in minutes (distance/speed) converted to minutes

            System.out.printf("Taxi fare from %d to %d: Rs. %.2f, Time: %.2f minutes%n", sourceStation.id, destStation.id, fare, time);

            return fare; // Return fare for storing
        }

        double getTravelTime(int src, int dest)
        {
            Station sourceStation = stations[src];
            Station destStation = stations[dest];
            double distance = calculateDistance(sourceStation, destStation);

            double time = (distance / 40) * 60; // Time in minutes
            return time; // Return time for storing
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

        return new Graph(21, stations); // Return the initialized taxi graph
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
