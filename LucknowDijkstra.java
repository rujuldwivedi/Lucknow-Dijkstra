import java.util.Scanner;

public class LucknowDijkstra
{
    private static final int SIGMA_DIVISOR = 1000;

    public static double calculateSigma(double fare, double time)
    {
        return (fare * time) / SIGMA_DIVISOR;
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        
        // Initialize metro and taxi graphs using existing classes
        MetroGraph.Graph metroGraph = MetroGraph.initializeMetroGraph();
        TaxiGraph.Graph taxiGraph = TaxiGraph.initializeTaxiGraph();

        System.out.print("Enter the starting station (1-20): ");
        int startStation = scanner.nextInt() - 1; // Adjusting for 0-based index
        System.out.print("Enter the destination station (1-20): ");
        int endStation = scanner.nextInt() - 1; // Adjusting for 0-based index

        if (startStation < 0 || startStation >= 20 || endStation < 0 || endStation >= 20)
        {
            System.out.println("Invalid station number. Please enter a number between 1 and 20.");
            scanner.close();
            return;
        }

        // Calculate metro fare and time
        double[] metroResult = metroGraph.bfs(startStation, endStation);
        double metroFare = metroResult[0];
        double metroTime = metroResult[1];

        // Calculate taxi fare and time
        double taxiFare = taxiGraph.getTaxiInfo(startStation, endStation);
        double taxiTime = taxiGraph.getTravelTime(startStation, endStation);

        // Calculate sigma values
        double metroSigma = calculateSigma(metroFare, metroTime);
        double taxiSigma = calculateSigma(taxiFare, taxiTime);

        // Print results
        System.out.printf("Metro: Fare = Rs. %.2f, Time = %.2f min, Sigma = %.2f%n", metroFare, metroTime, metroSigma);
        System.out.printf("Taxi: Fare = Rs. %.2f, Time = %.2f min, Sigma = %.2f%n", taxiFare, taxiTime, taxiSigma);

        if (metroSigma < taxiSigma)
        System.out.println("Metro is better for this journey.");
        
        else
        System.out.println("Taxi is better for this journey.");

        // Calculate and print sigma values for all stations from the first station
        System.out.println("\nSigma values for all stations from the first station:");
        
        for (int i = 0; i < 20; i++)
        {
            double[] metroResultFromFirst = metroGraph.bfs(0, i);
            double metroFareFromFirst = metroResultFromFirst[0];
            double metroTimeFromFirst = metroResultFromFirst[1];
            
            double taxiFareFromFirst = taxiGraph.getTaxiInfo(0, i);
            double taxiTimeFromFirst = taxiGraph.getTravelTime(0, i);
            
            double metroSigmaFromFirst = calculateSigma(metroFareFromFirst, metroTimeFromFirst);
            double taxiSigmaFromFirst = calculateSigma(taxiFareFromFirst, taxiTimeFromFirst);
            
            System.out.printf("Station %d: Metro Sigma = %.2f, Taxi Sigma = %.2f%n", i + 1, metroSigmaFromFirst, taxiSigmaFromFirst);
        }

        scanner.close();
    }
}