package dsachat.client.gui.test;

import static org.junit.Assert.*;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;
import javax.swing.*;
import javax.swing.event.MouseEvent;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = new MainFrame();
 chatFrame = new InternalChatFrame(parent);
 }

 @Test
 public void testInternalChatFrameConstructor() {
 assertNotNull(chatFrame);
 assertEquals("Chat", chatFrame.getTitle());
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 }

 @Test
 public void testCreateGUI() {
 assertEquals(2, chatFrame.getComponentCount());
 JSplitPane splitPane = (JSplitPane) chatFrame.getComponent(0);
 assertEquals(1, splitPane.getComponentCount());
 JScrollPane leftComponent = (JScrollPane) splitPane.getLeftComponent();
 assertEquals(1, leftComponent.getComponentCount());
 JList list = (JList) leftComponent.getViewport().getView();
 assertEquals(0, list.getModel().getSize());
 JPanel rightComponent = (JPanel) splitPane.getRightComponent();
 assertEquals(2, rightComponent.getComponentCount());
 JTextPane showTxt = (JTextPane) rightComponent.getComponent(0);
 assertEquals(false, showTxt.isEditable());
 JTextField txtInput = (JTextField) rightComponent.getComponent(1);
 assertNull(txtInput.getText());
 }

 @Test
 public void testAddText() {
 String line = "Test: Test";
 chatFrame.addText(line);
 assertEquals(1, chatFrame.list.getModel().getSize());
 assertEquals(line, chatFrame.list.getModel().getElementAt(0).toString());
 String[] styles = {"regular", "system"};
 Style[] stylesArray = new Style[styles.length];
 for (int i = 0; i < styles.length; i++) {
 stylesArray[i] = chatFrame.doc.getStyle(styles[i]);
 }
 assertEquals(stylesArray[1].getFontFamily(), "SansSerif");
 assertEquals(stylesArray[1].getFontSize(), 14);
 }

 @Test
 public void testAddStylesToDocument() {
 Style regular = chatFrame.doc.getStyle("regular");
 Style italic = chatFrame.doc.getStyle("italic");
 Style bold = chatFrame.doc.getStyle("bold");
 Style boldItalic = chatFrame.doc.getStyle("bold-italic");
 Style name = chatFrame.doc.getStyle("name");
 Style system = chatFrame.doc.getStyle("system");
 Style small = chatFrame.doc.getStyle("small");
 Style large = chatFrame.doc.getStyle("large");
 assertEquals(Styles.DEFAULT_FONT, regular.getFont());
 assertEquals(false, italic.isItalic());
 assertEquals(false, bold.isBold());
 assertEquals(false, boldItalic.isItalic());
 assertEquals(false, boldItalic.isBold());
 assertEquals(Color.BLUE, name.getForeground());
 assertEquals(16, name.getFontSize());
 assertEquals(Color.decode("#006000"), system.getForeground());
 assertEquals(14, system.getFontSize());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("Test User");
 assertEquals(1, chatFrame.list.getModel().getSize());
 assertEquals("Test User", chatFrame.list.getModel().getElementAt(0).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUserNullUser() {
 chatFrame.addUser(null);
 }

 @Test
 public void testGetUserlist() {
 String[] userList = chatFrame.getUserlist();
 assertEquals(0, userList.length);
 chatFrame.addUser("Test User 1");
 chatFrame.addUser("Test User 2");
 userList = chatFrame.getUserlist();
 assertEquals(2, userList.length);
 assertEquals("Test User 1", userList[0]);
 assertEquals("Test User 2", userList[1]);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetUserlistEmptyList() {
 chatFrame.getUserlist();
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("Test User 1");
 chatFrame.addUser("Test User 2");
 String[] userList = chatFrame.getUserlist();
 assertEquals(2, userList.length);
 chatFrame.deleteUser("Test User 1");
 userList = chatFrame.getUserlist();
 assertEquals(1, userList.length);
 assertNotEquals("Test User 1", userList[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteUserNullUser() {
 chatFrame.deleteUser(null);
 }

 @Test
 public void testActionPerformedChooseColor() {
 JColorChooser chooser = mock(JColorChooser.class);
 when(chooser.showDialog(eq(chatFrame), anyString(), any(Color.class))).thenReturn(Color.RED);
 chatFrame.listPop = new JPopupMenu();
 JMenuItem menuItem = new JMenuItem("Change Color");
 menuItem.setActionCommand("chooseColor");
 menuItem.addActionListener(chatFrame);
 chatFrame.listPop.add(menuItem);
 MouseEvent mouseEvent = mock(MouseEvent.class);
 when(mouseEvent.getSource()).thenReturn(chatFrame.list);
 when(mouseEvent.getX()).thenReturn(0);
 when(mouseEvent.getY()).thenReturn(0);
 chatFrame.maybeShowPopup(mouseEvent);
 assertEquals(Color.RED, chatFrame.getStyle("name").getForeground());
 }

 @Test
 public void testActionPerformedBgColor() {
 JColorChooser chooser = mock(JColorChooser.class);
 when(chooser.showDialog(eq(chatFrame), anyString(), any(Color.class))).thenReturn(Color.GREEN);
 chatFrame.txtPop = new JPopupMenu();
 JMenuItem menuItem = new JMenuItem("Change Color");
 menuItem.setActionCommand("bgColor");
 menuItem.addActionListener(chatFrame);
 chatFrame.showTxt.setBackground(Color.BLACK);
 MouseEvent mouseEvent = mock(MouseEvent.class);
 when(mouseEvent.getSource()).thenReturn(chatFrame.showTxt);
 when(mouseEvent.getX()).thenReturn(0);
 when(mouseEvent.getY()).thenReturn(0);
 chatFrame.maybeShowPopup(mouseEvent);
 assertEquals(Color.GREEN, chatFrame.showTxt.getBackground());
 }

 @Test
 public void testActionPerformedWhisper() {
 String expectedText = "/w " + chatFrame.list.getSelectedValue() + " ";
 chatFrame.txtInput.setText(expectedText);
 chatFrame.actionPerformed(new ActionEvent(chatFrame.txtInput, 0, "sendInput"));
 assertEquals(expectedText, chatFrame.txtInput.getText());
 }

 @Test(expected = IllegalStateException.class)
 public void testActionPerformedWhisperNoSelection() {
 chatFrame.txtInput.setText("/w ");
 chatFrame.actionPerformed(new ActionEvent(chatFrame.txtInput, 0, "sendInput"));
 }

 @Test
 public void testMouseClicked() {
 MouseEvent mouseEvent = mock(MouseEvent.class);
 chatFrame.maybeShowPopup(mouseEvent);
 assertFalse(chatFrame.listPop.isShowing());
 assertFalse(chatFrame.txtPop.isShowing());
 }

 @Test
 public void testMouseReleased() {
 MouseEvent mouseEvent = mock(MouseEvent.class);
 chatFrame.maybeShowPopup(mouseEvent);
 assertFalse(chatFrame.listPop.isShowing());
 assertFalse(chatFrame.txtPop.isShowing());
 }
}