package controller;
// how to jar https://stackoverflow.com/questions/39273877/intellij-java-2016-maven-how-to-embed-dependencies-in-jar
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.List;

public class TestWord {
    List<XWPFParagraph> paragraphs;
    static List<XWPFRun> runs;
   /* public static void main(String[] args) {
        TestWord t = new TestWord();
        t.readDocxFile("C:\\Users\\Janus\\Dropbox\\Indstillingen\\Indstillingsskabeloner m.m\\Dispensation\\Dispensation tildeles beboeren.docx");
        t.replace("NAVN", "Janus er awesome");
        t.replace("DAGSDATO" , "21/12/2019");
        t.writeDocX("Test.docx");
        //t.makeCopy();

    }*/
    public void readDocxFile(String filDerHentesFra) {
        try {
            File file = new File(filDerHentesFra);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XWPFDocument document = new XWPFDocument(fis);

            paragraphs = document.getParagraphs();
            System.out.println(paragraphs.size());

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void replace(String target, String replacement){
/*        for (XWPFParagraph para : paragraphs) {
            System.out.println(para.getText().replace(target,replacement));
            for(XWPFRun r : para.getRuns()){

                r.setText(para.getText().replace(target, replacement));
            }
            //para.getText().replace(target, replacement); VIRKER IKKE
            //XWPFRun r = para.createRun().setText(para.getText().replace(target, replacement));

        }*/
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
    public void writeDocX(String filname){
        try {
            InputStream template = new FileInputStream("C:\\Users\\Janus\\Dropbox\\Indstillingen\\Indstillingsskabeloner m.m\\Brevpapir A4\\BrevpapirA4Indstillingen.docx");
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
