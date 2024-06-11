package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.ListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

    private InternalChatFrame frame;
    private MainFrame parent;
    private StyledDocument doc;

    @Before
    public void setUp() {
        parent = new MainFrame() {
            @Override
            public void setSize(int width, int height) {
                // Do nothing
            }

            @Override
            public void pack() {
                // Do nothing
            }
        };
        frame = new InternalChatFrame(parent);
        doc = frame.doc;
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> frame.dispose());
    }

    @Test
    public void testCreateGUI() {
        assertEquals("Chat", frame.getTitle());
        assertEquals(true, frame.isResizable());
        assertEquals(true, frame.isFocusable());
        assertEquals(true, frame.isVisible());

        JList list = frame.list;
        assertEquals(0, list.getModel().getSize());
        assertEquals("", list.getSelectedValue());
        assertNull(list.getPopupMenu());

        JTextPane showTxt = frame.showTxt;
        assertEquals(false, showTxt.isEditable());
        assertEquals("", showTxt.getText());

        JTextField txtInput = frame.txtInput;
        assertEquals(false, txtInput.isEditable());
        assertEquals("", txtInput.getText());
        assertEquals("sendInput", txtInput.getActionCommand());

        assertNull(frame.listPop);
        assertNull(frame.txtPop);
    }

    @Test
    public void testAddText() {
        frame.addText("You: Hello");
        assertEquals("You: Hello\n", showTxt.getText());

        frame.addText("User1: World");
        assertEquals("You: Hello\nUser1: World\n", showTxt.getText());

        frame.addText("*User2*: Hello");
        assertEquals("You: Hello\nUser1: World\n*User2*: Hello\n", showTxt.getText());

        frame.addText("User1: *Hello*");
        assertEquals("You: Hello\nUser1: *Hello*\nUser1: World\n*User2*: Hello\n", showTxt.getText());

        frame.addText("*User2*: *Hello*");
        assertEquals("You: Hello\nUser1: *Hello*\nUser1: World\n*User2*: *Hello*\n", showTxt.getText());

        frame.addText("User1: **Hello**");
        assertEquals("You: Hello\nUser1: **Hello**\nUser1: World\n*User2*: *Hello*\n", showTxt.getText());

        frame.addText("*User2*: **Hello**");
        assertEquals("You: Hello\nUser1: **Hello**\nUser1: World\n*User2*: **Hello**\n", showTxt.getText());

        frame.addText("User1: ***Hello***");
        assertEquals("You: Hello\nUser1: ***Hello***\nUser1: World\n*User2*: **Hello**\n", showTxt.getText());

        frame.addText("*User2*: ***Hello***");
        assertEquals("You: Hello\nUser1: ***Hello***\nUser1: World\n*User2*: ***Hello***\n", showTxt.getText());
    }

    @Test
    public void testAddStylesToDocument() {
        frame.addStylesToDocument(doc);

        List<String> styles = Arrays.asList("regular", "italic", "bold", "bold-italic", "name", "system", "small", "large");
        for (String styleName : styles) {
            Style style = doc.getStyle(styleName);
            assertNotNull(style);
            assertSame(style, doc.getStyle(styleName));
        }
    }

    @Test
    public void testAddUser() {
        frame.addUser("User1");
        ListCellRenderer renderer = list.getCellRenderer();
        JLabel label = (JLabel) renderer.getListCellRendererComponent(list, "User1", 0, false, false);
        assertEquals("User1", label.getText());

        frame.addText("User1: World");
        assertEquals("You: Hello\nUser1: World\n", showTxt.getText());

        frame.addUser("User2");
        label = (JLabel) renderer.getListCellRendererComponent(list, "User2", 1, false, false);
        assertEquals("User2", label.getText());

        frame.addText("User2: Hello");
        assertEquals("You: Hello\nUser1: World\nUser2: Hello\n", showTxt.getText());
    }

    @Test
    public void testDeleteUser() {
        frame.addUser("User1");
        frame.addText("User1: World");
        assertEquals("You: Hello\nUser1: World\n", showTxt.getText());

        frame.deleteUser("User1");
        assertEquals("", showTxt.getText());
        assertEquals(0, list.getModel().getSize());
    }

    @Test
    public void testGetUserlist() {
        frame.addUser("User1");
        frame.addUser("User2");
        String[] expected = {"User1", "User2"};
        assertArrayEquals(expected, frame.getUserlist());
    }

    @Test
    public void testActionPerformedWhenChooseColor() {
        final Color newColor = Color.RED;
        SwingUtilities.invokeLater(() -> {
            internalFrameMouseEvent(MouseEvent.MOUSE_EVENT_MOUSE_CLICKED, frame.list);
            frame.actionPerformed(new ActionEvent(frame, 0, "chooseColor"));
            JPopupMenu popup = frame.listPop;
            assertNotNull(popup);
            JMenuItem item = (JMenuItem) popup.getComponent(0);
            assertSame(item, popup.getComponent(0));
            Color oldColor = (Color) item.getClientProperty("oldColor");
            assertNull(oldColor);
            item.setBackground(newColor);
            item.putClientProperty("oldColor", oldColor);
            SwingUtilities.invokeLater(() -> frame.changeStyle(12, newColor, false, false, frame.list.getSelectedValue().toString()));
        });

        SwingUtilities.invokeLater(() -> {
            assertEquals("User1", frame.list.getSelectedValue());
            JTextPane textPane = frame.showTxt;
            assertEquals(newColor, textPane.getBackground());
        });
    }

    @Test
    public void testActionPerformedWhenBgColor() {
        final Color newColor = Color.GREEN;
        SwingUtilities.invokeLater(() -> {
            internalFrameMouseEvent(MouseEvent.MOUSE_EVENT_MOUSE_CLICKED, frame.showTxt);
            frame.actionPerformed(new ActionEvent(frame, 0, "bgColor"));
            JPopupMenu popup = frame.txtPop;
            assertNotNull(popup);
            JMenuItem item = (JMenuItem) popup.getComponent(0);
            assertSame(item, popup.getComponent(0));
            Color oldColor = (Color) item.getClientProperty("oldColor");
            assertNull(oldColor);
            item.setBackground(newColor);
            item.putClientProperty("oldColor", oldColor);
            SwingUtilities.invokeLater(() -> frame.changeStyle(12, null, false, false, frame.list.getSelectedValue().toString()));
        });

        SwingUtilities.invokeLater(() -> {
            JTextPane textPane = frame.showTxt;
            assertEquals(newColor, textPane.getBackground());
        });
    }

    @Test
    public void testActionPerformedWhenWhisper() {
        String recipient = "/w User1";
        SwingUtilities.invokeLater(() -> {
            frame.txtInput.setText(recipient);
            frame.actionPerformed(new ActionEvent(frame, 0, "sendInput"));
        });

        SwingUtilities.invokeLater(() -> {
            assertEquals(recipient, frame.txtInput.getText());
            String chatLog = frame.getChatlog();
            assertEquals("", chatLog);
        });
    }

    @Test
    public void testMousePressedAndMaybeShowPopup() {
        SwingUtilities.invokeLater(() -> {
            internalFrameMouseEvent(MouseEvent.MOUSE_EVENT_MOUSE_PRESSED, frame.list);
            internalFrameMouseEvent(MouseEvent.MOUSE_EVENT_MOUSE_RELEASED, frame.list);
        });

        SwingUtilities.invokeLater(() -> {
            JPopupMenu popup = frame.listPop;
            assertNotNull(popup);
            popup.dispose();
        });

        SwingUtilities.invokeLater(() -> {
            internalFrameMouseEvent(MouseEvent.MOUSE_EVENT_MOUSE_PRESSED, frame.showTxt);
            internalFrameMouseEvent(MouseEvent.MOUSE_EVENT_MOUSE_RELEASED, frame.showTxt);
        });

        SwingUtilities.invokeLater(() -> {
            JPopupMenu popup = frame.txtPop;
            assertNotNull(popup);
            popup.dispose();
        });
    }

    private void internalFrameMouseEvent(int id, Component component) {
        MouseEvent mouseEvent = new MouseEvent(component, id, System.currentTimeMillis(), InputEvent.BUTTON1_MASK, 0, 0, 0, 0, false);
        SwingUtilities.invokeLater(() -> {
            component.dispatchEvent(mouseEvent);
            if (MouseEvent.MOUSE_EVENT_MOUSE_CLICKED == id) {
                Component parent = component.getParent();
                if (parent instanceof JComponent) {
                    JComponent jComponent = (JComponent) parent;
                    jComponent.setSelected(true);
                }
                ListSelectionModel model = (ListSelectionModel) component.getModel();
                if (component instanceof JList) {
                    model.setSelectionInterval(component.getIndex(), component.getIndex());
                    model.fireValueChanged(new ListSelectionEvent(component, 0, component.getIndex(), component.getIndex()));
                } else if (component instanceof JTextPane) {
                    model.setSelectionInterval(component.getCaretPosition(), component.getCaretPosition());
                    model.fireValueChanged(new ListSelectionEvent(component, 0, component.getSelectedIndex(), component.getSelectedIndex()));
                }
            }
        });
    }
}