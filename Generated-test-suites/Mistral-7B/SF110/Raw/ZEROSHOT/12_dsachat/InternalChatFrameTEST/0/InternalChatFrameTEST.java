package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private MainFrame parent;
 private InternalChatFrame chatFrame;

 @Before
 public void setUp() {
 parent = new MainFrame();
 chatFrame = new InternalChatFrame(parent);
 }

 @Test
 public void testCreateGUI() {
 // Check if the JList is created
 assertNotNull(chatFrame.list);
 // Check if the JTextPane is created
 assertNotNull(chatFrame.showTxt);
 // Check if the JTextField is created
 assertNotNull(chatFrame.txtInput);
 // Check if the list model is created
 assertNotNull(chatFrame.listModel);
 // Check if the parent reference is set
 assertEquals(parent, chatFrame.parent);
 // Check if the doc and listPop are null initially
 assertNull(chatFrame.doc);
 assertNull(chatFrame.listPop);
 // Check if the txtPop is null initially
 assertNull(chatFrame.txtPop);
 }

 @Test
 public void testAddText() {
 String line = "Test:Hello";
 chatFrame.addText(line);
 // Check if the line is added to the text pane
 assertEquals(line, chatFrame.showTxt.getText().substring(0, chatFrame.showTxt.getCaretPosition()));
 // Check if the line style is set to "system"
 StyleContext context = new StyleContext();
 Style attr = context.getStyle(StyleContext.DEFAULT_STYLE);
 Style namedStyle = context.getStyle("system");
 assertEquals(namedStyle, chatFrame.showTxt.getStyle(line));
 }

 @Test
 public void testAddStylesToDocument() {
 // Check if the regular style is added
 Style regular = chatFrame.doc.getStyle("regular");
 assertNotNull(regular);
 // Check if the "name" style is added
 Style name = chatFrame.doc.getStyle("name");
 assertNotNull(name);
 // Check if the font size, color and bold/italic properties are set correctly
 assertEquals(14, ((Number)StyleConstants.getFontSize(name)).intValue());
 assertEquals(Color.BLUE, StyleConstants.getForeground(name));
 assertFalse(StyleConstants.isBold(name));
 assertFalse(StyleConstants.isItalic(name));
 }

 @Test
 public void testAddUser() {
 String user = "TestUser";
 chatFrame.addUser(user);
 // Check if the user is added to the list
 assertEquals(user, chatFrame.listModel.get(0));
 // Check if the user style is created
 Style userStyle = chatFrame.doc.getStyle(user);
 assertNotNull(userStyle);
 }

 @Test
 public void testDeleteUser() {
 String user = "TestUser";
 chatFrame.addUser(user);
 chatFrame.deleteUser(user);
 // Check if the user is removed from the list model
 assertEquals(0, chatFrame.listModel.size());
 // Check if the user style is removed
 assertNull(chatFrame.doc.getStyle(user));
 }

 @Test
 public void testGetUserlist() {
 String user1 = "TestUser1";
 String user2 = "TestUser2";
 chatFrame.addUser(user1);
 chatFrame.addUser(user2);
 List<String> userList = chatFrame.getUserlist();
 // Check if the user list contains the added users
 assertEquals(2, userList.size());
 assertEquals(user1, userList.get(0));
 assertEquals(user2, userList.get(1));
 }

 @Test
 public void testChangeStyle() {
 int fontSize = 12;
 Color color = new Color(255, 0, 0);
 boolean isBold = true;
 boolean isItalic = false;
 String name = "TestStyle";
 chatFrame.changeStyle(fontSize, color, isBold, isItalic, name);
 // Check if the style is created and set correctly
 Style style = chatFrame.doc.getStyle(name);
 assertEquals(fontSize, ((Number)StyleConstants.getFontSize(style)).intValue());
 assertEquals(color, StyleConstants.getForeground(style));
 assertEquals(isBold, StyleConstants.isBold(style));
 assertEquals(isItalic, StyleConstants.isItalic(style));
 }

 @Test
 public void testActionPerformed() {
 String name = "TestUser";
 chatFrame.addUser(name);
 chatFrame.show();
 chatFrame.txtInput.setText("TestMessage");
 chatFrame.txtInput.doClick();
 chatFrame.txtInput.dispatchEvent(new ActionEvent(chatFrame.txtInput, 0, "sendInput"));
 // Check if the text is added to the text pane
 assertEquals("TestMessage\n", chatFrame.showTxt.getText().substring(0, chatFrame.showTxt.getCaretPosition()));
 // Check if the user style is updated
 Style userStyle = chatFrame.doc.getStyle(name);
 assertTrue(StyleConstants.isBold(userStyle));
 }

 @Test
 public void testMouseClicked() {
 chatFrame.show();
 MouseEvent mouseEvent = new MouseEvent(chatFrame, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 10, 10, 1, false);
 chatFrame.maybeShowPopup(mouseEvent);
 // Check if the list popup menu is shown
 assertTrue(chatFrame.listPop.isVisible());
 }
}