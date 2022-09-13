package com.distancelearning.course.controllers;

import com.distancelearning.course.clients.AuthUserClient;
import com.distancelearning.course.dtos.SubscriptionDto;
import com.distancelearning.course.dtos.UserDto;
import com.distancelearning.course.enums.UserStatus;
import com.distancelearning.course.models.CourseModel;
import com.distancelearning.course.models.CourseUserModel;
import com.distancelearning.course.services.CourseService;
import com.distancelearning.course.services.CourseUserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses/{courseId}/users")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Log4j2
public class CourseUserController {

    private final AuthUserClient authUserClient;
    private final CourseService courseService;
    private final CourseUserService courseUserService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsersInCourse(@PathVariable UUID courseId,
                                                             @PageableDefault(page = 0, size = 5, sort = "userId",
                                                               direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllUsersInCourse(courseId, pageable));
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID courseId,
                                                               @RequestBody @Valid SubscriptionDto subscriptionDto){
        ResponseEntity<UserDto> responseUser;
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(courseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        if (courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDto.getUserId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: user is already subscribed to this course.");
        }

        try {
            responseUser = authUserClient.getOneUserById(subscriptionDto.getUserId());
            if (responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: user is blocked.");
            }
        } catch (HttpStatusCodeException e){
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: user not found.");
            }
        }

        CourseUserModel courseUserModel = courseUserService.save(courseModelOptional.get().convertToCourseUserModel(subscriptionDto.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }
}
