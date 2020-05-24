package controller.word;
// how to jar https://stackoverflow.com/questions/39273877/intellij-java-2016-maven-how-to-embed-dependencies-in-jar
import model.Beboer;
import model.Studiekontrolstatus;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WordConnection {
    private List<XWPFParagraph> paragraphs;
    //static WordConnection t = new WordConnection();



    /*public static void main(String[] args) {
        WordConnection t = new WordConnection();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Beboer b = new Beboer("Mikael Larsen","123", LocalDate.now(),LocalDate.of(1991,10,20), "22222222", Studiekontrolstatus.IKKEAFLEVERET, "", "", LocalDate.of(2000, 1,1), LocalDate.of(2001, 4, 5));
        Beboer b2 = new Beboer("Janus Olsen","111", LocalDate.now(),LocalDate.of(1991,10,20), "22222222", Studiekontrolstatus.IKKEAFLEVERET, "", "", LocalDate.of(2000, 1,1), LocalDate.of(2001, 4, 5));
        t.startStudiekontrol(b, LocalDate.now().toString(), LocalDate.of(2000,10,29).format(formatter).toString(), LocalDate.of(1999,10,10).format(formatter).toString());
        t.startStudiekontrol(b2, LocalDate.now().toString(), LocalDate.of(2000,10,29).format(formatter).toString(), LocalDate.of(1999,10,10).format(formatter).toString());

    }*/
    public void startStudiekontrol(List<Beboer> beboere, LocalDate dagsDato, LocalDate lejeAftalensUdløb, LocalDate studieKontrolAfsluttesDato) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dagsdato = dagsDato.format(formatter);
        String lejeaftale = lejeAftalensUdløb.format(formatter);
        String afslut = studieKontrolAfsluttesDato.format(formatter);

        for (Beboer b: beboere) {
            startEnkeltStudiekontrol(b,dagsdato, lejeaftale, afslut);
        }

    }
    /**
     * Metode til at udfylde blanketter til studiekontrol
     * @param beboer
     * @param dagsDato
     */

    private void startEnkeltStudiekontrol(Beboer beboer, String dagsDato, String lejeaftalensUdløb, String studiekontrolAfsluttesDato){

        //TODO find basefil og readDocX path på anden måde
        if(beboer!=null) {
            String baseFil = "C:\\Users\\Janus\\Dropbox\\Indstillingen\\Indstillingsskabeloner m.m\\Brevpapir A4\\Studiekontrolbase.docx";
            readDocxFile("C:\\Users\\Janus\\Desktop\\Studiekontrol\\Studiekontrolsblanket.docx");

            List<XWPFParagraph> paragraphsToPrint = copyParagraphs(paragraphs);

                replace("DAGS DATO", dagsDato, paragraphsToPrint);
                replace("UDLØBSDATO", lejeaftalensUdløb, paragraphsToPrint);
                replace("DATOEN FOR FRISTEN", studiekontrolAfsluttesDato, paragraphsToPrint);
                replace("BEBOERNAVN", beboer.getNavn(), paragraphsToPrint);
                replace("VÆRELSESNUMMER", beboer.getVærelse(), paragraphsToPrint);

            writeDocX(baseFil, beboer.getVærelse()+".docx", paragraphsToPrint);
        }
    }
    public void lavDispensation(){
         //TODO Body
    }
    public List<XWPFParagraph> copyParagraphs(List<XWPFParagraph> paragraphs){
        List<XWPFParagraph> returnValue = new ArrayList() ;
        for(XWPFParagraph xwpf : paragraphs){
            returnValue.add(xwpf);
        }
        return returnValue;
    }

    public boolean readDocxFile(String filDerHentesFra) {
        try (XWPFDocument document = new XWPFDocument (new FileInputStream(new File(filDerHentesFra).getAbsolutePath()))){

            paragraphs = document.getParagraphs();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void replace(String target, String replacement, List<XWPFParagraph> paragraphs){

        for (XWPFParagraph p : paragraphs) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains(target)) {
                        text = text.replace(target, replacement);
                        r.setText(text, 0);
                    }
                }
            }
        }
    }

    public void writeDocX(String skabelonfil, String filname, List<XWPFParagraph> paragraphs){
        try {
            //TODO skal ikke hardcodes en placering til brevpapir
            InputStream template = new FileInputStream(skabelonfil);
            XWPFDocument document = new XWPFDocument(template);


            int pos = 0;
            for (XWPFParagraph p : paragraphs) {
                document.createParagraph();
                document.setParagraph(p, pos);
                pos++;
            }


            FileOutputStream fos = new FileOutputStream(filname);
            document.write(fos);
            document.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoden
     */
    public void makeCopy(){
        try {
            InputStream template = new FileInputStream("C:\\Users\\Janus\\Dropbox\\Indstillingen\\Indstillingsskabeloner m.m\\Brevpapir A4\\BrevpapirA4Indstillingen.docx");
            XWPFDocument document = new XWPFDocument(template);
            System.out.println(document.getParagraphs().size());
                int pos = 0;
                for (XWPFParagraph p : paragraphs) {
                    //System.out.println(p.getRuns().toString() + "!!!!!!!");
                    document.createParagraph();
                    document.setParagraph(p, pos);
                    //document.getParagraphs() = paragraphs;
                    pos++;

            }
            FileOutputStream out = new FileOutputStream("testVirker.docx");
            document.write(out);
            document.close();
            out.close();
            template.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
