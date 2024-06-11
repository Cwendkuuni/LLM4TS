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
 parent = new MainFrame();
 chatFrame = new InternalChatFrame(parent);
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @Test
 public void testAddText() {
 String line1 = "You: Hello, World!";
 chatFrame.addText(line1);
 assertEquals("" + line1 + "\n", showTxt.getText());

 String line2 = "*User1*: Hi!";
 chatFrame.addText(line2);
 assertEquals("You: Hello, World!\n" + "*User1*: Hi!\n", showTxt.getText());

 String line3 = "*User1*: *Hello* there! *User1*.";
 chatFrame.addText(line3);
 assertEquals("You: Hello, World!\n" + "*User1*: Hi!\n" + "*User1*: *Hello* there! *User1*.\n", showTxt.getText());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 assertEquals("User1", list.getModel().getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 String[] userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 chatFrame.deleteUser("User1");
 chatFrame.addText("Test delete user");
 String text = showTxt.getText();
 assertTrue(text.contains("Test delete user"));
 assertEquals(1, list.getModel().getSize());
 }

 @Test
 public void testGetChatlog() {
 chatFrame.addText("Test get chatlog");
 String chatlog = chatFrame.getChatlog();
 assertEquals("Test get chatlog\n", chatlog);
 }

 @Test
 public void testActionCommandSendInput() {
 String text = "Test action command send input";
 txtInput.setText(text);
 txtInput.doClick();
 chatFrame.actionPerformed(new ActionEvent(txtInput, 0, "sendInput"));
 assertEquals(text, chatFrame.getChatlog().substring(chatFrame.getChatlog().length() - text.length()));
 }

 @Test
 public void testListSelectionListener() {
 list.addListSelectionListener(new ListSelectionListener() {
 @Override
 public void valueChanged(ListSelectionEvent e) {
 if (e.getValueIsAdjusting()) {
 return;
 }
 String selectedUser = (String) list.getSelectedValue();
 assertTrue(selectedUser != null);
 }
 });
 list.setSelectedValue("You", false);
}

 @Test(expected = NullPointerException.class)
 public void testNullPointerException() {
 chatFrame.addText(null);
 }
}