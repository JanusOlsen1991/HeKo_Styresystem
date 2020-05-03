package controller;
// how to jar https://stackoverflow.com/questions/39273877/intellij-java-2016-maven-how-to-embed-dependencies-in-jar
import model.Beboer;
import model.Studiekontrolstatus;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WordConnection {
    private List<XWPFParagraph> paragraphs;
    //static List<XWPFRun> runs;
    static WordConnection t = new WordConnection();

    /*public static void main(String[] args) {
       /* WordConnection t = new WordConnection();
        t.readDocxFile("C:\\Users\\Janus\\Dropbox\\Indstillingen\\Indstillingsskabeloner m.m\\Dispensation\\Dispensation tildeles beboeren.docx");
        t.replace("NAVN", "Janus er awesome");
        t.replace("DAGSDATO" , "21/12/2019");
        t.writeDocX("Test.docx");
               //t.makeCopy();
         //WordConnection t = new WordConnection();
         Beboer b = new Beboer("Mikael Larsen","111", LocalDate.now(),LocalDate.of(1991,10,20), "22222222", Studiekontrolstatus.IKKEAFLEVERET, "", "", LocalDate.of(2000, 1,1), LocalDate.of(2001, 4, 5));
         Beboer b2 = new Beboer("Janus Olsen","111", LocalDate.now(),LocalDate.of(1991,10,20), "22222222", Studiekontrolstatus.IKKEAFLEVERET, "", "", LocalDate.of(2000, 1,1), LocalDate.of(2001, 4, 5));
         ArrayList<Beboer> bb = new ArrayList<>();
         bb.add(b);
         bb.add(b2);
         t.startStudiekontrol(bb, LocalDate.now().toString(), LocalDate.of(2000,10,29).toString(), LocalDate.of(1999,10,10).toString());
     }*/

    /**
     * Metode til at udfylde blanketter til studiekontrol
     * @param beboerOplysninger
     * @param dagsDato
     */
    public void startStudiekontrol(ArrayList<Beboer> beboerOplysninger, String dagsDato, String lejeaftalensUdløb, String studiekontrolAfsluttesDato){
         //TODO udfyld studiekontrolsblanketter
        if(beboerOplysninger.size()>0 || beboerOplysninger!=null) {
            //WordConnection t = new WordConnection();
            String baseFil = "C:\\Users\\Janus\\Dropbox\\Indstillingen\\Indstillingsskabeloner m.m\\Brevpapir A4\\Studiekontrolbase.docx";
            t.readDocxFile("C:\\Users\\Janus\\Desktop\\Studiekontrol\\Studiekontrolsblanket.docx");

            List<XWPFParagraph> paragraphsToPrint = null;
            //System.out.println(paragraphs.get(0).getText());
            List<XWPFParagraph> tempParagraphs = null;
            for (Beboer beboer : beboerOplysninger) {
                tempParagraphs = copyParagraphs(paragraphs);
                t.replace("DAGS DATO", dagsDato, tempParagraphs);
                t.replace("UDLØBSDATO", lejeaftalensUdløb, tempParagraphs);
                t.replace("DATOEN FOR FRISTEN", studiekontrolAfsluttesDato, tempParagraphs);
                t.replace("BEBOERNAVN", beboer.getNavn(), tempParagraphs);
                t.replace("VÆRELSESNUMMER", beboer.getVærelse(), tempParagraphs);

            }
            //TODO gem alle paragraphs i ny omgang paragraphs
            for(XWPFParagraph paragraph : tempParagraphs){
                paragraphsToPrint.add(paragraph);
            }
            t.writeDocX(baseFil, "Test.docx", paragraphsToPrint);
        }
    }
    public void lavDispensation(){
         //TODO Body
    }
    public List<XWPFParagraph> copyParagraphs(List<XWPFParagraph> paragraphs){
        List<XWPFParagraph> returnValue = null;
        for(XWPFParagraph xwpf : paragraphs){
            returnValue.add(xwpf);
        }
        return returnValue;
    }

    public boolean readDocxFile(String filDerHentesFra) {
        try {
            File file = new File(filDerHentesFra);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XWPFDocument document = new XWPFDocument(fis);

            paragraphs = document.getParagraphs();
            System.out.println(paragraphs.size());

            fis.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void replace(String target, String replacement, List<XWPFParagraph> paragraphs){

int count = 0;
        for (XWPFParagraph p : paragraphs) {
            List<XWPFRun> runs = p.getRuns();
            //System.out.println(runs.toString() +" " + runs.size());
            if (runs != null) {
                //System.out.println("Jeg kommer her");
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    System.out.println(text);
                    if (text != null && text.contains(target)) {
                        text = text.replace(target, replacement);
                        r.setText(text, 0);
                        System.out.println(r + " " + ++count);
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
