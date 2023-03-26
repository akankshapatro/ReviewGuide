package com.post.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.logging.log4j.util.PropertySource.Comparator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Comparable<Post>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String showName;
	private String category;
	private String image;
	private String yearOfRelease;
	private String cast;
	private String genre;
	@Column(length = 65555)
	private String review;
	private int ratings;
	private long authorId;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	public int compareTo(Post o) {
		return this.getYearOfRelease().compareTo(o.getYearOfRelease());
		
	}
	

}
