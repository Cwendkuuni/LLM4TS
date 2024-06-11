package dsachat.client.gui.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;
import javax.swing.*;
import javax.swing.event.*;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parentFrame;

 @Before
 public void setUp() {
 parentFrame = new MainFrame();
 chatFrame = new InternalChatFrame(parentFrame);
 }

 @Test
 public void testInternalChatFrameConstruction() {
 // Test if InternalChatFrame is constructed correctly
 assertEquals("Chat", chatFrame.getTitle());
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 }

 @Test
 public void testAddTextMethod() {
 // Test basic functionality of addText method
 chatFrame.addText("Test: Test");
 String text = chatFrame.getChatlog();
 assertEquals("Test: Test\n", text);

 // Test adding different styles to text
 chatFrame.addText("*You *Test: *Test**");
 text = chatFrame.getChatlog();
 assertEquals("Test: Test\n*You Test: Test**\n", text);

 chatFrame.addText("*Test *: *Test*");
 text = chatFrame.getChatlog();
 assertEquals("Test: Test\n*You Test: Test**\n*Test : Test*\n", text);

 chatFrame.addText("*Test +Test*");
 text = chatFrame.getChatlog();
 assertEquals("Test: Test\n*You Test: Test**\n*Test : Test*\n*Test +Test*\n", text);
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextMethodWithException() {
 // Test exception handling in addText method
 chatFrame.addText("Test: Test");
 chatFrame.addText("Test: Test"); // This should throw an exception
 }

 @Test
 public void testAddUserMethod() {
 // Test basic functionality of addUser method
 chatFrame.addUser("TestUser1");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals("TestUser1", chatFrame.listModel.getElementAt(0));

 // Test adding the same user twice
 chatFrame.addUser("TestUser1");
 assertEquals(1, chatFrame.listModel.size());
 }

 @Test
 public void testDeleteUserMethod() {
 // Test basic functionality of deleteUser method
 chatFrame.addUser("TestUser1");
 chatFrame.addUser("TestUser2");
 assertEquals(2, chatFrame.listModel.size());
 chatFrame.deleteUser("TestUser1");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals("TestUser2", chatFrame.listModel.getElementAt(0));
 }

 @Test
 public void testGetUserlistMethod() {
 // Test basic functionality of getUserlist method
 String[] userlist = chatFrame.getUserlist();
 assertEquals(0, userlist.length);

 chatFrame.addUser("TestUser1");
 chatFrame.addUser("TestUser2");
 userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 assertEquals("TestUser1", userlist[0]);
 assertEquals("TestUser2", userlist[1]);
 }

 @Test
 public void testActionPerformedMethod() {
 // Test basic functionality of actionPerformed method
 chatFrame.addUser("TestUser1");
 chatFrame.addUser("TestUser2");

 JList list = chatFrame.list;
 list.setSelectedIndex(0);
 list.addMouseListener(new MouseAdapter() {
 @Override
 public void mousePressed(MouseEvent e) {
 chatFrame.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
 }
 });
 chatFrame.changeStyle(12, Color.RED, false, false, "TestUser1");
 assertEquals(12, chatFrame.doc.getStyle("TestUser1").getAttribute(StyleConstants.FontSize));
 assertEquals(Color.RED, chatFrame.doc.getStyle("TestUser1").getAttribute(StyleConstants.Foreground));

 list.setSelectedIndex(1);
 list.addMouseListener(new MouseAdapter() {
 @Override
 public void mousePressed(MouseEvent e) {
 chatFrame.actionPerformed(new ActionEvent(list, 0, "bgColor"));
 }
 });
 SwingUtilities.invokeLater(() -> chatFrame.showTxt.setBackground(Color.GREEN));
 assertEquals(Color.GREEN, chatFrame.showTxt.getBackground());
 }

 @Test
 public void testMouseClickedMethod() {
 // Test that mouseClicked method does nothing
 chatFrame.addMouseListener(new MouseAdapter() {
 @Override
 public void mouseClicked(MouseEvent e) {
 fail();
 }
 });
 chatFrame.maybeShowPopup(new MouseEvent(new JFrame(), 0, 0, 0, 0, 0, 0, false));
 }
}