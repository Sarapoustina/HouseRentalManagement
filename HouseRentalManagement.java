import java.io.*;
import java.util.*;

public final class HouseRentalManagement {
    public static final String HOUSES_FILE = "houses.txt";
    public static final String TENANTS_FILE = "tenants.txt";
    public static final String AGREEMENTS_FILE = "agreements.txt";
    public static final String PAYMENTS_FILE = "payments.txt";

    public final List<House> houses = new ArrayList<>();
    public final List<Tenant> tenants = new ArrayList<>();

    public HouseRentalManagement() {
        loadData();
    }

    public void loadData() {
        houses.addAll(loadList(HOUSES_FILE, House.class));
        tenants.addAll(loadList(TENANTS_FILE, Tenant.class));
    }

    public <T> List<T> loadList(final String filename, final Class<T> clazz) {
        final List<T> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (clazz == House.class && line.startsWith("ID:")) {
                    String[] parts = line.split(", ");
                    final int id = Integer.parseInt(parts[0].split(": ")[1]);
                    final String location = parts[1].split(": ")[1];
                    final double price = Double.parseDouble(parts[2].split(": ")[1].replace("$", ""));
                    final int bedrooms = Integer.parseInt(parts[3].split(": ")[1]);
                    final String ownerInfo = parts[4].split(": ")[1];
                    list.add(clazz.cast(new House(id, location, price, bedrooms, ownerInfo)));
                } else if (clazz == Tenant.class && line.startsWith("Name:")) {
                    String[] parts = line.split(", ");
                    final String name = parts[0].split(": ")[1];
                    final String contact = parts[1].split(": ")[1];
                    final String preferredLocation = parts[2].split(": ")[1];
                    list.add(clazz.cast(new Tenant(name, contact, preferredLocation)));
                }
            }
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
        }
        return list;
    }

    public void saveToFile(final String filename, final List<?> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (final Object obj : list) {
                writer.write(obj.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to " + filename + ": " + e.getMessage());
        }
    }

    public static void main(final String[] args) {
        final HouseRentalManagement hrm = new HouseRentalManagement();
        final Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add House\n2. Register Tenant\n3. Search Houses\n4. Book House\n5. Exit");
            System.out.print("Choose an option: ");

            final int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter ID: ");
                    final int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter location: ");
                    final String location = scanner.nextLine();
                    System.out.print("Enter price: ");
                    final double price = scanner.nextDouble();
                    System.out.print("Enter bedrooms: ");
                    final int bedrooms = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter owner info: ");
                    final String ownerInfo = scanner.nextLine();

                    final House house = new House(id, location, price, bedrooms, ownerInfo);
                    hrm.houses.add(house);
                    hrm.saveToFile(HOUSES_FILE, hrm.houses);
                }
                case 2 -> {
                    System.out.print("Enter name: ");
                    final String name = scanner.nextLine();
                    System.out.print("Enter contact: ");
                    final String contact = scanner.nextLine();
                    System.out.print("Enter preferred location: ");
                    final String location = scanner.nextLine();

                    final Tenant tenant = new Tenant(name, contact, location);
                    hrm.tenants.add(tenant);
                    hrm.saveToFile(TENANTS_FILE, hrm.tenants);
                }
                case 3 -> {
                    System.out.print("Enter location: ");
                    final String location = scanner.nextLine();
                    System.out.print("Enter max price: ");
                    final double price = scanner.nextDouble();

                    hrm.houses.stream()
                            .filter(h -> h.location.equalsIgnoreCase(location) && h.price <= price)
                            .forEach(System.out::println);
                }
                case 4 -> {
                    System.out.print("Enter house ID: ");
                    final int houseId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter tenant name: ");
                    final String tenantName = scanner.nextLine();

                    final House house = hrm.houses.stream().filter(h -> h.id == houseId).findFirst().orElse(null);
                    final Tenant tenant = hrm.tenants.stream().filter(t -> t.name.equalsIgnoreCase(tenantName)).findFirst().orElse(null);

                    if (house == null || tenant == null) {
                        System.out.println("House or tenant not found.");
                        break;
                    }

                    final RentalAgreement agreement = new RentalAgreement(house, tenant, 12, house.price * 0.1);
                    final Payment payment = new Payment(tenant.name, String.valueOf(houseId), house.price, 30);

                    hrm.houses.remove(house);
                    hrm.saveToFile(HOUSES_FILE, hrm.houses);

                    hrm.appendToFile(AGREEMENTS_FILE, agreement.toString());
                    hrm.appendToFile(PAYMENTS_FILE, payment.toString());

                    System.out.println("House booked successfully.");
                }
                case 5 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
            }
        }
    }

    private void appendToFile(final String filename, final String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error appending to " + filename + ": " + e.getMessage());
        }
    }
}
