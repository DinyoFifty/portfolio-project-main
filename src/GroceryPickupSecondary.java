import components.map.Map;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.map.Map1L;

/**
 * GroceryPickup abstract class which implements only secondary methods.
 */
public abstract class GroceryPickupSecondary implements GroceryPickup {

    /*
     * Secondary methods
     */

    @Override
    public void substitute(String originalItem, String newItem) {
        assert originalItem != null : "Violation of: originalItem is not null";
        assert this.getStatus(
                originalItem) == Status.PENDING : "Violation of: originalItem status is PENDING";
        assert newItem != null : "Violation of: newItem is not null";

        String location = this.getLocation(originalItem);
        this.setStatus(originalItem, Status.SUBSTITUTED);
        this.add(newItem, location);
    }

    @Override
    public boolean isOrderComplete() {
        boolean complete = true;
        GroceryPickup temp = this.newInstance();
        int size = this.size();

        /* Remove items one by one and check if they're PENDING */
        for (int i = 0; i < size; i++) {
            Map.Pair<String, Map.Pair<Status, String>> entry = this.removeAny();
            String item = entry.key();
            Status status = entry.value().key();
            String location = entry.value().value();
            if (status == Status.PENDING) {
                complete = false;
            }
            temp.add(item, location);
            temp.setStatus(item, status);
        }
        this.transferFrom(temp);
        return complete;
    }

    @Override
    public void markOutOfStock(String item) {
        assert item != null : "Violation of: item is not null";
        assert this.getStatus(
                item) == Status.PENDING : "Violation of: item status is PENDING";

        this.setStatus(item, Status.OUT_OF_STOCK);
    }

    @Override
    public Map<String, Sequence<String>> getPickingPath() {
        Map<String, Sequence<String>> path = new Map1L<>();
        GroceryPickup temp = this.newInstance();
        int size = this.size();

        /* Transfer all items to temp while building the picking path */
        for (int i = 0; i < size; i++) {
            Map.Pair<String, Map.Pair<Status, String>> entry = this.removeAny();
            String item = entry.key();
            Status status = entry.value().key();
            String location = entry.value().value();

            if (status == Status.PENDING) {
                if (!path.hasKey(location)) {
                    path.add(location, new Sequence1L<>());
                }
                path.value(location).add(path.value(location).length(), item);
            }

            temp.add(item, location);
            temp.setStatus(item, status);
        }
        this.transferFrom(temp);
        return path;
    }

    /*
     * Key Objects methods.
     */

    /* toString will give the customer order with the size and completion. */
    @Override
    public String toString() {
        String result = "Order: {";
        GroceryPickup temp = this.newInstance();
        int size = this.size();

        for (int i = 0; i < size; i++) {
            var entry = this.removeAny();
            String item = entry.key();
            Status status = entry.value().key();
            String loc = entry.value().value();

            result += item + "=(" + status + ", " + loc + ")";

            temp.add(item, loc);
            temp.setStatus(item, status);

            if (this.size() > 0) {
                result += ", ";
            }
        }

        this.transferFrom(temp);
        result += "}";
        return result;
    }

    /* Will check if GroceryPickup orders are the same */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GroceryPickup)) {
            return false;
        }
        GroceryPickup other = (GroceryPickup) obj;
        if (this.size() != other.size()) {
            return false;
        }

        boolean isEqual = true;
        GroceryPickup temp = this.newInstance();

        while (this.size() > 0) {
            var entry = this.removeAny();
            String item = entry.key();
            Status status = entry.value().key();
            String loc = entry.value().value();

            if (!other.hasItem(item) || other.getStatus(item) != status
                    || !other.getLocation(item).equals(loc)) {
                isEqual = false;
            }

            temp.add(item, loc);
            temp.setStatus(item, status);
        }

        this.transferFrom(temp);

        return isEqual;
    }
    // Didn't include hashCode as this project doesn't need it
}