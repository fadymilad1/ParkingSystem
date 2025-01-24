import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ParkingSystem parkingSystem = new ParkingSystem();

        String fileName = "kk.txt";

        List<Car> gate1Cars = new ArrayList<>();
        List<Car> gate2Cars = new ArrayList<>();
        List<Car> gate3Cars = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",\\s*"); 
                int gateId = Integer.parseInt(parts[0].split(" ")[1]);
                int carId = Integer.parseInt(parts[1].split(" ")[1]);
                int arrivalTime = Integer.parseInt(parts[2].split(" ")[1]);
                int parkingDuration = Integer.parseInt(parts[3].split(" ")[1]);

                Car car = new Car(carId, gateId, arrivalTime, parkingDuration, parkingSystem);

            
                if (gateId == 1) {
                    gate1Cars.add(car);
                } else if (gateId == 2) {
                    gate2Cars.add(car);
                } else if (gateId == 3) {
                    gate3Cars.add(car);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return;
        }

        Thread gate1Thread = new Thread(new Gate(1, gate1Cars));
        Thread gate2Thread = new Thread(new Gate(2, gate2Cars));
        Thread gate3Thread = new Thread(new Gate(3, gate3Cars));

        gate1Thread.start();
        gate2Thread.start();
        gate3Thread.start();

        try {
            gate1Thread.join();
            gate2Thread.join();
            gate3Thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nSimulation Summary:");
        System.out.println("Total Cars Served: " + parkingSystem.getCarsServed());
        System.out.println("Current Cars in Parking: " + parkingSystem.getCurrentCarsInParking());
        System.out.println("Details:");
        System.out.println("- Gate 1 served " + parkingSystem.getCarsServedByGate(1) + " cars.");
        System.out.println("- Gate 2 served " + parkingSystem.getCarsServedByGate(2) + " cars.");
        System.out.println("- Gate 3 served " + parkingSystem.getCarsServedByGate(3) + " cars.");
    }
}