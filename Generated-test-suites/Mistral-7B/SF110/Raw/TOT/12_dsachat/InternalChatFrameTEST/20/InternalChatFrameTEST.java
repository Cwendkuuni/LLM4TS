package dsachat.client.gui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private JFrame frame;
 private InternalChatFrame chatFrame;

 @Before
 public void setup() {
 JFrame.setDefaultLookAndFeelDecorated(true);
 frame = new JFrame();
 chatFrame = new InternalChatFrame(frame);
 frame.getContentPane().add(chatFrame);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> chatFrame.addUser("User1"));
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testAddUser() {
 assertEquals("User1", chatFrame.getUserlist()[0]);
 JList<String> list = chatFrame.list;
 list.ensureIndexIsVisible(0);
 list.setSelectedValue("User1", false);
 assertFalse(list.isSelectionEmpty());
 }

 @Test
 public void testAddText() {
 JTextPane textPane = chatFrame.showTxt;
 chatFrame.addText("User1: Hello World!");
 String text = textPane.getText();
 assertTrue(text.contains("User1: Hello World!"));
 }

 @Test
 public void testAddStylesToDocument() {
 JTextPane textPane = chatFrame.showTxt;
 textPane.setDocument(chatFrame.doc);
 Style regular = chatFrame.doc.getStyle("regular");
 Font regularFont = StyleConstants.getFontFamily(regular);
 assertEquals("SansSerif", regularFont.getName());
 }

 @Test
 public void testChangeStyle() {
 Style nameStyle = chatFrame.doc.getStyle("name");
 chatFrame.changeStyle(12, Color.RED, true, false, "User1");
 assertEquals(Color.RED, StyleConstants.getForeground(nameStyle));
 }

 @Test
 public void testDeleteUser() {
 String[] userlist = chatFrame.getUserlist();
 assertEquals(1, userlist.length);
 chatFrame.deleteUser("User1");
 assertNull(chatFrame.list.getSelectedValue());
 }

 @Test
 public void testGetChatlog() {
 JTextPane textPane = chatFrame.showTxt;
 chatFrame.addText("User1: Hello World!");
 chatFrame.addText("User2: Hi!");
 String chatlog = chatFrame.getChatlog();
 assertTrue(chatlog.contains("User1: Hello World!"));
 assertTrue(chatlog.contains("User2: Hi!"));
 }

 @Test
 public void testMouseEvent() {
 JList<String> list = chatFrame.list;
 list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 MouseEvent mouseEvent = new MouseEvent(list, 1, 1, 0, 1, 1, 1, false);
 chatFrame.maybeShowPopup(mouseEvent);
 assertNotNull(chatFrame.listPop);
 }

}