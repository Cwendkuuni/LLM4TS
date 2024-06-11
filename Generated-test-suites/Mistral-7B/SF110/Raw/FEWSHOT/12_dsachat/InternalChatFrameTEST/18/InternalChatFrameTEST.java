package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.MouseEvent;

public class InternalChatFrameTest {

    @Test
    public void testCreateGUI() {
        InternalChatFrame icf = new InternalChatFrame(null);
        assertNotNull(icf.list);
        assertNotNull(icf.showTxt);
        assertNotNull(icf.txtInput);
        assertNotNull(icf.parent);
        assertNotNull(icf.doc);
        assertNotNull(icf.listPop);
        assertNotNull(icf.txtPop);
    }

    @Test
    public void testAddText() {
        InternalChatFrame icf = new InternalChatFrame(null);
        icf.addText("You: Hello World");
        String chatLog = icf.getChatlog();
        assertEquals("You: Hello World\n", chatLog);

        icf.addText("User1: Hi, You!");
        chatLog = icf.getChatlog();
        assertEquals("You: Hello World\nUser1: Hi, You!\n", chatLog);

        icf.addText("User1: *Hi there*");
        chatLog = icf.getChatlog();
        assertEquals("You: Hello World\nUser1: Hi, You!\nUser1: *Hi there*\n", chatLog);

        icf.addText("User1: *Hi there* and *hello*");
        chatLog = icf.getChatlog();
        assertEquals("You: Hello World\nUser1: Hi, You!\nUser1: *Hi there*\nUser1: and *hello*\n", chatLog);

        icf.addText("*User1: Hi there* and User1: hello");
        chatLog = icf.getChatlog();
        assertEquals("You: Hello World\nUser1: Hi there and hello\n", chatLog);

        icf.addText("User1: Hi there *and* hello");
        chatLog = icf.getChatlog();
        assertEquals("You: Hello World\nUser1: Hi there and hello\n", chatLog);

        icf.addText("User1: **Hi there** and **hello**");
        chatLog = icf.getChatlog();
        assertEquals("You: Hello World\nUser1: **Hi there** and **hello**\n", chatLog);

        icf.addText("User1: **Hi there** and *hello*");
        chatLog = icf.getChatlog();
        assertEquals("You: Hello World\nUser1: **Hi there** and *hello*\n", chatLog);

        icf.addText("User1: Hi there and **hello**");
        chatLog = icf.getChatlog();
        assertEquals("You: Hello World\nUser1: Hi there and **hello**\n", chatLog);

        icf.addText("User1: *Hi there* and **hello**");
        chatLog = icf.getChatlog();
        assertEquals("You: Hello World\nUser1: *Hi there* and **hello**\n", chatLog);
    }

    @Test
    public void testAddStylesToDocument() {
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

        assertEquals(StyleConstants.DEFAULT_FONT_SIZE, StyleConstants.getFontSize(regular));
        assertEquals(StyleConstants.getDefaultFontFamily(), StyleConstants.getFontFamily(regular));

        assertEquals(StyleConstants.ITALIC, StyleConstants.getItalic(italic));
        assertFalse(StyleConstants.isBold(italic));

        assertTrue(StyleConstants.isBold(bold));
        assertFalse(StyleConstants.isItalic(bold));

        assertTrue(StyleConstants.isBold(boldItalic));
        assertTrue(StyleConstants.isItalic(boldItalic));

        assertEquals(Color.BLUE, StyleConstants.getForeground(name));
        assertEquals(16, StyleConstants.getFontSize(name));

        assertEquals(0.0f, 0.6f, 0.0f, 1.0f, StyleConstants.getForeground(system).getRGB());
        assertTrue(StyleConstants.isItalic(system));
        assertEquals(14, StyleConstants.getFontSize(system));

        assertEquals(10, StyleConstants.getFontSize(small));
        assertEquals(16, StyleConstants.getFontSize(large));
    }

    @Test
    public void testActionPerformed() {
        InternalChatFrame icf = new InternalChatFrame(null) {
            @Override
            void maybeShowPopup(MouseEvent e) {
            }
        };
        JList list = new JList();
        list.addMouseListener(icf);
        JTextField txtInput = new JTextField();
        txtInput.addActionListener(icf.parent);
        txtInput.setActionCommand("sendInput");

        icf.actionPerformed(new ActionEvent(list, 1, "chooseColor"));
        assertNull(icf.doc.getStyle("newColor"));

        icf.actionPerformed(new ActionEvent(list, 1, "bgColor"));
        assertNull(icf.showTxt.getBackground());

        icf.actionPerformed(new ActionEvent(list, 1, "whisper"));
        assertEquals("/w ", icf.txtInput.getText());
    }

    @Test
    public void testMouseListener() {
        InternalChatFrame icf = new InternalChatFrame(null);
        JList list = icf.list;
        JTextPane showTxt = icf.showTxt;
        MouseEvent e1 = new MouseEvent(list, 1, 1, 1, 1, MouseEvent.MOUSE_CLICKED);
        MouseEvent e2 = new MouseEvent(showTxt, 1, 1, 1, 1, MouseEvent.MOUSE_CLICKED);

        icf.mouseClicked(e1);
        icf.listPop.show(list, 1, 1);

        icf.mouseClicked(e2);
        icf.txtPop.show(showTxt, 1, 1);
    }
}