package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;
 private JList list;
 private DefaultListModel listModel;
 private JTextPane showTxt;
 private JTextField txtInput;
 private StyledDocument doc;
 private JPopupMenu listPop;
 private JPopupMenu txtPop;

 @Before
 public void setUp() {
 parent = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parent);
 list = chatFrame.list;
 listModel = chatFrame.listModel;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 doc = showTxt.getStyledDocument();
 listPop = chatFrame.listPop;
 txtPop = chatFrame.txtPop;
 }

 @Test
 public void testInternalChatFrameConstruction() {
 assertEquals("Chat", chatFrame.getTitle());
 assertFalse(chatFrame.isResizable());
 assertTrue(chatFrame.isFocusTraversable());
 assertNotNull(list);
 assertNotNull(showTxt);
 assertNotNull(txtInput);
 }

 @Test
 public void testCreateGUI() {
 GridBagConstraints c = new GridBagConstraints();
 JSplitPane split = chatFrame.split;
 assertEquals(JSplitPane.HORIZONTAL_SPLIT, split.getOrientation());
 assertTrue(split.isVisible());
 JScrollPane scrList = (JScrollPane) split.getLeftComponent();
 assertEquals(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, scrList.getVerticalScrollBarPolicy());
 JScrollPane scrTxt = (JScrollPane) split.getRightComponent();
 assertEquals(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS, scrTxt.getHorizontalScrollBarPolicy());
 assertTrue(txtInput.isFocusable());
 assertTrue(txtInput.isEditable());
 assertTrue(listPop.isLightWeightPopup());
 assertTrue(txtPop.isLightWeightPopup());
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello";
 chatFrame.addText(line);
 String expectedText = "User1: Hello\n";
 assertEquals(expectedText, showTxt.getText());
 }

 @Test
 public void testAddTextWithFormatting() {
 String line = "*User1*: *Hello*";
 chatFrame.addText(line);
 String expectedText = "*User1*: Hello\n";
 assertEquals(expectedText, showTxt.getText());
 }

 @Test
 public void testAddTextWithBoldAndItalic() {
 String line = "*User1*: **Hello* **world**";
 chatFrame.addText(line);
 String expectedText = "*User1*: **Hello** world\n";
 assertEquals(expectedText, showTxt.getText());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 assertEquals(1, listModel.size());
 assertEquals("User1", listModel.get(0));
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.deleteUser("User1");
 assertEquals(1, listModel.size());
 assertFalse(listModel.contains("User1"));
 }

 @Test
 public void testGetUserlist() {
 String[] userlist = { "User1", "User2" };
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 assertArrayEquals(userlist, chatFrame.getUserlist());
 }

 @Test
 public void testActionPerformedWhenChooseColorForUser() {
 String name = "User1";
 Color newColor = Color.RED;
 doReturn(name).when(list).getSelectedValue();
 JColorChooser chooser = mock(JColorChooser.class);
 doReturn(newColor).when(chooser).showDialog(any(), anyString(), any());
 doReturn(chooser).when(parent).showComponentDialog(any(), any());
 listPop.add(mock(JMenuItem.class));
 chatFrame.actionPerformed(mock(ActionEvent.class));
 verify(listPop).show(any(), anyInt(), anyInt());
 verify(listPop).setVisible(true);
 verify(parent).showComponentDialog(any(), any());
 changeStyle(12, newColor, false, false, name);
 }

 @Test
 public void testActionPerformedWhenWhisper() {
 String name = "/w User1 ";
 doReturn(name).when(list).getSelectedValue();
 txtInput.setText(name);
 txtInput.doActionPerform();
 verify(parent).sendMessage(eq("/w User1 "), anyString());
 }
}