import components.standard.Standard;
import components.map.Map;

/**
 * GroceryPickupKernel component. Models a grocery pickup order being fulfilled
 * by a store associate, tracking items, their aisle locations, and their
 * fulfillment status.
 */
public interface GroceryPickupKernel extends Standard<GroceryPickup> {

    /**
     * Fulfillment statuses for each item in the order.
     */
    enum Status {
        /** Item has not yet been addressed in the order. */
        PENDING,

        /** Item was found and added to the cart. */
        PICKED,

        /** Item was unavailable and replaced with another item. */
        SUBSTITUTED,

        /** Item was not available and no substitute was provided. */
        OUT_OF_STOCK
    }

    /**
     * Adds the item to this order at the given location with status PENDING.
     *
     * @param item
     *            the name of the item to add
     * @param location
     *            the aisle location of the item in the store
     * @updates this
     * @requires item is not null and item is not already in this
     * @ensures this = #this union {(item, location, PENDING)}
     */
    void add(String item, String location);

    /**
     * Removes the item from this order.
     *
     * @param item
     *            the name of the item to remove
     * @updates this
     * @requires item is not null and item is in this
     * @ensures this = #this \\ {(item, location, status)}
     */
    void remove(String item);

    /**
     * Updates the status of item to s.
     *
     * @param item
     *            the name of the item to update
     * @param s
     *            the new status
     * @updates this
     * @requires item is not null and item is in this
     * @ensures the status of item in this = s
     */
    void setStatus(String item, Status s);

    /**
     * Returns the current status of the item.
     *
     * @param item
     *            the name of the item to check
     * @return the current status of item
     * @requires item is not null and item is in this
     * @ensures getStatus = status of item in this
     */
    Status getStatus(String item);

    /**
     * Returns the aisle location of the item.
     *
     * @param item
     *            the name of the item to look up
     * @return the aisle location of item
     * @requires item is not null and item is in this
     * @ensures getLocation = location of item in this
     */
    String getLocation(String item);

    /**
     * Removes and returns an arbitrary item entry from the order.
     *
     * @return a map pair containing the item name and its status
     * @updates this
     * @requires |this| > 0
     * @ensures removeAny is in #this and this = #this \ {removeAny}
     */
    Map.Pair<String, Status> removeAny();

    /**
     * Returns the number of items in this order.
     *
     * @return the number of items
     * @ensures size = |this|
     */
    int size();

}