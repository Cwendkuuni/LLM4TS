package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.*;

import org.junit.*;
import org.junit.runner.*;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;
import dsachat.client.gui.StyledDocument;

@RunWith(MockitoJUnitRunner.class)
public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;

 @Before
 public void setup() {
 parent = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parent);
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @Test
 public void testCreateGUI() {
 // Test that components are added correctly
 assertEquals(1, chatFrame.getContentPane().getComponentCount());
 assertSame(chatFrame.list, chatFrame.getContentPane().getComponent(0));
 // Test that listeners are added correctly
 verify(list, times(1)).addMouseListener(chatFrame);
 verify(showTxt, times(1)).addMouseListener(chatFrame);
 // Test that correct sizes are set
 assertEquals(800, chatFrame.getWidth());
 assertEquals(650, chatFrame.getHeight());
 }

 @Test
 public void testAddText() {
 // Test adding regular text
 String text = "John: Hello";
 chatFrame.addText(text);
 assertEquals("John: Hello\n", showTxt.getText());
 // Test adding italic text
 text = "*John: Hello";
 chatFrame.addText(text);
 assertEquals("John: Hello\n", showTxt.getText());
 Style italicStyle = chatFrame.doc.getStyle("italic");
 assertEquals(1, chatFrame.doc.getParagraphElements(showTxt.getCaretPosition()).length);
 assertEquals(italicStyle, chatFrame.doc.getParagraphElement(showTxt.getCaretPosition()).getCharacterElement(0).getStyle());
 // Test adding bold text
 text = "*John:*Hello";
 chatFrame.addText(text);
 assertEquals("John: Hello\n", showTxt.getText());
 Style boldStyle = chatFrame.doc.getStyle("bold");
 assertEquals(1, chatFrame.doc.getParagraphElements(showTxt.getCaretPosition()).length);
 assertEquals(boldStyle, chatFrame.doc.getParagraphElement(showTxt.getCaretPosition()).getCharacterElement(0).getStyle());
 // Test adding bold italic text
 text = "*John:**Hello";
 chatFrame.addText(text);
 assertEquals("John: Hello\n", showTxt.getText());
 Style boldItalicStyle = chatFrame.doc.getStyle("bold-italic");
 assertEquals(1, chatFrame.doc.getParagraphElements(showTxt.getCaretPosition()).length);
 assertEquals(boldItalicStyle, chatFrame.doc.getParagraphElement(showTxt.getCaretPosition()).getCharacterElement(0).getStyle());
 }

 @Test
 public void testChangeStyle() {
 // Test changing font size
 chatFrame.changeStyle(14, null, false, false, "user1");
 assertEquals(14, StyleConstants.getFontSize(chatFrame.doc.getStyle("user1")));
 // Test changing color
 chatFrame.changeStyle(12, Color.RED, false, false, "user1");
 assertEquals(Color.RED, chatFrame.doc.getStyle("user1").getForeground());
 // Test changing bold
 chatFrame.changeStyle(12, null, true, false, "user1");
 assertTrue(chatFrame.doc.getStyle("user1").isBold());
 // Test changing italic
 chatFrame.changeStyle(12, null, false, true, "user1");
 assertTrue(chatFrame.doc.getStyle("user1").isItalic());
 // Test changing both bold and italic
 chatFrame.changeStyle(12, null, true, true, "user1");
 assertTrue(chatFrame.doc.getStyle("user1").isBold());
 assertTrue(chatFrame.doc.getStyle("user1").isItalic());
 }

 @Test
 public void testAddUser() {
 // Test adding user to list model
 chatFrame.addUser("user1");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals("user1", chatFrame.listModel.get(0));
 // Test adding user to document
 chatFrame.addText("user1: Hello");
 assertEquals("user1: Hello\n", showTxt.getText());
 Style user1Style = chatFrame.doc.getStyle("user1");
 assertEquals(1, chatFrame.doc.getParagraphElements(showTxt.getCaretPosition()).length);
 assertEquals(user1Style, chatFrame.doc.getParagraphElement(showTxt.getCaretPosition()).getCharacterElement(0).getStyle());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullUser() {
 chatFrame.addUser(null);
 }

 @Test
 public void testGetUserlist() {
 String[] users = chatFrame.getUserlist();
 assertEquals(0, users.length);
 // Test adding users and getting list
 chatFrame.addUser("user1");
 chatFrame.addUser("user2");
 users = chatFrame.getUserlist();
 assertEquals(2, users.length);
 assertEquals("user1", users[0]);
 assertEquals("user2", users[1]);
 }

 @Test
 public void testDeleteUser() {
 // Test deleting non-existent user
 chatFrame.deleteUser("user1");
 // Test deleting user from list model
 chatFrame.addUser("user1");
 chatFrame.addUser("user2");
 chatFrame.deleteUser("user1");
 assertEquals(1, chatFrame.listModel.size());
 assertEquals("user2", chatFrame.listModel.get(0));
 // Test deleting user from document
 chatFrame.addText("user1: Hello");
 chatFrame.deleteUser("user1");
 assertEquals("user2: ", showTxt.getText());
 }

 @Test
 public void testActionPerformed_chooseColor() {
 // Test choosing color for user
 chatFrame.list.setSelectedValue("user1");
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(parent, times(1)).changeColor("user1", Color.RED);
 }

 @Test
 public void testActionPerformed_bgColor() {
 // Test choosing background color for user
 chatFrame.list.setSelectedValue("user1");
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(showTxt, times(1)).setBackground(Color.RED);
 }

 @Test
 public void testActionPerformed_whisper() {
 // Test whispering to user
 chatFrame.list.setSelectedValue("user1");
 txtInput.setText("/w user1 Hello");
 chatFrame.actionPerformed(mock(ActionEvent.class));
 assertEquals("John: /w user1 Hello\n", showTxt.getText());
 }

}