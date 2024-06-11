package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InternalChatFrameTest {

 InternalChatFrame icf = null;
 MainFrame parent = null; // Assuming MainFrame is instantiated and accessible

 @Before
 public void setUp() {
 parent = new MainFrame(); // Assuming MainFrame has a no-args constructor
 icf = new InternalChatFrame(parent);
 }

 @Test
 public void testAddText() {
 String line = "You: Hello World";
 icf.addText(line);
 Style nameStyle = icf.doc.getStyle("name");
 assertEquals("Hello World", icf.doc.getText(0, icf.doc.getLength()));
 assertEquals(line, icf.listModel.getElementAt(0));
 assertEquals(Color.BLUE, nameStyle.getForeground());
 }

 @Test
 public void testAddTextWithBoldItalic() {
 String line = "*You* _Hello World_";
 icf.addText(line);
 Style nameStyle = icf.doc.getStyle("name");
 assertEquals("*You* _Hello World_", icf.doc.getText(0, icf.doc.getLength()));
 Style boldStyle = icf.doc.getStyle("bold-italic");
 assertEquals(StyleConstants.BOLD | StyleConstants.ITALIC, boldStyle.getAttributes().getBold() | boldStyle.getAttributes().getItalic());
 }

 @Test
 public void testAddTextWithBold() {
 String line = "+You+ Hello World";
 icf.addText(line);
 Style nameStyle = icf.doc.getStyle("name");
 assertEquals("+You+ Hello World", icf.doc.getText(0, icf.doc.getLength()));
 Style boldStyle = icf.doc.getStyle("bold");
 assertEquals(StyleConstants.BOLD, boldStyle.getAttributes().getBold());
 }

 @Test
 public void testAddTextWithItalic() {
 String line = "_You_ Hello World";
 icf.addText(line);
 Style nameStyle = icf.doc.getStyle("name");
 assertEquals("_You_ Hello World", icf.doc.getText(0, icf.doc.getLength()));
 Style italicStyle = icf.doc.getStyle("italic");
 assertEquals(StyleConstants.ITALIC, italicStyle.getAttributes().getItalic());
 }

 @Test
 public void testAddTextWithColor() {
 String line = "User: " + Color.RED.getName() + ": Hello World";
 icf.addText(line);
 Style userStyle = icf.doc.getStyle("User");
 assertEquals(Color.RED, userStyle.getForeground());
 }

 @Test
 public void testAddUser() {
 icf.addUser("TestUser");
 assertEquals(1, icf.listModel.size());
 assertEquals("TestUser", icf.listModel.getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 icf.addUser("TestUser1");
 icf.addUser("TestUser2");
 icf.deleteUser("TestUser1");
 assertEquals(1, icf.listModel.size());
 assertNull(icf.listModel.getElementAt(0));
 }

 @Test
 public void testGetUserlist() {
 icf.addUser("TestUser1");
 icf.addUser("TestUser2");
 String[] userlist = icf.getUserlist();
 assertEquals(2, userlist.length);
 assertEquals("TestUser1", userlist[0]);
 assertEquals("TestUser2", userlist[1]);
 }

 @Test
 public void testChangeStyle() {
 icf.changeStyle(14, Color.GREEN, false, false, "name");
 Style nameStyle = icf.doc.getStyle("name");
 assertEquals(Color.GREEN, nameStyle.getForeground());
 icf.changeStyle(14, null, false, false, "name");
 Style nameStyle2 = icf.doc.getStyle("name");
 assertNotNull(nameStyle2.getForeground()); // Default color is used
 }

 @Test
 public void testActionPerformed() {
 icf.addText("You: Test");
 icf.addUser("TestUser");
 icf.list.setSelectedValue("TestUser", false);
 icf.actionPerformed(new ActionEvent(icf.list, ActionEvent.ACTION_PERFORMED, "chooseColor"));
 icf.changeStyle(14, Color.CYAN, false, false, "TestUser");
 assertEquals(Color.CYAN, icf.showTxt.getBackground());
 }

 @Test
 public void testWhisper() {
 icf.addText("You: Test");
 icf.addUser("TestUser");
 icf.txtInput.setText("/w TestUser Test Whisper");
 icf.actionPerformed(new ActionEvent(icf.txtInput, ActionEvent.ACTION_PERFORMED, "sendInput"));
 String whisperedText = icf.getChatlog();
 assertEquals("/w TestUser Test Whisper", whisperedText.substring(0, whisperedText.indexOf("\n")).trim());
 }

 @Test
 public void testMouseEvent() {
 MouseEvent mouseEvent = new MouseEvent(icf, 1, 10, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis());
 icf.maybeShowPopup(mouseEvent);
 assertNotNull(icf.listPop);
 }

 @After
 public void tearDown() {
 icf = null;
 parent = null;
 }

}