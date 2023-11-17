package org.binaracademy.challenge_7.service;

import org.binaracademy.challenge_7.entity.dto.UserDto;
import org.binaracademy.challenge_7.entity.response.Response;

public interface UserService {
    Response<UserDto> save(UserDto user);
    Response<String> update(Long userId, UserDto user);
    Response<String> delete(Long userId);
}
