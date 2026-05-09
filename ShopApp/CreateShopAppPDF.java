import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateShopAppPDF {
    public static void main(String[] args) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("ShopApp_Complete_Code.pdf"));
            document.open();
            
            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLUE);
            Font codeFont = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
            
            document.add(new Paragraph("ShopApp - Complete Source Code", titleFont));
            document.add(new Paragraph("==================================================", titleFont));
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            
            // Add all Java files
            addJavaFile(document, "MainApp.java", "c:\\Users\\userm\\OneDrive\\Desktop\\TEMP\\codingg\\ShopApp\\MainApp.java", headerFont, codeFont);
            addJavaFile(document, "DBConnection.java", "c:\\Users\\userm\\OneDrive\\Desktop\\TEMP\\codingg\\ShopApp\\DBConnection.java", headerFont, codeFont);
            addJavaFile(document, "LoginFrame.java", "c:\\Users\\userm\\OneDrive\\Desktop\\TEMP\\codingg\\ShopApp\\LoginFrame.java", headerFont, codeFont);
            addJavaFile(document, "Dashboard.java", "c:\\Users\\userm\\OneDrive\\Desktop\\TEMP\\codingg\\ShopApp\\Dashboard.java", headerFont, codeFont);
            addJavaFile(document, "ProductPanel.java", "c:\\Users\\userm\\OneDrive\\Desktop\\TEMP\\codingg\\ShopApp\\ProductPanel.java", headerFont, codeFont);
            addJavaFile(document, "CustomerPanel.java", "c:\\Users\\userm\\OneDrive\\Desktop\\TEMP\\codingg\\ShopApp\\CustomerPanel.java", headerFont, codeFont);
            addJavaFile(document, "BillingPanel.java", "c:\\Users\\userm\\OneDrive\\Desktop\\TEMP\\codingg\\ShopApp\\BillingPanel.java", headerFont, codeFont);
            addJavaFile(document, "ReportPanel.java", "c:\\Users\\userm\\OneDrive\\Desktop\\TEMP\\codingg\\ShopApp\\ReportPanel.java", headerFont, codeFont);
            addJavaFile(document, "SalesReportPanel.java", "c:\\Users\\userm\\OneDrive\\Desktop\\TEMP\\codingg\\ShopApp\\SalesReportPanel.java", headerFont, codeFont);
            addJavaFile(document, "UITheme.java", "c:\\Users\\userm\\OneDrive\\Desktop\\TEMP\\codingg\\ShopApp\\UITheme.java", headerFont, codeFont);
            
            document.close();
            writer.close();
            
            System.out.println("✓ ShopApp Complete Code PDF created successfully!");
            System.out.println("📁 File saved as: ShopApp_Complete_Code.pdf");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void addJavaFile(Document document, String fileName, String filePath, Font headerFont, Font codeFont) {
        try {
            document.add(new Paragraph(fileName, headerFont));
            document.add(new Paragraph("--------------------------------------------------", headerFont));
            
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            
            // Replace special characters for PDF
            content = content.replace("≤", "<=").replace("≥", ">=").replace("≠", "!=");
            
            Paragraph codeParagraph = new Paragraph(content, codeFont);
            codeParagraph.setAlignment(Element.ALIGN_LEFT);
            document.add(codeParagraph);
            
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            
        } catch (Exception e) {
            System.out.println("Error reading file: " + fileName + " - " + e.getMessage());
        }
    }
}
