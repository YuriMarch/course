package com.distancelearning.course.clients;

import com.distancelearning.course.dtos.ResponsePageDto;
import com.distancelearning.course.dtos.UserDto;
import com.distancelearning.course.services.UtilsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
@Log4j2
public class CourseClient {
    private final RestTemplate restTemplate;

    private final UtilsService utilsService;

    public Page<UserDto> getAllUsersInCourse(UUID courseId, Pageable pageable) {
        List<UserDto> searchResult = null;
        String url = utilsService.createUrl(courseId, pageable);

        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);
        ResponseEntity<ResponsePageDto<UserDto>> result = null;
        try {
            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType =
                    new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {};
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();

            log.debug("Response number of elements: {}", searchResult.size());
        } catch (HttpStatusCodeException e) {

            log.error("Error request /courses {}", e);
        }
        log.info("Ending request /users courseId {}", courseId);
        return result.getBody();
    }
}
