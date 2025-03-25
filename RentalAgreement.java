import java.io.Serializable;
import java.time.LocalDate;

public final class RentalAgreement implements Serializable {
    private static final long serialVersionUID = 1L;
    public final House house;
    public final Tenant tenant;
    public final LocalDate startDate;
    public final LocalDate endDate;
    public final double deposit;

    public RentalAgreement(final House house, final Tenant tenant, final int months, final double deposit) {
        this.house = house;
        this.tenant = tenant;
        this.startDate = LocalDate.now();
        this.endDate = startDate.plusMonths(months);
        this.deposit = deposit;
    }

    @Override
    public String toString() {
        return house.id + "," + tenant.name + "," + startDate + "," + endDate + "," + deposit;
    }
}
