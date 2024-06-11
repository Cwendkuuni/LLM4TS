
package dsachat.client.gui;

import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {

    private InternalChatFrame frame;

    @Before
    public void setUp() {
        frame = new InternalChatFrame(new MainFrame());
    }

    @Test
    public void testAddText() {
        frame.addText("You: Hello World!");
        assertEquals("You: Hello World!\n", frame.showTxt.getText());
    }

    @Test
    public void testAddUser() {
        frame.addUser("User1");
        assertEquals("User1", frame.list.getSelectedValue());
    }

    @Test
    public void testDeleteUser() {
        frame.addUser("User1");
        frame.deleteUser("User1");
        assertEquals(0, frame.listModel.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTextNullString() {
        frame.addText(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTextInvalidStyleName() {
        frame.addText(":invalid_style: Hello World!");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDeleteUserNonexistentUser() {
        frame.deleteUser("User100");
    }

    @After
    public void tearDown() {
        frame.dispose();
    }
}
