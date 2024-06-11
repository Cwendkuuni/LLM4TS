package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JFrame parent;

 @Before
 public void setUp() {
 parent = new JFrame();
 chatFrame = new InternalChatFrame(parent);
 parent.add(chatFrame);
 parent.pack();
 parent.setVisible(true);
 }

 @After
 public void tearDown() {
 parent.dispose();
 }

 @Test
 public void testInternalChatFrameConstruction() {
 // Check if the InternalChatFrame is created correctly
 assertNotNull(chatFrame);
 assertEquals("Chat", chatFrame.getTitle());
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 assertNotNull(chatFrame.listModel);
 assertNotNull(chatFrame.parent);
 assertNotNull(chatFrame.doc);
 assertNotNull(chatFrame.listPop);
 assertNotNull(chatFrame.txtPop);
 }

 @Test
 public void testCreateGUI() {
 // Check if the createGUI method is called and the UI is created correctly
 chatFrame.createGUI();
 assertEquals(2, chatFrame.getComponentCount());
 JList list = (JList) chatFrame.getComponent(0);
 JTextPane showTxt = (JTextPane) chatFrame.getComponent(1);
 assertNotNull(list);
 assertNotNull(showTxt);
 }

 @Test
 public void testAddText() {
 // Test adding text to the text pane with regular formatting
 chatFrame.addText("User: Hello World");
 String expected = "User: Hello World\n";
 String actual = chatFrame.showTxt.getText();
 assertEquals(expected, actual);

 // Test adding text to the text pane with italic formatting
 chatFrame.addText("You: *Hi there*");
 expected = "You: Hi there\n";
 actual = chatFrame.showTxt.getText();
 assertEquals(expected, actual);

 // Test adding text to the text pane with bold formatting
 chatFrame.addText("*User*: **Hi**");
 expected = "*User*: Hi\n";
 actual = chatFrame.showTxt.getText();
 assertEquals(expected, actual);

 // Test adding text to the text pane with bold and italic formatting
 chatFrame.addText("*User*: **Hi** *there*");
 expected = "*User*: Hi there\n";
 actual = chatFrame.showTxt.getText();
 assertEquals(expected, actual);

 // Test adding text to the text pane with different formatting combinations
 chatFrame.addText("*You*: **Hi** *there* *and* **here**");
 expected = "*You*: Hi there and here\n";
 actual = chatFrame.showTxt.getText();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddStylesToDocument() {
 // Test adding styles to the document
 chatFrame.addStylesToDocument(chatFrame.doc);
 assertEquals(13, chatFrame.doc.getLogicalStyleCount());
 }

 @Test
 public void testChangeStyle() {
 // Test changing the style of a selected user
 chatFrame.changeStyle(14, Color.RED, true, true, "User");
 ListModel listModel = chatFrame.listModel;
 String expected = "User: ";
 String actual = (String) listModel.getElementAt(0);
 assertEquals(expected, actual);
 // Check if the text pane background color is changed
 JTextPane showTxt = chatFrame.showTxt;
 Color oldBackground = showTxt.getBackground();
 Color newBackground = Color.RED;
 chatFrame.changeStyle(0, newBackground, false, false, "User");
 assertTrue(newBackground.equals(showTxt.getBackground()));
 assertFalse(oldBackground.equals(showTxt.getBackground()));
 }

 @Test
 public void testAddUser() {
 // Test adding a user to the list and check if it's displayed correctly
 chatFrame.addUser("User");
 ListModel listModel = chatFrame.listModel;
 String expected = "User";
 String actual = (String) listModel.getElementAt(0);
 assertEquals(expected, actual);
 // Check if the selected user's name is set correctly
 String selectedUser = (String) chatFrame.list.getSelectedValue();
 assertEquals("User", selectedUser);
 }

 @Test
 public void testDeleteUser() {
 // Test deleting a user from the list and check if it's removed correctly
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.deleteUser("User1");
 ListModel listModel = chatFrame.listModel;
 int expectedSize = 1;
 int actualSize = listModel.getSize();
 assertEquals(expectedSize, actualSize);
 String expectedUser = "User2";
 String actualUser = (String) listModel.getElementAt(0);
 assertEquals(expectedUser, actualUser);
 }

 @Test
 public void testGetUserlist() {
 // Test getting the user list and check if it contains all users correctly
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 String[] expectedUsers = {"User1", "User2"};
 String[] actualUsers = chatFrame.getUserlist();
 assertArrayEquals(expectedUsers, actualUsers);
 }

 @Test
 public void testActionPerformed() {
 // Test the actionPerformed method with different commands
 chatFrame.addText("User: Hello World");
 chatFrame.txtInput.setText("Whisper User: Hi");
 chatFrame.txtInput.doClick();
 chatFrame.txtInput.sendKeyEvent(new KeyEvent(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedComponent(), 1, 1, 0));
 chatFrame.actionPerformed(new ActionEvent("", 0, "whisper"));
 // Check if the text pane contains the expected text
 String expected = "User: Hello World\nWhisper User: Hi\n";
 String actual = chatFrame.showTxt.getText();
 assertEquals(expected, actual);
 }

 @Test
 public void testMouseClicked() {
 // Test the mouseClicked method with different sources
 chatFrame.createGUI();
 JList list = (JList) chatFrame.getComponent(0);
 JTextPane showTxt = (JTextPane) chatFrame.getComponent(1);
 chatFrame.mouseClicked(new MouseEvent(list, 1, 0, 0, 0, 0, 0, false));
 assertFalse(chatFrame.listPop.isVisible());
 chatFrame.mouseClicked(new MouseEvent(showTxt, 1, 0, 0, 0, 0, 0, false));
 assertFalse(chatFrame.txtPop.isVisible());
 }

}