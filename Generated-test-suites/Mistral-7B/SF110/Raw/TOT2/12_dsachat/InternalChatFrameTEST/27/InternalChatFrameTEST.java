import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        String line = "You: Hello World";
        chatFrame.addText(line);
        JTextPane textPane = chatFrame.getTextPane();
        String expectedText = "You: Hello World\n";
        String actualText = textPane.getText();
        assertEquals(expectedText, actualText);
    }
}