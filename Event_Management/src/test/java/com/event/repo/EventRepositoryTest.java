package com.event.repo;

import com.event.entity.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("Should find events by category")
    void testFindByCategory() {
        Event event = new Event();
        event.setName("Music Fest");
        event.setCategory("Music");
        event.setLocation("Hyderabad");
        event.setDate(LocalDate.of(2025, 7, 1));
        eventRepository.save(event);

        List<Event> found = eventRepository.findByCategory("Music");

        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getCategory()).isEqualTo("Music");
    }

    @Test
    @DisplayName("Should find events by location")
    void testFindByLocation() {
        Event event = new Event();
        event.setName("Tech Summit");
        event.setCategory("Technology");
        event.setLocation("Bangalore");
        event.setDate(LocalDate.of(2025, 8, 15));
        eventRepository.save(event);

        List<Event> found = eventRepository.findByLocation("Bangalore");

        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getLocation()).isEqualTo("Bangalore");
    }

    @Test
    @DisplayName("Should find events by date")
    void testFindByDate() {
        LocalDate date = LocalDate.of(2025, 9, 5);

        Event event = new Event();
        event.setName("Startup Meetup");
        event.setCategory("Business");
        event.setLocation("Pune");
        event.setDate(date);
        eventRepository.save(event);

        List<Event> found = eventRepository.findByDate(date);

        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getDate()).isEqualTo(date);
    }
}
