package dev.marjoz.course;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
class CourseEndpoint {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    CourseEndpoint(final CourseRepository courseRepository,
                   final CourseMapper courseMapper) {

        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @GetMapping("/{id}")
    Optional<CourseProjection> findById(@PathVariable Integer id) {
        return Optional.ofNullable(courseMapper.apply(courseRepository.findById(id)
                       .orElseThrow(CourseNotFoundException::new)));
    }

    @GetMapping("")
    List<CourseProjection> findAll() {
        return courseRepository.findAll().stream()
                                         .map(courseMapper)
                                         .toList();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        courseRepository.deleteById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    CourseProjection save(@RequestBody @Valid Course course) {
        return courseMapper.apply(courseRepository.save(course));
    }

    @PutMapping("/{id}")
    CourseProjection update(@PathVariable Integer id, @RequestBody Course course) {
        return courseMapper.apply(courseRepository.findById(id)
                                                  .map(it -> {
            var updated = Course.builder()
                                .withUserId(course.userId())
                                .withId(course.id())
                                .withTitle(course.title())
                                .withComment(course.comment())
                                .build();

            courseRepository.save(updated);
            return updated;
        }).orElseThrow(CourseNotFoundException::new));
    }
}
