/**
 * A demonstration of GroceryPickup being used as a manager.The manager oversees
 * multiple customer orders at once, checks which ones are complete, identifies
 * bottlenecks, and reassigns or resolves outstanding items across all active
 * orders.
 */
public final class GroceryPickupManager {

    /**
     * Private constructor.
     */
    private GroceryPickupManager() {
    }

    /**
     * Simulates a manager reviewing and resolving a batch of active orders.
     *
     * @param args
     */
    public static void main(String[] args) {

        // Set up several customer orders simultaneously
        GroceryPickup orderA = new GroceryPickup1L();
        orderA.add("Almond Milk", "A1");
        orderA.add("Eggs", "A2");
        orderA.add("Bacon", "C1");
        orderA.setStatus("Almond Milk", GroceryPickupKernel.Status.PICKED);
        orderA.setStatus("Eggs", GroceryPickupKernel.Status.PICKED);
        // Bacon still PENDING

        GroceryPickup orderB = new GroceryPickup1L();
        orderB.add("Pasta", "B3");
        orderB.add("Marinara Sauce", "B3");
        orderB.add("Parmesan", "A1");
        orderB.setStatus("Pasta", GroceryPickupKernel.Status.PICKED);
        orderB.markOutOfStock("Marinara Sauce");
        orderB.setStatus("Parmesan", GroceryPickupKernel.Status.PICKED);

        GroceryPickup orderC = new GroceryPickup1L();
        orderC.add("Sparkling Water", "D1");
        orderC.add("Granola Bars", "B2");
        orderC.add("Peanut Butter", "B2");
        orderC.setStatus("Sparkling Water", GroceryPickupKernel.Status.PICKED);
        orderC.setStatus("Granola Bars", GroceryPickupKernel.Status.PICKED);
        orderC.setStatus("Peanut Butter", GroceryPickupKernel.Status.PICKED);

        // Store orders in an array for batch processing
        GroceryPickup[] orders = { orderA, orderB, orderC };
        String[] orderNames = { "Order A (Customer 101)",
                "Order B (Customer 102)", "Order C (Customer 103)" };

        // Manager dashboard summary
        System.out.println("========================================");
        System.out.println("     MANAGER ORDER DASHBOARD");
        System.out.println("========================================");
        System.out.println();

        int completeCount = 0;
        int pendingOrderCount = 0;

        for (int i = 0; i < orders.length; i++) {
            GroceryPickup order = orders[i];
            System.out.println("--- " + orderNames[i] + " ---");
            System.out.println(order);

            if (order.isOrderComplete()) {
                System.out.println("Status: COMPLETE - Ready for pickup");
                completeCount++;
            } else {
                System.out.println("Status: INCOMPLETE - Needs attention");
                pendingOrderCount++;
            }
            System.out.println();
        }

        // Overall summary
        System.out.println("========================================");
        System.out.println("SUMMARY");
        System.out.println("Total orders:    " + orders.length);
        System.out.println("Complete:        " + completeCount);
        System.out.println("Needs attention: " + pendingOrderCount);
        System.out.println();

        // Manager resolves Order A by marking Bacon out of stock and
        // substituting it, then confirms the order is now complete
        System.out.println("========================================");
        System.out.println("MANAGER ACTION: Resolving Order A");
        System.out
                .println("Bacon unavailable -> substituting with Turkey Bacon");
        orderA.substitute("Bacon", "Turkey Bacon");
        orderA.setStatus("Turkey Bacon", GroceryPickupKernel.Status.PICKED);

        System.out.println("Order A updated: " + orderA);
        System.out.println();

        // Manager resolves Order B by substituting the out-of-stock item
        System.out.println("MANAGER ACTION: Resolving Order B");
        System.out.println(
                "Marinara Sauce out of stock -> substituting with Tomato Basil Sauce");
        orderB.substitute("Marinara Sauce", "Tomato Basil Sauce");
        orderB.setStatus("Tomato Basil Sauce",
                GroceryPickupKernel.Status.PICKED);

        System.out.println("Order B updated: " + orderB);
        System.out.println();

        // Final check - all orders should now be complete
        System.out.println("========================================");
        System.out.println("FINAL STATUS CHECK");

        boolean allDone = true;
        for (int i = 0; i < orders.length; i++) {
            boolean done = orders[i].isOrderComplete();
            System.out.println(
                    orderNames[i] + ": " + (done ? "COMPLETE" : "INCOMPLETE"));
            if (!done) {
                allDone = false;
            }
        }
        System.out.println();
        if (allDone) {
            System.out.println(
                    "All orders complete! Notify customers for pickup.");
        } else {
            System.out.println("Some orders still need attention.");
        }
    }
}
