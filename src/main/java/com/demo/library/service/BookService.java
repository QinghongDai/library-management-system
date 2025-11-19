package com.demo.library.service;

import com.demo.library.repository.AuthorRepository;
import com.demo.library.repository.BookRepository;
import com.demo.library.dto.CreateBookRequest;
import com.demo.library.entity.Author;
import com.demo.library.entity.Book;
import com.demo.library.exceptions.AuthorNotFoundException;
import com.demo.library.exceptions.BookNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BookService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Book createBook(CreateBookRequest request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException(request.getAuthorId()));

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Book getBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId);
    }
}
