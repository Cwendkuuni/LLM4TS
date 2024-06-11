package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.InternalChatFrame.Style;

public class InternalChatFrameTest {

 private InternalChatFrame frame;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;
 private MainFrame parentMock;

 @Before
 public void setup() {
 this.parentMock = mock(MainFrame.class);
 this.frame = new InternalChatFrame(this.parentMock);
 this.list = this.frame.getList();
 this.showTxt = this.frame.getShowTxt();
 this.txtInput = this.frame.getTxtInput();
 }

 @After
 public void tearDown() {
 this.frame.dispose();
 }

 @Test
 public void testInternalChatFrameConstructor() {
 assertEquals("Chat", this.frame.getTitle());
 assertTrue(this.frame.isFocusable());
 assertFalse(this.frame.isResizable());
 assertFalse(this.frame.isAlwaysOnTop());
 }

 @Test
 public void testCreateGUI() {
 GridBagConstraints c = new GridBagConstraints();
 JSplitPane split = this.frame.getSplitPane();
 JScrollPane scrList = (JScrollPane) split.getLeftComponent();
 JPanel right = (JPanel) split.getRightComponent();
 JTextPane scrTxt = (JTextPane) right.getComponent(0);
 JTextField input = (JTextField) right.getComponent(1);

 assertEquals(1, split.getOrientation());
 assertEquals(JSplitPane.HORIZONTAL_SPLIT, split.getDividerLocation());
 assertSame(this.list, scrList.getViewport().getView());
 assertSame(this.frame, scrList.getComponentPopupMenu());
 assertSame(this.showTxt, scrTxt.getDocument());
 assertSame(this.txtInput, input);
 }

 @Test
 public void testAddText() throws BadLocationException {
 String line = "You: Hello";
 this.frame.addText(line);

 assertEquals(line, this.showTxt.getText().substring(0, this.showTxt.getCaretPosition()));

 Style s = this.frame.getDoc().getStyle("You");
 assertNotNull(s);
 assertEquals("You", s.getName());
 assertTrue(StyleConstants.isItalic(s));
 assertFalse(StyleConstants.isBold(s));
 }

 @Test
 public void testAddTextWithColor() throws BadLocationException {
 String line = "*Red:* You: Hello";
 this.frame.addText(line);

 assertEquals(line, this.showTxt.getText().substring(0, this.showTxt.getCaretPosition()));

 Style s = this.frame.getDoc().getStyle("You");
 assertEquals(Color.RED, s.getBackground());
 }

 @Test
 public void testAddTextWithBold() throws BadLocationException {
 String line = "**You**: Hello";
 this.frame.addText(line);

 assertEquals(line, this.showTxt.getText().substring(0, this.showTxt.getCaretPosition()));

 Style s = this.frame.getDoc().getStyle("You");
 assertTrue(StyleConstants.isBold(s));
 assertFalse(StyleConstants.isItalic(s));
 }

 @Test
 public void testAddTextWithBoldAndItalic() throws BadLocationException {
 String line = "**You**: *Hello*";
 this.frame.addText(line);

 assertEquals(line, this.showTxt.getText().substring(0, this.showTxt.getCaretPosition()));

 Style s = this.frame.getDoc().getStyle("You");
 assertTrue(StyleConstants.isBold(s));
 assertTrue(StyleConstants.isItalic(s));
 }

