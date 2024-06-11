package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;

 @Before
 public void setUp() {
 chatFrame = new InternalChatFrame(new JFrame());
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @Test
 public void testCreateGUI() {
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 }

 @Test
 public void testAddText() {
 String line = "TestUser: Hello World!";
 chatFrame.addText(line);
 String expectedText = "TestUser: Hello World!\n";
 assertEquals(expectedText, showTxt.getText());
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

 assertEquals("default", regular.getName());
 assertEquals("italic", italic.getName());
 assertEquals("bold", bold.getName());
 assertEquals("bold-italic", boldItalic.getName());
 assertEquals("name", name.getName());
 assertEquals("system", system.getName());
 assertEquals("SansSerif", StyleConstants.getFontFamily(def));
 assertNotNull(name.getForeground());
 assertNotNull(system.getForeground());
 assertNotNull(regular.getFont());
 }

 @Test
 public void testChangeStyle() {
 chatFrame.changeStyle(12, Color.RED, true, false, "TestUser");
 Style testUserStyle = chatFrame.doc.getStyle("TestUser");
 assertEquals(Color.RED, testUserStyle.getForeground());
 chatFrame.changeStyle(12, null, false, false, "TestUser");
 assertEquals(Color.BLUE, testUserStyle.getForeground());
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("TestUser1");
 String[] expectedUserlist = {"TestUser1"};
 assertEquals(expectedUserlist.length, chatFrame.getUserlist().length);
 assertEquals(expectedUserlist[0], chatFrame.getUserlist()[0]);
 }

 @Test
 public void testDeleteUser() {
 String[] users = {"TestUser1", "TestUser2", "TestUser3"};
 chatFrame.addUser("TestUser1");
 chatFrame.addUser("TestUser2");
 chatFrame.addUser("TestUser3");
 String[] expectedUserlist = {"TestUser2", "TestUser3"};
 chatFrame.deleteUser("TestUser1");
 assertEquals(expectedUserlist.length, chatFrame.getUserlist().length);
 assertEquals(expectedUserlist[0], chatFrame.getUserlist()[0]);
 }

 @Test
 public void testGetChatlog() {
 String testText = "TestUser1: Hello TestUser2\nTestUser2: Hello TestUser1\nTestUser1: Bye\n";
 chatFrame.showTxt.setText(testText);
 assertEquals(testText, chatFrame.getChatlog());
 }

 @Test
 public void testActionPerformed() {
 final String name = "TestUser";
 final String chooseColorCommand = "chooseColor";
 final String bgColorCommand = "bgColor";
 final String whisperCommand = "whisper";

 chatFrame.addMouseListener(new MouseAdapter() {
 @Override
 public void mousePressed(MouseEvent e) {
 if (e.getSource().equals(chatFrame.list)) {
 chatFrame.listPop.show(chatFrame.list, e.getX(), e.getY());
 }
 }
 });
 chatFrame.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 if (e.getActionCommand().equals(chooseColorCommand)) {
 assertEquals("Choose Color for user " + name, e.getActionCommand());
 }
 if (e.getActionCommand().equals(bgColorCommand)) {
 assertEquals("Choose Color for user " + name, e.getActionCommand());
 }
 if (e.getActionCommand().equals(whisperCommand)) {
 assertEquals("/w " + name + " ", e.getActionCommand());
 }
 }
 });

 list.addListSelectionListener(new ListSelectionListener() {
 @Override
 public void valueChanged(ListSelectionEvent e) {
 if (!e.getValueIsAdjusting()) {
 assertEquals(list.getSelectedValue(), name);
 }
 }
 });

 txtInput.setText("TestMessage");
 chatFrame.actionPerformed(new ActionEvent(txtInput, 0, "sendInput"));
 assertEquals("TestUser: TestMessage\n", chatFrame.showTxt.getText());
 }
}