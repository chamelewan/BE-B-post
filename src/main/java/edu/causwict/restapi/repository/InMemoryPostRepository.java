package edu.causwict.restapi.repository;

import java.util.ArrayList;
import java.util.List;
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

}
