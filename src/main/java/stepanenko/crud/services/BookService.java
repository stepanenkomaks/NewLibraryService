package stepanenko.crud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stepanenko.crud.models.Book;
import stepanenko.crud.models.Person;
import stepanenko.crud.repositories.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAll(Boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(Sort.by("year"));
        } else {
            return bookRepository.findAll();
        }
    }

    public List<Book> findAll(Integer page, Integer books_per_page, Boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(PageRequest.of(page, books_per_page, Sort.by("year"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page, books_per_page)).getContent();
        }
    }

    public Book findOne(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> findByNameStartingWith(String startingWith) {
        return bookRepository.findByNameStartingWith(startingWith);
    }

    @Transactional
    public void save(Book person) {
        bookRepository.save(person);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = bookRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());

        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public Optional<Person> getOwner(int id) {
        return bookRepository.findById(id).map(Book::getOwner);
    }

    @Transactional
    public void assign(int id, Person person) {
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setTakenAt(new Date());
                    book.setOwner(person);
                }
        );
    }

    @Transactional
    public void release(int id) {
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                }
        );
    }
}
