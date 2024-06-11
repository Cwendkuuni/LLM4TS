package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parentFrameMock;
 private JList listMock;
 private JTextPane showTxtMock;
 private JTextField txtInputMock;
 private JPopupMenu listPopupMock;
 private JPopupMenu txtPopupMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 parentFrameMock = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parentFrameMock);
 listMock = mock(JList.class);
 showTxtMock = mock(JTextPane.class);
 txtInputMock = mock(JTextField.class);
 listPopupMock = mock(JPopupMenu.class);
 txtPopupMock = mock(JPopupMenu.class);
 chatFrame.setList(listMock);
 chatFrame.setShowTxt(showTxtMock);
 chatFrame.setTxtInput(txtInputMock);
 chatFrame.setListPop(listPopupMock);
 chatFrame.setTextPop(txtPopupMock);
 }

 @Test
 public void testAddTextToChatFrame() {
 String line = "User1: Hello";
 chatFrame.addText(line);
 String expectedChatLog = "User1: Hello\n";
 assertEquals(expectedChatLog, chatFrame.getChatlog());
 }

 @Test
 public void testAddTextWithInvalidLineFormat() {
 String line = "Hello";
 chatFrame.addText(line);
 String expectedChatLog = "\n";
 assertEquals(expectedChatLog, chatFrame.getChatlog());
 }

 @Test
 public void testChangeStyleForUser() {
 String user = "User1";
 Color newColor = Color.RED;
 chatFrame.changeStyle(12, newColor, false, false, user);
 Style style = chatFrame.doc.getStyle(user);
 assertEquals(newColor, style.getBackground());
 }

 @Test
 public void testAddUserToList() {
 String user = "User1";
 chatFrame.addUser(user);
 assertEquals(user, chatFrame.listModel.get(0));
 }

 @Test
 public void testDeleteUserFromList() {
 String user = "User1";
 chatFrame.addUser(user);
 chatFrame.deleteUser(user);
 assertEquals(0, chatFrame.listModel.size());
 }

 @Test
 public void testGetUserList() {
 String user1 = "User1";
 String user2 = "User2";
 chatFrame.addUser(user1);
 chatFrame.addUser(user2);
 List<String> userList = chatFrame.getUserlist();
 assertEquals(2, userList.size());
 assertEquals(user1, userList.get(0));
 assertEquals(user2, userList.get(1));
 }

 @Test
 public void testMouseClickOnList() {
 String user = "User1";
 chatFrame.listModel.add(user);
 doReturn(user).when(listMock).getSelectedValue();
 doNothing().when(listPopupMock).show(listMock, anyInt(), anyInt());
 chatFrame.mouseClicked(new MouseEvent(listMock, 1, 1, 0, 1, 1, 0, false));
 verify(listPopupMock).show(listMock, anyInt(), anyInt());
 }

 @Test
 public void testMouseClickOnTextArea() {
 doNothing().when(txtPopupMock).show(listMock, anyInt(), anyInt());
 chatFrame.mouseClicked(new MouseEvent(showTxtMock, 1, 1, 0, 1, 1, 0, false));
 verify(txtPopupMock, never()).show(listMock, anyInt(), anyInt());
 }

 @Test
 public void testActionPerformedForChangeColor() {
 Color newColor = Color.GREEN;
 doReturn(listMock).when(chatFrame).getList();
 doReturn(newColor).when(JColorChooser.showDialog(chatFrame, "Choose Color for user User1", Color.BLACK));
 doNothing().when(listPopupMock).show(listMock, anyInt(), anyInt());
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "chooseColor"));
 verify(listPopupMock).show(listMock, anyInt(), anyInt());
 verify(listPopupMock).setVisible(true);
 }

 @Test
 public void testActionPerformedForBgColor() {
 Color newColor = Color.BLUE;
 doReturn(showTxtMock).when(chatFrame).getTextPane();
 doReturn(newColor).when(JColorChooser.showDialog(chatFrame, "Choose Color for user User1", Color.BLACK));
 doNothing().when(chatFrame).changeStyle(anyInt(), any(Color.class), anyBoolean(), anyBoolean(), anyString());
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "bgColor"));
 verify(showTxtMock).setBackground(newColor);
 }

 @Test
 public void testActionPerformedForWhisper() {
 String whisperUser = "/w User1 ";
 doReturn(listMock).when(chatFrame).getList();
 doReturn("/w User1 ").when(txtInputMock).getText();
 doNothing().when(parentFrameMock).sendWhisper(whisperUser);
 chatFrame.actionPerformed(new ActionEvent(txtInputMock, 0, "sendInput"));
 verify(parentFrameMock).sendWhisper(whisperUser);
 }
}