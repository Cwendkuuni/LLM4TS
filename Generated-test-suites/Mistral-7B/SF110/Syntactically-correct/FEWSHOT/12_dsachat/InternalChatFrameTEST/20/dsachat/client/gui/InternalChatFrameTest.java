package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {

 InternalChatFrame icf = new InternalChatFrame(new MainFrame()); // Assuming MainFrame is initialized

 @Test
 public void testAddText() {
 String line = "You: Hello, World!";
 icf.addText(line);
 StyledDocument doc = icf.doc;
 String text = "";
 try {
 text = doc.getText(0, doc.getLength()).toString();
 } catch (BadLocationException e) {
 e.printStackTrace();
 }
 assertEquals(line, text);
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = icf.doc;
 Style defaultStyle = doc.getStyle("default");
 Style regularStyle = doc.getStyle("regular");
 Style italicStyle = doc.getStyle("italic");
 Style boldStyle = doc.getStyle("bold");
 Style boldItalicStyle = doc.getStyle("bold-italic");
 Style nameStyle = doc.getStyle("name");
 Style smallStyle = doc.getStyle("small");
 Style largeStyle = doc.getStyle("large");

 assertEquals(StyleContext.getDefaultStyleContext().getStyle("default"), defaultStyle);
 assertEquals(StyleContext.getDefaultStyleContext().getStyle("default"), regularStyle.getParent());
 assertTrue(italicStyle.isSubStyleOf(regularStyle));
 assertTrue(boldStyle.isSubStyleOf(regularStyle));
 assertTrue(boldItalicStyle.isSubStyleOf(regularStyle));
 assertEquals(Color.BLUE, nameStyle.getForeground());
 assertEquals(16, (int) nameStyle.getFontSize());
 assertEquals(Color.BLACK, defaultStyle.getFontColor());
 assertFalse(regularStyle.isBold());
 assertFalse(regularStyle.isItalic());
 }

 @Test
 public void testChangeStyle() {
 icf.changeStyle(12, Color.RED, true, true, "name");
 Style nameStyle = icf.doc.getStyle("name");
 assertEquals(Color.RED, nameStyle.getForeground());
 assertTrue(nameStyle.isBold());
 assertTrue(nameStyle.isItalic());
 }

 @Test
 public void testAddUser() {
 icf.addUser("User1");
 assertEquals(1, icf.listModel.size());
 assertEquals("User1", icf.listModel.get(0));
 }

 @Test
 public void testDeleteUser() {
 icf.addUser("User1");
 icf.addUser("User2");
 icf.deleteUser("User1");
 assertEquals(1, icf.listModel.size());
 assertFalse(icf.listModel.contains("User1"));
 }

 @Test
 public void testGetUserlist() {
 icf.addUser("User1");
 icf.addUser("User2");
 String[] userList = icf.getUserlist();
 assertEquals(2, userList.length);
 assertEquals("User1", userList[0]);
 assertEquals("User2", userList[1]);
 }
}