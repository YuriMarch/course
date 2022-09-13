package com.distancelearning.course.services;

import com.distancelearning.course.models.CourseModel;
import com.distancelearning.course.models.CourseUserModel;

import java.util.UUID;

public interface CourseUserService {
    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

    CourseUserModel save(CourseUserModel courseUserModel);
}
