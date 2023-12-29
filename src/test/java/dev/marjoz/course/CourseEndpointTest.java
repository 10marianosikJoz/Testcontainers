package dev.marjoz.course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseEndpointTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void shouldReturnAllCourses() {
        var courses = testRestTemplate.getForObject("/api/courses", CourseProjection[].class);

        assertThat(courses.length).isEqualTo(11);
    }

    @Test
    void shouldThrowCourseNotFoundExceptionWhenInvalidIDIsProvided() {
        var response = testRestTemplate.exchange("/api/courses/1000",
                                                HttpMethod.GET,
                                                null,
                                                Course.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldFindCourseWhenValidIDIsProvided() {
        var response = testRestTemplate.exchange("/api/courses/2",
                                                 HttpMethod.GET,
                                                 null,
                                                 Course.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Rollback
    void shouldCreateNewCourseWhenRequestBodyIsValid() {
        var course = Course.builder()
                           .withId(11)
                           .withUserId(1)
                           .withTitle("Testcontainers")
                           .withComment("Docker in tests")
                           .withVersion(null)
                           .build();

        var response = testRestTemplate.exchange("/api/courses",
                                                 HttpMethod.POST,
                                                 new HttpEntity<>(course),
                                                 Course.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(11);
        assertThat(response.getBody().title()).isEqualTo("Testcontainers");
    }

    @Test
    @Rollback
    void shouldDeleteCourseById() {
        var response = testRestTemplate.exchange("/api/courses/1",
                                                 HttpMethod.DELETE,
                                                 null,
                                                 Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Rollback
    void shouldUpdateGivenCourse() {
        var response = testRestTemplate.exchange("/api/courses/1",
                                                 HttpMethod.GET,
                                                 null,
                                                 CourseProjection.class);
        var existingCourse = response.getBody();
        assertThat(existingCourse).isNotNull();

        var updated = Course.builder()
                            .withId(existingCourse.id())
                            .withUserId(existingCourse.userId())
                            .withTitle("Example title")
                            .withComment("Example comment")
                            .withVersion(existingCourse.version())
                            .build();

        assertThat(updated.id()).isEqualTo(1);
        assertThat(updated.userId()).isEqualTo(1);
        assertThat(updated.title()).isEqualTo("Example title");
        assertThat(updated.comment()).isEqualTo("Example comment");
    }
}