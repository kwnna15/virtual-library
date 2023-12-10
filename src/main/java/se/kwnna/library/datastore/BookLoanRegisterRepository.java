package se.kwnna.library.datastore;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.kwnna.library.datastore.dto.BookLoanRegisterDto;

@Repository
public interface BookLoanRegisterRepository extends CrudRepository<BookLoanRegisterDto, Integer> {
}