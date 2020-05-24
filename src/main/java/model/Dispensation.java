package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 
 * @author Janus 
 * Klassen anvendes ved oprettelse af dispensationer til en beboer
 *
 */

public class Dispensation {
	private Beboer beboer;
	private String beboerNavn;
	private String beboerVærelse;
	private LocalDate startDato;
	private LocalDate slutDato;
	private ArrayList<Deadline> deadlines;//Skal den have dem eller bare referencen?
	private String ID;
//	private String deadlineIDs; // Alternativ til deadlines
	private boolean iGang;
	/**
	 * @param beboer
	 *            : beboeren der får dispensation
	 * @param startDato
	 *            : startdato for dispensation
	 * @param slutDato
	 *            : slutdato for dispensation
	 * @param deadlines
	 *            : eventuelle deadlines der skal tilføres. Håndtering af String
	 *            separation foregår før de gives til konstruktøren.
	 * @param ID
	 *            : anvendes hvis Dispensationen allerede har et ID
	 * @param deadlines:
	 *            Denne parameter gives hvis ikke der gives et ID.
	 */
	public Dispensation(Beboer beboer, LocalDate startDato, LocalDate slutDato, boolean iGang, String ID,
                        ArrayList<Deadline> deadlines) {
		this.beboer = beboer;
		this.startDato = startDato;
		this.slutDato = slutDato;
		this.iGang = iGang;
		this.deadlines = deadlines;
		if (ID == null) {
			this.ID = UUID.randomUUID().toString();
		} else {
			this.ID = ID;
		}
		this.setBeboerNavn(beboer.getNavn());
		this.setBeboerVærelse(beboer.getVærelse());
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public Beboer getBeboer() {
		return beboer;
	}

	public void setBeboer(Beboer beboer) {
		this.beboer = beboer;
	}

	public LocalDate getStartDato() {
		return startDato;
	}

	public void setStartDato(LocalDate startDato) {
		this.startDato = startDato;
	}

	public LocalDate getSlutDato() {
		return slutDato;
	}

	public void setSlutDato(LocalDate slutDato) {
		this.slutDato = slutDato;
	}

	public ArrayList<Deadline> getDeadlines() {
		return deadlines;
	}

	public void setDeadlines(ArrayList<Deadline> deadlines) {
		this.deadlines = deadlines;
	}

	public boolean isiGang() {
		return iGang;
	}

	public void setiGang(boolean iGang) {
		this.iGang = iGang;
	}

	public String getDeadlinesNumbers() {
		String s = "";
		int d = deadlines.size();
		//Hvis der er nogle deadlines
		if (d > 0) {

			for (int i = 0; i < d; i++) {
				if (d-1 > i) {
					s += deadlines.get(i).getID() + "#";
				}else
					s += deadlines.get(i).getID();
			}
			return s;
		} else
			return null;
	}

	public String getBeboerNavn() {
		return beboerNavn;
	}

	public void setBeboerNavn(String beboerNavn) {
		this.beboerNavn = beboerNavn;
	}

	public String getBeboerVærelse() {
		return beboerVærelse;
	}

	public void setBeboerVærelse(String beboerVærelse) {
		this.beboerVærelse = beboerVærelse;
	}

}
