/**
 * CS3700 - Homework 2
 * 
 * This file is used by UDPServer.java to create a table of data and reference its contents.
 * 
 * Required file(s): UDPServer.java
 * 
 * @author Dustin Fay
 */
import java.util.*;

/**
 * Inventory class uses calls a method to store list of inventory.
 */
public class Inventory {
	
	// The items.
	private List<Inventory> itemList;
        // The resource id
        String id;
        // The resource description.
        private String description;
        // The resource price
        private double price;
        // The number available in inventory
        private int quantity;
            
	/**
	 * Instantiates a new list and populates with specified values.
	 */
	public Inventory() {
		itemList = new ArrayList<Inventory>();
		// Custom inventory class accepts parameters (id, description, price, quantity)
		itemList.add(new Inventory("00001", "New Inspiron 15", 379.99, 157));
		itemList.add(new Inventory("00002", "New Inspiron 17", 449.99, 128));
		itemList.add(new Inventory("00003", "New Inspiron 15R", 549.99, 202));
		itemList.add(new Inventory("00004", "New Inspiron 15z Ultrabook", 749.99, 315));
		itemList.add(new Inventory("00005", "XPS 14 Ultrabook", 999.99, 261));
		itemList.add(new Inventory("00006", "New XPS 12 UltrabookXPS", 1199.99, 178));
	}

	/**
	 * Gets the item.
	 *
	 * @param id the item id
	 * @return all of the items attributes if found, otherwise an error message
	 */
	public String getItem(String id) {
		for (int i = 0; i < itemList.size(); i++) {
			if (id.equals(itemList.get(i).getId())) {
				return toFullString(itemList.get(i));
			}
		}
		return "Item id not found. Please try another id.\n";
	}

	/**
         * 
	 * @return item id and description for all items in list
	 */
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("Item ID\t\tItem Description\n");
		for (Inventory item : itemList) {
			strBuilder.append(item.getId() + "\t\t" + item.getDescription() + "\n");
		}
		return strBuilder.toString();
	}

	/**
	 * Accepts an item id and concatenates all related information into a single string.
	 * @return the string with all of the resource's attributes
	 */
	public String toFullString(Inventory item) {
		StringBuilder builder = new StringBuilder();
		return builder.append("ID: " + item.getId() + "\tItem Description: "
				+ item.getDescription() + "\t\tUnit Price: $"
				+ item.getPrice() + "\t\tInventory: "
				+ item.getQuantity()).toString()+ "\n";
	}

	/**
	 * Instantiates a new inventory item.
	 * @param id the id
	 * @param desc the description
	 * @param price the price
	 * @param quantity the quantity
	 */
	public Inventory(String id, String desc, double price, int quantity) {
                this.id = id;
		this.description = desc;
		this.price = price;
		this.quantity = quantity;
	}

	/**
	 * Accessors to return Inventory parameters.
	 */
	public String getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public double getPrice() {
		return price;
	}
	public int getQuantity() {
		return quantity;
	}

} //end Inventory
