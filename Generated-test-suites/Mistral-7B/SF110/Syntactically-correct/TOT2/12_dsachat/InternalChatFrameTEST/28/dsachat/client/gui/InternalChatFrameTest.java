package dsachat.client.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.MouseEvent;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;
    
    @Before
    public void setUp() {
        JFrame parent = new JFrame();
        this.chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.chatFrame);
    }

    @Test
    public void testCreateGUI() {
        this.chatFrame.createGUI();
        assertTrue(this.chatFrame.getTitle().equals("Chat"));
    }

    @Test
    public void testAddText() {
        this.chatFrame.addText("Test: Test");
        String chatLog = this.chatFrame.getChatlog();
        assertTrue(chatLog.contains("Test: Test"));
    }

    @Test
    public void testAddStylesToDocument() {
        this.chatFrame.addStylesToDocument(this.chatFrame.doc);
        Style s = this.chatFrame.doc.getStyle("regular");
        StyleConstants.setFontFamily(s, "SansSerif");
        assertEquals(s.getName(), "regular");
    }

    @Test
    public void testChangeStyle() {
        this.chatFrame.changeStyle(12, Color.RED, true, false, "name");
        Style s = this.chatFrame.doc.getStyle("name");
        assertEquals(s.getName(), "name");
        assertEquals(s.getFontSize(), 12);
        assertTrue(s.isBold());
        assertFalse(s.isItalic());
    }

    @Test
    public void testAddUser() {
        this.chatFrame.addUser("Test");
        assertEquals(this.chatFrame.listModel.getSize(), 1);
        assertEquals(this.chatFrame.listModel.getElementAt(0), "Test");
    }

    @Test
    public void testDeleteUser() {
        this.chatFrame.addUser("Test");
        this.chatFrame.deleteUser("Test");
        assertEquals(this.chatFrame.listModel.getSize(), 0);
    }

    @Test
    public void testGetUserlist() {
        this.chatFrame.addUser("Test1");
        this.chatFrame.addUser("Test2");
        String[] userList = this.chatFrame.getUserlist();
        assertEquals(userList.length, 2);
        assertEquals(userList[0], "Test1");
        assertEquals(userList[1], "Test2");
    }

    @Test
    public void testMouseEvent() {
        JList mockList = new JList();
        JTextPane mockTextPane = new JTextPane();
        MouseEvent mockEvent = new MouseEvent(mockList, 1, 1, 1, 1, new Point(1, 1));
        this.chatFrame.maybeShowPopup(mockEvent);
        assertNull(this.chatFrame.listPop);
        assertNull(this.chatFrame.txtPop);
    }
}