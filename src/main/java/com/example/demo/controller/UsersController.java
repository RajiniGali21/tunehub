package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.LoginData;
import com.example.demo.entity.Song;
import com.example.demo.entity.Users;
import com.example.demo.services.SongService;
import com.example.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;
@CrossOrigin("*")
@RestController
@Controller
public class UsersController 
{
@Autowired
UsersService service;
@Autowired
SongService songService;
@PostMapping("/register")
//public String addUsers(@RequestParam("username")String username,
//		@RequestParam("email")String email,
//		@RequestParam("password")String password,
//		@RequestParam("gender")String gender,
//		@RequestParam("role")String role,
//		@RequestParam("address")String address
//		) 
public String addUsers(@ModelAttribute Users user)
{
boolean userStatus = service.emailExists(user.getEmail());
if(userStatus == false)
{
service.addUser(user);
System.out.println("user added");
}
else
{
	System.out.println("user added exists");
}
return "home";
}

@PostMapping("/validate")
public String validate(@RequestBody LoginData data, HttpSession session,Model model)
{
	System.out.println("call received");
	String email = data.getEmail();
	String password = data.getPassword();
	if(service.validateUser(email,password)==true)
	{
		String role=service.getRole(email);
		session.setAttribute("email",email);
		if(role.equals("admin"))
		{
			return "adminHome";
		}
		else
		{
			Users user = service.getUser(email);
			boolean userStatus = user.isPremium();
			List<Song> songsList = songService.fetchAllSongs();
			model.addAttribute("songs",songsList);
			model.addAttribute("isPremium",userStatus);
			
			return "customerHome";
		}
	}
	else
	{
		
		return "home";
	}
	
}

@GetMapping("/logout")
public String logout(HttpSession session)

{
	session.invalidate();
	return "logout";
}
}

