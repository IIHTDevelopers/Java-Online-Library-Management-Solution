package com.onlinelibrarymanagement.repository;

import java.util.List;

import com.onlinelibrarymanagement.model.Subject;

public interface SubjectDAO {
	void create(Subject subject);

	Subject getById(int id);

	List<Subject> getAll();

	void update(Subject subject);

	boolean delete(int id);

	List<Subject> searchByName(String name);

	void deleteAll();

}
