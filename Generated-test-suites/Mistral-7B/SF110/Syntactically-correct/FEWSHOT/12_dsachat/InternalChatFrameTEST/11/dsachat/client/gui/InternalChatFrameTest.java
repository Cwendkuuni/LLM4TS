package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame icf = new InternalChatFrame(null);
        DefaultStyledDocument doc = icf.doc;
        String line1 = "You: Hello World";
        String line2 = "*You*: Hello World *bold and italic*";
        String line3 = "*You **bold**: Hello World **bold**";
        String line4 = "*You **bold**: Hello World *italic*";
        String line5 = "*You **bold italic**: Hello World **bold italic**";

        icf.addText(line1);
        assertEquals(1, doc.getLength());
        assertEquals(line1, doc.getText(0, doc.getLength()));

        icf.addText(line2);
        assertEquals(2, doc.getLength());
        assertEquals(line2, doc.getText(1, doc.getLength() - line1.length()));
        Style s = doc.getStyle("You");
        assertTrue(StyleConstants.isBold(s));
        assertTrue(StyleConstants.isItalic(s));

        icf.addText(line3);
        assertEquals(3, doc.getLength());
        assertEquals(line3, doc.getText(2, doc.getLength() - line2.length() - line1.length()));
        s = doc.getStyle("You");
        assertTrue(StyleConstants.isBold(s));

        icf.addText(line4);
        assertEquals(4, doc.getLength());
        assertEquals(line4, doc.getText(3, doc.getLength() - line3.length() - line2.length() - line1.length()));
        s = doc.getStyle("You");
        assertFalse(StyleConstants.isBold(s));
        assertTrue(StyleConstants.isItalic(s));

        icf.addText(line5);
        assertEquals(5, doc.getLength());
        assertEquals(line5, doc.getText(4, doc.getLength() - line5.length() - line4.length() - line3.length() - line2.length() - line1.length()));
        s = doc.getStyle("You");
        assertTrue(StyleConstants.isBold(s));
        assertTrue(StyleConstants.isItalic(s));
    }

    @Test
    public void testAddStylesToDocument() {
        DefaultStyledDocument doc = new DefaultStyledDocument();
        InternalChatFrame.addStylesToDocument(doc);
        Style regular = doc.getStyle("regular");
        Style name = doc.getStyle("name");
        Style system = doc.getStyle("system");

        assertEquals("SansSerif", StyleConstants.getFontFamily(regular.getParent()));
        assertEquals(Color.BLUE, StyleConstants.getForeground(name));
        assertEquals(new Color(0.0f, 0.6f, 0.0f, 1.0f), StyleConstants.getForeground(system));
    }
}