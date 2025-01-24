public class Car implements Runnable {
    private final int id;
    private final int gateId;
    private final int arrivalTime;
    private final int parkingDuration;
    private final ParkingSystem parkingSystem;

    public Car(int id, int gateId, int arrivalTime, int parkingDuration, ParkingSystem parkingSystem) {
        this.id = id;
        this.gateId = gateId;
        this.arrivalTime = arrivalTime;
        this.parkingDuration = parkingDuration;
        this.parkingSystem = parkingSystem;
    }

    public int getId() {
        return id;
    }

    public int getGateId() {
        return gateId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getParkingDuration() {
        return parkingDuration;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(arrivalTime * 1000);
            parkingSystem.parkCar(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}