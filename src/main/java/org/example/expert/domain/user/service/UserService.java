package org.example.expert.domain.user.service;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.request.UserPageSearchRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        validateNewPassword(userChangePasswordRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }

    // 레벨 13 검색 API 서비스 로직
    public Slice<UserSearchResponse> getUserByNickname(UserPageSearchRequest request) {

        Pageable pageable = PageRequest.of(request.getPage()- 1, request.getSize());
        String nickname = request.getNickname();

        if (nickname == null || nickname.length() < 2) {
            throw new InvalidRequestException("닉네임은 최소 두글자 이상이여야 합니다.");
        }

        return userRepository.findByNickname(nickname, pageable);
    }

    // 레벨 13 비교용 서비스 로직
    public Page<UserSearchResponse> getUserPageByNickname(UserPageSearchRequest request) {

        Pageable pageable = PageRequest.of(request.getPage()- 1, request.getSize());
        String nickname = request.getNickname();

        if (nickname == null || nickname.length() < 2) {
            throw new InvalidRequestException("닉네임은 최소 두글자 이상이여야 합니다.");
        }

        return userRepository.findPageByNickname(request.getNickname(), pageable);
    }
}
