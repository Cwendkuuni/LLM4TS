package dsachat.client.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;
import javax.swing.event.MouseEvent;

public class InternalChatFrameTest {

    @Test
    public void testCreateGUI() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setVisible(false);
            }
        };
        assertNotNull(chatFrame.listModel);
        assertNotNull(chatFrame.parent);
        assertNotNull(chatFrame.createGUI());
        assertNotNull(chatFrame.list);
        assertNotNull(chatFrame.showTxt);
        assertNotNull(chatFrame.txtInput);
        assertNotNull(chatFrame.doc);
        assertNotNull(chatFrame.listPop);
        assertNotNull(chatFrame.txtPop);
    }

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setVisible(false);
            }
        };
        String line = "You: Hello World";
        chatFrame.addText(line);
        String expected = "You: Hello World\n";
        String actual = chatFrame.showTxt.getText();
        assertEquals(expected, actual);

        line = "*John: Hi";
        chatFrame.addText(line);
        expected += "*John: Hi\n";
        actual = chatFrame.showTxt.getText();
        assertEquals(expected, actual);

        line = "*John: *Hi";
        chatFrame.addText(line);
        expected += "*John: *Hi\n";
        actual = chatFrame.showTxt.getText();
        assertEquals(expected, actual);

        line = "*John: Hi *Mike";
        chatFrame.addText(line);
        expected += "*John: Hi *Mike\n";
        actual = chatFrame.showTxt.getText();
        assertEquals(expected, actual);

        line = "*John: Hi *Mike *Mark";
        chatFrame.addText(line);
        expected += "*John: Hi *Mike *Mark\n";
        actual = chatFrame.showTxt.getText();
        assertEquals(expected, actual);
    }

    @Test
    public void testAddStylesToDocument() {
        StyledDocument doc = new PlainDocument();
        InternalChatFrame.addStylesToDocument(doc);
        assertEquals(14, doc.getStyleNames().length);
        assertTrue(doc.getStyleNames()[0].equals("regular"));
        assertTrue(doc.getStyleNames()[1].equals("italic"));
        assertTrue(doc.getStyleNames()[2].equals("bold"));
        assertTrue(doc.getStyleNames()[3].equals("bold-italic"));
        assertTrue(doc.getStyleNames()[4].equals("name"));
        assertTrue(doc.getStyleNames()[5].equals("system"));
        assertTrue(doc.getStyleNames()[6].equals("small"));
        assertTrue(doc.getStyleNames()[7].equals("large"));
    }

    @Test
    public void testChangeStyle() {
        StyledDocument doc = new PlainDocument();
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setVisible(false);
                this.doc = doc;
            }
        };
        chatFrame.changeStyle(12, Color.BLUE, false, false, "name");
        Style nameStyle = doc.getStyle("name");
        assertEquals(Color.BLUE, StyleConstants.getForeground(nameStyle));
        assertEquals(12, StyleConstants.getFontSize(nameStyle));
        assertEquals(false, StyleConstants.isBold(nameStyle));
        assertEquals(false, StyleConstants.isItalic(nameStyle));

        chatFrame.changeStyle(12, null, true, false, "name");
        nameStyle = doc.getStyle("name");
        assertEquals(null, StyleConstants.getForeground(nameStyle));
        assertEquals(12, StyleConstants.getFontSize(nameStyle));
        assertTrue(StyleConstants.isBold(nameStyle));
        assertEquals(false, StyleConstants.isItalic(nameStyle));

        chatFrame.changeStyle(12, Color.BLUE, true, true, "name");
        nameStyle = doc.getStyle("name");
        assertEquals(Color.BLUE, StyleConstants.getForeground(nameStyle));
        assertEquals(12, StyleConstants.getFontSize(nameStyle));
        assertTrue(StyleConstants.isBold(nameStyle));
        assertTrue(StyleConstants.isItalic(nameStyle));
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setVisible(false);
            }
        };
        String user = "Alice";
        chatFrame.addUser(user);
        String[] expectedUsers = {user};
        String[] actualUsers = chatFrame.getUserlist();
        assertArrayEquals(expectedUsers, actualUsers);
        assertEquals(user, chatFrame.listModel.get(0));
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setVisible(false);
                this.listModel = new DefaultListModel() {
                    {
                        addElement("Alice");
                        addElement("Bob");
                    }
                };
            }
        };
        String user = "Alice";
        chatFrame.deleteUser(user);
        String[] expectedUsers = {"Bob"};
        String[] actualUsers = chatFrame.getUserlist();
        assertArrayEquals(expectedUsers, actualUsers);
        assertFalse(chatFrame.listModel.contains(user));
    }

    @Test
    public void testMouseEventHandling() {
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame()) {
            {
                setVisible(false);
                this.addMouseListener(e -> {
                });
            }
        };
        MouseEvent mouseEvent = new MouseEvent(chatFrame.showTxt, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        chatFrame.maybeShowPopup(mouseEvent);
        assertTrue(chatFrame.txtPop.isVisible());
    }
}