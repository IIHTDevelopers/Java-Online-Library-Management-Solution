package com.assessment.onlinelibrarymanagement.functional;

import static com.assessment.onlinelibrarymanagement.testutils.TestUtils.businessTestFile;
import static com.assessment.onlinelibrarymanagement.testutils.TestUtils.currentTest;
import static com.assessment.onlinelibrarymanagement.testutils.TestUtils.testReport;
import static com.assessment.onlinelibrarymanagement.testutils.TestUtils.yakshaAssert;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.jboss.jandex.Main;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import com.onlinelibrarymanagement.OnlineLibraryApplication;
import com.onlinelibrarymanagement.model.Book;
import com.onlinelibrarymanagement.model.Subject;
import com.onlinelibrarymanagement.repository.BookDAO;
import com.onlinelibrarymanagement.repository.BookDAOImpl;
import com.onlinelibrarymanagement.repository.SubjectDAO;
import com.onlinelibrarymanagement.repository.SubjectDAOImpl;

@Component
public class FunctionalTests {

	private BookDAO bookDAO;
	private SubjectDAO subjectDAO;

	@BeforeEach
	public void setUp() {
		OnlineLibraryApplication mainObj = new OnlineLibraryApplication();
		Properties properties = new Properties();

		try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
			properties.load(inputStream);

			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String password = properties.getProperty("jdbc.password");

			mainObj.loadDatabaseConfiguration();
			mainObj.createDatabaseIfNotExists();
			mainObj.createBookTableIfNotExists();
			mainObj.createSubjectTableIfNotExists();

			bookDAO = new BookDAOImpl();
			subjectDAO = new SubjectDAOImpl();
//			dishDAO.removeAllDishes();
//			restaurantDAO.deleteAllRestaurants();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	void createAndGetById() throws IOException {
		Subject subject = new Subject();
		subject.setName("Maths");
		subjectDAO.create(subject);

		int subjectId = subject.getId();
		Subject fetchedSubject = subjectDAO.getById(subjectId);

//		assertNotNull(fetchedSubject);
//		assertEquals(subject.getName(), fetchedSubject.getName());
		try {
			yakshaAssert(currentTest(),
					fetchedSubject != null && fetchedSubject.getName().equals(subject.getName()) ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	void getAll() throws IOException {
		subjectDAO.deleteAll();

		Subject subject1 = new Subject();
		subject1.setName("English");
		subjectDAO.create(subject1);

		Subject subject2 = new Subject();
		subject2.setName("Science");
		subjectDAO.create(subject2);

		Subject subject3 = new Subject();
		subject3.setName("History");
		subjectDAO.create(subject3);

		List<Subject> subjects = subjectDAO.getAll();

//		assertEquals(3, subjects.size());
		try {
			yakshaAssert(currentTest(), subjects.size() == 3 ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	void update() throws IOException {
		Subject subject = new Subject();
		subject.setName("Physics");
		subjectDAO.create(subject);

		subject.setName("Chemistry");
		subjectDAO.update(subject);

		Subject updatedSubject = subjectDAO.getById(subject.getId());

//		assertEquals("Chemistry", updatedSubject.getName());
		try {
			yakshaAssert(currentTest(), updatedSubject.getName().equals("Chemistry") ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	void delete() throws IOException {
		Subject subject = new Subject();
		subject.setName("Geography");
		subjectDAO.create(subject);

		boolean isDeleted = subjectDAO.delete(subject.getId());

		Subject fetchedSubject = subjectDAO.getById(subject.getId());
//		assertNull(fetchedSubject);
		try {
			yakshaAssert(currentTest(), isDeleted == true && fetchedSubject == null ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	void searchByName() throws IOException {
		subjectDAO.deleteAll();

		Subject subject1 = new Subject();
		subject1.setName("Biology");
		subjectDAO.create(subject1);

		Subject subject2 = new Subject();
		subject2.setName("Maths");
		subjectDAO.create(subject2);

		Subject subject3 = new Subject();
		subject3.setName("Physics");
		subjectDAO.create(subject3);

		List<Subject> searchResults = subjectDAO.searchByName("math");

//		assertEquals(1, searchResults.size());
//		assertEquals("Mathematics", searchResults.get(0).getName());
		try {
			yakshaAssert(currentTest(),
					searchResults.size() == 1 && searchResults.get(0).getName().equals("Maths") ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	void createAndGetBookById() throws IOException {
		Book book = new Book();
		book.setName("Java");
		book.setSubjectId(1);
		book.setISBN("9781234567890");
		book.setIssued(false);
		bookDAO.create(book);

		int bookId = book.getId();
		Book fetchedBook = bookDAO.getById(bookId);

//        assertNotNull(fetchedBook);
//        assertEquals(book.getName(), fetchedBook.getName());
//        assertEquals(book.getSubjectId(), fetchedBook.getSubjectId());
//        assertEquals(book.getISBN(), fetchedBook.getISBN());
//        assertEquals(book.isIssued(), fetchedBook.isIssued());
		try {
			yakshaAssert(currentTest(), fetchedBook != null & book.getName().equals(fetchedBook.getName())
					&& book.getISBN().equals(fetchedBook.getISBN()) ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	void getAllBooks() throws IOException {
		bookDAO.deleteAll();

		Book book1 = new Book();
		book1.setName("Python");
		book1.setSubjectId(2);
		book1.setISBN("9780987654321");
		book1.setIssued(false);
		bookDAO.create(book1);

		Book book2 = new Book();
		book2.setName("DSA");
		book2.setSubjectId(1);
		book2.setISBN("9789876543210");
		book2.setIssued(false);
		bookDAO.create(book2);

		List<Book> books = bookDAO.getAll();
//		assertEquals(2, books.size());
		try {
			yakshaAssert(currentTest(), books.size() == 2 ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	void updateBook() throws IOException {
		Book book = new Book();
		book.setName("Web");
		book.setSubjectId(3);
		book.setISBN("9785678901234");
		book.setIssued(false);
		bookDAO.create(book);

		book.setName("FE");
		book.setIssued(true);
		bookDAO.update(book);

		Book updatedBook = bookDAO.getById(book.getId());

//		assertEquals("Frontend Development", updatedBook.getName());
//		assertTrue(updatedBook.isIssued());
		try {
			yakshaAssert(currentTest(), updatedBook.getName().equals("FE") ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	void deleteBook() throws IOException {
		Book book = new Book();
		book.setName("DB Sys");
		book.setSubjectId(2);
		book.setISBN("9785432109876");
		book.setIssued(false);
		bookDAO.create(book);

		bookDAO.delete(book.getId());

		Book fetchedBook = bookDAO.getById(book.getId());
//		assertNull(fetchedBook);
		try {
			yakshaAssert(currentTest(), fetchedBook == null ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	void searchBookByName() throws IOException {
		bookDAO.deleteAll();

		Book book1 = new Book();
		book1.setName("Java");
		book1.setSubjectId(1);
		book1.setISBN("9781234567890");
		book1.setIssued(false);
		bookDAO.create(book1);

		Book book2 = new Book();
		book2.setName("Python");
		book2.setSubjectId(2);
		book2.setISBN("9780987654321");
		book2.setIssued(false);
		bookDAO.create(book2);

		Book book3 = new Book();
		book3.setName("Web");
		book3.setSubjectId(3);
		book3.setISBN("9785678901234");
		book3.setIssued(false);
		bookDAO.create(book3);

		List<Book> searchResults = bookDAO.searchByName("Java");

//		assertEquals(1, searchResults.size());
//		assertEquals("Java Programming", searchResults.get(0).getName());
		try {
			yakshaAssert(currentTest(),
					searchResults.size() == 1 && searchResults.get(0).getName().equals("Java") ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	void getAllIssuedBooks() throws IOException {
		bookDAO.deleteAll();

		Book book1 = new Book();
		book1.setName("Java");
		book1.setSubjectId(1);
		book1.setISBN("9781234567890");
		book1.setIssued(false);
		bookDAO.create(book1);

		Book book2 = new Book();
		book2.setName("Python");
		book2.setSubjectId(2);
		book2.setISBN("9780987654321");
		book2.setIssued(true);
		bookDAO.create(book2);

		Book book3 = new Book();
		book3.setName("Web");
		book3.setSubjectId(3);
		book3.setISBN("9785678901234");
		book3.setIssued(true);
		bookDAO.create(book3);

		List<Book> issuedBooks = bookDAO.getAllIssued();

//		assertEquals(2, issuedBooks.size());
		try {
			yakshaAssert(currentTest(), issuedBooks.size() == 2 ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

}
