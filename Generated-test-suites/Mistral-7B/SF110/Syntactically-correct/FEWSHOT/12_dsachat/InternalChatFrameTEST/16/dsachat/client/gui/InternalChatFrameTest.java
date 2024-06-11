package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

    private static final int DEFAULT_FONT_SIZE = 12;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    private JFrame frame;
    private InternalChatFrame chatFrame;
    private JList list;
    private JTextPane showTxt;
    private JTextField txtInput;

    @Before
    public void setUp() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatFrame = new InternalChatFrame(frame);
        frame.getContentPane().add(chatFrame);
        frame.pack();
        frame.setVisible(true);

        list = chatFrame.list;
        showTxt = chatFrame.showTxt;
        txtInput = chatFrame.txtInput;
    }

    @After
    public void tearDown() {
        frame.dispose();
    }

    @Test
    public void testCreateGUI() {
        assertNotNull(chatFrame.list);
        assertNotNull(chatFrame.showTxt);
        assertNotNull(chatFrame.txtInput);
    }

    @Test
    public void testAddText() {
        String line = "You: Hello";
        chatFrame.addText(line);
        assertEquals(line, showTxt.getText());

        line = "*User1: Hello*";
        chatFrame.addText(line);
        assertEquals("You: Hello\n" + "*User1: Hello*", showTxt.getText());

        line = "*User1*: Hello*";
        chatFrame.addText(line);
        assertEquals("You: Hello\n" + "*User1: Hello", showTxt.getText());

        line = "*User1+User2*: Hello*";
        chatFrame.addText(line);
        assertEquals("You: Hello\n" + "*User1: Hello\n" + "*User1+User2: Hello", showTxt.getText());

        line = "User1: Hello User2";
        chatFrame.addText(line);
        assertEquals("You: Hello\n" + "*User1: Hello User2", showTxt.getText());

        line = "*User1: Hello User2: world";
        chatFrame.addText(line);
        assertEquals("You: Hello\n" + "*User1: Hello User2: world", showTxt.getText());

        line = "*User1: Hello User2: world*";
        chatFrame.addText(line);
        assertEquals("You: Hello\n" + "*User1: Hello User2: world", showTxt.getText());
    }

    @Test
    public void testAddStylesToDocument() {
        chatFrame.addStylesToDocument(chatFrame.doc);

        StyledDocument doc = chatFrame.doc;
        Style defaultStyle = doc.getStyle("default");
        Style regularStyle = doc.getStyle("regular");
        Style italicStyle = doc.getStyle("italic");
        Style boldStyle = doc.getStyle("bold");
        Style boldItalicStyle = doc.getStyle("bold-italic");
        Style nameStyle = doc.getStyle("name");
        Style systemStyle = doc.getStyle("system");
        Style smallStyle = doc.getStyle("small");
        Style largeStyle = doc.getStyle("large");

        assertEquals(defaultStyle, regularStyle.getParent());

        assertEquals(Color.BLUE, nameStyle.getForeground());
        assertEquals(16, nameStyle.getFontSize());

        assertEquals(Color.BLACK, systemStyle.getForeground());
        assertEquals(new Color(0.0f, 0.6f, 0.0f, 1.0f), systemStyle.getBackground());
        assertEquals(true, systemStyle.isItalic());
        assertEquals(14, systemStyle.getFontSize());

        assertEquals(Color.BLACK, defaultStyle.getForeground());
        assertEquals(Color.decode("#808080"), defaultStyle.getBackground());
        assertEquals(false, defaultStyle.isBold());
        assertEquals(false, defaultStyle.isItalic());

        assertEquals(Color.BLACK, regularStyle.getForeground());
        assertEquals(Color.decode("#808080"), regularStyle.getBackground());
        assertEquals(false, regularStyle.isBold());
        assertEquals(false, regularStyle.isItalic());

        assertEquals(Color.BLACK, italicStyle.getForeground());
        assertEquals(Color.decode("#808080"), italicStyle.getBackground());
        assertEquals(true, italicStyle.isBold());
        assertEquals(false, italicStyle.isItalic());

        assertEquals(Color.BLACK, boldStyle.getForeground());
        assertEquals(Color.decode("#808080"), boldStyle.getBackground());
        assertEquals(false, boldStyle.isBold());
        assertEquals(false, boldStyle.isItalic());

        assertEquals(Color.BLACK, boldItalicStyle.getForeground());
        assertEquals(Color.decode("#808080"), boldItalicStyle.getBackground());
        assertEquals(true, boldItalicStyle.isBold());
        assertEquals(true, boldItalicStyle.isItalic());
    }

    @Test
    public void testChangeStyle() {
        chatFrame.changeStyle(24, null, true, false, "name");
        Style nameStyle = chatFrame.doc.getStyle("name");
        assertEquals(24, nameStyle.getFontSize());

        chatFrame.changeStyle(DEFAULT_FONT_SIZE, Color.RED, false, false, "name");
        assertEquals(DEFAULT_FONT_SIZE, nameStyle.getFontSize());
        assertEquals(Color.RED, nameStyle.getForeground());

        chatFrame.changeStyle(DEFAULT_FONT_SIZE, null, true, false, "name");
        assertEquals(DEFAULT_FONT_SIZE, nameStyle.getFontSize());
        assertEquals(Color.BLACK, nameStyle.getForeground());

        chatFrame.changeStyle(DEFAULT_FONT_SIZE, Color.RED, true, false, "name");
        assertEquals(DEFAULT_FONT_SIZE, nameStyle.getFontSize());
        assertEquals(Color.RED, nameStyle.getForeground());

        chatFrame.changeStyle(DEFAULT_FONT_SIZE, Color.RED, false, true, "name");
        assertEquals(DEFAULT_FONT_SIZE, nameStyle.getFontSize());
        assertEquals(Color.RED, nameStyle.getForeground());
        assertEquals(true, nameStyle.isItalic());

        chatFrame.changeStyle(DEFAULT_FONT_SIZE, Color.RED, true, true, "name");
        assertEquals(DEFAULT_FONT_SIZE, nameStyle.getFontSize());
        assertEquals(Color.RED, nameStyle.getForeground());
        assertEquals(true, nameStyle.isBold());
        assertEquals(true, nameStyle.isItalic());
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        assertEquals(1, chatFrame.listModel.size());
        assertEquals("User1", chatFrame.list.getModel().getElementAt(0));

        chatFrame.addText("User1: Hello");
        assertEquals("User1: Hello", chatFrame.showTxt.getText());

        chatFrame.deleteUser("User1");
        assertEquals(0, chatFrame.listModel.size());
    }

    @Test
    public void testGetUserlist() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");

        String[] userlist = chatFrame.getUserlist();
        assertEquals(2, userlist.length);
        assertEquals("User1", userlist[0]);
        assertEquals("User2", userlist[1]);
    }

    @Test
    public void testActionPerformed() {
        chatFrame.txtInput.setText("User1");

        MouseEvent me = new MouseEvent(chatFrame.list, 1, System.currentTimeMillis(), 0, 0, 0, 1, false);
        chatFrame.actionPerformed(new ActionEvent(chatFrame.list, 0, "chooseColor"));

        JColorChooser chooser = new JColorChooser();
        Color newColor = chooser.showDialog(chatFrame, "Choose Color for user User1", Color.BLUE);
        if (newColor != null) {
            chatFrame.changeStyle(DEFAULT_FONT_SIZE, newColor, false, false, "User1");
        }

        assertEquals(Color.BLUE, chatFrame.list.getBackground());
        assertEquals(Color.BLUE, showTxt.getBackground());

        chatFrame.txtInput.setText("User2");
        me = new MouseEvent(chatFrame.list, 1, System.currentTimeMillis(), 0, 0, 0, 1, false);
        chatFrame.actionPerformed(new ActionEvent(chatFrame.list, 0, "deleteUser"));

        assertEquals(1, chatFrame.listModel.size());
        assertEquals("User1", chatFrame.list.getModel().getElementAt(0));
    }

    @Test
    public void testMousePressed() {
        chatFrame.list.setCellRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                return label;
            }
        });

        SwingUtilities.invokeLater(() -> {
            chatFrame.list.setSelectedIndex(0);
        });

        JPopupMenu popupMenu = chatFrame.listPop;
        assertEquals(0, popupMenu.getComponentCount());

        MouseEvent me = new MouseEvent(chatFrame.list, 1, System.currentTimeMillis(), 0, 0, 0, 1, false);
        chatFrame.maybeShowPopup(me);

        assertEquals(2, popupMenu.getComponentCount());
        assertEquals("Change Color", popupMenu.getComponentAt(0).getText());
        assertEquals("whisper", popupMenu.getComponentAt(1).getText());
    }

    @Test
    public void testMouseReleased() {
        JPopupMenu popupMenu = chatFrame.listPop;
        assertEquals(0, popupMenu.getComponentCount());

        MouseEvent me = new MouseEvent(chatFrame.list, 1, System.currentTimeMillis(), 0, 0, 0, 1, false);
        chatFrame.maybeShowPopup(me);

        JMenuItem chooseColorItem = (JMenuItem) popupMenu.getComponentAt(0);
        assertEquals("Change Color", chooseColorItem.getText());
        assertEquals("chooseColor", chooseColorItem.getActionCommand());

        ActionEvent ae = new ActionEvent(chooseColorItem, 0, "chooseColor");
        chatFrame.actionPerformed(ae);

        JColorChooser chooser = new JColorChooser();
        Color newColor = chooser.showDialog(chatFrame, "Choose Color for user User1", Color.BLUE);
        if (newColor != null) {
            chatFrame.changeStyle(DEFAULT_FONT_SIZE, newColor, false, false, "User1");
        }

        assertEquals(Color.BLUE, chatFrame.list.getBackground());
        assertEquals(Color.BLUE, showTxt.getBackground());

        popupMenu = chatFrame.txtPop;
        assertEquals(0, popupMenu.getComponentCount());

        me = new MouseEvent(chatFrame.showTxt, 1, System.currentTimeMillis(), 0, 0, 0, 1, false);
        chatFrame.maybeShowPopup(me);

        assertEquals(1, popupMenu.getComponentCount());
        assertEquals("Change Color", popupMenu.getComponentAt(0).getText());
        assertEquals("bgColor", popupMenu.getComponentAt(0).getActionCommand());

        ae = new ActionEvent(popupMenu.getComponentAt(0), 0, "bgColor");
        chatFrame.actionPerformed(ae);

        chooser = new JColorChooser();
        newColor = chooser.showDialog(chatFrame, "Choose Color for user User1", Color.GREEN);
        if (newColor != null) {
            chatFrame.changeStyle(DEFAULT_FONT_SIZE, newColor, false, false, "User1");
        }

        assertEquals(Color.GREEN, chatFrame.list.getBackground());
        assertEquals(Color.GREEN, showTxt.getBackground());
    }
}