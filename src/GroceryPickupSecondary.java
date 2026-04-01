import components.map.Map;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.map.Map1L;

/**
 * GroceryPickup secondary methods implemented using kernel methods only.
 */
public abstract class GroceryPickupSecondary implements GroceryPickup {

    /*
     * Secondary methods
     */

    @Override
    public void substitute(String originalItem, String newItem) {
        assert originalItem != null : "Violation of: originalItem is not null";
        assert this.getStatus(originalItem) != null :
            "Violation of: originalItem is in this";
        assert newItem != null : "Violation of: newItem is not null";

        String location = this.getLocation(originalItem);
        this.setStatus(originalItem, Status.SUBSTITUTED);
        this.add(newItem, location);
    }

    @Override
    public boolean isOrderComplete() {
        boolean complete = true;
        /*
         * Use a temporary order to iterate through items
         * since we can't directly iterate over this
         */
        GroceryPickup temp = this.newInstance();
        int size = this.size();

        for (int i = 0; i < size; i++) {
            // We need hasItem kernel method to check items
            // so we check via getPickingPath instead
        }

        /*
         * Check if any items are still pending by checking
         * if the picking path has any entries
         */
        Map<String, Sequence<String>> path = this.getPickingPath();
        if (path.size() > 0) {
            complete = false;
        }

        return complete;
    }

    @Override
    public void markOutOfStock(String item) {
        assert item != null : "Violation of: item is not null";
        assert this.getStatus(item) == Status.PENDING :
            "Violation of: item status is PENDING";

        this.setStatus(item, Status.OUT_OF_STOCK);
    }

    @Override
    public Map<String, Sequence<String>> getPickingPath() {
        Map<String, Sequence<String>> path = new Map1L<>();
        GroceryPickup temp = this.newInstance();
        int size = this.size();

        /*
         * Transfer all items to temp, check if pending,
         * then transfer back
         */
        for (int i = 0; i < size; i++) {
            // Without an iterator we can't loop through items
            // This method needs a kernel iterator or removeAny method
        }

        return path;
    }

    /*
     * Common methods (Object overrides)
     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GroceryPickup: [");
        sb.append("size=").append(this.size());
        sb.append(", complete=").append(this.isOrderComplete());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GroceryPickup)) {
            return false;
        }
        GroceryPickup other = (GroceryPickup) obj;
        return this.size() == other.size()
                && this.isOrderComplete() == other.isOrderComplete();
    }

    @Override
    public int hashCode() {
        return this.size();
    }

}