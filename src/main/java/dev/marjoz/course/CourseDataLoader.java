package dev.marjoz.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.asm.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
class CourseDataLoader implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final CourseRepository courseRepository;

    CourseDataLoader(final ObjectMapper objectMapper,
                     final CourseRepository courseRepository) {

        this.objectMapper = objectMapper;
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) {
        if (courseRepository.count() == 0) {
            var coursesJSON = "/data/courses.json";
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(coursesJSON)) {
                var response = objectMapper.readValue(inputStream, Courses.class);
                courseRepository.saveAll(response.courses());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }
}
