package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 @Mock
 private MainFrame parent;

 @InjectMocks
 private InternalChatFrame chatFrame;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCreateGUI() {
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 assertNotNull(chatFrame.listPop);
 assertNotNull(chatFrame.txtPop);
 }

 @Test
 public void testAddText() {
 String line = "John: Hello";
 chatFrame.addText(line);
 String expectedText = "John: Hello\n";
 assertEquals(expectedText, chatFrame.showTxt.getText());
 }

 @Test
 public void testAddTextWithStyles() {
 String line = "*John*: Hi *Mary*, how are you? *Mary*: I'm good.";
 chatFrame.addText(line);
 String expectedText = "*John*: Hi *Mary*, how are you? *Mary*: I'm good.\n";
 assertEquals(expectedText, chatFrame.showTxt.getText());
 }

 @Test
 public void testChangeStyle() {
 chatFrame.changeStyle(12, Color.RED, true, false, "John");
 Style style = chatFrame.doc.getStyle("John");
 assertEquals(Color.RED, style.getForeground());
 assertTrue(style.isBold());
 assertFalse(style.isItalic());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("John");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals("John", chatFrame.listModel.get(0));
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("John");
 chatFrame.addUser("Mary");
 assertEquals(2, chatFrame.listModel.size());
 chatFrame.deleteUser("John");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals("Mary", chatFrame.listModel.get(0));
 }

 @Test
 public void testGetUserlist() {
 chatFrame.addUser("John");
 chatFrame.addUser("Mary");
 List<String> expectedUserlist = Arrays.asList("John", "Mary");
 List<String> actualUserlist = new ArrayList<>(chatFrame.getUserlist().length);
 for (String user : chatFrame.getUserlist()) {
 actualUserlist.add(user);
 }
 assertEquals(expectedUserlist, actualUserlist);
 }

 @Test
 public void testActionPerformed_chooseColor() {
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(parent).setBackgroundColor(mock(Color.class));
 }

 @Test
 public void testActionPerformed_bgColor() {
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(chatFrame.showTxt).setBackground(mock(Color.class));
 }

 @Test
 public void testActionPerformed_whisper() {
 String wUser = "/w " + "Mary" + " ";
 chatFrame.txtInput.setText(wUser);
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(parent).sendWhisper("/w Mary ");
 }

 @Test
 public void testMouseEventHandling_list() {
 chatFrame.maybeShowPopup(mock(MouseEvent.class));
 verify(chatFrame.listPop, times(1)).show(chatFrame.list, anyInt(), anyInt());
 }

 @Test
 public void testMouseEventHandling_showTxt() {
 chatFrame.maybeShowPopup(mock(MouseEvent.class));
 verify(chatFrame.txtPop, times(1)).show(chatFrame.list, anyInt(), anyInt());
 }

 @Test
 public void testPopupMenuSelection_list() {
 PopupMenuEvent event = mock(PopupMenuEvent.class);
 chatFrame.listPop.pack();
 chatFrame.listPop.setVisible(true);
 chatFrame.listPop.setInvoker(chatFrame.list);
 chatFrame.listPop.setLocation(0, 0);
 chatFrame.listPop.setSize(50, 100);
 chatFrame.listPop.setSelectedIndex(1);
 chatFrame.listPop.handleEvent(event);
 verify(chatFrame, times(1)).changeStyle(anyInt(), any(Color.class), anyBoolean(), anyBoolean(), "John");
 }

 @Test
 public void testPopupMenuSelection_txt() {
 PopupMenuEvent event = mock(PopupMenuEvent.class);
 chatFrame.txtPop.pack();
 chatFrame.txtPop.setVisible(true);
 chatFrame.txtPop.setInvoker(chatFrame.showTxt);
 chatFrame.txtPop.setLocation(0, 0);
 chatFrame.txtPop.setSize(50, 100);
 chatFrame.txtPop.setSelectedIndex(1);
 chatFrame.txtPop.handleEvent(event);
 verify(chatFrame, times(1)).changeStyle(anyInt(), any(Color.class), anyBoolean(), anyBoolean(), "System");
 }
}