package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.MouseEventAdapter;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JPopupMenu listPop;
 private JPopupMenu txtPop;

 @Before
 public void setUp() {
 chatFrame = new InternalChatFrame(new JFrame()) {
 @Override
 public void createGUI() {
 super.createGUI();
 listPop = new JPopupMenu();
 listPop.add(new JMenuItem("Test"));
 txtPop = new JPopupMenu();
 txtPop.add(new JMenuItem("Test"));
 }
 };
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 }

 @Test
 public void testConstructor() {
 assertEquals("Chat", chatFrame.getTitle());
 assertFalse(chatFrame.isFocusable());
 assertFalse(chatFrame.isResizable());
 assertSame(list, chatFrame.getLeftComponent());
 assertSame(showTxt, chatFrame.getRightComponent());
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello World";
 chatFrame.addText(line);

 assertEquals("User1: Hello World\n", showTxt.getText());

 Style nameStyle = chatFrame.doc.getStyle("name");
 Style regularStyle = chatFrame.doc.getStyle("regular");

 assertEquals(nameStyle, chatFrame.doc.getStyle("User1"));
 assertEquals(regularStyle, chatFrame.doc.getStyle("Hello World"));
 }

 @Test
 public void testAddStylesToDocument() {
 Style def = chatFrame.doc.getDefaultStyle();
 Style regular = chatFrame.doc.getStyle("regular");
 Style italic = chatFrame.doc.getStyle("italic");
 Style bold = chatFrame.doc.getStyle("bold");
 Style boldItalic = chatFrame.doc.getStyle("bold-italic");
 Style name = chatFrame.doc.getStyle("name");
 Style system = chatFrame.doc.getStyle("system");
 Style small = chatFrame.doc.getStyle("small");
 Style large = chatFrame.doc.getStyle("large");

 assertEquals(def, italic.getParent());
 assertEquals(def, bold.getParent());
 assertEquals(def, boldItalic.getParent());
 assertEquals(def, name.getParent());
 assertEquals(def, system.getParent());
 assertEquals(def, small.getParent());
 assertEquals(def, large.getParent());

 assertEquals(regular, italic.getParent());
 assertEquals(regular, bold.getParent());
 assertEquals(regular, boldItalic.getParent());
 assertEquals(regular, name.getParent());
 assertEquals(regular, system.getParent());
 assertEquals(regular, small.getParent());
 assertEquals(regular, large.getParent());

 assertEquals(Color.BLUE, name.getForeground());
 assertEquals(Color.BLACK, def.getForeground());

 assertEquals(16, name.getFontSize());
 assertEquals(14, system.getFontSize());
 assertEquals(10, small.getFontSize());
 assertEquals(16, large.getFontSize());

 assertEquals(ListSelectionModel.SINGLE_SELECTION, list.getSelectionModel().getSelectionMode());
 }

 @Test
 public void testChangeStyle() {
 String name = "User1";

 chatFrame.changeStyle(14, null, false, false, name);

 assertEquals(name, chatFrame.doc.getStyle(name).getName());
 assertEquals(14, chatFrame.doc.getStyle(name).getFontSize());
 assertNull(chatFrame.doc.getStyle(name).getForeground());

 chatFrame.changeStyle(14, Color.RED, false, false, name);

 assertEquals(name, chatFrame.doc.getStyle(name).getName());
 assertEquals(14, chatFrame.doc.getStyle(name).getFontSize());
 assertEquals(Color.RED, chatFrame.doc.getStyle(name).getForeground());

 chatFrame.changeStyle(14, Color.RED, true, false, name);

 assertEquals(name, chatFrame.doc.getStyle(name).getName());
 assertEquals(14, chatFrame.doc.getStyle(name).getFontSize());
 assertEquals(Color.RED, chatFrame.doc.getStyle(name).getForeground());
 assertTrue(chatFrame.doc.getStyle(name).isBold());

 chatFrame.changeStyle(14, Color.RED, true, true, name);

 assertEquals(name, chatFrame.doc.getStyle(name).getName());
 assertEquals(14, chatFrame.doc.getStyle(name).getFontSize());
 assertEquals(Color.RED, chatFrame.doc.getStyle(name).getForeground());
 assertTrue(chatFrame.doc.getStyle(name).isBold());
 assertTrue(chatFrame.doc.getStyle(name).isItalic());
 }

 @Test
 public void testAddUser() {
 String user1 = "User1";
 chatFrame.addUser(user1);

 assertEquals(user1, chatFrame.listModel.get(0));
 assertEquals(user1, chatFrame.doc.getStyle(user1).getName());

 String user2 = "User2";
 chatFrame.addUser(user2);

 assertEquals(2, chatFrame.listModel.size());
 assertEquals(user2, chatFrame.listModel.get(1));
 }

 @Test
 public void testDeleteUser() {
 String user1 = "User1";
 chatFrame.addUser(user1);

 assertEquals(1, chatFrame.list.getSelectedIndex());
 chatFrame.deleteUser(user1);

 assertEquals(0, chatFrame.listModel.size());
 assertSame(null, chatFrame.list.getSelectedValue());
 }

 @Test
 public void testGetUserlist() {
 String user1 = "User1";
 String user2 = "User2";
 chatFrame.addUser(user1);
 chatFrame.addUser(user2);

 List<String> uList = chatFrame.getUserlist();

 assertEquals(2, uList.size());
 assertEquals(user1, uList.get(0));
 assertEquals(user2, uList.get(1));
 }

 @Test
 public void testActionPerformed() {
 chatFrame.addUser("User1");
 list.setSelectedIndex(0);

 MouseEvent menuEvent = new MouseEvent(list, 1, System.currentTimeMillis(), 0, 1, 1, 1, false);
 chatFrame.listPop.show(list, 0, 0);
 listPop.setSelectedIndex(0);
 listPop.getInvoker().actionPerformed(new ActionEvent(listPop, 0, "Test"));

 assertEquals("Test", chatFrame.list.getSelectedValue());

 menuEvent = new MouseEvent(showTxt, 1, System.currentTimeMillis(), 0, 1, 1, 1, false);
 txtPop.show(showTxt, 0, 0);
 txtPop.setSelectedIndex(0);
 txtPop.getInvoker().actionPerformed(new ActionEvent(txtPop, 0, "Test"));

 assertEquals("Test", chatFrame.txtInput.getText());
 }

 @Test
 public void testMouseClicked() {
 MouseEvent event = new MouseEvent(list, 1, System.currentTimeMillis(), 0, 1, 1, 1, false);
 chatFrame.list.dispatchEvent(event);

 assertTrue(listPop.isVisible());

 event = new MouseEvent(showTxt, 1, System.currentTimeMillis(), 0, 1, 1, 1, false);
 chatFrame.showTxt.dispatchEvent(event);

 assertTrue(txtPop.isVisible());
 }

 @Test
 public void testMousePressed() {
 MouseEvent event = new MouseEvent(list, 1, System.currentTimeMillis(), 0, 1, 1, 1, false);
 chatFrame.list.dispatchEvent(event);

 assertTrue(listPop.isVisible());

 event = new MouseEvent(showTxt, 1, System.currentTimeMillis(), 0, 1, 1, 1, false);
 chatFrame.showTxt.dispatchEvent(event);

 assertTrue(txtPop.isVisible());
 }

 @Test
 public void testGetChatlog() {
 String log = "User1: Hello World\nUser2: Hello User1\n";
 chatFrame.showTxt.setText(log);

 assertEquals(log, chatFrame.getChatlog());
 }
}