package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame frame;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = new MainFrame();
 frame = new InternalChatFrame(parent);
 }

 @Test
 public void testConstructor() {
 assertEquals("Chat", frame.getTitle());
 assertNotNull(frame.list);
 assertNotNull(frame.showTxt);
 assertNotNull(frame.txtInput);
 }

 @Test
 public void testCreateGUI() {
 assertTrue(frame.isVisible());
 assertTrue(frame.list.isOpaque());
 assertTrue(frame.showTxt.isOpaque());
 assertTrue(frame.txtInput.isOpaque());
 }

 @Test
 public void testAddText() {
 final String line = "You: Hello World";
 frame.addText(line);
 String text = frame.showTxt.getText();
 assertTrue(text.contains(line));
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextBadLocationException() {
 final String line = "You: Hello World";
 frame.addText(line);
 frame.addText(line);
 }

 @Test
 public void testAddStylesToDocument() {
 final StyledDocument doc = new SimpleAttributeSet().getDocument();
 frame.addStylesToDocument(doc);
 assertEquals(15, doc.getStyleCount());
 }

 @Test
 public void testChangeStyle() {
 final String name = "testUser";
 frame.addUser(name);
 frame.changeStyle(12, Color.BLUE, false, false, name);
 assertEquals(Color.BLUE, frame.doc.getStyle(name).getForeground());
 }

 @Test
 public void testAddUser() {
 String[] users = frame.getUserlist();
 assertEquals(0, users.length);
 frame.addUser("testUser");
 users = frame.getUserlist();
 assertEquals(1, users.length);
 assertEquals("testUser", users[0]);
 }

 @Test
 public void testDeleteUser() {
 String[] users = frame.getUserlist();
 frame.addUser("testUser1");
 frame.addUser("testUser2");
 assertEquals(2, users.length);
 frame.deleteUser("testUser1");
 users = frame.getUserlist();
 assertEquals(1, users.length);
 assertEquals("testUser2", users[0]);
 }

 @Test
 public void testGetChatlog() {
 final String line = "You: Hello World";
 frame.addText(line);
 assertEquals(line, frame.getChatlog());
 }

 @Test
 public void testMouseClicked() {
 final MouseEvent event = new MouseEvent(frame, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 0, false);
 frame.maybeShowPopup(event);
 assertNull(frame.listPop);
 assertNull(frame.txtPop);
 }
}