package com.onlinelibrarymanagement.repository;

import java.util.List;

import com.onlinelibrarymanagement.model.Book;

public interface BookDAO {
	void create(Book book);

	Book getById(int id);

	List<Book> getAll();

	void update(Book book);

	void delete(int id);

	List<Book> getAllIssued();

	List<Book> searchByName(String name);

	void deleteAll();

}
