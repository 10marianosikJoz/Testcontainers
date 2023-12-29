package dev.marjoz.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {
    
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");
    
    @Autowired
    CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        var courses = List.of(Course.builder()
                                    .withId(1)
                                    .withUserId(1)
                                    .withTitle("Kotlin OOP")
                                    .withComment("Great course")
                                    .build(),
                              Course.builder()
                                    .withId(2)
                                    .withUserId(3)
                                    .withTitle("Java OOP")
                                    .withComment("Wonderful course")
                                    .build());

        courseRepository.saveAll(courses);
    }

    @Test
    void shouldReturnCourseByTitle() {
        var course = courseRepository.findByTitle("Kotlin OOP");

        assertThat(course).isNotNull();
        assertThat(course.getClass()).isEqualTo(CourseProjection.class);
    }
}