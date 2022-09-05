package com.distancelearning.course.services.impl;

import com.distancelearning.course.models.CourseModel;
import com.distancelearning.course.models.LessonModel;
import com.distancelearning.course.models.ModuleModel;
import com.distancelearning.course.repositories.CourseRepository;
import com.distancelearning.course.repositories.LessonRepository;
import com.distancelearning.course.repositories.ModuleRepository;
import com.distancelearning.course.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if (!moduleModelList.isEmpty()){
            for (ModuleModel module : moduleModelList){
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if (!lessonModelList.isEmpty()){
                    lessonRepository.deleteAll(lessonModelList);
                }
            }
            moduleRepository.deleteAll(moduleModelList);
        }
        courseRepository.delete(courseModel);
    }
}
