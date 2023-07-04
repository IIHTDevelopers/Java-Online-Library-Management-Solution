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

import com.onlinelibrarymanagement.model.Subject;

public class SubjectDAOImpl implements SubjectDAO {
	private static Connection connection;

	public SubjectDAOImpl() {
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
	public void create(Subject subject) {
		try {
			String query = "INSERT INTO subjects (name) VALUES (?)";
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, subject.getName());
			statement.executeUpdate();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				int id = generatedKeys.getInt(1);
				subject.setId(id);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Subject getById(int id) {
		try {
			String query = "SELECT * FROM subjects WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String name = resultSet.getString("name");
				Subject subject = new Subject();
				subject.setId(id);
				subject.setName(name);
				return subject;
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Subject> getAll() {
		List<Subject> subjects = new ArrayList<>();
		try {
			String query = "SELECT * FROM subjects";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				Subject subject = new Subject();
				subject.setId(id);
				subject.setName(name);
				subjects.add(subject);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subjects;
	}

	@Override
	public void update(Subject subject) {
		try {
			String query = "UPDATE subjects SET name = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, subject.getName());
			statement.setInt(2, subject.getId());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean delete(int id) {
		try {
			String query = "DELETE FROM subjects WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			statement.executeUpdate();
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Subject> searchByName(String name) {
		List<Subject> subjects = new ArrayList<>();

		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM subjects WHERE name LIKE ?");
			statement.setString(1, "%" + name + "%");
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Subject subject = new Subject();
				subject.setId(resultSet.getInt("id"));
				subject.setName(resultSet.getString("name"));
				subjects.add(subject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return subjects;
	}

	@Override
	public void deleteAll() {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM subjects");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
