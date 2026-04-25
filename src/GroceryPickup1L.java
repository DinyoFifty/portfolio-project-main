import components.map.Map;
import components.map.Map1L;

/**
 * .
 */
public class GroceryPickup1L extends GroceryPickupSecondary {
    /**
     * .
     */
    private Map<String, Map.Pair<Status, String>> rep;

    /**
     * .
     */
    private void createNewRep() {
        this.rep = new Map1L<>();
    }

    /**
     * .
     */
    public GroceryPickup1L() {
        this.createNewRep();
    }

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
    public boolean hasItem(String item) {
        assert item != null : "Violation of: item is not null";
        return this.rep.hasKey(item);
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
    public Map.Pair<String, Map.Pair<Status, String>> removeAny() {
        assert this.size() > 0 : "Violation of: |this| > 0";
        return this.rep.removeAny();
    }

    @Override
    public int size() {
        return this.rep.size();
    }

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
        assert source instanceof GroceryPickup1L : "Violation of: dynamic type match";

        GroceryPickup1L locSource = (GroceryPickup1L) source;
        this.rep = locSource.rep;
        locSource.createNewRep();
    }
}
