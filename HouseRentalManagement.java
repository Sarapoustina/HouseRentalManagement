import java.io.*;
import java.util.*;

class House {
    int id;
    String location;
    double price;
    int bedrooms;
    String ownerInfo;

    public House(int id, String location, double price, int bedrooms, String ownerInfo) {
        this.id = id;
        this.location = location;
        this.price = price;
        this.bedrooms = bedrooms;
        this.ownerInfo = ownerInfo;
    }

    @Override
    public String toString() {
        return id + "," + location + "," + price + "," + bedrooms + "," + ownerInfo;
    }
}

class Tenant {
    String name;
    String contact;
    String preferredLocation;

    public Tenant(String name, String contact, String preferredLocation) {
        this.name = name;
        this.contact = contact;
        this.preferredLocation = preferredLocation;
    }

    @Override
    public String toString() {
        return name + "," + contact + "," + preferredLocation;
    }
}

public class HouseRentalManagement {
    private static final String HOUSES_FILE = "houses.txt";
    private static final String TENANTS_FILE = "tenants.txt";
    private final List<House> houses = new ArrayList<>();
    private  final List<Tenant> tenants = new ArrayList<>();

    public HouseRentalManagement() {
        loadHouses();
        loadTenants();
    }

    // Load Houses from a Text File
    private void loadHouses() {
        houses.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(HOUSES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    int id = Integer.parseInt(data[0]);
                    String location = data[1];
                    double price = Double.parseDouble(data[2]);
                    int bedrooms = Integer.parseInt(data[3]);
                    String ownerInfo = data[4];
                    houses.add(new House(id, location, price, bedrooms, ownerInfo));
                }
            }
            System.out.println("✅ Houses loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No previous house records found.");
        } catch (IOException e) {
            System.out.println("❌ Error loading house records: " + e.getMessage());
        }
    }

    // Load Tenants from a Text File
    private void loadTenants() {
        tenants.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(TENANTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String name = data[0];
                    String contact = data[1];
                    String preferredLocation = data[2];
                    tenants.add(new Tenant(name, contact, preferredLocation));
                }
            }
            System.out.println("✅ Tenants loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No previous tenant records found.");
        } catch (IOException e) {
            System.out.println("❌ Error loading tenant records: " + e.getMessage());
        }
    }

    // Save Houses to a Text File
    private void saveHouses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HOUSES_FILE))) {
            for (House house : houses) {
                writer.write(house.toString());
                writer.newLine();
            }
            System.out.println("✅ Houses saved successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error saving house records: " + e.getMessage());
        }
    }

    // Save Tenants to a Text File
    private void saveTenants() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TENANTS_FILE))) {
            for (Tenant tenant : tenants) {
                writer.write(tenant.toString());
                writer.newLine();
            }
            System.out.println("✅ Tenants saved successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error saving tenant records: " + e.getMessage());
        }
    }

    public void addHouse(int id, String location, double price, int bedrooms, String ownerInfo) {
        houses.add(new House(id, location, price, bedrooms, ownerInfo));
        saveHouses();
    }

    public void removeHouse(int id) {
        houses.removeIf(h -> h.id == id);
        saveHouses();
    }

    public void searchHouses(String location, double maxPrice) {
        for (House house : houses) {
            if (house.location.equalsIgnoreCase(location) && house.price <= maxPrice) {
                System.out.println(house);
            }
        }
    }

    public void registerTenant(String name, String contact, String preferredLocation) {
        tenants.add(new Tenant(name, contact, preferredLocation));
        saveTenants();
    }

    public void bookHouse(int houseId, String tenantName) {
        House selectedHouse = null;
        for (House house : houses) {
            if (house.id == houseId) {
                selectedHouse = house;
                break;
            }
        }
        
        if (selectedHouse == null) {
            System.out.println("House not found.");
            return;
        }
        
        System.out.println("House booked by " + tenantName + ". Lease agreement generated.");
        removeHouse(houseId);
    }

    public void displaySavedHouses() {
        System.out.println("\n📂 Saved Houses:");
        for (House house : houses) {
            System.out.println(house);
        }
    }

    public void displaySavedTenants() {
        System.out.println("\n📂 Saved Tenants:");
        for (Tenant tenant : tenants) {
            System.out.println(tenant);
        }
    }

    public static void main(String[] args) {
        HouseRentalManagement hrm = new HouseRentalManagement();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add House\n2. Remove House\n3. Search Houses\n4. Register Tenant\n5. Book House\n6. Display Data\n7. Exit");
            System.out.print("Choose an option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter location: ");
                        String location = scanner.nextLine();
                        System.out.print("Enter price: ");
                        double price = scanner.nextDouble();
                        System.out.print("Enter bedrooms: ");
                        int bedrooms = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter owner info: ");
                        String ownerInfo = scanner.nextLine();
                        hrm.addHouse(id, location, price, bedrooms, ownerInfo);
                    }
                    case 2 -> {
                        System.out.print("Enter house ID to remove: ");
                        int removeId = scanner.nextInt();
                        hrm.removeHouse(removeId);
                    }
                    case 3 -> {
                        System.out.print("Enter location: ");
                        String searchLocation = scanner.nextLine();
                        System.out.print("Enter max price: ");
                        double maxPrice = scanner.nextDouble();
                        hrm.searchHouses(searchLocation, maxPrice);
                    }
                    case 4 -> {
                        System.out.print("Enter tenant name: ");
                        String tenantName = scanner.nextLine();
                        System.out.print("Enter contact: ");
                        String contact = scanner.nextLine();
                        System.out.print("Enter preferred location: ");
                        String preferredLocation = scanner.nextLine();
                        hrm.registerTenant(tenantName, contact, preferredLocation);
                    }
                    case 5 -> {
                        System.out.print("Enter house ID to book: ");
                        int bookId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter tenant name: ");
                        String bookTenant = scanner.nextLine();
                        hrm.bookHouse(bookId, bookTenant);
                    }
                    case 6 -> {
                        hrm.displaySavedHouses();
                        hrm.displaySavedTenants();
                    }
                    case 7 -> {
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: Invalid input. Please try again.");
                scanner.nextLine();
            }
        }
    }
}

