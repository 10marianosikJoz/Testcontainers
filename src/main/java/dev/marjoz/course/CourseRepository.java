package dev.marjoz.course;

import org.springframework.data.repository.ListCrudRepository;

interface CourseRepository extends ListCrudRepository<Course, Integer> {

    CourseProjection findByTitle(String title);
}
