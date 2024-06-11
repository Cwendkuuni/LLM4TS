package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parentFrame;

 @Before
 public void setUp() {
 parentFrame = new MainFrame();
 chatFrame = new InternalChatFrame(parentFrame);
 }

 @After
 public void tearDown() {
 chatFrame.dispose();
 parentFrame.dispose();
 }

 @Test
 public void testInternalChatFrameConstructor() {
 InternalChatFrame actualInternalChatFrame = chatFrame;
 assertNotNull(actualInternalChatFrame);
 }

 @Test
 public void testCreateGUI() {
 assertNotNull(chatFrame.getList());
 assertNotNull(chatFrame.getShowTxt());
 assertNotNull(chatFrame.getTextInput());
 }

 @Test
 public void testAddText() {
 String line = "Test:Test";
 chatFrame.addText(line);
 String expectedChatlog = line + "\n";
 String actualChatlog = chatFrame.getChatlog();
 assertEquals(expectedChatlog, actualChatlog);
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithBadLocationException() {
 chatFrame.addText("Test:Test");
 chatFrame.addText("Test:Test");
 }

 @Test
 public void testAddStylesToDocument() {
 assertNotNull(chatFrame.doc);
 }

 @Test
 public void testChangeStyle() {
 Color newColor = Color.RED;
 chatFrame.changeStyle(12, newColor, false, false, "TestUser");
 assertEquals(newColor, chatFrame.doc.getStyle("TestUser").getForeground());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("TestUser");
 assertEquals("TestUser", chatFrame.list.getModel().getElementAt(0));
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("TestUser1");
 chatFrame.addUser("TestUser2");
 chatFrame.deleteUser("TestUser1");
 assertEquals(1, chatFrame.list.getModel().getSize());
 }

 @Test
 public void testGetUserlist() {
 String[] expectedUserlist = {"TestUser2"};
 String[] actualUserlist = chatFrame.getUserlist();
 assertArrayEquals(expectedUserlist, actualUserlist);
 }

 @Test
 public void testActionPerformedChooseColor() {
 JColorChooser dialog = new JColorChooser();
 Color newColor = dialog.showDialog(null, "Choose Color for user TestUser", Color.BLACK);
 chatFrame.actionPerformed(new ActionEvent(new Object(), 0, "chooseColor"));
 assertEquals(newColor, chatFrame.doc.getStyle("TestUser").getForeground());
 }

 @Test
 public void testActionPerformedBgColor() {
 JColorChooser dialog = new JColorChooser();
 Color newColor = dialog.showDialog(null, "Choose Color for user TestUser", Color.BLACK);
 chatFrame.actionPerformed(new ActionEvent(new Object(), 0, "bgColor"));
 assertEquals(newColor, chatFrame.getShowTxt().getBackground());
 }

 @Test
 public void testActionPerformedWhisper() {
 String expectedTextInput = "/w TestUser ";
 String actualTextInput = chatFrame.getTextInput().getText();
 mouseEventForTextInput(MouseEvent.MOUSE_CLICKED);
 assertEquals(expectedTextInput, actualTextInput);
 }

 @Test
 public void testMouseClickedOnList() {
 chatFrame.listPop.show(chatFrame.list, 10, 10);
 mouseEventForList(MouseEvent.MOUSE_CLICKED);
 assertTrue(chatFrame.listPop.isVisible());
 }

 @Test
 public void testMouseClickedOnText() {
 chatFrame.txtPop.show(chatFrame.list, 10, 10);
 mouseEventForText(MouseEvent.MOUSE_CLICKED);
 assertTrue(chatFrame.txtPop.isVisible());
 }

 private void mouseEventForList(MouseEvent event) {
 Point point = new Point(10, 10);
 Robot robot = new Robot();
 robot.mousePress(InputEvent.BUTTON1_MASK);
 robot.mouseMove(point.x, point.y);
 robot.mouseRelease(InputEvent.BUTTON1_MASK);
 chatFrame.list.addMouseListener(e -> chatFrame.maybeShowPopup(e));
 }

 private void mouseEventForText(MouseEvent event) {
 Point point = new Point(10, 10);
 Robot robot = new Robot();
 robot.mousePress(InputEvent.BUTTON1_MASK);
 robot.mouseMove(point.x, point.y);
 robot.mouseRelease(InputEvent.BUTTON1_MASK);
 chatFrame.showTxt.addMouseListener(e -> chatFrame.maybeShowPopup(e));
 }
}