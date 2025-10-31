package edu.causwict.restapi.service;

import org.springframework.stereotype.Service;
import edu.causwict.restapi.entity.Post;
import edu.causwict.restapi.repository.InMemoryPostRepository;

import java.util.List;

@Service
public class PostService {

	private final InMemoryPostRepository postRepository;

	public PostService(InMemoryPostRepository postRepository) { // 생성자 주입
		this.postRepository = postRepository;
	}

	public Post create(String title, String content) {

        // 제목 비어있는지 검증하는 로직 추가
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목(title)은 비어 있을 수 없습니다.");
        }
        // trim()을 통해서 ""인경우와 "    " 처럼 여러번 공백들어간 경우 모두 ""로 바꿔버림.

        // 제목 30글자 넘는지 체크
        if (title.length() > 30) {
            throw new IllegalArgumentException("제목(title)은 30자를 넘을 수 없습니다.");
        }

        // (5번쨰 기능) 제목 중복 검증 - InMemoryPostRepository의 findByTitle 메소드를 사용
        if (postRepository.findByTitle(title) != null) {
            throw new IllegalArgumentException("이미 존재하는 제목(title)입니다.");
        }

		Post post = new Post(null, title, content);
		return postRepository.save(post);
	}

    public Post update(Long id, String title, String content) {
        // 제목 비어있는지 검증하는 로직 추가, create와 update 메소드 전부 추가
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목(title)은 비어 있을 수 없습니다.");
        }

        // 제목 30글자 넘는지 체크
        if (title.length() > 30) {
            throw new IllegalArgumentException("제목(title)은 30자를 넘을 수 없습니다.");
        }

        // create와 다르게 우선 제목이 같은 게시글(객체)를 먼저 찾음.
        Post postWithSameTitle = postRepository.findByTitle(title);
        // 찾았는데(not null) && 그 게시글의 ID가 지금 수정하려는 ID와 다른 케이스
        // 즉, 다른 게시글이 이 제목을 이미 쓰고 있는 경우.
        if (postWithSameTitle != null && !postWithSameTitle.getId().equals(id)) {
            throw new IllegalArgumentException("다른 게시글에서 이미 사용 중인 제목(title)입니다.");
        }

        // 1. 기존 게시글이 존재하는지 확인
        Post existingPost = postRepository.findById(id);
        if (existingPost == null) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. id: " + id);
        }

        // 2. 새로운 게시글 객체 생성
        Post updatedPost = new Post(id, title, content);

        // 3. 저장소에 업데이트
        return postRepository.update(id, updatedPost);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}


