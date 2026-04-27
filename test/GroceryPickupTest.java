import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map;
import components.sequence.Sequence;

/**
 * JUnit test fixture for {@code GroceryPickupSecondary} methods.
 *
 * <p>
 * Tests cover: substitute, isOrderComplete, markOutOfStock, getPickingPath,
 * toString, and equals. Each method has normal, boundary, and extreme cases.
 * </p>
 */
public class GroceryPickupTest {

    /*
     * -------------------------------------------------------------------------
     * Helpers
     * -------------------------------------------------------------------------
     */

    /**
     * Creates and returns a fresh empty {@code GroceryPickup1L}.
     *
     * @return empty GroceryPickup instance
     */
    private GroceryPickup createEmpty() {
        return new GroceryPickup1L();
    }

    /*
     * -------------------------------------------------------------------------
     * substitute
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests substitute marks the original item as SUBSTITUTED.
     */
    @Test
    public void testSubstituteOriginalIsSubstituted() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.substitute("Milk", "Oat Milk");

        assertEquals(GroceryPickupKernel.Status.SUBSTITUTED,
                order.getStatus("Milk"));
    }

    /**
     * [Boundary] Tests substitute where original and new item names differ only
     * by one character.
     */
    @Test
    public void testSubstituteSimilarNames() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.substitute("Milk", "Milx");

        assertTrue(order.hasItem("Milk"));
        assertTrue(order.hasItem("Milx"));
        assertEquals(GroceryPickupKernel.Status.SUBSTITUTED,
                order.getStatus("Milk"));
        assertEquals(GroceryPickupKernel.Status.PENDING,
                order.getStatus("Milx"));
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests substituting every item in a large order; all original
     * items are SUBSTITUTED and all new items are PENDING.
     */
    @Test
    public void testSubstituteManyItems() {
        GroceryPickup order = this.createEmpty();
        int count = 20;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
        }
        for (int i = 0; i < count; i++) {
            order.substitute("Item" + i, "Sub" + i);
        }

        assertEquals(count * 2, order.size());
        for (int i = 0; i < count; i++) {
            assertEquals(GroceryPickupKernel.Status.SUBSTITUTED,
                    order.getStatus("Item" + i));
            assertEquals(GroceryPickupKernel.Status.PENDING,
                    order.getStatus("Sub" + i));
            assertEquals("Loc" + i, order.getLocation("Sub" + i));
        }
    }

    /**
     * [Normal] Tests isOrderComplete returns true when all items are PICKED.
     */
    @Test
    public void testIsOrderCompleteAllPicked() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);
        order.setStatus("Bread", GroceryPickupKernel.Status.PICKED);

        assertTrue(order.isOrderComplete());
    }

    /**
     * [Boundary] Tests isOrderComplete on a single PICKED item returns true.
     */
    @Test
    public void testIsOrderCompleteSinglePickedItem() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        assertTrue(order.isOrderComplete());
    }

    /**
     * [Extreme] Tests isOrderComplete returns true for a large all-PICKED
     * order.
     */
    @Test
    public void testIsOrderCompleteLargeAllPicked() {
        GroceryPickup order = this.createEmpty();
        int count = 50;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
            order.setStatus("Item" + i, GroceryPickupKernel.Status.PICKED);
        }

        assertTrue(order.isOrderComplete());
    }

    /**
     * [Normal] Tests markOutOfStock sets the item status to OUT_OF_STOCK.
     */
    @Test
    public void testMarkOutOfStockSetsStatus() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.markOutOfStock("Milk");

        assertEquals(GroceryPickupKernel.Status.OUT_OF_STOCK,
                order.getStatus("Milk"));
    }

    /**
     * [Boundary] Tests markOutOfStock on the only item in the order.
     */
    @Test
    public void testMarkOutOfStockOnlyItem() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.markOutOfStock("Milk");

        assertEquals(GroceryPickupKernel.Status.OUT_OF_STOCK,
                order.getStatus("Milk"));
        assertEquals(1, order.size());
    }

    /**
     * [Extreme] Tests marking all items in a large order as out of stock; none
     * are PENDING.
     */
    @Test
    public void testMarkOutOfStockAllItemsInLargeOrder() {
        GroceryPickup order = this.createEmpty();
        int count = 50;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
        }
        for (int i = 0; i < count; i++) {
            order.markOutOfStock("Item" + i);
        }

        for (int i = 0; i < count; i++) {
            assertEquals(GroceryPickupKernel.Status.OUT_OF_STOCK,
                    order.getStatus("Item" + i));
        }
        assertTrue(order.isOrderComplete());
    }

    /**
     * [Normal] Tests getPickingPath returns only PENDING items.
     */
    @Test
    public void testGetPickingPathOnlyPending() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "A1");
        order.add("Eggs", "B2");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        Map<String, Sequence<String>> path = order.getPickingPath();

        assertFalse(path.hasKey("A1")
                && path.value("A1").toString().contains("Milk"));
        assertTrue(path.hasKey("A1"));
        assertTrue(path.hasKey("B2"));
    }

    /**
     * [Boundary] Tests getPickingPath on a single PENDING item returns a map
     * with one location and one item.
     */
    @Test
    public void testGetPickingPathSinglePendingItem() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");

        Map<String, Sequence<String>> path = order.getPickingPath();

        assertEquals(1, path.size());
        assertTrue(path.hasKey("A1"));
        assertEquals(1, path.value("A1").length());
    }

    /**
     * [Extreme] Tests getPickingPath with many items spread across many
     * locations.
     */
    @Test
    public void testGetPickingPathManyLocations() {
        GroceryPickup order = this.createEmpty();
        int locationCount = 10;
        int itemsPerLocation = 5;
        for (int loc = 0; loc < locationCount; loc++) {
            for (int item = 0; item < itemsPerLocation; item++) {
                order.add("Item" + loc + "_" + item, "Loc" + loc);
            }
        }

        Map<String, Sequence<String>> path = order.getPickingPath();

        assertEquals(locationCount, path.size());
        for (int loc = 0; loc < locationCount; loc++) {
            assertTrue(path.hasKey("Loc" + loc));
            assertEquals(itemsPerLocation, path.value("Loc" + loc).length());
        }
    }

    /**
     * [Normal] Tests toString on an empty order returns the correct format.
     */
    @Test
    public void testToStringEmptyOrder() {
        GroceryPickup order = this.createEmpty();

        assertEquals("Order: {}", order.toString());
    }

    /**
     * [Boundary] Tests toString on a single-item order starts and ends with the
     * correct braces.
     */
    @Test
    public void testToStringSingleItemFormat() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        String result = order.toString();

        assertTrue(result.startsWith("Order: {"));
        assertTrue(result.endsWith("}"));
    }

    /**
     * [Extreme] Tests toString on a large order contains all item names.
     */
    @Test
    public void testToStringLargeOrderContainsAllItems() {
        GroceryPickup order = this.createEmpty();
        int count = 20;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
        }
        String result = order.toString();

        for (int i = 0; i < count; i++) {
            assertTrue(result.contains("Item" + i));
        }
    }

    /**
     * [Normal] Tests equals returns true for two empty orders.
     */
    @Test
    public void testEqualsEmptyOrders() {
        GroceryPickup a = this.createEmpty();
        GroceryPickup b = this.createEmpty();

        assertTrue(a.equals(b));
    }

    /**
     * [Boundary] Tests equals returns false when one order is empty and the
     * other has one item.
     */
    @Test
    public void testEqualsOneEmptyOneNot() {
        GroceryPickup a = this.createEmpty();
        GroceryPickup b = this.createEmpty();
        b.add("Milk", "A1");

        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    /**
     * [Extreme] Tests equals returns true for two large identical orders.
     */
    @Test
    public void testEqualsLargeIdenticalOrders() {
        GroceryPickup a = this.createEmpty();
        GroceryPickup b = this.createEmpty();
        int count = 50;
        for (int i = 0; i < count; i++) {
            a.add("Item" + i, "Loc" + i);
            b.add("Item" + i, "Loc" + i);
        }

        assertTrue(a.equals(b));
    }
}