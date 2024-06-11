package dsachat.client.gui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseEventListeners;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JPopupMenu listPop;
 private JPopupMenu txtPop;

 @Before
 public void setUp() {
 chatFrame = new InternalChatFrame(new MainFrame()) {
 @Override
 public void createGUI() {
 super.createGUI();
 list = (JList) this.getComponent(0);
 showTxt = (JTextPane) this.getComponent(1);
 listPop = (JPopupMenu) this.getComponent(2);
 txtPop = (JPopupMenu) this.getComponent(3);
 }
 };
 SwingUtilities.invokeLater(() -> chatFrame.setVisible(true));
 }

 @Test
 public void testCreateGui() {
 assertNotNull(chatFrame);
 assertNotNull(list);
 assertNotNull(showTxt);
 assertNotNull(listPop);
 assertNotNull(txtPop);
 }

 @Test
 public void testAddText() {
 final String line1 = "You: Hello";
 final String line2 = "@user: Hi";
 chatFrame.addText(line1);
 chatFrame.addText(line2);

 String chatlog = chatFrame.getChatlog();
 List<String> lines = Arrays.asList(line1, line2);

 assertEquals(lines.toString(), chatlog);
 }

 @Test
 public void testChangeStyle() {
 final String name = "user";
 final int fontSize = 12;
 final Color color = new Color(1, 1, 1);
 final boolean isBold = true;
 final boolean isItalic = false;
 chatFrame.changeStyle(fontSize, color, isBold, isItalic, name);

 Style style = chatFrame.doc.getStyle(name);
 assertEquals(fontSize, StyleConstants.getFontSize(style));
 assertEquals(color, StyleConstants.getForeground(style));
 assertEquals(isBold, StyleConstants.isBold(style));
 assertEquals(isItalic, StyleConstants.isItalic(style));
 }

 @Test
 public void testAddUser() {
 final String user1 = "user1";
 final String user2 = "user2";
 chatFrame.addUser(user1);
 chatFrame.addUser(user2);

 assertEquals(2, chatFrame.listModel.size());
 assertEquals(user1, chatFrame.listModel.get(0));
 assertEquals(user2, chatFrame.listModel.get(1));
 }

 @Test
 public void testDeleteUser() {
 final String user1 = "user1";
 final String user2 = "user2";
 chatFrame.addUser(user1);
 chatFrame.addUser(user2);
 chatFrame.deleteUser(user1);

 assertEquals(1, chatFrame.listModel.size());
 assertFalse(chatFrame.listModel.contains(user1));
 }

 @Test
 public void testGetUserlist() {
 final String user1 = "user1";
 final String user2 = "user2";
 chatFrame.addUser(user1);
 chatFrame.addUser(user2);

 String[] userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 assertEquals(user1, userlist[0]);
 assertEquals(user2, userlist[1]);
 }

 @Test
 public void testMouseEvent() {
 final MouseEvent mouseEvent = new MouseEvent(list, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 10, 10, 1, false);
 chatFrame.maybeShowPopup(mouseEvent);
 assertTrue(listPop.isVisible());
 listPop.setVisible(false);

 final MouseEvent mouseEvent2 = new MouseEvent(showTxt, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 20, 20, 1, false);
 chatFrame.maybeShowPopup(mouseEvent2);
 assertTrue(txtPop.isVisible());
 txtPop.setVisible(false);
 }

 @Test
 public void testActionPerformed() {
 final String name = "user";
 final String colorString = "#FF0000";
 final Color color = new Color(Integer.parseInt(colorString.substring(1), 16));

 chatFrame.addUser(name);

 final MouseEvent mouseEvent = new MouseEvent(list, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 10, 10, 1, false);
 chatFrame.list.dispatchEvent(mouseEvent);

 final JColorChooser dialog = new JColorChooser();
 dialog.setColor(Color.BLACK);
 final JFrame frame = new JFrame();
 dialog.showDialog(frame, "Choose Color for user " + name, null);
 final Color newColor = dialog.getColor();

 List<MouseEvent> events = Arrays.asList(mouseEvent);
 SwingUtilities.invokeLater(() -> {
 chatFrame.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
 for (MouseEvent event : events) {
 chatFrame.mouseReleased(event);
 }
 });

 assertEquals(color, chatFrame.getStyle("user").getBackground());
 }
}