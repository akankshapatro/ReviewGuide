package com.user.entity;

import java.time.LocalDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String showName;
	private String category;
	private String image;
	private String yearOfRelease;
	private String cast;
	private String genre;
	private String review;
	private int ratings;
	private long authorId;
	private String authorName;
	private LocalDate createdAt;
	private LocalDate updatedAt;

}
