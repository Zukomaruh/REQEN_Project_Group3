import java.time.Duration;
import java.time.LocalDateTime;

public class PricingStrategy {
    private Long pricingId;
    private Long stationId;
    private Double pricePerKwh;
    private Double fixedFee;
    private Double timeBasedRate; // per minute
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;

    public boolean isActive() {
        return false;
    }

    public Double calculateEnergyCost(Double energyConsumed) {
        return null;
    }

    public Double calculateTimeCost(Duration duration) {
        return null;
    }

    public Double calculateTotalCost(Double energyConsumed, Duration duration) {
        return null;
    }
}
