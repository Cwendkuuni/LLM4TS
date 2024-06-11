import static org.junit.Assert.*;
import org.junit.Test;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String line = "User1: Hello World!";
        chatFrame.addText(line);

        String expectedOutput = "User1: Hello World!\n";
        String actualOutput = chatFrame.getChatlog();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAddEmptyText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("");

        String expectedOutput = "";
        String actualOutput = chatFrame.getChatlog();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText(null);
    }

    @Test
    public void testAddTextWithInvalidFormat() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String line = "User1: Hello: World!";
        chatFrame.addText(line);

        String expectedOutput = "User1: Hello: World!\n";
        String actualOutput = chatFrame.getChatlog();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = BadLocationException.class)
    public void testAddTextWithBadLocationException() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String line = "User1: Hello World!";
        chatFrame.addText(line);

        try {
            chatFrame.addText("ExceptionMessage");
        } catch (BadLocationException e) {
            // Expected exception
        }
    }
}

//