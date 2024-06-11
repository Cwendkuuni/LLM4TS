package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;

public class InternalChatFrameTest {

 private InternalChatFrame frame;
 private JTextPane showTxt;
 private JList list;
 private JTextField txtInput;

 @Before
 public void setUp() {
 MainFrame parent = new MainFrame(); // Assume MainFrame is initialized
 frame = new InternalChatFrame(parent);
 showTxt = frame.getShowTxt();
 list = frame.getList();
 txtInput = frame.getTxtInput();
 }

 @Test
 public void testConstructor() {
 assertNotNull(frame);
 }

 @Test
 public void testCreateGUI() {
 // Check if components are added correctly
 assertEquals(2, frame.getContentPane().getComponentCount());
 JComponent split = (JComponent) frame.getContentPane().getComponent(0);
 assertTrue(split instanceof JSplitPane);
 JComponent right = (JComponent) split.getRightComponent();
 assertTrue(right instanceof JPanel);
 JComponent scrTxt = (JComponent) right.getComponent(0);
 assertTrue(scrTxt instanceof JScrollPane);
 JTextPane txtPane = (JTextPane) scrTxt.getViewportView();
 assertSame(txtPane, frame.getShowTxt());
 }

 @Test
 public void testAddText() throws Exception {
 String line = "You: Hello World";
 frame.addText(line);
 StyledDocument doc = showTxt.getStyledDocument();
 String text = doc.getText(0, doc.getLength());
 assertEquals(line, text);
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithException() throws Exception {
 frame.addText("Invalid line");
 fail("Expected BadLocationException but none was thrown.");
 }

 @Test
 public void testChangeStyle() {
 // Assuming changeStyle method is public
 frame.changeStyle(12, Color.RED, true, false, "name");
 Style nameStyle = frame.doc.getStyle("name");
 assertEquals(Color.RED, nameStyle.getForeground());
 }

 @Test
 public void testAddUser() {
 String user = "User1";
 frame.addUser(user);
 assertEquals(user, list.getModel().getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 String user = "User1";
 frame.addUser(user);
 String[] userlist = frame.getUserlist();
 assertEquals(1, userlist.length);
 frame.deleteUser(user);
 String[] updatedUserlist = frame.getUserlist();
 assertNull(Arrays.asList(updatedUserlist).contains(user));
 }

 @Test
 public void testActionPerformedWhisper() {
 String user = "/w User1 ";
 txtInput.setText(user);
 txtInput.doClick();
 assertEquals("/w User1 ", txtInput.getText());
 }

 @Test
 public void testMouseClicked() {
 MouseEvent mouseEvent = new MouseEvent(list, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, 0);
 frame.maybeShowPopup(mouseEvent);
 assertTrue(listPop.isVisible());
 }

 @Test
 public void testGetChatlog() {
 String expectedChatlog = "Expected Chatlog";
 showTxt.setText(expectedChatlog);
 assertEquals(expectedChatlog, frame.getChatlog());
 }

 @After
 public void tearDown() {
 frame.dispose();
 }
}