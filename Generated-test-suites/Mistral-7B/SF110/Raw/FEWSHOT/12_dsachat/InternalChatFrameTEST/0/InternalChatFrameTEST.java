package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class InternalChatFrameTest {

    private InternalChatFrame icf;

    @Test
    public void testInternalChatFrame() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        icf = new InternalChatFrame(parent);
        assertNotNull(icf);
    }

    @Test
    public void testCreateGUI() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame icf = new InternalChatFrame(parent);
        icf.createGUI();
        JList list = icf.list;
        assertNotNull(list);
        JTextPane showTxt = icf.showTxt;
        assertNotNull(showTxt);
        JTextField txtInput = icf.txtInput;
        assertNotNull(txtInput);
    }

    @Test
    public void testAddStylesToDocument() throws BadLocationException {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        StyledDocument doc = new PlainDocument();
        InternalChatFrame.addStylesToDocument(doc);

        Style regular = doc.getStyle("regular");
        Style italic = doc.getStyle("italic");
        Style bold = doc.getStyle("bold");
        Style boldItalic = doc.getStyle("bold-italic");
        Style name = doc.getStyle("name");
        Style system = doc.getStyle("system");
        Style small = doc.getStyle("small");
        Style large = doc.getStyle("large");

        assertEquals(StyleContext.getDefaultStyleContext().getStyle("default"), regular.getParent());
        assertEquals(StyleConstants.FontFamily.SANS_SERIF, StyleConstants.getFontFamily(regular));

        assertEquals(regular, italic.getParent());
        assertTrue(StyleConstants.isItalic(italic));

        assertEquals(regular, bold.getParent());
        assertTrue(StyleConstants.isBold(bold));

        assertEquals(regular, boldItalic.getParent());
        assertTrue(StyleConstants.isBold(boldItalic));
        assertTrue(StyleConstants.isItalic(boldItalic));

        assertEquals(regular, name.getParent());
        assertEquals(Color.BLUE, StyleConstants.getForeground(name));
        assertEquals(16, StyleConstants.getFontSize(name));

        assertEquals(regular, system.getParent());
        assertEquals(new Color(0.0f, 0.6f, 0.0f, 1.0f), StyleConstants.getForeground(system));
        assertTrue(StyleConstants.isItalic(system));
        assertEquals(14, StyleConstants.getFontSize(system));

        assertEquals(regular, small.getParent());
        assertEquals(10, StyleConstants.getFontSize(small));

        assertEquals(regular, large.getParent());
        assertEquals(16, StyleConstants.getFontSize(large));

        assertEquals(doc, doc.getDocument());
    }

    @Test
    public void testAddText() throws BadLocationException {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame icf = new InternalChatFrame(parent);
        Document doc = icf.showTxt.getStyledDocument();

        icf.addText("You : Hello World");
        JTextPane showTxt = icf.showTxt;
        DefaultCaret caret = (DefaultCaret) showTxt.getCaret();
        assertEquals("You : Hello World\n", showTxt.getText());
        assertEquals("You", doc.getStyle("You ").getName());
        assertEquals(StyleConstants.DEFAULT_FONT, doc.getStyle("You ").getFontFamily());

        icf.addText("*user1: *Hi");
        assertEquals("You : Hello World\n*user1: *Hi\n", showTxt.getText());
        assertEquals("user1", doc.getStyle("user1").getName());
        assertEquals(StyleConstants.DEFAULT_FONT, doc.getStyle("user1").getFontFamily());

        icf.addText("*user1: *Hi *user2");
        assertEquals("You : Hello World\n*user1: *Hi *user2\n", showTxt.getText());
        assertEquals("user2", doc.getStyle("user2").getName());
        assertEquals(StyleConstants.DEFAULT_FONT, doc.getStyle("user2").getFontFamily());

        icf.addText("*user1: *Hi *user2: Hello");
        assertEquals("You : Hello World\n*user1: *Hi *user2: Hello\n", showTxt.getText());
        assertEquals("user1", doc.getStyle("user1").getName());
        assertEquals(StyleConstants.DEFAULT_FONT, doc.getStyle("user1").getFontFamily());

        assertEquals("user2", doc.getStyle("user2").getName());
        assertEquals(StyleConstants.DEFAULT_FONT, doc.getStyle("user2").getFontFamily());

        icf.addText("*user1: *Hi *user2: *Hello");
        assertEquals("You : Hello World\n*user1: *Hi *user2: *Hello\n", showTxt.getText());
        assertEquals("user1", doc.getStyle("user1").getName());
        assertEquals(StyleConstants.DEFAULT_FONT, doc.getStyle("user1").getFontFamily());

        assertEquals("user2", doc.getStyle("user2").getName());
        assertEquals(StyleConstants.ITALIC, doc.getStyle("user2").getAttributes().get(SimpleAttributeSet.ITALIC));
    }

    @Test
    public void testChangeStyle() throws Exception {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame icf = new InternalChatFrame(parent);
        Document doc = icf.showTxt.getStyledDocument();

        icf.changeStyle(12, Color.RED, false, false, "name");
        Style name = doc.getStyle("name");
        assertEquals(Color.RED, StyleConstants.getForeground(name));

        icf.changeStyle(12, null, true, false, "name");
        name = doc.getStyle("name");
        assertNull(StyleConstants.getForeground(name));

        icf.changeStyle(12, Color.BLUE, true, true, "name");
        name = doc.getStyle("name");
        assertTrue(StyleConstants.isBold(name));
        assertTrue(StyleConstants.isItalic(name));
        assertEquals(Color.BLUE, StyleConstants.getForeground(name));
    }

    @Test
    public void testAddUser() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame icf = new InternalChatFrame(parent);
        String[] expectedUserList = {"user1", "user2"};

        icf.addUser("user1");
        icf.addUser("user2");

        String[] actualUserList = icf.getUserlist();

        assertArrayEquals(expectedUserList, actualUserList);
    }

    @Test
    public void testDeleteUser() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame icf = new InternalChatFrame(parent);
        String[] expectedUserList = {"user1"};

        icf.addUser("user1");
        icf.addUser("user2");
        icf.deleteUser("user2");

        String[] actualUserList = icf.getUserlist();

        assertArrayEquals(expectedUserList, actualUserList);
    }

    @Test
    public void testGetChatlog() {
        MainFrame parent = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame icf = new InternalChatFrame(parent);

        icf.addText("You : Hello World");
        icf.addText("*user1: *Hi");
        icf.addText("*user1: *Hi *user2");
        icf.addText("*user1: *Hi *user2: Hello");

        assertEquals("You : Hello World\n*user1: *Hi *user2: Hello\n", icf.getChatlog());
    }
}