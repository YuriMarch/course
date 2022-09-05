package com.distancelearning.course.services.impl;

import com.distancelearning.course.models.LessonModel;
import com.distancelearning.course.models.ModuleModel;
import com.distancelearning.course.repositories.LessonRepository;
import com.distancelearning.course.repositories.ModuleRepository;
import com.distancelearning.course.services.ModuleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(ModuleModel moduleModel) {
        List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());
        if (!lessonModelList.isEmpty()){
            lessonRepository.deleteAll(lessonModelList);
        }
        moduleRepository.delete(moduleModel);
    }
}
