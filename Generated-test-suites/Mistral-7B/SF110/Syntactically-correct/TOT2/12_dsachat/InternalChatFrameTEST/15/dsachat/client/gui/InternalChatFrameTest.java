import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.styles.StyleConstants;

public class InternalChatFrameTest {
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(null);
        chatFrame.addStylesToDocument(chatFrame.doc);

        String line = "You: Hello";
        chatFrame.addText(line);

        String expected = "You: Hello\n";
        String actual = chatFrame.doc.getDocumentElement().getText(0, chatFrame.doc.getDocumentElement().getLength());

        assertEquals(expected, actual);

        line = "*You: Hello";
        chatFrame.addText(line);

        expected += "*You";
        actual = chatFrame.doc.getDocumentElement().getText(0, chatFrame.doc.getDocumentElement().getLength());

        assertEquals(expected, actual);

        line = "*You:*Hi";
        chatFrame.addText(line);

        expected += "*Hi";
        actual = chatFrame.doc.getDocumentElement().getText(0, chatFrame.doc.getDocumentElement().getLength());

        assertEquals(expected, actual);

        line = "*You: *Hi";
        chatFrame.addText(line);

        expected += "*Hi*";
        actual = chatFrame.doc.getDocumentElement().getText(0, chatFrame.doc.getDocumentElement().getLength());

        assertEquals(expected, actual);
    }
}