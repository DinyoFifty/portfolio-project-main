import components.map.Map;
import components.sequence.Sequence;

/**
 * GroceryPickup that inherts the GroceryPickupKernel methods for secondary
 * methods.
 */
public interface GroceryPickup extends GroceryPickupKernel {

    /**
     * Marks the originalItem as SUBSTITUTED and adds a newItem to this order at
     * the same aisle location with status PENDING.
     *
     * @param originalItem
     *            the item being substituted
     * @param newItem
     *            the replacement item
     * @updates this
     * @requires originalItem is not null and is in this, newItem is not null
     *           and is not already in this
     * @ensures originalItem status = SUBSTITUTED and newItem is in this with
     *          status PENDING at the same location as originalItem
     */
    void substitute(String originalItem, String newItem);

    /**
     * Checks whether every item in this order has been resolved, shouldn't have
     * a PENDING status anywhere.
     *
     * @return true iff no items in this are PENDING
     * @ensures isOrderComplete = (for all items in this, status != PENDING)
     */
    boolean isOrderComplete();

    /**
     * Marks the item as OUT_OF_STOCK.
     *
     * @param item
     *            the item that could not be found
     * @updates this
     * @requires item is not null, item is in this, and item status is PENDING
     * @ensures status of item in this = OUT_OF_STOCK
     */
    void markOutOfStock(String item);

    /**
     * Returns a map grouping all PENDING items by their aisle location. This
     * allows the associate to walk through the store more efficiently.
     *
     * @return map from aisle location to list of pending items at that location
     * @ensures each entry in getPickingPath contains only items whose status is
     *          PENDING
     */
    Map<String, Sequence<String>> getPickingPath();

}