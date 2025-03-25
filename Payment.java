import java.io.Serializable;
import java.time.LocalDate;

public final class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String tenantName;
    public final String houseId;
    public final double amount;
    public final LocalDate dueDate;
    public boolean isPaid;

    public Payment(final String tenantName, final String houseId, final double amount, final int dueInDays) {
        this.tenantName = tenantName;
        this.houseId = houseId;
        this.amount = amount;
        this.dueDate = LocalDate.now().plusDays(dueInDays);
        this.isPaid = false;
    }

    public void markAsPaid() {
        isPaid = true;
    }

    @Override
    public String toString() {
        return tenantName + "," + houseId + "," + amount + "," + dueDate + "," + isPaid;
    }
}
