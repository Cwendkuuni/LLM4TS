package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;
 private MainFrame parentMock;

 @Before
 public void setUp() {
 parentMock = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parentMock);
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @Test
 public void testCreateGUI() {
 // Check if components are created
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 // Check if listeners are added
 assertTrue(chatFrame.list.hasFocusTraversalPolicyComponent(0));
 assertTrue(chatFrame.list.getModel() instanceof DefaultListModel);
 assertTrue(list instanceof JList);
 assertFalse(chatFrame.isVisible());
 // Check if split pane is created
 JSplitPane splitPane = (JSplitPane) chatFrame.getContentPane();
 assertNotNull(splitPane);
 assertSame(splitPane.getLeftComponent(), list);
 assertSame(splitPane.getRightComponent(), chatFrame.getRightComponent());
 }

 @Test
 public void testAddText() {
 // Create styled document and add styles
 StyledDocument doc = chatFrame.doc;
 Style def = StyleContext.getDefaultStyleContext().getStyle("default");
 Style regular = doc.addStyle("regular", def);
 Style italic = doc.addStyle("italic", regular);
 Style bold = doc.addStyle("bold", regular);
 Style boldItalic = doc.addStyle("bold-italic", regular);
 Style name = doc.addStyle("name", regular);
 Style system = doc.addStyle("system", regular);
 StyleConstants.setFontFamily(def, "SansSerif");
 // Test adding text with no format
 String text1 = "Test message 1";
 chatFrame.addText(text1);
 assertEquals(text1, showTxt.getText());
 // Test adding text with format "name: message"
 String text2 = "You: Test message 2";
 chatFrame.addText(text2);
 assertEquals("You: " + text1 + "\n" + text2, showTxt.getText());
 // Test adding text with format "name: message with * and +"
 String text3 = "*You*: Test message 3 + Test message 4";
 chatFrame.addText(text3);
 assertEquals("You: " + text1 + "\n" + text2 + "\n" + text3, showTxt.getText());
 // Test adding text with format "message with * and +"
 String text4 = "*Test message 5*: Test message 6 + Test message 7";
 chatFrame.addText(text4);
 assertEquals("You: " + text1 + "\n" + text2 + "\n" + text3 + "\n" + text4, showTxt.getText());
 // Test adding text with different styles
 String text5 = "*You*: Test message 5 * Test message 6";
 String text6 = "*Test message 7*: Test message 8";
 chatFrame.addText(text5);
 chatFrame.addText(text6);
 Style style5 = doc.getStyle("You");
 Style style6 = doc.getStyle("Test message 7");
 assertEquals(StyleConstants.BOLD, style5.getAttributes().getBold());
 assertEquals(StyleConstants.ITALIC, style5.getAttributes().isItalic());
 assertEquals(StyleConstants.BOLD, style6.getAttributes().isBold());
 assertEquals(StyleConstants.ITALIC, style6.getAttributes().isItalic());
}

 @Test
 public void testAddStylesToDocument() {
 // Test if styles are added to document
 StyledDocument doc = chatFrame.doc;
 Style def = StyleContext.getDefaultStyleContext().getStyle("default");
 Style regular = doc.addStyle("regular", def);
 Style italic = doc.addStyle("italic", regular);
 Style bold = doc.addStyle("bold", regular);
 Style boldItalic = doc.addStyle("bold-italic", regular);
 Style name = doc.addStyle("name", regular);
 Style system = doc.addStyle("system", regular);
 StyleConstants.setFontFamily(def, "SansSerif");
 // Test styles are added with correct names
 assertEquals("italic", italic.getName());
 assertEquals("bold", bold.getName());
 assertEquals("bold-italic", boldItalic.getName());
 assertEquals("name", name.getName());
 assertEquals("system", system.getName());
 // Test styles have correct attributes
 assertEquals(StyleConstants.ITALIC, italic.getAttributes().isItalic());
 assertEquals(StyleConstants.BOLD, bold.getAttributes().isBold());
 assertEquals(StyleConstants.BOLD, boldItalic.getAttributes().isBold());
 assertEquals(StyleConstants.ITALIC, boldItalic.getAttributes().isItalic());
 assertEquals(StyleConstants.FONT_SIZE, italic.getAttributes().getProperty("font-size"));
 assertEquals(StyleConstants.ITALIC, system.getAttributes().isItalic());
 assertEquals(StyleConstants.DEFAULT_FONT, system.getAttributes().getFontFamily());
}

 @Test
 public void testChangeStyle() {
 // Test changing style of text
 chatFrame.changeStyle(12, Color.RED, true, true, "name");
 Style style = doc.getStyle("name");
 assertEquals(Color.RED, style.getAttributes().getForeground());
 assertEquals(StyleConstants.BOLD, style.getAttributes().getBold());
 assertEquals(StyleConstants.ITALIC, style.getAttributes().isItalic());
}

 @Test
 public void testAddUser() {
 // Test adding user to list and chat frame
 String user = "Test User";
 chatFrame.addUser(user);
 // Check if user is added to list model
 assertEquals(1, chatFrame.listModel.size());
 assertEquals(user, chatFrame.listModel.get(0));
 // Check if user is added to chat frame
 assertEquals(user, list.getModel().getElementAt(0));
 // Test adding user with same name
 chatFrame.addUser(user);
 assertEquals(1, chatFrame.listModel.size());
}

 @Test
 public void testDeleteUser() {
 // Test deleting user from list and chat frame
 String user = "Test User";
 chatFrame.addUser(user);
 chatFrame.deleteUser(user);
 // Check if user is deleted from list model
 assertEquals(0, chatFrame.listModel.size());
 // Check if user is deleted from chat frame
 assertEquals(0, list.getModel().size());
}

 @Test
 public void testGetUserlist() {
 // Test getting user list from chat frame
 String[] userList = chatFrame.getUserlist();
 // Check if user list is empty
 assertEquals(0, userList.length);
 // Test adding user to list and getting list
 chatFrame.addUser("User 1");
 chatFrame.addUser("User 2");
 userList = chatFrame.getUserlist();
 // Check if user list contains users
 assertEquals(2, userList.length);
 assertEquals("User 1", userList[0]);
 assertEquals("User 2", userList[1]);
}

 @Test
 public void testActionPerformed() {
 // Test action performed event for chooseColor, bgColor and whisper commands
 String name = "Test User";
 chatFrame.addUser(name);
 chatFrame.listModel.setSize(0);
 chatFrame.list.setSelectedValue(name, true);
 txtInput.setText("Test message");
 chatFrame.txtInput.doClick();
 chatFrame.actionPerformed(new ActionEvent(txtInput, 0, "sendInput"));
 chatFrame.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
 Color newColor = Color.RED;
 assertTrue(JColorChooser.showDialog(chatFrame, "Choose Color", Color.BLACK) == newColor);
 chatFrame.changeStyle(12, newColor, false, false, name);
 assertEquals(newColor, doc.getStyle("Test User").getAttributes().getForeground());
 chatFrame.actionPerformed(new ActionEvent(list, 0, "bgColor"));
 Color bgColor = Color.GREEN;
 assertTrue(JColorChooser.showDialog(chatFrame, "Choose Background Color", Color.BLACK) == bgColor);
 assertEquals(bgColor, showTxt.getBackground());
}

 @Test
 public void testMouseClicked() {
 // Test mouse clicked event for showing popup menu
 MouseEvent mouseEvent = mock(MouseEvent.class);
 chatFrame.maybeShowPopup(mouseEvent);
 assertFalse(chatFrame.listPop.isVisible());
 assertFalse(chatFrame.txtPop.isVisible());
}

 @Test
 public void testGetChatlog() {
 // Test getting chat log from chat frame
 String chatLog = chatFrame.getChatlog();
 // Check if chat log is empty
 assertEquals("", chatLog);
 // Test adding text to chat log
 chatFrame.addText("Test message 1");
 chatLog = chatFrame.getChatlog();
 assertEquals("Test message 1", chatLog);
 chatFrame.addText("Test message 2");
 chatLog = chatFrame.getChatlog();
 assertEquals("Test message 1\nTest message 2", chatLog);
}
}