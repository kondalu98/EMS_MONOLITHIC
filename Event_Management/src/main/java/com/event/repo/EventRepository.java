package com.event.repo;


import com.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByDateAndLocation(LocalDate date, String location);
    List<Event> findByCategory(String category);
    List<Event> findByLocation(String location);
    List<Event> findByDate(LocalDate date);


}

