import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingSystem {
    private static final int TOTAL_SPOTS = 4;
    private final Semaphore parkingSpots = new Semaphore(TOTAL_SPOTS, true);
    private final AtomicInteger carsServed = new AtomicInteger(0);
    private final AtomicInteger[] carsServedByGate = {new AtomicInteger(0), new AtomicInteger(0), new AtomicInteger(0)};
    private final List<String> activityLog = new ArrayList<>();

    public synchronized void logActivity(String message) {
        activityLog.add(message);
        System.out.println(message);
    }

    public void parkCar(Car car) {
        try {
            logActivity("Car " + car.getId() + " from Gate " + car.getGateId() + " arrived at time " + car.getArrivalTime());

            if (!parkingSpots.tryAcquire()) {
                logActivity("Car " + car.getId() + " from Gate " + car.getGateId() + " waiting for a spot.");
                parkingSpots.acquire();
            }

            logActivity("Car " + car.getId() + " from Gate " + car.getGateId() + 
            " parked. (Parking Status: " + (TOTAL_SPOTS - parkingSpots.availablePermits()) + " spots occupied)");
            carsServed.incrementAndGet();
            carsServedByGate[car.getGateId() - 1].incrementAndGet();

            Thread.sleep(car.getParkingDuration() * 1000);

            parkingSpots.release();
            logActivity("Car " + car.getId() + " from Gate " + car.getGateId() + " left after " + car.getParkingDuration() + " units of time. (Parking Status: " + (TOTAL_SPOTS - parkingSpots.availablePermits()) + " spots occupied)");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getCarsServed() {
        return carsServed.get();
    }

    public int getCarsServedByGate(int gateId) {
        return carsServedByGate[gateId - 1].get();
    }

    public int getCurrentCarsInParking() {
        return TOTAL_SPOTS - parkingSpots.availablePermits();
    }

    public List<String> getActivityLog() {
        return activityLog;
    }
}