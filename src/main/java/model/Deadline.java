package model;

import controller.ExcelConnection;

import java.time.LocalDate;

/**
 * 
 * @author Janus Klassen anvendes til at oprette deadlines/påmindelser i
 *         "hovedmenuen".
 */
public class Deadline {
	private String hvem;
	private String hvad;
	private LocalDate hvornår;
	private boolean klaret;
	private String ID;// Et ID kan sammensættes ud fra værelsesnummer og beboer.getNavn

	/**
	 * VIGTIGT - HUSK AT BRUG ID PARAMETERen RIGTIGT - ELLERS VIL DER HELE TIDEN
	 * BLIVE OPRETTET EN NY DEADLINE
	 * 
	 * @param hvem
	 *            : Hvem der er deadline for (værelse)((indstilling)
	 * @param hvad
	 *            : hvad deadlinen er.
	 * @param hvornår
	 *            : Hvornår skal deadlinen klares.
	 * @param ID
	 *            : ID skal altid angives
	 * 
	 */
	public Deadline(String hvem, String hvad, LocalDate hvornår, String ID, ExcelConnection ec) {

		this.hvem = hvem;
		this.hvad = hvad;
		this.hvornår = hvornår;

		klaret = false;
		if(ID == null) {
			this.ID = Integer.toString(ec.getDeadlines().size());
		} else
		this.ID = ID;

	}

	public String getHvem() {
		return hvem;
	}

	public void setHvem(String hvem) {
		this.hvem = hvem;
	}

	public String getHvad() {
		return hvad;
	}

	public void setHvad(String hvad) {
		this.hvad = hvad;
	}

	public LocalDate getHvornår() {
		return hvornår;
	}

	public void setHvornår(LocalDate hvornår) {
		this.hvornår = hvornår;
	}

	public boolean isKlaret() {
		return klaret;
	}

	public void setKlaret(boolean klaret) {
		this.klaret = klaret;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

}
