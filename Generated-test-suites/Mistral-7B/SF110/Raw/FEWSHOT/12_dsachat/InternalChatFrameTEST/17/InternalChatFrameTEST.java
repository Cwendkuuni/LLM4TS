package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            @Override
            public void addText(String line) {
                String result = getChatlog();
                super.addText(line);
                assertEquals(getExpectedChatlog(line), result);
            }

            private String getExpectedChatlog(String line) {
                // Implement this method to generate the expected chatlog based on the given line
                // Use the method addStylesToDocument() in InternalChatFrame class to create styles
                // and add them to the document when generating the expected chatlog
                // Use StringUtils or other string manipulation libraries to build the expected chatlog
                return "";
            }
        };

        chatFrame.addText("You: Hello");
        chatFrame.addText("User1: Hi");
        chatFrame.addText("User1: How are you?");
        chatFrame.addText("You: I'm good, thanks. How about you?");
        chatFrame.addText("User1: I'm fine, thanks.");
        chatFrame.addText("You: Nice to hear that.");
        chatFrame.addText("User1: Bye.");
        chatFrame.addText("You: Bye.");
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            @SuppressWarnings("unchecked")
            private JList<String> getList() {
                return (JList<String>) chatFrame.list;
            }

            @Test
            public void testAddUserAndGetUserlist() {
                chatFrame.addText("You: User1 joined the chat");
                chatFrame.addUser("User1");
                String[] expectedUserlist = {"User1"};
                assertArrayEquals(expectedUserlist, chatFrame.getUserlist());
            }

            @Test
            public void testAddUserTwice() {
                chatFrame.addText("You: User1 joined the chat");
                chatFrame.addUser("User1");
                chatFrame.addText("User2: User1 is already in the chat");
                chatFrame.addUser("User1");
                String[] expectedUserlist = {"User1"};
                assertArrayEquals(expectedUserlist, chatFrame.getUserlist());
            }

            @Test
            public void testDeleteUser() {
                chatFrame.addText("You: User1 joined the chat");
                chatFrame.addText("You: User2 joined the chat");
                chatFrame.addUser("User1");
                chatFrame.addUser("User2");
                String[] expectedUserlistBeforeDeletion = {"User1", "User2"};
                assertArrayEquals(expectedUserlistBeforeDeletion, chatFrame.getUserlist());
                chatFrame.deleteUser("User1");
                String[] expectedUserlistAfterDeletion = {"User2"};
                assertArrayEquals(expectedUserlistAfterDeletion, chatFrame.getUserlist());
            }
        };
    }
}