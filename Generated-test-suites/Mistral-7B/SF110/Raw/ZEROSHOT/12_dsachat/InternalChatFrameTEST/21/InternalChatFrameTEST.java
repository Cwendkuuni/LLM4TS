package dsachat.client.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
 parent = new JFrame();
 chatFrame = new InternalChatFrame(parent);
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello";
 chatFrame.addText(line);
 StyledDocument doc = showTxt.getStyledDocument();
 String text = doc.getText(0, doc.getLength());
 assertEquals(line, text);
 }

 @Test
 public void testAddTextWithStyles() {
 String line = "*User1:*Hello** *User2*:Hi";
 chatFrame.addText(line);
 StyledDocument doc = showTxt.getStyledDocument();
 String expected = "*User1*:Hello** <i>*User2*:Hi</i>";
 String text = doc.getText(0, doc.getLength());
 assertEquals(expected, text);
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals("User1", chatFrame.listModel.getElementAt(0));
 }

 @Test
 public void testAddUsers() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 assertEquals(2, chatFrame.listModel.size());
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.deleteUser("User1");
 assertEquals(1, chatFrame.listModel.size());
 assertNotEquals("User1", chatFrame.listModel.getElementAt(0));
 }

 @Test
 public void testGetUserlist() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 String[] userList = chatFrame.getUserlist();
 assertEquals(2, userList.length);
 assertEquals("User1", userList[0]);
 assertEquals("User2", userList[1]);
 }

 @Test
 public void testActionPerformed() {
 String name = "User1";
 chatFrame.addUser(name);
 chatFrame.addText(name + ": Hello");
 txtInput.setText("User2: Hi");
 txtInput.doClick();
 txtInput.sendKeyEvent(new KeyEvent(KeyboardFocusManager.getCurrentFocus(), 1, true, false, KeyEvent.VK_ENTER, 0));
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "sendInput"));
 StyledDocument doc = showTxt.getStyledDocument();
 String expected = "*User1*: Hello\n<i>User2:</i> Hi";
 String text = doc.getText(0, doc.getLength());
 assertEquals(expected, text);
 }

 @Test
 public void testMouseClicked() {
 list.addListSelectionListener(new ListSelectionListener() {
 @Override
 public void valueChanged(ListSelectionEvent e) {
 chatFrame.maybeShowPopup(new MouseEvent(chatFrame, 1, 1, 1, 1, 1, 1, false));
 }
 });
 list.setSelectedValue("User1", false);
 chatFrame.addUser("User1");
 chatFrame.addText("User1: Hello");
 assertEquals(1, chatFrame.listModel.size());
 assertNull(chatFrame.listPop.isVisible());
 list.setSelectedValue("User1", true);
 chatFrame.mouseClicked(new MouseEvent(chatFrame, 1, 1, 1, 1, 1, 1, false));
 assertTrue(chatFrame.listPop.isVisible());
 }
}