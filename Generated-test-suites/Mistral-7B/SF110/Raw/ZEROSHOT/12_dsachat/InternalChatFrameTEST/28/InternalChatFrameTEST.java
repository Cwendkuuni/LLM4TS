package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;

import org.junit.*;

import dsachat.client.gui.*;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parentFrame;

 @Before
 public void setUp() {
 parentFrame = new MainFrame();
 chatFrame = new InternalChatFrame(parentFrame);
 }

 @After
 public void tearDown() {
 chatFrame.dispose();
 parentFrame.dispose();
 }

 @Test
 public void testCreateGUI() {
 assertTrue(chatFrame.isVisible());
 assertEquals(2, chatFrame.getNumberOfPanes());
 JList list = chatFrame.getList();
 JTextPane showTxt = chatFrame.getShowTxt();
 JTextField txtInput = chatFrame.getTxtInput();
 assertNotNull(list);
 assertNotNull(showTxt);
 assertNotNull(txtInput);
 }

 @Test
 public void testAddText() {
 String line = "You: Hello, World!";
 chatFrame.addText(line);
 StyledDocument doc = chatFrame.getShowTxt().getStyledDocument();
 String text = "";
 try {
 text = doc.getText(0, doc.getLength());
 }
 catch (BadLocationException e) {
 e.printStackTrace();
 }
 assertEquals("You: Hello, World!\n", text);
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

 assertEquals(StyleContext.getDefaultStyleContext().getStyle("default"), regular.getParent());
 assertEquals(regular, doc.getStyle("default"));

 assertEquals(regular, italic.getParent());
 assertFalse(italic.isBold());
 assertTrue(italic.isItalic());

 assertEquals(regular, bold.getParent());
 assertTrue(bold.isBold());
 assertFalse(bold.isItalic());

 assertEquals(regular, boldItalic.getParent());
 assertTrue(boldItalic.isBold());
 assertTrue(boldItalic.isItalic());

 assertEquals(regular, name.getParent());
 assertFalse(name.isBold());
 assertFalse(name.isItalic());
 assertNotNull(name.getForeground());

 assertEquals(regular, system.getParent());
 assertFalse(system.isBold());
 assertFalse(system.isItalic());
 assertNotNull(system.getForeground());

 assertEquals(regular, small.getParent());
 assertEquals(regular, large.getParent());
 assertFalse(small.isBold());
 assertFalse(small.isItalic());
 assertEquals(10, small.getFontSize());
 assertFalse(large.isBold());
 assertFalse(large.isItalic());
 assertEquals(16, large.getFontSize());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 JList list = chatFrame.getList();
 assertEquals(1, list.getModel().getSize());
 assertEquals("User1", list.getModel().getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 JList list = chatFrame.getList();
 chatFrame.deleteUser("User1");
 assertEquals(1, list.getModel().getSize());
 assertFalse(list.isSelectionEmpty());
 assertEquals("User2", list.getSelectedValue());
 }

 @Test
 public void testGetUserlist() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 String[] userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 assertEquals("User1", userlist[0]);
 assertEquals("User2", userlist[1]);
 }

 @Test
 public void testActionPerformed() {
 chatFrame.list.setSelectedValue("User1", true);
 chatFrame.actionPerformed(new ActionEvent("chooseColor", ActionEvent.ACTION_PERFORMED, "chooseColor"));
 Style style = chatFrame.getShowTxt().getStyledDocument().getStyle("User1");
 Color newColor = Color.RED;
 chatFrame.changeStyle(12, newColor, false, false, "User1");
 assertEquals(newColor, style.getForeground());
}

 @Test
 public void testSendInput() {
 String input = "Hello";
 chatFrame.txtInput.setText(input);
 chatFrame.actionPerformed(new ActionEvent("sendInput", ActionEvent.ACTION_PERFORMED, "sendInput"));
 JList list = chatFrame.getList();
 assertEquals(1, list.getModel().getSize());
 String user = (String) list.getModel().getElementAt(0);
 String expected = "You: " + input;
 String actual = user.substring(0, user.lastIndexOf(":"));
 assertEquals(expected, actual);
 }

 @Test
 public void testListSelectionListener() {
 JList list = chatFrame.getList();
 ListSelectionModel selectionModel = list.getSelectionModel();
 selectionModel.addListSelectionListener(new ListSelectionListener() {
 @Override
 public void valueChanged(ListSelectionEvent e) {
 if (e.getValueIsAdjusting()) {
 return;
 }
 String selectedUser = (String) list.getSelectedValue();
 assertEquals(selectedUser, chatFrame.list.getSelectedValue());
 }
 });
 String user1 = "User1";
 String user2 = "User2";
 chatFrame.addUser(user1);
 chatFrame.addUser(user2);
 list.setSelectedIndex(1);
 chatFrame.fireValueChanged(new ListSelectionEvent(list, 1, 1, ListSelectionModel.VALUE_CHANGED));
 String selectedUser = (String) list.getSelectedValue();
 assertEquals(user2, selectedUser);
 }
}