package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InternalChatFrameTest {

 InternalChatFrame icf;
 MainFrame parentMock = new MainFrame() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do nothing
 }
 };

 @Test
 public void testInternalChatFrameConstruction() {
 icf = new InternalChatFrame(parentMock);
 assertNotNull(icf);
 }

 @Test
 public void testAddText() {
 String textToAdd = "You: Hello, World!";
 icf = new InternalChatFrame(parentMock);
 icf.addText(textToAdd);
 assertEquals(textToAdd, icf.getChatlog().substring(0, icf.getChatlog().lastIndexOf("\n") + textToAdd.length()));
 }

 @Test
 public void testAddStylesToDocument() {
 icf = new InternalChatFrame(parentMock);
 icf.addStylesToDocument(icf.doc);
 assertNotNull(icf.doc.getStyle("regular"));
 assertNotNull(icf.doc.getStyle("italic"));
 assertNotNull(icf.doc.getStyle("bold"));
 assertNotNull(icf.doc.getStyle("bold-italic"));
 assertNotNull(icf.doc.getStyle("name"));
 assertNotNull(icf.doc.getStyle("system"));
 assertNotNull(icf.doc.getStyle("small"));
 assertNotNull(icf.doc.getStyle("large"));
 }

 @Test
 public void testChangeStyle() {
 icf = new InternalChatFrame(parentMock);
 icf.changeStyle(12, Color.BLUE, false, false, "name");
 Style nameStyle = icf.doc.getStyle("name");
 assertEquals(Color.BLUE, nameStyle.getBackground());
 assertFalse(nameStyle.isBold());
 assertFalse(nameStyle.isItalic());
 }

 @Test
 public void testAddUser() {
 icf = new InternalChatFrame(parentMock);
 icf.addUser("User1");
 assertEquals(1, icf.getUserlist().length);
 assertEquals("User1", icf.getUserlist()[0]);
 }

 @Test
 public void testDeleteUser() {
 icf = new InternalChatFrame(parentMock);
 icf.addUser("User1");
 icf.addUser("User2");
 icf.deleteUser("User1");
 assertEquals(1, icf.getUserlist().length);
 assertNull(Arrays.asList(icf.getUserlist()).contains("User1"));
 }

 @Test
 public void testGetUserlist() {
 icf = new InternalChatFrame(parentMock);
 icf.addUser("User1");
 icf.addUser("User2");
 String[] users = icf.getUserlist();
 assertEquals(2, users.length);
 assertEquals("User1", users[0]);
 assertEquals("User2", users[1]);
 }
}