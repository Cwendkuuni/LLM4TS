package dsachat.client.gui; // make sure to import necessary packages

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
    // Assuming you have the MainFrame class and its hero field is accessible
    private MainFrame parent;
    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testInternalChatFrameConstructor() {
        assertNotNull(chatFrame);
    }

    @Test
    public void testCreateGUI() {
        assertNotNull(chatFrame.list);
        assertNotNull(chatFrame.showTxt);
        assertNotNull(chatFrame.txtInput);
    }

    @Test
    public void testAddText() throws BadLocationException {
        String line = "You: Hello";
        chatFrame.addText(line);
        Document doc = chatFrame.showTxt.getDocument();
        String actualText = doc.getText(0, doc.getLength()).trim();
        String expectedText = line + "\n";
        assertEquals(expectedText, actualText);

        line = "You: *Hello*";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = "You: *Hello*\n";
        assertEquals(expectedText, actualText);

        line = "You: **Hello**";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = "You: **Hello**\n";
        assertEquals(expectedText, actualText);

        line = "You: ***Hello***";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = "You: ***Hello***\n";
        assertEquals(expectedText, actualText);

        line = "User: Hello: World";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = line + "\n";
        assertEquals(expectedText, actualText);

        line = "User: *Hello: World*";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = "User: *Hello: World*\n";
        assertEquals(expectedText, actualText);

        line = "User: **Hello: World**";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = "User: **Hello: World**\n";
        assertEquals(expectedText, actualText);

        line = "User: ***Hello: World***";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = "User: ***Hello: World***\n";
        assertEquals(expectedText, actualText);

        line = "User: Hello: *World*";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = "User: Hello: *World*\n";
        assertEquals(expectedText, actualText);

        line = "User: Hello: **World**";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = "User: Hello: **World**\n";
        assertEquals(expectedText, actualText);

        line = "User: Hello: ***World***";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = "User: Hello: ***World***\n";
        assertEquals(expectedText, actualText);

        line = "User: Hello: ****World****";
        chatFrame.addText(line);
        doc = chatFrame.showTxt.getDocument();
        actualText = doc.getText(0, doc.getLength()).trim();
        expectedText = "User: Hello: ****World****\n";
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testAddStylesToDocument() {
        chatFrame.addStylesToDocument(chatFrame.doc);
        Style nameStyle = chatFrame.doc.getStyle("name");
        assertEquals(Color.BLUE, nameStyle.getBackground());
        assertEquals(16, nameStyle.getFontSize());

        Style regularStyle = chatFrame.doc.getStyle("regular");
        assertEquals(Color.BLACK, regularStyle.getBackground());
        assertEquals(14, regularStyle.getFontSize());
    }

    @Test
    public void testChangeStyle() {
        Style regularStyle = chatFrame.doc.getStyle("regular");
        Style newStyle = new SimpleAttributeSet();
        StyleConstants.setFontSize(newStyle, 18);
        StyleConstants.setForeground(newStyle, Color.RED);
        chatFrame.changeStyle(18, Color.RED, false, false, "regular");
        assertEquals(newStyle, chatFrame.doc.getStyle("regular"));

        newStyle = new SimpleAttributeSet();
        StyleConstants.setFontSize(newStyle, 18);
        StyleConstants.setForeground(newStyle, Color.GREEN);
        StyleConstants.setBold(newStyle, true);
        chatFrame.changeStyle(18, Color.GREEN, true, false, "regular");
        assertEquals(newStyle, chatFrame.doc.getStyle("regular"));

        newStyle = new SimpleAttributeSet();
        StyleConstants.setFontSize(newStyle, 18);
        StyleConstants.setForeground(newStyle, Color.GREEN);
        StyleConstants.setBold(newStyle, false);
        StyleConstants.setItalic(newStyle, true);
        chatFrame.changeStyle(18, Color.GREEN, false, true, "regular");
        assertEquals(newStyle, chatFrame.doc.getStyle("regular"));

        newStyle = new SimpleAttributeSet();
        StyleConstants.setFontSize(newStyle, 18);
        StyleConstants.setForeground(newStyle, Color.GREEN);
        StyleConstants.setBold(newStyle, true);
        StyleConstants.setItalic(newStyle, true);
        chatFrame.changeStyle(18, Color.GREEN, true, true, "regular");
        assertEquals(newStyle, chatFrame.doc.getStyle("regular"));
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        JList list = chatFrame.list;
        assertEquals(1, list.getModel().getSize());
        assertEquals("User1", list.getModel().getElementAt(0));
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertEquals(2, userList.length);

        chatFrame.deleteUser("User1");
        userList = chatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertNotEquals("User1", userList[0]);
    }

    @Test
    public void testGetUserlist() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertEquals("User1", userList[0]);
        assertEquals("User2", userList[1]);
    }

    @Test
    public void testActionPerformed() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatFrame.list.setSelectedValue("User1", true);
                chatFrame.actionPerformed(new ActionEvent("test", 0, "chooseColor"));
                Style nameStyle = chatFrame.doc.getStyle("User1");
                Color newColor = Color.YELLOW;
                chatFrame.changeStyle(12, newColor, false, false, "User1");
                assertEquals(newColor, nameStyle.getForeground());

                chatFrame.txtInput.setText("bgColor");
                chatFrame.actionPerformed(new ActionEvent("test", 0, "bgColor"));
                assertEquals(newColor, chatFrame.showTxt.getBackground());

                chatFrame.list.setSelectedValue("User2", true);
                chatFrame.actionPerformed(new ActionEvent("test", 0, "whisper"));
                String expectedText = "/w User2 : ";
                String actualText = chatFrame.txtInput.getText();
                assertEquals(expectedText, actualText);
            }
        });
    }

    @Test
    public void testMouseClicked() {
        chatFrame.listPop = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Change Color");
        menuItem.setActionCommand("chooseColor");
        menuItem.addActionListener(chatFrame);
        chatFrame.listPop.add(menuItem);

        JList list = chatFrame.list;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                list.setSelectedValue("User1", true);
                MouseEvent mouseEvent = new MouseEvent(list, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false);
                chatFrame.mouseClicked(mouseEvent);
                assertTrue(chatFrame.listPop.isVisible());
                chatFrame.listPop.setVisible(false);
            }
        });

        JPopupMenu txtPopup = chatFrame.txtPop;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatFrame.showTxt.setCaretPosition(0);
                DefaultCaret caret = (DefaultCaret) chatFrame.showTxt.getCaret();
                caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
                MouseEvent mouseEvent = new MouseEvent(chatFrame.showTxt, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false);
                chatFrame.mouseClicked(mouseEvent);
                assertTrue(chatFrame.txtPop.isVisible());
                chatFrame.txtPop.setVisible(false);
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            }
        });
    }

    @Test
    public void testGetChatlog() {
        String expectedText = "You: Hello\n";
        String line = "You: Hello";
        chatFrame.addText(line);
        String actualText = chatFrame.getChatlog();
        assertEquals(expectedText, actualText);
    }
}