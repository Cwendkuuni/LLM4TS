package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;
    private MainFrame parentMock;
    private JList listSpy;
    private DefaultListModel listModelSpy;
    private JTextPane showTxtSpy;
    private JTextField txtInputSpy;
    private JPopupMenu listPopSpy;
    private JPopupMenu txtPopSpy;

    @Before
    public void setUp() {
        parentMock = mock(MainFrame.class);
        chatFrame = new InternalChatFrame(parentMock);
        listModelSpy = spy(new DefaultListModel());
        listSpy = new JList(listModelSpy);
        showTxtSpy = mock(JTextPane.class);
        txtInputSpy = mock(JTextField.class);
        listPopSpy = mock(JPopupMenu.class);
        txtPopSpy = mock(JPopupMenu.class);
        chatFrame.listModel = listModelSpy;
        chatFrame.list = listSpy;
        chatFrame.showTxt = showTxtSpy;
        chatFrame.txtInput = txtInputSpy;
        chatFrame.listPop = listPopSpy;
        chatFrame.txtPop = txtPopSpy;

        chatFrame.add(listSpy);
        chatFrame.add(showTxtSpy);
        chatFrame.add(txtInputSpy);
    }

    @Test
    public void testAddText() {
        chatFrame.addText("You: Hello World: Hello");

        verify(showTxtSpy, times(1)).setText("You: Hello World: Hello\n");

        assertEquals(1, listModelSpy.size());
        assertEquals("You: Hello World", listModelSpy.get(0));
    }

    @Test
    public void testAddTextWithBoldItalicStyles() {
        chatFrame.addText("**You:** *Hello World:* Hello");

        verify(showTxtSpy, times(1)).setText("**You:** *Hello World:* Hello\n");

        Style style = chatFrame.doc.getStyle("You");
        assertTrue(StyleConstants.isBold(style));
        assertTrue(StyleConstants.isItalic(style));

        verify(listModelSpy, times(1)).addElement("You: Hello World");
    }

    @Test
    public void testAddTextWithBoldStyles() {
        chatFrame.addText("**You:** Hello World");

        verify(showTxtSpy, times(1)).setText("**You:** Hello World\n");

        Style style = chatFrame.doc.getStyle("You");
        assertTrue(StyleConstants.isBold(style));
        assertFalse(StyleConstants.isItalic(style));

        verify(listModelSpy, times(1)).addElement("You: Hello World");
    }

    @Test
    public void testAddTextWithItalicStyles() {
        chatFrame.addText("You: *Hello World*");

        verify(showTxtSpy, times(1)).setText("You: Hello World\n");

        Style style = chatFrame.doc.getStyle("You");
        assertFalse(StyleConstants.isBold(style));
        assertTrue(StyleConstants.isItalic(style));

        verify(listModelSpy, times(1)).addElement("You: Hello World");
    }

    @Test
    public void testAddTextWithoutStyle() {
        chatFrame.addText("You: Hello");

        verify(showTxtSpy, times(1)).setText("You: Hello\n");

        Style style = chatFrame.doc.getStyle("You");
        assertFalse(StyleConstants.isBold(style));
        assertFalse(StyleConstants.isItalic(style));

        verify(listModelSpy, times(1)).addElement("You: Hello");
    }

    @Test
    public void testCreateGUI() {
        chatFrame.createGUI();

        assertEquals("Chat", chatFrame.getTitle());
        assertTrue(chatFrame.isResizable());

        JComponent[] components = chatFrame.getComponents();
        assertEquals(3, components.length);

        JSplitPane splitPane = (JSplitPane) components[0];
        assertEquals(JSplitPane.VERTICAL_SPLIT, splitPane.getOrientation());

        JList leftComponent = (JList) splitPane.getLeftComponent();
        assertEquals(listSpy, leftComponent);

        JComponent rightComponent = splitPane.getRightComponent();
        assertTrue(rightComponent instanceof JPanel);
        JPanel panel = (JPanel) rightComponent;

        JTextPane textPane = (JTextPane) panel.getComponent(0);
        assertEquals(showTxtSpy, textPane);

        JTextField textField = (JTextField) panel.getComponent(1);
        assertEquals(txtInputSpy, textField);
    }

    @Test
    public void testCreateGUI_SplitterLocation() {
        chatFrame.createGUI();

        JSplitPane splitPane = (JSplitPane) chatFrame.getComponents()[0];
        assertEquals(50, splitPane.getDividerLocation());
    }

    @Test
    public void testMouseClicked() {
        chatFrame.maybeShowPopup(mock(MouseEvent.class));

        verify(listPopSpy, never()).show(any(), anyInt(), anyInt());
        verify(txtPopSpy, never()).show(any(), anyInt(), anyInt());
    }

    @Test
    public void testMousePressed() {
        chatFrame.maybeShowPopup(mock(MouseEvent.class));

        verify(listPopSpy, times(1)).show(eq(listSpy), anyInt(), anyInt());
        verify(txtPopSpy, times(1)).show(eq(showTxtSpy), anyInt(), anyInt());
    }

    @Test
    public void testActionPerformed_chooseColor() {
        when(listSpy.getSelectedValue()).thenReturn("User");

        chatFrame.actionPerformed(mock(ActionEvent.class));

        Color newColor = Color.RED;
        chatFrame.changeStyle(12, newColor, false, false, "User");

        verify(listPopSpy, never()).add(any(JMenuItem.class));
        verify(txtPopSpy, never()).add(any(JMenuItem.class));
        verify(listModelSpy, never()).addElement(anyString());

        Style style = chatFrame.doc.getStyle("User");
        assertEquals(newColor, StyleConstants.getForeground(style));
    }

    @Test
    public void testActionPerformed_bgColor() {
        when(listSpy.getSelectedValue()).thenReturn("User");

        Color newColor = Color.GREEN;
        chatFrame.changeStyle(12, null, false, false, "User");
        chatFrame.showTxt.setBackground(newColor);

        verify(listPopSpy, never()).add(any(JMenuItem.class));
        verify(txtPopSpy, never()).add(any(JMenuItem.class));
        verify(listModelSpy, never()).addElement(anyString());

        assertEquals(newColor, chatFrame.showTxt.getBackground());
    }

    @Test
    public void testActionPerformed_whisper() {
        when(listSpy.getSelectedValue()).thenReturn("User");
        when(txtInputSpy.getText()).thenReturn("/w User Hello");

        chatFrame.actionPerformed(mock(ActionEvent.class));

        verify(parentMock, times(1)).sendMessage("/w User Hello");
    }
}