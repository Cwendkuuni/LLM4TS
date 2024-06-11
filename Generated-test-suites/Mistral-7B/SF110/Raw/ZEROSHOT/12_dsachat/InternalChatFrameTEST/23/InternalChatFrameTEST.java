package dsachat.client.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import javax.swing.text.*;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parentMock;

 @Before
 public void setUp() {
 parentMock = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parentMock);
 }

 @Test
 public void testCreateGUI() {
 // Check if components are created
 assertNotNull(chatFrame.getList());
 assertNotNull(chatFrame.getShowTxt());
 assertNotNull(chatFrame.getTxtInput());
 }

 @Test
 public void testAddText() {
 // Create test data
 String line = "User1: Hello";
 String styleName = "User1";
 // Set up document
 StyledDocument doc = mock(StyledDocument.class);
 chatFrame.addStylesToDocument(doc);
 // Test method
 chatFrame.addText(line);
 // Check if text was added to document
 try {
 String actual = doc.getText(0, doc.getLength()).toString();
 String expected = line + "\n";
 assertEquals(expected, actual);
 } catch (BadLocationException e) {
 e.printStackTrace();
 fail("Failed to get text from document");
 }
 // Check if style was applied
 Style style = doc.getStyle(styleName);
 assertNotNull(style);
 }

 @Test
 public void testAddStylesToDocument() {
 // Test that default styles are added
 StyledDocument doc = chatFrame.getShowTxt().getDocument();
 Style defaultStyle = doc.getStyle("default");
 Style regularStyle = doc.getStyle("regular");
 assertEquals(defaultStyle, regularStyle.getParent());
 // Test that custom styles are added
 Style nameStyle = doc.getStyle("name");
 Style systemStyle = doc.getStyle("system");
 Style italicStyle = doc.getStyle("italic");
 Style boldStyle = doc.getStyle("bold");
 Style boldItalicStyle = doc.getStyle("bold-italic");
 assertEquals(regularStyle, nameStyle.getParent());
 assertEquals(systemStyle, italicStyle.getParent());
 assertEquals(regularStyle, boldStyle.getParent());
 assertEquals(regularStyle, boldItalicStyle.getParent());
 }

 @Test
 public void testChangeStyle() {
 // Test that style is changed when passed valid parameters
 chatFrame.changeStyle(12, Color.GREEN, true, false, "User1");
 Style userStyle = chatFrame.getDoc().getStyle("User1");
 assertTrue(StyleConstants.isBold(userStyle));
 assertFalse(StyleConstants.isItalic(userStyle));
 assertNotNull(userStyle.getFont());
 assertEquals(12, userStyle.getFont().getSize());
 // Test that style is not changed when passed invalid parameters
 chatFrame.changeStyle(12, null, true, false, "User1");
 Style userStyleNull = chatFrame.getDoc().getStyle("User1");
 assertFalse(StyleConstants.isBold(userStyleNull));
 assertFalse(StyleConstants.isItalic(userStyleNull));
 assertNotNull(userStyleNull.getFont());
 }

 @Test
 public void testAddUser() {
 // Test that user is added to list model
 chatFrame.addUser("User1");
 assertEquals("User1", chatFrame.getList().getModel().getElementAt(0));
 // Test that user is added to document
 Style userStyle = chatFrame.getDoc().getStyle("User1");
 assertEquals(StyleConstants.DEFAULT_FONT, userStyle.getFont());
 }

 @Test
 public void testDeleteUser() {
 // Test that user is removed from list model
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.deleteUser("User1");
 assertEquals(1, chatFrame.getList().getModel().getSize());
 assertEquals("User2", chatFrame.getList().getModel().getElementAt(0));
 }

 @Test
 public void testGetUserlist() {
 // Test that user list is returned correctly
 String[] userList = chatFrame.getUserlist();
 assertEquals(0, userList.length);
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 String[] expectedUserList = {"User1", "User2"};
 assertArrayEquals(expectedUserList, userList);
 }

 @Test
 public void testActionPerformed() {
 // Test that color is changed when "chooseColor" action is performed
 chatFrame.getList().setSelectedValue("User1");
 chatFrame.actionPerformed(mock(ActionEvent.class));
 Style userStyle = chatFrame.getDoc().getStyle("User1");
 Color newColor = new Color(1, 0, 0);
 chatFrame.changeStyle(12, newColor, false, false, "User1");
 assertEquals(newColor, userStyle.getForeground());
 // Test that background color is changed when "bgColor" action is performed
 chatFrame.actionPerformed(mock(ActionEvent.class));
 chatFrame.getShowTxt().setBackground(Color.WHITE);
 assertEquals(Color.WHITE, chatFrame.getShowTxt().getBackground());
 // Test that whisper command is added to input field when "whisper" action is performed
 chatFrame.getList().setSelectedValue("User1");
 chatFrame.actionPerformed(mock(ActionEvent.class));
 assertEquals("/w User1 ", chatFrame.getTxtInput().getText());
 }

 @Test
 public void testMouseClicked() {
 // Test that popup menu is not shown when mouse is clicked outside of list or text pane
 MouseEvent mouseEventList = new MouseEvent(new JComponent() {}, 0, 10, 1, 10, 10, 0, false);
 MouseEvent mouseEventTxt = new MouseEvent(new JComponent() {}, 0, 20, 1, 20, 20, 0, false);
 chatFrame.maybeShowPopup(mouseEventList);
 chatFrame.maybeShowPopup(mouseEventTxt);
 assertFalse(chatFrame.listPop.isShowing());
 assertFalse(chatFrame.txtPop.isShowing());
 }

 @Test
 public void testGetChatlog() {
 // Test that chat log is returned correctly
 String expectedChatlog = "User1: Hello\nYou: world\nUser2: test\n";
 chatFrame.addText("User1: Hello");
 chatFrame.addText("You: world");
 chatFrame.addText("User2: test");
 String actualChatlog = chatFrame.getChatlog();
 assertEquals(expectedChatlog, actualChatlog);
 }
}