package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.ParagraphView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

    private JInternalFrame chatFrame;
    private JList list;
    private JTextPane showTxt;
    private JTextField txtInput;

    @Before
    public void setup() {
        SwingUtilities.invokeLater(() -> {
            final MainFrame parent = new MainFrame();
            chatFrame = new InternalChatFrame(parent);
            chatFrame.setVisible(true);
            list = chatFrame.list;
            showTxt = chatFrame.showTxt;
            txtInput = chatFrame.txtInput;
        });
    }

    @After
    public void tearDown() {
        chatFrame.dispose();
    }

    @Test
    public void testAddText() throws BadLocationException {
        SwingUtilities.invokeLater(() -> {
            chatFrame.addText("You: Hello, world!");
            final Document doc = showTxt.getDocument();
            final int length = doc.getLength();
            assertEquals("You: Hello, world!\n", doc.getText(0, length));
        });
    }

    @Test
    public void testAddStylesToDocument() {
        SwingUtilities.invokeLater(() -> {
            final Document doc = showTxt.getDocument();
            final Style def = doc.getDefaultStyle();
            final Style regular = doc.getStyle("regular");
            assertNotNull(regular);
            assertEquals(def, regular.getParent());
            assertTrue(regular.isParagraph());
            assertTrue(regular.isLeaf());
            assertNull(regular.getName());
            final ParagraphView pv = ((ParagraphElement) regular).getView();
            assertNotNull(pv);
            assertEquals(StyleConstants.getDefaultParagraphAlignment(regular), ParagraphView.ALIGN_LEFT);
            assertEquals(StyleConstants.getDefaultParagraphIndent(regular), 0);
            final DefaultCaret caret = (DefaultCaret) showTxt.getCaret();
            assertEquals(StyleConstants.DEFAULT_MARGIN, caret.getMargin());
        });
    }

    @Test
    public void testChangeStyle() {
        SwingUtilities.invokeLater(() -> {
            chatFrame.changeStyle(12, Color.RED, true, false, "name");
            final Style name = chatFrame.doc.getStyle("name");
            assertTrue(name.isBold());
            assertTrue(name.isItalic());
            assertTrue(name.getForeground().equals(Color.RED));
            assertEquals(12, name.getFontSize());
        });
    }

    @Test
    public void testGetUserlist() {
        SwingUtilities.invokeLater(() -> {
            chatFrame.addUser("Alice");
            chatFrame.addUser("Bob");
            final String[] userlist = chatFrame.getUserlist();
            assertEquals(2, userlist.length);
            assertEquals("Alice", userlist[0]);
            assertEquals("Bob", userlist[1]);
        });
    }

    @Test
    public void testActionPerformed() {
        SwingUtilities.invokeLater(() -> {
            final String name = "Alice";
            chatFrame.listModel.addElement(name);
            txtInput.setText("Message");
            txtInput.doClick();
            txtInput.dispatchEvent(new ActionEvent(txtInput, 0, "sendInput"));
            chatFrame.actionPerformed(new ActionEvent(txtInput, 0, "sendInput"));
            final Document doc = showTxt.getDocument();
            assertEquals("Alice: Message\n", doc.getText(0, doc.getLength()));
        });
    }

    @Test
    public void testMouseListener() {
        SwingUtilities.invokeLater(() -> {
            final int index = 0;
            final String name = "Alice";
            chatFrame.listModel.addElement(name);
            final MouseEvent e = new MouseEvent(list, 1, System.currentTimeMillis(), 0, index, 0, 0, false);
            chatFrame.list.dispatchEvent(e);
            chatFrame.maybeShowPopup(e);
            assertTrue(listPop.isVisible());
        });
    }
}