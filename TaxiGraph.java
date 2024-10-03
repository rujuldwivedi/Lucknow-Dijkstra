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

    static class Graph
    {
        int V; // Number of stations
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

        // Method to calculate taxi fare using Dijkstra with dynamic fare rates for crowded stations
        public double dijkstra(int src, int dest)
        {
            Station sourceStation = stations[src];
            Station destStation = stations[dest];
            double distance = calculateDistance(sourceStation, destStation);

            // Dynamic fare based on crowded stations
            double baseFarePerKm = 10;
            double fare = baseFarePerKm;

            // Crowded station fare surcharges
            if (src == 2 || dest == 2) fare += 2; // Bhootnath
            if (src == 4 || dest == 4) fare += 3; // Badshah Nagar
            if (src == 8 || dest == 8) fare += 5; // Hazratganj
            if (src == 12 || dest == 12) fare += 5; // Charbagh
            if (src == 18 || dest == 18) fare += 3; // Amausi
            if (src == 19 || dest == 19) fare += 10; // CCS Airport

            return fare * distance; // Total fare in rupees
        }

        // Method to calculate travel time (in minutes) based on distance and average speed
        public double getTravelTime(int src, int dest)
        {
            Station sourceStation = stations[src];
            Station destStation = stations[dest];
            double distance = calculateDistance(sourceStation, destStation);

            // Assume an average speed of 40 km/h for taxis
            double speed = 40.0;
            double timeInHours = distance / speed;

            return timeInHours * 60; // Convert time to minutes
        }
    }

    // Initialize the taxi graph with stations
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
}
