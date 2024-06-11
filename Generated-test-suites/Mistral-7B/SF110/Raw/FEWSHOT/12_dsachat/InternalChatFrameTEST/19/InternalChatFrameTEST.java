package dsachat.client.gui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.swing.*;
import javax.swing.event.MouseEvent;

import org.junit.Test;

public class InternalChatFrameTest {

    @Test
    public void testCreateGUI() {
        final MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        final InternalChatFrame chatFrame = new InternalChatFrame(parent);
        assertNotNull(chatFrame.list);
        assertNotNull(chatFrame.showTxt);
        assertNotNull(chatFrame.txtInput);
        assertNotNull(chatFrame.listPop);
        assertNotNull(chatFrame.txtPop);
        assertTrue(chatFrame.isVisible());
    }

    @Test
    public void testAddText() {
        final InternalChatFrame chatFrame = new InternalChatFrame(null);
        final String line = "You: Hello World";
        chatFrame.addText(line);
        JTextPane txtPane = chatFrame.showTxt;
        assertEquals(line, txtPane.getText(0, txtPane.getDocument().getLength()));
    }

    @Test
    public void testChangeStyle() {
        final InternalChatFrame chatFrame = new InternalChatFrame(null);
        final String name = "Test User";
        Style style = chatFrame.doc.getStyle(name);
        chatFrame.changeStyle(12, Color.RED, true, false, name);
        assertEquals(Color.RED, style.getForeground());
        assertTrue(style.isBold());
    }

    @Test
    public void testAddUser() {
        final InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addUser("Test User");
        assertEquals("Test User", chatFrame.list.getModel().getElementAt(0));
    }

    @Test
    public void testMouseEvent() {
        final JFrame frame = new JFrame();
        final InternalChatFrame chatFrame = new InternalChatFrame(null);
        frame.add(chatFrame);
        frame.setVisible(true);
        final MouseEvent mouseEvent = new MouseEvent(chatFrame, 1, 50, 1, 50, 50, 0);
        chatFrame.maybeShowPopup(mouseEvent);
        assertTrue(chatFrame.listPop.isVisible());
        chatFrame.listPop.setVisible(false);
        assertTrue(chatFrame.txtPop.isVisible());
        chatFrame.txtPop.setVisible(false);
    }

    @Test
    public void testGetChatlog() {
        final InternalChatFrame chatFrame = new InternalChatFrame(null);
        String log = chatFrame.getChatlog();
        assertNotNull(log);
    }
}