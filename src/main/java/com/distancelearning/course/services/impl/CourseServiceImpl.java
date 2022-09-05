package com.distancelearning.course.services.impl;

import com.distancelearning.course.repositories.CourseRepository;
import com.distancelearning.course.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
}
