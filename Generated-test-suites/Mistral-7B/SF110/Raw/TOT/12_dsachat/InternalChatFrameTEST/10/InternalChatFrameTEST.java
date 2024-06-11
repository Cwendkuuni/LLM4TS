package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.MouseEventlistener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class InternalChatFrameTest {

 private InternalChatFrame frame;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;
 private MainFrame parentMock;

 @Before
 public void setup() {
 parentMock = mock(MainFrame.class);
 frame = new InternalChatFrame(parentMock);
 list = frame.list;
 showTxt = frame.showTxt;
 txtInput = frame.txtInput;
 }

 @After
 public void teardown() {
 frame.dispose();
 }

 @Test
 public void testAddText() {
 String line = "John: Hello World";
 frame.addText(line);
 ArgumentCaptor<String> lineCaptor = ArgumentCaptor.forClass(String.class);
 verify(showTxt).setText(lineCaptor.capture());
 String capturedText = lineCaptor.getValue();
 assertEquals(line, capturedText);
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = frame.doc;
 Style regular = doc.getStyle("regular");
 Style italic = doc.getStyle("italic");
 Style bold = doc.getStyle("bold");
 Style boldItalic = doc.getStyle("bold-italic");
 Style name = doc.getStyle("name");
 Style system = doc.getStyle("system");
 Style small = doc.getStyle("small");
 Style large = doc.getStyle("large");

 assertEquals(Color.BLUE, name.getForeground());
 assertEquals(16, name.getFontSize());
 assertEquals(Color.BLACK, regular.getForeground());
 assertEquals(14, regular.getFontSize());
 assertEquals(Color.LIGHT_GRAY, system.getForeground());
 assertEquals(14, system.getFontSize());

 assertTrue(italic.isItalic());
 assertTrue(bold.isBold());
 assertTrue(boldItalic.isBold());
 assertTrue(boldItalic.isItalic());
 }

 @Test
 public void testChangeStyle() {
 frame.changeStyle(12, Color.RED, true, false, "name");
 Style name = frame.doc.getStyle("name");
 assertEquals(Color.RED, name.getForeground());
 frame.changeStyle(12, null, false, false, "name");
 Style nameWithoutColor = frame.doc.getStyle("name");
 assertNull(nameWithoutColor.getForeground());
 }

 @Test
 public void testAddUser() {
 frame.addUser("John");
 assertEquals(1, frame.listModel.getSize());
 assertEquals("John", frame.listModel.getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 frame.addUser("John");
 frame.addUser("Mike");
 frame.deleteUser("John");
 assertEquals(1, frame.listModel.getSize());
 assertFalse(frame.listModel.contains("John"));
 }

 @Test
 public void testGetUserlist() {
 frame.addUser("John");
 frame.addUser("Mike");
 String[] userList = frame.getUserlist();
 assertEquals(2, userList.length);
 assertArrayEquals(new String[]{"John", "Mike"}, userList);
 }

 @Test
 public void testActionPerformedWhisper() {
 String targetUser = "/w John ";
 txtInput.setText(targetUser);
 txtInput.doClick();
 txtInput.sendActionEvent();
 verify(parentMock).sendMessage(targetUser);
 }

 @Test
 public void testActionPerformedChangeColor() {
 Color newColor = new Color(1, 1, 1);
 MouseEvent mouseEvent = mock(MouseEvent.class);
 when(mouseEvent.getComponent()).thenReturn(showTxt);
 when(mouseEvent.getX()).thenReturn(10);
 when(mouseEvent.getY()).thenReturn(10);
 frame.maybeShowPopup(mouseEvent);
 verify(listPop).isVisible();
 verify(txtPop).isVisible();

 verify(listPop).getComponentAt(0).doClick();
 verify(frame).changeStyle(12, newColor, false, false, "name");
 }

 @Test
 public void testActionPerformedBgColor() {
 Color newColor = new Color(1, 1, 1);
 MouseEvent mouseEvent = mock(MouseEvent.class);
 when(mouseEvent.getComponent()).thenReturn(showTxt);
 when(mouseEvent.getX()).thenReturn(10);
 when(mouseEvent.getY()).thenReturn(10);
 frame.maybeShowPopup(mouseEvent);
 verify(txtPop).isVisible();
 verify(frame).changeStyle(null, newColor, false, false, "showTxt");
 }
}