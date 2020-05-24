package model;

import controller.excel.ExcelConnection;

import java.time.LocalDate;

public class Beboer implements Comparable<Beboer>{
	private String navn;
	private String værelse;
	private LocalDate indflytningsdato;
	private LocalDate lejeaftalensUdløb;
	private String telefonnummer;
	private Enum<Studiekontrolstatus> studiekontrolstatus;
	private String uddannelsesretning;
	private String uddannelsessted;
	private LocalDate påbegyndtDato;
	private LocalDate forventetAfsluttetDato;
	private String statusPåStudiekontrol;
	
	/**
	 * @param navn
	 *            : Beboerens navn
	 * @param værelse
	 *            : Værelsesnummer
	 * @param indflytningsdato
	 *            : Dato beboeren har lejemålet fra.
	 * @param uddannelsessted
	 *            : en der udbyder uddannelsesretningen
	 * @param uddannelsesretning
	 *            : Den bestemte uddannelsesretning den studerende følger.
	 * @param påbegyndtDato
	 *            : påbegyndelsesdatoen
	 * @param forventetAfsluttetDato
	 *            : datoen for forventet afslutning af uddannelse
	 */
	public Beboer(String navn, String værelse, LocalDate indflytningsdato, LocalDate lejeaftalensUdløb,
			String telefonnummer, Enum<Studiekontrolstatus> studiekontrolstatus, String uddannelsessted,
			String uddannelsesretning, LocalDate påbegyndtDato, LocalDate forventetAfsluttetDato) {
		this.navn = navn;
		this.værelse = værelse;
		this.indflytningsdato = indflytningsdato;
		this.setLejeaftalensUdløb(lejeaftalensUdløb);
		this.telefonnummer = telefonnummer;
		if (studiekontrolstatus == null) {
			this.studiekontrolstatus = Studiekontrolstatus.IKKEIGANG;
			statusPåStudiekontrol = ExcelConnection.konverterEnumTilString((Studiekontrolstatus) studiekontrolstatus);
		} else {
		this.studiekontrolstatus = studiekontrolstatus;
		statusPåStudiekontrol = ExcelConnection.konverterEnumTilString((Studiekontrolstatus) studiekontrolstatus);
		}
		this.setUddannelsessted(uddannelsessted);
		this.setUddannelsesretning(uddannelsesretning);
		this.setForventetAfsluttetDato(forventetAfsluttetDato);
		this.setPåbegyndtDato(påbegyndtDato);
	}

	public String getNavn() {
		return navn;
	}

	public void setNavn(String navn) {
		this.navn = navn;
	}

	public String getVærelse() {
		return værelse;
	}

	public void setVærelse(String værelse) {
		this.værelse = værelse;
	}


	public LocalDate getIndflytningsdato() {
		return indflytningsdato;
	}

	public void setIndflytningsdato(LocalDate indflytningsdato) {
		this.indflytningsdato = indflytningsdato;
	}


	public String getTelefonnummer() {
		return telefonnummer;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	public Enum<Studiekontrolstatus> getStudiekontrolstatus() {
		return studiekontrolstatus;
	}

	public void setStudiekontrolstatus(Enum<Studiekontrolstatus> studiekontrolstatus) {
		this.studiekontrolstatus = studiekontrolstatus;
		statusPåStudiekontrol = ExcelConnection.konverterEnumTilString((Studiekontrolstatus) studiekontrolstatus);
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

	public LocalDate getPåbegyndtDato() {
		return påbegyndtDato;
	}

	public void setPåbegyndtDato(LocalDate påbegyndtDato) {
		this.påbegyndtDato = påbegyndtDato;
	}

	public LocalDate getForventetAfsluttetDato() {
		return forventetAfsluttetDato;
	}

	public void setForventetAfsluttetDato(LocalDate forventetAfsluttetDato) {
		this.forventetAfsluttetDato = forventetAfsluttetDato;
	}

	public String getStatusPåStudiekontrol() {
		return statusPåStudiekontrol;
	}

	public LocalDate getLejeaftalensUdløb() {
		return lejeaftalensUdløb;
	}

	public void setLejeaftalensUdløb(LocalDate lejeaftalensUdløb) {
		this.lejeaftalensUdløb = lejeaftalensUdløb;
	}


	@Override
	public int compareTo(Beboer o) {
		return this.værelse.compareTo(o.getVærelse());
	}
}
