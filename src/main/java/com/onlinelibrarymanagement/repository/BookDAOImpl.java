package com.onlinelibrarymanagement.repository;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jboss.jandex.Main;

import com.onlinelibrarymanagement.model.Book;

public class BookDAOImpl implements BookDAO {
	private static Connection connection;

	public BookDAOImpl() {
//		try {
//			Properties props = new Properties();
//			FileInputStream in = new FileInputStream("application.properties");
//			props.load(in);
//			in.close();
//
//			String url = props.getProperty("db.url");
//			String username = props.getProperty("db.username");
//			String password = props.getProperty("db.password");
//
//			connection = DriverManager.getConnection(url, username, password);
//		} catch (IOException | SQLException e) {
//			e.printStackTrace();
//		}
		try {
			Properties props = new Properties();
			InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("application.properties");
			props.load(inputStream);
			inputStream.close();

			String url = props.getProperty("db.url");
			String username = props.getProperty("db.username");
			String password = props.getProperty("db.password");

			connection = DriverManager.getConnection(url, username, password);
//			if (connection != null) {
//				connection.close();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void create(Book book) {
		try {
			String query = "INSERT INTO books (name, subjectId, ISBN, isIssued) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, book.getName());
			statement.setInt(2, book.getSubjectId());
			statement.setString(3, book.getISBN());
			statement.setBoolean(4, book.isIssued());
			statement.executeUpdate();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				int id = generatedKeys.getInt(1);
				book.setId(id);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Book getById(int id) {
		try {
			String query = "SELECT * FROM books WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String name = resultSet.getString("name");
				int subjectId = resultSet.getInt("subjectId");
				String ISBN = resultSet.getString("ISBN");
				boolean isIssued = resultSet.getBoolean("isIssued");
				Book book = new Book();
				book.setId(id);
				book.setName(name);
				book.setSubjectId(subjectId);
				book.setISBN(ISBN);
				book.setIssued(isIssued);
				return book;
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Book> getAll() {
		List<Book> books = new ArrayList<>();
		try {
			String query = "SELECT * FROM books";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				int subjectId = resultSet.getInt("subjectId");
				String ISBN = resultSet.getString("ISBN");
				boolean isIssued = resultSet.getBoolean("isIssued");
				Book book = new Book();
				book.setId(id);
				book.setName(name);
				book.setSubjectId(subjectId);
				book.setISBN(ISBN);
				book.setIssued(isIssued);
				books.add(book);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
	}

	@Override
	public void update(Book book) {
		try {
			String query = "UPDATE books SET name = ?, subjectId = ?, ISBN = ?, isIssued = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, book.getName());
			statement.setInt(2, book.getSubjectId());
			statement.setString(3, book.getISBN());
			statement.setBoolean(4, book.isIssued());
			statement.setInt(5, book.getId());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		try {
			String query = "DELETE FROM books WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Book> getAllIssued() {
		List<Book> issuedBooks = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM books WHERE isIssued = true");
			while (resultSet.next()) {
				Book book = new Book();
				book.setId(resultSet.getInt("id"));
				book.setName(resultSet.getString("name"));
				book.setSubjectId(resultSet.getInt("subjectId"));
				book.setISBN(resultSet.getString("ISBN"));
				book.setIssued(resultSet.getBoolean("isIssued"));
				issuedBooks.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return issuedBooks;
	}

	@Override
	public List<Book> searchByName(String name) {
		List<Book> books = new ArrayList<>();

		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM books WHERE name LIKE ?");
			statement.setString(1, "%" + name + "%");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Book book = new Book();
				book.setId(resultSet.getInt("id"));
				book.setName(resultSet.getString("name"));
				book.setSubjectId(resultSet.getInt("subjectId"));
				book.setISBN(resultSet.getString("ISBN"));
				book.setIssued(resultSet.getBoolean("isIssued"));
				books.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return books;
	}

	@Override
	public void deleteAll() {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM books");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
