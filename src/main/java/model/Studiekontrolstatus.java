package model;

public enum Studiekontrolstatus {
	IKKEIGANG("Ikke i gang"),
	MODTAGETIKKEGODKENDT("Modtaget, ikke godkendt"),
	MODTAGETAFSLUTTERUDDANNELSE ("Modtaget og godkendt, men afslutter uddannelse."),
	DISPENSATION ("Dispensation"),
	FREMLEJER ("Fremlejer værelse"),
	IKKEAFLEVERET("Ikke Modtaget"),
	SENDTTILBOLIGSELSKAB("Sendt til boligselskab"),
	GODKENDT ("Godkendt"),

	;

	public final String status;
	private Studiekontrolstatus(String s) {
		this.status = s;
	}
}
