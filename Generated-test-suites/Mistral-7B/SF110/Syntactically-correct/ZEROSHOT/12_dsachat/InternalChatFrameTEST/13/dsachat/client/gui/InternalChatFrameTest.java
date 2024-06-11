package dsachat.client.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private MainFrame parent;
 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;

 @Before
 public void setUp() {
 parent = new MainFrame();
 chatFrame = new InternalChatFrame(parent);
 list = chatFrame.getList();
 showTxt = chatFrame.getShowTxt();
 txtInput = chatFrame.getTxtInput();
 }

 @Test
 public void testCreateGUI() {
 // Test if InternalChatFrame is created correctly
 assertEquals("Chat", chatFrame.getTitle());
 assertFalse(chatFrame.isResizable());
 assertFalse(chatFrame.isFocusable());
 assertFalse(chatFrame.isClosable());
 assertFalse(chatFrame.isIconifiable());

 JSplitPane split = (JSplitPane) chatFrame.getContentPane();
 assertSame(JSplitPane.HORIZONTAL_SPLIT, split.getOrientation());

 JScrollPane scrList = (JScrollPane) split.getLeftComponent();
 assertInstanceOf(JScrollPane.class, scrList);
 assertSame(list, scrList.getViewport().getView());

 JPanel right = (JPanel) split.getRightComponent();
 assertInstanceOf(JPanel.class, right);

 JScrollPane scrTxt = (JScrollPane) right.getComponent(0);
 assertInstanceOf(JScrollPane.class, scrTxt);
 assertSame(showTxt, scrTxt.getViewport().getView());

 JTextField txtField = (JTextField) right.getComponent(1);
 assertInstanceOf(JTextField.class, txtField);
 assertSame(txtInput, txtField);
 }

 @Test
 public void testAddText() {
 // Test if addText method correctly adds text to the text pane
 String testText = "Test: You : Hello World! :bold, italic, color(red)";
 chatFrame.addText(testText);

 try {
 StyledDocument doc = showTxt.getStyledDocument();
 String text = doc.getText(0, doc.getLength());
 String expectedText = "Test:\nYou : Hello World!\n";
 assertEquals(expectedText, text);
 } catch (BadLocationException e) {
 e.printStackTrace();
 fail("BadLocationException was thrown");
 }
 }

 @Test
 public void testAddStylesToDocument() {
 // Test if addStylesToDocument method correctly adds styles to the document
 StyledDocument doc = chatFrame.getShowTxt().getStyledDocument();
 assertNotNull(doc.getStyle("regular"));
 assertNotNull(doc.getStyle("italic"));
 assertNotNull(doc.getStyle("bold"));
 assertNotNull(doc.getStyle("bold-italic"));
 assertNotNull(doc.getStyle("name"));
 assertNotNull(doc.getStyle("system"));
 }

 @Test
 public void testActionPerformed() {
 // Test if actionPerformed method correctly handles user actions
 ActionEvent event = new ActionEvent(list, ActionEvent.ACTION_PERFORMED, "chooseColor");
 chatFrame.actionPerformed(event);

 assertEquals("chooseColor", chatFrame.getSelectedActionCommand());

 event = new ActionEvent(list, ActionEvent.ACTION_PERFORMED, "bgColor");
 chatFrame.actionPerformed(event);

 assertEquals("bgColor", chatFrame.getSelectedActionCommand());
 }

 @Test
 public void testMouseClicked() {
 // Test if mouseClicked method correctly handles mouse clicks
 MouseEvent event = new MouseEvent(list, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 0, false);
 chatFrame.mouseClicked(event);

 assertTrue(chatFrame.isPopupMenuShowing());
 chatFrame.listPop.setVisible(false);
 }

 @Test
 public void testGetChatlog() {
 // Test if getChatlog method correctly returns the chat log as a string
 String testText = "Test: You : Hello World! :bold, italic, color(red)";
 chatFrame.addText(testText);
 String chatLog = chatFrame.getChatlog();
 String expectedChatLog = "Test:You : Hello World!\n";
 assertEquals(expectedChatLog, chatLog);
 }
}