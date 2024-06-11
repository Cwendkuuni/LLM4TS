package dsachat.client.gui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JFrame parentFrame;

 @Before
 public void setUp() {
 parentFrame = new JFrame();
 chatFrame = new InternalChatFrame(parentFrame);
 list = chatFrame.getList();
 showTxt = chatFrame.getShowTxt();
 }

 @Test
 public void testAddText() {
 String line = "You: Hello World";
 chatFrame.addText(line);
 String expected = "You: Hello World\n";
 String actual = showTxt.getText();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddTextWithStyle() {
 String line = "*You*: *Hello World*";
 chatFrame.addText(line);
 String expected = "*You*: *Hello World*\n";
 String actual = showTxt.getText();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddTextWithMultipleStyles() {
 String line = "*You*: **Hello** *World*";
 chatFrame.addText(line);
 String expected = "*You*: **Hello** *World*\n";
 String actual = showTxt.getText();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 String[] expected = {"User1"};
 String[] actual = chatFrame.getUserlist();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.deleteUser("User1");
 String[] expected = {"User2"};
 String[] actual = chatFrame.getUserlist();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testActionPerformedWithChooseColor() {
 chatFrame.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
 JColorChooser chooser = new JColorChooser();
 Color newColor = chooser.showDialog(null, "Choose Color", Color.BLACK);
 chatFrame.changeStyle(12, newColor, false, false, "User1");
 String expected = "User1[color=newColor]";
 Style userStyle = chatFrame.doc.getStyle("User1");
 assertEquals(expected, userStyle.getName());
 }

 @Test
 public void testActionPerformedWithBgColor() {
 chatFrame.actionPerformed(new ActionEvent(showTxt, 0, "bgColor"));
 JColorChooser chooser = new JColorChooser();
 Color newColor = chooser.showDialog(null, "Choose Color", Color.BLACK);
 chatFrame.changeStyle(12, null, false, false, "User1");
 Color expectedBackground = newColor;
 assertEquals(expectedBackground, showTxt.getBackground());
 }

 @Test
 public void testActionPerformedWithWhisper() {
 chatFrame.actionPerformed(new ActionEvent(list, 0, "whisper"));
 String expected = "/w User1 ";
 String actual = chatFrame.txtInput.getText();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetChatlog() {
 chatFrame.addText("User1: Hello World");
 chatFrame.addText("User2: Hello User1");
 String expected = "User1: Hello World\nUser2: Hello User1\n";
 String actual = chatFrame.getChatlog();
 assertEquals(expected, actual);
 }

 @After
 public void tearDown() {
 parentFrame.dispose();
 }
}