package org.example.expert.domain.user.service;

import java.util.UUID;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import jakarta.persistence.EntityManager;

@SpringBootTest
class UserInsertTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityManager em;

	@Test
	@Rollback(value = false) // Test환경에선 테스트 후에 db를 롤백하기 때문에 명시적으로 롤백하지 않게끔 함
	void insertMillionUsers() {
		// 1,000,000개의 유저 데이터 삽입 메서드
		for(int i = 0 ; i < 1000; i++){
			create1000User(i*1000);
		}
	}

	private void create1000User(int offset){
		/* 1000개의 유저 데이터 삽입 메서드
		*  중간중간 flush()랑 clear()를 해줘도 힙메모리는 초기화되지 않기 때문에
		*  일부러 메서드를 나눠서 힙 메모리와 1차캐시를 1000번 마다 초기화 해줌
		* */
		for(int i = 0 ; i < 1000; i++){
			User user = new User(offset+i+"@google.com",
				UUID.randomUUID().toString().substring(0,3)+offset+i);
			userRepository.save(user);
		}
		userRepository.flush();
		em.clear();
	}
}