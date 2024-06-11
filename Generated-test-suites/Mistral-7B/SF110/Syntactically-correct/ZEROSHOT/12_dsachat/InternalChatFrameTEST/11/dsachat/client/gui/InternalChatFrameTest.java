package dsachat.client.gui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

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
 assertNotNull(parent);
 }

 @Test
 public void testAddTextMethod() {
 String line = "You: Hello, World!";
 chatFrame.addText(line);
 assertEquals("You: Hello, World!\n", showTxt.getText());
 }

 @Test
 public void testAddStylesToDocumentMethod() {
 assertNotNull(chatFrame.doc);
 }

 @Test
 public void testAddUserMethod() {
 chatFrame.addUser("User1");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals("User1", chatFrame.listModel.getElementAt(0));
 }

 @Test
 public void testDeleteUserMethod() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 assertEquals(2, chatFrame.listModel.size());
 chatFrame.deleteUser("User1");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals("User2", chatFrame.listModel.getElementAt(0));
 }

 @Test
 public void testGetUserlistMethod() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 List<String> userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.size());
 assertEquals("User1", userlist.get(0));
 assertEquals("User2", userlist.get(1));
 }

 @Test
 public void testActionPerformedMethod() {
 String name = "User1";
 chatFrame.addUser(name);
 chatFrame.list.setSelectedValue(name, true);
 txtInput.setText("Test Message");
 txtInput.doClick();
 txtInput.sendKeyEvent(new KeyEvent(txtInput, 1, System.currentTimeMillis(), 0, KeyEvent.KEY_PRESSED, 'S'));
 txtInput.sendKeyEvent(new KeyEvent(txtInput, 1, System.currentTimeMillis(), 0, KeyEvent.KEY_RELEASED, 'S'));
 chatFrame.actionPerformed(new ActionEvent(txtInput, 0, "sendInput"));
 assertEquals("You: Test Message\n", showTxt.getText());
 }

 @Test
 public void testMouseClickedMethod() {
 MouseEvent mouseEvent = new MouseEvent(chatFrame, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 0, false);
 chatFrame.maybeShowPopup(mouseEvent);
 assertNotNull(chatFrame.listPop);
 }

 @Test
 public void testGetChatlogMethod() {
 String chatlog = "You: Hello, World!\nUser1: Hi there!\nYou: How are you?";
 chatFrame.addText("You: Hello, World!");
 chatFrame.addText("User1: Hi there!");
 chatFrame.addText("You: How are you?");
 assertEquals(chatlog, chatFrame.getChatlog());
 }
}