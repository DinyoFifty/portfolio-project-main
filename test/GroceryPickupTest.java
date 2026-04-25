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
     * [Normal] Tests substitute adds the new item with status PENDING.
     */
    @Test
    public void testSubstituteNewItemIsPending() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.substitute("Milk", "Oat Milk");

        assertTrue(order.hasItem("Oat Milk"));
        assertEquals(GroceryPickupKernel.Status.PENDING,
                order.getStatus("Oat Milk"));
    }

    /**
     * [Normal] Tests substitute places the new item at the same location as the
     * original.
     */
    @Test
    public void testSubstituteNewItemSameLocation() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.substitute("Milk", "Oat Milk");

        assertEquals("A1", order.getLocation("Oat Milk"));
    }

    /**
     * [Normal] Tests substitute increases size by exactly one (original stays,
     * new item added).
     */
    @Test
    public void testSubstituteSizeIncreasedByOne() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.substitute("Milk", "Oat Milk");

        assertEquals(2, order.size());
    }

    /**
     * [Normal] Tests substitute does not affect other items in the order.
     */
    @Test
    public void testSubstituteDoesNotAffectOtherItems() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.substitute("Milk", "Oat Milk");

        assertEquals(GroceryPickupKernel.Status.PENDING,
                order.getStatus("Bread"));
        assertEquals("B2", order.getLocation("Bread"));
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests substitute on the only item in the order results in two
     * items total.
     */
    @Test
    public void testSubstituteOnlyItemInOrder() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.substitute("Milk", "Almond Milk");

        assertEquals(2, order.size());
        assertTrue(order.hasItem("Milk"));
        assertTrue(order.hasItem("Almond Milk"));
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
     * [Extreme] Tests substitute with very long item names works correctly.
     */
    @Test
    public void testSubstituteVeryLongItemNames() {
        GroceryPickup order = this.createEmpty();
        String original = "A".repeat(500);
        String replacement = "B".repeat(500);
        order.add(original, "Z1");
        order.substitute(original, replacement);

        assertEquals(GroceryPickupKernel.Status.SUBSTITUTED,
                order.getStatus(original));
        assertTrue(order.hasItem(replacement));
        assertEquals("Z1", order.getLocation(replacement));
    }

    /*
     * -------------------------------------------------------------------------
     * isOrderComplete
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

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
     * [Normal] Tests isOrderComplete returns false when at least one item is
     * PENDING.
     */
    @Test
    public void testIsOrderCompleteOnePending() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        assertFalse(order.isOrderComplete());
    }

    /**
     * [Normal] Tests isOrderComplete returns false when all items are PENDING.
     */
    @Test
    public void testIsOrderCompleteAllPending() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");

        assertFalse(order.isOrderComplete());
    }

    /**
     * [Normal] Tests isOrderComplete returns true for a mix of non-PENDING
     * statuses.
     */
    @Test
    public void testIsOrderCompleteMixedNonPending() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.add("Eggs", "C3");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);
        order.setStatus("Bread", GroceryPickupKernel.Status.OUT_OF_STOCK);
        order.setStatus("Eggs", GroceryPickupKernel.Status.SUBSTITUTED);

        assertTrue(order.isOrderComplete());
    }

    /**
     * [Normal] Tests isOrderComplete does not mutate the order.
     */
    @Test
    public void testIsOrderCompleteDoesNotMutate() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);
        GroceryPickup copy = this.createEmpty();
        copy.add("Milk", "A1");
        copy.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        order.isOrderComplete();

        assertEquals(copy, order);
    }

    // --- Boundary ---

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
     * [Boundary] Tests isOrderComplete on a single PENDING item returns false.
     */
    @Test
    public void testIsOrderCompleteSinglePendingItem() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");

        assertFalse(order.isOrderComplete());
    }

    // --- Extreme ---

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
     * [Extreme] Tests isOrderComplete returns false when exactly one item of
     * many is still PENDING.
     */
    @Test
    public void testIsOrderCompleteOneRemainingPendingInLargeOrder() {
        GroceryPickup order = this.createEmpty();
        int count = 50;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
            if (i != count - 1) {
                order.setStatus("Item" + i, GroceryPickupKernel.Status.PICKED);
            }
        }

        assertFalse(order.isOrderComplete());
    }

    /*
     * -------------------------------------------------------------------------
     * markOutOfStock
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

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
     * [Normal] Tests markOutOfStock does not change the item's location.
     */
    @Test
    public void testMarkOutOfStockPreservesLocation() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.markOutOfStock("Milk");

        assertEquals("A1", order.getLocation("Milk"));
    }

    /**
     * [Normal] Tests markOutOfStock does not affect other items.
     */
    @Test
    public void testMarkOutOfStockDoesNotAffectOthers() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.markOutOfStock("Milk");

        assertEquals(GroceryPickupKernel.Status.PENDING,
                order.getStatus("Bread"));
    }

    // --- Boundary ---

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
     * [Boundary] Tests markOutOfStock does not mutate the order beyond the
     * status change.
     */
    @Test
    public void testMarkOutOfStockSizeUnchanged() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.markOutOfStock("Milk");

        assertEquals(2, order.size());
    }

    // --- Extreme ---

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

    /*
     * -------------------------------------------------------------------------
     * getPickingPath
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

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
     * [Normal] Tests getPickingPath groups items by location correctly.
     */
    @Test
    public void testGetPickingPathGroupsByLocation() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "A1");
        order.add("Eggs", "B2");

        Map<String, Sequence<String>> path = order.getPickingPath();

        assertEquals(2, path.value("A1").length());
        assertEquals(1, path.value("B2").length());
    }

    /**
     * [Normal] Tests getPickingPath returns an empty map when no items are
     * PENDING.
     */
    @Test
    public void testGetPickingPathNoPendingItems() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        Map<String, Sequence<String>> path = order.getPickingPath();

        assertEquals(0, path.size());
    }

    /**
     * [Normal] Tests getPickingPath does not mutate the order.
     */
    @Test
    public void testGetPickingPathDoesNotMutate() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        GroceryPickup copy = this.createEmpty();
        copy.add("Milk", "A1");

        order.getPickingPath();

        assertEquals(copy, order);
    }

    // --- Boundary ---

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
     * [Boundary] Tests getPickingPath returns empty map for a single
     * non-PENDING item.
     */
    @Test
    public void testGetPickingPathSingleNonPendingItem() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.markOutOfStock("Milk");

        Map<String, Sequence<String>> path = order.getPickingPath();

        assertEquals(0, path.size());
    }

    // --- Extreme ---

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
     * [Extreme] Tests getPickingPath with all items at the same location groups
     * them into one entry.
     */
    @Test
    public void testGetPickingPathAllItemsSameLocation() {
        GroceryPickup order = this.createEmpty();
        int count = 30;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "A1");
        }

        Map<String, Sequence<String>> path = order.getPickingPath();

        assertEquals(1, path.size());
        assertTrue(path.hasKey("A1"));
        assertEquals(count, path.value("A1").length());
    }

    /*
     * -------------------------------------------------------------------------
     * toString
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests toString on an empty order returns the correct format.
     */
    @Test
    public void testToStringEmptyOrder() {
        GroceryPickup order = this.createEmpty();

        assertEquals("Order: {}", order.toString());
    }

    /**
     * [Normal] Tests toString contains the item name, status, and location.
     */
    @Test
    public void testToStringContainsItemInfo() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        String result = order.toString();

        assertTrue(result.contains("Milk"));
        assertTrue(result.contains("PENDING"));
        assertTrue(result.contains("A1"));
    }

    /**
     * [Normal] Tests toString does not mutate the order.
     */
    @Test
    public void testToStringDoesNotMutate() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        GroceryPickup copy = this.createEmpty();
        copy.add("Milk", "A1");

        order.toString();

        assertEquals(copy, order);
    }

    // --- Boundary ---

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
     * [Boundary] Tests toString reflects an updated status correctly.
     */
    @Test
    public void testToStringReflectsUpdatedStatus() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);
        String result = order.toString();

        assertTrue(result.contains("PICKED"));
    }

    // --- Extreme ---

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
     * [Extreme] Tests toString with very long item name and location includes
     * them in the output.
     */
    @Test
    public void testToStringVeryLongItemNameAndLocation() {
        GroceryPickup order = this.createEmpty();
        String longName = "X".repeat(200);
        String longLoc = "Y".repeat(200);
        order.add(longName, longLoc);
        String result = order.toString();

        assertTrue(result.contains(longName));
        assertTrue(result.contains(longLoc));
    }

    /*
     * -------------------------------------------------------------------------
     * equals
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

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
     * [Normal] Tests equals returns true for two identical orders.
     */
    @Test
    public void testEqualsIdenticalOrders() {
        GroceryPickup a = this.createEmpty();
        a.add("Milk", "A1");
        a.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        GroceryPickup b = this.createEmpty();
        b.add("Milk", "A1");
        b.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        assertTrue(a.equals(b));
    }

    /**
     * [Normal] Tests equals returns false when orders differ by item name.
     */
    @Test
    public void testEqualsDifferentItem() {
        GroceryPickup a = this.createEmpty();
        a.add("Milk", "A1");

        GroceryPickup b = this.createEmpty();
        b.add("Bread", "A1");

        assertFalse(a.equals(b));
    }

    /**
     * [Normal] Tests equals returns false when orders differ by status.
     */
    @Test
    public void testEqualsDifferentStatus() {
        GroceryPickup a = this.createEmpty();
        a.add("Milk", "A1");

        GroceryPickup b = this.createEmpty();
        b.add("Milk", "A1");
        b.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        assertFalse(a.equals(b));
    }

    /**
     * [Normal] Tests equals returns false when orders differ by location.
     */
    @Test
    public void testEqualsDifferentLocation() {
        GroceryPickup a = this.createEmpty();
        a.add("Milk", "A1");

        GroceryPickup b = this.createEmpty();
        b.add("Milk", "Z9");

        assertFalse(a.equals(b));
    }

    /**
     * [Normal] Tests equals returns false when orders differ by size.
     */
    @Test
    public void testEqualsDifferentSize() {
        GroceryPickup a = this.createEmpty();
        a.add("Milk", "A1");

        GroceryPickup b = this.createEmpty();
        b.add("Milk", "A1");
        b.add("Bread", "B2");

        assertFalse(a.equals(b));
    }

    /**
     * [Normal] Tests equals does not mutate either order.
     */
    @Test
    public void testEqualsDoesNotMutate() {
        GroceryPickup a = this.createEmpty();
        a.add("Milk", "A1");
        GroceryPickup aCopy = this.createEmpty();
        aCopy.add("Milk", "A1");

        GroceryPickup b = this.createEmpty();
        b.add("Milk", "A1");
        GroceryPickup bCopy = this.createEmpty();
        bCopy.add("Milk", "A1");

        a.equals(b);

        assertEquals(aCopy, a);
        assertEquals(bCopy, b);
    }

    /**
     * [Normal] Tests equals returns true when compared to itself.
     */
    @Test
    public void testEqualsSameReference() {
        GroceryPickup a = this.createEmpty();
        a.add("Milk", "A1");

        assertTrue(a.equals(a));
    }

    /**
     * [Normal] Tests equals returns false when compared to a non-GroceryPickup
     * object.
     */
    @Test
    public void testEqualsWrongType() {
        GroceryPickup a = this.createEmpty();
        a.add("Milk", "A1");

        assertFalse(a.equals("not a GroceryPickup"));
    }

    // --- Boundary ---

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
     * [Boundary] Tests equals returns false when same items have different
     * mixed statuses.
     */
    @Test
    public void testEqualsMixedStatusesDiffer() {
        GroceryPickup a = this.createEmpty();
        a.add("Milk", "A1");
        a.setStatus("Milk", GroceryPickupKernel.Status.OUT_OF_STOCK);

        GroceryPickup b = this.createEmpty();
        b.add("Milk", "A1");
        b.setStatus("Milk", GroceryPickupKernel.Status.SUBSTITUTED);

        assertFalse(a.equals(b));
    }

    // --- Extreme ---

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

    /**
     * [Extreme] Tests equals returns false for two large orders differing in
     * exactly one item's status.
     */
    @Test
    public void testEqualsLargeOrdersDifferByOneStatus() {
        GroceryPickup a = this.createEmpty();
        GroceryPickup b = this.createEmpty();
        int count = 50;
        for (int i = 0; i < count; i++) {
            a.add("Item" + i, "Loc" + i);
            b.add("Item" + i, "Loc" + i);
        }
        a.setStatus("Item0", GroceryPickupKernel.Status.PICKED);

        assertFalse(a.equals(b));
    }
}