package dsachat.client.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;
    private JList list;
    private JTextPane showTxt;
    private JTextField txtInput;
    private MainFrame parent;

    @Before
    public void setup() {
        parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
        list = chatFrame.list;
        showTxt = chatFrame.showTxt;
        txtInput = chatFrame.txtInput;
    }

    @Test
    public void testInternalChatFrameConstructor() {
        assertNotNull(chatFrame);
    }

    @Test
    public void testCreateGUI() {
        assertEquals(2, chatFrame.getComponentCount());
        assertInstanceOf(JSplitPane.class, chatFrame.getComponent(0));
        assertInstanceOf(JPanel.class, chatFrame.getComponent(1));
    }

    @Test
    public void testAddText() {
        String line = "Test: Test";
        chatFrame.addText(line);
        assertEquals(line, showTxt.getText().substring(showTxt.getText().lastIndexOf("\n") + 1));
    }

    @Test
    public void testAddStylesToDocument() {
        chatFrame.addStylesToDocument(chatFrame.doc);
        assertEquals(13, chatFrame.doc.getStyleCount());
    }

    @Test
    public void testChangeStyle() {
        chatFrame.changeStyle(12, Color.RED, true, false, "name");
        Style nameStyle = chatFrame.doc.getStyle("name");
        assertTrue(StyleConstants.isBold(nameStyle));
        assertFalse(StyleConstants.isItalic(nameStyle));
        assertNotNull(StyleConstants.getForeground(nameStyle));
        assertEquals(Color.RED, StyleConstants.getForeground(nameStyle));
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("TestUser");
        assertEquals("TestUser", list.getModel().getElementAt(0));
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("TestUser1");
        chatFrame.addUser("TestUser2");
        chatFrame.deleteUser("TestUser1");
        assertEquals(1, list.getModel().getSize());
        assertFalse(list.getItemAt(0).equals("TestUser1"));
    }

    @Test
    public void testGetUserlist() {
        chatFrame.addUser("TestUser1");
        chatFrame.addUser("TestUser2");
        String[] userlist = chatFrame.getUserlist();
        assertEquals(2, userlist.length);
        assertTrue(userlist[0].equals("TestUser1"));
        assertTrue(userlist[1].equals("TestUser2"));
    }

    @Test
    public void testActionPerformedWhisper() {
        String name = "/w TestUser TestMessage";
        txtInput.setText(name);
        txtInput.doClick();
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 113)); // 'w' key
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 32)); // space key
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 32)); // space key
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 32)); // space key
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 32)); // space key
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 32)); // space key
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 32)); // space key
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 32)); // space key
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 32)); // space key
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 32)); // space key
        txtInput.sendKeyEvents(new ActionEvent(txtInput, 0, 13)); // 'Enter' key
        String chatlog = chatFrame.getChatlog();
        assertTrue(chatlog.contains(name));
    }

    @Test
    public void testMouseClickedList() {
        MouseEvent mouseEvent = new MouseEvent(list, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 100, 100, 1, false);
        list.dispatchEvent(mouseEvent);
        assertTrue(chatFrame.listPop.isShowing());
    }

    @Test
    public void testMouseClickedShowText() {
        MouseEvent mouseEvent = new MouseEvent(showTxt, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 100, 100, 1, false);
        showTxt.dispatchEvent(mouseEvent);
        assertTrue(chatFrame.txtPop.isShowing());
    }
}