package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.MouseEvent;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setSize(new Dimension(100, 100));
            }
        };
        StyleContext sc = new StyleContext();
        StyledDocument doc = new PlainDocument();
        chatFrame.addStylesToDocument(doc);

        chatFrame.addText("You: Hello");
        assertEquals("Hello\n", chatFrame.showTxt.getText());

        chatFrame.addText("User1: Hi");
        assertEquals("Hello\nUser1: Hi\n", chatFrame.showTxt.getText());

        chatFrame.addText("*User1*: Hi there");
        assertEquals("Hello\nUser1: Hi\n*User1*: Hi there\n", chatFrame.showTxt.getText());

        chatFrame.addText("*User1* +User2: Hello again");
        assertEquals("Hello\nUser1: Hi\n*User1*: Hi there\n*User1*: Hello again\n", chatFrame.showTxt.getText());

        chatFrame.addText("User1: Hi User2: Hello");
        assertEquals("Hello\nUser1: Hi\n*User1*: Hi there\n*User1*: Hello again\nUser1: Hi User2: Hello\n", chatFrame.showTxt.getText());

        chatFrame.addText("*User1* +User2: Hello again *User3*");
        assertEquals("Hello\nUser1: Hi\n*User1*: Hi there\n*User1*: Hello again *User3*\nUser1: Hi User2: Hello\n*User1* +User2: Hello again *User3*\n", chatFrame.showTxt.getText());

        chatFrame.addText("User1: Hi *User2*");
        assertEquals("Hello\nUser1: Hi *User2*\nUser1: Hi User2: Hello\n*User1*: Hi there\n*User1*: Hello again *User3*\nUser1: Hi *User2*\n", chatFrame.showTxt.getText());

        chatFrame.addText("User1: Hi *User2*");
        chatFrame.addText("User1: Hi *User2*");
        assertEquals("Hello\nUser1: Hi *User2*\nUser1: Hi *User2*\nUser1: Hi User2: Hello\n*User1*: Hi there\n*User1*: Hello again *User3*\nUser1: Hi *User2*\n", chatFrame.showTxt.getText());

        chatFrame.addText("User1: Hi *User2*");
        chatFrame.addText("User1: Hi *User2*");
        chatFrame.addText("User1: Hi *User2*");
        assertEquals("Hello\nUser1: Hi *User2*\nUser1: Hi *User2*\nUser1: Hi *User2*\nUser1: Hi User2: Hello\n*User1*: Hi there\n*User1*: Hello again *User3*\nUser1: Hi *User2*\n", chatFrame.showTxt.getText());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setSize(new Dimension(100, 100));
            }
        };
        assertEquals(0, chatFrame.listModel.size());

        chatFrame.addUser("User1");
        assertEquals(1, chatFrame.listModel.size());
        assertEquals("User1", chatFrame.listModel.get(0));

        chatFrame.addUser("User2");
        assertEquals(2, chatFrame.listModel.size());
        assertEquals("User2", chatFrame.listModel.get(1));

        chatFrame.addUser("User3");
        assertEquals(3, chatFrame.listModel.size());
        assertEquals("User3", chatFrame.listModel.get(2));

        chatFrame.addUser("User3");
        assertEquals(3, chatFrame.listModel.size());
        assertEquals("User3", chatFrame.listModel.get(2));
    }

    @Test
    public void testGetUserlist() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setSize(new Dimension(100, 100));
            }
        };
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");

        String[] users = chatFrame.getUserlist();
        assertArrayEquals(new String[]{"User1", "User2", "User3"}, users);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setSize(new Dimension(100, 100));
            }
        };
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");

        chatFrame.deleteUser("User2");
        assertEquals(2, chatFrame.listModel.size());
        assertEquals("User1", chatFrame.listModel.get(0));
        assertEquals("User3", chatFrame.listModel.get(1));

        chatFrame.deleteUser("User1");
        assertEquals(0, chatFrame.listModel.size());
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setSize(new Dimension(100, 100));
            }
        };
        StyleContext sc = new StyleContext();
        StyledDocument doc = new PlainDocument();
        chatFrame.addStylesToDocument(doc);

        Style style = doc.getStyle("regular");
        Style newStyle = new SimpleAttributeSet();
        newStyle.addAll(style);
        StyleConstants.setBold(newStyle, true);

        chatFrame.changeStyle(16, null, false, false, "bold");
        Style boldStyle = doc.getStyle("bold");
        assertEquals(style, boldStyle);
        assertTrue(StyleConstants.isBold(boldStyle));

        chatFrame.changeStyle(16, null, true, false, "bold");
        Style boldItalicStyle = doc.getStyle("bold-italic");
        assertEquals(style, boldItalicStyle);
        assertTrue(StyleConstants.isBold(boldItalicStyle));
        assertTrue(StyleConstants.isItalic(boldItalicStyle));

        chatFrame.changeStyle(16, Color.RED, true, true, "name");
        Style nameStyle = doc.getStyle("name");
        assertEquals(style, nameStyle);
        assertEquals(Color.RED, StyleConstants.getForeground(nameStyle));
        assertTrue(StyleConstants.isBold(nameStyle));
        assertTrue(StyleConstants.isItalic(nameStyle));
    }

    @Test
    public void testMouseListener() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setSize(new Dimension(100, 100));
            }
        };

        JList list = chatFrame.list;
        JTextPane textPane = chatFrame.showTxt;

        MouseEvent listMouseEvent = new MouseEvent(list, 1, System.currentTimeMillis(), 0, 1, 1, 1, false);
        chatFrame.mousePressed(listMouseEvent);
        chatFrame.maybeShowPopup(listMouseEvent);
        assertNotNull(chatFrame.listPop);
        chatFrame.listPop.dispose();

        MouseEvent textPaneMouseEvent = new MouseEvent(textPane, 1, System.currentTimeMillis(), 0, 1, 1, 1, false);
        chatFrame.mousePressed(textPaneMouseEvent);
        chatFrame.maybeShowPopup(textPaneMouseEvent);
        assertNotNull(chatFrame.txtPop);
        chatFrame.txtPop.dispose();
    }
}