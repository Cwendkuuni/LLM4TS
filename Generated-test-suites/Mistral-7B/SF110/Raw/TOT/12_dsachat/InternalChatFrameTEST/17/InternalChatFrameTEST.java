package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.*;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private MainFrame parentMock;
 private InternalChatFrame chatFrame;

 @Before
 public void setup() {
 parentMock = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parentMock);
 }

 @Test
 public void testCreateGUI() {
 // Check if components are added correctly
 assertEquals(2, chatFrame.getComponentCount());
 JComponent[] components = chatFrame.getComponents();
 assertTrue(components[0] instanceof JSplitPane);
 assertTrue(components[1] instanceof JInternalFrame);
 // Check if JSplitPane split correctly
 JSplitPane splitPane = (JSplitPane) components[0];
 assertEquals(1, splitPane.getComponents().length);
 JComponent leftComponent = splitPane.getLeftComponent();
 assertTrue(leftComponent instanceof JScrollPane);
 JComponent rightComponent = splitPane.getRightComponent();
 assertTrue(rightComponent instanceof JPanel);
 // Check if JTextPane and JTextField are added to the right component
 JPanel rightPanel = (JPanel) rightComponent;
 assertTrue(rightPanel.getComponentCount() == 2);
 JComponent txtInput = rightPanel.getComponent(0);
 assertTrue(txtInput instanceof JTextField);
 JComponent showTxt = rightPanel.getComponent(1);
 assertTrue(showTxt instanceof JTextPane);
 }

 @Test
 public void testAddText() {
 // Create mock StyledDocument for JTextPane
 StyledDocument mockDoc = mock(StyledDocument.class);
 when(chatFrame.doc).thenReturn(mockDoc);
 // Test adding normal text
 chatFrame.addText("User1: Hello");
 verify(mockDoc).insertString(anyInt(), "User1: Hello\n", any(Style.class));
 // Test adding text with formatting
 chatFrame.addText("*User1*: Hi *User2*");
 String expectedText = "*User1*: Hi *User2*\n";
 verify(mockDoc).insertString(anyInt(), expectedText, any(Style.class));
 }

 @Test
 public void testChangeStyle() {
 // Test changing style for a user
 chatFrame.changeStyle(12, Color.RED, true, false, "User1");
 Style userStyle = chatFrame.doc.getStyle("User1");
 StyleConstants.verifyStyleProperties(userStyle, 12, Color.RED, false, false);
 }

 @Test
 public void testActionPerformedWhenChooseColor() {
 // Test choosing a color for a user
 Color newColor = Color.RED;
 doAnswer(invocation -> {
 JColorChooser.showDialog(chatFrame, "Choose Color for user", Color.BLACK);
 return newColor;
 }).when(parentMock).showDialog(any(Component.class), anyString(), any(Color.class));
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "chooseColor"));
 verify(parentMock).showDialog(eq(chatFrame), equals("Choose Color for user"), eq(Color.BLACK));
 Style userStyle = chatFrame.doc.getStyle("User1");
 StyleConstants.verifyStyleProperties(userStyle, 12, Color.RED, false, false);
 }

 @Test
 public void testActionPerformedWhenBgColor() {
 // Test choosing a background color for the chat frame
 Color newColor = Color.GREEN;
 doAnswer(invocation -> JColorChooser.showDialog(chatFrame, "Choose Background Color", Color.BLACK)).when(parentMock).showDialog(any(Component.class), anyString(), any(Color.class));
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "bgColor"));
 assertEquals(newColor, chatFrame.showTxt.getBackground());
 }

 @Test
 public void testActionPerformedWhenWhisper() {
 // Test whispering to a user
 String expectedText = "/w User1 ";
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "whisper"));
 assertEquals(expectedText, chatFrame.txtInput.getText());
 }
}