package com.distancelearning.course.controllers;

import com.distancelearning.course.clients.CourseClient;
import com.distancelearning.course.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/courses/{courseId}/users")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Log4j2
public class CourseUserController {

    private final CourseClient courseClient;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsersInCourse(@PathVariable UUID courseId,
                                                             @PageableDefault(page = 0, size = 5, sort = "userId",
                                                               direction = Sort.Direction.ASC) Pageable pageable){


        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllUsersInCourse(courseId, pageable));
    }

}
