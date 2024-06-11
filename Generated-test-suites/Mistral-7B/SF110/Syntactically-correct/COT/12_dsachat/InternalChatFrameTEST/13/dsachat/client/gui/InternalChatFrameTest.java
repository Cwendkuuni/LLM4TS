package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame frame;
 private MainFrame parent;

 @Before
 public void setUp() throws Exception {
 parent = new MainFrame();
 frame = new InternalChatFrame(parent);
 SwingUtilities.invokeAndWait(() -> frame.setVisible(true));
 }

 @After
 public void tearDown() throws Exception {
 SwingUtilities.invokeAndWait(() -> frame.dispose());
 parent.dispose();
 }

 @Test
 public void testInternalChatFrameConstructor() {
 assertNotNull(frame);
 assertEquals("Chat", frame.getTitle());
 assertTrue(frame.isFocusable());
 }

 @Test
 public void testAddText() {
 String line = "You: Hello World";
 frame.addText(line);
 String chatLog = frame.getChatlog();
 assertTrue(chatLog.contains(line));
 }

 @Test
 public void testAddTextWithInvalidLineFormat() {
 String line = "Hello World";
 frame.addText(line);
 String chatLog = frame.getChatlog();
 assertFalse(chatLog.contains(line));
 }

 @Test
 public void testAddStylesToDocument() {
 final int fontSize = 12;
 final Color color = Color.RED;
 final boolean isBold = false;
 final boolean isItalic = false;
 final String name = "TestUser";
 frame.addUser(name);
 SwingUtilities.invokeAndWait(() -> frame.changeStyle(fontSize, color, isBold, isItalic, name));
 JList list = frame.getList();
 JList selectedItem = (JList) list.getSelectedValue();
 assertEquals(name, selectedItem.toString());
 }

 @Test
 public void testAddUser() {
 String user = "TestUser";
 frame.addUser(user);
 JList list = frame.getList();
 String[] expectedUserList = { user };
 assertArrayEquals(expectedUserList, frame.getUserlist());
 }

 @Test
 public void testDeleteUser() {
 String user = "TestUser";
 frame.addUser(user);
 SwingUtilities.invokeAndWait(() -> frame.deleteUser(user));
 String[] expectedUserList = {};
 assertArrayEquals(expectedUserList, frame.getUserlist());
 }

 @Test
 public void testGetUserlist() {
 String user1 = "User1";
 String user2 = "User2";
 frame.addUser(user1);
 frame.addUser(user2);
 String[] expectedUserList = { user1, user2 };
 assertArrayEquals(expectedUserList, frame.getUserlist());
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithBadLocationException() {
 String line = "You: Hello World:";
 frame.addText(line);
 fail("Expected BadLocationException");
 }

 @Test
 public void testChangeStyle() {
 final int fontSize = 12;
 final Color color = Color.RED;
 final boolean isBold = false;
 final boolean isItalic = false;
 final String name = "TestUser";
 SwingUtilities.invokeAndWait(() -> frame.changeStyle(fontSize, color, isBold, isItalic, name));
 JTextPane showTxt = frame.getShowTxt();
 assertEquals(color, showTxt.getBackground());
 }

 @Test
 public void testActionPerformed() {
 final String name = "TestUser";
 frame.addUser(name);
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "chooseColor");
 frame.actionPerformed(actionEvent);
 assertNotNull(frame.getListPop());
 }

 @Test
 public void testActionPerformedWithInvalidActionCommand() {
 final String name = "TestUser";
 frame.addUser(name);
 final ActionEvent actionEvent = new ActionEvent(new Object(), 0, "invalidCommand");
 frame.actionPerformed(actionEvent);
 assertNull(frame.getListPop());
 }

 @Test
 public void testMouseClicked() {
 final MouseEvent mouseEvent = new MouseEvent(new Object(), 0, 0, 0, 0, MouseEvent.BUTTON1, false);
 frame.maybeShowPopup(mouseEvent);
 assertNull(frame.getListPop());
 }

 @Test
 public void testMouseClickedWithPopupTrigger() {
 final MouseEvent mouseEvent = new MouseEvent(new Object(), 0, 0, MouseEvent.BUTTON1_DOWN_MASK, 0, 0, 0, false);
 frame.maybeShowPopup(mouseEvent);
 assertNotNull(frame.getListPop());
 }

 @Test
 public void testGetChatlog() {
 String line1 = "User1: Hello World";
 String line2 = "User2: Hi User1";
 SwingUtilities.invokeAndWait(() -> frame.addText(line1));
 SwingUtilities.invokeAndWait(() -> frame.addText(line2));
 String chatLog = frame.getChatlog();
 String expectedChatLog = line1 + "\n" + line2;
 assertEquals(expectedChatLog, chatLog);
 }

 @Test
 public void testChangeColor() {
 final Color newColor = JColorChooser.showDialog(frame, "Choose Color for user TestUser", Color.black);
 if (newColor != null) {
 SwingUtilities.invokeAndWait(() -> frame.changeStyle(12, newColor, false, false, "TestUser"));
 JList list = frame.getList();
 JList selectedItem = (JList) list.getSelectedValue();
 assertEquals("TestUser [" + newColor.getRed() + ", " + newColor.getGreen() + ", " + newColor.getBlue() + "]", selectedItem.toString());
 }
 }
}