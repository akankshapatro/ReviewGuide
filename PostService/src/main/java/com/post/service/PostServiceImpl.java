package com.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.post.dao.PostRepository;
import com.post.entity.Post;

@Service
public class PostServiceImpl implements PostService{
	
	@Autowired
	private PostRepository postRepository;
	
	/* method used to add a new post. Takes post object as inputs */
	public Post addPost(Post post) {
		return postRepository.save(post);
	}

	/* method used to get posts of a particular based on authorId */
	public List<Post> getPostByAuthor(Long authorid) {
		return postRepository.findPostsByAuthorId(authorid);
	}

	/* method used to get all posts to populate the timeline */
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}
	
	/* method used to get post by its id */
	public Post getPostById(Long id) {
		
		return postRepository.findById(id).orElse(null);
	}

	/* method used to delete a post by its id */
	public String deleteById(Long id) {
		Post post = getPostById(id);
		if(post==null) {
			return "No such post existis";
		}
		else {
			postRepository.deleteById(id);
		}
		return "Post Deleted Successfully";
	}

	/* method used to search posts with a given keyword */
	public List<Post> searchByKeyword(String keyword) {
		
		return postRepository.getPostBykeyword(keyword);
	}

}
