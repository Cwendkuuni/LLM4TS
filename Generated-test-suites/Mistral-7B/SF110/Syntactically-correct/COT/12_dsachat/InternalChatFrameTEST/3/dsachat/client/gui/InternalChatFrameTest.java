package dsachat.client.gui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;
import dsachat.client.gui.util.GuiHelper;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parentFrame;
 private JList list;
 private JTextPane showTxt;
 private JFrame testFrame;

 @Before
 public void setUp() {
 parentFrame = new MainFrame();
 chatFrame = new InternalChatFrame(parentFrame);
 testFrame = new JFrame();
 testFrame.getContentPane().add(chatFrame);
 testFrame.pack();
 testFrame.setVisible(true);
 SwingUtilities.invokeLater(() -> chatFrame.add(new JLabel("Test User")));
 }

 @After
 public void tearDown() {
 testFrame.dispose();
 parentFrame.dispose();
 }

 @Test
 public void testInternalChatFrameConstructor() {
 assertNotNull(chatFrame);
 assertEquals("Chat", chatFrame.getTitle());
 }

 @Test
 public void testAddText() {
 final String line = "User1: Test";
 chatFrame.addText(line);
 assertNotNull(chatFrame.showTxt.getDocument());
 final int length = chatFrame.showTxt.getDocument().getLength();
 assertEquals(line, chatFrame.showTxt.getDocument().getText(0, length));
 }

 @Test
 public void testAddStylesToDocument() {
 final StyledDocument doc = chatFrame.doc;
 final Style def = StyleContext.getDefaultStyleContext().getStyle("default");
 final Style regular = doc.getStyle("regular");

 assertEquals(def, doc.getStyle("regular").getParent());
 assertEquals(def, doc.getStyle("name").getParent());
 assertEquals(regular, doc.getStyle("italic").getParent());
 assertEquals(regular, doc.getStyle("bold").getParent());
 assertEquals(regular, doc.getStyle("bold-italic").getParent());

 final String styleName = "name";
 final Style style = doc.getStyle(styleName);
 assertNotNull(style);
 assertEquals(Color.BLUE, style.getForeground());
 assertEquals(16, (int) style.getFontSize());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 final JList list = chatFrame.list;
 assertNotNull(list);
 assertEquals("User1", list.getModel().getElementAt(0));
 assertEquals("User1", chatFrame.doc.getStyle(styleName).getName());
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 final String[] userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 chatFrame.deleteUser("User1");
 final String[] newUserlist = chatFrame.getUserlist();
 assertEquals(1, newUserlist.length);
 assertEquals("User2", newUserlist[0]);
 }

 @Test
 public void testGetUserlist() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 final String[] userlist = chatFrame.getUserlist();
 assertEquals(2, userlist.length);
 }

 @Test
 public void testChangeStyle() {
 final int fontSize = 14;
 final Color color = new Color(1, 2, 3);
 final boolean isBold = true;
 final boolean isItalic = false;
 final String name = "styleName";
 chatFrame.changeStyle(fontSize, color, isBold, isItalic, name);
 final Style style = chatFrame.doc.getStyle(name);
 assertNotNull(style);
 if (color != null) {
 assertEquals(color, style.getForeground());
 }
 assertEquals(isBold, style.isBold());
 assertEquals(isItalic, style.isItalic());
 }

 @Test
 public void testAddStylesToDocumentWithUserSelected() {
 final String user = "User1";
 chatFrame.addUser(user);
 SwingUtilities.invokeLater(() -> list.setSelectedValue(user, true));
 final ListSelectionEvent event = new ListSelectionEvent(list, 0, user, 1, false);
 SwingUtilities.invokeLater(() -> list.fireSelectionEvent(event));
 chatFrame.changeStyle(16, null, false, false, user);
 final Style style = chatFrame.doc.getStyle(user);
 assertNotNull(style);
 assertEquals(16, (int) style.getFontSize());
 }

 @Test
 public void testMouseClickedOnList() {
 final String user = "User1";
 chatFrame.addUser(user);
 final MouseEvent mouseEvent = new MouseEvent(list, 1, 0, 0, 0, 0, 0, false);
 SwingUtilities.invokeLater(() -> list.dispatchEvent(mouseEvent));
 chatFrame.maybeShowPopup(mouseEvent);
 assertTrue(listPop.isShowing());
 }

 @Test
 public void testChangeColorForUser() {
 final String user = "User1";
 chatFrame.addUser(user);
 final Color newColor = new Color(4, 5, 6);
 SwingUtilities.invokeLater(() -> list.setSelectedValue(user, true));
 final ListSelectionEvent event = new ListSelectionEvent(list, 0, user, 1, false);
 SwingUtilities.invokeLater(() -> list.fireSelectionEvent(event));
 chatFrame.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
 JColorChooser chooser = new JColorChooser();
 final Color newColorChosen = chooser.showDialog(null, "Choose a color", newColor);
 SwingUtilities.invokeLater(() -> chatFrame.actionPerformed(new ActionEvent(list, 0, "chooseColor")));
 final Style style = chatFrame.doc.getStyle(user);
 assertNotNull(style);
 assertEquals(newColorChosen, style.getForeground());
 }

 @Test
 public void testChangeBackgroundColorForUser() {
 final String user = "User1";
 chatFrame.addUser(user);
 final Color newColor = new Color(4, 5, 6);
 SwingUtilities.invokeLater(() -> list.setSelectedValue(user, true));
 final ListSelectionEvent event = new ListSelectionEvent(list, 0, user, 1, false);
 SwingUtilities.invokeLater(() -> list.fireSelectionEvent(event));
 chatFrame.actionPerformed(new ActionEvent(list, 0, "bgColor"));
 JColorChooser chooser = new JColorChooser();
 final Color newColorChosen = chooser.showDialog(null, "Choose a color", newColor);
 SwingUtilities.invokeLater(() -> chatFrame.actionPerformed(new ActionEvent(list, 0, "bgColor")));
 assertEquals(newColorChosen, chatFrame.showTxt.getBackground());
 }

 @Test
 public void testWhisperToUser() {
 final String user = "User1";
 chatFrame.addUser(user);
 SwingUtilities.invokeLater(() -> list.setSelectedValue(user, true));
 final ListSelectionEvent event = new ListSelectionEvent(list, 0, user, 1, false);
 SwingUtilities.invokeLater(() -> list.fireSelectionEvent(event));
 chatFrame.actionPerformed(new ActionEvent(list, 0, "whisper"));
 assertEquals("/w User1 ", chatFrame.txtInput.getText());
 }

 @Test
 public void testGetChatlog() {
 final String line1 = "User1: Test1";
 final String line2 = "User2: Test2";
 chatFrame.addText(line1);
 chatFrame.addText(line2);
 assertEquals(line1 + line2, chatFrame.getChatlog());
 }
}