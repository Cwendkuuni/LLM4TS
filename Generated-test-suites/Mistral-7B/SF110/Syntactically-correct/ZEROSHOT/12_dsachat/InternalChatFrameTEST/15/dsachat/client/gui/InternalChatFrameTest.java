package dsachat.client.gui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private MainFrame parentFrame;
 private InternalChatFrame internalChatFrame;

 @Before
 public void setUp() {
 parentFrame = new MainFrame();
 internalChatFrame = new InternalChatFrame(parentFrame);
 SwingUtilities.invokeLater(() -> {
 parentFrame.setVisible(true);
 internalChatFrame.setVisible(true);
 });
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(() -> {
 parentFrame.dispose();
 internalChatFrame.dispose();
 });
 }

 @Test
 public void testConstructor() {
 assertNotNull(internalChatFrame);
 assertEquals("Chat", internalChatFrame.getTitle());
 }

 @Test
 public void testCreateGUI() {
 assertNotNull(internalChatFrame.list);
 assertNotNull(internalChatFrame.showTxt);
 assertNotNull(internalChatFrame.txtInput);
 assertEquals(0, internalChatFrame.list.getSelectedIndex());
 }

 @Test
 public void testAddText() {
 final String text = "Test: You: Hello World";
 internalChatFrame.addText(text);
 String chatLog = internalChatFrame.getChatlog();
 assertEquals(text, chatLog);
 }

 @Test
 public void testAddStylesToDocument() {
 final int fontSize = 12;
 final Color color = new Color(255, 0, 0);
 final boolean isBold = true;
 final boolean isItalic = false;
 final String name = "TestUser";
 internalChatFrame.changeStyle(fontSize, color, isBold, isItalic, name);
 Style nameStyle = internalChatFrame.doc.getStyle(name);
 assertEquals(fontSize, StyleConstants.getFontSize(nameStyle));
 assertEquals(color, StyleConstants.getForeground(nameStyle));
 assertNull(StyleConstants.isBold(nameStyle));
 assertFalse(StyleConstants.isItalic(nameStyle));
 }

 @Test
 public void testAddUser() {
 final String user = "TestUser";
 internalChatFrame.addUser(user);
 assertEquals(1, internalChatFrame.listModel.getSize());
 assertEquals(user, internalChatFrame.listModel.getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 final String user = "TestUser";
 internalChatFrame.addUser(user);
 internalChatFrame.deleteUser(user);
 assertEquals(0, internalChatFrame.listModel.getSize());
 }

 @Test
 public void testGetUserlist() {
 final String[] userlist = new String[] { "User1", "User2", "User3" };
 internalChatFrame.listModel.addAll(Arrays.asList(userlist));
 assertEquals(3, internalChatFrame.listModel.getSize());
 String[] testUserlist = internalChatFrame.getUserlist();
 assertArrayEquals(userlist, testUserlist);
 }

 @Test
 public void testActionPerformed() {
 final String name = "TestUser";
 final String command = "chooseColor";
 internalChatFrame.list.setSelectedValue(name, true);
 SwingUtilities.invokeLater(() -> {
 internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame.list, 1, command));
 });
 assertNotNull(internalChatFrame.doc.getStyle(name));
 }

 @Test
 public void testMousePressed() {
 SwingUtilities.invokeLater(() -> {
 internalChatFrame.maybeShowPopup(new MouseEvent(internalChatFrame, 1, 1, 1, 1, 1, 1, false));
 });
 assertNotNull(internalChatFrame.listPop);
 assertNotNull(internalChatFrame.txtPop);
 }

}