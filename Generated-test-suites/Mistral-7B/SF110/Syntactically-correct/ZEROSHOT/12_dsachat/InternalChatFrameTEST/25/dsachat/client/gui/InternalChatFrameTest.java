package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JFrame parentFrameMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 parentFrameMock = mock(JFrame.class);
 chatFrame = new InternalChatFrame(parentFrameMock);
 }

 @After
 public void tearDown() {
 chatFrame.dispose();
 }

 @Test
 public void testInternalChatFrameConstruction() {
 assertEquals("Chat", chatFrame.getTitle());
 assertTrue(chatFrame.isFocusable());
 assertTrue(chatFrame.isResizable());
 assertFalse(chatFrame.isAlwaysOnTop());
 assertFalse(chatFrame.isShowing());
 }

 @Test
 public void testCreateGUI() {
 assertNotNull(chatFrame.getList());
 assertNotNull(chatFrame.getShowTxt());
 assertNotNull(chatFrame.getTextInput());
 assertTrue(chatFrame.getList().isVisible());
 assertTrue(chatFrame.getShowTxt().isEditable());
 }

 @Test
 public void testAddText() {
 String line = "John: Hello";
 chatFrame.addText(line);
 JTextPane showTxt = chatFrame.getShowTxt();
 ListModel<String> listModel = chatFrame.getListModel();

 assertEquals(1, listModel.getSize());
 assertEquals("John", listModel.getElementAt(0));
 assertEquals(line, showTxt.getText(0, showTxt.getText().length()));
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = chatFrame.getShowTxt().getStyledDocument();
 Style regular = doc.getStyle("regular");
 Style italic = doc.getStyle("italic");
 Style bold = doc.getStyle("bold");
 Style boldItalic = doc.getStyle("bold-italic");
 Style name = doc.getStyle("name");
 Style system = doc.getStyle("system");
 Style small = doc.getStyle("small");
 Style large = doc.getStyle("large");

 assertEquals(8, doc.getStyles().length);
 assertEquals("default", regular.getName());
 assertEquals(Font.ITALIC, italic.getFontAttributes().getBold());
 assertEquals(Font.BOLD, bold.getFontAttributes().getItalic());
 assertEquals(Font.BOLD | Font.ITALIC, boldItalic.getFontAttributes().getItalic());
 assertEquals(Color.BLUE, name.getForeground());
 assertEquals(new Color(0.0f, 0.6f, 0.0f, 1.0f), system.getForeground());
 assertEquals(Font.ITALIC, small.getFontAttributes().getItalic());
 assertEquals(Font.BOLD, large.getFontAttributes().getBold());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("John");
 JList<String> list = chatFrame.getList();

 assertEquals(1, list.getModel().getSize());
 assertEquals("John", list.getModel().getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("John");
 chatFrame.addUser("Mary");
 JList<String> list = chatFrame.getList();
 String[] users = chatFrame.getUserlist();

 assertEquals(2, list.getModel().getSize());
 assertEquals("John", users[0]);
 assertEquals("Mary", users[1]);
 chatFrame.deleteUser("John");
 assertEquals(1, list.getModel().getSize());
 assertEquals("Mary", users[0]);
 }

 @Test
 public void testGetUserlist() {
 chatFrame.addUser("John");
 chatFrame.addUser("Mary");
 String[] users = chatFrame.getUserlist();

 assertEquals(2, users.length);
 assertEquals("John", users[0]);
 assertEquals("Mary", users[1]);
 }

 @Test
 public void testActionPerformed() {
 chatFrame.addActionListener(mock(ActionListener.class));
 String user = "John";
 chatFrame.addUser(user);
 chatFrame.getTextInput().setText("message");
 SwingUtilities.invokeLater(new Runnable() {
 @Override public void run() {
 chatFrame.getTextInput().doClick();
 }
 });
 verify(chatFrame, times(1)).addText("You: message\n");
 verify(chatFrame, times(1)).addUser("You");
 }
}