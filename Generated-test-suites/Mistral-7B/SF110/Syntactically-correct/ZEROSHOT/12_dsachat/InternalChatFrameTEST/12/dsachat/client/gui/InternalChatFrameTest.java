package dsachat.client.gui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JFrame parentFrame;

 @Before
 public void setUp() {
 ParentFrame parent = new ParentFrame();
 this.parentFrame = new JFrame();
 this.parentFrame.add(parent);
 this.parentFrame.setVisible(true);
 this.parentFrame.pack();
 this.chatFrame = new InternalChatFrame(parentFrame);
 this.list = chatFrame.getList();
 this.showTxt = chatFrame.getShowTxt();
 }

 @Test
 public void testCreateGUI() {
 assertEquals("Chat", chatFrame.getTitle());
 assertNotNull(list);
 assertNotNull(showTxt);
 }

 @Test
 public void testAddText() {
 String line = "You: Hello World";
 chatFrame.addText(line);
 String expected = "You: " + line.substring(5) + "\n";
 String actual = showTxt.getText(0, showTxt.getCaretPosition());
 assertEquals(expected, actual);
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = chatFrame.getShowTxt().getStyledDocument();
 Style regular = doc.getStyle("regular");
 assertNotNull(regular);
 Style italic = doc.getStyle("italic");
 assertNotNull(italic);
 Style bold = doc.getStyle("bold");
 assertNotNull(bold);
 Style boldItalic = doc.getStyle("bold-italic");
 assertNotNull(boldItalic);
 Style name = doc.getStyle("name");
 assertNotNull(name);
 Style system = doc.getStyle("system");
 assertNotNull(system);
 Style small = doc.getStyle("small");
 assertNotNull(small);
 Style large = doc.getStyle("large");
 assertNotNull(large);
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 assertEquals(1, chatFrame.getUserlist().length);
 assertEquals("User1", chatFrame.getUserlist()[0]);
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 String[] userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 chatFrame.deleteUser("User1");
 assertEquals(1, chatFrame.getUserlist().length);
 assertNotEquals("User1", chatFrame.getUserlist()[0]);
 }

 @Test
 public void testGetChatlog() {
 String log = "User1: Hello\nYou: World\nUser1: Goodbye";
 chatFrame.addText("User1: Hello");
 chatFrame.addText("You: World");
 chatFrame.addText("User1: Goodbye");
 String expected = log;
 String actual = chatFrame.getChatlog();
 assertEquals(expected, actual);
 }

 @Test
 public void testActionPerformed_chooseColor() {
 Color newColor = Color.RED;
 chatFrame.listPop.getComponent(0).actionPerformed(new ActionEvent("test", 0, "chooseColor"));
 chatFrame.changeStyle(12, newColor, false, false, "User1");
 Color color = chatFrame.getList().getSelectedValue(0);
 assertEquals(newColor, color);
 }

 @Test
 public void testActionPerformed_bgColor() {
 Color newColor = Color.GREEN;
 chatFrame.txtPop.getComponent(0).actionPerformed(new ActionEvent("test", 0, "bgColor"));
 Color background = chatFrame.getShowTxt().getBackground();
 assertEquals(newColor, background);
 }

 @Test
 public void testActionPerformed_whisper() {
 String whisper = "/w User1 ";
 chatFrame.list.setSelectedValue("User1", false);
 chatFrame.list.addListSelectionListener(e -> {
 if (e.getValueIsAdjusting()) {
 return;
 }
 chatFrame.txtInput.setText(whisper);
 });
 chatFrame.txtInput.addActionListener(e -> {
 if (e.getActionCommand().equals("sendInput")) {
 String whisperText = chatFrame.txtInput.getText();
 assertEquals("/w User1 ", whisperText);
 chatFrame.addText("You: " + whisperText.substring(5));
 }
 });
 }

 @Test
 public void testMouseClicked() {
 MouseEvent event = new MouseEvent(list, 1, System.currentTimeMillis(), 1, 1, 1, 1, false);
 chatFrame.list.dispatchEvent(event);
 JPopupMenu popup = chatFrame.listPop;
 assertEquals(1, popup.getComponentCount());
 assertTrue(popup.isVisible());
 }

 @Test
 public void testMousePressed() {
 MouseEvent event = new MouseEvent(showTxt, 1, System.currentTimeMillis(), 1, 1, 1, 1, false);
 chatFrame.showTxt.dispatchEvent(event);
 JPopupMenu popup = chatFrame.txtPop;
 assertEquals(1, popup.getComponentCount());
 assertTrue(popup.isVisible());
 }
}

 class ParentFrame extends JFrame {
 // Empty class
 }