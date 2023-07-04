package com.assessment.onlinelibrarymanagement.testutils;

import java.util.ArrayList;
import java.util.List;

import com.onlinelibrarymanagement.model.Book;
import com.onlinelibrarymanagement.model.Subject;

public class MasterData {
	public static Book getBook() {
		Book book = new Book(1, "Java By ABC", 1, "AHGBC123JH", false);
		return book;
	}

	public static List<Book> getBookList() {
		List<Book> books = new ArrayList<>();

		Book book = new Book(1, "Java By ABC", 1, "AHGBC123JH", false);
		books.add(book);

		book = new Book(2, "C++ By XYZ", 2, "AH234ASDFJH", false);
		books.add(book);

		return books;
	}

	public static Subject getSubject() {
		Subject subject = new Subject(1, "Java");
		return subject;
	}

	public static List<Subject> getSubjectList() {
		List<Subject> subjects = new ArrayList<>();

		Subject subject = new Subject(1, "Java");
		subjects.add(subject);

		subject = new Subject(2, "C++");
		subjects.add(subject);

		return subjects;
	}
}
