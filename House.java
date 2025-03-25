import java.io.Serializable;

public final class House implements Serializable {
    private static final long serialVersionUID = 1L;
    public final int id;
    public final String location;
    public final double price;
    public final int bedrooms;
    public final String ownerInfo;

    public House(final int id, final String location, final double price, final int bedrooms, final String ownerInfo) {
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
