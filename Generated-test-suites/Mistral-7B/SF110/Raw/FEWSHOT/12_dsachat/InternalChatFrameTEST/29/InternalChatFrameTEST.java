package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame()) {
            @Override
            public void addText(String line) {
                String result = getChatlog();
                super.addText(line);
                assertEquals(line + "\n", result);
            }
        };
        icf.addText("You: Hello");
        icf.addText("User1: Hi");
        icf.addText("User1: How are you?");
        icf.addText("You: I'm good, thanks. How about you?");
        icf.addText("User1: I'm doing well, too.");
        icf.addText("You: Great!");

        String[] expected = {"You: Hello", "User1: Hi", "User1: How are you?", "You: I'm good, thanks. How about you?", "User1: I'm doing well, too.", "You: Great!"};
        String[] actual = icf.getUserlist();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame()) {
            @Override
            public void changeStyle(int fontSize, Color c, boolean isBold, boolean isItalic, String name) {
                super.changeStyle(fontSize, c, isBold, isItalic, name);
                JTextPane txtPane = icf.showTxt;
                StyleConstants.setBold(txtPane.getStyle(name), isBold);
                StyleConstants.setItalic(txtPane.getStyle(name), isItalic);
                assertEquals(isBold, txtPane.getStyle(name).isBold());
                assertEquals(isItalic, txtPane.getStyle(name).isItalic());
            }
        };
        icf.changeStyle(12, Color.RED, true, false, "User1");
        icf.changeStyle(12, null, false, true, "You");
    }

    @Test
    public void testAddUser() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame()) {
            @Override
            public void addUser(String user) {
                super.addUser(user);
                JList list = icf.list;
                assertEquals(user, list.getModel().getElementAt(0));
            }
        };
        icf.addUser("User1");
        icf.addUser("User2");
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame()) {
            @Override
            public void deleteUser(String user) {
                super.deleteUser(user);
                String[] userlist = icf.getUserlist();
                assertArrayNotEquals(new String[]{"User1", "User2"}, userlist);
                for (String user1 : userlist) {
                    if (user1.equals("User1")) {
                        fail("User1 should not be in the list after deletion.");
                    }
                }
            }
        };
        icf.addUser("User1");
        icf.addUser("User2");
        icf.deleteUser("User1");
    }

    @Test
    public void testGetChatlog() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame()) {
            @Override
            public String getChatlog() {
                String result = super.getChatlog();
                String expected = "You: Hello\nUser1: Hi\nUser1: How are you?\nYou: I'm good, thanks. How about you?\nUser1: I'm doing well, too.\nYou: Great!\n";
                assertEquals(expected, result);
                return result;
            }
        };
        icf.addText("You: Hello");
        icf.addText("User1: Hi");
        icf.addText("User1: How are you?");
        icf.addText("You: I'm good, thanks. How about you?");
        icf.addText("User1: I'm doing well, too.");
        icf.addText("You: Great!");

        String result = icf.getChatlog();
        assertEquals(icf.getChatlog(), result);
    }

    @Test
    public void testMouseClicked() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame()) {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        };
        JPopupMenu popupMenu = icf.listPop;
        MouseEvent mouseEvent = new MouseEvent(popupMenu, 1, System.currentTimeMillis(), 0, 10, 10, 1, false);
        icf.list.dispatchEvent(mouseEvent);
        assertTrue(popupMenu.isVisible());
    }

    @Test
    public void testMousePressed() {
        InternalChatFrame icf = new InternalChatFrame(new MainFrame()) {
            @Override
            public void maybeShowPopup(MouseEvent e) {
                super.maybeShowPopup(e);
            }
        };
        JPopupMenu popupMenu = icf.listPop;
        MouseEvent mouseEvent = new MouseEvent(icf.list, 1, System.currentTimeMillis(), 0, 10, 10, 1, false);
        icf.list.dispatchEvent(mouseEvent);
        assertTrue(popupMenu.isVisible());
    }
}