 @Test
 public void testAddTextWithMultipleUsers() throws BadLocationException {
 String line1 = "User1: Hello User2";
 String line2 = "User2: Hi User1";
 this.frame.addText(line1);
 this.frame.addText(line2);

 String expected = line1 + "\n" + line2 + "\n";
 assertEquals(expected, this.showTxt.getText());
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithInvalidLine() {
 String line = " :Hello";
 this.frame.addText(line);
 fail("Expected BadLocationException");
 }

 @Test
 public void testAddStylesToDocument() {
 Style regular = this.frame.getDoc().getStyle("regular");
 Style italic = this.frame.getDoc().getStyle("italic");
 Style bold = this.frame.getDoc().getStyle("bold");
 Style boldItalic = this.frame.getDoc().getStyle("bold-italic");
 Style name = this.frame.getDoc().getStyle("name");
 Style system = this.frame.getDoc().getStyle("system");

 assertEquals("regular", regular.getName());
 assertSame(regular, this.frame.getDoc().getStyle("default"));
 assertNotNull(italic);
 assertNotNull(bold);
 assertNotNull(boldItalic);
 assertNotNull(name);
 assertNotNull(system);
 }

 @Test
 public void testChangeStyle() {
 Style style = this.frame.getDoc().getStyle("name");
 this.frame.changeStyle(14, null, false, false, "name");

 StyleConstants.setFontSize(style, 14);
 assertEquals(14, style.getFontSize());

 this.frame.changeStyle(16, null, false, false, "name");
 assertEquals(16, style.getFontSize());

 this.frame.changeStyle(12, Color.RED, false, false, "name");
 assertEquals(Color.RED, style.getForeground());

 this.frame.changeStyle(14, null, true, false, "name");
 assertTrue(StyleConstants.isBold(style));

 this.frame.changeStyle(14, null, false, true, "name");
 assertTrue(StyleConstants.isItalic(style));
 }

 @Test
 public void testAddUser() {
 String user = "User1";
 this.frame.addUser(user);

 assertEquals(1, this.frame.getList().getModel().getSize());
 assertEquals(user, this.frame.getList().getModel().getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 String user = "User1";
 this.frame.addUser(user);
 this.frame.deleteUser(user);

 assertEquals(0, this.frame.getList().getModel().getSize());
 }

 @Test
 public void testGetUserlist() {
 String[] users = {"User1", "User2", "User3"};
 for (String user : users) {
 this.frame.addUser(user);
 }

 String[] uList = this.frame.getUserlist();
 assertArrayEquals(users, uList);
 }

 @Test
 public void testActionPerformedChooseColor() {
 JColorChooser chooser = mock(JColorChooser.class);
 when(this.parentMock.getColorChooser()).thenReturn(chooser);

 String name = "User1";
 this.frame.addUser(name);
 this.frame.list.setSelectedValue(name);
 this.frame.actionPerformed(new ActionEvent(this.frame.list, ActionEvent.ACTION_PERFORMED, "chooseColor"));

 Style style = this.frame.getDoc().getStyle(name);
 Color newColor = Color.GREEN;
 when(chooser.showDialog(this.frame, "Choose Color for user User1", Color.black)).thenReturn(newColor);
 this.frame.actionPerformed(new ActionEvent(this.frame.list, ActionEvent.ACTION_PERFORMED, "chooseColor"));

 assertEquals(newColor, style.getBackground());
 }

 @Test
 public void testActionPerformedBgColor() {
 JColorChooser chooser = mock(JColorChooser.class);
 when(this.parentMock.getColorChooser()).thenReturn(chooser);

 String name = "User1";
 this.frame.addUser(name);
 this.frame.showTxt.setBackground(Color.BLACK);
 this.frame.actionPerformed(new ActionEvent(this.frame.txtInput, ActionEvent.ACTION_PERFORMED, "bgColor"));

 Color newColor = Color.GREEN;
 when(chooser.showDialog(this.frame, "Choose Color for user User1", Color.black)).thenReturn(newColor);
 this.frame.actionPerformed(new ActionEvent(this.frame.txtInput, ActionEvent.ACTION_PERFORMED, "bgColor"));

 assertEquals(newColor, this.frame.showTxt.getBackground());
 }

 @Test
 public void testActionPerformedWhisper() {
 String wUser = "/w User1";
 this.frame.txtInput.setText(wUser);
 this.frame.actionPerformed(new ActionEvent(this.frame.txtInput, ActionEvent.ACTION_PERFORMED, "sendInput"));

 assertEquals(wUser, this.frame.getDoc().getText(0, this.frame.getDoc().getLength()).substring(0, this.frame.getDoc().getLength() - 1));
 }

 @Test
 public void testMouseClicked() {
 MouseEvent event = new MouseEvent(this.frame, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false);
 this.frame.maybeShowPopup(event);

 assertTrue(this.listPop.isVisible());
 }
}