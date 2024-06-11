package dsachat.client.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InternalChatFrameTest {

    @Test
    public void testCreateGUI() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame()) {
            {
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        assertEquals(true, icf.isVisible());
        assertTrue(icf.getTitle().equals("Chat"));
    }

    @Test
    public void testActionPerformed_chooseColor() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame()) {
            {
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        JPopupMenu popupMenu = mock(JPopupMenu.class);
        when(popupMenu.isPopupVisible()).thenReturn(false);
        icf.listPop = popupMenu;
        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(0);
        when(mouseEvent.getY()).thenReturn(0);
        when(mouseEvent.getSource()).thenReturn(icf.list);
        icf.actionPerformed(new ActionEvent(icf, 0, "chooseColor"));
        verify(popupMenu).show(icf.list, 0, 0);
    }

    @Test
    public void testActionPerformed_bgColor() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame()) {
            {
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        JPopupMenu popupMenu = mock(JPopupMenu.class);
        when(popupMenu.isPopupVisible()).thenReturn(false);
        icf.txtPop = popupMenu;
        MouseEvent mouseEvent = mock(MouseEvent.class);
        when(mouseEvent.getX()).thenReturn(0);
        when(mouseEvent.getY()).thenReturn(0);
        when(mouseEvent.getSource()).thenReturn(icf.showTxt);
        icf.actionPerformed(new ActionEvent(icf, 0, "bgColor"));
        verify(popupMenu).show(icf.list, 0, 0);
    }

    @Test
    public void testAddText_regularText() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame()) {
            {
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        StyleContext styleContext = mock(StyleContext.class);
        Style defaultStyle = mock(Style.class);
        Style regularStyle = mock(Style.class);
        when(styleContext.getDefaultStyleContext()).thenReturn(styleContext);
        when(styleContext.getStyle("regular")).thenReturn(regularStyle);
        StyledDocument doc = mock(StyledDocument.class);
        when(icf.doc).thenReturn(doc);
        String line = "user: text";
        icf.addText(line);
        String expectedText = "user: text\n";
        try {
            assertEquals(expectedText, icf.showTxt.getText(0, icf.showTxt.getText().length()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddText_italicText() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame()) {
            {
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        StyleContext styleContext = mock(StyleContext.class);
        Style defaultStyle = mock(Style.class);
        Style regularStyle = mock(Style.class);
        Style italicStyle = mock(Style.class);
        when(styleContext.getDefaultStyleContext()).thenReturn(styleContext);
        when(styleContext.getStyle("regular")).thenReturn(regularStyle);
        when(styleContext.getStyle("italic")).thenReturn(italicStyle);
        StyledDocument doc = mock(StyledDocument.class);
        when(icf.doc).thenReturn(doc);
        String line = "user:*italic* text";
        icf.addText(line);
        String expectedText = "user: text\n";
        try {
            assertEquals(expectedText, icf.showTxt.getText(0, icf.showTxt.getText().length()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        verify(doc).insertString(icf.doc.getLength(), "user:", italicStyle);
        verify(doc).insertString(icf.doc.getLength(), "text", italicStyle);
        verify(doc).insertString(icf.doc.getLength(), "\n", regularStyle);
    }

    @Test
    public void testAddText_boldText() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame()) {
            {
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        StyleContext styleContext = mock(StyleContext.class);
        Style defaultStyle = mock(Style.class);
        Style regularStyle = mock(Style.class);
        Style boldStyle = mock(Style.class);
        when(styleContext.getDefaultStyleContext()).thenReturn(styleContext);
        when(styleContext.getStyle("regular")).thenReturn(regularStyle);
        when(styleContext.getStyle("bold")).thenReturn(boldStyle);
        StyledDocument doc = mock(StyledDocument.class);
        when(icf.doc).thenReturn(doc);
        String line = "user:*bold* text";
        icf.addText(line);
        String expectedText = "user: text\n";
        try {
            assertEquals(expectedText, icf.showTxt.getText(0, icf.showTxt.getText().length()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        verify(doc).insertString(icf.doc.getLength(), "user:", regularStyle);
        verify(doc).insertString(icf.doc.getLength(), "text", boldStyle);
        verify(doc).insertString(icf.doc.getLength(), "\n", regularStyle);
    }

    @Test
    public void testAddText_boldItalicText() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame()) {
            {
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        StyleContext styleContext = mock(StyleContext.class);
        Style defaultStyle = mock(Style.class);
        Style regularStyle = mock(Style.class);
        Style italicStyle = mock(Style.class);
        Style boldStyle = mock(Style.class);
        when(styleContext.getDefaultStyleContext()).thenReturn(styleContext);
        when(styleContext.getStyle("regular")).thenReturn(regularStyle);
        when(styleContext.getStyle("italic")).thenReturn(italicStyle);
        when(styleContext.getStyle("bold")).thenReturn(boldStyle);
        when(styleContext.getStyle("bold-italic")).thenReturn(mock(Style.class));
        StyledDocument doc = mock(StyledDocument.class);
        when(icf.doc).thenReturn(doc);
        String line = "user:*bold-italic* text";
        icf.addText(line);
        String expectedText = "user: text\n";
        try {
            assertEquals(expectedText, icf.showTxt.getText(0, icf.showTxt.getText().length()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        verify(doc).insertString(icf.doc.getLength(), "user:", regularStyle);
        verify(doc).insertString(icf.doc.getLength(), "text", boldStyle);
        verify(doc).insertString(icf.doc.getLength(), "\n", regularStyle);
    }

    @Test
    public void testActionPerformed_deleteUser() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame()) {
            {
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        JList list = mock(JList.class);
        when(icf.list).thenReturn(list);
        JPopupMenu popupMenu = mock(JPopupMenu.class);
        when(icf.listPop).thenReturn(popupMenu);
        when(popupMenu.isPopupVisible()).thenReturn(false);
        String userToDelete = "user";
        icf.addUser(userToDelete);
        icf.addText("user: text");

        icf.actionPerformed(new ActionEvent(icf, 0, "deleteUser"));

        verify(icf.listModel).removeElement(userToDelete);
        String expectedText = "text\n";
        try {
            assertEquals(expectedText, icf.showTxt.getText(0, icf.showTxt.getText().length()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserlist() {
        InternalChatFrame icf = new InternalChatFrame(new JFrame()) {
            {
                setVisible(true);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        };
        JList list = mock(JList.class);
        when(icf.list).thenReturn(list);
        JPopupMenu popupMenu = mock(JPopupMenu.class);
        when(icf.listPop).thenReturn(popupMenu);
        DefaultListModel listModel = new DefaultListModel();
        when(icf.listModel).thenReturn(listModel);
        listModel.addElement("user1");
        listModel.addElement("user2");
        String[] expectedUserlist = {"user1", "user2"};
        assertEquals(expectedUserlist, icf.getUserlist());
    }
}