package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                this.doc = new SimpleAttributeSet();
            }
        };

        frame.addText("You: hello");
        assertEquals("You: hello\n", frame.doc.getDocumentElement().getText(0, frame.doc.getLength()));

        frame.addText("*User1: hello*");
        Style user1Style = frame.doc.getStyle("User1");
        assertEquals("User1", user1Style.getName());
        StyleConstants.assertBoolean(user1Style, StyleConstants.Bold, true);
        StyleConstants.assertBoolean(user1Style, StyleConstants.Italic, true);

        frame.addText("User2: world");
        Style user2Style = frame.doc.getStyle("User2");
        assertEquals("User2", user2Style.getName());
        StyleConstants.assertBoolean(user2Style, StyleConstants.Bold, false);
        StyleConstants.assertBoolean(user2Style, StyleConstants.Italic, false);
    }

    @Test
    public void testAddStylesToDocument() {
        InternalChatFrame frame = new InternalChatFrame(new MainFrame());
        StyledDocument doc = frame.doc;

        assertEquals(13, doc.getStyleCount());

        Style regular = doc.getStyle("regular");
        assertEquals("default", regular.getName());

        Style name = doc.getStyle("name");
        assertEquals("name", name.getName());
        assertEquals(Color.BLUE, name.getForeground());
        assertEquals(16, name.getFontSize());

        Style system = doc.getStyle("system");
        assertEquals("system", system.getName());
        assertEquals(new Color(0.0f, 0.6f, 0.0f, 1.0f), system.getForeground());
        assertEquals(StyleConstants.ITALIC, system.getBackground());
        assertEquals(14, system.getFontSize());
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame frame = new InternalChatFrame(new MainFrame()) {
            {
                this.doc = new SimpleAttributeSet();
            }
        };

        Style style = frame.doc.getStyle("name");
        frame.changeStyle(18, Color.RED, true, false, "name");
        assertEquals(18, style.getFontSize());
        assertFalse(style.getBold());
        assertFalse(style.isItalic());

        frame.changeStyle(18, null, false, true, "name");
        assertTrue(style.isItalic());
        assertFalse(style.getBold());

        frame.changeStyle(18, Color.CYAN, true, true, "name");
        assertEquals(18, style.getFontSize());
        assertTrue(style.getBold());
        assertTrue(style.isItalic());
    }

    @Test
    public void testActionPerformed() {
        MainFrame mainFrame = mock(MainFrame.class);
        when(mainFrame.hero).thenReturn(new Hero("Hero"));
        InternalChatFrame frame = new InternalChatFrame(mainFrame) {
            {
                this.doc = new SimpleAttributeSet();
            }
        };

        frame.listModel.addElement("User1");
        frame.listModel.addElement("User2");

        frame.actionPerformed(new ActionEvent(frame.list, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        Style user1Style = frame.doc.getStyle("User1");
        Color newColor = Color.RED;
        frame.changeStyle(12, newColor, false, false, "User1");
        assertEquals(newColor, user1Style.getForeground());

        frame.actionPerformed(new ActionEvent(frame.list, ActionEvent.ACTION_PERFORMED, "bgColor"));
        JColorChooser chooser = mock(JColorChooser.class);
        when(chooser.showDialog(frame, "Choose Color for user User1", Color.BLACK)).thenReturn(newColor);
        doAnswer(invocation -> frame.showTxt.setBackground(newColor)).when(frame).changeStyle(anyInt(), any(Color.class), anyBoolean(), anyBoolean(), anyString());
        frame.actionPerformed(new ActionEvent(frame.list, ActionEvent.ACTION_PERFORMED, "bgColor"));
        assertEquals(newColor, frame.showTxt.getBackground());
    }
}