package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

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

 @After
 public void tearDown() {
 chatFrame.dispose();
 parent.dispose();
 }

 @Test
 public void testConstructor() {
 assertTrue(chatFrame.isVisible());
 assertEquals("Chat", chatFrame.getTitle());
 }

 @Test
 public void testCreateGUI() {
 assertEquals(2, chatFrame.getContentPane().getComponentCount());
 assertTrue(chatFrame.getContentPane().getComponentAt(0) instanceof JSplitPane);
 assertTrue(chatFrame.getContentPane().getComponentAt(1) instanceof JPanel);
 }

 @Test
 public void testAddText() {
 String line = "TestUser: Hello World!";
 chatFrame.addText(line);
 assertEquals(1, chatFrame.getUserlist().length);
 assertEquals("TestUser", chatFrame.getUserlist()[0]);
 String chatLog = chatFrame.getChatlog();
 assertTrue(chatLog.contains(line));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddTextInvalidLineFormat() {
 chatFrame.addText("Hello World!");
 fail("Failed to throw IllegalArgumentException with invalid line format");
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = chatFrame.doc;
 assertEquals(13, doc.getStyleCount());
 }

 @Test
 public void testChangeStyle() {
 String name = "TestUser";
 chatFrame.addUser(name);
 chatFrame.addText("TestUser: Test");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals(name, chatFrame.listModel.get(0));
 chatFrame.changeStyle(14, Color.RED, true, false, name);
 JTextComponent txt = chatFrame.showTxt;
 assertEquals(14, txt.getComponentCount());
 assertTrue(txt.getComponentAt(0) instanceof StyleContext);
 StyleContext sc = (StyleContext) txt.getComponentAt(0);
 StyleAttributes sa = sc.getStyleAttributes();
 assertEquals(Color.RED, sa.getBackground());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChangeStyleInvalidName() {
 chatFrame.changeStyle(14, Color.RED, true, false, "InvalidName");
 fail("Failed to throw IllegalArgumentException with invalid name");
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("TestUser1");
 chatFrame.addText("TestUser1: Test1");
 chatFrame.addUser("TestUser2");
 chatFrame.addText("TestUser2: Test2");
 String[] userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 assertEquals("TestUser1", userlist[0]);
 assertEquals("TestUser2", userlist[1]);
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("TestUser1");
 chatFrame.addText("TestUser1: Test1");
 chatFrame.deleteUser("TestUser1");
 String[] userlist = chatFrame.getUserlist();
 assertEquals(0, userlist.length);
 }

 @Test
 public void testGetUserlist() {
 chatFrame.addUser("TestUser1");
 chatFrame.addUser("TestUser2");
 String[] userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 }

 @Test
 public void testActionPerformedChooseColor() {
 Color newColor = Color.GREEN;
 chatFrame.actionPerformed(new ActionEvent("chooseColor", ActionEvent.ACTION_PERFORMED, "chooseColor"));
 JTextComponent txt = chatFrame.showTxt;
 StyleAttributes sa = ((StyleContext) txt.getComponentAt(0)).getStyleAttributes();
 assertEquals(newColor, sa.getBackground());
 }

 @Test
 public void testActionPerformedWhisper() {
 String wUser = "/w TestUser TestWhisper";
 chatFrame.actionPerformed(new ActionEvent("whisper", ActionEvent.ACTION_PERFORMED, "whisper"));
 assertEquals(wUser, txtInput.getText());
 }
}