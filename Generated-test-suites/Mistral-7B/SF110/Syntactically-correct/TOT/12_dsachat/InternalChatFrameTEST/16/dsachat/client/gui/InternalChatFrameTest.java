package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 parent = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parent);
 }

 @Test
 public void testCreateGUI() {
 // Testing if the JList and JTextPane are added to the JInternalFrame
 assertEquals("Chat", chatFrame.getTitle());
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 // Testing if the components are added to the JSplitPane
 JSplitPane splitPane = chatFrame.getContentPane();
 assertEquals(JSplitPane.HORIZONTAL_SPLIT, splitPane.getOrientation());
 assertEquals(ComponentOrientation.LEFT_TO_RIGHT, splitPane.getComponentOrientation());
 assertTrue(splitPane.getLeftComponent() instanceof JScrollPane);
 assertTrue(splitPane.getRightComponent() instanceof JPanel);
 // Testing if the JTextField is added to the JPanel
 JPanel rightPanel = (JPanel) splitPane.getRightComponent();
 assertTrue(rightPanel.getComponentCount() == 2);
 assertTrue(rightPanel.getComponentAt(0) instanceof JScrollPane);
 assertTrue(rightPanel.getComponentAt(1) instanceof JTextField);
 }

 @Test
 public void testAddText() {
 // Creating a mock StyledDocument
 StyledDocument doc = mock(StyledDocument.class);
 doReturn(doc).when(chatFrame).doc;
 // Testing adding text with no formatting
 chatFrame.addText("Test User: Hello World");
 String expectedText = "Test User: Hello World\n";
 assertEquals(expectedText, doc.getText(0, doc.getLength()));
 // Testing adding text with formatting
 chatFrame.addText("**You:** *Hi there*");
 String formattedText = "**You:** *Hi there*\n";
 assertEquals(formattedText, doc.getText(0, doc.getLength()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddTextWithNullString() {
 chatFrame.addText(null);
 }

 @Test
 public void testAddUser() {
 // Testing adding a new user to the list model
 chatFrame.addUser("Test User");
 assertEquals(1, chatFrame.listModel.size());
 // Testing if the user is added to the list
 assertEquals("Test User", chatFrame.list.getValueIsAdjusting() ? chatFrame.list.getSelectedValue() : chatFrame.list.getValueAt(0));
 // Testing if the user is added to the document
 assertEquals("Test User", chatFrame.doc.getStyle("name").getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUserWithNullString() {
 chatFrame.addUser(null);
 }

 @Test
 public void testDeleteUser() {
 // Testing deleting a user from the list model
 chatFrame.addUser("Test User 1");
 chatFrame.addUser("Test User 2");
 chatFrame.deleteUser("Test User 1");
 assertEquals(1, chatFrame.listModel.size());
 // Testing if the user is removed from the list
 assertFalse(chatFrame.list.isSelectedIndex(0));
 assertFalse(chatFrame.list.isSelectedIndex(1));
 assertEquals("Test User 2", chatFrame.list.getValueAt(0).toString());
 // Testing if the user is removed from the document
 assertFalse(chatFrame.doc.getStyle("name").getName().contains("Test User 1"));
 }

 @Test
 public void testGetUserlist() {
 // Testing getting the user list
 String[] userList = chatFrame.getUserlist();
 assertEquals(0, userList.length);
 chatFrame.addUser("Test User 1");
 chatFrame.addUser("Test User 2");
 userList = chatFrame.getUserlist();
 assertArrayEquals(new String[] {"Test User 1", "Test User 2"}, userList);
 }

 @Test
 public void testActionPerformed() {
 // Testing if the JPopupMenu is shown when right clicking on the JTextPane
 ActionEvent event = mock(ActionEvent.class);
 chatFrame.maybeShowPopup(new MouseEvent(chatFrame, 0, 0, 0, 0, 0, 0, false));
 verify(chatFrame.txtPop, times(1)).show(eq(chatFrame.showTxt), anyInt(), anyInt());
 // Testing if the correct action is performed when clicking on the "chooseColor" option
 doReturn(Color.RED).when(JColorChooser.showDialog(eq(chatFrame), "Choose Color for user Test User", any(Color.class)));
 chatFrame.actionPerformed(event);
 verify(parent).setBackgroundColor(eq(Color.RED));
 }
}