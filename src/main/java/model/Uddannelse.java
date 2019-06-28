package model;

import java.time.LocalDate;

/**
 * Uddannelsesklassen anvendes til at tilkendegive den studerendes uddannelsesforhold.
 * @author Janus
 *
 */
public class Uddannelse {
	
	private String uddannelsesretning;
	private String uddannelsessted;
	private LocalDate påbegyndtDato;
	private LocalDate forventetAfsluttetDato;
	
	/**
	 * 
	 * @param uddannelsessted : en der udbyder uddannelsesretningen
	 * @param uddannelsesretning : Den bestemte uddannelsesretning den studerende følger.
	 * @param påbegyndtDato : påbegyndelsesdatoen
	 * @param forventetAfsluttetDato : datoen for forventet afslutning af uddannelse
	 */
	public Uddannelse(String uddannelsessted, String uddannelsesretning, LocalDate påbegyndtDato, LocalDate forventetAfsluttetDato) {
		this.uddannelsessted = uddannelsessted;
		this.uddannelsesretning = uddannelsesretning;
		this.forventetAfsluttetDato = forventetAfsluttetDato;
		this.påbegyndtDato = påbegyndtDato;
		
	}
	public String getUddannelsesretning() {
		return uddannelsesretning;
	}
	public void setUddannelsesretning(String uddannelsesretning) {
		this.uddannelsesretning = uddannelsesretning;
	}
	public String getUddannelsessted() {
		return uddannelsessted;
	}
	public void setUddannelsessted(String uddannelsessted) {
		this.uddannelsessted = uddannelsessted;
	}
	public LocalDate getForventetAfsluttetDato() {
		return forventetAfsluttetDato;
	}
	public void setForventetAfsluttetDato(LocalDate forventetAfsluttetDato) {
		this.forventetAfsluttetDato = forventetAfsluttetDato;
	}
	public LocalDate getPåbegyndtDato() {
		return påbegyndtDato;
	}
	public void setPåbegyndtDato(LocalDate påbegyndtDato) {
		this.påbegyndtDato = påbegyndtDato;
	}
	
	

}
