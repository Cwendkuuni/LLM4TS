package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parentFrame;

 @Before
 public void setup() {
 parentFrame = new MainFrame();
 chatFrame = new InternalChatFrame(parentFrame);
 }

 @Test
 public void testInternalChatFrameConstructor() {
 // Check that the InternalChatFrame is created without exceptions
 assertNull(chatFrame.getException());
 }

 @Test
 public void testCreateGUI() {
 // Check that the components are added to the frame
 assertEquals(2, chatFrame.getContentPane().getComponentCount());
 }

 @Test
 public void testAddText() {
 // Test adding a simple text message
 chatFrame.addText("Test message: Test");
 String expectedText = "Test: Test\n";
 String actualText = chatFrame.getChatlog();
 assertEquals(expectedText, actualText);

 // Test adding a message with formatting
 chatFrame.addText("*Test message:* Test");
 expectedText = "*Test message:* Test\n";
 actualText = chatFrame.getChatlog();
 assertEquals(expectedText, actualText);
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithException() {
 // Test adding a message with an exception
 chatFrame.addText("Test message:\nTest");
 fail("Expected BadLocationException but no exception was thrown.");
 }

 @Test
 public void testAddUser() {
 // Test adding a user to the user list
 chatFrame.addUser("User1");
 String[] expectedUserlist = {"User1"};
 String[] actualUserlist = chatFrame.getUserlist();
 assertArrayEquals(expectedUserlist, actualUserlist);
 }

 @Test
 public void testDeleteUser() {
 // Test deleting a user from the user list
 chatFrame.addUser("User1");
 chatFrame.deleteUser("User1");
 String[] expectedUserlist = {};
 String[] actualUserlist = chatFrame.getUserlist();
 assertArrayEquals(expectedUserlist, actualUserlist);
 }

 @Test
 public void testGetChatlog() {
 // Test getting the chat log from the text pane
 String expectedText = "";
 String actualText = chatFrame.getChatlog();
 assertEquals(expectedText, actualText);

 // Test adding messages to the chat log and getting them
 chatFrame.addText("Test message: Test");
 expectedText = "Test: Test\n";
 actualText = chatFrame.getChatlog();
 assertEquals(expectedText, actualText);
 }

 @Test
 public void testMouseEventInList() {
 // Test showing the list popup menu when clicking on a list item
 MouseEvent mouseEvent = new MouseEvent(chatFrame.getList(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false);
 chatFrame.maybeShowPopup(mouseEvent);
 assertTrue(chatFrame.listPop.isVisible());
 }

 @Test
 public void testMouseEventInTextPane() {
 // Test showing the text popup menu when clicking on the text pane
 mouseEvent = new MouseEvent(chatFrame.getShowTxt(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false);
 chatFrame.maybeShowPopup(mouseEvent);
 assertTrue(chatFrame.txtPop.isVisible());
 }
}