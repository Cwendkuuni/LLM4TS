package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 parent = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parent);
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @Test
 public void testAddText_validInput() {
 String line = "User1: Hello World";
 chatFrame.addText(line);
 assertEquals("User1\nHello World\n", showTxt.getText());
 }

 @Test
 public void testAddText_invalidInput() {
 String line = "InvalidInput";
 chatFrame.addText(line);
 assertEquals("", showTxt.getText());
 }

 @Test
 public void testAddText_emptyLine() {
 String line = "";
 chatFrame.addText(line);
 assertEquals("", showTxt.getText());
 }

 @Test
 public void testAddText_multipleLines() {
 String line1 = "User1: Hello World";
 String line2 = "User2: Hi User1";
 chatFrame.addText(line1);
 chatFrame.addText(line2);
 assertEquals("User1\nHello World\nUser2\nHi User1\n", showTxt.getText());
 }

 @Test
 public void testChangeStyle_validInput() {
 final int fontSize = 12;
 final Color color = Color.RED;
 final boolean isBold = false;
 final boolean isItalic = false;
 final String name = "User1";
 chatFrame.changeStyle(fontSize, color, isBold, isItalic, name);
 Style style = chatFrame.doc.getStyle(name);
 assertEquals(color, style.getForeground());
 assertEquals(fontSize, (int) style.getFontSize());
 assertFalse(isBold);
 assertFalse(isItalic);
 }

 @Test
 public void testChangeStyle_invalidInput() {
 final int fontSize = -5;
 final Color color = null;
 final boolean isBold = true;
 final boolean isItalic = false;
 final String name = "User1";
 chatFrame.changeStyle(fontSize, color, isBold, isItalic, name);
 assertNull(chatFrame.doc.getStyle(name));
 }

 @Test
 public void testAddUser_validInput() {
 String user = "User1";
 chatFrame.addUser(user);
 assertEquals(user, list.getModel().getElementAt(0));
 }

 @Test
 public void testDeleteUser_validInput() {
 String user = "User1";
 chatFrame.addUser(user);
 chatFrame.deleteUser(user);
 assertFalse(list.getModel().contains(user));
 }

 @Test
 public void testGetUserlist_validInput() {
 String[] userList = {"User1", "User2", "User3"};
 chatFrame.addUser(userList[0]);
 chatFrame.addUser(userList[1]);
 chatFrame.addUser(userList[2]);
 assertArrayEquals(userList, chatFrame.getUserlist());
 }

 @Test
 public void testActionPerformed_chooseColor() {
 final Color newColor = Color.BLUE;
 doReturn(newColor).when(parent).getHeroColor();
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(chatFrame, times(1)).changeStyle(12, newColor, false, false, eq("User1"));
 }

 @Test
 public void testActionPerformed_bgColor() {
 final Color newColor = Color.GREEN;
 doReturn(newColor).when(parent).getBackgroundColor();
 chatFrame.actionPerformed(mock(ActionEvent.class));
 assertEquals(newColor, chatFrame.showTxt.getBackground());
 }

 @Test
 public void testActionPerformed_whisper() {
 String wUser = "/w User1 ";
 doReturn(wUser).when(txtInput).getText();
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(parent, times(1)).sendWhisper("/w User1 Hello World");
 }

 @Test
 public void testMousePressed_list() {
 final MouseEvent mouseEvent = mock(MouseEvent.class);
 chatFrame.maybeShowPopup(mouseEvent);
 verify(chatFrame.listPop, times(1)).show(eq(chatFrame.list), anyInt(), anyInt());
 }

 @Test
 public void testMousePressed_showTxt() {
 final MouseEvent mouseEvent = mock(MouseEvent.class);
 chatFrame.maybeShowPopup(mouseEvent);
 verify(chatFrame.txtPop, times(1)).show(eq(chatFrame.list), anyInt(), anyInt());
 }
}