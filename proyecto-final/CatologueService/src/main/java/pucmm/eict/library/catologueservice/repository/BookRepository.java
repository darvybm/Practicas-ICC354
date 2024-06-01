package pucmm.eict.library.catologueservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pucmm.eict.library.catologueservice.model.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByGenre(String genre);
}
