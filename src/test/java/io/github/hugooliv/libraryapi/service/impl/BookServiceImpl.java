package io.github.hugooliv.libraryapi.service.impl;

import io.github.hugooliv.libraryapi.exception.BusinessException;
import io.github.hugooliv.libraryapi.model.entity.Book;
import io.github.hugooliv.libraryapi.model.repository.BookRepository;
import io.github.hugooliv.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;
    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw new BusinessException("ISBN ja cadastrado.");
        }
        return repository.save(book);
    }
}
