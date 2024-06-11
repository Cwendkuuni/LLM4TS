package dsachat.client.gui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.InternalChatFrame.Style;

public class InternalChatFrameTest {

 private InternalChatFrame internalChatFrame;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;

 @Before
 public void setUp() {
 internalChatFrame = new InternalChatFrame(new JFrame()) {
 @Override
 public void createGUI() {
 super.createGUI();
 list = (JList) this.list;
 showTxt = (JTextPane) this.showTxt;
 txtInput = (JTextField) this.txtInput;
 }
 };
 }

 @After
 public void tearDown() {
 if (internalChatFrame != null) {
 internalChatFrame.dispose();
 }
 }

 @Test
 public void testConstructor() {
 assertNotNull(internalChatFrame);
 }

 @Test
 public void testAddText() {
 final String line = "You : Hello";
 internalChatFrame.addText(line);
 final int lengthBefore = showTxt.getDocument().getLength();
 SwingUtilities.invokeLater(() -> {
 list.setSelectedIndex(0);
 list.ensureIndexIsVisible(0);
 list.validate();
 });
 final int lengthAfter = showTxt.getDocument().getLength();
 assertEquals(lengthAfter, lengthBefore + line.length() + 1);
 }

 @Test
 public void testAddTextWithInvalidLine() {
 final String line = "Invalid line";
 internalChatFrame.addText(line);
 assertNull(showTxt.getText());
 }

 @Test
 public void testAddTextWithTwoLines() {
 final String line1 = "You : Hello";
 final String line2 = "Other : World";
 internalChatFrame.addText(line1);
 internalChatFrame.addText(line2);
 final String expected = line1 + "\n" + line2;
 assertEquals(expected, internalChatFrame.getChatlog());
 }

 @Test
 public void testAddTextWithFormattedLine() {
 final String line = "*You* : *Hello* _World_";
 internalChatFrame.addText(line);
 final int lengthBefore = showTxt.getDocument().getLength();
 SwingUtilities.invokeLater(() -> {
 list.setSelectedIndex(0);
 list.ensureIndexIsVisible(0);
 list.validate();
 });
 final int lengthAfter = showTxt.getDocument().getLength();
 String actual = internalChatFrame.getChatlog();
 String expected = "*You*\n" + "*Hello*\n_World_";
 assertEquals(expected, actual.substring(0, lengthAfter - 1));
 }

 @Test
 public void testAddUser() {
 final String user = "User1";
 internalChatFrame.addUser(user);
 final int initialSize = list.getModel().getSize();
 assertEquals(initialSize + 1, list.getModel().getSize());
 assertEquals(user, list.getModel().getElementAt(initialSize));
 }

 @Test
 public void testDeleteUser() {
 final String user = "User1";
 internalChatFrame.addUser(user);
 internalChatFrame.deleteUser(user);
 final int initialSize = list.getModel().getSize();
 assertEquals(initialSize - 1, list.getModel().getSize());
 assertNull(list.getModel().getElementAt(initialSize));
 }

 @Test
 public void testGetUserlist() {
 final int size = 5;
 for (int i = 0; i < size; ++i) {
 internalChatFrame.addUser("User" + i);
 }
 List<String> userlist = internalChatFrame.getUserlist();
 assertEquals(size, userlist.size());
 }

 @Test
 public void testActionPerformedWhenChooseColor() {
 final String name = "User1";
 internalChatFrame.addUser(name);
 SwingUtilities.invokeLater(() -> {
 list.setSelectedValue(name, true);
 final Component component = list.getSelectedValue();
 JColorChooser.showDialog(internalChatFrame, "Choose Color for user " + name, Color.BLACK);
 });
 final Style style = internalChatFrame.doc.getStyle(name);
 final Color color = style.getForeground();
 assertEquals(Color.BLACK, color);
 }

 @Test
 public void testActionPerformedWhenBgColor() {
 final String name = "User1";
 internalChatFrame.addUser(name);
 SwingUtilities.invokeLater(() -> {
 list.setSelectedValue(name, true);
 final JTextPane textPane = (JTextPane) showTxt;
 JColorChooser.showDialog(internalChatFrame, "Choose Color for user " + name, Color.BLACK);
 textPane.setBackground(Color.RED);
 });
 final Color background = showTxt.getBackground();
 assertEquals(Color.RED, background);
 }

 @Test
 public void testActionPerformedWhenWhisper() {
 final String name = "User1";
 internalChatFrame.addUser(name);
 SwingUtilities.invokeLater(() -> {
 list.setSelectedValue(name, true);
 txtInput.setText("/w " + name + " Test");
 txtInput.doClick();
 txtInput.sendKeyEvent(new KeyEvent(txtInput, 1, 0, KeyEvent.VK_ENTER));
 });
 final String chatlog = internalChatFrame.getChatlog();
 final String expected = "/w User1 Test\n";
 assertEquals(expected, chatlog.substring(0, chatlog.length() - 1));
 }

 @Test
 public void testMousePressedOutsideComponent() {
 SwingUtilities.invokeLater(() -> {
 final MouseEvent mouseEvent = new MouseEvent(new Component() {}, 0, 1, 0, 1, 1, 0, MouseEvent.MOUSE_PRESSED);
 internalChatFrame.maybeShowPopup(mouseEvent);
 });
 assertNull(listPop);
 assertNull(txtPop);
 }

 @Test
 public void testMousePressedOnList() {
 SwingUtilities.invokeLater(() -> {
 final MouseEvent mouseEvent = new MouseEvent(list, 0, 1, 0, 1, 1, 0, MouseEvent.MOUSE_PRESSED);
 internalChatFrame.maybeShowPopup(mouseEvent);
 });
 assertNotNull(listPop);
 }

 @Test
 public void testMousePressedOnTextPane() {
 SwingUtilities.invokeLater(() -> {
 final MouseEvent mouseEvent = new MouseEvent(showTxt, 0, 1, 0, 1, 1, 0, MouseEvent.MOUSE_PRESSED);
 internalChatFrame.maybeShowPopup(mouseEvent);
 });
 assertNotNull(txtPop);
 }

 @Test(expected = NullPointerException.class)
 public void testNullListModel() {
 final InternalChatFrame internalChatFrame = new InternalChatFrame(new JFrame()) {
 @Override
 public void createGUI() {
 super.createGUI();
 list = null;
 }
 };
 internalChatFrame.addText("Test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeListIndex() {
 final InternalChatFrame internalChatFrame = new InternalChatFrame(new JFrame());
 internalChatFrame.addUser("User1");
 SwingUtilities.invokeLater(() -> {
 final ListSelectionModel listSelectionModel = list.getSelectionModel();
 listSelectionModel.setSelectionInterval(-1, -1);
 internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "chooseColor"));
 });
 }

 @Test(expected = HeadlessException.class)
 public void testHeadlessEnvironment() {
 final InternalChatFrame internalChatFrame = new InternalChatFrame(null);
 internalChatFrame.addText("Test");
 }

 @Test
 public void testGetChatlogWhenEmpty() {
 assertEquals("", internalChatFrame.getChatlog());
 }
}