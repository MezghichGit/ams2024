package com.sip.ams.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sip.ams.entities.User;
import com.sip.ams.services.AccountService;





@Controller
@RequestMapping("/accounts/")

public class AccountController {

	@Autowired
	private final AccountService accountService;
	
	@Autowired
    public AccountController(AccountService accountService) {
        this.accountService =  accountService;
    }
	
	@GetMapping("list")
    public String listUsers(Model model) {
    	
    	List<User> users = (List<User>)  accountService.listUsers();
    	long nbr =  accountService.listUsers().size();
    	if(users.size()==0)
    		users = null;
        model.addAttribute("users", users);
        model.addAttribute("nbr", nbr);
        return "user/listUsers";
    }
	
	@GetMapping("enable/{id}/{email}")
	public String enableUserAcount(@PathVariable ("id") int id, @PathVariable ("email") String email) {
		accountService.enableAccount(id,email);
    	return "redirect:../../list";
    }
	
	@GetMapping("disable/{id}/{email}")
	public String disableUserAcount(@PathVariable ("id") int id, @PathVariable ("email") String email) {
		accountService.disableAccount(id,email);
    	return "redirect:../../list";
    }

	@PostMapping("updateRole")
	//@ResponseBody
	public String UpdateUserRole(@RequestParam ("id") int id, @RequestParam ("newrole")String newRole
			) {
    	
		accountService.updateRole(id, newRole);
		
		
    	return "redirect:list";
    }

	  
    
}
