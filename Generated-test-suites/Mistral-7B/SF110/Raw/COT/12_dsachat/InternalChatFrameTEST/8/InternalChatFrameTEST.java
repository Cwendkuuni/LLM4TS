package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame frame;
 private JList list;
 private JTextPane showTxt;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = new MainFrame();
 frame = new InternalChatFrame(parent);
 list = frame.list;
 showTxt = frame.showTxt;
 }

 @Test
 public void testInternalChatFrameConstructor() {
 assertNotNull(frame);
 assertEquals("Chat", frame.getTitle());
 assertTrue(frame.isFocusable());
 }

 @Test
 public void testCreateGUI() {
 GridBagLayout gridBagLayout = new GridBagLayout();
 JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, true);
 JList list = new JList();
 JScrollPane listScrollPane = new JScrollPane(list);
 JTextPane showTxt = new JTextPane();
 JScrollPane showTxtScrollPane = new JScrollPane(showTxt);
 JTextField txtInput = new JTextField();
 JPanel rightPanel = new JPanel(gridBagLayout);
 assertSame(list, frame.list);
 assertSame(listScrollPane, splitPane.getLeftComponent());
 assertSame(rightPanel, splitPane.getRightComponent());
 assertSame(showTxt, frame.showTxt);
 assertSame(showTxtScrollPane, frame.getContentPane().getComponent(1));
 assertSame(txtInput, frame.txtInput);
 }

 @Test
 public void testAddText() {
 String line = "User: Hello";
 frame.addText(line);
 assertEquals(line, showTxt.getText().substring(showTxt.getText().lastIndexOf("\n") + 1));
 }

 @Test
 public void testAddTextWithStyle() {
 String line = "*User*: *Hello*";
 frame.addText(line);
 assertTrue(showTxt.getStyle(StyleContext.START_ElementName("i").getName()) != null);
 assertTrue(showTxt.getStyle(StyleContext.START_ElementName("b").getName()) != null);
 }

 @Test
 public void testChangeStyle() {
 Color color = Color.RED;
 frame.changeStyle(12, color, true, true, "User");
 assertEquals(color, frame.doc.getStyle("User").getBackground());
 }

 @Test
 public void testAddUser() {
 frame.addUser("User1");
 assertEquals(1, frame.listModel.getSize());
 assertEquals("User1", frame.listModel.getElementAt(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUserWithNull() {
 frame.addUser(null);
 }

 @Test
 public void testDeleteUser() {
 frame.addUser("User1");
 frame.addUser("User2");
 assertEquals(2, frame.listModel.getSize());
 frame.deleteUser("User1");
 assertEquals(1, frame.listModel.getSize());
 assertFalse(frame.listModel.contains("User1"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testDeleteUserWithInvalidIndex() {
 frame.addUser("User1");
 frame.deleteUser(0);
 }

 @Test
 public void testGetUserlist() {
 String[] userlist = frame.getUserlist();
 assertEquals(0, userlist.length);
 frame.addUser("User1");
 frame.addUser("User2");
 String[] expected = {"User1", "User2"};
 assertArrayEquals(expected, userlist);
 }

 @Test
 public void testActionPerformed() {
 String name = "User";
 frame.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
 assertNotNull(frame.doc.getStyle("User"));
 frame.actionPerformed(new ActionEvent(list, 0, "bgColor"));
 assertSame(Color.BLACK, showTxt.getBackground());
 frame.actionPerformed(new ActionEvent(list, 0, "whisper"));
 assertEquals("/w " + name + " ", txtInput.getText());
 }

 @Test
 public void testMouseClicked() {
 MouseEvent event = new MouseEvent(list, 1, 0, 0, 0, 0, 0, false);
 frame.maybeShowPopup(event);
 assertFalse(listPop.isVisible());
 }
}