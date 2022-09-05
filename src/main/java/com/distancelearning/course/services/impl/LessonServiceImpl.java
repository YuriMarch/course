package com.distancelearning.course.services.impl;

import com.distancelearning.course.repositories.LessonRepository;
import com.distancelearning.course.services.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
}
