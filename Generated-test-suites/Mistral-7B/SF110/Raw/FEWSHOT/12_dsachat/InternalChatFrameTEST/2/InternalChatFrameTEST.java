package dsachat.client.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class InternalChatFrameTest {

    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String testLine = "You: Hello World";
        chatFrame.addText(testLine);
        StyledDocument doc = chatFrame.doc;
        Style regular = doc.getStyle("regular");
        Style name = doc.getStyle("name");

        assertEquals("You: Hello World\n", chatFrame.showTxt.getText());
        assertEquals(testLine, doc.getText(0, doc.getLength()));
        assertEquals(name, doc.getStyleAt(0));
        assertFalse(doc.isParagraphDocument());
        assertEquals(Arrays.asList("regular", "name"), doc.getCharacterIter(0).getAttributes().keySet().stream().map(Object::toString).toList());
    }

    @Test
    public void testAddTextWithBoldAndItalic() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String testLine = "*You**:Hello **World**";
        chatFrame.addText(testLine);
        StyledDocument doc = chatFrame.doc;
        Style regular = doc.getStyle("regular");
        Style name = doc.getStyle("name");
        Style bold = doc.getStyle("bold");
        Style italic = doc.getStyle("italic");

        assertEquals("*You**:Hello **World**\n", chatFrame.showTxt.getText());
        String text = doc.getText(0, doc.getLength());
        String[] splittedText = text.split("\n");
        assertEquals(2, splittedText.length);
        assertEquals(testLine, splittedText[0]);
        assertEquals("name", doc.getStyleAt(0).getName());
        assertEquals(Arrays.asList("regular", "name", "bold", "italic"), doc.getCharacterIter(0).getAttributes().keySet().stream().map(Object::toString).toList());
        assertTrue(doc.isParagraphDocument());
        assertEquals(2, doc.getCharacterCount());

        CharacterIterator iterator = doc.getCharacterIterator(0);
        int index = 0;
        boolean isBold = false;
        boolean isItalic = false;
        while (!iterator.done()) {
            Style style = doc.getStyleAt(iterator.getIndex());
            if (style.getName().equals("bold")) {
                isBold = true;
            }
            if (style.getName().equals("italic")) {
                isItalic = true;
            }
            if (isBold && isItalic) {
                break;
            }
            index++;
            iterator.next();
        }
        assertEquals(3, index);
        assertTrue(isBold);
        assertTrue(isItalic);
    }

    @Test
    public void testAddTextWithBold() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String testLine = "+You:Hello World";
        chatFrame.addText(testLine);
        StyledDocument doc = chatFrame.doc;
        Style regular = doc.getStyle("regular");
        Style name = doc.getStyle("name");
        Style bold = doc.getStyle("bold");

        assertEquals("+You:Hello World\n", chatFrame.showTxt.getText());
        String text = doc.getText(0, doc.getLength());
        String[] splittedText = text.split("\n");
        assertEquals(1, splittedText.length);
        assertEquals(testLine, splittedText[0]);
        assertEquals("name", doc.getStyleAt(0).getName());
        assertEquals(Arrays.asList("regular", "name", "bold"), doc.getCharacterIter(0).getAttributes().keySet().stream().map(Object::toString).toList());
    }

    @Test
    public void testAddTextWithItalic() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String testLine = "_You:Hello World";
        chatFrame.addText(testLine);
        StyledDocument doc = chatFrame.doc;
        Style regular = doc.getStyle("regular");
        Style name = doc.getStyle("name");
        Style italic = doc.getStyle("italic");

        assertEquals("_You:Hello World\n", chatFrame.showTxt.getText());
        String text = doc.getText(0, doc.getLength());
        String[] splittedText = text.split("\n");
        assertEquals(1, splittedText.length);
        assertEquals(testLine, splittedText[0]);
        assertEquals("name", doc.getStyleAt(0).getName());
        assertEquals(Arrays.asList("regular", "name", "italic"), doc.getCharacterIter(0).getAttributes().keySet().stream().map(Object::toString).toList());
    }

    @Test
    public void testChangeStyle() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String testUser = "TestUser";
        chatFrame.changeStyle(14, Color.RED, false, false, testUser);
        Style testStyle = chatFrame.doc.getStyle(testUser);
        StyleConstants.setForeground(testStyle, Color.RED);

        assertEquals(Color.RED, testStyle.getBackground());
        assertEquals(14, testStyle.getFontSize());
        assertFalse(testStyle.isBold());
        assertFalse(testStyle.isItalic());
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String testUser = "TestUser";
        chatFrame.addUser(testUser);
        assertEquals(Arrays.asList(testUser), Arrays.asList(chatFrame.listModel.toArray()));
    }

    @Test
    public void testGetUserlist() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String testUser1 = "TestUser1";
        String testUser2 = "TestUser2";
        chatFrame.addUser(testUser1);
        chatFrame.addUser(testUser2);

        String[] expectedUserlist = {testUser1, testUser2};
        String[] actualUserlist = chatFrame.getUserlist();

        assertArrayEquals(expectedUserlist, actualUserlist);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String testUser = "TestUser";
        chatFrame.addUser(testUser);
        chatFrame.deleteUser(testUser);
        assertEquals(0, chatFrame.listModel.getSize());
    }

    @Test
    public void testMouseEventHandling() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        MouseEvent mouseEvent = new MouseEvent(new JButton(), 1, 1, 1, 1, 1, 1, true);

        chatFrame.maybeShowPopup(mouseEvent);
    }
}