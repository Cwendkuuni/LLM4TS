package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.InternalChatFrame.Style;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JPopupMenu listPop;

 @Before
 public void setUp() {
 chatFrame = new InternalChatFrame(new JFrame());
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 listPop = chatFrame.listPop;
 }

 @After
 public void tearDown() {
 chatFrame.dispose();
 }

 @Test
 public void testConstructor() {
 assertEquals("Chat", chatFrame.getTitle());
 assertFalse(chatFrame.isResizable());
 assertFalse(chatFrame.isFocusable());
 }

 @Test
 public void testCreateGUI() {
 assertTrue(list.isVisible());
 assertTrue(showTxt.isEditable());
 assertFalse(txtInput.isEditable());
 assertNotNull(listPop);
 assertNotNull(txtPop);
 }

 @Test
 public void testAddText() {
 String line = "You: Hello World";
 chatFrame.addText(line);
 String expected = "You: Hello World\n";
 assertEquals(expected, showTxt.getText());
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithBadLocationException() {
 chatFrame.addText("Line with BadLocationException");
 }

 @Test
 public void testAddStylesToDocument() {
 Style regular = chatFrame.doc.getStyle("regular");
 Style italic = chatFrame.doc.getStyle("italic");
 Style bold = chatFrame.doc.getStyle("bold");
 Style boldItalic = chatFrame.doc.getStyle("bold-italic");
 Style name = chatFrame.doc.getStyle("name");
 Style system = chatFrame.doc.getStyle("system");
 Style small = chatFrame.doc.getStyle("small");
 Style large = chatFrame.doc.getStyle("large");

 assertEquals(Color.BLUE, StyleConstants.getForeground(name));
 assertEquals(16, StyleConstants.getFontSize(system));
 }

 @Test
 public void testChangeStyle() {
 Style s = chatFrame.doc.getStyle("name");
 Color newColor = new Color(1, 1, 1);
 chatFrame.changeStyle(12, newColor, false, false, "name");
 assertEquals(newColor, StyleConstants.getForeground(s));
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 List<String> userlist = chatFrame.getUserlist();
 assertEquals(1, userlist.length);
 assertEquals("User1", userlist.get(0));
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.deleteUser("User1");
 List<String> userlist = chatFrame.getUserlist();
 assertEquals(0, userlist.size());
 }

 @Test
 public void testGetChatlog() {
 String expected = "You: Hello World\n";
 chatFrame.addText("You: Hello World");
 assertEquals(expected, chatFrame.getChatlog());
 }

 @Test
 public void testListPopupMenu() {
 list.addMouseListener(e -> listPop.show(list, e.getX(), e.getY()));
 listPop.add(new JMenuItem("Test"));
 listPop.show(list, 0, 0);
 assertTrue(listPop.isVisible());
 }

 @Test
 public void testTextPopupMenu() {
 showTxt.addMouseListener(e -> txtPop.show(list, e.getX(), e.getY()));
 txtPop.add(new JMenuItem("Test"));
 listPop.show(showTxt, 0, 0);
 assertTrue(txtPop.isVisible());
 }

}