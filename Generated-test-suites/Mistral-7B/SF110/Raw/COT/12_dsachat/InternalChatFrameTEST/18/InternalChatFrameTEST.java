package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.*;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = new MainFrame();
 chatFrame = new InternalChatFrame(parent);
 }

 @Test
 public void testInternalChatFrameConstructor() {
 assertEquals("Chat", chatFrame.getTitle());
 assertTrue(chatFrame.isResizable());
 assertTrue(chatFrame.isFocusable());
 assertTrue(chatFrame.isVisible());
 }

 @Test
 public void testCreateGUI() {
 JList list = chatFrame.getList();
 JTextPane showTxt = chatFrame.getShowTxt();
 JTextField txtInput = chatFrame.getTxtInput();
 assertEquals(0, list.getSelectedIndex());
 assertNotNull(list);
 assertNotNull(showTxt);
 assertNotNull(txtInput);
 }

 @Test
 public void testAddText() throws BadLocationException {
 String line = "test:test";
 chatFrame.addText(line);
 String expectedText = "test:test\n";
 StyledDocument doc = chatFrame.getShowTxt().getStyledDocument();
 String actualText = doc.getText(0, doc.getLength()).replaceAll("\r\n", "\n");
 assertEquals(expectedText, actualText);
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = chatFrame.getShowTxt().getStyledDocument();
 List<String> expectedStyles = Arrays.asList("regular", "italic", "bold", "bold-italic", "name", "system", "small", "large");
 List<String> actualStyles = Arrays.asList(doc.getStyleNames());
 assertEquals(expectedStyles, actualStyles);
 }

 @Test
 public void testAddUser() {
 String user = "testUser";
 chatFrame.addUser(user);
 assertEquals(1, chatFrame.getList().getModel().getSize());
 assertEquals(user, chatFrame.getList().getModel().getElementAt(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUserWithNullUser() {
 chatFrame.addUser(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testDeleteUserWithEmptyUserlist() {
 chatFrame.deleteUser(null);
 }

 @Test
 public void testDeleteUser() {
 String user = "testUser";
 chatFrame.addUser(user);
 chatFrame.deleteUser(user);
 assertEquals(0, chatFrame.getList().getModel().getSize());
 }

 @Test
 public void testGetUserlist() {
 String user1 = "user1";
 String user2 = "user2";
 chatFrame.addUser(user1);
 chatFrame.addUser(user2);
 String[] expectedUserlist = {user1, user2};
 String[] actualUserlist = chatFrame.getUserlist();
 assertArrayEquals(expectedUserlist, actualUserlist);
 }

 @Test
 public void testActionPerformedWithChooseColor() {
 Color newColor = Color.RED;
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 1, "chooseColor"));
 JColorChooser chooser = new JColorChooser();
 Color newColorSelected = chooser.showDialog(null, "Choose a color", Color.WHITE);
 if (newColorSelected != null) {
 chatFrame.changeStyle(12, newColorSelected, false, false, "testUser");
 StyledDocument doc = chatFrame.getShowTxt().getStyledDocument();
 Style s = doc.getStyle("testUser");
 assertEquals("testUser", s.getName());
 assertEquals(newColorSelected, s.getBackground());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testActionPerformedWithNullEvent() {
 chatFrame.actionPerformed(null);
 }

 @Test
 public void testMouseClicked() {
 chatFrame.maybeShowPopup(new MouseEvent(new JFrame(), 0, 0, 0, 0, 0, 0, false));
 assertFalse(chatFrame.isPopupMenuLightWeight());
 }

 @Test
 public void testGetChatlog() {
 String testText = "testText";chatFrame.addText(testText);
 assertEquals(testText, chatFrame.getChatlog());
 }
}