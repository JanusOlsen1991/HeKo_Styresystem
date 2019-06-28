package model;

import controller.ExcelConnection;

import java.time.LocalDate;

public class Værelsesudlejning {
	private LocalDate indflytningsdato;
	private String værelse;
	private String navn;
	private LocalDate behandlingsdato;
	private String behandlerInitialer;
	private String ID;
	/**
	 * @param indflytningsdato : den dato hvor en ny beboer skal overtage et værelse
	 * @param værelse : Værelsesnummer
	 * @param navn	: indflytterens navn
	 * @param behandlingsdato: datoen hvor sagen er blevet behandlet
	 * @param behandlerInitialer : initialerne på indstillingsrepræsentanten
	 */
	public Værelsesudlejning(LocalDate indflytningsdato, String værelse, String navn, LocalDate behandlingsdato,
			String behandlerInitialer, String ID, ExcelConnection ec) {
		this.indflytningsdato = indflytningsdato;
		this.værelse = værelse;
		this.navn = navn;
		this.behandlingsdato = behandlingsdato;
		this.behandlerInitialer = behandlerInitialer;
		if(ID==null) {
			//TODO Tænk over hvordan ID tildeles
			this.ID = Integer.toString(ec.getVærelsesudlejning().size());
		}
		else 
			this.ID=ID;
	}

	public LocalDate getIndflytningsdato() {
		return indflytningsdato;
	}
	public void setIndflytningsdato(LocalDate indflytningsdato) {
		this.indflytningsdato = indflytningsdato;
	}
	public String getVærelse() {
		return værelse;
	}
	public void setVærelse(String værelse) {
		this.værelse = værelse;
	}
	public String getNavn() {
		return navn;
	}
	public void setNavn(String navn) {
		this.navn = navn;
	}
	public LocalDate getBehandlingsdato() {
		return behandlingsdato;
	}
	public void setBehandlingsdato(LocalDate behandlingsdato) {
		this.behandlingsdato = behandlingsdato;
	}
	public String getBehandlerInitialer() {
		return behandlerInitialer;
	}
	public void setBehandlerInitialer(String behandlerInitialer) {
		this.behandlerInitialer = behandlerInitialer;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
