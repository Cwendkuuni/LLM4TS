package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;

import org.junit.After;
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
 chatFrame = new InternalChatFrame(new MainFrame()) {
 @Override
 public void actionPerformed(ActionEvent e) {
 super.actionPerformed(e);
 }
 };
 SwingUtilities.invokeLater(() -> {
 try {
 chatFrame.setVisible(true);
 } catch (Exception e) {
 e.printStackTrace();
 }
 });
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(chatFrame::dispose);
 }

 @Test
 public void testCreateGUI() {
 // Check if the components are added correctly
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 // Check if the components are visible
 assertTrue(chatFrame.isVisible());
 // Check if the list is initialized with an empty model
 assertTrue(chatFrame.listModel.isEmpty());
 // Check if the list has a default renderer
 ListCellRenderer renderer = list.getCellRenderer();
 assertNotNull(renderer);
 // Check if the text pane has default styles
 assertEquals("default", chatFrame.doc.getStyleNames()[0]);
 }

 @Test
 public void testAddText() {
 // Test adding a simple message
 chatFrame.addText("John: Hello");
 String chatLog = chatFrame.getChatlog();
 assertEquals("Hello\n", chatLog);
 // Test adding a message with styles
 chatFrame.addText("*John:* Hello");
 chatLog = chatFrame.getChatlog();
 assertEquals("*John:* Hello\n", chatLog);
 // Test adding multiple messages with different styles
 chatFrame.addText("*John:* Hello");
 chatFrame.addText("Mary: Hi");
 chatFrame.addText("*John: *Hi Mary");
 chatFrame.addText("Mary: Bye");
 chatLog = chatFrame.getChatlog();
 assertEquals("*John:* Hello\nMary: Hi\n*John: *Hi Mary\nMary: Bye\n", chatLog);
 }

 @Test
 public void testAddUser() {
 // Test adding a user to the list model
 chatFrame.addUser("John");
 assertEquals(1, chatFrame.listModel.size());
 // Test adding a user with the same name
 chatFrame.addUser("John");
 assertEquals(1, chatFrame.listModel.size());
 // Test adding multiple users
 List<String> users = Arrays.asList("John", "Mary", "Peter");
 for (String user : users) {
 chatFrame.addUser(user);
 }
 assertEquals(3, chatFrame.listModel.size());
 // Test setting the selected user
 chatFrame.list.setSelectedIndex(1);
 assertEquals("Mary", chatFrame.list.getSelectedValue());
 }

 @Test
 public void testDeleteUser() {
 // Test deleting a user from the list model
 chatFrame.addUser("John");
 chatFrame.addUser("Mary");
 chatFrame.deleteUser("John");
 assertEquals(1, chatFrame.listModel.size());
 // Test deleting a non-existent user
 chatFrame.deleteUser("Peter");
 assertEquals(1, chatFrame.listModel.size());
 }

 @Test
 public void testGetUserlist() {
 // Test getting the user list from the list model
 chatFrame.addUser("John");
 chatFrame.addUser("Mary");
 String[] userList = chatFrame.getUserlist();
 assertEquals(2, userList.length);
 assertEquals("John", userList[0]);
 assertEquals("Mary", userList[1]);
 }

 @Test
 public void testChangeStyle() {
 // Test changing the font size, color, bold, and italic for a user
 chatFrame.addUser("John");
 chatFrame.changeStyle(14, null, false, false, "John");
 // Test getting the updated style for the user
 Style style = chatFrame.doc.getStyle("John");
 assertTrue(style.isDefined());
 StyleConstants.setFontSize(style, 14);
 assertEquals(14, style.getFontSize());
 // Test changing the font color for the user
 chatFrame.changeStyle(14, Color.RED, false, false, "John");
 assertEquals(Color.RED, style.getForeground());
 // Test changing the bold and italic for the user
 chatFrame.changeStyle(14, null, true, true, "John");
 assertTrue(style.isBold());
 assertTrue(style.isItalic());
 }

 @Test
 public void testActionPerformed() {
 // Test the "chooseColor" action
 chatFrame.listPop.setVisible(false);
 list.addMouseListener(e -> chatFrame.listPop.show(list, e.getX(), e.getY()));
 list.dispatchEvent(new MouseEvent(list, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false));
 JPopupMenu menu = chatFrame.listPop;
 JMenuItem chooseColorItem = menu.getComponents(JMenuItem.class)[0];
 chooseColorItem.doClick();
 Color newColor = JColorChooser.showDialog(chatFrame, "Choose Color for user John", Color.BLACK);
 assertNotNull(newColor);
 chatFrame.changeStyle(14, newColor, false, false, "John");
 // Test the "whisper" action
 txtInput.setText("/w John Hello");
 txtInput.dispatchEvent(new ActionEvent(txtInput, ActionEvent.ACTION_PERFORMED, "sendInput"));
 assertEquals("Hello", chatFrame.listModel.get(0));
 }

 @Test
 public void testMouseClicked() {
 // Test showing the popup menu when right-clicking on the text pane
 txtInput.addMouseListener(this::maybeShowPopup);
 showTxt.addMouseListener(this::maybeShowPopup);
 showTxt.dispatchEvent(new MouseEvent(showTxt, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false));
 JPopupMenu menu = chatFrame.txtPop;
 assertTrue(menu.isVisible());
 }

 private void maybeShowPopup(MouseEvent e) {
 if (e.isPopupTrigger() && (e.getSource() == showTxt || e.getSource() == list)) {
 chatFrame.maybeShowPopup(e);
 }
 }

}