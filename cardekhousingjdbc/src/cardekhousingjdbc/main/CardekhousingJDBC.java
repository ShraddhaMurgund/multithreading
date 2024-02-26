package cardekhousingjdbc.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CardekhousingJDBC {

	private static Connection connection;
	private static PreparedStatement preparedStatement;
	private static String query;
	private static ResultSet resultSet;

	public static void main(String[] args) {
		System.out.println("Enter your choice\nEnter 1 to view all car\n" + "Enter 2 to search car by id\r\n"
				+ "Enter 3 to add  cars\r\n" + "Enter 4 to delete a car\r\n" + "Enter 5 to edit car \r\n"
				+ "Enter 6 to exit");
		Scanner scanner = new Scanner(System.in);
		try {
			openConnection();
			int choice = scanner.nextInt();
			switch (choice) {
			case 1:
				displayAllCar();
				break;
			case 2:
				searchCar(scanner);
				break;
			case 3:
				addCar(scanner);
				break;
			case 4:
				deleteCar(scanner);
				break;
			case 5:
				editCar(scanner);
				break;
			case 6:
				System.out.println("Thank you visit again");
				break;

			default:
				System.out.println("Invalid choice please enter a valid number");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private static void displayAllCar() throws SQLException {
		query = "SELECT * FROM carData;";
		preparedStatement = connection.prepareStatement(query);
		resultSet = preparedStatement.executeQuery();
		System.out.println("+--------+--------------+------------+------------+-------------+");
		System.out.println("| car_id |   car_name   |   brand    |   color    |    price    |");
		System.out.println("+--------+--------------+------------+------------+-------------+");
		while (resultSet.next()) {
			int id = resultSet.getInt(1);
			String name = resultSet.getString(2);
			String brand = resultSet.getString(3);
			String color = resultSet.getString(4);
			int price = resultSet.getInt(5);
			System.out.printf("| %6d | %-12s | %-10s | %-10s | %,11d |%n", id, name, brand, color, price);
		}
		System.out.println("+--------+--------------+------------+------------+-------------+");
		resultSet.close();

	}

	private static void searchCar(Scanner scanner) throws SQLException {

		System.out.println("Enter the car which you want to search");
		int carid = scanner.nextInt();
		query = "SELECT * FROM carData WHERE id = ?";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, carid);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String brand = resultSet.getString("brand");
			String color = resultSet.getString("color");
			int price = resultSet.getInt("price");

			System.out.printf("| %6d | %-12s | %-10s | %-10s | %,11d |%n", id, name, brand, color, price);
		} else {
			System.out.println("Car not found");
		}
		resultSet.close();

	}

	private static void addCar(Scanner scanner) {

		try {

			System.out.println("Enter car id");
			int id = scanner.nextInt();

			System.out.println("Enter car_name:");
			String name = scanner.next();

			System.out.println("Enter brand:");
			String brand = scanner.next();

			System.out.println("Enter color:");
			String color = scanner.next();

			System.out.println("Enter price:");
			int price = scanner.nextInt();

			query = "INSERT INTO carData(id,name,brand,color,price) VALUES(?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, brand);
			preparedStatement.setString(4, color);
			preparedStatement.setInt(5, price);
			preparedStatement.execute();
			System.out.println("Data Inserted");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void deleteCar(Scanner scanner) throws SQLException {

		System.out.println("Enter car id which you want to remove");
		int id = scanner.nextInt();
		query = "DELETE FROM carData WHERE id = ?";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, id);
		int res = preparedStatement.executeUpdate();
		if (res > 0) {
			System.out.println("Car removed successfully");
		} else {
			System.out.println("Car not found");
		}

	}

	private static void editCar(Scanner scanner) throws SQLException {

		System.out.println("Enter the car id which you want to update");
		int id = scanner.nextInt();

		System.out.println("Enter New_car_name:");
		String name = scanner.next();

		System.out.println("Enter New_brand:");
		String brand = scanner.next();

		System.out.println("Enter New_color:");
		String color = scanner.next();

		System.out.println("Enter New_price:");
		int price = scanner.nextInt();

		query = "UPDATE carData SET name = ?,brand = ?,color = ?,price = ? WHERE id = ?";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(5, id);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, brand);
		preparedStatement.setString(3, color);
		preparedStatement.setInt(4, price);
		preparedStatement.executeUpdate();
		System.out.println("Car details updated successfully");

	}

	private static void openConnection() throws SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/weja4", "root", "root");
	}

	private static void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
		}
		if (preparedStatement != null) {
			preparedStatement.close();
		}

	}
}
