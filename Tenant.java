import java.io.Serializable;

public final class Tenant implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String name;
    public final String contact;
    public final String preferredLocation;

    public Tenant(final String name, final String contact, final String preferredLocation) {
        this.name = name;
        this.contact = contact;
        this.preferredLocation = preferredLocation;
    }

    @Override
    public String toString() {
        return name + "," + contact + "," + preferredLocation;
    }
}
