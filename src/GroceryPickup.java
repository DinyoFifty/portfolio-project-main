import java.util.ArrayList;

import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Models a grocery pickup order being fulfilled by a store associate. Keeps
 * track of what items were requested, where they are in the store, and whether
 * each one has been picked, substituted, or marked out of stock.
 *
 * @convention every item in statuses has a matching entry in locations
 * @correspondence this = the set of (item, location, status)
 *
 * @author Lashond Thambyrajah
 */
public class GroceryPickup {

    // States for each item in the order
    /**
     * .
     */
    public enum Status {
        /**
         * .
         */
        PENDING, // Item hasn't been addressed yet
        /**
         * .
         */
        PICKED, // Item has been confirmed
        /**
         * .
         */
        SUBSTITUTED, // Item wasn't found, had to be subsituted
        /**
         * .
         */
        OUT_OF_STOCK // Item wasn't on the shelf or in inventory
    }

    // Maps item name -> its current status
    /**
     * .
     */
    private Map<String, Status> statuses;

    // Maps item name -> its aisle location
    /**
     * .
     */
    private Map<String, String> locations;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.statuses = new Map1L<>();
        this.locations = new Map1L<>();
    }

    /**
     * No-argument constructor.
     */
    public GroceryPickup() {
        this.createNewRep();
    }

    // Kernel methods

    /**
     * Adds an item to the order with its aisle location. The order will
     * initially Sstart as PENDING.
     *
     * @param item
     *            the item name
     * @param location
     *            where it is in the store (e.g. "Aisle 3, Shelf A")
     * @requires item is not null and not already in the order
     * @ensures item is in the order with status PENDING
     */
    public final void add(String item, String location) {
        assert item != null : "Violation of: item is not null";
        assert !this.statuses.hasKey(
                item) : "Violation of: item is not already in this order";

        this.statuses.add(item, Status.PENDING);
        this.locations.add(item, location);
    }

    /**
     * Removes an item from the order entirely.
     *
     * @param item
     *            the item to remove
     * @requires item is not null and is in the order
     * @ensures item is no longer in the order
     */
    public final void remove(String item) {
        assert item != null : "Violation of: item is not null";
        assert this.statuses
                .hasKey(item) : "Violation of: item is in this order";

        this.statuses.remove(item);
        this.locations.remove(item);
    }

    /**
     * Updates the status of an item.
     *
     * @param item
     *            the item to update
     * @param s
     *            the new status
     * @requires item is not null and is in the order
     * @ensures the item's status is set to s
     */
    public final void setStatus(String item, Status s) {
        assert item != null : "Violation of: item is not null";
        assert this.statuses
                .hasKey(item) : "Violation of: item is in this order";

        this.statuses.replaceValue(item, s);
    }

    /**
     * Returns the current status of an item.
     *
     * @param item
     *            the item to check
     * @return the item's current Status
     * @requires item is not null and is in the order
     */
    public final Status getStatus(String item) {
        assert item != null : "Violation of: item is not null";
        assert this.statuses
                .hasKey(item) : "Violation of: item is in this order";

        return this.statuses.value(item);
    }

    /**
     * Returns the aisle location of an item.
     *
     * @param item
     *            the item to look up
     * @return the aisle location string
     * @requires item is not null and is in the order
     */
    public final String getLocation(String item) {
        assert item != null : "Violation of: item is not null";
        assert this.locations
                .hasKey(item) : "Violation of: item is in this order";

        return this.locations.value(item);
    }

    /**
     * Returns how many items are in the order.
     *
     * @return the number of items
     */
    public final int size() {
        return this.statuses.size();
    }

    // Secondary methods

    /**
     * Marks the original item as SUBSTITUTED and adds the replacement to the
     * order at the same aisle location.
     *
     * @param originalItem
     *            the item being swapped out
     * @param newItem
     *            the replacement item
     * @requires both items are not null, originalItem is in the order, newItem
     *           is not already in the order
     * @ensures originalItem is SUBSTITUTED, newItem is added as PENDING
     */
    public final void substitute(String originalItem, String newItem) {
        assert originalItem != null : "Violation of: originalItem is not null";
        assert this.statuses.hasKey(
                originalItem) : "Violation of: originalItem is in this order";
        assert newItem != null : "Violation of: newItem is not null";
        assert !this.statuses.hasKey(
                newItem) : "Violation of: newItem is not already in this order";

        String location = this.getLocation(originalItem);
        this.setStatus(originalItem, Status.SUBSTITUTED);
        this.add(newItem, location);
    }

    /**
     * Returns true if every item in the order has been resolved (i.e. nothing
     * is still PENDING).
     *
     * @return true if no items are PENDING
     */
    public final boolean isOrderComplete() {
        for (Map.Pair<String, Status> entry : this.statuses) {
            if (entry.value() == Status.PENDING) {
                return false;
            }
        }
        return true;
    }

    /**
     * Groups all PENDING items by their aisle location so the associate can
     * walk the store without backtracking.
     *
     * @return a map from aisle location to a list of pending items there
     */
    public final Map<String, Sequence<String>> getPickingPath() {
        Map<String, Sequence<String>> path = new Map1L<>();

        for (Map.Pair<String, Status> entry : this.statuses) {
            if (entry.value() == Status.PENDING) {
                String item = entry.key();
                String location = this.getLocation(item);
                if (!path.hasKey(location)) {
                    path.add(location, new Sequence1L<>());
                }
                path.value(location).add(path.value(location).length(), item);
            }
        }

        return path;
    }

    /**
     * Marks an item as OUT_OF_STOCK if it couldn't be found. Only works if the
     * item is still PENDING.
     *
     * @param item
     *            the item that's missing
     * @requires item is not null, is in the order, and is currently PENDING
     * @ensures item's status is set to OUT_OF_STOCK
     */
    public final void markOutofStock(String item) {
        assert item != null : "Violation of: item is not null";
        assert this.statuses
                .hasKey(item) : "Violation of: item is in this order";
        assert this.getStatus(
                item) == Status.PENDING : "Violation of: item status is PENDING";

        this.setStatus(item, Status.OUT_OF_STOCK);
    }

    /**
     * Returns a simple table showing all items, their locations, and statuses.
     *
     * @return string representation of the order
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ITEM                 LOCATION             STATUS\n");
        sb.append(
                "--------------------------------------------------------------\n");

        for (Map.Pair<String, Status> entry : this.statuses) {
            String name = entry.key();
            String location = this.locations.value(name);
            Status stat = entry.value();
            // Format the items to line up with each other
            sb.append(String.format("%-20s %-20s %s\n", name, location, stat));
        }
        return sb.toString();
    }

    /**
     * Main method.
     *
     * @param args
     *            command line arguments (not used)
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();

        GroceryPickup order = new GroceryPickup();

        // Add items to the order
        order.add("Whole Milk", "Aisle 2, Shelf A");
        order.add("Sourdough Bread", "Aisle 5, Shelf C");
        order.add("Cheddar Cheese", "Aisle 2, Shelf B");
        order.add("Chicken Breast", "Aisle 8, Shelf A");
        order.add("Orange Juice", "Aisle 3, Shelf D");

        out.println("Initial order (" + order.size() + " items):");
        out.println(order.toString());

        // Show where the associate needs to go
        out.println("Picking path:");
        Map<String, Sequence<String>> path = order.getPickingPath();

        // Collect aisle keys into a regular list and sort them
        ArrayList<String> aisles = new ArrayList<>();
        for (Map.Pair<String, Sequence<String>> entry : path) {
            aisles.add(entry.key());
        }
        java.util.Collections.sort(aisles);

        for (String aisle : aisles) {
            out.println("  " + aisle + " -> " + path.value(aisle));
        }
        out.println();

        // Simulate the associate working through the order
        order.setStatus("Whole Milk", Status.PICKED);
        order.setStatus("Cheddar Cheese", Status.PICKED);
        order.markOutofStock("Sourdough Bread");
        order.substitute("Chicken Breast", "Turkey Breast");

        out.println("Order partially filled:");
        out.println(order.toString());
        out.println("Order complete? " + order.isOrderComplete());

        // Finish the rest
        order.setStatus("Orange Juice", Status.PICKED);
        order.setStatus("Turkey Breast", Status.PICKED);

        out.println("Final order:");
        out.println(order.toString());
        out.println("Order complete? " + order.isOrderComplete());

        out.close();
    }
    //Version 1.0cddfadsf

}
