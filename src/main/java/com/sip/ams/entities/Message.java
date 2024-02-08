package com.sip.ams.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank(message = "Name is mandatory")
	private String titre;
	
	@NotBlank(message = "Name is mandatory")
	private String description;
	
	@NotBlank(message = "Name is mandatory")
	private String emailSender;
	
	@NotBlank(message = "Name is mandatory")
	private Date dateEnvois;
	

	public long getId() {
		return id;
	}
	public Message(long id, String titre, String description, String emailSender, Date dateEnvois) {
		super();
		this.id = id;
		this.titre = titre;
		this.description = description;
		this.emailSender = emailSender;
		this.dateEnvois = dateEnvois;
	}
	
	public Message() {
		super();
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmailSender() {
		return emailSender;
	}
	public void setEmailSender(String emailSender) {
		this.emailSender = emailSender;
	}
	public Date getDateEnvois() {
		return dateEnvois;
	}
	public void setDateEnvois(Date dateEnvois) {
		this.dateEnvois = dateEnvois;
	}
	
	@Override
	public String toString() {
		return "Message [id=" + id + ", titre=" + titre + ", description=" + description + ", emailSender="
				+ emailSender + ", dateEnvois=" + dateEnvois + "]";
	}

}
