package dsachat.client.gui;
import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.client.gui.InternalChatFrame;

public class InternalChatFrameTest {
    @Test
    public void testConstructor() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame icf = new InternalChatFrame(parent);
        assertNotNull(icf);
    }

    @Test
    public void testAddText() {
        MainFrame parent = new MainFrame();
        InternalChatFrame icf = new InternalChatFrame(parent);
        icf.addText("Test Message");
        String chatLog = icf.getChatlog();
        assertTrue(chatLog.contains("Test Message"));
    }
}