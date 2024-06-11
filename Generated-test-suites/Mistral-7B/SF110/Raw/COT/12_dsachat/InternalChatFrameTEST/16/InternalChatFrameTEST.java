package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.*;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = Mockito.spy(new MainFrame());
 chatFrame = new InternalChatFrame(parent);
 }

 @Test
 public void testInternalChatFrameConstructor() {
 assertNotNull(chatFrame);
 assertEquals("Chat", chatFrame.getTitle());
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 assertNull(chatFrame.doc);
 assertNull(chatFrame.listPop);
 assertNull(chatFrame.txtPop);
 }

 @Test
 public void testCreateGUI() {
 // Check if the components are added to the frame
 assertTrue(chatFrame.isDisplayable());
 assertTrue(chatFrame.list.isShowing());
 assertTrue(chatFrame.showTxt.isShowing());
 assertTrue(chatFrame.txtInput.isShowing());
 // Check if the layout manager is set correctly
 GridBagLayout gridbagLayout = (GridBagLayout) chatFrame.getLayout();
 JSplitPane split = (JSplitPane) chatFrame.getContentPane();
 assertTrue(gridbagLayout instanceof GridBagLayout);
 assertTrue(split instanceof JSplitPane);
 // Check if the components are added to the correct grid cell
 JComponent[] components = { chatFrame.list, chatFrame.showTxt, chatFrame.txtInput };
 for (int i = 0; i < components.length; i++) {
 GridBagConstraints constraints = gridbagLayout.getConstraints(components[i]);
 int expectedGridY = i * 2;
 int expectedWeightY = (i == components.length - 1) ? 1 : 0;
 assertEquals("Unexpected grid Y for component: " + components[i], expectedGridY, constraints.gridy);
 assertEquals("Unexpected weight Y for component: " + components[i], expectedWeightY, constraints.weighty);
 }
}

 @Test
 public void testAddTextMethod() {
 // Test adding a simple message
 chatFrame.addText("John: Hello");
 assertEquals(1, chatFrame.listModel.getSize());
 assertEquals("John\n", chatFrame.list.getSelectedValue());
 assertEquals("Hello\n", chatFrame.showTxt.getText());
 // Test adding a message with formatting
 chatFrame.addText("*John*: *Hi* *there*");
 assertEquals(2, chatFrame.listModel.getSize());
 assertEquals("John\n", chatFrame.list.getSelectedValue());
 assertEquals("*Hi* there\n", chatFrame.showTxt.getText());
 // Test adding a message with multiple parts
 chatFrame.addText("*John*: *Hi* there *Mary*");
 assertEquals(3, chatFrame.listModel.getSize());
 assertEquals("John\n", chatFrame.list.getSelectedValue());
 assertEquals("*Hi* there *Mary*\n", chatFrame.showTxt.getText());
 // Test adding a message with an empty line at the end
 chatFrame.addText("John: ");
 assertEquals(4, chatFrame.listModel.getSize());
 assertEquals("John\n", chatFrame.list.getSelectedValue());
 assertEquals("*Hi* there *Mary*\nJohn\n", chatFrame.showTxt.getText());
}

 @Test
 public void testAddStylesToDocumentMethod() {
 // Test adding styles with default settings
 StyledDocument doc = chatFrame.doc;
 assertNotNull(doc);
 assertEquals(17, doc.getStyleCount());
 Style regular = doc.getStyle("regular");
 Style italic = doc.getStyle("italic");
 Style bold = doc.getStyle("bold");
 Style boldItalic = doc.getStyle("bold-italic");
 Style name = doc.getStyle("name");
 Style system = doc.getStyle("system");
 Style small = doc.getStyle("small");
 Style large = doc.getStyle("large");
 // Test getting styles with custom settings
 Style italicBold = doc.addStyle("italicBold", italic);
 StyleConstants.setBold(italicBold, true);
 StyleConstants.setItalic(italicBold, true);
 Style nameBlue16 = doc.addStyle("nameBlue16", name);
 StyleConstants.setForeground(nameBlue16, Color.BLUE);
 StyleConstants.setFontSize(nameBlue16, 16);
 // Test setting styles with custom settings
 chatFrame.changeStyle(12, Color.RED, false, false, "nameBlue16");
 assertEquals(Color.RED, StyleManager.getStyle(nameBlue16).getForeground());
}

 @Test
 public void testAddUserMethod() {
 // Test adding a new user
 chatFrame.addUser("Alice");
 assertEquals(1, chatFrame.listModel.getSize());
 assertEquals("Alice", chatFrame.list.getSelectedValue());
 // Test adding an existing user
 chatFrame.addUser("Alice");
 assertEquals(1, chatFrame.listModel.getSize());
 // Test adding multiple users
 chatFrame.addUser("Bob");
 chatFrame.addUser("Charlie");
 assertEquals(3, chatFrame.listModel.getSize());
 assertEquals("Alice", chatFrame.list.getSelectedValue());
}

 @Test
 public void testDeleteUserMethod() {
 // Test deleting a non-existent user
 chatFrame.deleteUser("Alice");
 assertEquals(3, chatFrame.listModel.getSize());
 // Test deleting an existing user
 chatFrame.addUser("Alice");
 chatFrame.deleteUser("Alice");
 assertEquals(2, chatFrame.listModel.getSize());
 // Test deleting multiple users
 chatFrame.addUser("Bob");
 chatFrame.addUser("Charlie");
 chatFrame.addUser("Alice");
 chatFrame.deleteUser("Alice");
 assertEquals(2, chatFrame.listModel.getSize());
}

 @Test
 public void testGetUserlistMethod() {
 String[] userlist = chatFrame.getUserlist();
 assertEquals(0, userlist.length);
 chatFrame.addUser("Alice");
 userlist = chatFrame.getUserlist();
 assertEquals(1, userlist.length);
 assertEquals("Alice", userlist[0]);
 chatFrame.addUser("Bob");
 userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 assertEquals("Alice", userlist[0]);
 assertEquals("Bob", userlist[1]);
}

 @Test
 public void testActionPerformedMethod() {
 // Test choosing a color for a user
 chatFrame.addUser("Alice");
 chatFrame.list.setSelectedValue("Alice");
 chatFrame.actionPerformed(Mockito.mock(ActionEvent.class));
 chatFrame.changeStyle(12, Color.GREEN, false, false, "Alice");
 assertEquals(Color.GREEN, StyleManager.getStyle("Alice").getForeground());
 // Test changing the background color for a user
 chatFrame.addUser("Alice");
 chatFrame.list.setSelectedValue("Alice");
 chatFrame.actionPerformed(Mockito.mock(ActionEvent.class, "bgColor"));
 Color newColor = Color.YELLOW;
 assertEquals(newColor, chatFrame.showTxt.getBackground());
 // Test whispering to a user
 chatFrame.addUser("Bob");
 chatFrame.addUser("Alice");
 chatFrame.list.setSelectedValue("Alice");
 chatFrame.txtInput.setText("/w Bob Hello");
 chatFrame.actionPerformed(Mockito.mock(ActionEvent.class, "sendInput"));
 assertEquals("*John*: /w Bob Hello\n", chatFrame.getChatlog());
}

 @Test
 public void testMouseListenerMethod() {
 // Test showing the list popup menu
 chatFrame.addUser("Alice");
 chatFrame.list.setSelectedValue("Alice");
 MouseEvent mouseEvent = Mockito.mock(MouseEvent.class);
 chatFrame.maybeShowPopup(mouseEvent);
 assertTrue(chatFrame.listPop.isVisible());
 // Test showing the text popup menu
 chatFrame.addUser("Alice");
 chatFrame.showTxt.setBackground(Color.BLACK);
 mouseEvent = Mockito.mock(MouseEvent.class);
 chatFrame.maybeShowPopup(mouseEvent);
 assertTrue(chatFrame.txtPop.isVisible());
}

 @Test(expected = BadLocationException.class)
 public void testAddTextMethodWithBadLocationException() {
 chatFrame.addText("John: Hello\nWorld");
}
}