package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

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
 SwingUtilities.invokeLater(() -> chatFrame.setVisible(true));
 }

 @Test
 public void testCreateGUI() {
 // Check if the JList is created and visible
 assertTrue(chatFrame.list != null);
 assertTrue(chatFrame.list.isVisible());

 // Check if the JTextPane is created and visible
 assertTrue(chatFrame.showTxt != null);
 assertTrue(chatFrame.showTxt.isVisible());

 // Check if the JTextField is created and added to the JPanel
 assertTrue(chatFrame.txtInput != null);
 assertTrue(chatFrame.right.getComponentCount() > 0);
 assertTrue(chatFrame.right.getComponent(1) instanceof JTextField);
 }

 @Test
 public void testAddText() {
 // Set up some test data
 String textToAdd = "You: Hello, world!";

 // Add the test data to the chat frame
 chatFrame.addText(textToAdd);

 // Check if the text was added to the JTextPane
 String chatLog = chatFrame.getChatlog();
 assertTrue(chatLog.contains(textToAdd));
 }

 @Test
 public void testAddStylesToDocument() {
 // Set up some test data
 StyledDocument doc = chatFrame.doc;

 // Check if the default style is set correctly
 assertEquals("default", doc.getName(0));

 // Check if the "regular" style is set correctly
 assertEquals("regular", doc.getName(1));
 Style regular = doc.getStyle("regular");
 assertEquals(Color.BLACK, regular.getForeground());
 assertEquals(14, regular.getFontSize());

 // Check if the "italic" style is set correctly
 assertEquals("italic", doc.getName(2));
 Style italic = doc.getStyle("italic");
 assertTrue(italic.isItalic());

 // Check if the "bold" style is set correctly
 assertEquals("bold", doc.getName(3));
 Style bold = doc.getStyle("bold");
 assertTrue(bold.isBold());

 // Check if the "bold-italic" style is set correctly
 assertEquals("bold-italic", doc.getName(4));
 Style boldItalic = doc.getStyle("bold-italic");
 assertTrue(boldItalic.isBold());
 assertTrue(boldItalic.isItalic());
 }

 @Test
 public void testChangeStyle() {
 // Set up some test data
 String name = "Test User";
 int fontSize = 12;
 Color color = Color.RED;
 boolean isBold = true;
 boolean isItalic = false;

 // Change the style for the given user
 chatFrame.changeStyle(fontSize, color, isBold, isItalic, name);

 // Check if the style was changed correctly
 Style style = chatFrame.doc.getStyle(name);
 assertEquals(fontSize, style.getFontSize());
 assertEquals(color, style.getForeground());
 assertEquals(isBold, style.isBold());
 assertEquals(isItalic, style.isItalic());
 }

 @Test
 public void testAddUser() {
 // Set up some test data
 String user = "Test User";

 // Add the user to the chat frame
 chatFrame.addUser(user);

 // Check if the user was added to the JList
 assertEquals(user, chatFrame.listModel.getElementAt(0));

 // Check if the user was added to the StyledDocument
 assertEquals(user, chatFrame.doc.getName(chatFrame.doc.getLength()));
 }

 @Test
 public void testDeleteUser() {
 // Set up some test data
 String user = "Test User";
 chatFrame.listModel.addElement(user);
 chatFrame.addText(user + ": Test message");

 // Delete the user from the chat frame
 chatFrame.deleteUser(user);

 // Check if the user was removed from the JList
 assertFalse(chatFrame.listModel.contains(user));

 // Check if the user was removed from the StyledDocument
 assertFalse(chatFrame.doc.getStyleNames().contains(user));
 }

 @Test
 public void testGetUserlist() {
 // Set up some test data
 String[] userList = {"User 1", "User 2", "User 3"};
 for (String user : userList) {
 chatFrame.addUser(user);
 }

 // Get the user list from the chat frame
 String[] actualUserList = chatFrame.getUserlist();
 assertArrayEquals(userList, actualUserList);
 }

 @Test
 public void testActionPerformed() {
 // Set up some test data
 String name = "Test User";
 String actionCommand = "chooseColor";
 Color newColor = new Color(255, 0, 0);

 // Simulate a mouse click on the JList to show the context menu
 MouseAdapter mouseAdapter = new MouseAdapter() {
 @Override
 public void mouseClicked(MouseEvent e) {
 chatFrame.maybeShowPopup(e);
 }
 };
 chatFrame.list.addMouseListener(mouseAdapter);
 chatFrame.list.requestFocus();
 MouseEvent mouseEvent = new MouseEvent(chatFrame.list, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 1, false);
 SwingUtilities.invokeLater(() -> chatFrame.mousePressed(mouseEvent));
 SwingUtilities.invokeLater(() -> chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, actionCommand)));
 SwingUtilities.invokeLater(() -> chatFrame.list.removeMouseListener(mouseAdapter));

 // Check if the color was changed for the given user
 Style style = chatFrame.doc.getStyle(name);
 assertEquals(newColor, style.getForeground());
 }

 @Test
 public void testMouseClicked() {
 // Set up some test data
 // This test is empty, as the method does not do anything in this class
 }

 @Test
 public void testMouseEntered() {
 // Set up some test data
 // This test is empty, as the method does not do anything in this class
 }

 @Test
 public void testMouseExited() {
 // Set up some test data
 // This test is empty, as the method does not do anything in this class
 }

 @Test
 public void testMousePressed() {
 // Set up some test data
 // This test is empty, as the method does not do anything in this class
 }

 @Test
 public void testMouseReleased() {
 // Set up some test data
 // This test is empty, as the method does not do anything in this class
 }
}