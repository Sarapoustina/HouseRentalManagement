import java.io.*;
import java.util.*;

class House implements Serializable {
    private static final long serialVersionUID = 1L;
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
        return "ID: " + id + ", Location: " + location + ", Price: " + price + ", Bedrooms: " + bedrooms + ", Owner: " + ownerInfo;
    }
}

class Tenant implements Serializable {
    private static final long serialVersionUID = 1L;
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
        return "Name: " + name + ", Contact: " + contact + ", Preferred Location: " + preferredLocation;
    }
}

public class HouseRentalManagement {
    private static final String HOUSES_FILE = "houses.txt";
    private static final String TENANTS_FILE = "tenants.txt";
    private List<House> houses = new ArrayList<>();
    private List<Tenant> tenants = new ArrayList<>();

    public HouseRentalManagement() {
        loadHouses();
        loadTenants();
    }

    private void loadHouses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HOUSES_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?> tempList) {  // Pattern Matching for instanceof
                houses = new ArrayList<>();
                for (Object item : tempList) {
                    if (item instanceof House house) {  // Pattern Matching
                        houses.add(house);
                    } else {
                        System.out.println("Invalid data found in houses file.");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous house records found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading house records: " + e.getMessage());
        }
    }
    
    private void loadTenants() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TENANTS_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?> tempList) {  // Pattern Matching for instanceof
                tenants = new ArrayList<>();
                for (Object item : tempList) {
                    if (item instanceof Tenant tenant) {  // Pattern Matching
                        tenants.add(tenant);
                    } else {
                        System.out.println("Invalid data found in tenants file.");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous tenant records found.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tenant records: " + e.getMessage());
        }
    }
    

    private void saveHouses() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HOUSES_FILE))) {
            oos.writeObject(houses);
        } catch (IOException e) {
            System.out.println("Error saving house records: " + e.getMessage());
        }
    }

    private void saveTenants() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TENANTS_FILE))) {
            oos.writeObject(tenants);
        } catch (IOException e) {
            System.out.println("Error saving tenant records: " + e.getMessage());
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

    public static void main(String[] args) {
        HouseRentalManagement hrm = new HouseRentalManagement();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add House\n2. Remove House\n3. Search Houses\n4. Register Tenant\n5. Book House\n6. Exit");
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
