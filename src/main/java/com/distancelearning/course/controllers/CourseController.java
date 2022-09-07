package com.distancelearning.course.controllers;

import com.distancelearning.course.dtos.CourseDto;
import com.distancelearning.course.models.CourseModel;
import com.distancelearning.course.services.CourseService;
import com.distancelearning.course.specifications.SpecificationTemplate;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto){
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        } else {
            courseService.delete(courseModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Course successfully deleted.");
        }
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable UUID courseId,
                                               @RequestBody @Valid CourseDto courseDto){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        } else {
            var courseModel = courseModelOptional.get();
            courseModel.setName(courseDto.getName());
            courseModel.setDescription(courseDto.getDescription());
            courseModel.setImageUrl(courseDto.getImageUrl());
            courseModel.setCourseStatus(courseDto.getCourseStatus());
            courseModel.setCourseLevel(courseDto.getCourseLevel());
            courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
        }
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault (size = 5, sort = "courseId",
                                                            direction = Sort.Direction.ASC) Pageable pageable){
        Page<CourseModel> coursePage = courseService.findAll(spec, pageable);
        if (!coursePage.isEmpty()){
            for (CourseModel courseModel: coursePage.toList()){
                courseModel.add(linkTo(methodOn(CourseController.class).getCourseById(courseModel.getCourseId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(coursePage);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourseById(@PathVariable UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        return courseModelOptional.<ResponseEntity<Object>>map(course -> ResponseEntity.status(HttpStatus.OK).body(course)).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found."));
    }
}
