package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.*;

import org.junit.*;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;
 private JList list;
 private DefaultListModel listModel;
 private JTextPane showTxt;
 private JTextField txtInput;
 private ActionEvent actionEvent;
 private MouseEvent mouseEvent;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 this.parent = mock(MainFrame.class);
 this.chatFrame = new InternalChatFrame(this.parent);
 this.list = this.chatFrame.list;
 this.listModel = this.chatFrame.listModel;
 this.showTxt = this.chatFrame.showTxt;
 this.txtInput = this.chatFrame.txtInput;
 this.actionEvent = mock(ActionEvent.class);
 this.mouseEvent = mock(MouseEvent.class);
 }

 @Test
 public void testInternalChatFrameConstruction() {
 assertEquals("Chat", this.chatFrame.getTitle());
 assertTrue(this.chatFrame.isResizable());
 assertFalse(this.chatFrame.isFocusTraversalKeysEnabled());
 }

 @Test
 public void testCreateGUI() {
 assertNotNull(this.splitPane = this.chatFrame.split);
 assertNotNull(this.list = this.chatFrame.list);
 assertNotNull(this.showTxt = this.chatFrame.showTxt);
 assertNotNull(this.txtInput = this.chatFrame.txtInput);
 }

 @Test
 public void testAddText() {
 String line = "User: Hello";
 this.chatFrame.addText(line);
 assertEquals(line, this.showTxt.getText().substring(0, this.showTxt.getCaretPosition()));
 }

 @Test
 public void testAddTextWithColor() {
 String line = "*User*: Hello";
 this.chatFrame.addText(line);
 StyleConstants.setBackground(this.chatFrame.doc.getStyle("name"), Color.RED);
 assertEquals(line.substring(1), this.showTxt.getText().substring(this.showTxt.getCaretPosition(), this.showTxt.getText().length()));
 }

 @Test
 public void testAddTextWithItalic() {
 String line = "User:* Hello";
 this.chatFrame.addText(line);
 assertEquals(line, this.showTxt.getText().substring(0, this.showTxt.getCaretPosition()));
 StyleConstants.setItalic(this.chatFrame.doc.getStyle("name"), true);
 assertEquals("User: Hello", this.showTxt.getText());
 }

 @Test
 public void testAddTextWithBold() {
 String line = "User:** Hello";
 this.chatFrame.addText(line);
 assertEquals(line, this.showTxt.getText().substring(0, this.showTxt.getCaretPosition()));
 StyleConstants.setBold(this.chatFrame.doc.getStyle("name"), true);
 assertEquals("User: Hello", this.showTxt.getText());
 }

 @Test
 public void testAddTextWithBoldAndItalic() {
 String line = "User:*** Hello";
 this.chatFrame.addText(line);
 assertEquals(line, this.showTxt.getText().substring(0, this.showTxt.getCaretPosition()));
 StyleConstants.setBold(this.chatFrame.doc.getStyle("name"), true);
 StyleConstants.setItalic(this.chatFrame.doc.getStyle("name"), true);
 assertEquals("User: Hello", this.showTxt.getText());
 }

 @Test
 public void testAddStylesToDocument() {
 Style defaultStyle = this.chatFrame.doc.getStyle("default");
 Style regularStyle = this.chatFrame.doc.getStyle("regular");
 Style nameStyle = this.chatFrame.doc.getStyle("name");
 Style italicStyle = this.chatFrame.doc.getStyle("italic");
 Style boldStyle = this.chatFrame.doc.getStyle("bold");
 Style boldItalicStyle = this.chatFrame.doc.getStyle("bold-italic");
 Style smallStyle = this.chatFrame.doc.getStyle("small");
 Style largeStyle = this.chatFrame.doc.getStyle("large");
 StyleSystemStyleDescriptor systemStyle = this.chatFrame.doc.getStyle("system");

 assertEquals(defaultStyle, this.chatFrame.doc.getStyle("default"));
 assertEquals(regularStyle, this.chatFrame.doc.getStyle("regular"));
 assertEquals(nameStyle, this.chatFrame.doc.getStyle("name"));
 assertEquals(italicStyle, this.chatFrame.doc.getStyle("italic"));
 assertEquals(boldStyle, this.chatFrame.doc.getStyle("bold"));
 assertEquals(boldItalicStyle, this.chatFrame.doc.getStyle("bold-italic"));
 assertEquals(smallStyle, this.chatFrame.doc.getStyle("small"));
 assertEquals(largeStyle, this.chatFrame.doc.getStyle("large"));
 assertEquals(systemStyle, this.chatFrame.doc.getStyle("system"));
 }

 @Test
 public void testChangeStyle() {
 this.chatFrame.changeStyle(12, Color.BLUE, false, false, "name");
 assertEquals(Color.BLUE, this.chatFrame.doc.getStyle("name").getForeground());
 }

 @Test
 public void testAddUser() {
 this.chatFrame.addUser("User1");
 assertEquals("User1", this.list.getModel().getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 this.chatFrame.addUser("User1");
 this.chatFrame.addUser("User2");
 this.chatFrame.deleteUser("User1");
 assertEquals(1, this.chatFrame.listModel.getSize());
 assertEquals("User2", this.chatFrame.list.getSelectedValue());
 }

 @Test
 public void testGetUserlist() {
 this.chatFrame.addUser("User1");
 this.chatFrame.addUser("User2");
 String[] userlist = this.chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 assertEquals("User1", userlist[0]);
 assertEquals("User2", userlist[1]);
 }

 @Test
 public void testActionPerformedWithChooseColor() {
 when(this.list.getSelectedValue()).thenReturn("User");
 this.chatFrame.actionPerformed(this.actionEvent);
 verify(this.parent).changeChatFrameColor("User", Color.BLACK);
 }

 @Test
 public void testActionPerformedWithBgColor() {
 this.chatFrame.actionPerformed(this.actionEvent);
 verify(this.showTxt).setBackground(Color.BLACK);
 }

 @Test
 public void testActionPerformedWithWhisper() {
 when(this.list.getSelectedValue()).thenReturn("/w User :");
 this.txtInput.setText("Test");
 this.chatFrame.txtInput.addActionListener(this.chatFrame);
 this.chatFrame.actionPerformed(this.actionEvent);
 verify(this.parent).sendWhisper("User", "Test");
 }

 @Test
 public void testMouseClickedOnList() {
 this.chatFrame.addUser("User1");
 this.chatFrame.addUser("User2");
 this.list.addMouseListener(this.chatFrame);
 this.chatFrame.mouseClicked(this.mouseEvent);
 assertEquals("User1", this.chatFrame.list.getSelectedValue());
 }

 @Test
 public void testMouseClickedOnText() {
 this.chatFrame.addUser("User1");
 this.chatFrame.addUser("User2");
 this.showTxt.addMouseListener(this.chatFrame);
 this.chatFrame.mouseClicked(this.mouseEvent);
 assertEquals("User2", this.chatFrame.list.getSelectedValue());
 }
}