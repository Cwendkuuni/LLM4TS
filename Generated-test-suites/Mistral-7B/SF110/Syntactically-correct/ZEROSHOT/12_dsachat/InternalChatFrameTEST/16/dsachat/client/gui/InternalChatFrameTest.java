package dsachat.client.gui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = new MainFrame();
 chatFrame = new InternalChatFrame(parent);
 SwingUtilities.invokeLater(() -> chatFrame.setVisible(true));
 }

 @After
 public void tearDown() {
 chatFrame.dispose();
 parent.dispose();
 }

 @Test
 public void testAddText() {
 chatFrame.addText("User1: Hello World");
 ListModel<String> listModel = chatFrame.listModel;
 assertEquals("Hello World", listModel.getElementAt(0));
 JTextPane showTxt = chatFrame.showTxt;
 assertEquals("User1: Hello World\n", showTxt.getText());
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = chatFrame.doc;
 Style regular = doc.getStyle("regular");
 Style name = doc.getStyle("name");
 Style italic = doc.getStyle("italic");
 Style bold = doc.getStyle("bold");
 Style boldItalic = doc.getStyle("bold-italic");

 assertEquals(regular, doc.getDefaultStyle());
 assertNotNull(name);
 assertNotNull(italic);
 assertNotNull(bold);
 assertNotNull(boldItalic);

 assertEquals("SansSerif", StyleContext.getDefaultStyleContext().getStyle("default").getName());
 assertEquals(14.0, StyleConstants.getFontSize(regular), 0.0);
 assertEquals("regular", name.getName());
 assertEquals(false, StyleConstants.isBold(name));
 assertEquals(false, StyleConstants.isItalic(name));
 assertEquals(Color.BLUE, StyleConstants.getForeground(name));
 assertEquals(false, StyleConstants.isBold(italic));
 assertEquals(true, StyleConstants.isItalic(italic));
 assertEquals(Color.BLACK, StyleConstants.getForeground(italic));
 assertEquals(16.0, StyleConstants.getFontSize(bold), 0.0);
 assertEquals(true, StyleConstants.isBold(bold));
 assertEquals(false, StyleConstants.isItalic(bold));
 assertEquals(Color.BLACK, StyleConstants.getForeground(bold));
 assertEquals(16.0, StyleConstants.getFontSize(boldItalic), 0.0);
 assertEquals(true, StyleConstants.isBold(boldItalic));
 assertEquals(true, StyleConstants.isItalic(boldItalic));
 assertEquals(Color.BLACK, StyleConstants.getForeground(boldItalic));
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 JList list = chatFrame.list;
 assertEquals(1, list.getModel().getSize());
 assertEquals("User1", list.getModel().getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.deleteUser("User1");
 JList list = chatFrame.list;
 assertEquals(1, list.getModel().getSize());
 assertFalse(list.isSelectionEmpty());
 assertEquals("User2", list.getSelectedValue());
 }

 @Test
 public void testGetUserlist() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 String[] uList = chatFrame.getUserlist();
 assertEquals(2, uList.length);
 assertTrue(Arrays.asList(uList).contains("User1"));
 assertTrue(Arrays.asList(uList).contains("User2"));
 }

 @Test
 public void testActionPerformed() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 chatFrame.addActionListener(e -> {});
 chatFrame.list.setSelectedValue("User1");
 chatFrame.txtInput.setText("Hello User1");
 chatFrame.txtInput.doClick();
 chatFrame.actionPerformed(new ActionEvent(chatFrame.txtInput, 0, "sendInput"));
 String chatlog = chatFrame.getChatlog();
 assertEquals("User2: Hello User1\n", chatlog);
 }

 @Test
 public void testChangeStyle() {
 chatFrame.addText("User1: Hello World");
 chatFrame.changeStyle(14, Color.RED, false, false, "name");
 JTextPane showTxt = chatFrame.showTxt;
 assertEquals("User1: Hello World\n", showTxt.getText());
 assertEquals(14.0, StyleConstants.getFontSize(chatFrame.doc.getStyle("name")), 0.0);
 assertEquals(Color.RED, StyleConstants.getForeground(chatFrame.doc.getStyle("name")));
 }
}