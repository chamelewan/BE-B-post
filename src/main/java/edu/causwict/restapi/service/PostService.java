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
		Post post = new Post(null, title, content);
		return postRepository.save(post);
	}

    public Post update(Long id, String title, String content) {
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


