package dsachat.client.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class InternalChatFrameTest {
    

    @Test
    public void testInternalChatFrameConstructor() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        assertNotNull(chatFrame);
    }

    @Test
    public void testCreateGUI() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        assertTrue(chatFrame.getTitle().equals("Chat"));
        assertTrue(chatFrame.isResizable());
        assertFalse(chatFrame.isFocusable());
        assertFalse(chatFrame.isMaximizable());
        assertFalse(chatFrame.isIconifiable());

        JSplitPane splitPane = (JSplitPane) chatFrame.getContentPane();
        JList list = (JList) splitPane.getLeftComponent();
        JTextPane showTxt = (JTextPane) splitPane.getRightComponent();
        JTextField txtInput = (JTextField) ((JPanel) splitPane.getRightComponent()).getComponent(1);

        assertEquals(0, list.getSelectedIndex());
        assertFalse(list.isEnabled());
        assertEquals(false, showTxt.isEditable());
        assertNotNull(txtInput);
        assertNull(chatFrame.listPop);
        assertNull(chatFrame.txtPop);
    }

    @Test
    public void testAddText() throws BadLocationException {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        StyledDocument doc = chatFrame.showTxt.getStyledDocument();

        chatFrame.addText("You: Hello World");
        String expectedText = "You: Hello World\n";
        assertEquals(expectedText, doc.getText(0, doc.getLength()));

        chatFrame.addText("User1: Hello You");
        expectedText += "User1: Hello You\n";
        assertEquals(expectedText, doc.getText(0, doc.getLength()));

        chatFrame.addText("*User1: Hello You*");
        expectedText += "*User1: Hello You*\n";
        assertEquals(expectedText, doc.getText(0, doc.getLength()));

        chatFrame.addText("**User1: Hello You**");
        expectedText += "**User1: Hello You**\n";
        assertEquals(expectedText, doc.getText(0, doc.getLength()));

        chatFrame.addText("User1: *Hello You*");
        expectedText += "User1: *Hello You*\n";
        assertEquals(expectedText, doc.getText(0, doc.getLength()));

        Style regular = doc.getStyle("regular");
        Style italic = doc.getStyle("italic");
        Style bold = doc.getStyle("bold");
        Style boldItalic = doc.getStyle("bold-italic");
        Style name = doc.getStyle("name");
        Style system = doc.getStyle("system");

        assertEquals(regular, doc.getStyle("regular"));
        assertEquals(italic, doc.getStyle("italic"));
        assertEquals(bold, doc.getStyle("bold"));
        assertEquals(boldItalic, doc.getStyle("bold-italic"));
        assertEquals(name, doc.getStyle("name"));
        assertEquals(system, doc.getStyle("system"));

        chatFrame.addText("User1: <html><body><b><i>Hello You</i></b></body></html>");
        expectedText += "<html><body><b><i>Hello You</i></b></body></html>\n";
        assertEquals(expectedText, doc.getText(0, doc.getLength()));
    }

    @Test
    public void testAddStylesToDocument() {
        StyledDocument doc = new PlainDocument();
        InternalChatFrame chatFrame = new InternalChatFrame();
        chatFrame.addStylesToDocument(doc);

        Style def = doc.getStyle("default");
        Style regular = doc.getStyle("regular");
        Style italic = doc.getStyle("italic");
        Style bold = doc.getStyle("bold");
        Style boldItalic = doc.getStyle("bold-italic");
        Style name = doc.getStyle("name");
        Style system = doc.getStyle("system");

        assertEquals(def, doc.getStyle("default"));
        assertEquals(regular, doc.getStyle("regular"));
        assertEquals(italic, doc.getStyle("italic"));
        assertEquals(bold, doc.getStyle("bold"));
        assertEquals(boldItalic, doc.getStyle("bold-italic"));
        assertEquals(name, doc.getStyle("name"));
        assertEquals(system, doc.getStyle("system"));
    }

    @Test
    public void testChangeStyle() {
        StyledDocument doc = new PlainDocument();
        InternalChatFrame chatFrame = new InternalChatFrame();
        chatFrame.addStylesToDocument(doc);

        Style name = doc.getStyle("name");
        Style regular = doc.getStyle("regular");

        chatFrame.changeStyle(12, Color.RED, false, false, "name");
        assertEquals(Color.RED, name.getForeground());
        assertEquals(12, (int) name.getFontSize());
        assertEquals(false, name.isBold());
        assertEquals(false, name.isItalic());

        chatFrame.changeStyle(12, null, true, false, "name");
        assertEquals(Color.BLACK, name.getForeground());
        assertEquals(12, (int) name.getFontSize());
        assertEquals(true, name.isBold());
        assertEquals(false, name.isItalic());

        chatFrame.changeStyle(12, Color.BLUE, false, true, "name");
        assertEquals(Color.BLACK, name.getForeground());
        assertEquals(12, (int) name.getFontSize());
        assertEquals(false, name.isBold());
        assertEquals(true, name.isItalic());
    }

    @Test
    public void testAddUser() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        JList list = (JList) chatFrame.getContentPane().getComponent(0);

        chatFrame.addUser("User1");
        assertEquals(1, list.getModel().getSize());
        assertEquals("User1", list.getModel().getElementAt(0));

        chatFrame.addUser("User2");
        assertEquals(2, list.getModel().getSize());
        assertEquals("User2", list.getModel().getElementAt(1));
    }

    @Test
    public void testDeleteUser() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        JList list = (JList) chatFrame.getContentPane().getComponent(0);

        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.deleteUser("User1");
        assertEquals(1, list.getModel().getSize());
        assertFalse(list.containsValue("User1"));
    }

    @Test
    public void testGetUserlist() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        JList list = (JList) chatFrame.getContentPane().getComponent(0);

        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        chatFrame.addUser("User3");

        List<String> userList = chatFrame.getUserlist();
        assertEquals(3, userList.size());
        assertEquals("User1", userList.get(0));
        assertEquals("User2", userList.get(1));
        assertEquals("User3", userList.get(2));
    }

    @Test(expected = NullPointerException.class)
    public void testActionPerformedWithNullName() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        chatFrame.actionPerformed(new ActionEvent(null, 0, "chooseColor"));
    }

    @Test
    public void testActionPerformedWhisper() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        JList list = (JList) chatFrame.getContentPane().getComponent(0);
        list.setSelectedValue("User1", true);
        chatFrame.actionPerformed(new ActionEvent(list, 0, "whisper"));
        assertEquals("/w User1 ", chatFrame.txtInput.getText());
    }

    @Test
    public void testMouseClicked() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        JList list = (JList) chatFrame.getContentPane().getComponent(0);

        MouseEvent event = new MouseEvent(list, 1, System.currentTimeMillis(), 0, 10, 10, 1, MouseEvent.BUTTON1_MASK, false);
        chatFrame.maybeShowPopup(event);
        assertTrue(chatFrame.listPop.isShowing());
    }

    @Test
    public void testGetChatlog() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        StyledDocument doc = chatFrame.showTxt.getStyledDocument();

        chatFrame.addText("You: Hello World");
        chatFrame.addText("User1: Hello You");
        chatFrame.addText("*User1: Hello You*");
        chatFrame.addText("**User1: Hello You**");
        chatFrame.addText("User1: <html><body><b><i>Hello You</i></b></body></html>");

        assertEquals("You: Hello World\nUser1: Hello You\n*User1: Hello You*\n**User1: Hello You**\n<html><body><b><i>Hello You</i></b></body></html>\n", chatFrame.getChatlog());
    }

    
}