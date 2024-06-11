package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        Document doc = chatFrame.doc;
        // Test adding text with no formatting
        chatFrame.addText("Hello World");
        assertEquals("Hello World\n", chatFrame.showTxt.getText());

        Style regular = doc.getStyle("regular");
        Style you = doc.getStyle("You ");
        Style name = doc.getStyle("name");

        // Test adding text as a user
        chatFrame.addUser("Alice");
        chatFrame.addText("Alice: Hello World");
        assertEquals("Alice: Hello World\n", chatFrame.showTxt.getText());
        assertEquals("Alice", chatFrame.list.getModel().getElementAt(0));
        assertEquals(StyleConstants.StyleConstants.getForeground(name), Color.BLUE);

        // Test adding text as a system message
        chatFrame.addText("System: New user connected");
        assertEquals("Alice: Hello World\nSystem: New user connected\n", chatFrame.showTxt.getText());
        Style system = doc.getStyle("system");
        assertEquals(StyleConstants.StyleConstants.getForeground(system), new Color(0.0f, 0.6f, 0.0f, 1.0f));

        // Test adding text with formatting
        chatFrame.addText("*You *are *awesome*!");
        assertEquals("Alice: Hello World\nSystem: New user connected\n*You *are *awesome*!\n", chatFrame.showTxt.getText());
        Style bold = doc.getStyle("bold");
        Style italic = doc.getStyle("italic");
        Style boldItalic = doc.getStyle("bold-italic");
        assertTrue(StyleConstants.isBold(boldItalic));
        assertTrue(StyleConstants.isItalic(boldItalic));
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        Document doc = chatFrame.doc;
        Style name = doc.getStyle("name");

        // Test changing color, bold, and italic for a user
        chatFrame.changeStyle(12, Color.RED, true, true, "Alice");
        String chatLog = chatFrame.getChatlog();
        String userName = chatFrame.list.getModel().getElementAt(0);
        assertEquals("Alice", userName);
        assertEquals("Alice: " + chatLog, chatFrame.showTxt.getText());
        assertEquals(Color.RED, StyleConstants.getForeground(doc.getStyle("Alice")));
        // Test changing color, bold, and italic for the system
        chatFrame.changeStyle(12, Color.GREEN, true, true, "system");
        chatLog = chatFrame.getChatlog();
        assertEquals("Alice: " + chatLog + "\nSystem: " + chatLog, chatFrame.showTxt.getText());
        assertEquals(Color.GREEN, StyleConstants.getForeground(doc.getStyle("system")));
    }

    @Test
    public void testAddUsers() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("Alice");
        chatFrame.addUser("Bob");
        chatFrame.addUser("Charlie");
        String[] userList = chatFrame.getUserlist();
        assertEquals(userList.length, 3);
        assertEquals(userList[0], "Alice");
        assertEquals(userList[1], "Bob");
        assertEquals(userList[2], "Charlie");
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("Alice");
        chatFrame.addUser("Bob");
        chatFrame.addUser("Charlie");
        String[] userList = chatFrame.getUserlist();
        chatFrame.deleteUser("Alice");
        userList = chatFrame.getUserlist();
        assertEquals(userList.length, 2);
        assertNotEquals(userList[0], "Alice");
        assertEquals(userList[0], "Bob");
        assertEquals(userList[1], "Charlie");
    }
}