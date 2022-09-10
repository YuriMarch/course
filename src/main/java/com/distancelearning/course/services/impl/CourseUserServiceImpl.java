package com.distancelearning.course.services.impl;

import com.distancelearning.course.repositories.CourseUserRepository;
import com.distancelearning.course.services.CourseUserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;
}
