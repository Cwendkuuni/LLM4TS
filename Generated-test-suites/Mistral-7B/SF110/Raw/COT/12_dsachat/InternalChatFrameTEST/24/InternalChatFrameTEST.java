package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.MouseEventlistener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;

 @Before
 public void setUp() {
 chatFrame = new InternalChatFrame(new JFrame()) {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Do nothing
 }
 };
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @Test
 public void testAddText() {
 String line1 = "You: Hello";
 String line2 = "*User1: Hello You";
 String line3 = "*User1:* User2: Hi You";
 String line4 = "*User1* User2: Hi *You*";
 String line5 = "*User1 User2: Hi You";
 String line6 = "*User1: Hi You*";
 String line7 = "*User1: Hi You:";
 String line8 = "*User1: Hi You: Hello";
 String line9 = "*User1 *User2: Hi You";
 String line10 = "*User1: Hi You *User2";

 Style regular = new DefaultStyledDocument().getStyle("regular");
 Style italic = new DefaultStyledDocument().addStyle("italic", regular);
 Style bold = new DefaultStyledDocument().addStyle("bold", regular);
 Style boldItalic = new DefaultStyledDocument().addStyle("bold-italic", regular);
 Style name = new DefaultStyledDocument().addStyle("name", regular);

 try {
 chatFrame.addStylesToDocument(showTxt.getStyledDocument());
 }
 catch (BadLocationException e) {
 e.printStackTrace();
 }

 chatFrame.addText(line1);
 assertEquals(line1, showTxt.getText(0, showTxt.getCaretPosition()));
 assertEquals(1, list.getModel().getSize());

 Style userStyle = showTxt.getStyledDocument().getStyle("User1");
 assertEquals(StyleConstants.BOLD, userStyle.getAttributes().get(StyleConstants.Bold));
 assertEquals(StyleConstants.ITALIC, userStyle.getAttributes().get(StyleConstants.Italic));

 chatFrame.addText(line2);
 assertEquals(line1 + "\n" + line2, showTxt.getText(0, showTxt.getCaretPosition()));
 assertEquals(2, list.getModel().getSize());

 Style user1Style = showTxt.getStyledDocument().getStyle("User1");
 Style user2Style = showTxt.getStyledDocument().getStyle("User2");
 assertEquals(StyleConstants.ITALIC, user1Style.getAttributes().get(StyleConstants.Italic));
 assertEquals(StyleConstants.ITALIC, user2Style.getAttributes().get(StyleConstants.Italic));

 chatFrame.addText(line3);
 assertEquals(line1 + "\n" + line2 + "\n" + line3, showTxt.getText(0, showTxt.getCaretPosition()));
 assertEquals(3, list.getModel().getSize());

 chatFrame.addText(line4);
 assertEquals(line1 + "\n" + line2 + "\n" + line3 + "\n" + line4, showTxt.getText(0, showTxt.getCaretPosition()));
 assertEquals(3, list.getModel().getSize());

 chatFrame.addText(line5);
 assertEquals(line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5, showTxt.getText(0, showTxt.getCaretPosition()));
 assertEquals(4, list.getModel().getSize());

 chatFrame.addText(line6);
 assertEquals(line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5 + "\n" + line6, showTxt.getText(0, showTxt.getCaretPosition()));
 assertEquals(4, list.getModel().getSize());

 chatFrame.addText(line7);
 assertEquals(line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5 + "\n" + line6 + "\n" + line7, showTxt.getText(0, showTxt.getCaretPosition()));
 assertEquals(5, list.getModel().getSize());

 chatFrame.addText(line8);
 assertEquals(line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5 + "\n" + line6 + "\n" + line7 + "\n" + line8, showTxt.getText(0, showTxt.getCaretPosition()));
 assertEquals(6, list.getModel().getSize());

 chatFrame.addText(line9);
 assertEquals(line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5 + "\n" + line6 + "\n" + line7 + "\n" + line8 + "\n" + line9, showTxt.getText(0, showTxt.getCaretPosition()));
 assertEquals(6, list.getModel().getSize());

 chatFrame.addText(line10);
 assertEquals(line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5 + "\n" + line6 + "\n" + line7 + "\n" + line8 + "\n" + line9 + "\n" + line10, showTxt.getText(0, showTxt.getCaretPosition()));
 assertEquals(7, list.getModel().getSize());
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithBadLocationException() {
 chatFrame.addText("Invalid text position");
 }

 @Test
 public void testChangeStyle() {
 Style regular = new DefaultStyledDocument().getStyle("regular");
 Style italic = new DefaultStyledDocument().addStyle("italic", regular);
 Style bold = new DefaultStyledDocument().addStyle("bold", regular);
 Style boldItalic = new DefaultStyledDocument().addStyle("bold-italic", regular);
 Style name = new DefaultStyledDocument().addStyle("name", regular);

 chatFrame.changeStyle(12, Color.RED, true, true, "name");
 Style nameStyle = showTxt.getStyledDocument().getStyle("name");
 assertEquals(Color.RED, nameStyle.getBackground());
 assertEquals(StyleConstants.BOLD, nameStyle.getAttributes().get(StyleConstants.Bold));
 assertEquals(StyleConstants.ITALIC, nameStyle.getAttributes().get(StyleConstants.Italic));
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 assertEquals(1, list.getModel().getSize());
 assertEquals("User1", list.getModel().getElementAt(0));
 Style userStyle = showTxt.getStyledDocument().getStyle("User1");
 assertEquals(StyleConstants.BOLD, userStyle.getAttributes().get(StyleConstants.Bold));
 assertEquals(StyleConstants.ITALIC, userStyle.getAttributes().get(StyleConstants.Italic));

 chatFrame.addUser("User2");
 assertEquals(2, list.getModel().getSize());
 assertEquals("User2", list.getModel().getElementAt(1));
 Style user2Style = showTxt.getStyledDocument().getStyle("User2");
 assertEquals(StyleConstants.BOLD, user2Style.getAttributes().get(StyleConstants.Bold));
 assertEquals(StyleConstants.ITALIC, user2Style.getAttributes().get(StyleConstants.Italic));
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 list.setSelectedValue("User1", false);
 chatFrame.deleteUser();
 assertEquals(1, list.getModel().getSize());
 assertNull(list.getModel().getElementAt(0));
 }

 @Test
 public void testGetUserlist() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 List<String> userList = Arrays.asList("User1", "User2");
 assertArrayEquals(userList.toArray(), chatFrame.getUserlist());
 }

 @Test
 public void testMouseEvent() {
 JPopupMenu listPop = chatFrame.listPop;
 listPop.addMouseListener(new MouseEventAdapter() {
 @Override
 public void mousePressed(MouseEvent e) {
 listPop.setVisible(false);
 }
 });

 JPopupMenu txtPop = chatFrame.txtPop;
 txtPop.addMouseListener(new MouseEventAdapter() {
 @Override
 public void mousePressed(MouseEvent e) {
 txtPop.setVisible(false);
 }
 });

 MouseEvent listMouseEvent = new MouseEvent(list, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false);
 MouseEvent txtMouseEvent = new MouseEvent(showTxt, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false);

 chatFrame.maybeShowPopup(listMouseEvent);
 assertTrue(listPop.isVisible());
 chatFrame.maybeShowPopup(listMouseEvent);
 assertFalse(listPop.isVisible());

 chatFrame.maybeShowPopup(txtMouseEvent);
 assertTrue(txtPop.isVisible());
 chatFrame.maybeShowPopup(txtMouseEvent);
 assertFalse(txtPop.isVisible());
 }
}