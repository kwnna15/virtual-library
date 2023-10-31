package se.kwnna.library.datastore;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.kwnna.library.datastore.dto.BookDto;

@Repository
public interface BookRepository extends CrudRepository<BookDto, Integer> {

    Optional<BookDto> findById(Integer id);

    void deleteById(Integer id);

    List<BookDto> findAllByTitleContaining(String title);

    List<BookDto> findAllByAuthorContaining(String author);

    List<BookDto> findAllByIsbnContaining(String isbn);

    List<BookDto> findAllByGenreContaining(String genre);
}
