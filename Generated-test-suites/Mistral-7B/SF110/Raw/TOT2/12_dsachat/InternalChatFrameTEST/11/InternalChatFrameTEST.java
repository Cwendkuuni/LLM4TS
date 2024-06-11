import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = new MainFrame();
 chatFrame = new InternalChatFrame(parent);
 }

 @Test
 public void testConstructor() {
 // Check if the constructor is called correctly and the component is initialized
 assertEquals("Chat", chatFrame.getTitle());
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 }

 @Test
 public void testAddText() {
 // Basic functionality test for addText method
 String line = "Test: This is a test line";
 chatFrame.addText(line);
 String expected = "Test: This is a test line\n";
 assertEquals(expected, chatFrame.showTxt.getText().substring(0, chatFrame.showTxt.getCaretPosition() - 1));

 // Edge case testing for addText method
 String line1 = "Test: <html><b>This is a test line</b></html>";
 chatFrame.addText(line1);
 String expected1 = "Test: <html><b>This is a test line</b></html>\n";
 assertEquals(expected1, chatFrame.showTxt.getText().substring(0, chatFrame.showTxt.getCaretPosition() - 1));

 String line2 = "Test: <html><i>This is a test line</i></html>";
 chatFrame.addText(line2);
 String expected2 = "Test: <html><i>This is a test line</i></html>\n";
 assertEquals(expected2, chatFrame.showTxt.getText().substring(0, chatFrame.showTxt.getCaretPosition() - 1));

 String line3 = "Test: <html><b>This is a <i>test</i> line</b></html>";
 chatFrame.addText(line3);
 String expected3 = "Test: <html><b>This is a <i>test</i> line</b></html>\n";
 assertEquals(expected3, chatFrame.showTxt.getText().substring(0, chatFrame.showTxt.getCaretPosition() - 1));

 String line4 = "Test: <html><b>This is a test line</b></html><html><i>This is another test line</i></html>";
 chatFrame.addText(line4);
 String expected4 = "Test: <html><b>This is a test line</b></html><html><i>This is another test line</i></html>\n";
 assertEquals(expected4, chatFrame.showTxt.getText().substring(0, chatFrame.showTxt.getCaretPosition() - 1));
 }

 @Test(expected = BadLocationException.class)
 public void testAddText_BadLocationException() {
 // Test for IllegalArgumentException when passing an empty line
 chatFrame.addText("");
 }

 @Test
 public void testAddUser() {
 // Basic functionality test for addUser method
 chatFrame.addUser("TestUser1");
 String[] expected = {"TestUser1"};
 assertArrayEquals(expected, chatFrame.getUserlist());

 // Edge case testing for addUser method
 chatFrame.addUser("TestUser1: TestMessage");
 String[] expected1 = {"TestUser1"};
 assertArrayEquals(expected1, chatFrame.getUserlist());
 }

 @Test
 public void testDeleteUser() {
 // Basic functionality test for deleteUser method
 chatFrame.addUser("TestUser1");
 chatFrame.addUser("TestUser2");
 chatFrame.deleteUser("TestUser1");
 String[] expected = {"TestUser2"};
 assertArrayEquals(expected, chatFrame.getUserlist());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testDeleteUser_ArrayIndexOutOfBoundsException() {
 // Test for ArrayIndexOutOfBoundsException when deleting an non-existing user
 chatFrame.deleteUser("NonExistingUser");
 }

 @Test
 public void testGetChatlog() {
 // Basic functionality test for getChatlog method
 String expected = "Test: This is a test line\nTest: <html><b>This is a test line</b></html>\nTest: <html><i>This is a test line</i></html>\nTest: <html><b>This is a <i>test</i> line</b></html>\nTest: <html><b>This is a test line</b></html><html><i>This is another test line</i></html>\n";
 assertEquals(expected, chatFrame.getChatlog());
 }
}