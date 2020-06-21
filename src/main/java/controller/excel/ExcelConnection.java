package controller.excel;

import model.*;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

//import javafx.scene.control.Cell;

/**
 * Klassen binder modellageret sammen med brugerinteraktionen i viewet
 * 
 * @author Janus
 *
 */

public class ExcelConnection {
	private ArrayList<Beboer> beboere = new ArrayList<Beboer>();
	private ArrayList<Deadline> deadlines = new ArrayList<Deadline>();
	private ArrayList<Beboer> fremlejere = new ArrayList<Beboer>();
	private ArrayList<Studiekontrol> studiekontroller = new ArrayList<Studiekontrol>();
	private ArrayList<Værelsesudlejning> værelsesudlejning = new ArrayList<Værelsesudlejning>();
	private ArrayList<Dispensation> dispensationer = new ArrayList<Dispensation>();
	private final String filnavn;


	public ExcelConnection(String excelplacering){
		this.filnavn = excelplacering;
		
				try {
					hentDeadlinesFraExcel();
					new Thread(() -> {
						hentBeboereFraExcel();
						hentFremlejerFraExcel();
						hentDispensationerFraExcel();
						hentStudiekontrollerfraExcel();
						hentVærelsesudlejningFraExcel();
					}).start();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
	public ArrayList<Dispensation> getDispensationer() {
		return dispensationer;
	}

	public void setDispensationer(ArrayList<Dispensation> dispensationer) {
		this.dispensationer = dispensationer;
	}

	public ArrayList<Deadline> getDeadlines() {
		return deadlines;
	}

	public void setDeadlines(ArrayList<Deadline> deadlines) {
		this.deadlines = deadlines;
	}

	public ArrayList<Beboer> getFremlejere() {
		return fremlejere;
	}

	public void setFremlejere(ArrayList<Beboer> fremlejere) {
		this.fremlejere = fremlejere;
	}

	public ArrayList<Studiekontrol> getStudiekontroller() {
		return studiekontroller;
	}

	public void setStudiekontroller(ArrayList<Studiekontrol> studiekontroller) {
		this.studiekontroller = studiekontroller;
	}

	public ArrayList<Værelsesudlejning> getVærelsesudlejning() {
		return værelsesudlejning;
	}

	public void setVærelsesudlejning(ArrayList<Værelsesudlejning> værelsesudlejning) {
		this.værelsesudlejning = værelsesudlejning;
	}

	public void setBeboere(ArrayList<Beboer> beboere) {
		this.beboere = beboere;
	}

	

	public void hentDispensationerFraExcel() {
		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);

			int startRække = 1;// Starter på 1 for ikke at tage overskrifter med

			int slutRække = workbook.getSheetAt(1).getLastRowNum();

			for (int i = startRække; i <= slutRække; i++) {
				Row row = workbook.getSheetAt(1).getRow(i);

				int kollonnenummer = 0;

				String værelse = row.getCell(kollonnenummer).getStringCellValue();

				++kollonnenummer; // Tælles op da "navn" ikke skal hentes fra filen, men en hel beboer skal
									// tilføres
				Beboer beboer = findBeboer(værelse);

				Date d = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate dispStart = konverterDateTilLocalDate(d);

				Date d1 = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate dispSlut = konverterDateTilLocalDate(d1);

				String dispID = row.getCell(++kollonnenummer).getStringCellValue();
				
				String deadlinesID = null;
				if(row.getCell(kollonnenummer+1).getStringCellValue() != null || ! row.getCell(kollonnenummer+1).getStringCellValue().equals(""))
				deadlinesID = row.getCell(++kollonnenummer).getStringCellValue();

				boolean iGang = row.getCell(++kollonnenummer).getBooleanCellValue();

				ArrayList<Deadline> dispDeadlines = findDispensationsDeadlines(deadlinesID); // Denne skal sættes i gang
																								// ved en metode til at
				// separere '.' og hente tilhørende deadlines

				Dispensation dispensation = new Dispensation(beboer, dispStart, dispSlut, iGang, dispID, dispDeadlines);
				dispensationer.add(dispensation);

			}
			fis.close();
			workbook.close();
		} catch (EncryptedDocumentException |

				IOException e) {
			System.out.println("Filen kan ikke findes");
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Metoden skal anvendes til at finde deadlines der hører til en bestemt
	 * Dispensation.
	 * 
	 * @param deadlinesID
	 *            : String der inderholder samtlige id'er på deadlines tilhørende
	 *            dispensationen separeret med et '-'
	 * @return ArrayList der indeholder deadlines
	 */
	private ArrayList<Deadline> findDispensationsDeadlines(String deadlinesID) {
		ArrayList<Deadline> list = new ArrayList<Deadline>();

		String[] temp = deadlinesID.split("#");

		for (int i = 0; i < temp.length; i++) {
			String s = temp[i];

			for (int j = 0; j < deadlines.size(); j++) {
				if (s.equals(deadlines.get(j).getID())) {
					list.add(deadlines.get(j));
				}
			}
		}
		return list;
	}

	/**
	 * Anvendes til at finde en bestemt beboer i beboerarrayet
	 * 
	 * @param værelsesNummer
	 *            : værelsesnummeret på den beboer der skal findes
	 * @return
	 */
	public Beboer findBeboer(String værelsesNummer) {
		for (int i = 0; i < beboere.size(); i++) {
			if (beboere.get(i).getVærelse().equals(værelsesNummer)) {
				return beboere.get(i);
			}
		}
		return null;

	}
	public ArrayList<Beboer> findBeboereTilOpretStudiekontrol(int månedsnummer){

		ArrayList<Beboer> temp = new ArrayList<Beboer>();
		for(Beboer b: beboere) {
			if (b.getLejeaftalensUdløb().getMonthValue() == månedsnummer) {//TODO evt. check om den tager forskellige år
				b.setStudiekontrolstatus(Studiekontrolstatus.IKKEAFLEVERET);
				
				opretBeboerIExcel(b);
				temp.add(b);
			}
		}
		return temp;
	}

	public void hentStudiekontrollerfraExcel() {
		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);

			int startRække = 1;// +1 for ikk at tageoverskriften med

			int slutRække = workbook.getSheetAt(4).getLastRowNum();


			// Opretter studiekontrolelementerne uden beboere der skal indgå
			for (int i = startRække; i <= slutRække; i++) {

				Row row = workbook.getSheetAt(4).getRow(i);
				
				//Tjekker om studiekontrollen er i gang
				boolean afsluttet = row.getCell(4).getBooleanCellValue();

				if (afsluttet == false) {

					int kollonnenummer = 0;

					Date d1 = row.getCell(kollonnenummer).getDateCellValue();
					LocalDate afleveringsfrist = konverterDateTilLocalDate(d1);

					Date d2 = row.getCell(++kollonnenummer).getDateCellValue();
					LocalDate påmindelsesdato = konverterDateTilLocalDate(d2);

					Date d3 = row.getCell(++kollonnenummer).getDateCellValue();
					LocalDate begyndelsesdato = konverterDateTilLocalDate(d3);

					int månedsnummer = (int) row.getCell(++kollonnenummer).getNumericCellValue(); // de +4 giver den måned der
													//TODO check om tidligere udgave med %12 bør anvendes i stedet								// påbegyndes
																					// for.
					Boolean afsluttet2 = row.getCell(++kollonnenummer).getBooleanCellValue();

					String studiekontrolID = row.getCell(++kollonnenummer).getStringCellValue();
					
					Studiekontrol studiekontrol = new Studiekontrol(null, afleveringsfrist, påmindelsesdato,
							begyndelsesdato, månedsnummer, afsluttet2, studiekontrolID);
					studiekontroller.add(studiekontrol);
				}
			}//TODO lav til separat metode der kaldes i ovenstående loop
			// tilføjer beboere til studiekontroller hvis der er nogen
			if (studiekontroller.size() > 0) {

				for (int j = 0; j < studiekontroller.size(); j++) {
					int måned = studiekontroller.get(j).getMånedsnummer();
					ArrayList<Beboer> list = new ArrayList<Beboer>();

					for (int i = 0; i < beboere.size(); i++) {
						if (beboere.get(i).getLejeaftalensUdløb().getMonthValue() == måned) {
							if (beboere.get(i).getStudiekontrolstatus() != Studiekontrolstatus.IKKEIGANG)
								list.add(beboere.get(i));
						}
						// Læg for loop der håndterer fremlejere?
					}
					studiekontroller.get(j).setBeboere(list);
				}
			}

		} catch (EncryptedDocumentException | IOException e) {

			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}

		// Første to loops henter alle de igangværende studiekontroller ind

	}
	public String findMånedsNavn(int månedsNummer) {
		switch (månedsNummer) {
		case 0:
			return "December";
		case 1:
			return "Januar";
		case 2:
			return "Februar";
		case 3:
			return "Marts";
		case 4:
			return "April";
		case 5:
			return "Maj";
		case 6:
			return "Juni";
		case 7:
			return "Juli";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "Oktober";
		case 11:
			return "November";
		case 12:
			return "December";
		default:
			return "Fejl";
		}
		
	}
	public int findMånedsNummer(String månedsNavn) {
		switch (månedsNavn) {
		case "Januar":
			return 1;
		case "Februar":
			return 2;
		case "Marts":
			return 3;
		case "April":
			return 4;
		case "Maj":
			return 5;
		case "Juni":
			return 6;
		case "Juli":
			return 7;
		case "August":
			return 8;
		case "September":
			return 9;
		case "Oktober":
			return 10;
		case "November":
			return 11;
		case "December":
			return 12;
		default:
			return 0;
		}
	}

