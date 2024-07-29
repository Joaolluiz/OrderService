package br.com.orderservice.service;

import br.com.orderservice.dto.ProductDTO;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReportService {

    @Autowired
    ProductService productService;

    public ByteArrayInputStream generateProductReport() {
        List<ProductDTO> products = productService.findAllProductsForReport();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            Paragraph title = new Paragraph("Product Report")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20);
            document.add(title);

            SolidLine line = new SolidLine(1f);
            line.setColor(ColorConstants.BLACK);
            LineSeparator ls = new LineSeparator(line);
            document.add(ls);

            float[] columnWidths = {1, 2, 2, 5, 2, 1};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            addTableHeader(table);
            addRows(table, products);

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(Table table) {
        Stream.of("ID", "SKU", "Name", "Description", "Value", "Quantity")
                .forEach(columnTitle -> {
                    Cell header = new Cell();
                    header.add(new Paragraph(columnTitle));
                    table.addHeaderCell(header);
                });
    }

    private void addRows(Table table, List<ProductDTO> products) {
        for (ProductDTO product : products) {
            table.addCell(product.getId().toString());
            table.addCell(product.getSku());
            table.addCell(product.getName());
            table.addCell(product.getDescription());
            table.addCell(product.getValue().toString());
            table.addCell(product.getQuantity().toString());
        }
    }
}

