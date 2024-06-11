package dsachat.client.gui; // replace with your package name if different

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;
    private JList list;
    private JTextPane showTxt;
    private JTextField txtInput;

    @Before
    public void setUp() {
        // create and initialize the InternalChatFrame instance
        chatFrame = new InternalChatFrame(new MainFrame());
        list = chatFrame.list;
        showTxt = chatFrame.showTxt;
        txtInput = chatFrame.txtInput;
    }

    @Test
    public void testConstructor() {
        // check that the constructor is called and sets the title and other properties correctly
        assertEquals("Chat", chatFrame.getTitle());
        // other checks as needed
    }

    @Test
    public void testCreateGUI() {
        // check that the GUI components are created and added correctly
        assertTrue(list != null);
        assertTrue(showTxt != null);
        assertTrue(txtInput != null);
        // other checks as needed
    }

    @Test
    public void testAddText() {
        // add some test data and verify that it is displayed correctly
        chatFrame.addText("You: Hello");
        chatFrame.addText("User1: Hi");
        chatFrame.addText("*User2: *Hi there");

        String expectedChatlog = "You: Hello\nUser1: Hi\n*User2: *Hi there\n";
        assertEquals(expectedChatlog, chatFrame.getChatlog());
    }

    @Test
    public void testChangeStyle() {
        // test the changeStyle method with different parameter combinations
        chatFrame.changeStyle(12, Color.RED, true, false, "User1");
        assertNotEquals(Color.BLACK, chatFrame.getStyle("User1").getBackground());
        chatFrame.changeStyle(12, null, false, false, "User1");
        // other checks as needed
    }

    @Test
    public void testMouseEvents() {
        // test that mouse events are handled correctly
        MouseEvent mouseEvent = new MouseEvent(list, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 10, 10, 1, false);
        chatFrame.maybeShowPopup(mouseEvent);
        assertTrue(listPop.isShowing());
        // other checks as needed
    }
}