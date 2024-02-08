package com.sip.ams.controllers;

import com.sip.ams.entities.User;
import com.sip.ams.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;


@Controller


public class RegisterController {


	@Autowired
	private  UserService userService;
  

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

   
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView  Register(@ModelAttribute("user") User user,BindingResult bindingResult) {
    ModelAndView modelAndView = new ModelAndView();
    User userExists = userService.findUserByEmail(user.getEmail());
    if (userExists != null) {
        bindingResult
                .rejectValue("email", "error.user",
                        "There is already a user registered with the email provided");
    }
    if (bindingResult.hasErrors()) {
        modelAndView.setViewName("registration");
    } else {
        userService.saveUser(user);
        modelAndView.addObject("successMessage", "User has been registered successfully");
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("registration");
    }
    return modelAndView;
    }

}
