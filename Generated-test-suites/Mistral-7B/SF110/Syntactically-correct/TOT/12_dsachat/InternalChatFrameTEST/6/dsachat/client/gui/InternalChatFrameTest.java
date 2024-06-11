package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private JFrame frame;
 private MainFrame parent;
 private InternalChatFrame chatFrame;

 @Before
 public void setUp() {
 parent = new MainFrame();
 frame = new JFrame();
 chatFrame = new InternalChatFrame(parent);
 frame.getContentPane().add(chatFrame);
 frame.pack();
 SwingUtilities.invokeLater(() -> frame.setVisible(true));
 }

 @Test
 public void testAddText() {
 final String text = "You: Hello World";
 chatFrame.addText(text);
 final String expected = "You: Hello World\n";
 final String actual = chatFrame.getChatlog();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddTextWithInvalidFormat() {
 final String text = "Hello World";
 chatFrame.addText(text);
 final String expected = "";
 final String actual = chatFrame.getChatlog();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddTextWithChangeColorStyle() {
 final String text = "*user1: *Hello World*";
 chatFrame.addText(text);
 final int fontSize = 12;
 final Color color = new Color(255, 0, 0);
 final boolean isBold = true;
 final boolean isItalic = true;
 final String name = "user1";
 chatFrame.changeStyle(fontSize, color, isBold, isItalic, name);
 final String expected = "*user1:*<font color=#ff0000>*Hello World*</font>\n";
 final String actual = chatFrame.getChatlog();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddUsers() {
 final String[] users = {"user1", "user2", "user3"};
 for (String user : users) {
 chatFrame.addUser(user);
 }
 final List<String> expectedUsers = Arrays.asList("user1", "user2", "user3");
 final List<String> actualUsers = Arrays.asList(chatFrame.getUserlist());
 assertEquals(expectedUsers, actualUsers);
 }

 @Test
 public void testDeleteUser() {
 final String[] users = {"user1", "user2", "user3"};
 chatFrame.addUser(users[0]);
 chatFrame.addUser(users[1]);
 chatFrame.addUser(users[2]);
 final int index = 1;
 chatFrame.deleteUser(users[index]);
 final String[] expectedUsers = {"user1", "user3"};
 final String[] actualUsers = chatFrame.getUserlist();
 assertNotSame(expectedUsers, actualUsers);
 assertEquals(expectedUsers[0], actualUsers[0]);
 assertEquals(expectedUsers[1], actualUsers[1]);
 }

 @Test
 public void testChangeColorBackground() {
 final Color color = new Color(255, 0, 0);
 chatFrame.changeStyle(0, null, false, false, "background");
 final JTextPane txtPane = chatFrame.getShowTxt();
 final Color expectedColor = color;
 final Color actualColor = txtPane.getBackground();
 assertEquals(expectedColor, actualColor);
 }

 @Test
 public void testListSelection() {
 final String[] users = {"user1", "user2", "user3"};
 for (String user : users) {
 chatFrame.addUser(user);
 }
 chatFrame.listSelectionChanged(new ListSelectionEvent(chatFrame.getList(), 0, 0, false));
 final String expected = "user1";
 final String actual = chatFrame.list.getSelectedValue().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testMouseEvent() {
 final MouseEvent mouseEvent = new MouseEvent(chatFrame.getShowTxt(), 1, 1, 1, 1, 1, 1, false);
 chatFrame.maybeShowPopup(mouseEvent);
 assertNull(chatFrame.listPop.isVisible());
 assertNull(chatFrame.txtPop.isVisible());
 }
}