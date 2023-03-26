package com.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.user.entity.Post;

@Service
public class DashBoardServiceImpl implements DashboardService {
	
	@Autowired
	private RestTemplate restTemplate;

	/* Method used to get post by its id by calling PostService microservice */
	public Post getPostByPostId(Long id) {
		String getPostUrl = "http://POST-SERVICE/post/getPostById/{id}";
		ResponseEntity<Post> postResponse = restTemplate.getForEntity(getPostUrl,Post.class,id);
		Post post = postResponse.getBody();
		return post;
	}

	/* Method used to delete a post by its id by calling PostService microservice */
	public String deletePost(Long id) {
		String url = "http://POST-SERVICE/post/deleteById/{id}";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class,id);
		String result = response.getBody();
		return result;
	}


}
