package edu.causwict.restapi.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import edu.causwict.restapi.entity.Post;

@Repository
public class InMemoryPostRepository {

	private final Map<Long, Post> store = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(0);

	public Post save(Post post) {
		if (post.getId() == null) {
			post.setId(sequence.incrementAndGet());
		}
		store.put(post.getId(), post);
		return post;
	}

	public List<Post> findAll() {
		return new ArrayList<>(store.values());
	}

    public Post findById(Long id) {
        return store.get(id);  // id로 게시글 찾기
    }

    public Post update(Long id, Post post) {
        if (!store.containsKey(id)) {
            return null;  // 해당 id의 게시글이 없으면 null 반환
        }
        post.setId(id);  // id 설정
        store.put(id, post);  // 기존 게시글 덮어쓰기
        return post;
    }


    public Post findByTitle(String title) {
        // store에 저장된 모든 Post 객체(store.values())를 순회(stream)하면서
        // post의 제목(post.getTitle())이 파라미터로 받은 title과 같은지(equals) 확인.
        // 찾은 첫 번째(findFirst) Post를 반환하고, 없으면 null을 반환.
        return store.values().stream()
                .filter(post -> post.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    // 제목(title)에 키워드가 포함된 모든 게시글을 찾는 메서드
    public List<Post> findByTitleContaining(String keyword) {
        // store의 모든 값을 순회하면서
        return store.values().stream()
                // post의 제목(getTitle())에 keyword가 포함되어 있는지(contains) 확인
                .filter(post -> post.getTitle().contains(keyword))
                // 필터를 통과한 모든 Post를 List로 모아서 반환
                .collect(java.util.stream.Collectors.toList());
    }

}
