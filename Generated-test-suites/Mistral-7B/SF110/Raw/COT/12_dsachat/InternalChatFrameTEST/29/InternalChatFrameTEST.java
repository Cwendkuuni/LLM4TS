package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

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
 InternalChatFrame internalChatFrame = new InternalChatFrame(parent);
 assertFalse(internalChatFrame.isClosed());
 }

 @Test
 public void testCreateGUI() {
 // Check if components are added correctly
 assertEquals(2, chatFrame.getContentPane().getComponentCount());
 // Test JSplitPane divider position
 chatFrame.setDividerLocation(50);
 chatFrame.setSize(400, 300);
 chatFrame.pack();
 Point dividerLocation = chatFrame.getJSplitPaneDividerLocation();
 assertTrue(dividerLocation.x > 200);
 }

 @Test
 public void testAddTextMethodBasic() {
 String line = "Test: Test";
 chatFrame.addText(line);
 assertNotNull(chatFrame.showTxt.getText());
 assertEquals(line, chatFrame.showTxt.getText().substring(0, chatFrame.showTxt.getCaretPosition()));
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextMethodException() {
 chatFrame.addText("Test");
 }

 @Test
 public void testAddStylesToDocumentMethodBasic() {
 Style regular = chatFrame.doc.getStyle("regular");
 assertTrue(regular != null);
 }

 @Test
 public void testChangeStyleMethodBasic() {
 chatFrame.changeStyle(12, Color.BLUE, false, false, "Test");
 Style testStyle = chatFrame.doc.getStyle("Test");
 assertEquals(Color.BLUE, testStyle.getForeground());
 }

 @Test
 public void testAddUserMethodBasic() {
 chatFrame.addUser("Test");
 assertEquals(1, chatFrame.list.getModel().getSize());
 }

 @Test
 public void testDeleteUserMethodBasic() {
 chatFrame.addUser("Test");
 chatFrame.deleteUser("Test");
 assertEquals(0, chatFrame.list.getModel().getSize());
 }

 @Test
 public void testGetUserlistMethodBasic() {
 String[] userlist = chatFrame.getUserlist();
 assertNotNull(userlist);
 assertEquals(0, userlist.length);
 }

 @Test
 public void testActionPerformedMethodChooseColor() {
 chatFrame.addUser("Test");
 chatFrame.list.setSelectedValue("Test");
 chatFrame.actionPerformed(new ActionEvent(chatFrame.list, ActionEvent.ACTION_PERFORMED, "chooseColor"));
 JColorChooser colorChooser = new JColorChooser();
 Color newColor = colorChooser.showDialog(chatFrame, "Choose Color for user Test", Color.BLACK);
 assertNotNull(newColor);
 chatFrame.changeStyle(12, newColor, false, false, "Test");
 }

 @Test
 public void testActionPerformedMethodBgColor() {
 chatFrame.addUser("Test");
 chatFrame.list.setSelectedValue("Test");
 chatFrame.actionPerformed(new ActionEvent(chatFrame.showTxt, ActionEvent.ACTION_PERFORMED, "bgColor"));
 JColorChooser colorChooser = new JColorChooser();
 Color newColor = colorChooser.showDialog(chatFrame, "Choose Color for user Test", Color.BLACK);
 assertNotNull(newColor);
 chatFrame.showTxt.setBackground(newColor);
 }

 @Test
 public void testActionPerformedMethodWhisper() {
 chatFrame.addUser("Test");
 chatFrame.list.setSelectedValue("Test");
 chatFrame.txtInput.setText("/w Test ");
 chatFrame.actionPerformed(new ActionEvent(chatFrame.txtInput, ActionEvent.ACTION_PERFORMED, "sendInput"));
 assertEquals("/w Test Test\n", chatFrame.showTxt.getText());
 }

 @Test
 public void testMouseClickedMethod() {
 chatFrame.addMouseListener(new MouseAdapter() {
 @Override
 public void mouseClicked(MouseEvent e) {
 // Emulate right mouse click on the showTextPane
 e.setModifiersExcluded(MouseEvent.BUTTON3_MASK);
 chatFrame.maybeShowPopup(e);
 }
 });
 chatFrame.setSize(400, 300);
 chatFrame.pack();
 chatFrame.setVisible(true);
 chatFrame.showTxt.requestFocus();
 Robot robot = new Robot();
 robot.mousePress(InputEvent.BUTTON3_MASK);
 robot.mouseRelease(InputEvent.BUTTON3_MASK);
 assertTrue(chatFrame.listPop.isVisible());
 }

}