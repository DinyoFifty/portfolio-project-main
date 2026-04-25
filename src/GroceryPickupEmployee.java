import components.map.Map;
import components.sequence.Sequence;

/**
 * A simple demonstration of GroceryPickup being used as an employee-facing
 * order picking assistant. The employee walks through the store aisle by aisle,
 * marking items as picked, substituted, or out of stock along the way.
 *
 * <p>
 * Use case: A grocery store employee receives a customer order and uses the
 * picking path to fulfill it efficiently. Items are grouped by aisle so the
 * employee does not backtrack. As the employee picks, they update statuses and
 * handle exceptions like substitutions or missing stock.
 * </p>
 */
public final class GroceryPickupEmployee {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private GroceryPickupEmployee() {
    }

    /**
     * Simulates an employee picking a customer grocery order.
     *
     * @param args
     *            command-line arguments (not used)
     */
    public static void main(String[] args) {

        // Build the customer's order
        GroceryPickup order = new GroceryPickup1L();
        order.add("Whole Milk", "A1");
        order.add("Cheddar Cheese", "A1");
        order.add("Greek Yogurt", "A2");
        order.add("Sourdough Bread", "B1");
        order.add("Wheat Bread", "B1");
        order.add("Chicken Breast", "C3");
        order.add("Ground Beef", "C3");
        order.add("Orange Juice", "D2");

        System.out.println("=== Order Received ===");
        System.out.println(order);
        System.out.println();

        // Generate the picking path so the employee can walk aisle by aisle
        System.out.println("=== Picking Path ===");
        Map<String, Sequence<String>> path = order.getPickingPath();
        for (Map.Pair<String, Sequence<String>> aisle : path) {
            System.out.print("Aisle " + aisle.key() + ": ");
            for (int i = 0; i < aisle.value().length(); i++) {
                System.out.print(aisle.value().entry(i));
                if (i < aisle.value().length() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
        System.out.println();

        // Simulate the employee moving through the store
        System.out.println("=== Picking In Progress ===");

        // Aisle A1 - dairy
        order.setStatus("Whole Milk", GroceryPickupKernel.Status.PICKED);
        System.out.println("Picked: Whole Milk");

        // Cheddar Cheese is out of stock, substitute with Colby Jack
        order.substitute("Cheddar Cheese", "Colby Jack Cheese");
        System.out.println(
                "Cheddar Cheese out of stock -> substituted with Colby Jack Cheese");

        // Aisle A2
        order.setStatus("Greek Yogurt", GroceryPickupKernel.Status.PICKED);
        System.out.println("Picked: Greek Yogurt");

        // Aisle B1 - bread
        order.setStatus("Sourdough Bread", GroceryPickupKernel.Status.PICKED);
        System.out.println("Picked: Sourdough Bread");

        order.markOutOfStock("Wheat Bread");
        System.out.println("Wheat Bread marked out of stock");

        // Aisle C3 - meat
        order.setStatus("Chicken Breast", GroceryPickupKernel.Status.PICKED);
        System.out.println("Picked: Chicken Breast");

        order.setStatus("Ground Beef", GroceryPickupKernel.Status.PICKED);
        System.out.println("Picked: Ground Beef");

        // Aisle D2
        order.setStatus("Orange Juice", GroceryPickupKernel.Status.PICKED);
        System.out.println("Picked: Orange Juice");

        // Pick the substitute item
        order.setStatus("Colby Jack Cheese", GroceryPickupKernel.Status.PICKED);
        System.out.println("Picked: Colby Jack Cheese (substitute)");

        System.out.println();

        // Check if the order is ready for the customer
        System.out.println("=== Order Status ===");
        System.out.println(order);
        System.out.println();

        if (order.isOrderComplete()) {
            System.out.println("Order is complete! Ready for customer pickup.");
        } else {
            System.out.println(
                    "Order still has pending items. Continue picking.");
        }
    }
}