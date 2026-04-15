import components.map.Map;
import components.map.Map1L;
public class GroceryPickup1L extends GroceryPickupSecondary {

    /*
     * Private representation
     */

    /**
     * Maps each item name to a pair of (Status, aisle location).
     */
    private Map<String, Map.Pair<Status, String>> rep;

    /*
     * Creator of initial representation
     */

    /**
     * Initializes {@code rep} to an empty map.
     */
    private void createNewRep() {
        this.rep = new Map1L<>();
    }

    /*
     * Constructors
     */

    /**
     * No-argument constructor: creates an empty GroceryPickup order.
     */
    public GroceryPickup1L() {
        this.createNewRep();
    }

    /*
     * Kernel methods
     */

    @Override
    public void add(String item, String location) {
        assert item != null : "Violation of: item is not null";
        assert !this.rep.hasKey(item) : "Violation of: item is not already in this";
        assert location != null : "Violation of: location is not null";

        // Store a pair of (PENDING status, location) as the value
        Map<Status, String> entryMap = new Map1L<>();
        entryMap.add(Status.PENDING, location);
        // We use a single-entry Map.Pair by pulling it back out immediately
        this.rep.add(item, entryMap.removeAny());
    }

    @Override
    public void remove(String item) {
        assert item != null : "Violation of: item is not null";
        assert this.rep.hasKey(item) : "Violation of: item is in this";

        this.rep.remove(item);
    }

    @Override
    public void setStatus(String item, Status s) {
        assert item != null : "Violation of: item is not null";
        assert this.rep.hasKey(item) : "Violation of: item is in this";
        assert s != null : "Violation of: s is not null";

        String location = this.rep.value(item).value();
        this.rep.remove(item);

        Map<Status, String> entryMap = new Map1L<>();
        entryMap.add(s, location);
        this.rep.add(item, entryMap.removeAny());
    }

    @Override
    public Status getStatus(String item) {
        assert item != null : "Violation of: item is not null";
        assert this.rep.hasKey(item) : "Violation of: item is in this";

        return this.rep.value(item).key();
    }

    @Override
    public String getLocation(String item) {
        assert item != null : "Violation of: item is not null";
        assert this.rep.hasKey(item) : "Violation of: item is in this";

        return this.rep.value(item).value();
    }

    @Override
    public Map.Pair<String, Status> removeAny() {
        assert this.rep.size() > 0 : "Violation of: |this| > 0";

        Map.Pair<String, Map.Pair<Status, String>> removed = this.rep.removeAny();

        // Build and return a Map.Pair<String, Status> using a temp map
        Map<String, Status> resultMap = new Map1L<>();
        resultMap.add(removed.key(), removed.value().key());
        return resultMap.removeAny();
    }

    @Override
    public int size() {
        return this.rep.size();
    }

    /*
     * Standard methods
     */

    @Override
    public GroceryPickup newInstance() {
        return new GroceryPickup1L();
    }

    @Override
    public void clear() {
        this.createNewRep();
    }

    @Override
    public void transferFrom(GroceryPickup source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof GroceryPickup1L :
            "Violation of: source is of dynamic type GroceryPickup1L";

        GroceryPickup1L localSource = (GroceryPickup1L) source;
        this.rep = localSource.rep;
        localSource.createNewRep();
    }

}