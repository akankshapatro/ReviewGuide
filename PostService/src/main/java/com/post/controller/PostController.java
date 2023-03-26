package com.post.controller;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.post.entity.Post;
import com.post.exception.PostDoesNotExistsException;
import com.post.service.PostService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/post")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	/* method used to add a new post. Takes post object as inputs */
	@RequestMapping(value = "/addPost",method = RequestMethod.POST)
	public String addPost(@RequestBody Post post) {
		Post postResult = postService.addPost(post);
		if(postResult!=null) {
			return "Post Added Successfully";
		}else {
			throw new PostDoesNotExistsException();
		}
		
	}
	
	/* method used to get posts of a particular based on authorId */
	@RequestMapping(value="/getPostByAuthor/{id}",method = RequestMethod.GET)
	public List<Post> getPostByAuthor(@PathVariable("id") Long authorid) {
		log.info("Entering into get posts by author method...");
		List<Post> posts = postService.getPostByAuthor(authorid);
		Collections.sort(posts, Collections.reverseOrder());
		for(int i = 0;i<posts.size();i++)
			System.out.println(posts.get(i));
		return posts;
	}
	
	/* method used to get all posts to populate the timeline */
	@RequestMapping(value="/getPosts",method = RequestMethod.GET)
	public List<Post> getAllPosts(){
		log.info("Entering into getAllPosts method...");
		List<Post> posts = postService.getAllPosts();
		Collections.sort(posts, Collections.reverseOrder());
		for(int i = 0;i<posts.size();i++)
			System.out.println(posts.get(i));
		return posts;
	}
	
	/* method used to get post by its id */
	@RequestMapping(value="/getPostById/{id}",method=RequestMethod.GET)
	public Post getPostById(@PathVariable("id") Long id) {
		log.info("Entering into getPostById method...");
		Post post = postService.getPostById(id);
		if(post!=null)
			return post;
		else
			throw new PostDoesNotExistsException();
	}
	
	/* method used to delete a post by its id */
	@RequestMapping(value="/deleteById/{id}",method = RequestMethod.GET)
	public String deleteById(@PathVariable("id") Long id) {
		log.info("Entering into deleteById method...");
		Post post = postService.getPostById(id);
		if(post!=null)
			return postService.deleteById(id);
		else
			throw new PostDoesNotExistsException("No such post exist");
		
	}
	
	/* method used to search posts with a given keyword */
	@RequestMapping(value="search/{keyword}",method = RequestMethod.GET)
	public List<Post> search(@PathVariable("keyword") String keyword) {
		log.info("Entering into search method...");
		return postService.searchByKeyword(keyword);
	}

}
