package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

    private InternalChatFrame testFrame;
    private MainFrame parentFrame;

    @Before
    public void setUp() {
        parentFrame = new MainFrame();
        testFrame = new InternalChatFrame(parentFrame);
        testFrame.setVisible(true);
    }

    @After
    public void tearDown() {
        testFrame.dispose();
        SwingUtilities.invokeLater(() -> parentFrame.dispose());
    }

    @Test
    public void testInternalChatFrameConstructor() {
        assertNotNull(testFrame);
    }

    @Test
    public void testCreateGUI() {
        assertNotNull(testFrame.getList());
        assertNotNull(testFrame.getShowTxt());
        assertNotNull(testFrame.getTextInput());
        assertEquals(2, testFrame.getContentPane().getComponentCount());
    }

    @Test
    public void testAddText() {
        String line = ":You Hello";
        testFrame.addText(line);
        assertEquals(line, testFrame.getShowTxt().getText());
    }

    @Test
    public void testAddTextWithStyles() {
        String line = "*bold*This is a bold text*italic* and this is an italic text.";
        testFrame.addText(line);
        String expectedText = "This is a bold text and this is an italic text.\n";
        assertEquals(expectedText, testFrame.getShowTxt().getText());
    }

    @Test(expected = BadLocationException.class)
    public void testAddTextWithException() {
        String line = "Invalid text";
        testFrame.addText(line);
    }

    @Test
    public void testAddStylesToDocument() {
        StyledDocument doc = testFrame.getShowTxt().getStyledDocument();
        int styleCount = doc.getStyleCount();
        assertEquals(14, styleCount);
    }

    @Test
    public void testChangeStyle() {
        testFrame.changeStyle(14, Color.black, false, false, "user1");
        Style style = testFrame.getShowTxt().getStyledDocument().getStyle("user1");
        Color newColor = style.getForeground();
        assertEquals(Color.black, newColor);
    }

    @Test
    public void testAddUser() {
        testFrame.addUser("user1");
        assertEquals(1, testFrame.getUserlist().length);
        assertEquals("user1", testFrame.getUserlist()[0]);
        assertEquals("user1", testFrame.getList().getModel().getElementAt(0).toString());
    }

    @Test
    public void testDeleteUser() {
        testFrame.addUser("user1");
        testFrame.deleteUser("user1");
        assertEquals(0, testFrame.getUserlist().length);
    }

    @Test
    public void testGetUserlist() {
        testFrame.addUser("user1");
        testFrame.addUser("user2");
        String[] userlist = testFrame.getUserlist();
        assertEquals(2, userlist.length);
        assertEquals("user1", userlist[0]);
        assertEquals("user2", userlist[1]);
    }

    @Test
    public void testActionPerformedWhisper() {
        String expectedText = "/w user1 ";
        testFrame.getList().setSelectedValue("user1", true);
        testFrame.getTextInput().setText("test");
        testFrame.getTextInput().doClick();
        testFrame.getTextInput().dispatchEvent(new ActionEvent(testFrame.getTextInput(), ActionEvent.ACTION_PERFORMED, "sendInput"));
        assertEquals(expectedText, testFrame.getShowTxt().getText());
    }

    @Test
    public void testActionPerformedChangeColor() {
        Color newColor = Color.RED;
        testFrame.getList().setSelectedValue("user1", true);
        testFrame.getTextInput().setText("Change Color");
        testFrame.getTextInput().doClick();
        testFrame.getTextInput().dispatchEvent(new ActionEvent(testFrame.getTextInput(), ActionEvent.ACTION_PERFORMED, "chooseColor"));
        JColorChooser dialog = new JColorChooser();
        dialog.setSelectedColor(newColor);
        dialog.setVisible(true);
        testFrame.getTextInput().setText("OK");
        testFrame.getTextInput().dispatchEvent(new ActionEvent(testFrame.getTextInput(), ActionEvent.ACTION_PERFORMED, "chooseColor"));
        assertEquals(newColor, testFrame.getShowTxt().getBackground());
    }

    @Test
    public void testActionPerformedBGColor() {
        Color newColor = Color.GREEN;
        testFrame.getTextInput().setText("Change Background Color");
        testFrame.getTextInput().doClick();
        testFrame.getTextInput().dispatchEvent(new ActionEvent(testFrame.getTextInput(), ActionEvent.ACTION_PERFORMED, "bgColor"));
        JColorChooser dialog = new JColorChooser();
        dialog.setSelectedColor(newColor);
        dialog.setVisible(true);
        testFrame.getTextInput().setText("OK");
        testFrame.getTextInput().dispatchEvent(new ActionEvent(testFrame.getTextInput(), ActionEvent.ACTION_PERFORMED, "bgColor"));
        assertEquals(newColor, testFrame.getShowTxt().getBackground());
    }

    @Test
    public void testListSelectionListener() {
        final ArrayList<String> selectedUsers = new ArrayList<>();
        ListSelectionListener listener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                selectedUsers.clear();
                if (e.getSource() instanceof JList) {
                    JList list = (JList) e.getSource();
                    for (int i = 0; i < list.getSelectedIndices().length; i++) {
                        selectedUsers.add(list.getModel().getElementAt(list.getSelectedIndices()[i]).toString());
                    }
                }
            }
        };

        testFrame.getList().addListSelectionListener(listener);
        testFrame.getList().setSelectedValue("user1", true);
        testFrame.getList().setSelectedValue("user2", true);
        assertEquals(2, selectedUsers.size());
        assertEquals("user1", selectedUsers.get(0));
        assertEquals("user2", selectedUsers.get(1));
    }
}