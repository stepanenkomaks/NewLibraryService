package stepanenko.crud.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stepanenko.crud.models.Book;
import stepanenko.crud.models.Person;
import stepanenko.crud.repositories.BookRepository;
import stepanenko.crud.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService{
    private final PeopleRepository peopleRepository;
    private final BookRepository bookRepository;


    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BookRepository bookRepository) {
        this.peopleRepository = peopleRepository;
        this.bookRepository = bookRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getBooks(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(
                    book -> {
                        if (book.getOwner() != null) {
                            Date date = new Date();
                            Long uptime = Math.abs(date.getTime() - book.getTakenAt().getTime());
                            if (uptime >= 864000000L) {
                                book.setExpired(true);
                            }
                        }
                    }
            );
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    public Optional<Person> findByName(String name) {
        return peopleRepository.findByName(name);
    }
}
