package controller.word;
// how to jar https://stackoverflow.com/questions/39273877/intellij-java-2016-maven-how-to-embed-dependencies-in-jar
import model.Beboer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import view.GuiSingleton;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WordConnection {
    private List<XWPFParagraph> paragraphs;
    private String baseDirectory;
    private GuiSingleton gui;

    public WordConnection() {
        gui = GuiSingleton.getInstance();
        baseDirectory = gui.filplaceringer.getPath(gui.skabeloner);

    }

    public void startStudiekontrol(List<Beboer> beboere, LocalDate dagsDato, LocalDate lejeAftalensUdløb, LocalDate studieKontrolAfsluttesDato) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dagsdato = dagsDato.format(formatter);
        String lejeaftale = lejeAftalensUdløb.format(formatter);
        String afslut = studieKontrolAfsluttesDato.format(formatter);
        String prefixName = "studiekontol " + lejeAftalensUdløb.getMonth() +" ";


        String extra = lejeAftalensUdløb.getMonth().toString() + "   \n";
        for (Beboer b: beboere) {
            startEnkeltStudiekontrol(b, prefixName,dagsdato, lejeaftale, afslut);
            extra += b.getVærelse() + " " + b.getNavn() + "      \n";
        }
        udfyldFølgeseddel(extra, dagsdato, prefixName);


    }
    /**
     * Metode til at udfylde blanketter til studiekontrol
     */
    private void udfyldFølgeseddel(String beboere, String dagsDato, String filePath) {
        //fileToReplaceIn = gui.filplaceringer.getPath(gui.studieKontrolFølgeSeddel);
        readDocxFile(baseDirectory+gui.studieKontrolFølgeSeddel);
        List<XWPFParagraph> paragraphsToPrint = copyParagraphs(paragraphs);
        replace("DAGS DATO", dagsDato, paragraphsToPrint);
        replace("LISTE", beboere, paragraphsToPrint);

        writeDocX(baseDirectory+gui.base,filePath+" Følgeseddel.docx",paragraphsToPrint);


    }


    private void startEnkeltStudiekontrol(Beboer beboer,String mappe, String dagsDato, String lejeaftalensUdløb, String studiekontrolAfsluttesDato){

        if(beboer!=null) {
            //String baseFil = "C:\\Users\\Janus\\Dropbox\\Indstillingen\\Indstillingsskabeloner m.m\\Brevpapir A4\\Studiekontrolbase.docx";
            //readDocxFile("C:\\Users\\Janus\\Desktop\\Studiekontrol\\Studiekontrolsblanket.docx");
            //must read every time, since file is to be replaced in.
            readDocxFile(baseDirectory+gui.studiekontrolSeddel);
            List<XWPFParagraph> paragraphsToPrint = copyParagraphs(paragraphs);

                replace("DAGS DATO", dagsDato, paragraphsToPrint);
                replace("UDLØBSDATO", lejeaftalensUdløb, paragraphsToPrint);
                replace("DATOEN FOR FRISTEN", studiekontrolAfsluttesDato, paragraphsToPrint);
                replace("BEBOERNAVN", beboer.getNavn(), paragraphsToPrint);
                replace("VÆRELSESNUMMER", beboer.getVærelse(), paragraphsToPrint);

            writeDocX(baseDirectory+gui.base, mappe + beboer.getVærelse()+".docx", paragraphsToPrint);
        }
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

    private void writeDocX(String skabelonfil, String filname, List<XWPFParagraph> paragraphs){
        try (XWPFDocument document = new XWPFDocument(new FileInputStream(skabelonfil))) {

            int pos = 0;
            for (XWPFParagraph p : paragraphs) {
                document.createParagraph();
                document.setParagraph(p, pos);
                pos++;
            }

            FileOutputStream fos = new FileOutputStream(filname);
            document.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
