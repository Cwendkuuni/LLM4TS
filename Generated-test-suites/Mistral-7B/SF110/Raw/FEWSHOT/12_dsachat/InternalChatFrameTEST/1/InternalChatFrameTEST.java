package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.junit.Test;

public class InternalChatFrameTest {

    @Test
    public void testCreateGUI() {
        final InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                setVisible(true);
            }
        };
        assertNotNull(frame.list);
        assertNotNull(frame.showTxt);
        assertNotNull(frame.txtInput);
        assertNull(frame.doc);
        assertNull(frame.listPop);
        assertNull(frame.txtPop);
        assertTrue(frame.isShowing());
    }

    @Test
    public void testAddText() throws BadLocationException {
        final StyledDocument doc = new DefaultStyledDocument();
        final InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                this.doc = doc;
                setVisible(true);
            }
        };
        frame.addText("You: Hello World");
        String expected = "Hello World\n";
        assertEquals(expected, doc.getText(0, doc.getLength()));

        frame.addText("User1: Hello");
        expected += "User1: Hello\n";
        assertEquals(expected, doc.getText(0, doc.getLength()));

        Style style = doc.getStyle("You ");
        assertNotNull(style);
        assertEquals("BLUE", StyleConstants.getName(style.getForeground()));
        assertEquals(16, StyleConstants.getFontSize(style));

        style = doc.getStyle("User1");
        assertNotNull(style);
        assertEquals("default", StyleConstants.getName(style.getParent()));
        assertEquals("SansSerif", StyleConstants.getFontFamily(style));
        assertEquals(14, StyleConstants.getFontSize(style));

        frame.addText("You: *Hello Again*");
        expected += "\nYou: *Hello Again*\n";
        assertEquals(expected, doc.getText(0, doc.getLength()));

        style = doc.getStyle("You *");
        assertNotNull(style);
        assertFalse(StyleConstants.isItalic(style));

        style = doc.getStyle("You _");
        assertNotNull(style);
        assertTrue(StyleConstants.isItalic(style));
    }

    @Test
    public void testAddStylesToDocument() {
        final StyledDocument doc = new DefaultStyledDocument();
        final InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                this.doc = doc;
                setVisible(true);
            }
        };
        frame.addStylesToDocument(doc);

        Style style = doc.getStyle("regular");
        assertEquals("default", StyleConstants.getName(style.getParent()));
        assertEquals("SansSerif", StyleConstants.getFontFamily(style));
        assertEquals(14, StyleConstants.getFontSize(style));

        style = doc.getStyle("italic");
        assertEquals("regular", StyleConstants.getName(style.getParent()));
        assertTrue(StyleConstants.isItalic(style));

        style = doc.getStyle("bold");
        assertEquals("regular", StyleConstants.getName(style.getParent()));
        assertTrue(StyleConstants.isBold(style));

        style = doc.getStyle("bold-italic");
        assertEquals("regular", StyleConstants.getName(style.getParent()));
        assertTrue(StyleConstants.isBold(style));
        assertTrue(StyleConstants.isItalic(style));

        style = doc.getStyle("name");
        assertEquals("regular", StyleConstants.getName(style.getParent()));
        assertEquals("BLUE", StyleConstants.getName(style.getForeground()));
        assertEquals(16, StyleConstants.getFontSize(style));

        style = doc.getStyle("system");
        assertEquals("regular", StyleConstants.getName(style.getParent()));
        assertEquals("SansSerif", StyleConstants.getFontFamily(style));
        assertEquals(14, StyleConstants.getFontSize(style));
        assertEquals(0.6f, StyleConstants.getAlpha(style.getForeground()));
    }

    @Test
    public void testAddUser() {
        final InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                setVisible(true);
            }
        };
        frame.addUser("User1");
        assertEquals(1, frame.listModel.getSize());
        assertEquals("User1", frame.listModel.getElementAt(0));

        frame.addText("User1: Hello");
        assertEquals("User1: Hello\n", frame.showTxt.getText());

        Style style = frame.doc.getStyle("User1");
        assertEquals("default", StyleConstants.getName(style.getParent()));
        assertEquals("SansSerif", StyleConstants.getFontFamily(style));
        assertEquals(14, StyleConstants.getFontSize(style));
    }

    @Test
    public void testDeleteUser() {
        final InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                setVisible(true);
            }
        };

        frame.addUser("User1");
        frame.addUser("User2");
        assertArrayEquals(new String[]{"User1", "User2"}, frame.getUserlist());

        frame.deleteUser("User1");
        assertArrayEquals(new String[]{"User2"}, frame.getUserlist());

        frame.deleteUser("User2");
        assertEquals(0, frame.listModel.getSize());
    }

    @Test
    public void testGetUserlist() {
        final InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                setVisible(true);
            }
        };

        frame.addUser("User1");
        frame.addUser("User2");
        assertArrayEquals(new String[]{"User1", "User2"}, frame.getUserlist());
    }

    @Test
    public void testActionPerformed() {
        final InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                setVisible(true);
            }
        };

        final JList list = frame.list;
        final String user1 = "User1";
        final String user2 = "User2";
        final String whisperCommand = "/w ";

        list.setSelectedValue(user1);
        frame.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
        // no assertions here, as we cannot test the GUI appearance

        list.setSelectedValue(user2);
        frame.actionPerformed(new ActionEvent(list, 0, "bgColor"));
        // no assertions here, as we cannot test the GUI appearance

        list.setSelectedValue(user1);
        frame.actionPerformed(new ActionEvent(list, 0, "whisper"));
        assertEquals(whisperCommand + user1, frame.txtInput.getText());
    }

    @Test
    public void testMouseListener() {
        final InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                setVisible(true);
            }
        };

        final JList list = frame.list;
        final JTextPane textPane = frame.showTxt;
        final JTextField inputField = frame.txtInput;
        final JPopupMenu popupMenu = frame.listPop;

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                popupMenu.show(list, e.getX(), e.getY());
            }
        });

        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                popupMenu.show(textPane, e.getX(), e.getY());
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == list && list.isSelectionEmpty()) {
                    inputField.requestFocus();
                }
            }
        });

        list.setSelectedValue("User1");
        list.dispatchEvent(new MouseEvent(list, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, list.getWidth(), list.getHeight(), 1, MouseEvent.BUTTON1_DOWN_MASK, false));
        assertTrue(popupMenu.isShowing());
        popupMenu.setVisible(false);

        textPane.setText("Hello World");
        textPane.dispatchEvent(new MouseEvent(textPane, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, textPane.getWidth(), textPane.getHeight(), 1, MouseEvent.BUTTON1_DOWN_MASK, false));
        assertTrue(popupMenu.isShowing());
        popupMenu.setVisible(false);

        list.setSelectedValue(null);
        inputField.requestFocus();
        list.dispatchEvent(new MouseEvent(list, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, MouseEvent.BUTTON1_DOWN_MASK, false));
        assertTrue(inputField.hasFocus());
    }

    @Test
    public void testGetChatlog() {
        final InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                setVisible(true);
            }
        };

        frame.addText("You: Hello World");
        frame.addText("User1: Hello");
        frame.addText("You: *Hello Again*");

        assertEquals("Hello World\nUser1: Hello\nYou: *Hello Again*\n", frame.getChatlog());
    }
}