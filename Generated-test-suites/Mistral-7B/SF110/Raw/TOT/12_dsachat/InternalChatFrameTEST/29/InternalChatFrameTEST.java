package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import org.junit.*;
import org.mockito.*;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parentMock;
 private JList listMock;
 private JTextPane showTxtMock;
 private JTextField txtInputMock;
 private DefaultListModel listModelMock;
 private JPopupMenu listPopMock;
 private JPopupMenu txtPopMock;

 @Before
 public void setUp() {
 parentMock = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parentMock);
 listMock = mock(JList.class);
 showTxtMock = mock(JTextPane.class);
 txtInputMock = mock(JTextField.class);
 listModelMock = new DefaultListModel();
 listFrame = spy(new InternalChatFrame(parentMock) {
 {
 setList(listMock);
 setShowTxt(showTxtMock);
 setTxtInput(txtInputMock);
 setListModel(listModelMock);
 }
 });
 listPopMock = mock(JPopupMenu.class);
 txtPopMock = mock(JPopupMenu.class);
 doNothing().when(listPopMock).show(any(Component.class), anyInt(), anyInt());
 doNothing().when(txtPopMock).show(any(Component.class), anyInt(), anyInt());
 }

 @Test
 public void testInternalChatFrameConstruction() {
 assertEquals("Chat", chatFrame.getTitle());
 assertTrue(chatFrame.isResizable());
 assertTrue(chatFrame.isFocusable());
 assertFalse(chatFrame.isFocusTraversable());
 assertFalse(chatFrame.isVisible());
 }

 @Test
 public void testCreateGUI() {
 // Check that components are added to the split pane correctly
 verify(splitPane, times(2)).add(anyComponent());
 // Check that listeners are added to the list and text pane
 verify(listMock, times(1)).addMouseListener(chatFrame);
 verify(listMock, times(1)).addActionListener(parentMock);
 verify(showTxtMock, times(1)).addMouseListener(chatFrame);
 }

 @Test
 public void testAddText() {
 final String line = "User: Hello World";
 String[] expectedStyles = {"user", "regular"};
 try {
 chatFrame.addText(line);
 }
 catch (BadLocationException e) {
 fail("Unexpected BadLocationException");
 }
 // Check that the text was inserted into the text pane with the correct styles
 StyleContext context = showTxtMock.getDocument().getStyleContext();
 for (int i = 0; i < line.length(); ++i) {
 String styleName = context.getName(context.getCharacterElement(context.getLogicalPosition(showTxtMock.getDocument(), i)).getName());
 assertEquals(expectedStyles[i % 2], styleName);
 }
 }

 @Test
 public void testChangeStyle() {
 final int fontSize = 12;
 final Color color = Color.RED;
 final boolean isBold = true;
 final boolean isItalic = false;
 final String name = "User";
 chatFrame.changeStyle(fontSize, color, isBold, isItalic, name);
 // Check that the style for the given name was updated
 Style style = showTxtMock.getDocument().getStyle(name);
 assertEquals(fontSize, StyleConstants.getFontSize(style));
 assertEquals(color, StyleConstants.getForeground(style));
 assertEquals(isBold, StyleConstants.isBold(style));
 assertEquals(isItalic, StyleConstants.isItalic(style));
 }

 @Test
 public void testAddUser() {
 final String user = "New User";
 chatFrame.addUser(user);
 // Check that the user was added to the list model
 assertEquals(1, listModelMock.size());
 assertEquals(user, listModelMock.getElementAt(0));
 // Check that a style was added for the user
 Style style = showTxtMock.getDocument().getStyle(user);
 assertNotNull(style);
 }

 @Test
 public void testDeleteUser() {
 final String user = "New User";
 chatFrame.addUser(user);
 chatFrame.deleteUser(user);
 // Check that the user was removed from the list model
 assertEquals(0, listModelMock.size());
 }

 @Test
 public void testGetUserlist() {
 final String[] expectedUserlist = {"User1", "User2", "User3"};
 when(listModelMock.toArray(any(String[].class)))
 .thenReturn(expectedUserlist);
 String[] actualUserlist = chatFrame.getUserlist();
 // Check that the userlist was returned correctly
 assertArrayEquals(expectedUserlist, actualUserlist);
 }

 @Test
 public void testActionPerformedWithChooseColorEvent() {
 final Color newColor = Color.RED;
 doNothing().when(listPopMock).show(any(Component.class), anyInt(), anyInt());
 when(listMock.getSelectedValue()).thenReturn("User");
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(listPopMock).show(eq(chatFrame), anyInt(), anyInt());
 verify(listFrame).changeStyle(anyInt(), eq(newColor), anyBoolean(), anyBoolean(), eq("User"));
 }

 @Test
 public void testActionPerformedWithWhisperEvent() {
 final String whisperTarget = "/w User";
 doNothing().when(txtPopMock).show(any(Component.class), anyInt(), anyInt());
 when(txtInputMock.getText()).thenReturn(whisperTarget);
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(txtPopMock).show(eq(chatFrame), anyInt(), anyInt());
 verify(parentMock).sendMessage(eq(whisperTarget));
 }
}