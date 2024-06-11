package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.text.StyledDocument;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            @Override
            protected void addStylesToDocument(StyledDocument doc) {
                // Empty implementation for test purposes
            }
        };
        String line = "You: Hello World";
        chatFrame.addText(line);
        StyledDocument doc = chatFrame.doc;
        Style regular = doc.getStyle("regular");
        Style name = doc.getStyle("name");

        assertEquals(line, doc.getText(0, doc.getLength()).replaceAll("[\r\n]", ""));
        assertEquals(line.substring(0, line.indexOf(":")), doc.getText(0, doc.getStyle("name").getStartOffset()));
        assertEquals(line.substring(line.indexOf(":") + 1), doc.getText(doc.getStyle("name").getEndOffset(), doc.getLength()));
        assertNotNull(doc.getStyle("name"));
        assertEquals(name.getStyleConstants().getForeground().getRGB(), 0x0000FF); // Blue color
        assertEquals(name.getStyleConstants().getFontSize(), 16);
        // Check that the "regular" style properties are not modified
        assertEquals(regular.getStyleConstants().getForeground().getRGB(), 0x006000); // Default color
        assertEquals(regular.getStyleConstants().getFontSize(), 14);
    }
}