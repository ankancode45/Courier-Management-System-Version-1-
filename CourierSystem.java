import java.util.*;

// -------------------------------------------------
// 1.  Enum for transport media

enum TransportMode {
    TRAIN, CAR, FLIGHT, SHIP
}

// -------------------------------------------------
// 2.  Custom exceptions

class InvalidTransportException extends Exception {
    public InvalidTransportException(String msg) {
        super(msg);
    }
}

class InvalidWeightException extends Exception {
    public InvalidWeightException(String msg) {
        super(msg);
    }
}

// -------------------------------------------------
// 3.  Contract for every courier service
// -------------------------------------------------
interface CourierService {
    double calculateCharges();
    void displayDetails();
}


class Courier implements CourierService {

    private final int courierId;
    private final String sender;
    private final String receiver;
    private double weight;
    private TransportMode mode;
    private double charges;

    public Courier(int courierId,
                   String sender,
                   String receiver,
                   double weight,
                   String modeStr)
            throws InvalidTransportException, InvalidWeightException {

        this.courierId = courierId;
        this.sender = sender;
        this.receiver = receiver;

        setWeight(weight);          // validates weight
        setTransportMode(modeStr);  // validates transport mode
        this.charges = calculateCharges();
    }

    // ----------------------- Helpers -----------------------
    private void setTransportMode(String modeStr) throws InvalidTransportException {
        try {
            this.mode = TransportMode.valueOf(modeStr.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidTransportException("Transport mode \"" + modeStr + "\" is not available!");
        }
    }

    private void setWeight(double weight) throws InvalidWeightException {
        if (weight <= 0) {
            throw new InvalidWeightException("Weight must be > 0 kg.");
        }
        if (weight > 100) {
            throw new InvalidWeightException("Weight exceeds the 100 kg maximum limit!");
        }
        this.weight = weight;
    }

    @Override
    public double calculateCharges() {
        double ratePerKg;
        switch (mode) {
            case TRAIN  -> ratePerKg = 10;
            case CAR    -> ratePerKg = 15;
            case FLIGHT -> ratePerKg = 30;
            case SHIP   -> ratePerKg = 8;
            default     -> ratePerKg = 0; // cannot really happen
        }
        return weight * ratePerKg;
    }

    @Override
    public void displayDetails() {
        System.out.println("Courier ID : " + courierId);
        System.out.println("Sender     : " + sender);
        System.out.println("Receiver   : " + receiver);
        System.out.println("Weight     : " + weight + " kg");
        System.out.println("Transport  : " + mode);
        System.out.println("Charges    : ₹" + charges);
        System.out.println("----------------------------------------");
    }
}



public class CourierSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Courier[] couriers = new Courier[5];    // simple fixed-size storage
        int count = 0;

        while (true) {
            try {
                // -------- Data entry --------
                System.out.print("\nEnter Courier ID               : ");
                int id = sc.nextInt();
                sc.nextLine(); // consume newline

                System.out.print("Enter Sender Name              : ");
                String sender = sc.nextLine();

                System.out.print("Enter Receiver Name            : ");
                String receiver = sc.nextLine();

                System.out.print("Enter Weight (kg)              : ");
                double weight = sc.nextDouble();
                sc.nextLine();

                System.out.print("Enter Transport Mode (TRAIN/CAR/FLIGHT/SHIP): ");
                String mode = sc.nextLine();

                // -------- Object creation --------
                couriers[count] = new Courier(id, sender, receiver, weight, mode);
                count++;

            } catch (InvalidTransportException | InvalidWeightException ex) {
                System.out.println("Error: " + ex.getMessage());
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("Storage full! Cannot add more than 5 couriers.");
                break;
            } catch (InputMismatchException ex) {
                System.out.println("Invalid input type. Please try again.");
                sc.nextLine(); // clear invalid token
            }

            System.out.print("Do you want to add another courier? (yes/no): ");
            String choice = sc.nextLine();
            if (!choice.equalsIgnoreCase("yes")) break;
        }

        // -------- Report --------
        System.out.println("\n-------------- Courier Details --------------");
        for (int i = 0; i < count; i++) {
            couriers[i].displayDetails();
        }

        sc.close();
    }
}
