import java.util.List;

public class ChargingLocation {
    private Long locationId;
    private String name;
    private String address;
    private List<ChargingStation> stations;

    public void addStation(ChargingStation station) {

    }

    public void removeStation(Long stationId) {

    }

    public List<ChargingStation> getAvailableStations() {
        return null;
    }

    public int getTotalCapacity() {
        return 0;
    }

    public int getAvailableCapacity() {
        return 0;
    }
}
