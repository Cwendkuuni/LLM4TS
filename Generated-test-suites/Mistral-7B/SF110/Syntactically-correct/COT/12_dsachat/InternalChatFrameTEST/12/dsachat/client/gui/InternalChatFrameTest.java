package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame frame;
 private MainFrame parentFrame;

 @Before
 public void setUp() {
 parentFrame = new MainFrame();
 frame = new InternalChatFrame(parentFrame);
 frame.pack();
 JFrame testingFrame = new JFrame();
 testingFrame.setContentPane(frame);
 testingFrame.pack();
 testingFrame.setVisible(true);
 SwingUtilities.invokeLater(() -> frame.createGUI());
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(() -> testingFrame.dispose());
 SwingUtilities.invokeLater(() -> parentFrame.dispose());
 }

 @Test
 public void testInternalChatFrameConstructor() {
 assertEquals("Chat", frame.getTitle());
 assertNotNull(frame.getList());
 assertNotNull(frame.getShowTxt());
 assertNotNull(frame.getTextInput());
 assertNotNull(frame.getParent());
 }

 @Test
 public void testAddText() {
 String line = "You: Hello World";
 frame.addText(line);
 JTextPane textPane = frame.getShowTxt();
 String expected = "You: Hello World\n";
 assertEquals(expected, textPane.getText(0, textPane.getDocument().getLength()));
 }

 @Test
 public void testAddTextWithInvalidLineFormat() {
 String line = "Hello World";
 frame.addText(line);
 JTextPane textPane = frame.getShowTxt();
 String expected = "";
 assertEquals(expected, textPane.getText(0, textPane.getDocument().getLength()));
 }

 @Test
 public void testChangeStyle() {
 Style regular = frame.getDoc().getStyle("regular");
 frame.changeStyle(12, Color.RED, true, false, "user1");
 Style user1 = frame.getDoc().getStyle("user1");
 assertEquals(Color.RED, user1.getBackground());
 assertEquals(12, user1.getFontSize());
 }

 @Test
 public void testAddUser() {
 String user = "user1";
 frame.addUser(user);
 JList list = frame.getList();
 int expectedSize = 1;
 assertEquals(expectedSize, list.getModel().getSize());
 assertEquals(user, list.getSelectedValue());
 }

 @Test
 public void testAddUsers() {
 String user1 = "user1";
 String user2 = "user2";
 frame.addUser(user1);
 frame.addUser(user2);
 JList list = frame.getList();
 int expectedSize = 2;
 assertEquals(expectedSize, list.getModel().getSize());
 assertFalse(list.isSelectionEmpty());
 }

 @Test
 public void testGetUserlist() {
 String[] expected = new String[]{"user1", "user2"};
 String[] actual = frame.getUserlist();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testDeleteUser() {
 String user = "user1";
 frame.addUser(user);
 frame.deleteUser(user);
 JList list = frame.getList();
 int expectedSize = 0;
 assertEquals(expectedSize, list.getModel().getSize());
 }

 @Test
 public void testMouseClicked() {
 MouseEvent mouseEvent = new MouseEvent(frame, 1, 1, 1, 1, 1, 1, false);
 frame.maybeShowPopup(mouseEvent);
 assertFalse(frame.listPop.isVisible());
 assertFalse(frame.txtPop.isVisible());
 }

 @Test
 public void testSelectUser() {
 String user = "user1";
 frame.addUser(user);
 frame.getList().setSelectedValue(user, true);
 assertTrue(frame.getShowTxt().isEditable());
 }

 @Test
 public void testChangeColor() {
 Color newColor = Color.BLUE;
 frame.getList().addMouseListener(e -> {
 if (e.getSource().equals(frame.getList())) {
 JColorChooser colorChooser = new JColorChooser();
 colorChooser.setColor(newColor);
 colorChooser.showDialog(frame, "Change Color");
 }
 });
 frame.getList().addMouseEvent(new MouseEvent(frame.getList(), 1, 1, 1, 1, 1, 1, false));
 assertTrue(frame.listPop.isVisible());
 JColorChooser colorChooser = JColorChooser.showDialog(frame, "Choose Color for user user1", newColor);
 assertTrue(colorChooser != null);
 assertTrue(frame.getList().getSelectedValue().equals("user1"));
 }

 @Test(expected = AssertionError.class)
 public void testExceptionHandlingBadLocationException() {
 try {
 frame.addText("Hello World");
 frame.addText("Hello World");
 } catch (BadLocationException e) {
 fail("BadLocationException should not be thrown");
 }
 }

}