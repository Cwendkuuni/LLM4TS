import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parentFrame;

 @Before
 public void setUp() {
 parentFrame = new MainFrame();
 chatFrame = new InternalChatFrame(parentFrame);
 }

 @Test
 public void testConstructor() {
 assertNotNull(chatFrame);
 assertEquals("Chat", chatFrame.getTitle());
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello";
 chatFrame.addText(line);
 assertEquals(line, chatFrame.getChatlog().substring(0, chatFrame.getChatlog().lastIndexOf("\n") + line.length()));
 }

 @Test
 public void testAddText_InvalidLineFormat() {
 String line = "Hello: User1";
 chatFrame.addText(line);
 assertEquals("", chatFrame.getChatlog().substring(0, chatFrame.getChatlog().lastIndexOf("\n")));
 }

 @Test
 public void testAddText_Whisper() {
 String line = "/w User1: Hi";
 chatFrame.addText(line);
 assertEquals("User1: Hi", chatFrame.getChatlog().substring(chatFrame.getChatlog().lastIndexOf("\n") + line.length(), chatFrame.getChatlog().length()));
 }

 @Test(expected = BadLocationException.class)
 public void testAddText_BadLocationException() {
 String line = "User1: Hello: World";
 chatFrame.addText(line);
 fail("Should have thrown BadLocationException");
 }

 @Test
 public void testChangeStyle() {
 chatFrame.changeStyle(12, Color.RED, true, false, "User1");
 assertEquals(Color.RED, chatFrame.getStyle("User1").getBackground());
 }

 @Test
 public void testAddUser() {
 String user = "User2";
 chatFrame.addUser(user);
 assertEquals(user, chatFrame.getUserlist()[0]);
 }

 @Test
 public void testDeleteUser() {
 String user = "User1";
 chatFrame.addUser(user);
 chatFrame.deleteUser(user);
 assertArrayEquals(new String[0], chatFrame.getUserlist());
 }

 @Test
 public void testGetChatlog() {
 String line1 = "User1: Hello";
 String line2 = "User2: World";
 chatFrame.addText(line1);
 chatFrame.addText(line2);
 assertEquals(line1 + line2, chatFrame.getChatlog());
 }
}