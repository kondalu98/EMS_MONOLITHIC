package com.event.repo;

import com.event.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByEventEventID(Long eventId);
    List<Feedback> findByUserId(Long userId);
}
