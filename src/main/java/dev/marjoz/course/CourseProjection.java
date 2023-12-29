package dev.marjoz.course;

record CourseProjection(Integer id,
                        Integer userId,
                        String title,
                        String comment,
                        Integer version) {}
