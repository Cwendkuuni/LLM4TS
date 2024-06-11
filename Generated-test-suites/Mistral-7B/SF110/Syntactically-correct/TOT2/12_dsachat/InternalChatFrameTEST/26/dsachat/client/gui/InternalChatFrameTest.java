import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame; // Assuming MainFrame is available for testing

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = new MainFrame(); // Assuming MainFrame has a no-arg constructor
 chatFrame = new InternalChatFrame(parent);
 }

 @Test
 public void testAddTextWithValidLine() {
 String line = "John: Hello";
 chatFrame.addText(line);
 String expectedOutput = "John:\n";
 assertEquals(expectedOutput, chatFrame.getChatlog().substring(0, expectedOutput.length()));
 }

 @Test
 public void testAddTextWithWhisperLine() {
 String line = "/w John: Hello";
 chatFrame.addText(line);
 String expectedOutput = "/w John:\n";
 assertEquals(expectedOutput, chatFrame.getChatlog().substring(0, expectedOutput.length()));
 }

 @Test
 public void testAddTextWithFormattedLine() {
 String line = "*John*: *Hello*";
 chatFrame.addText(line);
 String expectedOutput = "*John*: Hello\n";
 assertEquals(expectedOutput, chatFrame.getChatlog().substring(0, expectedOutput.length()));
 }

 @Test
 public void testAddTextWithBoldItalicLine() {
 String line = "*_John__*: _Hello__*";
 chatFrame.addText(line);
 String expectedOutput = "*_John__*: Hello__*\n";
 assertEquals(expectedOutput, chatFrame.getChatlog().substring(0, expectedOutput.length()));
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("John");
 String[] expectedUserlist = {"John"};
 assertArrayEquals(expectedUserlist, chatFrame.getUserlist());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullUser() {
 chatFrame.addUser(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testDeleteUserAtInvalidIndex() {
 chatFrame.deleteUser(0);
 chatFrame.addUser("John");
 chatFrame.deleteUser(1);
 String[] userlist = chatFrame.getUserlist();
 assertArrayEquals(new String[] {"John"}, userlist);
 chatFrame.deleteUser(0); // should throw an exception
 }

 @Test
 public void testChangeStyle() {
 chatFrame.changeStyle(12, Color.RED, true, false, "John");
 String expectedOutput = "\n\n*John*\n";
 assertEquals(expectedOutput, chatFrame.getChatlog().substring(chatFrame.getChatlog().length() - expectedOutput.length()));
 }

 @Test(expected = NullPointerException.class)
 public void testChangeNullStyle() {
 chatFrame.changeStyle(null, null, false, false, "John");
 }
}