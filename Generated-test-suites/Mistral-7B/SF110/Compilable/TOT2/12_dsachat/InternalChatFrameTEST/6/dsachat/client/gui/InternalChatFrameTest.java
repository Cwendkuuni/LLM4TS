package dsachat.client.gui;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Test User: Hello");
        String expectedText = "Test User: Hello\n";
        String actualText = chatFrame.getChatlog();
        assertEquals(expectedText, actualText);
    }
    
    @Test
    public void testAddTextWithSpecialCharacters() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Test User: Hello! World ^_^");
        String expectedText = "Test User: Hello! World ^_^\n";
        String actualText = chatFrame.getChatlog();
        assertEquals(expectedText, actualText);
    }
    
    @Test
    public void testAddTextEmptyString() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("");
        String expectedText = "";
        String actualText = chatFrame.getChatlog();
        assertEquals(expectedText, actualText);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddTextNullInput() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText(null);
    }
    
    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("Test User");
        String[] expectedUsers = {"Test User"};
        String[] actualUsers = chatFrame.getUserlist();
        assertArrayEquals(expectedUsers, actualUsers);
    }
    
    @Test
    public void testAddUserWithExistingName() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("Test User");
        chatFrame.addUser("Test User");
        String[] expectedUsers = {"Test User", "Test User"};
        String[] actualUsers = chatFrame.getUserlist();
        assertArrayEquals(expectedUsers, actualUsers);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddUserEmptyString() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddUserNullName() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser(null);
    }
}