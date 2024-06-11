package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.*;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;

 @Before
 public void setUp() {
 parent = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parent);
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @Test
 public void testCreateGUI() {
 // Testing if the chat frame is created properly
 assertTrue("Chat frame is not created", chatFrame.isVisible());
 assertEquals("Name of the frame is not correct", "Chat", chatFrame.getTitle());
 // Testing the JSplitPane
 JComponent leftComponent = (JComponent) chatFrame.getContentPane().getComponent(0);
 JComponent rightComponent = (JComponent) chatFrame.getContentPane().getComponent(1);
 JSplitPane splitPane = (JSplitPane) leftComponent;
 assertEquals("Number of components in the chat frame is not correct", 2, chatFrame.getContentPane().getComponentCount());
 assertTrue("Left component is not a JList", list instanceof JList);
 assertTrue("Right component is not a JPanel", rightComponent instanceof JPanel);
 // Testing the JList
 assertThat(list.getModel(), instanceOf(DefaultListModel.class));
 // Testing the JTextPane
 assertTrue("Text pane is not created", showTxt != null);
 assertEquals("Text pane is editable", false, showTxt.isEditable());
 // Testing the JTextField
 assertTrue("TextField is not created", txtInput != null);
 }

 @Test
 public void testAddText() {
 // Testing if addText method adds text correctly
 String line = "User1: Hello, World!";
 chatFrame.addText(line);
 String chatlog = showTxt.getText();
 assertTrue("Text is not added to the text pane", chatlog.contains(line));

 // Testing if addText method handles invalid text
 String invalidLine = "Invalid text";
 try {
 chatFrame.addText(invalidLine);
 fail("Invalid text should throw an exception");
 } catch (IllegalArgumentException e) {
 // pass
 }

 // Testing if addText method handles text formatting
 String formattedLine = "*User1*:**bold* Hello, *italic*World!*reset*!";
 chatFrame.addText(formattedLine);
 String formattedChatlog = showTxt.getText();
 assertTrue("Text formatting is not applied", formattedChatlog.contains("*User1*"));
 assertTrue("Text formatting is not removed after inserting normal text", formattedChatlog.contains("\n"));
 }

 @Test
 public void testAddUser() {
 // Testing if addUser method adds user to the list correctly
 String user = "User1";
 chatFrame.addUser(user);
 assertEquals("User is not added to the list", user, (String) list.getModel().getElementAt(0));

 // Testing if addUser method handles duplicate users
 chatFrame.addUser(user);
 assertEquals("User is added again to the list", user, (String) list.getModel().getElementAt(1));
 }

 @Test
 public void testDeleteUser() {
 // Testing if deleteUser method removes user from the list correctly
 String user = "User1";
 chatFrame.addUser(user);
 chatFrame.deleteUser(user);
 assertTrue("User is still in the list", list.getModel().contains(user) == false);
 }

 @Test
 public void testGetUserlist() {
 // Testing if getUserlist method returns the correct list of users
 String[] userlist = chatFrame.getUserlist();
 assertEquals("Number of users in the list is not correct", 0, userlist.length);
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 assertEquals("Number of users in the list is not correct", 2, userlist.length);
 assertEquals("First user is not correct", "User1", userlist[0]);
 assertEquals("Second user is not correct", "User2", userlist[1]);
 }

 @Test
 public void testActionPerformed() {
 // Testing if actionPerformed method handles different events correctly
 String name = "User1";
 String command = "chooseColor";
 chatFrame.listPop.setVisible(false); // Hide the popup menu to prevent interference
 list.setSelectedValue(name);
 txtInput.setText("testText");
 txtInput.dispatchEvent(new ActionEvent(txtInput, ActionEvent.ACTION_PERFORMED, "sendInput")); // Simulate user input
 verify(parent, times(1)).processCommand("sendInput", eq(chatFrame));
 chatFrame.listPop.setVisible(true); // Show the popup menu
 list.addMouseListener(chatFrame); // Register chatFrame as listener to the JList
 list.addMouseListener(mock(MouseListener.class)); // Register another listener to prevent interference
 list.fireEventMouseClicked(new MouseEvent(list, 1, 1, 1, 1, 1, 1, true)); // Simulate mouse click on the JList
 verify(chatFrame, times(1)).actionPerformed(any(ActionEvent.class));

 name = "bgColor";
 txtInput.setText("testText");
 txtInput.dispatchEvent(new ActionEvent(txtInput, ActionEvent.ACTION_PERFORMED, "bgColor")); // Simulate user input
 verifyZeroInteractions(parent); // Nothing should be called on the parent
 }
}