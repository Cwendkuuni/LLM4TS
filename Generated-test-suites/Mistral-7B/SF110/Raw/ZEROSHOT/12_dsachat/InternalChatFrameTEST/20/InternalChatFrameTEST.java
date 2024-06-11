package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
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
import dsachat.client.gui.InternalChatFrame.Style;

public class InternalChatFrameTest {

 private InternalChatFrame frame;
 private JList list;
 private JTextPane txtPane;
 private JFrame parentFrame;

 @Before
 public void setUp() {
 parentFrame = new JFrame();
 parentFrame.setVisible(true);
 frame = new InternalChatFrame(parentFrame);
 list = frame.getList();
 txtPane = frame.getShowTxt();
 }

 @Test
 public void testCreateGUI() {
 assertTrue(frame.isVisible());
 assertEquals(2, frame.getContentPane().getComponentCount());
 JComponent leftComponent = (JComponent) frame.getContentPane().getComponent(0);
 assertEquals(JSplitPane.HORIZONTAL_SPLIT, leftComponent.getComponentOrientation().getType());
 JComponent rightComponent = (JComponent) frame.getContentPane().getComponent(1);
 assertEquals(1, rightComponent.getComponentCount());
 JComponent txtInputComponent = (JComponent) rightComponent.getComponent(0);
 assertEquals(JTextField.class, txtInputComponent.getClass());
 }

 @Test
 public void testAddText() {
 String line = "You: Hello";
 frame.addText(line);
 List<Object> elements = Arrays.asList(txtPane.getDocument().getElements(new LogicalStructureElementFilter()));
 Style style = (Style) elements.get(elements.size() - 2);
 assertEquals("You ", style.getName());
 assertEquals(Style.StyleConstants.DEFAULT_BOLD, style.isBold());
 assertEquals(Style.StyleConstants.DEFAULT_ITALIC, style.isItalic());
 assertEquals(Color.BLUE, style.getForeground());
 String line2 = "*John:* Hello";
 frame.addText(line2);
 Style style2 = (Style) elements.get(elements.size() - 2);
 assertEquals("John ", style2.getName());
 assertEquals(Style.StyleConstants.DEFAULT_BOLD, style2.isBold());
 assertEquals(Style.StyleConstants.DEFAULT_ITALIC, style2.isItalic());
 assertEquals(Color.BLACK, style2.getForeground());
 }

 @Test
 public void testAddStylesToDocument() {
 assertEquals(22, frame.getDocument().getStyles().length);
 }

 @Test
 public void testChangeStyle() {
 frame.changeStyle(12, Color.RED, true, true, "name");
 List<Object> elements = Arrays.asList(txtPane.getDocument().getElements(new LogicalStructureElementFilter()));
 Style style = (Style) elements.get(elements.size() - 1);
 assertEquals("name", style.getName());
 assertEquals(Color.RED, style.getForeground());
 assertTrue(style.isBold());
 assertTrue(style.isItalic());
 }

 @Test
 public void testAddUser() {
 frame.addUser("John");
 assertEquals(1, list.getModel().getSize());
 assertEquals("John", list.getModel().getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 frame.addUser("John");
 List<Object> oldUserlist = frame.getUserlist();
 assertEquals(1, oldUserlist.length);
 frame.deleteUser("John");
 List<Object> newUserlist = frame.getUserlist();
 assertEquals(0, newUserlist.length);
 }

 @Test
 public void testGetUserlist() {
 frame.addUser("John");
 frame.addUser("Mike");
 List<Object> userlist = frame.getUserlist();
 assertEquals(2, userlist.length);
 assertEquals("John", userlist[0]);
 assertEquals("Mike", userlist[1]);
 }

 @Test
 public void testActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "chooseColor");
 frame.actionPerformed(actionEvent);
 JColorChooser colorChooser = JColorChooser.showDialog(frame, "Choose Color", Color.BLACK);
 assertEquals(Color.BLACK, colorChooser.getSelectedColor());
 }

 @Test
 public void testMouseClicked() {
 SwingUtilities.invokeLater(() -> {
 frame.maybeShowPopup(new MouseEvent(frame, 1, 0, 0, 0, 0, 0, false));
 JPopupMenu popupMenu = (JPopupMenu) frame.getComponent(0);
 assertTrue(popupMenu.isVisible());
 });
 }

 @Test
 public void testGetChatlog() {
 frame.addText("You: Hello");
 frame.addText("John:* Hello");
 String chatlog = frame.getChatlog();
 assertEquals("You: Hello\nJohn:* Hello\n", chatlog);
 }
}