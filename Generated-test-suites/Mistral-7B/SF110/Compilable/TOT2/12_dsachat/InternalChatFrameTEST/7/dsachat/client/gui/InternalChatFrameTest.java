package dsachat.client.gui;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InternalChatFrameTest {
    @Test
    public void testInternalChatFrameConstruction() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame frame = new InternalChatFrame(parent);
        assertEquals("Chat", frame.getTitle());
    }
}