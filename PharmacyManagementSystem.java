import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PharmacyManagementSystem extends JFrame {
    private JTabbedPane tabbedPane;
    private InventoryPanel inventoryPanel;
    private SalesPanel salesPanel;

    private List<Medicine> inventory;

    public PharmacyManagementSystem() {
        setTitle("Pharmacy Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center the frame

        // Initialize inventory
        inventory = new ArrayList<>();
        inventory.add(new Medicine("Paracetamol", 100, 5.0));
        inventory.add(new Medicine("Amoxicillin", 50, 10.0));

        // Initialize tabs
        tabbedPane = new JTabbedPane();
        inventoryPanel = new InventoryPanel();
        salesPanel = new SalesPanel();
        tabbedPane.addTab("Inventory", inventoryPanel);
        tabbedPane.addTab("Sales", salesPanel);

        // Add tabbed pane to frame
        add(tabbedPane);

        setVisible(true);
    }

    // Inner class for managing inventory panel
    class InventoryPanel extends JPanel {
        private DefaultListModel<Medicine> inventoryListModel;
        private JList<Medicine> inventoryList;

        public InventoryPanel() {
            setLayout(new BorderLayout());

            // Initialize inventory list model and list
            inventoryListModel = new DefaultListModel<>();
            inventory.forEach(inventoryListModel::addElement); // Load initial inventory
            inventoryList = new JList<>(inventoryListModel);
            JScrollPane scrollPane = new JScrollPane(inventoryList);
            add(scrollPane, BorderLayout.CENTER);

            // Panel for adding new medicine
            JPanel addMedicinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JTextField nameField = new JTextField(10);
            JTextField quantityField = new JTextField(5);
            JTextField priceField = new JTextField(5);
            JButton addButton = new JButton("Add Medicine");

            addMedicinePanel.add(new JLabel("Name:"));
            addMedicinePanel.add(nameField);
            addMedicinePanel.add(new JLabel("Quantity:"));
            addMedicinePanel.add(quantityField);
            addMedicinePanel.add(new JLabel("Price:"));
            addMedicinePanel.add(priceField);
            addMedicinePanel.add(addButton);

            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    double price = Double.parseDouble(priceField.getText());
                    Medicine medicine = new Medicine(name, quantity, price);
                    inventoryListModel.addElement(medicine);
                    inventory.add(medicine); // Add to actual inventory list
                    nameField.setText("");
                    quantityField.setText("");
                    priceField.setText("");
                }
            });

            add(addMedicinePanel, BorderLayout.SOUTH);
        }
    }

    // Inner class for managing sales panel
    class SalesPanel extends JPanel {
        private DefaultListModel<Medicine> cartListModel;
        private JList<Medicine> cartList;

        public SalesPanel() {
            setLayout(new BorderLayout());

            // Initialize cart list model and list
            cartListModel = new DefaultListModel<>();
            cartList = new JList<>(cartListModel);
            JScrollPane scrollPane = new JScrollPane(cartList);
            add(scrollPane, BorderLayout.CENTER);

            // Panel for adding medicine to cart
            JPanel addMedicinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JTextField nameField = new JTextField(10);
            JButton addButton = new JButton("Add to Cart");

            addMedicinePanel.add(new JLabel("Medicine Name:"));
            addMedicinePanel.add(nameField);
            addMedicinePanel.add(addButton);

            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameField.getText();
                    Medicine medicine = findMedicineInInventory(name);
                    if (medicine != null) {
                        cartListModel.addElement(medicine);
                        nameField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(SalesPanel.this,
                                "Medicine not found in inventory!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // Panel for checkout
            JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton checkoutButton = new JButton("Checkout");

            checkoutPanel.add(checkoutButton);

            checkoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double totalPrice = calculateTotalPrice();
                    JOptionPane.showMessageDialog(SalesPanel.this,
                            "Total Price: $" + totalPrice,
                            "Checkout", JOptionPane.INFORMATION_MESSAGE);
                    cartListModel.removeAllElements(); // Clear the cart after checkout
                }
            });

            add(addMedicinePanel, BorderLayout.NORTH);
            add(checkoutPanel, BorderLayout.SOUTH);
        }

        // Method to find medicine in inventory
        private Medicine findMedicineInInventory(String name) {
            for (Medicine med : inventory) {
                if (med.getName().equalsIgnoreCase(name)) {
                    return med;
                }
            }
            return null;
        }

        // Method to calculate total price of items in cart
        private double calculateTotalPrice() {
            double totalPrice = 0;
            for (int i = 0; i < cartListModel.size(); i++) {
                totalPrice += cartListModel.getElementAt(i).getPrice();
            }
            return totalPrice;
        }
    }

    // Medicine class
    static class Medicine {
        private String name;
        private int quantity;
        private double price;

        public Medicine(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Medicine: " + name + ", Quantity: " + quantity + ", Price: $" + price;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PharmacyManagementSystem());
    }
}
