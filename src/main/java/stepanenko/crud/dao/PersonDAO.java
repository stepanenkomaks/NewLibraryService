package stepanenko.crud.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import stepanenko.crud.models.Book;
import stepanenko.crud.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Personbook", new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> show(String name) {
        return jdbcTemplate.query("SELECT * FROM Personbook WHERE name=?", new Object[]{name},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Personbook WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Personbook(name, year) VALUES(?, ?)", person.getName(), person.getYear());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Personbook SET name=?, year=? WHERE id=?", updatedPerson.getName(), updatedPerson.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Personbook WHERE id=?", id);
    }

    public List<Book> getBooks(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE owner_id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
