package pos.presentation.producto;


import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import pos.Application;
import pos.logic.Producto;
import pos.logic.Service;
import pos.logic.SocketListener;
import pos.logic.ThreadListener;

public class Controller {

    View view;
    Model model;
    public Controller(View view, Model model) {
        model.init( Service.instance().search(new Producto()));
        model.setCategorias( Service.instance().getCategorias());
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }
    public void search(Producto filter) throws  Exception{
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Producto());
        model.setList( Service.instance().search(model.getFilter()));
    }

    public void save(Producto e) throws  Exception{
        switch (model.getMode()) {
            case Application.MODE_CREATE:
                Service.instance().create(e);
                break;
            case Application.MODE_EDIT:
                Service.instance().update(e);
                break;
        }
        model.setFilter(new Producto());
        search(model.getFilter());
    }

    public void edit(int row){
        Producto e = model.getList().get(row);
        try {
            model.setMode(Application.MODE_EDIT);
            model.setCurrent( Service.instance().read(e));
        } catch (Exception ex) {}
    }

    public void delete() throws Exception {
        Service.instance().delete(model.getCurrent());
        search(model.getFilter());
    }

    public void clear() {
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Producto());
    }
    public void print()throws Exception{
        String dest="productos.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);

        //Document document = new Document(pdf, PageSize.A4.rotate());
        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        Table header = new Table(1);
        header.setWidth(400);
        header.setHorizontalAlignment(HorizontalAlignment.CENTER);
        header.addCell(getCell(new Paragraph("Listado de Productos").setFont(font).setBold().setFontSize(20f), TextAlignment.CENTER,false));
        //header.addCell(getCell(new Image(ImageDataFactory.create("logo.jpg")), HorizontalAlignment.CENTER,false));
        document.add(header);

        document.add(new Paragraph(""));document.add(new Paragraph(""));

        Color bkg = ColorConstants.RED;
        Color frg= ColorConstants.WHITE;
        Table body = new Table(6);
        body.setWidth(400);
        body.setHorizontalAlignment(HorizontalAlignment.CENTER);
        body.addCell(getCell(new Paragraph("Codigo").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Descripcion").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Unidad de Medida").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Precio Unitario").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Existencia").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Categoria").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));


        for(Producto e: model.getList()){
            body.addCell(getCell(new Paragraph(e.getCodigo()),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(e.getDescripcion()),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(e.getUnidadDeMedidad()),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(String.valueOf(e.getPrecioUnitario())),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(String.valueOf(e.getExistencia())),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(e.getCategoria().getNombre()),TextAlignment.CENTER,true));

        }
        document.add(body);
        document.close();
    }

    private Cell getCell(Paragraph paragraph, TextAlignment alignment, boolean hasBorder) {
        Cell cell = new Cell().add(paragraph);
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        if(!hasBorder) cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    private Cell getCell(Image image, HorizontalAlignment alignment, boolean hasBorder) {
        Cell cell = new Cell().add(image);
        image.setHorizontalAlignment(alignment);
        cell.setPadding(0);
        if(!hasBorder) cell.setBorder(Border.NO_BORDER);
        return cell;
    }

}
