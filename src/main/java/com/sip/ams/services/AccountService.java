package com.sip.ams.services;

import java.util.*;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//import com.sip.ams.controllers.HashSet;
//import com.sip.ams.controllers.Role;
import com.sip.ams.entities.Role;
import com.sip.ams.entities.User;
import com.sip.ams.repositories.RoleRepository;
import com.sip.ams.repositories.UserRepository;


@Service
public class AccountService {

	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired  // Spring va se charger de créer un objet qui implémente cette interface
	UserRepository userRepository ;
	@Autowired
	RoleRepository roleRepository;
	
	public List<User> listUsers()
	{
		return (List<User>) userRepository.findAll();	 // lister tous les roles de la base
	}

	
	
	public User enableAccount(int id, String email)
	{
		 sendEmail( email, true);
		 User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid User Id:" + id));
	     user.setActive(1);
	     return userRepository.save(user);
	}
	
	public User disableAccount(int id, String email)
	{
		 sendEmail( email, false);
		 User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid User Id:" + id));
	     user.setActive(0);
	     return userRepository.save(user);
	}
	
	public Optional<User> findById(int id) {
		return userRepository.findById(id);

	}

	public User save(User user) {
		return userRepository.save(user);
		
	}



	public void updateRole(int id, String newRole) {
		User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid User Id:" + id));
	     
		 Role userRole = roleRepository.findByRole(newRole);
		 
	     user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
	     
	     userRepository.save(user);
		
	}
	
	void sendEmail(String email, boolean state) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        if(state == true)
        {
        msg.setSubject("Account Has Been Activated");
        msg.setText("Hello, Your account has been activated. "
        		+ 
        		"You can log in : http://127.0.0.1:81/login"
        		+ " \n Best Regards!");
        }
        else
        {
        	msg.setSubject("Account Has Been disactivated");
            msg.setText("Hello, Your account has been disactivated.");
        }
        javaMailSender.send(msg);

    }


}
