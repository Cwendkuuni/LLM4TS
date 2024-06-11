package dsachat.client.gui.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.util.Arrays;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;
 private MainFrame parentMock;

 @Before
 public void setUp() {
 parentMock = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parentMock);
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @Test
 public void testInternalChatFrameConstructor() {
 assertNotNull(chatFrame);
 assertTrue(chatFrame.isVisible());
 assertNotNull(chatFrame.getTitle());
 assertEquals("Chat", chatFrame.getTitle());
 }

 @Test
 public void testAddTextMethod() {
 String line = "Test: This is a test message";
 chatFrame.addText(line);
 String expectedText = "Test: This is a test message\n";
 assertEquals(expectedText, showTxt.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddTextMethodWithInvalidLine() {
 chatFrame.addText("Hello");
 }

 @Test
 public void testAddStylesToDocumentMethod() {
 StyledDocument doc = chatFrame.doc;
 assertNotNull(doc);
 }

 @Test
 public void testChangeStyleMethod() {
 Color newColor = Color.RED;
 chatFrame.changeStyle(12, newColor, false, false, "name");
 Style s = doc.getStyle("name");
 assertEquals(newColor, s.getBackground());
 }

 @Test
 public void testAddUserMethod() {
 chatFrame.addUser("User1");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals("User1", chatFrame.listModel.getElementAt(0));
 }

 @Test
 public void testDeleteUserMethod() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.deleteUser("User1");
 assertEquals(1, chatFrame.listModel.size());
 assertFalse(chatFrame.listModel.contains("User1"));
 }

 @Test
 public void testGetUserlistMethod() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 String[] expectedUserlist = new String[]{"User1", "User2"};
 assertArrayEquals(expectedUserlist, chatFrame.getUserlist());
 }

 @Test
 public void testActionPerformedMethodWithChooseColor() {
 final Color newColor = new Color(135, 206, 235);
 chatFrame.addMouseListener(new MouseAdapter() {
 @Override
 public void mousePressed(MouseEvent e) {
 chatFrame.listPop.show(list, 0, 0);
 }
 });
 chatFrame.list.dispatchEvent(new MouseEvent(list, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 0, 1));
 chatFrame.listPop.getComponentAt(0).requestFocus();
 chatFrame.listPop.getComponentAt(0).dispatchEvent(new ActionEvent("chooseColor", ActionEvent.ACTION_PERFORMED, "chooseColor"));
 assertEquals(newColor, chatFrame.getStyle("name").getBackground());
 }

 @Test
 public void testActionPerformedMethodWithBgColor() {
 final Color newColor = new Color(135, 206, 235);
 chatFrame.addMouseListener(new MouseAdapter() {
 @Override
 public void mousePressed(MouseEvent e) {
 chatFrame.txtPop.show(showTxt, 0, 0);
 }
 });
 chatFrame.showTxt.dispatchEvent(new MouseEvent(showTxt, MouseEvent.MOUSE_PRESSED, System.currenttimeMillis(), 0, 0, 0, 0, 1));
 chatFrame.txtPop.getComponentAt(0).requestFocus();
 chatFrame.txtPop.getComponentAt(0).dispatchEvent(new ActionEvent("bgColor", ActionEvent.ACTION_PERFORMED, "bgColor"));
 assertEquals(newColor, showTxt.getBackground());
 }

 @Test
 public void testGetChatlogMethod() {
 chatFrame.addText("Test1: This is Test1 message");
 chatFrame.addText("Test2: This is Test2 message");
 assertEquals("Test1: This is Test1 message\nTest2: This is Test2 message\n", chatFrame.getChatlog());
 }
}