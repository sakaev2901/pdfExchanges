package com.example.demo.utils;

import com.example.demo.models.PacketType;
import com.example.demo.models.Statement;
import com.example.demo.models.User;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.UUID;

@Component
public class PdfGeneratorImpl implements PdfGenerator {

    @Override
    public void generatePdfDocs(Statement statement, String key) {
        String[] parts = key.split("\\.");
        String templateFileName = parts[parts.length - 1] + ".html";
        String readyTemplateString = setUserFields(statement.getUser(), getStringifyPageTemplate(templateFileName));
        try {
            String newDir = statement.getDate().toString()
                    .replaceAll(" ", "-")
                    .replaceAll(":", "-");
            System.out.println(newDir);
            new File("generated/" + newDir).mkdirs();
            String pdfPath = "generated/" + newDir + "/" +UUID.randomUUID() + ".pdf";
            File file = new File(pdfPath);
            file.createNewFile();
                HtmlConverter.convertToPdf(new ByteArrayInputStream(readyTemplateString.getBytes()),
                    new FileOutputStream(file), new ConverterProperties());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }





    private String getStringifyPageTemplate(String templateName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("templates/" + templateName))) {
            String line;
            StringBuilder pageContent = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                pageContent.append(line);
            }
            return pageContent.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String setUserFields(User user, String template) {
        return template
                .replace("${name}", user.getName())
                .replace("${surname}", user.getSurname())
                .replace("${patronymic}", user.getPatronymic())
                .replace("${birthday}", user.getBirthday())
                .replace("${passwordId}", user.getPasswordId())
                ;
    }


}
