package org.example.expert.domain.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
;

import org.example.expert.domain.user.dto.request.UserPageSearchRequest;
import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	// 레벨 13 검색 API 시간 측정 테스트
	@Test
	void getUserByNickname_fromRealDatabase() {
		// given
		long startTime = System.currentTimeMillis();
		String nickname = "12b149000999";
		int page = 1;
		int size = 10;
		UserPageSearchRequest request = new UserPageSearchRequest(nickname,page,size);

		// when
		Slice<UserSearchResponse> result = userService.getUserByNickname(request);

		// then
		assertThat(result).isNotNull();
		assertThat(result.hasContent()).isTrue();
		assertThat(result.getContent().get(0).getNickname()).isEqualTo(nickname);
		assertThat(result.getContent().get(0).getId()).isEqualTo(150000L);
		assertThat(result.getContent().get(0).getEmail()).isEqualTo("149999@google.com");
		long endTime = System.currentTimeMillis();
		System.out.println("시간 측정: " +(endTime-startTime) + "ms");
	}

	// 레벨 13 비교용 검색 API 시간 측정 테스트
	@Test
	void getUserPageByNickname_fromDatabase() {
		// given
		long startTime = System.currentTimeMillis();
		String nickname = "12b149000999";
		int page = 1;
		int size = 10;
		UserPageSearchRequest request = new UserPageSearchRequest(nickname,page,size);

		// when
		Page<UserSearchResponse> result = userService.getUserPageByNickname(request);

		// then
		assertThat(result).isNotNull();
		assertThat(result.hasContent()).isTrue();
		assertThat(result.getContent().get(0).getNickname()).isEqualTo(nickname);
		assertThat(result.getContent().get(0).getId()).isEqualTo(150000L);
		assertThat(result.getContent().get(0).getEmail()).isEqualTo("149999@google.com");
		long endTime = System.currentTimeMillis();
		System.out.println("시간 측정: " +(endTime-startTime) + "ms");
	}

}