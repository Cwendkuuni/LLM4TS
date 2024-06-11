package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private StyledDocument doc;
 private MainFrame parentMock;

 @Before
 public void setup() {
 parentMock = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parentMock);
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 doc = showTxt.getStyledDocument();
 }

 @Test
 public void testCreateGUI() {
 assertEquals("Chat", chatFrame.getTitle());
 assertTrue(chatFrame.isResizable());

 JPopupMenu listPop = chatFrame.listPop;
 assertEquals(2, listPop.getComponentCount());

 JMenuItem chooseColorItem = listPop.getComponentAt(0);
 assertEquals("Change Color", chooseColorItem.getText());
 chooseColorItem.doClick();
 verify(parentMock, times(1)).show(eq(chatFrame), anyInt(), anyInt());

 JPopupMenu txtPop = chatFrame.txtPop;
 assertEquals(1, txtPop.getComponentCount());

 JMenuItem bgColorItem = txtPop.getComponentAt(0);
 assertEquals("Change Color", bgColorItem.getText());
 bgColorItem.doClick();
 verify(parentMock, times(1)).show(eq(chatFrame), anyInt(), anyInt());
 }

 @Test
 public void testAddText() throws BadLocationException {
 String line = "You: Hello World";
 chatFrame.addText(line);

 assertEquals(line, showTxt.getText(0, showTxt.getCaretPosition()));

 String styleName = "You ";
 Style style = doc.getStyle(styleName);
 assertEquals(14, style.getFontSize());
 assertEquals(Color.BLUE, style.getForeground());

 line = "User1: Hi";
 chatFrame.addText(line);
 assertEquals(2, chatFrame.list.getModel().getSize());
 assertEquals(line, list.getModel().getElementAt(0).toString());

 line = "*User1*: Hi";
 chatFrame.addText(line);
 assertEquals(3, chatFrame.list.getModel().getSize());
 assertEquals(line, list.getModel().getElementAt(1).toString());
 }

 @Test
 public void testAddStylesToDocument() {
 Style regular = doc.getStyle("regular");
 Style def = doc.getDefaultStyle();

 assertEquals(regular, doc.getStyle("system"));

 assertEquals(Color.BLUE, doc.getStyle("name").getForeground());
 assertEquals(16, doc.getStyle("system").getFontSize());
 }

 @Test
 public void testChangeStyle() {
 String name = "User1";
 chatFrame.changeStyle(12, Color.RED, false, false, name);
 Style style = doc.getStyle(name);
 assertEquals(Color.RED, style.getForeground());
 assertEquals(12, style.getFontSize());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 assertEquals(1, chatFrame.list.getModel().getSize());
 assertEquals("User1", chatFrame.list.getSelectedValue());
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.deleteUser("User1");
 assertEquals(1, chatFrame.list.getModel().getSize());
 assertFalse(chatFrame.list.isSelectionEmpty());
 }

 @Test
 public void testGetUserlist() {
 String[] users = {"User1", "User2", "User3"};
 chatFrame.addUser(users[0]);
 chatFrame.addUser(users[1]);
 chatFrame.addUser(users[2]);
 assertEquals(Arrays.toString(users), Arrays.toString(chatFrame.getUserlist()));
 }

 @Test
 public void testActionPerformed_ChooseColor() {
 Color newColor = Color.GREEN;
 doAnswer(new Answer<Void>() {
 @Override public Void answer(InvocationOnMock invocation) throws Throwable {
 JColorChooser.showDialog(chatFrame, "Choose Color for user User1", Color.black);
 return null;
 }
 }).when(parentMock).show(eq(chatFrame), anyInt(), anyInt());

 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "chooseColor"));
 verify(parentMock, times(1)).show(eq(chatFrame), anyInt(), anyInt());
 Style style = doc.getStyle("User1");
 assertEquals(Color.GREEN, style.getForeground());
 }

 @Test
 public void testActionPerformed_BgColor() {
 Color newColor = Color.PINK;
 doAnswer(new Answer<Void>() {
 @Override public Void answer(InvocationOnMock invocation) throws Throwable {
 JColorChooser.showDialog(chatFrame, "Choose Color for user User1", Color.black);
 return null;
 }
 }).when(parentMock).show(eq(chatFrame), anyInt(), anyInt());

 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "bgColor"));
 verify(parentMock, times(1)).show(eq(chatFrame), anyInt(), anyInt());
 assertEquals(newColor, showTxt.getBackground());
 }

 @Test
 public void testActionPerformed_Whisper() {
 String wUser = "/w User1 ";
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "whisper"));
 assertEquals(wUser, txtInput.getText());
 }
}