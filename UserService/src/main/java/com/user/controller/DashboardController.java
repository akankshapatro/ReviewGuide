package com.user.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.user.dao.UserRepository;
import com.user.entity.Post;
import com.user.entity.User;
import com.user.helper.Message;
import com.user.service.DashboardService;
import com.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DashboardController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DashboardService dashboardService;
	
	/*method used to execute common functionalities*/
	@ModelAttribute
	public void commonFunction(Model model, Principal principal) {
		log.info("Entered into commonFunction of DashboardController");
		String username = principal.getName();
		log.info("Username of logged in user: " + username);
		User user = userService.getUserByEmail(username);
		log.info("User Object "+user.toString() );
		model.addAttribute("user",user);
	}
	
	/*Method Handler to load dashboard page*/
	@RequestMapping(value = "/dashboard")
	public String getDashboard(Model model,Principal principal) {
		log.info("Entered into getDashboard of DashboardController");
		String url = "http://POST-SERVICE/post/getPosts";
		ResponseEntity<Post[]> response = restTemplate.getForEntity(url, Post[].class);
		Post[] posts=response.getBody();
		/*ResponseEntity<String> posts = restTemplate.getForEntity(url, String.class, authorId);*/
		for(int i=0;i<posts.length;i++) {
			posts[i].setAuthorName(userService.getNameById(posts[i].getAuthorId()));
			System.out.println(posts[i]);
		}
		List<Post> allPosts = Arrays.asList(posts);
		model.addAttribute("Posts", allPosts);
		model.addAttribute("title","Dashboard- Review Guide");
		return "dashboard";
	}
	
	/*Method Handler to load settings page*/
	@RequestMapping(value = "/settings")
	public String showSettings(Model model,Principal principal) {
		log.info("Entered into showSettings of DashboardController");
		model.addAttribute("title","Settings- Review Guide");
		return "settings";
	}
	
	/*Method Handler to change password */
	@RequestMapping(value = "/changePassword",method = RequestMethod.POST)
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,Principal principal,Model model,HttpSession session) {
		log.info("Entered into changePassword of DashboardController");
		log.info("Old passwprd:  "+oldPassword);
		log.info("New passwprd:  "+newPassword);
		String username = principal.getName();
		User user = userService.getUserByEmail(username);
		if(this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword()))
		{
			user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			userService.addUser(user);
			session.setAttribute("message", new Message("Password Changed Successfully ","alert-success"));
			
		}
		else
		{
			session.setAttribute("message", new Message("Old Password Entered is wrong ","alert-danger"));
		}
		model.addAttribute("user",user);
		return "redirect:/settings";
	}
	
	/*Method Handler to load profile page*/
	@RequestMapping(value = "/profile",method=RequestMethod.GET)
	public String getProfile(Model model) {
		log.info("Entered into getProfile of DashboardController");
		model.addAttribute("title","Profile- Review Guide");
		return "profile";
	}
	
	/*Method Handler to load addPost page*/
	@RequestMapping(value="/addPost",method=RequestMethod.GET)
	public String addPost(Model model) {
		log.info("Entered into addPost of DashboardController");
		model.addAttribute("title","New Post- Review Guide");
		return "post";
	}
	
	/*Method Handler to save a new post*/
	@RequestMapping(value="/savePost",method=RequestMethod.POST)
	public String savePost(Post post,@RequestParam("showImage") MultipartFile image,Principal principal) {
		log.info("Entered into savePost of DashboardController");
		log.info("post details...."+post.toString());
		try {
			String username = principal.getName();
			User user = userService.getUserByEmail(username);
			//for post image
			if(image.isEmpty()) {
				log.info("Image is empty");
			}else {
				String dateTimeNow = new SimpleDateFormat("yyyyMMddHHmmss'.txt'", Locale.getDefault()).format(new Date());
				String filename = post.getShowName()+"_"+user.getName()+dateTimeNow+"_"+image.getOriginalFilename();
				post.setImage(filename);
				File file = new ClassPathResource("/static/img/postimage").getFile();
				Path path = Paths.get(file.getAbsolutePath()+File.separator+filename);
				
				Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				log.info("Image is uploaded");
			}
			post.setAuthorId(user.getId());
			post.setCreatedAt(LocalDate.now());
			
			String url = "http://POST-SERVICE/post/addPost";
			String result = restTemplate.postForObject(url, post, String.class);
			return "success";
		} catch (Exception e) {
			log.info("Error in Saving New Post"+e.getMessage());
			return "post";
		}
		
				
	}
	
	/*Method Handler to load the timeline*/
	@RequestMapping(value="/viewPosts",method=RequestMethod.GET)
	public String viewPosts(Model model,Principal principal) {
		log.info("Entered into viewPosts of DashboardController");
		String username = principal.getName();
		User user = userService.getUserByEmail(username);
		Long authorId = user.getId();
		
		String url = "http://POST-SERVICE/post/getPostByAuthor/{id}";
		ResponseEntity<Post[]> response = restTemplate.getForEntity(url, Post[].class,authorId);
		Post[] posts=response.getBody();
		/*ResponseEntity<String> posts = restTemplate.getForEntity(url, String.class, authorId);*/
		for(int i=0;i<posts.length;i++) {
			posts[i].setAuthorName(userService.getNameById(posts[i].getAuthorId()));
			System.out.println(posts[i]);
		}
		List<Post> postOfAuthor = Arrays.asList(posts);
		model.addAttribute("Posts", postOfAuthor);
		return "myPosts";
	}
	
	/*Method Handler to load the updatePost page*/
	@RequestMapping(value="/update/{id}",method = RequestMethod.GET)
	public String updatePost(@PathVariable("id") Long id,Model model)
	{
		log.info("Entered into updatePost of DashboardController");
		log.info("Post Id for update: "+id);
		Post post = dashboardService.getPostByPostId(id); 
		model.addAttribute("post",post);
		return "updatePost";
	}
	
	/*Method Handler to delete a post*/
	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
	public String deletePost(@PathVariable("id") Long id) throws IOException
	{
		log.info("Entered into deletePost of DashboardController");
		log.info("Post Id for delete:"+id);
		//need to remove the image from the postimage folder as well
		Post post = dashboardService.getPostByPostId(id); 
		log.info("post to be deleted: "+post);
		if(post.getImage()!=null && !post.getImage().isEmpty()) {
			
			File file = new ClassPathResource("/static/img/postimage").getFile();
			File deleteFile = new File(file, post.getImage());
			deleteFile.delete();
			log.info("image Deleted successfully...");
		}
		
		String result = dashboardService.deletePost(id);
		log.info(result);
		
		return "redirect:/viewPosts";
	}
	
	/*Method Handler to update a Post*/
	@RequestMapping(value="/processUpdate",method=RequestMethod.POST)
	public String processUpdate(Post post,Principal principal) {
		log.info("Entered into processUpdate of DashboardController");
		log.info("Post for update: "+post.toString());
		String username = principal.getName();
		User user = userService.getUserByEmail(username);
		Post originalPost = dashboardService.getPostByPostId(post.getId());
		originalPost.setReview(post.getReview());
		originalPost.setRatings(post.getRatings());
		/*post.setAuthorId(user.getId());*/
		post.setUpdatedAt(LocalDate.now());
		String url = "http://POST-SERVICE/post/addPost";
		String result = restTemplate.postForObject(url, originalPost, String.class);
		return "redirect:/viewPosts";
	}
	
	/*Method Handler to load the searchPost page*/
	@RequestMapping(value="/search",method = RequestMethod.GET)
	public String searchPost(Model model)
	{
		log.info("Entered into searchPost of DashboardController");
		
		model.addAttribute("title","Search- Review Guide");
		return "search";
	}
	
	/*Method Handler to search a Post*/
	@RequestMapping(value="/searchKeyword",method = RequestMethod.GET)
	public String searchKeyword(@RequestParam("keyword")String keyword,Model model)
	{
		log.info("Entered into searchKeyword of DashboardController");
		System.out.println("keyword entered: "+keyword);
		model.addAttribute("title","Search- Review Guide");
		String url = "http://POST-SERVICE/post/search/{keyword}";
		ResponseEntity<Post[]> response = restTemplate.getForEntity(url, Post[].class,keyword);
		Post[] posts=response.getBody();
		for(int i=0;i<posts.length;i++)
			System.out.println(posts[i]);
		List<Post> searchResult = Arrays.asList(posts);
		model.addAttribute("Posts", searchResult);
		return "search";
	}
	
	/*@ExceptionHandler(value = RuntimeException.class)
	public String handleException(RuntimeException ex,Principal principal,Model model) {
		log.info("Entered exception handler");
		String username = principal.getName();
		User user = userService.getUserByEmail(username);
		model.addAttribute("user",user);
		return "error";
	}*/
	

}
