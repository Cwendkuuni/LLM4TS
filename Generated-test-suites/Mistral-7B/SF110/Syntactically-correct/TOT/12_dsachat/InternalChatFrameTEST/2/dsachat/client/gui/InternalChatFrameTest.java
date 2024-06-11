package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parentMock;
 private JList listMock;
 private DefaultListModel listModelMock;
 private JTextPane showTxtMock;
 private JTextField txtInputMock;
 private StyledDocument docMock;
 private JPopupMenu listPopMock;
 private JPopupMenu txtPopMock;

 @Before
 public void setUp() {
 parentMock = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parentMock);
 listModelMock = new DefaultListModel();
 listMock = mock(JList.class);
 when(listMock.getSelectedValue()).thenReturn("User1");
 showTxtMock = mock(JTextPane.class);
 docMock = mock(StyledDocument.class);
 txtInputMock = mock(JTextField.class);

 chatFrame.list = listMock;
 chatFrame.listModel = listModelMock;
 chatFrame.showTxt = showTxtMock;
 chatFrame.parent = parentMock;
 chatFrame.doc = docMock;
 }

 @Test
 public void testCreateGUI() {
 assertTrue(chatFrame.isVisible());
 assertTrue(chatFrame.getTitle().equals("Chat"));
 assertFalse(chatFrame.isResizable());
 assertFalse(chatFrame.isFocusTraversable());

 JSplitPane split = chatFrame.getContentPane().getComponent(0);
 assertEquals(JSplitPane.HORIZONTAL_SPLIT, split.getOrientation());

 JScrollPane scrList = (JScrollPane) split.getLeftComponent();
 assertEquals(listMock, scrList.getViewport().getComponent(0));

 JPanel right = (JPanel) split.getRightComponent();
 JTextPane scrTxt = (JTextPane) right.getComponent(0);
 assertEquals(showTxtMock, scrTxt);

 JTextField txt = (JTextField) right.getComponent(1);
 assertEquals(txtInputMock, txt);
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello!";
 chatFrame.addText(line);

 String chatlog = chatFrame.getChatlog();
 assertEquals(line, chatlog.substring(0, chatlog.indexOf("\n") + line.length()));
 }

 @Test
 public void testAddStylesToDocument() {
 Style regular = mock(Style.class);
 Style italic = mock(Style.class);
 Style bold = mock(Style.class);
 Style boldItalic = mock(Style.class);
 Style name = mock(Style.class);
 Style system = mock(Style.class);

 StyleContext sc = mock(StyleContext.class);
 when(docMock.getStyleContext()).thenReturn(sc);

 when(sc.getStyle("default")).thenReturn(regular);
 when(sc.addStyle("regular", regular)).thenReturn(regular);
 when(sc.addStyle("italic", regular)).thenReturn(italic);
 when(sc.addStyle("bold", regular)).thenReturn(bold);
 when(sc.addStyle("bold-italic", regular)).thenReturn(boldItalic);
 when(sc.addStyle("name", regular)).thenReturn(name);
 when(sc.addStyle("system", regular)).thenReturn(system);

 StyleConstants cc = mock(StyleConstants.class);
 when(regular.getComponents()).thenReturn(cc);
 when(cc.setFontFamily(cc, "SansSerif")).thenReturn(cc);

 Style s = chatFrame.doc.addStyle("italic", regular);
 StyleConstants is = mock(StyleConstants.class);
 when(is.setItalic(s, true)).thenReturn(s);
 s = chatFrame.doc.addStyle("bold", regular);
 s = chatFrame.doc.addStyle("bold-italic", regular);
 s = chatFrame.doc.addStyle("name", regular);
 s = chatFrame.doc.addStyle("system", regular);

 verify(cc, times(3)).setFontFamily(cc, "SansSerif");
 }

 @Test
 public void testChangeStyle() {
 Style s = chatFrame.doc.getStyle("name");
 StyleConstants c = mock(StyleConstants.class);

 chatFrame.changeStyle(12, Color.BLUE, false, false, "name");
 verify(c, times(1)).setForeground(s, eq(Color.BLUE));

 chatFrame.changeStyle(12, null, false, false, "name");
 verifyNoMoreInteractions(c);

 chatFrame.changeStyle(12, Color.BLUE, true, false, "name");
 verify(c, times(1)).setForeground(s, eq(Color.BLUE));

 chatFrame.changeStyle(12, Color.BLUE, false, true, "name");
 verify(c, times(1)).setItalic(s, true);

 chatFrame.changeStyle(12, Color.BLUE, true, true, "name");
 verify(c, times(1)).setForeground(s, eq(Color.BLUE));
 verify(c, times(1)).setItalic(s, true);
 }

 @Test
 public void testAddUser() {
 String user = "User2";
 chatFrame.addUser(user);

 assertEquals(2, chatFrame.listModel.size());
 assertEquals(user, chatFrame.listModel.get(1));
 }

 @Test
 public void testDeleteUser() {
 String user = "User1";
 chatFrame.addUser(user);
 chatFrame.deleteUser(user);

 assertEquals(0, chatFrame.listModel.size());
 }

 @Test
 public void testGetUserlist() {
 String[] uList = { "User1" };
 chatFrame.addUser("User1");

 assertArrayEquals(uList, chatFrame.getUserlist());
 }

 @Test
 public void testActionPerformedWhenChooseColor() {
 Color newColor = Color.YELLOW;
 doAnswer(invocation -> {
 JColorChooser.showDialog(chatFrame, "Choose Color for user User1", Color.black);
 return newColor;
 }).when(parentMock).showDialog(eq(chatFrame), anyString(), any(Color.class));

 chatFrame.actionPerformed(new ActionEvent(listMock, 0, "chooseColor"));
 verify(parentMock, times(1)).showDialog(eq(chatFrame), eq("Choose Color for user User1"), any(Color.class));
 verify(docMock, times(1)).getStyle("name");
 verify(docMock, times(1)).getStyle("name").setForeground(eq(newColor));
 }

 @Test
 public void testActionPerformedWhenBgColor() {
 Color newColor = Color.PINK;
 doAnswer(invocation -> {
 JColorChooser.showDialog(chatFrame, "Choose Color for user User1", Color.black);
 return newColor;
 }).when(parentMock).showDialog(eq(chatFrame), anyString(), any(Color.class));

 chatFrame.actionPerformed(new ActionEvent(listMock, 0, "bgColor"));
 verify(parentMock, times(1)).showDialog(eq(chatFrame), eq("Choose Color for user User1"), any(Color.class));
 verify(showTxtMock, times(1)).setBackground(eq(newColor));
 }

 @Test
 public void testActionPerformedWhenWhisper() {
 String wUser = "/w User1 ";
 chatFrame.actionPerformed(new ActionEvent(listMock, 0, "whisper"));
 assertEquals(wUser, chatFrame.txtInput.getText());
 }

 @Test
 public void testMousePressed() {
 chatFrame.maybeShowPopup(mock(MouseEvent.class));
 verify(listPopMock, times(1)).show(eq(listMock), anyInt(), anyInt());
 }
}