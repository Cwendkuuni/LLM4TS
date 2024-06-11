package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.text.StyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            {
                doc = new SimpleAttributeSet();
            }
        };

        Style style = chatFrame.doc.addStyle("regular", null);
        StyleConstants.setFontFamily(style, "SansSerif");

        chatFrame.addText("You: Hello World");
        assertEquals("You: Hello World\n", chatFrame.showTxt.getText());

        Style nameStyle = chatFrame.doc.getStyle("name");
        assertEquals("Blue", StyleConstants.getName(nameStyle));
        assertEquals(16, StyleConstants.getFontSize(nameStyle));

        Style systemStyle = chatFrame.doc.getStyle("system");
        assertEquals("default", StyleConstants.getName(systemStyle));
        assertEquals(0.0f, StyleConstants.getForeground(systemStyle).getRGB(), 0.001f);
        assertEquals(1.0f, StyleConstants.getAlpha(systemStyle));
        assertEquals(14, StyleConstants.getFontSize(systemStyle));

        chatFrame.addText("John: Hi");
        assertEquals("You: Hello World\nJohn: Hi\n", chatFrame.showTxt.getText());
    }

    @Test
    public void testAddStylesToDocument() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            {
                doc = new SimpleAttributeSet();
            }
        };

        StyleConstants.setFontFamily(chatFrame.doc, "SansSerif");

        chatFrame.addStylesToDocument(chatFrame.doc);

        Style regular = chatFrame.doc.getStyle("regular");
        assertEquals("default", StyleConstants.getName(regular));
        assertEquals("SansSerif", StyleConstants.getFontFamily(regular));

        Style italic = chatFrame.doc.getStyle("italic");
        assertEquals("italic", StyleConstants.getName(italic));
        assertTrue(StyleConstants.isItalic(italic));

        Style bold = chatFrame.doc.getStyle("bold");
        assertEquals("bold", StyleConstants.getName(bold));
        assertTrue(StyleConstants.isBold(bold));

        Style boldItalic = chatFrame.doc.getStyle("bold-italic");
        assertEquals("bold-italic", StyleConstants.getName(boldItalic));
        assertTrue(StyleConstants.isBold(boldItalic));
        assertTrue(StyleConstants.isItalic(boldItalic));

        Style name = chatFrame.doc.getStyle("name");
        assertEquals("name", StyleConstants.getName(name));
        assertEquals(Color.BLUE, StyleConstants.getForeground(name));
        assertEquals(16, StyleConstants.getFontSize(name));

        Style system = chatFrame.doc.getStyle("system");
        assertEquals("system", StyleConstants.getName(system));
        assertEquals(new Color(0.0f, 0.6f, 0.0f, 1.0f), StyleConstants.getBackground(system));
        assertEquals(14, StyleConstants.getFontSize(system));
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            {
                doc = new SimpleAttributeSet();
            }
        };

        Style regular = chatFrame.doc.getStyle("regular");

        chatFrame.changeStyle(12, new Color(1, 1, 1), false, false, "name");
        Style name = chatFrame.doc.getStyle("name");
        assertEquals(12, StyleConstants.getFontSize(name));
        assertEquals(Color.WHITE, StyleConstants.getForeground(name));

        chatFrame.changeStyle(12, Color.RED, true, false, "name");
        name = chatFrame.doc.getStyle("name");
        assertEquals(12, StyleConstants.getFontSize(name));
        assertEquals(Color.RED, StyleConstants.getForeground(name));
        assertFalse(StyleConstants.isBold(name));
        assertFalse(StyleConstants.isItalic(name));

        chatFrame.changeStyle(12, Color.BLUE, false, true, "name");
        name = chatFrame.doc.getStyle("name");
        assertEquals(12, StyleConstants.getFontSize(name));
        assertEquals(Color.BLUE, StyleConstants.getForeground(name));
        assertTrue(StyleConstants.isItalic(name));
        assertFalse(StyleConstants.isBold(name));

        chatFrame.changeStyle(12, Color.BLUE, true, false, "name");
        name = chatFrame.doc.getStyle("name");
        assertEquals(12, StyleConstants.getFontSize(name));
        assertEquals(Color.BLUE, StyleConstants.getForeground(name));
        assertTrue(StyleConstants.isBold(name));
        assertFalse(StyleConstants.isItalic(name));
    }
}