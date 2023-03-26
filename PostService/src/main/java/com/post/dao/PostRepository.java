package com.post.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("select p from Post p where p.authorId=:authorId")
	List<Post> findPostsByAuthorId(@Param("authorId") Long authorid);

	@Query("select p from Post p where p.showName like %:showName%")
	List<Post> getPostBykeyword(@Param("showName")String showName);

}
