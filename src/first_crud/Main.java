package first_crud;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import entities.Product;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {

	public static void saveProducts(List<Product> products) {
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("products.txt"))) {
			bufferedWriter.write("PRODUCTS LIST:");
			bufferedWriter.newLine();
			for (Product product : products) {
				bufferedWriter.write(product.toString());
				bufferedWriter.newLine();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void loadProducts(String filePath, List<Product> products) {
		final String PRODUCT_REGEX = "ID:\\s*(\\d+),\\s*NAME:\\s*([^,]+),\\s*PRICE:\\s*(\\d+\\.?\\d*),\\s*QUANTITY:\\s*(\\d+)";
		final Pattern PATTERN = Pattern.compile(PRODUCT_REGEX);

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();

				if (line.isEmpty() || line.startsWith("PRODUCTS LIST:")) {
					continue;
				}

				Matcher matcher = PATTERN.matcher(line);
				if (matcher.matches()) {
					int id = Integer.parseInt(matcher.group(1));
					String name = matcher.group(2).trim();
					double price = Double.parseDouble(matcher.group(3));
					int quantity = Integer.parseInt(matcher.group(4));

					products.add(new Product(id, name, price, quantity));
				} else {
					System.err.println("INVALID TYPE: " + line);
				}
			}
		} catch (IOException e) {
			System.err.println("CANT READ THE FILE: " + e.getMessage());
		}
	}

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		List<Product> products = new ArrayList<>();
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("MENU:");
			System.out.println("1 - Add new product");
			System.out.println("2 - Remove product");
			System.out.println("3 - Edit product's info");
			System.out.println("4 - List all products");
			System.out.println("5 - Load existing products");
			System.out.println("0 - Exit");

			int option = -1;

			try {
				option = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("PLEASE ENTER A VALID MENU OPTION!");
				sc.nextLine();
			}

			System.out.println();

			switch (option) {
			case 1:
				String name;
				double price;
				int quantity;
				while (true) {
					try {
						System.out.print("NAME: ");
						name = sc.nextLine();

						if (name.equals("")) {
							throw new IllegalArgumentException("THE NAME OF THE PRODUCT SHOULDN'T BE EMPTY!");
						}

						System.out.print("PRICE: ");
						price = sc.nextDouble();

						if (price < 0) {
							throw new IllegalArgumentException("THE PRICE OF THE PRODUCT SHOULDN'T BE NEGATIVE!");
						}

						System.out.print("QUANTITY: ");
						quantity = sc.nextInt();

						if (quantity < 0) {
							throw new IllegalArgumentException("THE QUANTITY OF THE PRODUCT SHOULDN'T BE NEGATIVE!");
						}
						sc.nextLine();
						break;
					} catch (InputMismatchException e) {
						System.out.println("PLEASE ENTER THE CORRECT TYPE DATA FOR THE NEW PRODUCT!");
						sc.nextLine();
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
					}
				}

				products.add(new Product(name, price, quantity));

				System.out.printf("%n%s WAS ADD%n%n", name.toUpperCase());
				break;
			case 2:
				if (!products.isEmpty()) {
					System.out.print("ENTER THE ID OF THE PRODUCT THAT SHOULD BE REMOVED: ");
					int productRemovedId = sc.nextInt();
					Product productRemoved = null;

					for (Product product : products) {
						if (product.getId() == productRemovedId) {
							productRemoved = product;
						}
					}

					System.out.printf("%n%s WAS REMOVED%n%n", productRemoved.getName().toUpperCase());
					products.remove(products.indexOf(productRemoved));
				} else {
					System.out.println("YOUR PRODUCTS LIST ARE EMPTY!\n");
				}
				break;
			case 3:
				if (products.isEmpty()) {
					System.out.println("YOUR PRODUCTS LIST ARE EMPTY!\n");
				} else {
					System.out.print("ENTER THE ID OF THE PRODUCT THAT SHOULD BE EDITED: ");
					int productEditedId = sc.nextInt();
					Product productEdited = null;

					for (Product product : products) {
						if (product.getId() == productEditedId) {
							productEdited = product;
						}
					}

					System.out.println();
					System.out.println("DATA TO EDIT:");
					System.out.println("1 - Name");
					System.out.println("2 - Price");
					System.out.println("3 - Quantity");
					System.out.println("0 - Exit");

					int dataOption = -1;

					try {
						dataOption = sc.nextInt();
						sc.nextLine();
					} catch (InputMismatchException e) {
						System.out.println("PLEASE ENTER A VALID MENU OPTION!");
						sc.nextLine();
					}

					System.out.println();

					switch (dataOption) {
					case 1:
						System.out.printf("ENTER THE NEW NAME: ");
						String newName = sc.nextLine();
						productEdited.setName(newName);
						break;
					case 2:
						System.out.printf("ENTER THE NEW PRICE: ");
						double newPrice = sc.nextDouble();
						productEdited.setPrice(newPrice);
						break;
					case 3:
						System.out.printf("ENTER THE NEW QUANTITY: ");
						int newQuantity = sc.nextInt();
						productEdited.setQuantity(newQuantity);
						break;
					case 0:
						System.out.println();
						break;
					}
					System.out.printf("%nPRODUCT %d WAS EDITED%n%n", productEdited.getId());
				}
				break;
			case 4:
				if (!products.isEmpty()) {
					System.out.println("YOUR PRODUCTS LIST:");
					for (Product product : products) {
						System.out.println(product);
					}
					System.out.println();
				} else {
					System.out.println("YOUR PRODUCTS LIST ARE EMPTY!\n");
				}
				break;
			case 5:
				System.out.print("TYPE THE FILE PATH: ");
				String filePath = sc.nextLine();
				File file = new File(filePath);
				if (!file.exists()) {
					System.out.println("FILE NOT FOUND: " + filePath);
				} else {
					loadProducts(filePath, products);
				}
				System.out.println();
				break;
			case 0:
				sc.close();
				saveProducts(products);
				System.out.println("EXITING PROGRAM...");
				return;
			default:
				System.out.println("INVALID MENU OPTION!");
				break;
			}
		}
	}
}