	/**
	 * Metoden henter både udlejede og ikke udlejede værelser og gemmer dem i en
	 * ArrayList ved navn værelsesudlejning
	 */
	public void hentVærelsesudlejningFraExcel() {

		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);

			int startRække = 1;// +1 for ikk at tage overskriften med

			int slutRække = workbook.getSheetAt(5).getLastRowNum();

			for (int i = startRække; i <= slutRække; i++) {
				Row row = workbook.getSheetAt(5).getRow(i);

				int kollonnenummer = 0;

				Date d1 = row.getCell(kollonnenummer).getDateCellValue();
				LocalDate indflytningsdato = konverterDateTilLocalDate(d1);

				String værelse;

				værelse = row.getCell(++kollonnenummer).getStringCellValue();

				String navn = "";
				try{
				if(row.getCell(kollonnenummer+1)!=null)
					navn = row.getCell(++kollonnenummer).getStringCellValue();
					System.out.println(navn);}
				 catch(NullPointerException e) {
					e.printStackTrace();
				 }

				// kan ikke hente "tomme" date celler, så det tjekkes der for
				LocalDate behandlingsdato;
				Cell c = row.getCell(++kollonnenummer);
				if (c != null) {
					Date d2 = row.getCell(kollonnenummer).getDateCellValue();
					behandlingsdato = konverterDateTilLocalDate(d2);
				} else
					behandlingsdato = null;

				String behandlerinitialer = "";
				try {
					behandlerinitialer = row.getCell(++kollonnenummer).getStringCellValue();
				} catch
				(NullPointerException e){
					e.printStackTrace();
				}

				String ID = "";
				try {
					 ID = row.getCell(++kollonnenummer).getStringCellValue();
				}catch (NullPointerException e){
					e.printStackTrace();
				}
				Værelsesudlejning v = new Værelsesudlejning(indflytningsdato, værelse, navn, behandlingsdato,
						behandlerinitialer, ID, null);

				værelsesudlejning.add(v);

			}
			fis.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Metoden konverterer String til Enum
	 * 
	 * @param s
	 *            String i forbindelse med studiekontrolstatus for beboer som skal
	 *            konverteres til ENUM
	 * @return Enum tilsvarende string der gemmes i Exceldokumentet
	 */
	public Enum<Studiekontrolstatus> konverterStringTilEnum(String s) {
		switch (s) {
		case "Ikke i gang":
			return Studiekontrolstatus.IKKEIGANG;
		case "Modtaget, ikke godkendt":
			return Studiekontrolstatus.MODTAGETIKKEGODKENDT;
		case "Ikke Modtaget":
			return Studiekontrolstatus.IKKEAFLEVERET;
		case "Sendt til boligselskab":
			return Studiekontrolstatus.SENDTTILBOLIGSELSKAB;
		case "Godkendt":
			return Studiekontrolstatus.GODKENDT;
		case "Modtaget og godkendt, men afslutter uddannelse." :
			return Studiekontrolstatus.MODTAGETAFSLUTTERUDDANNELSE;
		case "Dispensation" :
			return Studiekontrolstatus.DISPENSATION;
		case "Fremlejer værelse" :
			return Studiekontrolstatus.FREMLEJER;

		default:
			return null;
		}

	}

