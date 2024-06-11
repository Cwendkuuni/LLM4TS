package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame icf;

 @Test
 public void testInitialization() {
 icf = new InternalChatFrame(new MainFrame());
 assertNotNull(icf.list);
 assertNotNull(icf.showTxt);
 assertNotNull(icf.txtInput);
 assertNull(icf.doc);
 assertNull(icf.listPop);
 assertNull(icf.txtPop);
 }

 @Test
 public void testAddText() {
 icf = new InternalChatFrame(new MainFrame());
 icf.addText("Alice: Hello");
 assertEquals("Hello\n", icf.getChatlog());
 icf.addText("Bob: Hi");
 assertEquals("Hello\nHi\n", icf.getChatlog());
 icf.addText("Alice: *Hi Bob*");
 assertEquals("Hello\nHi\n*Hi Bob*\n", icf.getChatlog());
 icf.addText("Alice: *Hi Bob* +this+ is a test");
 assertEquals("Hello\nHi\n*Hi Bob*\n*Hi Bob* +this+ is a test\n", icf.getChatlog());
 }

 @Test
 public void testChangeStyle() {
 icf = new InternalChatFrame(new MainFrame());
 icf.changeStyle(12, Color.RED, true, true, "Alice");
 assertEquals(Color.RED, icf.doc.getStyle("Alice").getBackground());
 }

 @Test
 public void testAddUser() {
 icf = new InternalChatFrame(new MainFrame());
 icf.addUser("Alice");
 assertEquals(1, icf.listModel.size());
 assertEquals("Alice", icf.listModel.get(0));
 }

 @Test
 public void testDeleteUser() {
 icf = new InternalChatFrame(new MainFrame());
 icf.addUser("Alice");
 icf.addUser("Bob");
 assertEquals(2, icf.listModel.size());
 icf.deleteUser("Alice");
 assertEquals(1, icf.listModel.size());
 assertEquals("Bob", icf.listModel.get(0));
 }

 @Test
 public void testGetUserlist() {
 icf = new InternalChatFrame(new MainFrame());
 icf.addUser("Alice");
 icf.addUser("Bob");
 String[] users = icf.getUserlist();
 assertEquals(2, users.length);
 assertEquals("Alice", users[0]);
 assertEquals("Bob", users[1]);
 }
}