package com.PostService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

import com.post.PostServiceApplication;
import com.post.controller.PostController;
import com.post.entity.Post;
import com.post.exception.PostDoesNotExistsException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PostServiceApplication.class)
public class PostTest {
	
	@Autowired
	PostController postController;
	
	@Test
	void addPostTest() {
		
		Post post = new Post();
		post.setShowName("Avengers");
		post.setCategory("Movie");
		post.setGenre("Action");
		post.setRatings(5);
		post.setYearOfRelease("2021");
		assertEquals("Post Added Successfully", postController.addPost(post));
		
	}
	
	
	@Test
	void getPostByIdTest() {
		Post post = new Post();
		post.setShowName("Business Proposal");
		post.setCategory("Tv Series");
		post.setGenre("Romance");
		post.setRatings(5);
		post.setYearOfRelease("2020");
		postController.addPost(post);
		Post result = postController.getPostById(post.getId());
		assertEquals(result.getShowName(),post.getShowName());
		
	}
	
	@Test
	void getPostByIdExceptionTest() {
        assertThrows(PostDoesNotExistsException.class, new Executable() {
             
            public void execute() throws Throwable {
            	Post result = postController.getPostById((long)355);
            }
        });
	}
	
	
	
	@Test
	void deletePostByIdTest() {
		List<Post> posts = postController.getAllPosts();
		if(posts.size()>0) {
		String result = postController.deleteById(posts.get(0).getId());
		assertEquals("Post Deleted Successfully", result);
		}
	}
	
	@Test
	void deletePostByIdExceptionTest() {
        assertThrows(PostDoesNotExistsException.class, new Executable() {
             
            public void execute() throws Throwable {
            	String result = postController.deleteById((long)355);
            }
        });
	}

}
