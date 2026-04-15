import components.map.Map;
import components.map.Map1L;

/**
 * GroceryPickup kernel implementation layered over using Map data structure.
 *
 * Convention: {@code rep} maps each item name to a {@code Map.Pair} whose key
 * is a {@code Status} and whose value is a String (the aisle location). No key
 * in {@code rep} is null, and no two keys are equal.
 *
 * Correspondence: {@code this} = the set of triples {(k, rep.value(k).key(),
 * rep.value(k).value()) | k in rep}, where each triple is (item name, status,
 * aisle location).
 */
public class GroceryPickup1L extends GroceryPickupSecondary {

    /*
     * Private representation
     */

    /**
     * Maps each item name to a pair of (Status, Aisle Location).
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
        assert !this.rep
                .hasKey(item) : "Violation of: item is not already in this";
        assert location != null : "Violation of: location is not null";

        Map<Status, String> entry = new Map1L<>();
        entry.add(Status.PENDING, location);
        this.rep.add(item, entry.removeAny());
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

        Map<Status, String> entry = new Map1L<>();
        entry.add(s, location);
        this.rep.add(item, entry.removeAny());
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

        Map.Pair<String, Map.Pair<Status, String>> removed = this.rep
                .removeAny();

        Map<String, Status> result = new Map1L<>();
        result.add(removed.key(), removed.value().key());

        return result.removeAny();
    }

    @Override
    public int size() {
        return this.rep.size();
    }

    /*
     * Standard method abstract contracts converted into Map.
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
        assert source instanceof GroceryPickup1L : "Violation of: source is of dynamic type GroceryPickup1L";

        GroceryPickup1L locSource = (GroceryPickup1L) source;
        this.rep = locSource.rep;
        locSource.createNewRep();
    }

}
