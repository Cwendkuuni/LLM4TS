package dsachat.client.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.junit.*;

public class InternalChatFrameTest {

 private InternalChatFrame icf;
 private MainFrame parent;

 @Before
 public void setUp() {
 parent = new MainFrame();
 icf = new InternalChatFrame(parent);
 }

 @Test
 public void testCreateGUI() {
 //Check if the JList is created
 assertNotNull(icf.list);
 //Check if the JTextPane is created
 assertNotNull(icf.showTxt);
 //Check if the JTextField is created
 assertNotNull(icf.txtInput);
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello World";
 icf.addText(line);
 //Check if the text is added to the JTextPane
 assertEquals(line, icf.showTxt.getText());
 }

 @Test
 public void testAddStylesToDocument() {
 //Assuming that addStylesToDocument method is called in the constructor
 //Check if the regular style is added
 Style regular = icf.doc.getStyle("regular");
 assertNotNull(regular);
 //Check if the name style is added
 Style name = icf.doc.getStyle("name");
 assertNotNull(name);
 }

 @Test
 public void testAddUser() {
 icf.addUser("User1");
 //Check if the user is added to the JList
 assertEquals("User1", icf.listModel.getElementAt(0));
 //Check if the user is added to the JTextPane
 assertEquals("User1", icf.doc.getStyleNames()[0]);
 }

 @Test
 public void testDeleteUser() {
 icf.addUser("User1");
 icf.deleteUser("User1");
 //Check if the user is removed from the JList
 assertFalse(icf.listModel.contains("User1"));
 //Check if the user is removed from the JTextPane
 assertEquals(0, icf.doc.getStyleNames().length);
 }

 @Test
 public void testGetUserlist() {
 icf.addUser("User1");
 icf.addUser("User2");
 String[] uList = icf.getUserlist();
 assertEquals(2, uList.length);
 assertEquals("User1", uList[0]);
 assertEquals("User2", uList[1]);
 }

 @Test
 public void testActionPerformed() {
 //Assuming that a mock MainFrame is passed with a spy on addActionListener
 ActionEvent event = mock(ActionEvent.class);
 doCallRealMethod().when(icf).actionPerformed(event);
 //Test choosing color for a user
 icf.listPop.getComponentAt(0).doClick();
 JColorChooser chooser = spy(JColorChooser.class);
 doReturn(chooser).when(icf).getComponent(eq("chooseColor"));
 doReturn(Color.RED).when(chooser).showDialog(icf, "Choose Color for user User1", Color.black);
 event.setSource(icf.list);
 event.setActionCommand("chooseColor");
 icf.actionPerformed(event);
 assertEquals("User1", icf.name);
 assertEquals(Color.RED, icf.newColor);
 //Test background color for a user
 event.setActionCommand("bgColor");
 icf.txtPop.getComponentAt(0).doClick();
 chooser = spy(JColorChooser.class);
 doReturn(chooser).when(icf).getComponent(eq("bgColor"));
 doReturn(Color.GREEN).when(chooser).showDialog(icf, "Choose Color for user User1", Color.black);
 icf.actionPerformed(event);
 assertEquals(Color.GREEN, icf.backgroundColor);
 //Test whispering a user
 event.setActionCommand("whisper");
 icf.list.setSelectedValue("User1", false);
 icf.actionPerformed(event);
 assertEquals("/w User1 ", icf.txtInput.getText());
 }

 @Test
 public void testMouseClicked() {
 MouseEvent event = new MouseEvent(icf, 1, 1, 1, 1, 1, 1, true);
 icf.list.dispatchEvent(event);
 assertTrue(icf.listPop.isVisible());
 }

 @After
 public void tearDown() {
 //Add code to clean up the test here
 icf.dispose();
 parent.dispose();
 }
}