package dsachat.client.gui.test;

import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame icf;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = new MainFrame();
 icf = new InternalChatFrame(parent);
 }

 @Test
 public void testConstructor() {
 assertNotNull(icf);
 assertEquals("Chat", icf.getTitle());
 }

 @Test
 public void testAddText() {
 String line = "You: Hello World";
 icf.addText(line);
 StyledDocument doc = icf.showTxt.getStyledDocument();
 assertEquals(line, doc.getText(0, doc.getLength()));
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithException() {
 icf.addText("Hello World");
 StyledDocument doc = icf.showTxt.getStyledDocument();
 doc.insertString(doc.getLength(), "Test", null);
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = icf.showTxt.getStyledDocument();
 assertEquals(11, doc.getStyleCount());
 }

 @Test
 public void testChangeStyle() {
 icf.changeStyle(12, Color.RED, true, false, "name");
 StyledDocument doc = icf.showTxt.getStyledDocument();
 Style name = doc.getStyle("name");
 assertEquals(Color.RED, StyleConstants.getForeground(name));
 }

 @Test
 public void testAddUser() {
 icf.addUser("User1");
 assertEquals(1, icf.getUserlist().length);
 assertEquals("User1", icf.listModel.elementAt(0));
 }

 @Test
 public void testDeleteUser() {
 icf.addUser("User1");
 icf.addUser("User2");
 icf.deleteUser("User1");
 assertEquals(1, icf.getUserlist().length);
 assertFalse(icf.listModel.contains("User1"));
 }

 @Test
 public void testGetUserlist() {
 icf.addUser("User1");
 icf.addUser("User2");
 String[] uList = icf.getUserlist();
 assertEquals(2, uList.length);
 assertEquals("User1", uList[0]);
 assertEquals("User2", uList[1]);
 }

 @Test
 public void testActionPerformed() {
 String name = "User1";
 icf.addUser(name);
 icf.addText("You: Hello " + name);
 icf.actionPerformed(new ActionEvent(icf, 0, "chooseColor"));
 icf.changeStyle(12, Color.GREEN, false, false, name);
 StyledDocument doc = icf.showTxt.getStyledDocument();
 Style nameStyle = doc.getStyle(name);
 assertEquals(Color.GREEN, StyleConstants.getForeground(nameStyle));
 }

 @Test
 public void testMouseClicked() {
 icf.addMouseListener(new MouseAdapter() {
 @Override
 public void mouseClicked(MouseEvent e) {
 fail("Mouse clicked event should not be triggered");
 }
 });
 icf.maybeShowPopup(new MouseEvent(icf, 0, 0, 0, 0, 0, 0, false));
 }

}