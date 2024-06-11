package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InternalChatFrameTest {
    @Test
    public void testConstructor() {
        MainFrame mainFrame = new MainFrame(); // Assuming MainFrame is initialized
        InternalChatFrame frame = new InternalChatFrame(mainFrame);
        assertEquals("Chat", frame.getTitle());
        assertNotNull(frame.list);
        assertNotNull(frame.showTxt);
        assertNotNull(frame.txtInput);
        assertNotNull(frame.parent);
        assertNotNull(frame.doc);
        assertNotNull(frame.listPop);
        assertNotNull(frame.txtPop);
    }

    @Test
    public void testCreateGUI() {
        InternalChatFrame frame = new InternalChatFrame(new JFrame());
        JList list = frame.list;
        JTextPane showTxt = frame.showTxt;
        JTextField txtInput = frame.txtInput;
        JInternalFrame internalFrame = (JInternalFrame) frame;
        GridBagConstraints c = new GridBagConstraints();
        JSplitPane split = (JSplitPane) internalFrame.getContentPane();
        JScrollPane scrList = (JScrollPane) split.getLeftComponent();
        JScrollPane scrTxt = (JScrollPane) split.getRightComponent();
        JPanel right = (JPanel) scrTxt.getViewport().getComponent(0);

        // Assert that the components are added to the frame and their layout
        // is correct
        assertEquals(GridBagLayout.VERTICAL_BASELINE, internalFrame.getLayout().getLayoutAlignmentX(), 0.01);
        assertEquals(GridBagLayout.VERTICAL_BASELINE, internalFrame.getLayout().getLayoutAlignmentY(), 0.01);
        assertEquals(1, internalFrame.getComponents().length);
        assertEquals(split, internalFrame.getContentPane());
        assertEquals(1, split.getComponents().length);
        assertEquals(2, split.getDividerLocation());
        assertEquals(list, scrList.getViewport().getComponent(0));
        assertEquals(showTxt, scrTxt.getViewport().getComponent(0));
        assertEquals(right.getLayout(), new GridBagLayout());
        assertEquals(2, right.getComponentCount());
        assertEquals(txtInput, right.getComponent(0));
        assertEquals(showTxt, right.getComponent(1));
    }

    @Test
    public void testAddText() {
        InternalChatFrame frame = new InternalChatFrame(new JFrame());
        String line = "Test line";
        frame.addText(line);
        StyledDocument doc = frame.doc;
        int length = doc.getLength();
        String text = doc.getText(0, length);
        assertEquals(line, text);
    }

    @Test
    public void testAddTextEdgeCases() {
        InternalChatFrame frame = new InternalChatFrame(new JFrame());
        String line = "";
        try {
            frame.addText(line);
            fail();
        } catch (IllegalArgumentException e) {
            // Assert that the correct exception is thrown
            assertEquals("Empty line", e.getMessage());
        }

        line = "Test:line";
        try {
            frame.addText(line);
            fail();
        } catch (BadLocationException e) {
            // Assert that the correct exception is thrown
            assertEquals("Invalid document position", e.getMessage());
        }
    }

    @Test
    public void testAddTextNullLine() {
        InternalChatFrame frame = new InternalChatFrame(new JFrame());
        String line = null;
        try {
            frame.addText(line);
            fail();
        } catch (NullPointerException e) {
            // Assert that the correct exception is thrown
        }
    }

    // Add test cases for other methods of the InternalChatFrame class
}