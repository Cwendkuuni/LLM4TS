import org.junit.Test;
import static org.junit.Assert.assertEquals;
import dsachat.client.gui.InternalChatFrame;

public class InternalChatFrameTest {
    @Test
    public void testConstructor() {
        MainFrame parent = new MainFrame(); // assuming MainFrame has a constructor
        InternalChatFrame icf = new InternalChatFrame(parent);
        assertEquals("Chat", icf.getTitle());
        // Test other initializations like list, showTxt, etc.
    }

    @Test
    public void testAddText() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame()); // assuming JFrame has a constructor
        StyledDocument doc = new PlainDocument();
        icf.setDocument(doc);
        icf.addText("test:test");
        String expected = "test:test\n";
        assertEquals(expected, doc.getText(0, doc.getLength()));
    }

    // Test addText with different message formats and styles, as well as edge cases like null document

    @Test
    public void testGetChatlog() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame());
        icf.showTxt.setText("test1:test1 test2:test2 test3:test3");
        String expected = "test1:test1 test2:test2 test3:test3\n";
        assertEquals(expected, icf.getChatlog());
    }
}