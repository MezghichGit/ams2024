package com.sip.ams.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sip.ams.controllers.ArticleController;
import com.sip.ams.entities.Article;
import com.sip.ams.entities.Provider;
import com.sip.ams.repositories.ArticleRepository;
import com.sip.ams.repositories.ProviderRepository;

@Service
public class ArticleService {

	@Autowired  // Spring va se charger de créer un objet qui implémente cette interface
	ArticleRepository articleRepository ;
	
	@Autowired  // Spring va se charger de créer un objet qui implémente cette interface
	ProviderRepository providerRepository ;
	
	public Article saveArticle(Article article, long p, MultipartFile[] files)
	{
		Provider provider = providerRepository.findById(p)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + p));
		article.setProvider(provider);
		// upload de l'image

		MultipartFile file = files[0];
		long id = new Date().getTime();
		Path fileNameAndPath = Paths.get(ArticleController.uploadDirectory, "" + id + file.getOriginalFilename());

		StringBuilder fileName = new StringBuilder();

		fileName.append("" + id + file.getOriginalFilename()); // nom du ficher
		try {
			Files.write(fileNameAndPath, file.getBytes()); // ecriture des bytes du fichier dans le nouveau emplacement
		} catch (IOException e) {
			e.printStackTrace();
		}

		article.setPicture(fileName.toString());

		return articleRepository.save(article);  // sauvegarder dans la base
		
	}
	
	public List<Article> listArticle()
	{
		return (List<Article>) articleRepository.findAll();	 // lister tous les articles de la base
	}

	public void deleteArticle(long id)  {
		// TO DO 1 : Delete de l'image
	    Optional<Article> oa =  findArticleById(id);
	    if(oa.isPresent())
	    {
	    	Article article = oa.get();
	    	String urlImage = article.getPicture();
	    	if(urlImage!=null) {
	    	Path path = Paths.get(ArticleController.uploadDirectory,urlImage);
	        
	    	try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	}
	    }
	     
	     // TO DO 2 :Delete de l'article de la base 
		articleRepository.deleteById(id);
	}
	
	public Optional<Article> findArticleById(long id)
	{
		return articleRepository.findById(id);
	}
	
	public Article updateArticle(Article article, long p, MultipartFile[] files)
	{
		Provider provider = providerRepository.findById(p)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + p));
		article.setProvider(provider);

		// upload de l'image

		MultipartFile file = files[0];
		if (!file.isEmpty()) {
			// System.out.println("Article name : " +article.getPicture());
			// System.out.println("File name : " +file.getOriginalFilename());
			long id = new Date().getTime();
			Path fileNameAndPath = Paths.get(ArticleController.uploadDirectory, "" + id + file.getOriginalFilename());

			StringBuilder fileName = new StringBuilder();

			fileName.append("" + id + file.getOriginalFilename()); // nom du ficher
			try {
				Files.write(fileNameAndPath, file.getBytes()); // ecriture des bytes du fichier dans le nouveau
																// emplacement

				// delete de l'ancienne image

				String urlImage = article.getPicture();
				if (urlImage != null) {
					Path path = Paths.get(ArticleController.uploadDirectory, urlImage);

					try {
						Files.deleteIfExists(path);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// fin delete picture

			} catch (IOException e) {
				e.printStackTrace();
			}

			article.setPicture(fileName.toString());
		}
		//
		// article.setPicture(fileName.toString());
		return articleRepository.save(article);
	}
	
}
