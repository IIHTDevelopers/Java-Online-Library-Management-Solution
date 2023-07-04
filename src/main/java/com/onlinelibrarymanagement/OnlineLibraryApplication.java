package com.onlinelibrarymanagement;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.jboss.jandex.Main;

import com.onlinelibrarymanagement.model.Book;
import com.onlinelibrarymanagement.model.Subject;
import com.onlinelibrarymanagement.repository.BookDAO;
import com.onlinelibrarymanagement.repository.BookDAOImpl;
import com.onlinelibrarymanagement.repository.SubjectDAO;
import com.onlinelibrarymanagement.repository.SubjectDAOImpl;

public class OnlineLibraryApplication {
	private static SubjectDAO subjectDAO;
	private static BookDAO bookDAO;
	private static Properties props;

	public static void main(String[] args) {
		loadDatabaseConfiguration();

		createDatabaseIfNotExists();
		createSubjectTableIfNotExists();
		createBookTableIfNotExists();

		subjectDAO = new SubjectDAOImpl();
		bookDAO = new BookDAOImpl();

		displayMenu();
	}

	public static void loadDatabaseConfiguration() {
		try {
			props = new Properties();
			InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("application.properties");
			props.load(inputStream);
			inputStream.close();

			String url = props.getProperty("db.url");
			String username = props.getProperty("db.username");
			String password = props.getProperty("db.password");

			Connection connection = DriverManager.getConnection(url, username, password);
//			if (connection != null) {
//				connection.close();
//			}
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createDatabaseIfNotExists() {
		try {
			String url = props.getProperty("db.url");
			String username = props.getProperty("db.username");
			String password = props.getProperty("db.password");

			Connection connection = DriverManager.getConnection(url, username, password);
			Statement statement = connection.createStatement();
			statement.executeUpdate("CREATE DATABASE IF NOT EXISTS library_db");
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createSubjectTableIfNotExists() {
		try {
			String url = props.getProperty("db.url");
			String username = props.getProperty("db.username");
			String password = props.getProperty("db.password");

			Connection connection = DriverManager.getConnection(url, username, password);
			Statement statement = connection.createStatement();
			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS subjects (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(10) NOT NULL)");
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createBookTableIfNotExists() {
		try {
			String url = props.getProperty("db.url");
			String username = props.getProperty("db.username");
			String password = props.getProperty("db.password");

			Connection connection = DriverManager.getConnection(url, username, password);
			Statement statement = connection.createStatement();
			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS books (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL, subjectId INT NOT NULL, ISBN VARCHAR(255) NOT NULL, isIssued BOOLEAN NOT NULL)");
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void displayMenu() {
		Scanner scanner = new Scanner(System.in);
		int choice;
		do {
			System.out.println("----- Library Management System Menu -----");
			System.out.println("1. Add Subject");
			System.out.println("2. Update Subject");
			System.out.println("3. Delete Subject");
			System.out.println("4. View All Subjects");
			System.out.println("5. Add Book");
			System.out.println("6. Update Book");
			System.out.println("7. Delete Book");
			System.out.println("8. View All Books");
			System.out.println("9. Search subject by name");
			System.out.println("10. List All Issued Books");
			System.out.println("11. Search Book by Name");
			System.out.println("12. Remove All Subjects");
			System.out.println("13. Remove All Books");
			System.out.println("0. Exit");
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (choice) {
			case 1:
				addSubject();
				break;
			case 2:
				updateSubject();
				break;
			case 3:
				deleteSubject();
				break;
			case 4:
				viewAllSubjects();
				break;
			case 5:
				addBook();
				break;
			case 6:
				updateBook();
				break;
			case 7:
				deleteBook();
				break;
			case 8:
				viewAllBooks();
				break;
			case 9:
				searchSubjectByName();
				break;
			case 10:
				listAllIssuedBooks();
				break;
			case 11:
				searchBookByName();
				break;
			case 12:
				removeAllSubjects();
				break;
			case 13:
				removeAllBooks();
				break;
			case 0:
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
				break;
			}
		} while (choice != 0);

		scanner.close();
	}

	private static void searchSubjectByName() {
		Scanner scanner = new Scanner(System.in);

		scanner.nextLine();
		System.out.println("Enter the subject name: ");
		String name = scanner.nextLine();

		List<Subject> subjects = subjectDAO.searchByName(name);

		if (subjects.isEmpty()) {
			System.out.println("No subjects found with the given name.");
		} else {
			System.out.println("Subjects found:");
			for (Subject subject : subjects) {
				System.out.println("ID: " + subject.getId() + ", Name: " + subject.getName());
			}
		}
	}

	private static void addSubject() {
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		System.out.print("Enter the name of the subject: ");
		String name = scanner.nextLine();

		Subject subject = new Subject();
		subject.setName(name);

		subjectDAO.create(subject);

		System.out.println("Subject added successfully!");
		System.out.println();
	}

	private static void updateSubject() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter the ID of the subject to update: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		Subject subject = subjectDAO.getById(id);
		if (subject != null) {
			System.out.print("Enter the new name of the subject: ");
			String name = scanner.nextLine();

			subject.setName(name);

			subjectDAO.update(subject);

			System.out.println("Subject updated successfully!");
		} else {
			System.out.println("Subject not found with ID: " + id);
		}
		System.out.println();
	}

	private static void deleteSubject() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter the ID of the subject to delete: ");
		int id = scanner.nextInt();

		subjectDAO.delete(id);

		System.out.println("Subject deleted successfully!");
		System.out.println();
	}

	private static void viewAllSubjects() {
		List<Subject> subjects = subjectDAO.getAll();
		if (!subjects.isEmpty()) {
			System.out.println("----- All Subjects -----");
			for (Subject subject : subjects) {
				System.out.println(subject.getId() + ": " + subject.getName());
			}
		} else {
			System.out.println("No subjects found.");
		}
		System.out.println();
	}

	private static void addBook() {
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		System.out.print("Enter the name of the book: ");
		String name = scanner.nextLine();

		System.out.print("Enter the subject ID of the book: ");
		int subjectId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		System.out.print("Enter the ISBN of the book: ");
		String ISBN = scanner.nextLine();

		System.out.print("Is the book issued? (true/false): ");
		boolean isIssued = scanner.nextBoolean();

		Book book = new Book();
		book.setName(name);
		book.setSubjectId(subjectId);
		book.setISBN(ISBN);
		book.setIssued(isIssued);

		bookDAO.create(book);

		System.out.println("Book added successfully!");
		System.out.println();
	}

	private static void updateBook() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter the ID of the book to update: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		Book book = bookDAO.getById(id);
		if (book != null) {
			System.out.print("Enter the new name of the book: ");
			String name = scanner.nextLine();

			System.out.print("Enter the new subject ID of the book: ");
			int subjectId = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			System.out.print("Enter the new ISBN of the book: ");
			String ISBN = scanner.nextLine();

			System.out.print("Is the book issued? (true/false): ");
			boolean isIssued = scanner.nextBoolean();

			book.setName(name);
			book.setSubjectId(subjectId);
			book.setISBN(ISBN);
			book.setIssued(isIssued);

			bookDAO.update(book);

			System.out.println("Book updated successfully!");
		} else {
			System.out.println("Book not found with ID: " + id);
		}
		System.out.println();
	}

	private static void deleteBook() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter the ID of the book to delete: ");
		int id = scanner.nextInt();

		bookDAO.delete(id);

		System.out.println("Book deleted successfully!");
		System.out.println();
	}

	private static void viewAllBooks() {
		List<Book> books = bookDAO.getAll();
		if (!books.isEmpty()) {
			System.out.println("----- All Books -----");
			for (Book book : books) {
				System.out.println(book.getId() + ": " + book.getName());
			}
		} else {
			System.out.println("No books found.");
		}
		System.out.println();
	}

	private static void listAllIssuedBooks() {
		List<Book> issuedBooks = bookDAO.getAllIssued();

		if (issuedBooks.isEmpty()) {
			System.out.println("No issued books found.");
		} else {
			System.out.println("Issued books:");
			for (Book book : issuedBooks) {
				System.out.println("ID: " + book.getId() + ", Name: " + book.getName() + ", ISBN: " + book.getISBN());
			}
		}
	}

	private static void searchBookByName() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the book name: ");
		String name = scanner.nextLine();

		List<Book> books = bookDAO.searchByName(name);

		if (books.isEmpty()) {
			System.out.println("No books found with the given name.");
		} else {
			System.out.println("Books found:");
			for (Book book : books) {
				System.out.println("ID: " + book.getId() + ", Name: " + book.getName() + ", ISBN: " + book.getISBN());
			}
		}
	}

	private static void removeAllSubjects() {
		subjectDAO.deleteAll();
		System.out.println("All subjects have been removed.");
	}

	private static void removeAllBooks() {
		bookDAO.deleteAll();
		System.out.println("All books have been removed.");
	}

}
