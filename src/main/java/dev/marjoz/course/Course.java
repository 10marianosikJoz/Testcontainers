package dev.marjoz.course;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import javax.validation.constraints.NotEmpty;

record Course(@Id
              Integer id,
              Integer userId,
              @NotEmpty
              String title,
              @NotEmpty
              String comment,
              @Version
              Integer version) {

    static CourseBuilder builder() {
        return new CourseBuilder();
    }

    static class CourseBuilder {
        private Integer id;
        private Integer userId;
        private String title;
        private String comment;
        private Integer version;

        private CourseBuilder() {}

        CourseBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        CourseBuilder withUserId(Integer userId) {
            this.userId = userId;
            return this;
        }

        CourseBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        CourseBuilder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        CourseBuilder withVersion(Integer version) {
            this.version = version;
            return this;
        }

        Course build() {
            return new Course(id,
                              userId,
                              title,
                              comment,
                              version);
        }
    }
}
