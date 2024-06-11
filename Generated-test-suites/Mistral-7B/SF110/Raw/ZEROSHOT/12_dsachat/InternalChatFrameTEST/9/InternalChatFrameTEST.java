package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private MainFrame mainFrame;
 private InternalChatFrame internalChatFrame;

 @Before
 public void setup() {
 mainFrame = new MainFrame();
 internalChatFrame = new InternalChatFrame(mainFrame);
 }

 @Test
 public void testInternalChatFrameConstruction() {
 // Test if InternalChatFrame is initialized correctly
 assertNotNull(internalChatFrame);
 assertNotNull(internalChatFrame.list);
 assertNotNull(internalChatFrame.showTxt);
 assertNotNull(internalChatFrame.txtInput);
 }

 @Test
 public void testCreateGUI() {
 // Test if createGUI method is called and GUI is created correctly
 internalChatFrame.createGUI();
 // Test if JList and JTextPane are added to JSplitPane and JFrame correctly
 assertTrue(internalChatFrame.getContentPane().getComponentCount() > 0);
 // Test if JTextPane is editable is set to false
 assertFalse(internalChatFrame.showTxt.isEditable());
 }

 @Test
 public void testAddText() {
 // Test if addText method is called and text is added to JTextPane correctly
 internalChatFrame.addText("User1: Hello World!");
 String chatLog = internalChatFrame.getChatlog();
 assertEquals("User1: Hello World!\n", chatLog);
 // Test if different styles are applied to text correctly
 internalChatFrame.addText("*You *User2: This is a test!");
 internalChatFrame.addText("*User3*: This is another test!");
 internalChatFrame.addText("User4: This is a regular test!");
 chatLog = internalChatFrame.getChatlog();
 String[] lines = chatLog.split("\n");
 assertEquals(4, lines.length);
 assertEquals("User1: Hello World!\n*You *User2: This is a test!\n*User3*: This is another test!\nUser4: This is a regular test!\n", chatLog);
 }

 @Test
 public void testAddStylesToDocument() {
 // Test if addStylesToDocument method is called and different styles are added to StyledDocument correctly
 InternalChatFrame.StyledDocument doc = new InternalChatFrame.StyledDocument();
 internalChatFrame.addStylesToDocument(doc);
 assertEquals(13, doc.getStyleCount());
 }

 @Test
 public void testActionPerformed() {
 // Test if actionPerformed method is called and correct action is performed based on ActionEvent command
 internalChatFrame.list.setSelectedIndex(0);
 internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "chooseColor"));
 SwingUtilities.invokeLater(() -> {
 JColorChooser colorChooser = new JColorChooser();
 Color newColor = colorChooser.showDialog(internalChatFrame, "Choose Color", Color.BLACK);
 assertEquals(Color.BLACK, newColor);
 });
 internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "bgColor"));
 SwingUtilities.invokeLater(() -> {
 JColorChooser colorChooser = new JColorChooser();
 Color newColor = colorChooser.showDialog(internalChatFrame, "Choose Color", Color.BLACK);
 assertEquals(Color.BLACK, newColor);
 internalChatFrame.showTxt.setBackground(Color.CYAN);
 assertEquals(Color.CYAN, internalChatFrame.showTxt.getBackground());
 });
 internalChatFrame.actionPerformed(new ActionEvent(internalChatFrame, 0, "whisper"));
 assertEquals("/w ", internalChatFrame.txtInput.getText());
 }

 @Test
 public void testMouseClicked() {
 // Test if mouseClicked method is called and correct JPopupMenu is shown based on source component
 internalChatFrame.setLocation(50, 50);
 JFrame frame = new JFrame();
 frame.add(internalChatFrame);
 frame.pack();
 frame.setVisible(true);
 MouseEvent mouseEvent = new MouseEvent(internalChatFrame, 1, 10, 1, 10, 10, 0, false);
 internalChatFrame.list.dispatchEvent(mouseEvent);
 assertTrue(internalChatFrame.listPop.isVisible());
 MouseEvent mouseEvent2 = new MouseEvent(internalChatFrame, 1, 10, 1, 10, 10, 0, false);
 internalChatFrame.showTxt.dispatchEvent(mouseEvent2);
 assertTrue(internalChatFrame.txtPop.isVisible());
 }

 @Test
 public void testAddUserAndDeleteUser() {
 // Test if addUser and deleteUser methods are called and user list is updated correctly
 internalChatFrame.addText("User1: Hello World!");
 internalChatFrame.addUser("User2");
 internalChatFrame.addText("User2: Hi User1!");
 internalChatFrame.deleteUser("User2");
 List<String> userList = Arrays.asList(internalChatFrame.getUserlist());
 assertEquals(1, userList.size());
 assertEquals("User1", userList.get(0));
 }
}