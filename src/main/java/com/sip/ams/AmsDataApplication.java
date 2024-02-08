package com.sip.ams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sip.ams.controllers.ArticleController;

import java.io.File;
@SpringBootApplication
public class AmsDataApplication {

	public static void main(String[] args) {
		Etudiant e = new Etudiant();
		new File(ArticleController.uploadDirectory).mkdir();  //création du dossier sous static
		SpringApplication.run(AmsDataApplication.class, args);
		System.out.println("");
		
	}

}
