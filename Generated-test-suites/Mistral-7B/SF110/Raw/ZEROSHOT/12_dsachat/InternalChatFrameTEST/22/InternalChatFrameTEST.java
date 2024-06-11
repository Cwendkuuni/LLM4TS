package dsachat.client.gui;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = new MainFrame();
 chatFrame = new InternalChatFrame(parent);
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @Test
 public void testInternalChatFrameConstruction() {
 assertNotNull(chatFrame);
 assertNotNull(list);
 assertNotNull(showTxt);
 assertNotNull(txtInput);
 }

 @Test
 public void testCreateGUI() {
 GridBagConstraints c = new GridBagConstraints();
 JSplitPane split = chatFrame.split;
 assertTrue(split.getLeftComponent() instanceof JScrollPane);
 assertTrue(split.getRightComponent() instanceof JPanel);
 assertNotNull(chatFrame.listPop);
 assertNotNull(chatFrame.txtPop);
 }

 @Test
 public void testAddText() {
 String line = "Test: Test";
 chatFrame.addText(line);
 String chatLog = chatFrame.getChatlog();
 assertEquals("Test: Test\n", chatLog);
 }

 @Test
 public void testAddTextWithStyles() {
 String line = "*Test* *Test*: Test_Test";
 chatFrame.addText(line);
 String chatLog = chatFrame.getChatlog();
 String expected = "*Test* Test:\n_Test_ Test\n";
 assertEquals(expected, chatLog);
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = chatFrame.doc;
 Style def = doc.getStyle("default");
 Style regular = doc.getStyle("regular");
 assertNotNull(doc.getStyle("regular"));
 assertNotNull(doc.getStyle("italic"));
 assertNotNull(doc.getStyle("bold"));
 assertNotNull(doc.getStyle("bold-italic"));
 assertNotNull(doc.getStyle("name"));
 assertNotNull(doc.getStyle("system"));
 assertNotNull(doc.getStyle("small"));
 assertNotNull(doc.getStyle("large"));
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 List<?> model = list.getModel();
 assertEquals(1, model.size());
 String[] userlist = chatFrame.getUserlist();
 assertEquals("User1", userlist[0]);
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 String[] userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 chatFrame.deleteUser("User1");
 userlist = chatFrame.getUserlist();
 assertEquals(1, userlist.length);
 assertEquals("User2", userlist[0]);
 }

 @Test
 public void testActionPerformed() {
 String name = "User1";
 chatFrame.listModel.addElement(name);
 chatFrame.addUser(name);
 chatFrame.actionPerformed(new ActionEvent(list, ActionEvent.ACTION_PERFORMED, "chooseColor"));
 Style s = chatFrame.doc.getStyle(name);
 Color color = new Color(100, 0, 0);
 chatFrame.changeStyle(12, color, false, false, name);
 assertEquals(color, s.getForeground());
}

 @Test
 public void testWhisper() {
 String name = "User1";
 chatFrame.listModel.addElement(name);
 chatFrame.addUser(name);
 String whisperText = "/w User1 Test";
 txtInput.setText(whisperText);
 txtInput.doClick();
 txtInput.sendKeyEvents(new KeyEvent(KeyboardFocusManager.getCurrentKeyEvent(), 1, true, false, false));
 assertEquals(whisperText, chatFrame.getChatlog().substring(chatFrame.getChatlog().lastIndexOf("\n") + 1));
}

 @Test
 public void testMouseClicked() {
 MouseEvent e = new MouseEvent(chatFrame, 1, 1, 1, 1, 1, 1, true);
 chatFrame.maybeShowPopup(e);
 assertFalse(chatFrame.listPop.isVisible());
 assertFalse(chatFrame.txtPop.isVisible());
}
}