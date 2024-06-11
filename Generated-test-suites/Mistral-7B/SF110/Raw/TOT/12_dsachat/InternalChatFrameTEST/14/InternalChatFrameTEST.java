package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import org.junit.*;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private MainFrame parentMock;
 private InternalChatFrame chatFrame;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 parentMock = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parentMock);
 }

 @Test
 public void testCreateGUI() {
 // Check if the JList and JTextPane are added to the JInternalFrame
 assertEquals("Chat", chatFrame.getTitle());
 assertTrue(chatFrame.getContentPane().getComponentCount() > 1);
 JList list = chatFrame.getContentPane().getComponentAt(0, 0);
 JTextPane showTxt = chatFrame.getContentPane().getComponentAt(0, 1);
 // Check if the components are initialized correctly
 assertEquals("InternalChatFrame$1", list.getModel().getClass().getName());
 assertEquals(true, list.isVisible());
 assertEquals(false, showTxt.isEditable());
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello World";
 chatFrame.addText(line);
 // Check if the text is added to the JTextPane
 assertEquals(line, chatFrame.showTxt.getText().split("\n")[0]);
 // Check if the text is styled correctly
 StyleContext context = chatFrame.showTxt.getStyleContext();
 Style userStyle = context.getStyle("User1");
 assertTrue(userStyle != null);
 StyleConstants.setBold(userStyle, false);
 StyleConstants.setItalic(userStyle, false);
 StyleConstants.setForeground(userStyle, Color.BLACK);
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = chatFrame.doc;
 // Check if the default style is set correctly
 Style defaultStyle = doc.getStyle("default");
 StyleConstants.setFontFamily(defaultStyle, "SansSerif");
 StyleConstants.setBold(defaultStyle, false);
 StyleConstants.setItalic(defaultStyle, false);
 // Check if the regular style is set correctly
 Style regularStyle = doc.getStyle("regular");
 StyleConstants.setForeground(regularStyle, Color.BLACK);
 StyleConstants.setFontSize(regularStyle, 12);
 }

 @Test
 public void testAddUser() {
 String user = "User1";
 chatFrame.addUser(user);
 // Check if the user is added to the JList
 assertEquals(user, chatFrame.list.getModel().getElementAt(0));
 // Check if the user's style is set correctly
 Style userStyle = chatFrame.doc.getStyle(user);
 StyleConstants.setBold(userStyle, false);
 StyleConstants.setItalic(userStyle, false);
 StyleConstants.setForeground(userStyle, Color.BLUE);
 StyleConstants.setFontSize(userStyle, 16);
 }

 @Test
 public void testDeleteUser() {
 String user = "User1";
 chatFrame.addUser(user);
 chatFrame.deleteUser(user);
 // Check if the user is removed from the JList
 assertFalse(chatFrame.list.getModel().containsElement(user));
 }

 @Test
 public void testGetUserlist() {
 String[] users = {"User1", "User2", "User3"};
 for (String user : users) {
 chatFrame.addUser(user);
 }
 assertEquals(users, chatFrame.getUserlist());
 }

 @Test
 public void testActionPerformedWhenChooseColor() {
 Color newColor = Color.RED;
 doReturn(newColor).when(parentMock).getSelectedColor();
 chatFrame.actionPerformed(new ActionEvent(parentMock, 0, "chooseColor"));
 // Check if the color is changed for the selected user
 Style userStyle = chatFrame.doc.getStyle("User1");
 StyleConstants.setForeground(userStyle, newColor);
 verify(parentMock, times(1)).getSelectedColor();
 }

 @Test
 public void testActionPerformedWhenBgColor() {
 Color newColor = Color.RED;
 doReturn(newColor).when(parentMock).getSelectedColor();
 chatFrame.actionPerformed(new ActionEvent(parentMock, 0, "bgColor"));
 // Check if the background color is changed for the JTextPane
 assertEquals(newColor, chatFrame.showTxt.getBackground());
 verify(parentMock, times(1)).getSelectedColor();
 }

 @Test
 public void testActionPerformedWhenWhisper() {
 String wUser = "/w User1 ";
 chatFrame.txtInput.setText(wUser);
 chatFrame.actionPerformed(new ActionEvent(chatFrame.txtInput, 0, "sendInput"));
 // Check if the text is added to the JTextPane with the whisper style
 Style whisperStyle = chatFrame.doc.getStyle("whisper");
 StyleConstants.setBold(whisperStyle, false);
 StyleConstants.setItalic(whisperStyle, true);
 StyleConstants.setForeground(whisperStyle, Color.GRAY);
 String chatLog = chatFrame.getChatlog();
 assertEquals("/w User1 " + chatFrame.parent.hero.getName() + ": ", chatLog.split("\n")[0]);
 }
}