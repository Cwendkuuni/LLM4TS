package dsachat.client.gui; // Import necessary packages here

import static org.junit.Assert.*;
import org.junit.Test;
import javax.swing.*;

public class InternalChatFrameTest {

    @Test
    public void testInternalChatFrameConstructor() {
        // Given
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized

        // When
        InternalChatFrame chatFrame = new InternalChatFrame(parent);

        // Then
        assertNotNull(chatFrame);
        assertEquals("Chat", chatFrame.getTitle());
        assertTrue(chatFrame.isFocusable());
    }

    @Test
    public void testAddText() {
        // Given
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame());
        StyledDocument doc = new PlainDocument();

        // When
        chatFrame.addText("Test: You : Hello", doc);

        // Then
        assertEquals("Test: You : Hello\n", chatFrame.getChatlog());
    }

    @Test
    public void testAddTextWithException() {
        // Given
        InternalChatFrame chatFrame = new InternalChatFrame(new JFrame());

        // When & Then
        try {
            chatFrame.addText(null);
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Add more test cases for other methods here
}