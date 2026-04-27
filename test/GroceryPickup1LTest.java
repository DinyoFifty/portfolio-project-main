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
     * Remove boundary case leaving one item remaining among two items.
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
     * Remove extreme case removing all items in a large order.
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
     * setStatus normal case testing status changes from pending to picked.
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
     * setStaus boundary case testing setting the status twice on the same item.
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
     * setStatus extreme case setting the status for items in a large order.
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
     * getStatus normal case returns PENDING for a newly added item.
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
     * getStatus boundary case doesn't change the order.
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
     * [Normal] Tests hasItem returns false on an empty order.
     */
    @Test
    public void testHasItemEmptyOrder() {
        GroceryPickup order = this.createEmpty();

        assertFalse(order.hasItem("Milk"));
    }




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



    /**
     * [Normal] Tests size is zero on an empty order.
     */
    @Test
    public void testSizeEmptyOrder() {
        GroceryPickup order = this.createEmpty();

        assertEquals(0, order.size());
    }



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
}