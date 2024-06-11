package dsachat.client.gui.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;
import dsachat.client.gui.test.mock.MockMainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parent);
 }

 @Test
 public void testAddTextWithValidMessage() {
 String message = "User1: Hello World!";
 chatFrame.addText(message);
 assertEquals(message, chatFrame.getChatlog());
 }

 @Test
 public void testAddTextWithEmptyMessage() {
 chatFrame.addText("");
 assertTrue(chatFrame.getChatlog().isEmpty());
 }

 @Test
 public void testAddTextWithNullMessage() {
 chatFrame.addText(null);
 assertTrue(chatFrame.getChatlog().isEmpty());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 assertEquals("User1", chatFrame.getUserlist()[0]);
 }

 @Test
 public void testDeleteUser() {
 String[] userlist = {"User1", "User2", "User3"};
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.addUser("User3");
 chatFrame.deleteUser("User1");
 assertArrayEquals(new String[]{"User2", "User3"}, chatFrame.getUserlist());
 }

 @Test
 public void testGetUserlist() {
 String[] userlist = {"User1", "User2", "User3"};
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.addUser("User3");
 assertArrayEquals(userlist, chatFrame.getUserlist());
 }

 @Test
 public void testActionPerformedWhisper() {
 String user = "User1";
 chatFrame.addUser(user);
 chatFrame.list.setSelectedValue(user, false);
 chatFrame.actionPerformed(mock(ActionEvent.class));
 assertEquals("/w " + user + " ", chatFrame.txtInput.getText());
 }

 @Test
 public void testActionPerformedChangeColor() {
 String user = "User1";
 chatFrame.addUser(user);
 chatFrame.list.setSelectedValue(user, false);
 String colorName = "blue";
 chatFrame.listPop.getPopupMenu().getComponent(0).doClick();
 JOptionPane.showMessageDialog(chatFrame, "Please choose a color");
 when(JColorChooser.showDialog(chatFrame, "Choose Color for user " + user, eq(Color.black))).thenReturn(Color.BLUE);
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(chatFrame).changeStyle(eq(12), eq(Color.BLUE), eq(false), eq(false), eq(user));
 }

 @Test
 public void testActionPerformedBgColor() {
 String user = "User1";
 chatFrame.addUser(user);
 chatFrame.list.setSelectedValue(user, false);
 String colorName = "red";
 chatFrame.txtPop.getPopupMenu().getComponent(0).doClick();
 JOptionPane.showMessageDialog(chatFrame, "Please choose a color");
 when(JColorChooser.showDialog(chatFrame, "Choose Color for user " + user, eq(Color.black))).thenReturn(Color.RED);
 chatFrame.actionPerformed(mock(ActionEvent.class));
 assertEquals(Color.RED, chatFrame.showTxt.getBackground());
 }

 @Test
 public void testMouseClickedOnList() {
 chatFrame.createGUI();
 MouseAdapter mouseAdapter = mock(MouseAdapter.class);
 chatFrame.list.addMouseListener(mouseAdapter);
 MouseEvent mouseEvent = mock(MouseEvent.class);
 chatFrame.list.dispatchEvent(mouseEvent);
 verify(mouseAdapter).mouseClicked(eq(mouseEvent));
 }

 @Test
 public void testMouseClickedOnTextPane() {
 chatFrame.createGUI();
 MouseAdapter mouseAdapter = mock(MouseAdapter.class);
 chatFrame.showTxt.addMouseListener(mouseAdapter);
 MouseEvent mouseEvent = mock(MouseEvent.class);
 chatFrame.showTxt.dispatchEvent(mouseEvent);
 verify(mouseAdapter).mouseClicked(eq(mouseEvent));
 }
}