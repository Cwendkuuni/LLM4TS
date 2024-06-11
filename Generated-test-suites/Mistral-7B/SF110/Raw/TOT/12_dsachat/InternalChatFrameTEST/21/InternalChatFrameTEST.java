package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.TableModelEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JFrame parentFrame;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 parentFrame = mock(JFrame.class);
 chatFrame = new InternalChatFrame(parentFrame);
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 }

 @After
 public void tearDown() {
 chatFrame.dispose();
 }

 @Test
 public void testCreateGUI() {
 // Test if the components are added to the JInternalFrame
 assertEquals("Chat", chatFrame.getTitle());
 assertEquals(2, chatFrame.getContentPane().getComponentCount());
 }

 @Test
 public void testAddText() {
 // Test adding regular text
 chatFrame.addText("User1: Hello");
 String expectedText = "User1: Hello\n";
 assertEquals(expectedText, showTxt.getText());

 // Test adding italic text
 chatFrame.addText("*User2: This is italic*");
 expectedText += "*User2: This is italic*\n";
 assertEquals(expectedText, showTxt.getText());

 // Test adding bold text
 chatFrame.addText("**User3: This is bold**");
 expectedText += "**User3: This is bold**\n";
 assertEquals(expectedText, showTxt.getText());

 // Test adding bold and italic text
 chatFrame.addText("**User4: This is bold and italic**");
 expectedText += "**User4: This is bold and italic*\n";
 assertEquals(expectedText, showTxt.getText());

 // Test adding text with different styles
 chatFrame.addText("*User5: This is italic and bold*");
 expectedText += "*User5: This is italic and bold*\n";
 assertEquals(expectedText, showTxt.getText());
 }

 @Test
 public void testAddUser() {
 // Test adding a user to the JList
 chatFrame.addUser("User1");
 assertEquals(1, list.getModel().getSize());
 assertEquals("User1", list.getSelectedValue());

 // Test adding multiple users to the JList
 chatFrame.addUser("User2");
 chatFrame.addUser("User3");
 assertEquals(3, list.getModel().getSize());
 }

 @Test
 public void testDeleteUser() {
 // Test deleting a user from the JList
 chatFrame.addUser("User1");
 chatFrame.deleteUser("User1");
 assertEquals(0, list.getModel().getSize());
 }

 @Test
 public void testGetUserlist() {
 // Test getting the user list from the JInternalFrame
 String[] expectedUsers = {"User1", "User2", "User3"};
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.addUser("User3");
 assertArrayEquals(expectedUsers, chatFrame.getUserlist());
 }

 @Test
 public void testActionPerformedWhenChooseColor() {
 // Test choosing a color and changing the style of a user
 Color newColor = Color.RED;
 when(parentFrame.getColorChooserDialog(anyString(), anyBoolean())).thenReturn(JColorChooser.showDialog(any(), anyString(), newColor));
 chatFrame.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
 verify(parentFrame).setBackground(newColor);
 }

 @Test
 public void testActionPerformedWhenBgColor() {
 // Test choosing a background color for the JTextPane
 when(parentFrame.getColorChooserDialog(anyString(), anyBoolean())).thenReturn(JColorChooser.showDialog(any(), anyString(), Color.GREEN));
 chatFrame.actionPerformed(new ActionEvent(showTxt, 0, "bgColor"));
 assertEquals(Color.GREEN, showTxt.getBackground());
 }

 @Test
 public void testActionPerformedWhenWhisper() {
 // Test whispering a user
 String user = "User1";
 chatFrame.addUser(user);
 showTxt.setText(null);
 chatFrame.actionPerformed(new ActionEvent(txtInput, 0, "sendInput"));
 assertEquals("", chatFrame.getChatlog());
 chatFrame.txtInput.setText("/w " + user + " Test whisper");
 chatFrame.actionPerformed(new ActionEvent(txtInput, 0, "sendInput"));
 assertEquals("/w User1 Test whisper", chatFrame.getChatlog());
 }

 @Test
 public void testMouseClickedOnList() {
 // Test showing the popup menu when clicking on a user in the JList
 MouseEvent mouseEvent = mock(MouseEvent.class);
 when(mouseEvent.getSource()).thenReturn(list);
 chatFrame.maybeShowPopup(mouseEvent);
 verify(listPop).isVisible();
 }

 @Test
 public void testMouseClickedOnShowTxt() {
 // Test showing the popup menu when clicking on the JTextPane
 MouseEvent mouseEvent = mock(MouseEvent.class);
 when(mouseEvent.getSource()).thenReturn(showTxt);
 chatFrame.maybeShowPopup(mouseEvent);
 verify(txtPop).isVisible();
 }
}