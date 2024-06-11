package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import javax.swing.text.StyledDocument;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame()) {
            {
                this.doc = new SimpleAttributeSet();
            }
        };
        StyledDocument doc = icf.doc;

        icf.addText("You: Hello, World!");
        assertEquals("You: Hello, World!\n", icf.showTxt.getText());

        icf.addText("User1: Hi!");
        assertEquals("You: Hello, World!\nUser1: Hi!\n", icf.showTxt.getText());

        icf.addText("*User1*: Hi there!");
        assertEquals("You: Hello, World!\nUser1: Hi!\n*User1*: Hi there!\n", icf.showTxt.getText());

        icf.addText("**User1**: Hi again!");
        assertEquals("You: Hello, World!\nUser1: Hi!\n*User1*: Hi there!\n**User1**: Hi again!\n", icf.showTxt.getText());

        icf.addText("**User1**: *Hi once more*");
        assertEquals("You: Hello, World!\nUser1: Hi!\n*User1*: Hi there!\n**User1**: Hi again!\n**User1**: *Hi once more*\n", icf.showTxt.getText());

        icf.addText("User2: Hey!");
        assertEquals("You: Hello, World!\nUser1: Hi!\n*User1*: Hi there!\n**User1**: Hi again!\n**User1**: *Hi once more*\nUser2: Hey!\n", icf.showTxt.getText());
    }

    @Test
    public void testAddStylesToDocument() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame()) {
            {
                this.doc = new SimpleAttributeSet();
            }
        };
        icf.addStylesToDocument(doc);

        Style[] styles = doc.getStyles();
        assertEquals(11, styles.length);

        Style regular = styles[0];
        assertEquals("regular", regular.getName());

        Style italic = styles[1];
        assertEquals("italic", italic.getName());
        assertTrue(StyleConstants.isItalic(italic));

        Style bold = styles[2];
        assertEquals("bold", bold.getName());
        assertTrue(StyleConstants.isBold(bold));

        Style boldItalic = styles[3];
        assertEquals("bold-italic", boldItalic.getName());
        assertTrue(StyleConstants.isBold(boldItalic));
        assertTrue(StyleConstants.isItalic(boldItalic));

        Style name = styles[4];
        assertEquals("name", name.getName());
        assertEquals(Color.BLUE, StyleConstants.getForeground(name));

        Style system = styles[5];
        assertEquals("system", system.getName());
        assertEquals(new Color(0.0f, 0.6f, 0.0f, 1.0f), StyleConstants.getForeground(system));
        assertTrue(StyleConstants.isItalic(system));

        Style small = styles[6];
        assertEquals("small", small.getName());
        assertEquals(10, StyleConstants.getFontSize(small));

        Style large = styles[7];
        assertEquals("large", large.getName());
        assertEquals(16, StyleConstants.getFontSize(large));
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame()) {
            {
                this.doc = new SimpleAttributeSet();
            }
        };
        icf.addStylesToDocument(doc);

        icf.changeStyle(14, Color.RED, false, false, "User1");
        assertEquals(Color.RED, StyleConstants.getForeground(icf.doc.getStyle("User1")));

        icf.changeStyle(14, null, true, false, "User1");
        assertFalse(StyleConstants.isBold(icf.doc.getStyle("User1")));

        icf.changeStyle(14, null, false, true, "User1");
        assertTrue(StyleConstants.isItalic(icf.doc.getStyle("User1")));

        icf.changeStyle(14, Color.GREEN, true, true, "User1");
        assertTrue(StyleConstants.isBold(icf.doc.getStyle("User1")));
        assertTrue(StyleConstants.isItalic(icf.doc.getStyle("User1")));
        assertEquals(Color.GREEN, StyleConstants.getForeground(icf.doc.getStyle("User1")));
    }

    @Test
    public void testActionPerformed() {
        MainFrame parent = new MainFrame() {
            {
                this.hero = new Hero("Test");
            }
        };

        InternalChatFrame icf = new InternalChatFrame(parent);

        JList list = icf.list;
        assertEquals("", list.getSelectedValue());

        icf.addText("User1: Hi!");
        icf.addText("You: Hello, User1!");
        assertEquals("User1", list.getSelectedValue());

        icf.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
        String colorName = "TestColor";
        Color newColor = new Color(1, 1, 1);
        icf.changeStyle(14, newColor, false, false, "User1");
        icf.actionPerformed(new ActionEvent(list, 0, "bgColor"));
        JColorChooser chooser = new JColorChooser();
        Color newBgColor = chooser.showDialog(icf, "Choose a background color", Color.WHITE);
        icf.showTxt.setBackground(newBgColor);

        icf.actionPerformed(new ActionEvent(list, 0, "whisper"));
        String whisper = "/w User1 ";
        assertEquals(whisper, icf.txtInput.getText());
    }
}