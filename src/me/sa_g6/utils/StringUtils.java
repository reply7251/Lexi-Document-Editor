package me.sa_g6.utils;

import me.sa_g6.iterator.ElementIterator;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.util.Iterator;
import java.util.StringTokenizer;

public class StringUtils {
    public static int count(String str, String toCount){
        return str.split(toCount,-1).length-1;
        //return new StringTokenizer(" "+str+" ", toCount).countTokens()-1;
    }

    public static void attributesToString(StringBuilder builder, AttributeSet attrs, String prefix){
        for (Iterator<?> it = attrs.getAttributeNames().asIterator(); it.hasNext(); ) {
            Object key = it.next();
            if(!key.toString().equals("name")){
                builder.append(prefix).append(key).append(": ").append(attrs.getAttribute(key)).append("\n");
            }
        }
    }

    public static void elementToString(StringBuilder builder, Element element){
        elementToString(builder, element, "");
    }

    public static void elementToString(StringBuilder builder, Element element, String prefix){
        elementToString(builder, element,prefix, "");
    }

    public static void elementToString(StringBuilder builder, Element element, String prefix, String childrenPrefix){
        if(element instanceof AbstractDocument.BranchElement elem){
            builder.append(prefix).append("BranchElement<").append(elem.getName()).append(">:\n");
            attributesToString(builder, elem.getAttributes(), childrenPrefix + "│    ");
            for(Iterator<Element> iterator = new ElementIterator(elem); iterator.hasNext(); ){
                Element e = iterator.next();
                if(iterator.hasNext()){
                    elementToString(builder, e,childrenPrefix + "├──", childrenPrefix+"│    ");
                }else {
                    elementToString(builder, e,childrenPrefix + "└──", childrenPrefix+"      ");
                }
            }
        }else if(element instanceof AbstractDocument.LeafElement elem){
            try {
                builder.append(prefix).append("LeafElement<").append(elem.getName()).append(">:\n");
                attributesToString(builder, elem.getAttributes(), childrenPrefix);
                String text = element.getDocument().getText(elem.getStartOffset(),elem.getEndOffset() - elem.getStartOffset());
                if(text.length() > 0 && !text.equals("\n")){
                    builder.append(childrenPrefix).append("text: ");
                    builder.append(text);
                    if(!text.endsWith("\n")){
                        builder.append("\n");
                    }
                }

            } catch (BadLocationException ignored) { }
        }
    }
}