	/**
	 * Metoden konverterer Enum til String
	 * 
	 * @param studiekontrolstatus
	 *            Den Enum der skal konverteres til en string der kan gemmes i
	 *            Exceldokumentet
	 * @return String på studiekontrolstatus
	 */
	public static String konverterEnumTilString(Studiekontrolstatus studiekontrolstatus) {
		return studiekontrolstatus.status;
	}


	/**
	 * Henter fremelejer fra excel dokument
	 */
	public void hentFremlejerFraExcel() {
		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);

			;
			int startRække = 1;// 1 for ikk at tageoverskriften med

			int slutRække = workbook.getSheetAt(2).getLastRowNum();

			for (int i = startRække; i <= slutRække; i++) {
				Row row = workbook.getSheetAt(2).getRow(i);
				// Load de forskellige ting til "beboere her"
				int kollonnenummer = 0;

				String værelse = row.getCell(kollonnenummer).getStringCellValue();

				String navn = row.getCell(++kollonnenummer).getStringCellValue();

				Date d1 = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate fremlejeStartdato = konverterDateTilLocalDate(d1);

				String uddannelsessted = row.getCell(++kollonnenummer).getStringCellValue();

				String uddannelsesretning = row.getCell(++kollonnenummer).getStringCellValue();

				Date d2 = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate uddStart = konverterDateTilLocalDate(d2);

				Date d3 = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate uddSlut = konverterDateTilLocalDate(d3);

				Date d4 = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate fremlejeSlutdato = konverterDateTilLocalDate(d4);

				String telefonnummer = row.getCell(++kollonnenummer).getStringCellValue();

				Enum<Studiekontrolstatus> studiekontrolstatus = konverterStringTilEnum(
						row.getCell(++kollonnenummer).getStringCellValue());

				Beboer beboer = new Beboer(navn, værelse, fremlejeStartdato, fremlejeSlutdato, telefonnummer,
						studiekontrolstatus, uddannelsessted, uddannelsesretning, uddStart, uddSlut);
				this.fremlejere.add(beboer);


			}
			fis.close();
			workbook.close();
		} catch (EncryptedDocumentException | IOException e) {
			System.out.println("Filen kan ikke findes");
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Henter deadlines fra Excel
	 */
	public void hentDeadlinesFraExcel() {

		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);

			int startRække = 1;// 1 for ikk at tageoverskriften med

			int slutRække = workbook.getSheetAt(3).getLastRowNum();

			for (int i = startRække; i <= slutRække; i++) {
				Row row = workbook.getSheetAt(3).getRow(i);
				// Load de forskellige ting til "beboere her"
				int kollonnenummer = 0;

				String hvem = row.getCell(kollonnenummer).getStringCellValue();

				String hvad = row.getCell(++kollonnenummer).getStringCellValue();

				Date d = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate hvornår = konverterDateTilLocalDate(d);

				boolean klaret = row.getCell(++kollonnenummer).getBooleanCellValue();

				String ID = row.getCell(++kollonnenummer).getStringCellValue();

				// Sidste del i deadline er null, da der altid vil være et ID på når der loades
				// fra excel
				Deadline deadline = new Deadline(hvem, hvad, hvornår, ID);
				deadline.setKlaret(klaret);
				deadlines.add(deadline);

			}
			fis.close();

			workbook.close();
		} catch (EncryptedDocumentException | IOException e) {
			System.out.println("Filen kan ikke findes");
			e.printStackTrace();
		} catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
			e.printStackTrace();
		}

	}

	public void opretDeadlineIExcel(Deadline deadline) {
		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);
			int startRække = 1;
			int slutRække = workbook.getSheetAt(3).getLastRowNum();
			boolean deadlineFindes = false;

			// Loop gennem excel dokumentet og find rækkepladsen
			// if(slutRække)
			for (int i = startRække; i <= slutRække; i++) {
				String s = workbook.getSheetAt(3).getRow(i).getCell(4).getStringCellValue();
				// Hvis det passer, så skriv til værelsesnummeret
				if (deadline.getID() != null) {

					if (s.equals(deadline.getID())) {

						int celleNr = 0;
						workbook.getSheetAt(3).getRow(i).getCell(celleNr).setCellValue(deadline.getHvem());

						workbook.getSheetAt(3).getRow(i).getCell(++celleNr).setCellValue(deadline.getHvad());

						Date d1 = konverterLocalDateTilDate(deadline.getHvornår());
						workbook.getSheetAt(3).getRow(i).getCell(++celleNr).setCellValue(d1);

						workbook.getSheetAt(3).getRow(i).getCell(++celleNr).setCellValue(deadline.isKlaret());

						workbook.getSheetAt(3).getRow(i).getCell(++celleNr).setCellValue(deadline.getID());

						deadlineFindes = true;
					}
				}
			}
			if (deadlineFindes == false) {
				int celleNr = 0;
				workbook.getSheetAt(3).createRow(slutRække + 1);
				workbook.getSheetAt(3).getRow(slutRække + 1).createCell(celleNr).setCellValue(deadline.getHvem());
				workbook.getSheetAt(3).getRow(slutRække + 1).createCell(++celleNr).setCellValue(deadline.getHvad());

				Date d1 = konverterLocalDateTilDate(deadline.getHvornår());
				workbook.getSheetAt(3).getRow(slutRække + 1).createCell(++celleNr).setCellValue(d1);

				workbook.getSheetAt(3).getRow(slutRække + 1).createCell(++celleNr).setCellValue(deadline.isKlaret());

				workbook.getSheetAt(3).getRow(slutRække + 1).createCell(++celleNr).setCellValue(deadline.getID());

			}

			// you have to close the input stream FIRST before writing to the same file.
			fis.close();

			// save your changes to the same file.
			workbook.write(new FileOutputStream(filnavn));
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void opretDispensationIExcel(Dispensation dispensation) {
		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);
			int startRække = 1;
			int slutRække = workbook.getSheetAt(1).getLastRowNum();
			boolean dispensationFindes = false;

			// Loop gennem excel dokumentet og find rækkepladsen
			for (int i = startRække; i <= slutRække; i++) {
				String s = workbook.getSheetAt(1).getRow(i).getCell(4).getStringCellValue();
				// Hvis det passer, så skriv til værelsesnummeret
				if (dispensation.getID() != null) {

					if (s.equals(dispensation.getID())) {
						int celleNr = 0;
						workbook.getSheetAt(1).getRow(i).getCell(celleNr)
								.setCellValue(dispensation.getBeboer().getVærelse());

						workbook.getSheetAt(1).getRow(i).getCell(++celleNr)
								.setCellValue(dispensation.getBeboer().getNavn());

						Date d1 = konverterLocalDateTilDate(dispensation.getStartDato());
						workbook.getSheetAt(1).getRow(i).getCell(++celleNr).setCellValue(d1);

						Date d2 = konverterLocalDateTilDate(dispensation.getSlutDato());
						workbook.getSheetAt(1).getRow(i).getCell(++celleNr).setCellValue(d2);

						workbook.getSheetAt(1).getRow(i).getCell(++celleNr).setCellValue(dispensation.getID());

						String deadlinesStrings = dispensation.getDeadlinesNumbers();
						workbook.getSheetAt(1).getRow(i).getCell(++celleNr).setCellValue(deadlinesStrings);

						workbook.getSheetAt(1).getRow(i).getCell(++celleNr).setCellValue(dispensation.isiGang());

						dispensationFindes = true;
					}
				}
			}
			if (dispensationFindes == false) {

				int celleNr = 0;
				workbook.getSheetAt(1).createRow(slutRække + 1);

				workbook.getSheetAt(1).getRow(slutRække + 1).createCell(celleNr)
						.setCellValue(dispensation.getBeboer().getVærelse());

				workbook.getSheetAt(1).getRow(slutRække + 1).createCell(++celleNr)
						.setCellValue(dispensation.getBeboer().getNavn());

				Date d1 = konverterLocalDateTilDate(dispensation.getStartDato());
				workbook.getSheetAt(1).getRow(slutRække + 1).createCell(++celleNr).setCellValue(d1);

				Date d2 = konverterLocalDateTilDate(dispensation.getSlutDato());
				workbook.getSheetAt(1).getRow(slutRække + 1).createCell(++celleNr).setCellValue(d2);

				workbook.getSheetAt(1).getRow(slutRække + 1).createCell(++celleNr).setCellValue("disp" + slutRække); //

				String deadlinesStrings = dispensation.getDeadlinesNumbers();
				workbook.getSheetAt(1).getRow(slutRække + 1).createCell(++celleNr).setCellValue(deadlinesStrings);

				workbook.getSheetAt(1).getRow(slutRække + 1).createCell(++celleNr).setCellValue(dispensation.isiGang());

			}

			// you have to close the input stream FIRST before writing to the same file.
			fis.close();

			// save your changes to the same file.
			workbook.write(new FileOutputStream(filnavn));
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Metoden opretter fanerne og overskrifter i en excelfil. Skal anvendes hvis
	 * filen ikke kan findes. VIRKER MEN MÆRKELIGT VED SHEET 2+++
	 */
	public void createExcelFile() {
		Workbook wb = new XSSFWorkbook();
		int start = 0;

		// Overskrifter til Beboerliste sheet
		Sheet sheet1 = wb.createSheet("Beboerliste");
		Row row1 = sheet1.createRow(0);

		row1.createCell(start).setCellValue("Værelse");
		row1.createCell(++start).setCellValue("Navn");
		row1.createCell(++start).setCellValue("Indflytningsdato");
		row1.createCell(++start).setCellValue("Uddannelsessted");
		row1.createCell(++start).setCellValue("Uddannelsesretning");
		row1.createCell(++start).setCellValue("Uddannelse påbegyndt:");
		row1.createCell(++start).setCellValue("Uddannelse forventes afsluttet");
		row1.createCell(++start).setCellValue("Udløbsdato på lejeaftale");
		row1.createCell(++start).setCellValue("Telefonnummer");
		row1.createCell(++start).setCellValue("Studiekontrolstatus");

		start = 0;

		// overksrifter til Dispensationer sheet
		Sheet sheet2 = wb.createSheet("Dispensationer");
		Row row2 = sheet2.createRow(0);
		// For at finde de tilhørende deadlines, så kan der ud fra værelsesnummer om
		// navn sammensættes en key der gives som søgebetingelse deadlines
		row2.createCell(start).setCellValue("Værelse");
		row2.createCell(++start).setCellValue("Navn");
		row2.createCell(++start).setCellValue("StartDato");
		row2.createCell(++start).setCellValue("SlutDato");
		row2.createCell(++start).setCellValue("Dispensations ID");
		row2.createCell(++start).setCellValue("Deadline ID'er");
		row2.createCell(++start).setCellValue("I gang");

		start = 0;

		// Overskrifter til fremlejer-sheet
		Sheet sheet3 = wb.createSheet("Fremlejer");
		Row row3 = sheet3.createRow(0);

		row3.createCell(start).setCellValue("Værelse");// Noget må bugge siden den kører som den skal med ++ her?
		row3.createCell(++start).setCellValue("navn");
		row3.createCell(++start).setCellValue("Fremleje Startdato");
		row3.createCell(++start).setCellValue("uddannelsessted");
		row3.createCell(++start).setCellValue("uddannelsesretning");
		row3.createCell(++start).setCellValue("uddannelsesstart");
		row3.createCell(++start).setCellValue("uddannelse afsluttes:");
		row3.createCell(++start).setCellValue("Fremleje slutdato");
		row3.createCell(++start).setCellValue("Telefonnummer");
		row3.createCell(++start).setCellValue("Studiekontrolstatus");

		start = 0;

		// Overskrifter til deadlines sheet
		Sheet sheet4 = wb.createSheet("Deadlines");
		Row row4 = sheet4.createRow(0);

		row4.createCell(start).setCellValue("Hvem");
		row4.createCell(++start).setCellValue("Hvad");
		row4.createCell(++start).setCellValue("Hvornår");
		row4.createCell(++start).setCellValue("Status");
		row4.createCell(++start).setCellValue("ID");

		start = 0;

		// Overskrifter til studiekontroller sheet
		Sheet sheet5 = wb.createSheet("Studiekontroller");
		Row row5 = sheet5.createRow(0);

		row5.createCell(start).setCellValue("Afleveringsfrist");
		row5.createCell(++start).setCellValue("Påmindelsesdato");
		row5.createCell(++start).setCellValue("Påbegyndelsesdato");
		row5.createCell(++start).setCellValue("Månedsnummer for den påbegyndte studiekontrol");
		row5.createCell(++start).setCellValue("afsluttet?");
		row5.createCell(++start).setCellValue("studiekontrol ID");

		start = 0;

		// Overskrifter til værelsesudlejning
		Sheet sheet6 = wb.createSheet("Værelsesudlejning");
		Row row6 = sheet6.createRow(0);
		row6.createCell(start).setCellValue("indflytningsdato");
		row6.createCell(++start).setCellValue("Værelse");
		row6.createCell(++start).setCellValue("Navn");
		row6.createCell(++start).setCellValue("Behandlingsdato");
		row6.createCell(++start).setCellValue("behandlerInitialer");
		row6.createCell(++start).setCellValue("Udlejnings ID");


		try {
			FileOutputStream stream = new FileOutputStream(filnavn);
			wb.write(stream);
			stream.close();
			wb.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void createStudiekontrolsExcelFile(String filplacering, ArrayList<Beboer> beboere) {
		Workbook wb = new XSSFWorkbook();
		int start = 0;

		// Overskrifter til Beboerliste sheet
		Sheet sheet1 = wb.createSheet("Beboerliste");
		Row row1 = sheet1.createRow(0);

		row1.createCell(start).setCellValue("Værelse");
		row1.createCell(++start).setCellValue("Navn");
		row1.createCell(++start).setCellValue("Udløbsmåned på lejeaftale");
		


		try {
			FileOutputStream stream = new FileOutputStream(filplacering);
			wb.write(stream);
			stream.close();
			wb.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		//Skriver beboere til excelfil
		for(int i = 0; i< beboere.size(); i++) {
			Beboer beboer = beboere.get(i);
			skrivBeboereTilStudiekontrol(beboer, filplacering);
		}
		
	}
	private void skrivBeboereTilStudiekontrol(Beboer beboer, String filnavn) {
		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);
			int slutRække = workbook.getSheetAt(0).getLastRowNum();


				workbook.getSheetAt(0).createRow(slutRække + 1);
				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(0).setCellValue(beboer.getVærelse());
				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(1).setCellValue(beboer.getNavn());
				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(2).setCellValue(beboer.getLejeaftalensUdløb().getMonth().toString());//


			
			fis.close();

			// save your changes to the same file.
			workbook.write(new FileOutputStream(filnavn));
			workbook.close();
			
	} catch(Exception e) {
		e.printStackTrace();
		}
	}

	/**
	 * Metoden tjekker først om værelset findes. og ellers skriver den
	 * værelsesnummeret ind nederst på listen
	 * 
	 * @param beboer
	 *            : Den beboer der oprettesi excelfilen
	 */
	public void opretBeboerIExcel(Beboer beboer) { //
		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);
			int startRække = 1;
			int slutRække = workbook.getSheetAt(0).getLastRowNum();
			boolean beboerFindes = false;
			// Loop gennem excel dokumentet og find rækkepladsen
			for (int i = startRække; i <= slutRække; i++) {
				String s = workbook.getSheetAt(0).getRow(i).getCell(0).getStringCellValue();
				// Hvis det passer, så skriv til værelsesnummeret
				if (s.equals(beboer.getVærelse())) {
					int celleNr = 0;
					workbook.getSheetAt(0).getRow(i).getCell(celleNr).setCellValue(beboer.getVærelse());

					workbook.getSheetAt(0).getRow(i).getCell(++celleNr).setCellValue(beboer.getNavn());

					Date d1 = konverterLocalDateTilDate(beboer.getIndflytningsdato());
					workbook.getSheetAt(0).getRow(i).getCell(++celleNr).setCellValue(d1);

					workbook.getSheetAt(0).getRow(i).getCell(++celleNr).setCellValue(beboer.getUddannelsessted());

					workbook.getSheetAt(0).getRow(i).getCell(++celleNr).setCellValue(beboer.getUddannelsesretning());

					Date d2 = konverterLocalDateTilDate(beboer.getPåbegyndtDato());
					workbook.getSheetAt(0).getRow(i).getCell(++celleNr).setCellValue(d2);

					Date d3 = konverterLocalDateTilDate(beboer.getForventetAfsluttetDato());
					workbook.getSheetAt(0).getRow(i).getCell(++celleNr).setCellValue(d3);

					Date d4 = konverterLocalDateTilDate(beboer.getLejeaftalensUdløb());
					workbook.getSheetAt(0).getRow(i).getCell(++celleNr).setCellValue(d4);

					workbook.getSheetAt(0).getRow(i).getCell(++celleNr).setCellValue(beboer.getTelefonnummer());

					String s1 = konverterEnumTilString((Studiekontrolstatus) beboer.getStudiekontrolstatus());
					workbook.getSheetAt(0).getRow(i).getCell(++celleNr).setCellValue(s1);

					beboerFindes = true;

				}
			}
			System.out.println("Beboeren findes " + beboerFindes);

			if (beboerFindes == false) {

				workbook.getSheetAt(0).createRow(slutRække + 1);
				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(0).setCellValue(beboer.getVærelse());
				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(1).setCellValue(beboer.getNavn());

				Date d1 = konverterLocalDateTilDate(beboer.getIndflytningsdato());
				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(2).setCellValue(d1);

				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(3).setCellValue(beboer.getUddannelsessted());

				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(4).setCellValue(beboer.getUddannelsesretning());

				Date d2 = konverterLocalDateTilDate(beboer.getPåbegyndtDato());
				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(5).setCellValue(d2);

				Date d3 = konverterLocalDateTilDate(beboer.getForventetAfsluttetDato());
				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(6).setCellValue(d3);

				Date d4 = konverterLocalDateTilDate(beboer.getLejeaftalensUdløb());
				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(7).setCellValue(d4);

				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(8).setCellValue(beboer.getTelefonnummer());

				String s1 = konverterEnumTilString((Studiekontrolstatus) beboer.getStudiekontrolstatus());
				workbook.getSheetAt(0).getRow(slutRække + 1).createCell(9).setCellValue(s1);
			}

			// you have to close the input stream FIRST before writing to the same file.
			fis.close();

			// save your changes to the same file.
			workbook.write(new FileOutputStream(filnavn));
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void opretVærelsesudlejningIExcel(Værelsesudlejning værelsesudlejning) {
		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);
			int startRække = 1;
			int slutRække = workbook.getSheetAt(5).getLastRowNum();
			boolean værelsesudlejningFindes = false;

			// Loop gennem excel dokumentet og find rækkepladsen
			for (int i = startRække; i <= slutRække; i++) {
				//TODO både oprette id's og finde dem
//				String sVærelse = workbook.getSheetAt(5).getRow(i).getCell(1).getStringCellValue(); // Hvilken celle?
//				String sNavn = workbook.getSheetAt(5).getRow(i).getCell(2).getStringCellValue(); // Hvilken celle?
				String idCheck = workbook.getSheetAt(5).getRow(i).getCell(5).getStringCellValue();
				String signedByCheck = "";
				try{
				signedByCheck = workbook.getSheetAt(5).getRow(i).getCell(4).getStringCellValue(); //Hvis der ikke er nogen værdi her, så er det et ledigt værelse der skal udfyldes
				System.out.println(signedByCheck);} catch (Exception e) { e.printStackTrace();}
				boolean nytVærelse =true;
				if( signedByCheck == null || !signedByCheck.equals(""))
					nytVærelse = false;

				// Hvis det passer, så skriv til værelsesnummeret
				if (idCheck.equals(værelsesudlejning.getID().toString())&& nytVærelse) {
//					if (sNavn == null || sNavn.equals("")) {
						int celleNr = 0;
					//System.out.println("fejl1" + idCheck);
					//System.out.println("fejl1" + værelsesudlejning.getID());

						Date d1 = konverterLocalDateTilDate(værelsesudlejning.getIndflytningsdato());
						workbook.getSheetAt(5).getRow(i).getCell(celleNr).setCellValue(d1);
					System.out.println(d1 + "ny");
						++celleNr; // Går hen over værelsesnummeret

						workbook.getSheetAt(5).getRow(i).getCell(++celleNr).setCellValue(værelsesudlejning.getNavn());

					System.out.println(værelsesudlejning.getBehandlingsdato().getMonthValue() + "Jeg findes lige her");
						Date d2 = konverterLocalDateTilDate(værelsesudlejning.getBehandlingsdato());
					System.out.println(d2 + "crash");
						workbook.getSheetAt(5).getRow(i).createCell(++celleNr).setCellValue(d2); // TODO

						workbook.getSheetAt(5).getRow(i).getCell(++celleNr)
								.setCellValue(værelsesudlejning.getBehandlerInitialer());
						
						workbook.getSheetAt(5).getRow(i).getCell(++celleNr)
						.setCellValue(værelsesudlejning.getID());

						værelsesudlejningFindes = true;

					}
//				}
			}
			if (værelsesudlejningFindes == false) {
				workbook.getSheetAt(5).createRow(slutRække + 1);
				int celleNr = 0;

				Date d1 = konverterLocalDateTilDate(værelsesudlejning.getIndflytningsdato());
				workbook.getSheetAt(5).getRow(slutRække + 1).createCell(celleNr).setCellValue(d1);

				workbook.getSheetAt(5).getRow(slutRække + 1).createCell(++celleNr)
						.setCellValue(værelsesudlejning.getVærelse());

				workbook.getSheetAt(5).getRow(slutRække + 1).createCell(++celleNr).setCellValue("");// Navn er null hvis
																									// værelset ikke
																									// skal udlejes

//				if (værelsesudlejning.getBehandlingsdato() != null) {
//					Date d2 = konverterLocalDateTilDate(værelsesudlejning.getBehandlingsdato());
//					workbook.getSheetAt(5).getRow(slutRække + 1).createCell(++celleNr).setCellValue(d2);
//				} else
					++celleNr;

				workbook.getSheetAt(5).getRow(slutRække + 1).createCell(++celleNr).setCellValue(""); // Behandler
				workbook.getSheetAt(5).getRow(slutRække + 1).createCell(++celleNr).setCellValue(værelsesudlejning.getID());		// initialer er
																										// = "" hvis
																										// ikke det er
																										// blevet
																										// udlejet endnu

			}

			// you have to close the input stream FIRST before writing to the same file.
			fis.close();

			// save your changes to the same file.
			workbook.write(new FileOutputStream(filnavn));
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void opretStudiekontrollerIExcel(Studiekontrol studiekontrol) {
		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);
			int startRække = 1;
			int slutRække = workbook.getSheetAt(4).getLastRowNum();
			boolean studiekontrolFindes = false;

			// Loop gennem excel dokumentet og find rækkepladsen eller ej
			for (int i = startRække; i <= slutRække; i++) {
				String s = workbook.getSheetAt(4).getRow(i).getCell(5).getStringCellValue();

				// Hvis det passer, så skriv til værelsesnummeret
				if (s.equals(studiekontrol.getStudiekontrolID())) {
					int celleNr = 0;

					Date d1 = konverterLocalDateTilDate(studiekontrol.getAfleveringsfrist());
					workbook.getSheetAt(4).getRow(i).getCell(celleNr).setCellValue(d1);

					Date d2 = konverterLocalDateTilDate(studiekontrol.getPåmindelse());
					workbook.getSheetAt(4).getRow(i).getCell(++celleNr).setCellValue(d2);

					Date d3 = konverterLocalDateTilDate(studiekontrol.getPåbegyndelsesdato());
					workbook.getSheetAt(4).getRow(i).getCell(++celleNr).setCellValue(d3);

					workbook.getSheetAt(4).getRow(i).getCell(++celleNr).setCellValue(studiekontrol.getMånedsnummer());

					workbook.getSheetAt(4).getRow(i).getCell(++celleNr).setCellValue(studiekontrol.isAfsluttet());

					// workbook.getSheetAt(4).getRow(i).getCell(++celleNr).setCellValue(); - Behøver
					// ikke overskrive ID'et

					studiekontrolFindes = true;

				}
			}
			if (studiekontrolFindes == false) {
				workbook.getSheetAt(4).createRow(slutRække + 1);
				int celleNr = 0;

				Date d1 = konverterLocalDateTilDate(studiekontrol.getAfleveringsfrist());
				workbook.getSheetAt(4).getRow(slutRække + 1).createCell(celleNr).setCellValue(d1);

				Date d2 = konverterLocalDateTilDate(studiekontrol.getPåmindelse());
				workbook.getSheetAt(4).getRow(slutRække + 1).createCell(++celleNr).setCellValue(d2);

				Date d3 = konverterLocalDateTilDate(studiekontrol.getPåbegyndelsesdato());
				workbook.getSheetAt(4).getRow(slutRække + 1).createCell(++celleNr).setCellValue(d3);

				workbook.getSheetAt(4).getRow(slutRække + 1).createCell(++celleNr)
						.setCellValue(studiekontrol.getMånedsnummer());

				workbook.getSheetAt(4).getRow(slutRække + 1).createCell(++celleNr)
						.setCellValue(studiekontrol.isAfsluttet());

				workbook.getSheetAt(4).getRow(slutRække + 1).createCell(++celleNr)
						.setCellValue(studiekontrol.getStudiekontrolID());
			}

			// you have to close the input stream FIRST before writing to the same file.
			fis.close();

			// save your changes to the same file.
			FileOutputStream fs = new FileOutputStream(filnavn);
			workbook.write(fs);
			fs.close();
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void afslutFremleje() {

	}

	public void opretFremlejerIExcel(Beboer beboer) { //
		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);
			int startRække = 1;
			int slutRække = workbook.getSheetAt(2).getLastRowNum();
			boolean beboerFindes = false;

			// Loop gennem excel dokumentet og find rækkepladsen
			for (int i = startRække; i <= slutRække; i++) {
				String s = workbook.getSheetAt(2).getRow(i).getCell(0).getStringCellValue();
				// Hvis det passer, så skriv til værelsesnummeret
				if (s.equals(beboer.getVærelse())) {
					int celleNr = 0;
					workbook.getSheetAt(2).getRow(i).getCell(celleNr).setCellValue(beboer.getVærelse());

					workbook.getSheetAt(2).getRow(i).getCell(++celleNr).setCellValue(beboer.getNavn());

					Date d1 = konverterLocalDateTilDate(beboer.getIndflytningsdato());
					workbook.getSheetAt(2).getRow(i).getCell(++celleNr).setCellValue(d1);

					workbook.getSheetAt(2).getRow(i).getCell(++celleNr).setCellValue(beboer.getUddannelsessted());

					workbook.getSheetAt(2).getRow(i).getCell(++celleNr).setCellValue(beboer.getUddannelsesretning());

					Date d2 = konverterLocalDateTilDate(beboer.getPåbegyndtDato());
					workbook.getSheetAt(2).getRow(i).getCell(++celleNr).setCellValue(d2);

					Date d3 = konverterLocalDateTilDate(beboer.getForventetAfsluttetDato());
					workbook.getSheetAt(2).getRow(i).getCell(++celleNr).setCellValue(d3);

					Date d4 = konverterLocalDateTilDate(beboer.getLejeaftalensUdløb());
					workbook.getSheetAt(2).getRow(i).getCell(++celleNr).setCellValue(d4);

					workbook.getSheetAt(2).getRow(i).getCell(++celleNr).setCellValue(beboer.getTelefonnummer());

					String s1 = konverterEnumTilString((Studiekontrolstatus) beboer.getStudiekontrolstatus());
					workbook.getSheetAt(2).getRow(i).getCell(++celleNr).setCellValue(s1);

					beboerFindes = true;

				}
			}
			if (beboerFindes == false) {

				workbook.getSheetAt(2).createRow(slutRække + 1);

				workbook.getSheetAt(2).getRow(slutRække + 1).createCell(0).setCellValue(beboer.getVærelse());

				workbook.getSheetAt(2).getRow(slutRække + 1).createCell(1).setCellValue(beboer.getNavn());

				Date d1 = konverterLocalDateTilDate(beboer.getIndflytningsdato());
				workbook.getSheetAt(2).getRow(slutRække + 1).createCell(2).setCellValue(d1);

				workbook.getSheetAt(2).getRow(slutRække + 1).createCell(3).setCellValue(beboer.getUddannelsessted());

				workbook.getSheetAt(2).getRow(slutRække + 1).createCell(4).setCellValue(beboer.getUddannelsesretning());

				Date d2 = konverterLocalDateTilDate(beboer.getPåbegyndtDato());
				workbook.getSheetAt(2).getRow(slutRække + 1).createCell(5).setCellValue(d2);

				Date d3 = konverterLocalDateTilDate(beboer.getForventetAfsluttetDato());
				workbook.getSheetAt(2).getRow(slutRække + 1).createCell(6).setCellValue(d3);

				Date d4 = konverterLocalDateTilDate(beboer.getLejeaftalensUdløb());
				workbook.getSheetAt(2).getRow(slutRække + 1).createCell(7).setCellValue(d4);

				workbook.getSheetAt(2).getRow(slutRække + 1).createCell(8).setCellValue(beboer.getTelefonnummer());

				String s1 = konverterEnumTilString((Studiekontrolstatus) beboer.getStudiekontrolstatus());
				workbook.getSheetAt(2).getRow(slutRække + 1).createCell(9).setCellValue(s1);
			}

			// you have to close the input stream FIRST before writing to the same file.
			fis.close();

			// save your changes to the same file.
			workbook.write(new FileOutputStream(filnavn));
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Beboer> getBeboere() {

		return beboere;
	}

	public void hentBeboereFraExcel() {

		try {
			FileInputStream fis = new FileInputStream(filnavn);
			Workbook workbook = WorkbookFactory.create(fis);

			int startRække = 1;// Starter på 1 for ikke at tage overskrifter med

			int slutRække = workbook.getSheetAt(0).getLastRowNum();

			for (int i = startRække; i <= slutRække; i++) {
				Row row = workbook.getSheetAt(0).getRow(i);

				int kollonnenummer = 0;

				String værelse = row.getCell(kollonnenummer).getStringCellValue();

				String navn = row.getCell(++kollonnenummer).getStringCellValue();

				Date d = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate indflytning = konverterDateTilLocalDate(d);// HERRRR

				String uddannelsessted = row.getCell(++kollonnenummer).getStringCellValue();

				String uddannelsesretning = row.getCell(++kollonnenummer).getStringCellValue();

				Date d1 = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate uddStart = konverterDateTilLocalDate(d1);

				Date d2 = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate uddSlut = konverterDateTilLocalDate(d2);

				Date d3 = row.getCell(++kollonnenummer).getDateCellValue();
				LocalDate lejeaftalensUdløb = konverterDateTilLocalDate(d3);

				String telefonnummer = row.getCell(++kollonnenummer).getStringCellValue();

				Enum<Studiekontrolstatus> studiekontrolstatus = konverterStringTilEnum(
						row.getCell(++kollonnenummer).getStringCellValue());

				Beboer beboer = new Beboer(navn, værelse, indflytning, lejeaftalensUdløb, telefonnummer,
						studiekontrolstatus, uddannelsessted, uddannelsesretning, uddStart, uddSlut);
				beboere.add(beboer);

			}
			fis.close();
			workbook.close();
		} catch (EncryptedDocumentException | IOException e) {
			System.out.println("Filen kan ikke findes");
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}

	}

	/**
	 *
	 * Metoden tager en Excelcelle indeholdende en Date() og konverterer
	 * Date-objektet til et LocalDate Objekt.
	 * 
	 * @param
	 *            d er cellen indeholdende Datoen.
	 * @return LocalDate objektet.
	 */
	public LocalDate konverterDateTilLocalDate(Date d) {
		// herunder konverteres date til LocalDate
		Instant instant = d.toInstant();
		ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
		LocalDate localDate = zdt.toLocalDate();
		return localDate;
	}

	/**
	 * Metoden konverterer et localdate objekt til et Date() objekt.
	 * 
	 * @param dato
	 *            : datoen der skal konverteres
	 * @return Date : et DateObjekt()
	 */
	public Date konverterLocalDateTilDate(LocalDate dato) {
	//	System.out.println(dato.getMonthValue());
	//	System.out.println(dato.getDayOfMonth());
	//	System.out.println(dato.getYear());
	//	System.out.println(Date.from(dato.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		Date date = Date.from(dato.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}
}
