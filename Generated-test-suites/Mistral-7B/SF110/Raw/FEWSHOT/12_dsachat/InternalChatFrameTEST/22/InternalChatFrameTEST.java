package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.MouseEvent;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            @Override
            void addStylesToDocument(StyledDocument doc) {
                // Do nothing
            }
        };
        String line = "User1: Hello World";
        chatFrame.addText(line);
        StyledDocument doc = chatFrame.doc;
        Style nameStyle = doc.getStyle("User1");
        assertEquals(line, doc.getText(0, doc.getLength()));
        assertEquals(line, chatFrame.showTxt.getText());
        assertEquals(StyleConstants.DEFAULT_FONT_SIZE, doc.getStyle("regular").getFontSize());
        assertEquals(StyleConstants.DEFAULT_FOREGROUND, doc.getStyle("User1").getForeground());
        assertEquals(true, nameStyle.isDefinedStyle("name"));
        assertEquals(StyleConstants.ITALIC_MASK, nameStyle.getParagraphAttributes().getParagraphFlags());
        assertEquals(StyleConstants.BOLD_MASK, 0);
        // Test italic and bold
        line = "User1: *Hello* World";
        chatFrame.addText(line);
        Style italicStyle = doc.getStyle("User1");
        assertEquals(true, italicStyle.isDefinedStyle("italic"));
        assertEquals(false, italicStyle.isDefinedStyle("bold"));
        // Test bold
        line = "User1:**Hello** World";
        chatFrame.addText(line);
        Style boldStyle = doc.getStyle("User1");
        assertEquals(false, boldStyle.isDefinedStyle("italic"));
        assertEquals(true, boldStyle.isDefinedStyle("bold"));
        // Test italic and bold together
        line = "User1:***Hello*** World";
        chatFrame.addText(line);
        Style italicBoldStyle = doc.getStyle("User1");
        assertEquals(true, italicBoldStyle.isDefinedStyle("italic"));
        assertEquals(true, italicBoldStyle.isDefinedStyle("bold"));
        // Test regular text
        line = "User1: Hello";
        chatFrame.addText(line);
        Style regularStyle = doc.getStyle("User1");
        assertEquals(false, regularStyle.isDefinedStyle("italic"));
        assertEquals(false, regularStyle.isDefinedStyle("bold"));
        // Test system message
        line = ":System Message";
        chatFrame.addText(line);
        Style systemStyle = doc.getStyle("system");
        assertEquals(false, systemStyle.isDefinedStyle("italic"));
        assertEquals(false, systemStyle.isDefinedStyle("bold"));
        assertEquals(Color.decode("#006600"), systemStyle.getForeground());
        assertEquals(14, systemStyle.getFontSize());
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            @Override
            void addStylesToDocument(StyledDocument doc) {
                // Do nothing
            }
        };
        String name = "User1";
        int fontSize = 12;
        Color color = Color.RED;
        boolean isBold = true;
        boolean isItalic = false;
        chatFrame.changeStyle(fontSize, color, isBold, isItalic, name);
        Style nameStyle = chatFrame.doc.getStyle(name);
        assertEquals(color, nameStyle.getForeground());
        assertEquals(isBold, nameStyle.isDefinedStyle("bold"));
        assertEquals(isItalic, nameStyle.isDefinedStyle("italic"));
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            @Override
            void addStylesToDocument(StyledDocument doc) {
                // Do nothing
            }
        };
        String user = "User1";
        chatFrame.addUser(user);
        assertEquals(user, chatFrame.listModel.getElementAt(0));
        assertEquals(user, (String) chatFrame.list.getSelectedValue());
        Style nameStyle = chatFrame.doc.getStyle(user);
        assertEquals(StyleConstants.DEFAULT_FONT_SIZE, nameStyle.getFontSize());
        assertEquals(Color.BLUE, nameStyle.getForeground());
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            @Override
            void addStylesToDocument(StyledDocument doc) {
                // Do nothing
            }
        };
        String user1 = "User1";
        String user2 = "User2";
        chatFrame.addUser(user1);
        chatFrame.addUser(user2);
        chatFrame.deleteUser(user1);
        assertEquals(1, chatFrame.listModel.getSize());
        assertNotEquals(user1, (String) chatFrame.list.getSelectedValue());
        assertNull(chatFrame.doc.getStyle(user1));
    }

    @Test
    public void testGetUserlist() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            @Override
            void addStylesToDocument(StyledDocument doc) {
                // Do nothing
            }
        };
        String user1 = "User1";
        String user2 = "User2";
        chatFrame.addUser(user1);
        chatFrame.addUser(user2);
        String[] users = chatFrame.getUserlist();
        assertArrayEquals(new String[]{user1, user2}, users);
    }

    @Test
    public void testActionPerformed() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            @Override
            void addStylesToDocument(StyledDocument doc) {
                // Do nothing
            }
        };
        String user = "User1";
        chatFrame.addUser(user);
        chatFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                chatFrame.maybeShowPopup(e);
            }
        });
        JPopupMenu popupMenu = mock(JPopupMenu.class);
        when(popupMenu.isShowing()).thenReturn(false);
        chatFrame.listPop = popupMenu;
        chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "chooseColor"));
        verify(popupMenu, never()).show(any(Component.class), anyInt(), anyInt());
        chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "bgColor"));
        verify(popupMenu, never()).show(any(Component.class), anyInt(), anyInt());
        chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "whisper"));
        assertEquals("/w " + user + " ", chatFrame.txtInput.getText());
    }

    @Test
    public void testMouseClicked() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame()) {
            @Override
            void addStylesToDocument(StyledDocument doc) {
                // Do nothing
            }
        };
        chatFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chatFrame.maybeShowPopup(e);
            }
        });
        JPopupMenu popupMenu = mock(JPopupMenu.class);
        when(popupMenu.isShowing()).thenReturn(false);
        chatFrame.listPop = popupMenu;
        chatFrame.mouseClicked(new MouseEvent(chatFrame, 1, 0, 0, 0, 0, null, false));
        verify(popupMenu, never()).show(any(Component.class), anyInt(), anyInt());
        chatFrame.txtPop = popupMenu;
        chatFrame.mouseClicked(new MouseEvent(chatFrame, 1, 0, 0, 0, 0, null, false));
        verify(popupMenu, never()).show(any(Component.class), anyInt(), anyInt());
    }
}