package me.sa_g6.adapter;

import me.sa_g6.database.ElementType;
import me.sa_g6.utils.ReflectionUtils;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;
import javax.persistence.*;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

import static javax.swing.text.html.CSS.Attribute.*;

@Entity @Access(AccessType.PROPERTY)
public class AbstractElementAdapter implements Serializable {
    static final HTML.Tag[] ignoredTags = {HTML.Tag.IMPLIED, HTML.Tag.COMMENT, HTML.Tag.CONTENT};

    private static final CSS.Attribute[] ALL_MARGINS =
            { MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM, MARGIN_LEFT };
    private static final CSS.Attribute[] ALL_PADDING =
            { PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM, PADDING_LEFT };
    private static final CSS.Attribute[] ALL_BORDER_WIDTHS =
            { BORDER_TOP_WIDTH, BORDER_RIGHT_WIDTH, BORDER_BOTTOM_WIDTH,
                    BORDER_LEFT_WIDTH };
    private static final CSS.Attribute[] ALL_BORDER_STYLES =
            { BORDER_TOP_STYLE, BORDER_RIGHT_STYLE, BORDER_BOTTOM_STYLE,
                    BORDER_LEFT_STYLE };
    private static final CSS.Attribute[] ALL_BORDER_COLORS =
            { BORDER_TOP_COLOR, BORDER_RIGHT_COLOR, BORDER_BOTTOM_COLOR,
                    BORDER_LEFT_COLOR };

    private static Hashtable<Object, Object> valueConvertor;

    transient Element target;

    long id;

    List<Long> children = new ArrayList<>();

    Map<Object, Object> attrs = new HashMap<>();

    int p0 = 0, p1 = 0;

    ElementType type;


    public AbstractElementAdapter(){}

    public AbstractElementAdapter(Element element){
        target = element;
        if(target.isLeaf()){
            type = ElementType.LEAF;
        }else {
            type = ElementType.BRANCH;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ELEMENT_ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "CHILDREN")
    public List<Long> getChildren() {
        return children;
    }

    public void setChildren(List<Long> children) {
        this.children = children;
    }

    @Column(name = "ATTRIBUTES")
    public Map<Object, Object> getAttrs() {
        Map<Object, Object> staticAttrs = new HashMap<>();
        attrs.forEach((key, value) -> {
            value = value.toString();
            Object tmp = StyleContext.getStaticAttributeKey(key);
            if(StyleContext.getStaticAttribute(tmp) != null){
                key = tmp;
            }
            staticAttrs.put(key, value);
        });
        return staticAttrs;
    }

    public Map<Object, Object> getObjectAttrs() {
        return attrs;
    }

    public void setAttrs(Map<Object, Object> attrs) {
        this.attrs = new HashMap<>();
        attrs.forEach((key, value) -> {
            Object tmp;
            if((tmp = HTML.getTag(value.toString())) != null){
                value = tmp;
            }else if((tmp = getIgnoredTag(value.toString())) != null){
                value = tmp;
            }/*else{
                try{
                    tmp = Color.decode(value.toString());
                    value = tmp;
                }catch (NumberFormatException ignored){}
            }
            */

            if((tmp = StyleContext.getStaticAttribute(key)) != null){
                key = tmp;
            }
            if(valueConvertor.containsKey(key)){
                Object convertor = valueConvertor.get(key);
                //Object parseCssValue(String value)
                tmp = ReflectionUtils.invoke(convertor.getClass(), convertor, "parseCssValue", new Class[]{String.class}, value.toString());
                value = tmp;
            }

            this.attrs.put(key, value);
        });
    }

    @Column(name = "START_POS")
    public int getStartPosition(){
        return p0;
    }

    public void setStartPosition(int p){
        p0 = p;
    }

    @Column(name = "END_POS")
    public int getEndPosition(){
        return p1;
    }

    public void setEndPosition(int p){
        p1 = p;
    }

    @Column(name = "TYPE")
    public ElementType getType(){
        return type;
    }

    public void setType(ElementType type){
        this.type = type;
    }

    static HTML.Tag getIgnoredTag(String value){
        for(HTML.Tag tag : ignoredTags){
            if(tag.toString().equals(value)){
                return tag;
            }
        }
        return null;
    }

    static boolean hasAttribute(CSS.Attribute[] attrs, Object target){
        for(CSS.Attribute attr : attrs){
            if(attr.toString().equals(target.toString())){
                return true;
            }
        }
        return false;
    }

    static {
        valueConvertor = ReflectionUtils.getVariable(CSS.class, new CSS(), "valueConvertor");
    }
}
