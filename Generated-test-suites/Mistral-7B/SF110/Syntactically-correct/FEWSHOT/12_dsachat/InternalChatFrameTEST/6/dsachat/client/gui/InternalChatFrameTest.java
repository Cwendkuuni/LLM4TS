package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        StyledDocument doc = chatFrame.doc;

        String line = "You: Hello!";
        chatFrame.addText(line);
        String actual = doc.getText(0, doc.getLength()).trim();
        String expected = "You: Hello!\n";
        assertEquals(expected, actual);

        line = "*John*: Hello, You!";
        chatFrame.addText(line);
        actual = doc.getText(0, doc.getLength()).trim();
        expected = "You: Hello!\n*John*: Hello, You!\n";
        assertEquals(expected, actual);

        line = "*John*: This is a *test*.";
        chatFrame.addText(line);
        actual = doc.getText(0, doc.getLength()).trim();
        expected = "You: Hello!\n*John*: Hello, You!\n*John*: This is a *test*\n";
        assertEquals(expected, actual);

        line = "*John*: This is a <b>test</b>.";
        chatFrame.addText(line);
        actual = doc.getText(0, doc.getLength()).trim();
        expected = "You: Hello!\n*John*: Hello, You!\n*John*: This is a <b>test</b>\n";
        assertEquals(expected, actual);

        line = "*John*: This is a <b>test</b> <i>with</i> <u>styles</u>.";
        chatFrame.addText(line);
        actual = doc.getText(0, doc.getLength()).trim();
        expected = "You: Hello!\n*John*: Hello, You!\n*John*: This is a <b>test</b> <i>with</i> <u>styles</u>\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testAddStylesToDocument() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        StyledDocument doc = chatFrame.doc;

        Style regular = doc.getStyle("regular");
        StyleConstants.setFontFamily(regular, "Serif");
        assertEquals("Serif", StyleConstants.getFontFamily(doc.getStyle("regular")));

        Style italic = doc.getStyle("italic");
        StyleConstants.setItalic(italic, true);
        assertEquals(true, StyleConstants.isItalic(doc.getStyle("italic")));

        Style bold = doc.getStyle("bold");
        StyleConstants.setBold(bold, true);
        assertEquals(true, StyleConstants.isBold(doc.getStyle("bold")));

        Style boldItalic = doc.getStyle("bold-italic");
        StyleConstants.setBold(boldItalic, true);
        StyleConstants.setItalic(boldItalic, true);
        assertEquals(true, StyleConstants.isBold(doc.getStyle("bold-italic")) & StyleConstants.isItalic(doc.getStyle("bold-italic")));

        Style name = doc.getStyle("name");
        StyleConstants.setForeground(name, Color.RED);
        assertEquals(Color.RED, StyleConstants.getForeground(doc.getStyle("name")));

        Style system = doc.getStyle("system");
        StyleConstants.setForeground(system, Color.GREEN);
        assertEquals(Color.GREEN, StyleConstants.getForeground(doc.getStyle("system")));
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        StyledDocument doc = chatFrame.doc;

        String user = "Alice";
        chatFrame.addUser(user);
        Style style = doc.getStyle(user);

        Color newColor = new Color(255, 0, 0);
        chatFrame.changeStyle(14, newColor, false, false, user);
        assertEquals(newColor, StyleConstants.getForeground(style));

        boolean isBold = true;
        chatFrame.changeStyle(14, null, isBold, false, user);
        assertEquals(isBold, StyleConstants.isBold(style));

        boolean isItalic = true;
        chatFrame.changeStyle(14, null, false, isItalic, user);
        assertEquals(isItalic, StyleConstants.isItalic(style));
    }

    @Test
    public void testGetUserlist() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("Alice");
        chatFrame.addUser("Bob");
        chatFrame.addUser("Charlie");

        String[] expected = {"Alice", "Bob", "Charlie"};
        String[] actual = chatFrame.getUserlist();

        assertArrayEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testAddTextWithException() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());

        try {
            chatFrame.addText(":)");
        } catch (BadLocationException e) {
            throw new AssertionError("addText should not throw an Exception with the given input", e);
        }
    }
}