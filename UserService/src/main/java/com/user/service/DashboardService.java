package com.user.service;

import com.user.entity.Post;

public interface DashboardService {
	
	Post getPostByPostId(Long id);
	
	String deletePost(Long id);

}
