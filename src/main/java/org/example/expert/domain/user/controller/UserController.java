package org.example.expert.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.request.UserPageSearchRequest;
import org.example.expert.domain.user.dto.response.SliceResponse;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/users")
    public void changePassword(@Auth AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }

    /* 레벨 13 닉네임을 조건으로 유저 목록 검색 API
    *  목록 검색이라 페이징을 하려다가 Slice가 훨씬 빠르다길래 Slice 변경
    * */
    @PostMapping("/users/slice")
    public ResponseEntity<SliceResponse<UserSearchResponse>> getUserByNickname(
        @Valid @RequestBody UserPageSearchRequest request
        ){
        Slice<UserSearchResponse> users = userService.getUserByNickname(request);

        return ResponseEntity.ok(SliceResponse.from(users));
    }

    // 레벨 13 비교용 Page 메서드
    @PostMapping("/users/page")
    public ResponseEntity<Page<UserSearchResponse>> getUserPageByNickname(
        @Valid @RequestBody UserPageSearchRequest request
    ){
        Page<UserSearchResponse> users = userService.getUserPageByNickname(request);

        return ResponseEntity.ok(users);
    }

}
