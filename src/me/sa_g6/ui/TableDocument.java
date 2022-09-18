package me.sa_g6.ui;

import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.util.ArrayList;

public class TableDocument extends DefaultStyledDocument {
    StyleSheet styleSheet = new StyleSheet();
    public static final String ELEMENT_NAME_TABLE = "table";
    public static final String ELEMENT_NAME_ROW = "row";
    public static final String ELEMENT_NAME_CELL = "cell";
    public static final String PARAM_CELL_WIDTH = "cell-width";

    public TableDocument() {
        super();
    }

    public void insertImage(int offset, Image image){

        SimpleAttributeSet attrs = new SimpleAttributeSet();
        attrs.addAttribute("img", image);
        attrs.addAttribute("ElementNameAttribute", "IMG");
        ElementSpec elementSpec = new ElementSpec(attrs,ElementSpec.ContentType);

        try {
            insert(offset, new ElementSpec[]{elementSpec});
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    protected void insertTable(int offset, int rowCount, int[] colWidths) {
        try {
            SimpleAttributeSet attrs = new SimpleAttributeSet();

            ArrayList<ElementSpec> tableSpecs = new ArrayList<>();
            tableSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType)); //close paragraph tag

            SimpleAttributeSet tableAttrs = new SimpleAttributeSet();
            tableAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_TABLE);
            ElementSpec tableStart = new ElementSpec(tableAttrs, ElementSpec.StartTagType);
            tableSpecs.add(tableStart); //start table tag

            fillRowSpecs(tableSpecs, rowCount, colWidths);

            ElementSpec tableEnd = new ElementSpec(tableAttrs, ElementSpec.EndTagType);
            tableSpecs.add(tableEnd); //end table tag

            tableSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType)); //open new paragraph after table

            ElementSpec[] spec = new ElementSpec[tableSpecs.size()];
            tableSpecs.toArray(spec);

            this.insert(offset, spec);
        }
        catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    protected void fillRowSpecs(ArrayList<ElementSpec> tableSpecs, int rowCount, int[] colWidths) {
        SimpleAttributeSet rowAttrs = new SimpleAttributeSet();
        rowAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_ROW);
        for (int i = 0; i < rowCount; i++) {
            ElementSpec rowStart = new ElementSpec(rowAttrs, ElementSpec.StartTagType);
            tableSpecs.add(rowStart);

            fillCellSpecs(tableSpecs, colWidths);

            ElementSpec rowEnd = new ElementSpec(rowAttrs, ElementSpec.EndTagType);
            tableSpecs.add(rowEnd);
        }

    }

    protected void fillCellSpecs(ArrayList<ElementSpec> tableSpecs, int[] colWidths) {
        for (int colWidth : colWidths) {
            SimpleAttributeSet cellAttrs = new SimpleAttributeSet();
            cellAttrs.addAttribute(ElementNameAttribute, ELEMENT_NAME_CELL);
            cellAttrs.addAttribute(PARAM_CELL_WIDTH, colWidth);
            ElementSpec cellStart = new ElementSpec(cellAttrs, ElementSpec.StartTagType);
            tableSpecs.add(cellStart);

            ElementSpec parStart = new ElementSpec(new SimpleAttributeSet(), ElementSpec.StartTagType);
            tableSpecs.add(parStart);
            ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(), ElementSpec.ContentType, "\n".toCharArray(), 0, 1);
            tableSpecs.add(parContent);
            ElementSpec parEnd = new ElementSpec(new SimpleAttributeSet(), ElementSpec.EndTagType);
            tableSpecs.add(parEnd);
            ElementSpec cellEnd = new ElementSpec(cellAttrs, ElementSpec.EndTagType);
            tableSpecs.add(cellEnd);
        }

    }

    public StyleSheet getStyleSheet(){
        return styleSheet;
    }
}
