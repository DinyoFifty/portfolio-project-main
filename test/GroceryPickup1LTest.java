import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.map.Map;

/**
 * JUnit test fixture for {@code GroceryPickup1L} kernel methods.
 *
 * Tests cover: add, remove, setStatus, getStatus, getLocation, hasItem,
 * removeAny, size, clear, newInstance, and transferFrom. Each method has
 * normal, boundary, and extreme cases.
 */
public class GroceryPickup1LTest {

    /**
     * Creates and returns a fresh empty {@code GroceryPickup1L}.
     *
     * @return empty GroceryPickup1L instance
     */
    private GroceryPickup createEmpty() {
        return new GroceryPickup1L();
    }

    /**
     * Add normal case for single itmes.
     */
    @Test
    public void testAddSingleItem() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");

        assertEquals(1, order.size());
        assertTrue(order.hasItem("Milk"));
    }

    /**
     * Add normal case for multiple items.
     */
    @Test
    public void testAddMultipleItems() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.add("Eggs", "C3");

        assertEquals(3, order.size());
        assertTrue(order.hasItem("Milk"));
        assertTrue(order.hasItem("Bread"));
        assertTrue(order.hasItem("Eggs"));
        assertEquals("A1", order.getLocation("Milk"));
        assertEquals("B2", order.getLocation("Bread"));
        assertEquals("C3", order.getLocation("Eggs"));
    }

    /**
     * Add boundary case for adding to an order with a single item already.
     */
    @Test
    public void testAddToSingleItemOrder() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");

        assertEquals(2, order.size());
        assertTrue(order.hasItem("Milk"));
        assertTrue(order.hasItem("Bread"));
    }

    /**
     * Add extreme case adding a large nuber of items.
     */
    @Test
    public void testAddManyItems() {
        GroceryPickup order = this.createEmpty();
        int count = 100;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
        }

        assertEquals(count, order.size());
        for (int i = 0; i < count; i++) {
            assertTrue(order.hasItem("Item" + i));
            assertEquals("Loc" + i, order.getLocation("Item" + i));
        }
    }

    /**
     * Remove normal case removing a single item.
     */
    @Test
    public void testRemoveSingleItem() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.remove("Milk");

        assertEquals(0, order.size());
        assertFalse(order.hasItem("Milk"));
    }

    /**
     * [Normal] Tests remove of one item from a multi-item order does not affect
     * others.
     */
    @Test
    public void testRemoveOneOfManyItems() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.remove("Milk");

        assertFalse(order.hasItem("Milk"));
        assertTrue(order.hasItem("Bread"));
        assertEquals(1, order.size());
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests remove leaves exactly one item remaining in a two-item
     * order.
     */
    @Test
    public void testRemoveFromTwoItemOrderLeavesOne() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.remove("Bread");

        assertEquals(1, order.size());
        assertTrue(order.hasItem("Milk"));
        assertFalse(order.hasItem("Bread"));
    }

    /**
     * [Boundary] Tests remove then re-add of the same item name works
     * correctly.
     */
    @Test
    public void testRemoveThenReAdd() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.remove("Milk");
        order.add("Milk", "B2");

        assertTrue(order.hasItem("Milk"));
        assertEquals("B2", order.getLocation("Milk"));
        assertEquals(1, order.size());
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests removing all items from a large order leaves it empty.
     */
    @Test
    public void testRemoveAllItemsFromLargeOrder() {
        GroceryPickup order = this.createEmpty();
        int count = 50;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
        }
        for (int i = 0; i < count; i++) {
            order.remove("Item" + i);
        }

        assertEquals(0, order.size());
    }

    /**
     * [Extreme] Tests remove with a very long item name succeeds.
     */
    @Test
    public void testRemoveVeryLongItemName() {
        GroceryPickup order = this.createEmpty();
        String longName = "B".repeat(1000);
        order.add(longName, "Z1");
        order.remove(longName);

        assertFalse(order.hasItem(longName));
        assertEquals(0, order.size());
    }

    /*
     * -------------------------------------------------------------------------
     * setStatus
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests setStatus changes status from PENDING to PICKED.
     */
    @Test
    public void testSetStatusToPicked() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        assertEquals(GroceryPickupKernel.Status.PICKED,
                order.getStatus("Milk"));
    }

    /**
     * [Normal] Tests setStatus changes status to OUT_OF_STOCK.
     */
    @Test
    public void testSetStatusToOutOfStock() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.OUT_OF_STOCK);

        assertEquals(GroceryPickupKernel.Status.OUT_OF_STOCK,
                order.getStatus("Milk"));
    }

    /**
     * [Normal] Tests setStatus does not change location of item.
     */
    @Test
    public void testSetStatusDoesNotChangeLocation() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        assertEquals("A1", order.getLocation("Milk"));
    }

    /**
     * [Normal] Tests setStatus on one item does not affect another item's
     * status.
     */
    @Test
    public void testSetStatusDoesNotAffectOtherItems() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        assertEquals(GroceryPickupKernel.Status.PENDING,
                order.getStatus("Bread"));
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests setStatus twice on the same item reflects the last
     * status set.
     */
    @Test
    public void testSetStatusTwiceReflectsLatest() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);
        order.setStatus("Milk", GroceryPickupKernel.Status.OUT_OF_STOCK);

        assertEquals(GroceryPickupKernel.Status.OUT_OF_STOCK,
                order.getStatus("Milk"));
    }

    /**
     * [Boundary] Tests setStatus on the only item in the order updates
     * correctly.
     */
    @Test
    public void testSetStatusOnlyItem() {
        GroceryPickup order = this.createEmpty();
        order.add("Eggs", "C3");
        order.setStatus("Eggs", GroceryPickupKernel.Status.SUBSTITUTED);

        assertEquals(GroceryPickupKernel.Status.SUBSTITUTED,
                order.getStatus("Eggs"));
        assertEquals(1, order.size());
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests setStatus on every item in a large order; all statuses
     * are updated correctly.
     */
    @Test
    public void testSetStatusOnAllItemsInLargeOrder() {
        GroceryPickup order = this.createEmpty();
        int count = 50;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
        }
        for (int i = 0; i < count; i++) {
            order.setStatus("Item" + i, GroceryPickupKernel.Status.PICKED);
        }
        for (int i = 0; i < count; i++) {
            assertEquals(GroceryPickupKernel.Status.PICKED,
                    order.getStatus("Item" + i));
        }
    }

    /**
     * [Extreme] Tests cycling through all status values on a single item ends
     * at the expected final status.
     */
    @Test
    public void testSetStatusCycleThroughAllValues() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);
        order.setStatus("Milk", GroceryPickupKernel.Status.OUT_OF_STOCK);
        order.setStatus("Milk", GroceryPickupKernel.Status.SUBSTITUTED);
        order.setStatus("Milk", GroceryPickupKernel.Status.PENDING);

        assertEquals(GroceryPickupKernel.Status.PENDING,
                order.getStatus("Milk"));
    }

    /*
     * -------------------------------------------------------------------------
     * getStatus
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests getStatus returns PENDING for a newly added item.
     */
    @Test
    public void testGetStatusNewItemIsPending() {
        GroceryPickup order = this.createEmpty();
        order.add("Juice", "D4");
        GroceryPickup copy = this.createEmpty();
        copy.add("Juice", "D4");

        assertEquals(GroceryPickupKernel.Status.PENDING,
                order.getStatus("Juice"));
        assertEquals(copy, order);
    }

    /**
     * [Normal] Tests getStatus returns SUBSTITUTED after setStatus.
     */
    @Test
    public void testGetStatusAfterSubstituted() {
        GroceryPickup order = this.createEmpty();
        order.add("Juice", "D4");
        order.setStatus("Juice", GroceryPickupKernel.Status.SUBSTITUTED);

        assertEquals(GroceryPickupKernel.Status.SUBSTITUTED,
                order.getStatus("Juice"));
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests getStatus does not mutate the order.
     */
    @Test
    public void testGetStatusDoesNotMutate() {
        GroceryPickup order = this.createEmpty();
        order.add("Juice", "D4");
        GroceryPickup copy = this.createEmpty();
        copy.add("Juice", "D4");

        order.getStatus("Juice");

        assertEquals(copy, order);
    }

    /**
     * [Boundary] Tests getStatus on item with PICKED status returns correctly.
     */
    @Test
    public void testGetStatusPicked() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        assertEquals(GroceryPickupKernel.Status.PICKED,
                order.getStatus("Milk"));
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests getStatus on every item in a large order returns the
     * correct status.
     */
    @Test
    public void testGetStatusManyItems() {
        GroceryPickup order = this.createEmpty();
        int count = 50;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
            if (i % 2 == 0) {
                order.setStatus("Item" + i, GroceryPickupKernel.Status.PICKED);
            }
        }
        for (int i = 0; i < count; i++) {
            GroceryPickupKernel.Status expected = (i % 2 == 0)
                    ? GroceryPickupKernel.Status.PICKED
                    : GroceryPickupKernel.Status.PENDING;
            assertEquals(expected, order.getStatus("Item" + i));
        }
    }

    /*
     * -------------------------------------------------------------------------
     * getLocation
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests getLocation returns the exact aisle added.
     */
    @Test
    public void testGetLocationCorrect() {
        GroceryPickup order = this.createEmpty();
        order.add("Cheese", "E5");
        GroceryPickup copy = this.createEmpty();
        copy.add("Cheese", "E5");

        assertEquals("E5", order.getLocation("Cheese"));
        assertEquals(copy, order);
    }

    /**
     * [Normal] Tests getLocation after status change still returns original
     * location.
     */
    @Test
    public void testGetLocationAfterStatusChange() {
        GroceryPickup order = this.createEmpty();
        order.add("Cheese", "E5");
        order.setStatus("Cheese", GroceryPickupKernel.Status.PICKED);

        assertEquals("E5", order.getLocation("Cheese"));
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests getLocation does not mutate the order.
     */
    @Test
    public void testGetLocationDoesNotMutate() {
        GroceryPickup order = this.createEmpty();
        order.add("Cheese", "E5");
        GroceryPickup copy = this.createEmpty();
        copy.add("Cheese", "E5");

        order.getLocation("Cheese");

        assertEquals(copy, order);
    }

    /**
     * [Boundary] Tests getLocation on an item with a single-character location.
     */
    @Test
    public void testGetLocationSingleChar() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A");

        assertEquals("A", order.getLocation("Milk"));
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests getLocation after many status changes still returns
     * original location.
     */
    @Test
    public void testGetLocationAfterManyStatusChanges() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);
        order.setStatus("Milk", GroceryPickupKernel.Status.OUT_OF_STOCK);
        order.setStatus("Milk", GroceryPickupKernel.Status.SUBSTITUTED);

        assertEquals("A1", order.getLocation("Milk"));
    }

    /**
     * [Extreme] Tests getLocation with a very long location string.
     */
    @Test
    public void testGetLocationVeryLongLocation() {
        GroceryPickup order = this.createEmpty();
        String longLoc = "Z".repeat(1000);
        order.add("Milk", longLoc);

        assertEquals(longLoc, order.getLocation("Milk"));
    }

    /*
     * -------------------------------------------------------------------------
     * hasItem
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests hasItem returns false on an empty order.
     */
    @Test
    public void testHasItemEmptyOrder() {
        GroceryPickup order = this.createEmpty();

        assertFalse(order.hasItem("Milk"));
    }

    /**
     * [Normal] Tests hasItem returns true after item is added.
     */
    @Test
    public void testHasItemAfterAdd() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");

        assertTrue(order.hasItem("Milk"));
    }

    /**
     * [Normal] Tests hasItem returns false after item is removed.
     */
    @Test
    public void testHasItemAfterRemove() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.remove("Milk");

        assertFalse(order.hasItem("Milk"));
    }

    /**
     * [Normal] Tests hasItem does not alter the state of the order.
     */
    @Test
    public void testHasItemDoesNotMutate() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        GroceryPickup copy = this.createEmpty();
        copy.add("Milk", "A1");

        order.hasItem("Milk");

        assertEquals(copy, order);
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests hasItem returns false for an item name not in a
     * non-empty order.
     */
    @Test
    public void testHasItemNotPresentInNonEmptyOrder() {
        GroceryPickup order = this.createEmpty();
        order.add("Bread", "B2");

        assertFalse(order.hasItem("Milk"));
    }

    /**
     * [Boundary] Tests hasItem returns true for the only item in a single-item
     * order.
     */
    @Test
    public void testHasItemOnlyItem() {
        GroceryPickup order = this.createEmpty();
        order.add("Eggs", "C3");

        assertTrue(order.hasItem("Eggs"));
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests hasItem on a large order correctly identifies presence
     * and absence.
     */
    @Test
    public void testHasItemLargeOrder() {
        GroceryPickup order = this.createEmpty();
        int count = 100;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
        }

        assertTrue(order.hasItem("Item0"));
        assertTrue(order.hasItem("Item99"));
        assertFalse(order.hasItem("Item100"));
    }

    /**
     * [Extreme] Tests hasItem with a very long item name returns true when
     * present.
     */
    @Test
    public void testHasItemVeryLongName() {
        GroceryPickup order = this.createEmpty();
        String longName = "C".repeat(1000);
        order.add(longName, "Z1");

        assertTrue(order.hasItem(longName));
        assertFalse(order.hasItem("C".repeat(999)));
    }

    /*
     * -------------------------------------------------------------------------
     * removeAny
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests removeAny on a single-item order returns that item and
     * leaves order empty.
     */
    @Test
    public void testRemoveAnySingleItem() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");

        Map.Pair<String, Map.Pair<GroceryPickupKernel.Status, String>> entry = order
                .removeAny();

        assertEquals("Milk", entry.key());
        assertEquals(GroceryPickupKernel.Status.PENDING, entry.value().key());
        assertEquals("A1", entry.value().value());
        assertEquals(0, order.size());
    }

    /**
     * [Normal] Tests removeAny decrements size by exactly one.
     */
    @Test
    public void testRemoveAnySizeDecreases() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");

        order.removeAny();

        assertEquals(1, order.size());
    }

    /**
     * [Normal] Tests removeAny returns an entry that was actually in the order.
     */
    @Test
    public void testRemoveAnyReturnedItemWasPresent() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");

        Map.Pair<String, Map.Pair<GroceryPickupKernel.Status, String>> entry = order
                .removeAny();
        String removedItem = entry.key();

        assertTrue(removedItem.equals("Milk") || removedItem.equals("Bread"));
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests removeAny on a two-item order leaves exactly one item.
     */
    @Test
    public void testRemoveAnyTwoItemsLeavesOne() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");

        Map.Pair<String, Map.Pair<GroceryPickupKernel.Status, String>> entry = order
                .removeAny();
        String removed = entry.key();
        String remaining = removed.equals("Milk") ? "Bread" : "Milk";

        assertEquals(1, order.size());
        assertTrue(order.hasItem(remaining));
        assertFalse(order.hasItem(removed));
    }

    /**
     * [Boundary] Tests removeAny preserves status and location of returned
     * entry.
     */
    @Test
    public void testRemoveAnyPreservesStatusAndLocation() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.setStatus("Milk", GroceryPickupKernel.Status.PICKED);

        Map.Pair<String, Map.Pair<GroceryPickupKernel.Status, String>> entry = order
                .removeAny();

        assertEquals(GroceryPickupKernel.Status.PICKED, entry.value().key());
        assertEquals("A1", entry.value().value());
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests removeAny until empty on a large order; all entries were
     * valid.
     */
    @Test
    public void testRemoveAnyUntilEmptyLargeOrder() {
        GroceryPickup order = this.createEmpty();
        int count = 50;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
        }

        int removed = 0;
        while (order.size() > 0) {
            Map.Pair<String, Map.Pair<GroceryPickupKernel.Status, String>> entry = order
                    .removeAny();
            assertTrue(entry.key().startsWith("Item"));
            removed++;
        }

        assertEquals(count, removed);
        assertEquals(0, order.size());
    }

    /*
     * -------------------------------------------------------------------------
     * size
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests size is zero on an empty order.
     */
    @Test
    public void testSizeEmptyOrder() {
        GroceryPickup order = this.createEmpty();

        assertEquals(0, order.size());
    }

    /**
     * [Normal] Tests size increments correctly as items are added.
     */
    @Test
    public void testSizeAfterAdds() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.add("Eggs", "C3");

        assertEquals(3, order.size());
    }

    /**
     * [Normal] Tests size does not change from a query operation like hasItem.
     */
    @Test
    public void testSizeUnchangedByQuery() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.hasItem("Milk");

        assertEquals(1, order.size());
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests size is exactly 1 after adding one item to empty order.
     */
    @Test
    public void testSizeIsOneAfterFirstAdd() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");

        assertEquals(1, order.size());
    }

    /**
     * [Boundary] Tests size decrements to zero after removing the last item.
     */
    @Test
    public void testSizeDecrementsToZero() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.remove("Milk");

        assertEquals(0, order.size());
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests size matches add/remove operations across many items.
     */
    @Test
    public void testSizeAfterManyAddsAndRemoves() {
        GroceryPickup order = this.createEmpty();
        int count = 100;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
        }
        assertEquals(count, order.size());

        for (int i = 0; i < count / 2; i++) {
            order.remove("Item" + i);
        }
        assertEquals(count / 2, order.size());
    }

    /*
     * -------------------------------------------------------------------------
     * clear
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests clear on an empty order keeps size at zero.
     */
    @Test
    public void testClearEmptyOrder() {
        GroceryPickup order = this.createEmpty();
        order.clear();

        assertEquals(0, order.size());
    }

    /**
     * [Normal] Tests clear removes all items and resets size to zero.
     */
    @Test
    public void testClearNonEmptyOrder() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.add("Bread", "B2");
        order.clear();

        assertEquals(0, order.size());
        assertFalse(order.hasItem("Milk"));
        assertFalse(order.hasItem("Bread"));
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests clear on a single-item order results in empty order.
     */
    @Test
    public void testClearSingleItemOrder() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.clear();

        assertEquals(0, order.size());
        assertFalse(order.hasItem("Milk"));
    }

    /**
     * [Boundary] Tests clear allows items to be added again afterward.
     */
    @Test
    public void testClearThenAdd() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.clear();
        order.add("Bread", "B2");

        assertEquals(1, order.size());
        assertTrue(order.hasItem("Bread"));
        assertFalse(order.hasItem("Milk"));
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests clear on a large order resets size to zero.
     */
    @Test
    public void testClearLargeOrder() {
        GroceryPickup order = this.createEmpty();
        int count = 100;
        for (int i = 0; i < count; i++) {
            order.add("Item" + i, "Loc" + i);
        }
        order.clear();

        assertEquals(0, order.size());
        assertFalse(order.hasItem("Item0"));
        assertFalse(order.hasItem("Item99"));
    }

    /**
     * [Extreme] Tests calling clear multiple times in succession stays empty.
     */
    @Test
    public void testClearCalledMultipleTimes() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.clear();
        order.clear();
        order.clear();

        assertEquals(0, order.size());
    }

    /*
     * -------------------------------------------------------------------------
     * newInstance
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests newInstance returns a non-null empty order.
     */
    @Test
    public void testNewInstanceIsEmpty() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        GroceryPickup fresh = order.newInstance();

        assertEquals(0, fresh.size());
    }

    /**
     * [Normal] Tests newInstance does not disturb the original order.
     */
    @Test
    public void testNewInstanceDoesNotMutateOriginal() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        order.newInstance();

        assertEquals(1, order.size());
        assertTrue(order.hasItem("Milk"));
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests newInstance from an empty order also returns empty.
     */
    @Test
    public void testNewInstanceFromEmptyOrder() {
        GroceryPickup order = this.createEmpty();
        GroceryPickup fresh = order.newInstance();

        assertEquals(0, fresh.size());
        assertEquals(0, order.size());
    }

    /**
     * [Boundary] Tests that adding to newInstance does not affect original.
     */
    @Test
    public void testNewInstanceIsIndependentFromOriginal() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");
        GroceryPickup fresh = order.newInstance();
        fresh.add("Bread", "B2");

        assertEquals(1, order.size());
        assertFalse(order.hasItem("Bread"));
        assertEquals(1, fresh.size());
        assertTrue(fresh.hasItem("Bread"));
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests calling newInstance many times does not mutate the
     * original.
     */
    @Test
    public void testNewInstanceCalledManyTimes() {
        GroceryPickup order = this.createEmpty();
        order.add("Milk", "A1");

        for (int i = 0; i < 50; i++) {
            GroceryPickup fresh = order.newInstance();
            assertEquals(0, fresh.size());
        }

        assertEquals(1, order.size());
        assertTrue(order.hasItem("Milk"));
    }

    /*
     * -------------------------------------------------------------------------
     * transferFrom
     * -------------------------------------------------------------------------
     */

    // --- Normal ---

    /**
     * [Normal] Tests transferFrom moves all items to the destination and clears
     * the source.
     */
    @Test
    public void testTransferFromMovesItems() {
        GroceryPickup source = this.createEmpty();
        source.add("Milk", "A1");
        source.add("Bread", "B2");

        GroceryPickup dest = this.createEmpty();
        dest.transferFrom(source);

        assertEquals(2, dest.size());
        assertTrue(dest.hasItem("Milk"));
        assertTrue(dest.hasItem("Bread"));
        assertEquals(0, source.size());
    }

    /**
     * [Normal] Tests transferFrom clears the destination's previous contents.
     */
    @Test
    public void testTransferFromClearsDest() {
        GroceryPickup source = this.createEmpty();
        source.add("Milk", "A1");

        GroceryPickup dest = this.createEmpty();
        dest.add("Eggs", "C3");
        dest.transferFrom(source);

        assertFalse(dest.hasItem("Eggs"));
        assertTrue(dest.hasItem("Milk"));
    }

    // --- Boundary ---

    /**
     * [Boundary] Tests transferFrom with an empty source results in empty
     * destination.
     */
    @Test
    public void testTransferFromEmptySource() {
        GroceryPickup source = this.createEmpty();
        GroceryPickup dest = this.createEmpty();
        dest.add("Milk", "A1");
        dest.transferFrom(source);

        assertEquals(0, dest.size());
        assertEquals(0, source.size());
    }

    /**
     * [Boundary] Tests transferFrom with a single-item source transfers
     * correctly.
     */
    @Test
    public void testTransferFromSingleItemSource() {
        GroceryPickup source = this.createEmpty();
        source.add("Milk", "A1");
        GroceryPickup dest = this.createEmpty();
        dest.transferFrom(source);

        assertEquals(1, dest.size());
        assertTrue(dest.hasItem("Milk"));
        assertEquals(0, source.size());
    }

    // --- Extreme ---

    /**
     * [Extreme] Tests transferFrom with a large source moves all items and
     * clears source.
     */
    @Test
    public void testTransferFromLargeSource() {
        GroceryPickup source = this.createEmpty();
        int count = 100;
        for (int i = 0; i < count; i++) {
            source.add("Item" + i, "Loc" + i);
        }

        GroceryPickup dest = this.createEmpty();
        dest.transferFrom(source);

        assertEquals(count, dest.size());
        assertEquals(0, source.size());
        for (int i = 0; i < count; i++) {
            assertTrue(dest.hasItem("Item" + i));
        }
    }

    /**
     * [Extreme] Tests transferFrom preserves status of all items in a large
     * source.
     */
    @Test
    public void testTransferFromPreservesStatusLargeSource() {
        GroceryPickup source = this.createEmpty();
        int count = 50;
        for (int i = 0; i < count; i++) {
            source.add("Item" + i, "Loc" + i);
            source.setStatus("Item" + i, GroceryPickupKernel.Status.PICKED);
        }

        GroceryPickup dest = this.createEmpty();
        dest.transferFrom(source);

        for (int i = 0; i < count; i++) {
            assertEquals(GroceryPickupKernel.Status.PICKED,
                    dest.getStatus("Item" + i));
        }
    }
}