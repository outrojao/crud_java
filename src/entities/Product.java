package entities;

public class Product {

	private static int generalId = 0;
	private int id = ++generalId;
	private String name;
	private double price;
	private int quantity;
	
	public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
	
	public Product(int id, String name, double price, int quantity) {
		this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

	public double TotalValueInStock() {
		return (double) price * quantity;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String toString() {
		return "ID: " + this.id + ", NAME: " + this.name + ", PRICE: " + this.price + ", QUANTITY: " + this.quantity;
	}

}