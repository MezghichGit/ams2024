package com.sip.ams.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sip.ams.entities.Article;
import com.sip.ams.entities.Provider;
import com.sip.ams.services.ProviderService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/providers")
public class ProviderController {
	
	 
	ProviderService service ;//= new ProviderService();
	
	
	@Autowired  //injection de dépendances
	public ProviderController(ProviderService service) {
		this.service = service;
	}

	@RequestMapping("/list")
	//@ResponseBody
	public String list(Model model) {
		List<Provider> res = service.listProvider();
		//return res.toString();
		if(res.size()==0)
			res = null;
		model.addAttribute("providers", res);
		 return "provider/listProviders";
	}
	
	@GetMapping("add")
    public String showAddProviderForm(Model model) {
    	Provider provider = new Provider();// object dont la valeur des attributs par defaut
    	model.addAttribute("provider", provider);
        return "provider/addProvider";
    }
    
    @PostMapping("add")
    public String addProvider(@Valid Provider provider, BindingResult result) {
        if (result.hasErrors()) {
            return "provider/addProvider";
        }
        service.addProvider(provider);
        return "redirect:list";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteProvider(@PathVariable("id")long id)
    {
    	try {
    	 service.findProviderById(id)
         .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + id));
    	 service.deleteProvider(id);
    	}
    	catch(IllegalArgumentException ex)
    	{
    		return "provider/500.html";
    	}
  
    	
    	return "redirect:../list";	
    }
    
    @GetMapping("edit/{id}")
    public String showProviderFormToUpdate(@PathVariable("id") long id, Model model) {
    	Provider provider  = null;
    	try {
    		provider= service.findProviderById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
    	}
    	catch(IllegalArgumentException ex)
    	{
    		return "provider/500.html";
    	}
        model.addAttribute("provider", provider);
        
        return "provider/updateProvider";
    }


    
    @PostMapping("update")
    public String updateProvider(@Valid Provider provider, BindingResult result, Model model) {
    	
    	
    	service.addProvider(provider);
    	return"redirect:list";
    	
    }
    
    @GetMapping("show/{id}")
	public String showProvider(@PathVariable("id") long id, Model model) {
		Provider provider = service.findProviderById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		List<Article> articles = service.findArticlesByProvider(id);
		for (Article a : articles)
			System.out.println("Article = " + a.getLabel());
		
		if(articles.size()==0)
		{
			articles=null;
		}
		model.addAttribute("articles", articles);
		model.addAttribute("provider", provider);
		return "provider/showProvider";
	}




}
