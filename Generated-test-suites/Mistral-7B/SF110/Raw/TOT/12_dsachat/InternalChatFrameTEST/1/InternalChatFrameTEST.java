package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame mainFrame;

 @Before
 public void setUp() {
 mainFrame = new MainFrame();
 chatFrame = new InternalChatFrame(mainFrame);
 }

 @Test
 public void testInternalChatFrameConstruction() {
 // Test default constructor
 assertNotNull(chatFrame);
 // Test constructor with MainFrame argument
 assertNotNull(chatFrame.parent);
 }

 @Test
 public void testCreateGUI() {
 // Test that components are created
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 // Test that components are added to the frame
 assertEquals(2, chatFrame.getLayout().getComponentCount());
 }

 @Test
 public void testListSelection() {
 // Set up test listener
 ListSelectionListener listener = new ListSelectionListener() {
 @Override
 public void valueChanged(ListSelectionEvent e) {
 // Test that selection event is detected
 assertTrue(e.getSource() instanceof JList);
 }
 };
 // Add listener to the list
 chatFrame.list.addListSelectionListener(listener);
 // Test that selection changes the selected user
 String testUser = "Test User";
 chatFrame.addUser(testUser);
 chatFrame.list.setSelectedValue(testUser, true);
 assertEquals(testUser, chatFrame.list.getSelectedValue());
 // Test that selection event is detected when user is selected
 chatFrame.list.setSelectedIndex(0);
 listener.valueChanged(null);
 assertEquals(testUser, chatFrame.list.getSelectedValue());
 // Clean up
 chatFrame.list.removeListSelectionListener(listener);
 }

 @Test
 public void testAddText() {
 // Test that text is added to the text pane
 String testText = "Test Text";
 chatFrame.addText(testText);
 String actualText = chatFrame.showTxt.getText();
 assertEquals(testText, actualText);
 // Test that formatting is applied correctly
 String formattedText = "<html><body style='color:blue;font-size:16px;'><b>You </b>Test Text</body></html>";
 chatFrame.addText("You: Test Text");
 String expectedText = chatFrame.showTxt.getText();
 assertEquals(formattedText, expectedText);
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithBadLocationException() {
 String testText = "Test Text";
 chatFrame.showTxt.setDocument(new PlainDocument());
 chatFrame.addText(testText);
 }

 @Test
 public void testChangeStyle() {
 // Test that style is changed when called
 Color testColor = new Color(1.0f, 0.0f, 0.0f);
 chatFrame.changeStyle(12, testColor, false, false, "Test User");
 assertNotNull(chatFrame.doc.getStyle("Test User"));
 Style testStyle = chatFrame.doc.getStyle("Test User");
 assertEquals(testColor, StyleConstants.getForeground(testStyle));
 }

 @Test
 public void testAddUser() {
 String testUser = "Test User";
 chatFrame.addUser(testUser);
 assertEquals(1, chatFrame.listModel.size());
 assertEquals(testUser, chatFrame.listModel.get(0));
 }

 @Test
 public void testDeleteUser() {
 String testUser = "Test User";
 chatFrame.addUser(testUser);
 chatFrame.deleteUser(testUser);
 assertEquals(0, chatFrame.listModel.size());
 }

 @Test
 public void testGetUserlist() {
 String[] testUsers = {"Test User 1", "Test User 2"};
 for (String testUser : testUsers) {
 chatFrame.addUser(testUser);
 }
 String[] actualUsers = chatFrame.getUserlist();
 assertNotNull(actualUsers);
 assertEquals(testUsers.length, actualUsers.length);
 for (int i = 0; i < testUsers.length; i++) {
 assertEquals(testUsers[i], actualUsers[i]);
 }
 }

}