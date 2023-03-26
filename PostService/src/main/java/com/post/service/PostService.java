package com.post.service;

import java.util.List;

import com.post.entity.Post;

public interface PostService {
	
	Post addPost(Post post);

	List<Post> getPostByAuthor(Long authorid);

	List<Post> getAllPosts();

	String deleteById(Long id);

	Post getPostById(Long id);

	List<Post> searchByKeyword(String keyword);

}
