package com.distancelearning.course.services.impl;

import com.distancelearning.course.repositories.ModuleRepository;
import com.distancelearning.course.services.ModuleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
}
