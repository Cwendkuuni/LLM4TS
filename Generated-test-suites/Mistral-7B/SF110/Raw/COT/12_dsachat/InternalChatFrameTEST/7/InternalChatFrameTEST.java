package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private InternalChatFrame chatFrame;
    private JList list;
    private JTextPane showTxt;
    private JTextPane textPaneSpy;
    private JFrame spyFrame;

    @Before
    public void setUp() {
        SwingUtilities.invokeLater(() -> {
            chatFrame = new InternalChatFrame(new MainFrame()) {
                @Override
                public void createGUI() {
                    super.createGUI();
                    list = (JList) findComponentByName("list");
                    showTxt = (JTextPane) findComponentByName("showTxt");
                    textPaneSpy = new JTextPane();
                    textPaneSpy.setEditable(false);
                    spyFrame = new JFrame();
                    spyFrame.getContentPane().add(textPaneSpy);
                    spyFrame.pack();
                    list.setCellRenderer(new ListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                            textPaneSpy.setText(value.toString());
                            return textPaneSpy;
                        }
                    });
                }
            };
            chatFrame.createGUI();
            chatFrame.setSize(WIDTH, HEIGHT);
            chatFrame.setVisible(true);
        });
    }

    @After
    public void tearDown() {
        if (chatFrame != null) {
            chatFrame.dispose();
        }
    }

    @Test
    public void testInternalChatFrameConstructor() {
        assertNotNull(chatFrame);
    }

    @Test
    public void testAddText() {
        String line = "You: Hello";
        chatFrame.addText(line);
        assertEquals("You: Hello\n", showTxt.getText());

        String lineWithStyle = "*You *Hi there*";
        chatFrame.addText(lineWithStyle);
        assertEquals("You: Hi there\n", showTxt.getText());
        assertEquals("*You* Hi there\n", showTxt.getDocument().getText(0, showTxt.getText().length()));

        String lineWithColor = "You: Hello (color: red)";
        chatFrame.addText(lineWithColor);
        assertEquals("You: Hello (color: red)\n", showTxt.getText());
        assertEquals("You [color=red]Hello [/color]\n", showTxt.getDocument().getText(0, showTxt.getText().length()));

        String lineWithBoldAndItalic = "*You **Hi** there*";
        chatFrame.addText(lineWithBoldAndItalic);
        assertEquals("You: Hi there\n", showTxt.getText());
        assertEquals("*You* Hi there\n", showTxt.getDocument().getText(0, showTxt.getText().length()));
        assertEquals("*You**Hi**there*\n", showTxt.getDocument().getText(0, showTxt.getText().length()));
    }

    @Test(expected = BadLocationException.class)
    public void testAddTextWithBadLocationException() {
        chatFrame.addText("Hello");
        chatFrame.addText("World");
        chatFrame.addText("!@#$%^&*()_+|={\\}:[\\]?><,");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTextWithIllegalArgumentException() {
        chatFrame.addText(":Hello");
    }

    @Test
    public void testAddStylesToDocument() {
        chatFrame.addStylesToDocument(chatFrame.getDocument());
        List<String> styles = Arrays.asList("name", "system", "italic", "bold", "bold-italic", "regular");
        for (String style : styles) {
            assertNotNull(chatFrame.getDocument().getStyle(style));
        }
    }

    @Test
    public void testChangeStyle() {
        chatFrame.changeStyle(16, Color.RED, true, false, "name");
        assertEquals(Color.RED, this.getStyleByName("name").getForeground());
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        assertEquals(1, list.getModel().getSize());
        assertEquals("User1", list.getModel().get(0));
        assertEquals("User1", chatFrame.getDocument().getStyle("User1").getName());
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        assertEquals(2, list.getModel().getSize());
        chatFrame.deleteUser("User1");
        assertEquals(1, list.getModel().getSize());
        assertEquals("User2", list.getModel().get(0));
        assertNull(chatFrame.getDocument().getStyle("User1"));
    }

    @Test
    public void testGetUserlist() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] expectedUserlist = {"User1", "User2"};
        assertArrayEquals(expectedUserlist, chatFrame.getUserlist());
    }

    @Test
    public void testActionPerformed() {
        String name = "User1";
        chatFrame.addUser(name);
        chatFrame.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
        assertEquals(Color.BLACK, this.getStyleByName(name).getForeground());

        chatFrame.actionPerformed(new ActionEvent(list, 0, "bgColor"));
        assertNotNull(JColorChooser.showDialog(null, "Choose Color for user " + name, Color.BLACK));

        chatFrame.actionPerformed(new ActionEvent(list, 0, "whisper"));
        assertEquals("/w " + name + " ", txtInput.getText());
    }

    @Test
    public void testMouseClicked() {
        MouseEvent event = new MouseEvent(list, 1, 1, 1, 1, 1, 1, false);
        chatFrame.maybeShowPopup(event);
        assertNotNull(listPop);
        listPop.dispose();
    }

    private Style getStyleByName(String name) {
        return chatFrame.getDocument().getStyle(name);
    }
}