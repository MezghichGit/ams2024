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
import org.springframework.web.bind.annotation.RequestParam;

import com.sip.ams.entities.Article;
import com.sip.ams.services.ArticleService;
import com.sip.ams.services.ProviderService;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/articles/")

public class ArticleController {

	private ArticleService articleService;
	private ProviderService providerService;
	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/uploadArticles";

	@Autowired
	public ArticleController(ArticleService articleService, ProviderService providerService) {
		this.articleService = articleService;
		this.providerService = providerService;
	}

	@GetMapping("list")
	public String listProviders(Model model) {

		List<Article> articles = articleService.listArticle();
		if (articles.size() == 0)
			articles = null;
		model.addAttribute("articles", articles);
		return "article/listArticles";
	}

	@GetMapping("add")
	public String showAddArticleForm(Model model) {

		model.addAttribute("providers", providerService.listProvider()); // récupération des providers
		model.addAttribute("article", new Article());
		return "article/addArticle";
	}

	@PostMapping("add")
	// @ResponseBody
	public String addArticle(Model model, @Valid Article article, BindingResult result,
			@RequestParam(name = "providerId", required = false) Long p, @RequestParam("files") MultipartFile[] files) {

		if (result.hasErrors()) {
			System.out.println("nb error : " + result.getErrorCount());
			// on doit récupérer de nouveaux la liste des providers en cas d'erreur
			model.addAttribute("providers", providerService.listProvider());
			return "article/addArticle";
		}

		
		// fin upload
		articleService.saveArticle(article,p,files);
		return "redirect:list";

		// return article.getLabel() + " " +article.getPrice() + " " + p.toString();
	}

	@GetMapping("delete/{id}")
	public String deleteArticle(@PathVariable("id") long id) {
		articleService.findArticleById(id).orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
		articleService.deleteArticle(id);
		return "redirect:../list";
	}

	@GetMapping("edit/{id}")
	public String showArticleFormToUpdate(@PathVariable("id") long id, Model model) {
		try {
			Article article = articleService.findArticleById(id)
					.orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
			model.addAttribute("article", article);
			model.addAttribute("providers", providerService.listProvider());
			// model.addAttribute("idProvider", article.getProvider().getId());
		} catch (IllegalArgumentException ex) {
			return "article/500.html";
		}

		return "article/updateArticle";
	}

	@PostMapping("edit")
	public String updateArticle(@Valid Article article, BindingResult result, Model model,
			@RequestParam(name = "providerId", required = false) Long p, @RequestParam("files") MultipartFile[] files) {
		if (result.hasErrors()) {

			return "article/updateArticle";
		}

		
		articleService.updateArticle(article,p,files);

		return "redirect:list";
	}

	@GetMapping("show/{id}")
	public String showArticleDetails(@PathVariable("id") long id, Model model) {
		Article article = articleService.findArticleById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));

		model.addAttribute("article", article);

		return "article/showArticle";
	}

}
