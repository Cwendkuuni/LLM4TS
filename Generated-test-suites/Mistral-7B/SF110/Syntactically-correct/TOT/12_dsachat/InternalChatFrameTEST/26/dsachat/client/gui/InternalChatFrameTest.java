package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private MainFrame parentMock;
 private InternalChatFrame chatFrame;
 private JList listMock;
 private JTextPane showTxtMock;
 private JTextField txtInputMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 parentMock = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parentMock);
 listMock = mock(JList.class);
 showTxtMock = mock(JTextPane.class);
 txtInputMock = mock(JTextField.class);

 chatFrame.list = listMock;
 chatFrame.showTxt = showTxtMock;
 chatFrame.txtInput = txtInputMock;
 }

 @Test
 public void testAddTextWithValidLine() {
 String line = "User1: Hello World";
 chatFrame.addText(line);

 String expectedText = "User1: Hello World\n";
 String actualText = chatFrame.showTxt.getText();
 assertEquals(expectedText, actualText);
 }

 @Test
 public void testAddTextWithInvalidLine() {
 String line = "Hello World";
 chatFrame.addText(line);

 String expectedText = "";
 String actualText = chatFrame.showTxt.getText();
 assertEquals(expectedText, actualText);
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 String[] expectedUserlist = {"User1"};
 String[] actualUserlist = chatFrame.getUserlist();
 assertArrayEquals(expectedUserlist, actualUserlist);
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.deleteUser("User1");
 String[] expectedUserlist = {};
 String[] actualUserlist = chatFrame.getUserlist();
 assertArrayEquals(expectedUserlist, actualUserlist);
 }

 @Test
 public void testGetChatlog() {
 String expectedChatlog = "";
 String actualChatlog = chatFrame.getChatlog();
 assertEquals(expectedChatlog, actualChatlog);
 }

 @Test
 public void testAddStylesToDocument() {
 StyleConstants.setForeground(chatFrame.doc, Color.BLUE);
 Style s = chatFrame.doc.getStyle("name");
 assertEquals(Color.BLUE, s.getForeground());
 }

 @Test
 public void testChangeStyle() {
 chatFrame.changeStyle(12, Color.BLACK, false, false, "name");
 Style s = chatFrame.doc.getStyle("name");
 assertEquals(Color.BLACK, s.getForeground());
 }

 @Test
 public void testActionPerformedWithChooseColor() {
 chatFrame.addActionListener(chatFrame);
 when(txtInputMock.getText()).thenReturn("User1: Color: #FF0000");
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "chooseColor"));

 verify(parentMock, times(1)).setBackground(argThat(isColor("#FF0000")));
 }

 @Test
 public void testActionPerformedWithBgColor() {
 chatFrame.addActionListener(chatFrame);
 when(txtInputMock.getText()).thenReturn("User1: bgColor: #00FF00");
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "bgColor"));

 verify(showTxtMock, times(1)).setBackground(argThat(isColor("#00FF00")));
 }

 @Test
 public void testActionPerformedWithWhisper() {
 chatFrame.addActionListener(chatFrame);
 when(listMock.getSelectedValue()).thenReturn("User1");
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "whisper"));

 verify(txtInputMock, times(1)).setText("/w User1 ");
 }

 @Test
 public void testMouseClickedOnList() {
 chatFrame.addMouseListener(chatFrame);
 chatFrame.listPop.show(chatFrame.list, 0, 0);
 chatFrame.mouseClicked(new MouseEvent(chatFrame.list, 0, System.currentTimeMillis(), 0, 0, 0, 1, false));

 verify(listMock, times(1)).setPopupMenu(chatFrame.listPop);
 }

 @Test
 public void testMouseClickedOnShowTxt() {
 chatFrame.addMouseListener(chatFrame);
 chatFrame.txtPop.show(chatFrame.list, 0, 0);
 chatFrame.mouseClicked(new MouseEvent(chatFrame.showTxt, 0, System.currentTimeMillis(), 0, 0, 0, 1, false));

 verify(showTxtMock, times(1)).setPopupMenu(chatFrame.txtPop);
 }
}