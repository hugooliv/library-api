package io.github.hugooliv.libraryapi.service;

import io.github.hugooliv.libraryapi.api.dto.BookDTO;
import io.github.hugooliv.libraryapi.exception.BusinessException;
import io.github.hugooliv.libraryapi.model.entity.Book;
import io.github.hugooliv.libraryapi.model.repository.BookRepository;
import io.github.hugooliv.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void savedBookTest(){
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book)).thenReturn(Book.builder().id(1l).isbn("123").author("Teste").title("O livro do mal").build());

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("O livro do mal");
        assertThat(savedBook.getAuthor()).isEqualTo("Teste");

    }

    @Test
    @DisplayName("Deve mandar erro de negocio com ISBN duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(()-> service.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("ISBN ja cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book);

    }

    public Book createValidBook(){
        return Book.builder().isbn("123").author("Teste").title("O livro do mal").build();
    }

}
