package org.example.repository;

import org.example.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT l FROM lessons l WHERE " +
        "(?#{hasRole('ROLE_ADMIN') ? true : false} = true)" +
        "OR (?#{hasRole('ROLE_TEACHER') ? true : false} = true AND l.teacher.user = ?#{principal.user}) " +
        "OR (?#{hasRole('ROLE_STUDENT') ? true : false} = true AND (SELECT s FROM students s WHERE s.user = ?#{principal.user}) MEMBER OF l.group.students) ")
    Page<Lesson> findAllByCurrentUser(Pageable pageable);

}