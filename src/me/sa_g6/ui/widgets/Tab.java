package me.sa_g6.ui.widgets;

import me.sa_g6.ui.*;
import me.sa_g6.ui.view.CellView;
import me.sa_g6.ui.view.RowView;
import me.sa_g6.ui.view.TableView;
import me.sa_g6.utils.CombinedAction;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tab extends JPanel {
    JTextPane editor = new JTextPane();

    public Tab(){
        super();



        GroupLayout layout = new GroupLayout(this);

        setLayout(layout);

        editor.setEditorKit(new TableEditorKit());
        editor.setDocument(new TableDocument());
        Action action = editor.getActionMap().get(DefaultEditorKit.pasteAction);
        //editor.getActionMap().put(DefaultEditorKit.pasteAction, new ProxyAction(action, DefaultEditorKit.pasteAction));

        JScrollPane pane = new JScrollPane(editor);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING) .addComponent(pane)
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING) .addComponent(pane)
        );
        DocumentFilter f;

        KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
        final ActionListener ctrlVAction = editor.getActionForKeyStroke(ctrlV);
        editor.unregisterKeyboardAction(ctrlV);
        editor.registerKeyboardAction(new CombinedAction(ctrlVAction, (e)->{
            Object src = e.getSource();
            if (src instanceof JComponent) {
                JComponent c = (JComponent) src;
                Clipboard cb = c.getToolkit().getSystemClipboard();
                Transferable trans = cb.getContents(null);
                for(DataFlavor dataFlavor : trans.getTransferDataFlavors()){
                    try {
                        Object o = trans.getTransferData(dataFlavor);
                        if(o instanceof String){
                            continue;
                        }else if(o instanceof BufferedImage){
                            BufferedImage bi = (BufferedImage) o;
                            final SimpleAttributeSet attrs=new SimpleAttributeSet();
                            ImageIcon icon = new ImageIcon(bi);
                            StyleConstants.setIcon(attrs, icon);
                            attrs.addAttribute("img",bi);
                            editor.getDocument().insertString(editor.getCaretPosition(),")", attrs);
                            //((TableDocument)editor.getDocument()).insertImage(editor.getCaretPosition(), bi);

                        }
                    } catch (UnsupportedFlavorException | IOException ex) {
                        ex.printStackTrace();
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }


            }
        }), ctrlV, JComponent.WHEN_FOCUSED);
    }

    public JTextPane getEditor() {
        return editor;
    }
}

class TableEditorKit extends StyledEditorKit {
    ViewFactory defaultFactory = new TableFactory();
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    public Document createDefaultDocument() {
        return new TableDocument();
    }
}

class TableFactory implements ViewFactory {
    public View create(Element elem) {


        String kind = elem.getName();
        switch (kind) {
            case AbstractDocument.ParagraphElementName:
                return new ParagraphView(elem);
            case AbstractDocument.SectionElementName:
                return new BoxView(elem, View.Y_AXIS);
            case StyleConstants.ComponentElementName:
                return new ComponentView(elem);
            case TableDocument.ELEMENT_NAME_TABLE:
                return new TableView(elem);
            case TableDocument.ELEMENT_NAME_ROW:
                return new RowView(elem);
            case TableDocument.ELEMENT_NAME_CELL:
                return new CellView(elem);
            case StyleConstants.IconElementName:
                return new IconView(elem);
            case "IMG":
                return new IconView(elem); //new PatchedImageView(elem);
            default:
                return new LabelView(elem);

        }

    }
}