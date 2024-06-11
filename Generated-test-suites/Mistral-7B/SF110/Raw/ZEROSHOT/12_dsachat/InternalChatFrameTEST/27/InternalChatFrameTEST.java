package dsachat.client.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame mainFrame;

 @Before
 public void setUp() {
 mainFrame = new MainFrame(); // Assuming MainFrame is instantiated without issues
 chatFrame = new InternalChatFrame(mainFrame);
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(() -> {
 chatFrame.dispose();
 mainFrame.dispose();
 });
 }

 @Test
 public void testInternalChatFrameConstruction() {
 assertTrue(chatFrame instanceof JInternalFrame);
 assertNotNull(chatFrame.getList());
 assertNotNull(chatFrame.getTextPane());
 assertNotNull(chatFrame.getTextField());
 assertNotNull(chatFrame.getParentFrame());
 }

 @Test
 public void testCreateGUI() {
 assertTrue(chatFrame.getList() instanceof JList);
 assertTrue(chatFrame.getTextPane() instanceof JTextPane);
 assertTrue(chatFrame.getTextField() instanceof JTextField);
 assertEquals(2, chatFrame.getNumberOfDividers());
 }

 @Test
 public void testAddText() {
 String line = "TestLine: TestStyle";
 String styleName = "TestStyle";
 chatFrame.addText(line);
 Style style = chatFrame.getDocument().getStyle(styleName);
 assertEquals(styleName, style.getName());
 StyleConstants.setBold(style, true);
 assertEquals(true, style.isBold());
}

 @Test
 public void testAddStylesToDocument() {
 int numStyles = chatFrame.getDocument().getStyleCount();
 assertEquals(13, numStyles);
}

 @Test
 public void testActionPerformed_chooseColor() {
 Color newColor = Color.RED;
 chatFrame.actionPerformed(new ActionEvent(chatFrame.getList(), 0, "chooseColor"));
 Color oldColor = chatFrame.getList().getBackground();
 SwingUtilities.invokeLater(() -> {
 chatFrame.changeStyle(12, newColor, false, false, "TestUser");
 });
 assertEquals(newColor, chatFrame.getList().getBackground());
 assertEquals(oldColor, chatFrame.getDocument().getStyle("TestUser").getBackground());
}

 @Test
 public void testActionPerformed_bgColor() {
 Color newColor = Color.GREEN;
 chatFrame.actionPerformed(new ActionEvent(chatFrame.getTextPane(), 0, "bgColor"));
 Color oldColor = chatFrame.getTextPane().getBackground();
 SwingUtilities.invokeLater(() -> {
 chatFrame.changeStyle(12, null, false, false, "TestUser");
 chatFrame.changeStyle(12, newColor, false, false, "TestUser");
 });
 assertEquals(newColor, chatFrame.getTextPane().getBackground());
 assertEquals(oldColor, chatFrame.getDocument().getStyle("TestUser").getBackground());
}

 @Test
 public void testActionPerformed_whisper() {
 String recipient = "/w TestUser ";
 chatFrame.actionPerformed(new ActionEvent(chatFrame.getList(), 0, "whisper"));
 assertEquals(recipient, chatFrame.getTextField().getText());
}

 @Test
 public void testGetChatlog() {
 String line1 = "User1: TestLine1";
 String line2 = "User2: TestLine2";
 chatFrame.addText(line1);
 chatFrame.addText(line2);
 String chatlog = chatFrame.getChatlog();
 String expectedChatlog = line1 + "\n" + line2 + "\n";
 assertEquals(expectedChatlog, chatlog);
}

 @Test
 public void testAddUser() {
 chatFrame.addUser("TestUser");
 List<String> userlist = (List<String>) chatFrame.getUserlist();
 assertEquals(1, userlist.size());
 assertEquals("TestUser", userlist.get(0));
}

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("TestUser1");
 chatFrame.addUser("TestUser2");
 chatFrame.deleteUser("TestUser1");
 List<String> userlist = (List<String>) chatFrame.getUserlist();
 assertEquals(1, userlist.size());
 assertEquals("TestUser2", userlist.get(0));
}
}