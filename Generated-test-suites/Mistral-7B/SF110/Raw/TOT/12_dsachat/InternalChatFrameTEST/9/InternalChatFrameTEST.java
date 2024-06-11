package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private MainFrame parent;
 private InternalChatFrame chatFrame;

 @Before
 public void setUp() {
 parent = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parent);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCreateGUI() {
 // test if the JList and JTextPane are added to the split pane
 assertEquals(2, chatFrame.getComponents().length);
 JComponent[] components = ((JSplitPane) chatFrame.getComponents()[0]).getComponents();
 assertEquals(2, components.length);
 assertTrue(components[0] instanceof JScrollPane);
 assertTrue(components[1] instanceof JPanel);
 JComponent[] components2 = ((JPanel) components[1]).getComponents();
 assertEquals(2, components2.length);
 assertTrue(components2[0] instanceof JScrollPane);
 assertTrue(components2[1] instanceof JPanel);
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello World";
 chatFrame.addText(line);
 String expectedText = "User1: Hello World\n";
 assertEquals(expectedText, chatFrame.showTxt.getText());

 line = "*User1* Hello World";
 chatFrame.addText(line);
 String expectedText2 = "*User1* Hello World\n";
 assertEquals(expectedText2, chatFrame.showTxt.getText());
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithInvalidInput() {
 String line = "InvalidInput:";
 chatFrame.addText(line);
 }

 @Test
 public void testAddUser() {
 String user = "User1";
 chatFrame.addUser(user);
 assertEquals(1, chatFrame.listModel.getSize());
 assertEquals(user, chatFrame.listModel.getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 String user = "User1";
 chatFrame.addUser(user);
 chatFrame.listModel.removeElement(user);
 assertEquals(0, chatFrame.listModel.getSize());
 }

 @Test
 public void testGetUserlist() {
 String user1 = "User1";
 String user2 = "User2";
 chatFrame.addUser(user1);
 chatFrame.addUser(user2);
 String[] uList = chatFrame.getUserlist();
 assertEquals(2, uList.length);
 assertEquals(user1, uList[0]);
 assertEquals(user2, uList[1]);
 }

 @Test
 public void testActionPerformedForChooseColor() {
 Color newColor = Color.RED;
 chatFrame.listPop.getComponentAt(0).doClick();
 verify(parent).setBackground(newColor);
 }

 @Test
 public void testActionPerformedForBgColor() {
 Color newColor = Color.RED;
 chatFrame.txtPop.getComponentAt(0).doClick();
 assertEquals(newColor, chatFrame.showTxt.getBackground());
 }

 @Test
 public void testActionPerformedForWhisper() {
 String wUser = "/w User1 ";
 chatFrame.list.setSelectedValue("User1", false);
 chatFrame.txtInput.doClick();
 chatFrame.txtInput.setText("/w User1 ");
 chatFrame.txtInput.addActionListener(parent);
 chatFrame.txtInput.actionPerformed(new ActionEvent(chatFrame.txtInput, 0, "sendInput"));
 assertEquals("/w User1 ", chatFrame.parent.getTextFieldText());
 }
}