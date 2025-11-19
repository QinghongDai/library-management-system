package com.demo.library.controller;

import com.demo.library.dto.CreateBookRequest;
import com.demo.library.entity.Author;
import com.demo.library.entity.Book;
import com.demo.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {
    private BookService bookService;
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        bookService = Mockito.mock(BookService.class);
        bookController = new BookController(bookService);
    }

    @Test
    public void createBook_success() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author1");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor(author);

        when(bookService.createBook(any(CreateBookRequest.class))).thenReturn(book);

        CreateBookRequest request = new CreateBookRequest("Test Book", 1L);
        ResponseEntity<Book> response = bookController.createBook(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Book", response.getBody().getTitle());
        verify(bookService, times(1)).createBook(any(CreateBookRequest.class));
    }

    @Test
    public void createBook_authorNotFound() {
        when(bookService.createBook(any(CreateBookRequest.class)))
                .thenThrow(new RuntimeException("Author not found with id: 999"));

        CreateBookRequest request = new CreateBookRequest("Book X", 999L);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookController.createBook(request));
        assertEquals("Author not found with id: 999", exception.getMessage());
    }

    @Test
    public void createBook_emptyTitle() {
        CreateBookRequest request = new CreateBookRequest("", 1L);

        when(bookService.createBook(request))
                .thenThrow(new IllegalArgumentException("Title is required"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookController.createBook(request));
        assertEquals("Title is required", exception.getMessage());
    }

    @Test
    public void getBookById_success() {
        Author author = new Author();
        author.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor(author);

        when(bookService.getBook(1L)).thenReturn(book);

        ResponseEntity<Book> response = bookController.getBook(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Book", response.getBody().getTitle());
        verify(bookService, times(1)).getBook(1L);
    }

    @Test
    public void getBookById_notFound() {
        when(bookService.getBook(999L)).thenThrow(new RuntimeException("Book not found with id: 999"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookController.getBook(999L));
        assertEquals("Book not found with id: 999", exception.getMessage());
    }

    @Test
    public void getBooksByAuthor_success() {
        Author author = new Author();
        author.setId(1L);

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book1");
        book1.setAuthor(author);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book2");
        book2.setAuthor(author);

        when(bookService.getBooksByAuthor(1L)).thenReturn(List.of(book1, book2));

        ResponseEntity<List<Book>> response = bookController.getBooksByAuthor(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(bookService, times(1)).getBooksByAuthor(1L);
    }

    @Test
    public void getBooksByAuthor_emptyList() {
        when(bookService.getBooksByAuthor(1L)).thenReturn(List.of());

        ResponseEntity<List<Book>> response = bookController.getBooksByAuthor(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void getBooksByAuthor_authorNotFound() {
        when(bookService.getBooksByAuthor(999L))
                .thenThrow(new RuntimeException("Author not found with id: 999"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookController.getBooksByAuthor(999L));
        assertEquals("Author not found with id: 999", exception.getMessage());
    }

    @Test
    public void createBook_serviceThrowsRuntimeException() {
        when(bookService.createBook(any(CreateBookRequest.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        CreateBookRequest request = new CreateBookRequest("Book Y", 1L);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookController.createBook(request));
        assertEquals("Unexpected error", exception.getMessage());
    }
}
