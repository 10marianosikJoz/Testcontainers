package dev.marjoz.course;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
class CourseMapper implements Function<Course, CourseProjection> {

    @Override
    public CourseProjection apply(Course course) {
        return new CourseProjection(course.id(),
                                    course.userId(),
                                    course.title(),
                                    course.comment(),
                                    course.version());
    }
}
