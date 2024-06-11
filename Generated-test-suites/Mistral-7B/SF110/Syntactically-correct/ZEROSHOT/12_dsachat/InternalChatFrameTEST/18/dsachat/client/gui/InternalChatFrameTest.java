package dsachat.client.gui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private MainFrame parent;
 private InternalChatFrame chatFrame;

 @Before
 public void setUp() {
 parent = new MainFrame();
 chatFrame = new InternalChatFrame(parent);
 }

 @After
 public void tearDown() {
 parent.dispose();
 chatFrame.dispose();
 }

 @Test
 public void testCreateGUI() {
 // Test if the frame is created correctly
 assertNotNull(chatFrame);
 assertEquals("Chat", chatFrame.getTitle());
 assertFalse(chatFrame.isResizable());

 // Test the components inside the frame
 GridBagConstraints c = new GridBagConstraints();
 JSplitPane split = (JSplitPane) chatFrame.getContentPane();
 JList list = (JList) split.getLeftComponent();
 JTextPane showTxt = (JTextPane) split.getRightComponent().getComponent(0);
 JTextField txtInput = (JTextField) split.getRightComponent().getComponent(1);

 // Test the list
 assertEquals(0, list.getSelectedIndex());
 assertNotNull(list.getModel());
 assertInstanceOf(DefaultListModel.class, list.getModel());

 // Test the text pane
 assertNotNull(showTxt);
 assertFalse(showTxt.isEditable());

 // Test the text field
 assertNotNull(txtInput);
 assertNull(txtInput.getText());

 // Test the action command of the text field
 assertEquals("sendInput", txtInput.getActionCommand());
 }

 @Test
 public void testAddText() throws BadLocationException {
 // Create a test document
 Document doc = new DefaultStyledDocument();

 // Test adding a simple text
 chatFrame.addStylesToDocument(doc);
 chatFrame.addText(chatFrame, "Test: Hello", doc);
 assertEquals("Test: Hello\n", showTxt.getText());

 // Test adding a formatted text
 StyleConstants.setBold(doc.getStyle("system"), true);
 chatFrame.addText(chatFrame, "Test:**Bold**:Hello", doc);
 assertEquals("Test:**Bold**:Hello\nTest: Hello\n", showTxt.getText());

 // Test adding an italic text
 StyleConstants.setItalic(doc.getStyle("system"), true);
 chatFrame.addText(chatFrame, "Test:*Italic*:Hello", doc);
 assertEquals("Test:**Bold**:Hello\nTest:*Italic*:Hello\nTest: Hello\n", showTxt.getText());

 // Test adding a bold and italic text
 chatFrame.addText(chatFrame, "Test:**Bold*:*Italic*:Hello", doc);
 assertEquals("Test:**Bold**:Hello\nTest:**Bold*:*Italic*:Hello\nTest:*Italic*:Hello\nTest: Hello\n", showTxt.getText());
 }

 @Test
 public void testChangeStyle() {
 // Test changing the text color
 chatFrame.changeStyle(12, Color.RED, false, false, "user1");
 StyleConstants.setForeground(chatFrame.doc.getStyle("user1"), Color.RED);
 assertEquals("user1", chatFrame.doc.getStyle("user1").getName());
 assertEquals(Color.RED, chatFrame.doc.getStyle("user1").getForeground());

 // Test changing the background color
 chatFrame.changeStyle(12, null, false, false, "user1");
 assertEquals("user1", chatFrame.doc.getStyle("user1").getName());
 assertEquals(Color.WHITE, chatFrame.showTxt.getBackground());

 // Test changing the text bold
 chatFrame.changeStyle(12, null, true, false, "user1");
 assertEquals("user1", chatFrame.doc.getStyle("user1").getName());
 assertTrue(StyleConstants.isBold(chatFrame.doc.getStyle("user1")));

 // Test changing the text italic
 chatFrame.changeStyle(12, null, false, true, "user1");
 assertEquals("user1", chatFrame.doc.getStyle("user1").getName());
 assertTrue(StyleConstants.isItalic(chatFrame.doc.getStyle("user1")));
 }

 @Test
 public void testAddUser() {
 // Test adding a user
 chatFrame.addUser("user1");
 assertEquals(1, chatFrame.listModel.getSize());
 assertEquals("user1", chatFrame.listModel.getElementAt(0));
 assertEquals("user1", chatFrame.doc.getStyle(chatFrame.listModel.getElementAt(0)).getName());

 // Test adding multiple users
 chatFrame.addUser("user2");
 chatFrame.addUser("user3");
 assertEquals(3, chatFrame.listModel.getSize());
 List<String> users = Arrays.asList(chatFrame.listModel.toArray());
 assertTrue(users.containsAll(Arrays.asList("user1", "user2", "user3")));
 }

 @Test
 public void testDeleteUser() {
 // Test deleting a user
 chatFrame.addUser("user1");
 chatFrame.addUser("user2");
 chatFrame.addUser("user3");
 assertEquals(3, chatFrame.listModel.getSize());
 chatFrame.deleteUser("user1");
 assertEquals(2, chatFrame.listModel.getSize());
 List<String> users = Arrays.asList(chatFrame.listModel.toArray());
 assertFalse(users.contains("user1"));
 }

 @Test
 public void testGetUserlist() {
 // Test getting the user list
 chatFrame.addUser("user1");
 chatFrame.addUser("user2");
 chatFrame.addUser("user3");
 String[] userlist = chatFrame.getUserlist();
 assertEquals(3, userlist.length);
 assertEquals("user1", userlist[0]);
 assertEquals("user2", userlist[1]);
 assertEquals("user3", userlist[2]);
 }

 @Test
 public void testActionPerformed() {
 // Test the "chooseColor" action
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 chatFrame.list.setSelectedValue("user1");
 chatFrame.listPop.getActionMap().get(ActionEvent.ACTION_PERFORM).actionPerformed(new ActionEvent(chatFrame, 0, "chooseColor"));
 }
 });
 assertEquals("user1", chatFrame.doc.getStyle("user1").getName());

 // Test the "bgColor" action
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 chatFrame.list.setSelectedValue("user1");
 JColorChooser chooser = new JColorChooser();
 chooser.setColor(Color.GREEN);
 chatFrame.txtPop.getActionMap().get(ActionEvent.ACTION_PERFORM).actionPerformed(new ActionEvent(chatFrame, 0, "bgColor", chooser.getSelectedColor(), 0));
 }
 });
 assertEquals(Color.GREEN, chatFrame.showTxt.getBackground());

 // Test the "whisper" action
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 chatFrame.list.setSelectedValue("user1");
 chatFrame.txtInput.setText("/w user1 Test");
 chatFrame.actionPerformed(new ActionEvent(chatFrame.txtInput, 0, "sendInput"));
 }
 });
 assertEquals("/w user1 Test\n", chatFrame.getChatlog());
 }

 @Test
 public void testMouseClicked() {
 // Test the mouse clicked event
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 chatFrame.maybeShowPopup(new MouseEvent(chatFrame, 0, 0, 0, 0, 0, 0, false));
 }
 });
 assertTrue(chatFrame.listPop.isVisible());
 assertTrue(chatFrame.txtPop.isVisible());
 }
}