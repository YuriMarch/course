package com.distancelearning.course.controllers;

import com.distancelearning.course.dtos.ModuleDto;
import com.distancelearning.course.models.CourseModel;
import com.distancelearning.course.models.ModuleModel;
import com.distancelearning.course.services.CourseService;
import com.distancelearning.course.services.ModuleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses/{courseId}/modules")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    private final ModuleService moduleService;
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveModule(@RequestBody @Valid ModuleDto moduleDto,
                                             @PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        } else {
            var moduleModel = new ModuleModel();
            BeanUtils.copyProperties(moduleDto, moduleModel);
            moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
            moduleModel.setCourse(courseModelOptional.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
        }
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "courseId") UUID courseId){
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        } else {
            moduleService.delete(moduleModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Module successfully deleted.");
        }
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "courseId") UUID courseId,
                                               @RequestBody @Valid ModuleDto moduleDto){
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        } else {
            var moduleModel = moduleModelOptional.get();
            moduleModel.setTitle(moduleDto.getTitle());
            moduleModel.setDescription(moduleDto.getDescription());
            return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
        }
    }

    @GetMapping
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId){
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse(courseId));
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<Object> getModuleById(@PathVariable(value = "moduleId") UUID moduleId,
                                                @PathVariable(value = "courseId") UUID courseId){
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        return moduleModelOptional.<ResponseEntity<Object>>map(module -> ResponseEntity.status(HttpStatus.OK).body(module)).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course."));
    }
}
