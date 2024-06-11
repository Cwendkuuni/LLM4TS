package dsachat.client.gui.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private InternalChatFrame internalChatFrame;
 private MainFrame mainFrame;

 @Before
 public void setUp() {
 mainFrame = new MainFrame();
 internalChatFrame = new InternalChatFrame(mainFrame);
 internalChatFrame.pack();
 }

 @Test
 public void testCreateGUI() {
 // Check if the JList and JTextPane are added to the JSplitPane
 JSplitPane splitPane = (JSplitPane) internalChatFrame.getContentPane();
 JList list = (JList) splitPane.getLeftComponent();
 JTextPane showTxt = (JTextPane) splitPane.getRightComponent();
 assertNotNull(list);
 assertNotNull(showTxt);

 // Check if the JTextField is added to the JPanel
 JPanel right = (JPanel) splitPane.getRightComponent();
 JTextField txtInput = (JTextField) right.getComponent(1);
 assertNotNull(txtInput);
 }

 @Test
 public void testAddText() throws BadLocationException {
 // Set up the document with some styles
 DefaultStyledDocument doc = new DefaultStyledDocument();
 StyleConstants.setFontFamily(doc, "SansSerif");
 Style def = StyleContext.getDefaultStyleContext().getStyle("default");
 Style regular = doc.addStyle("regular", def);
 Style italic = doc.addStyle("italic", regular);
 Style bold = doc.addStyle("bold", regular);
 Style boldItalic = doc.addStyle("bold-italic", regular);
 Style name = doc.addStyle("name", regular);
 Style system = doc.addStyle("system", regular);

 // Set up the internalChatFrame with the document
 internalChatFrame.doc = doc;

 // Test adding text with no formatting
 internalChatFrame.addText("Test User: Hello");
 assertTextEquals("Test User: Hello\n", internalChatFrame.showTxt.getText());

 // Test adding text with formatting
 internalChatFrame.addText("* Test User *: *Hello*");
 assertTextEquals("Test User: Hello\n* Test User *\n", internalChatFrame.showTxt.getText());

 // Test adding text with multiple formattings
 internalChatFrame.addText("** *Test User* **: *Hello World*");
 assertTextEquals("Test User: Hello\n* Test User *\n** Test User **\n* Hello World *\n", internalChatFrame.showTxt.getText());
 }

 @Test
 public void testAddStylesToDocument() {
 // Check if the styles are added to the document
 DefaultStyledDocument doc = internalChatFrame.doc;
 Style regular = doc.getStyle("regular");
 Style italic = doc.getStyle("italic");
 Style bold = doc.getStyle("bold");
 Style boldItalic = doc.getStyle("bold-italic");
 Style name = doc.getStyle("name");
 Style system = doc.getStyle("system");

 // Check the properties of the styles
 assertEquals(StyleConstants.DefaultFontFamily, StyleConstants.getFontFamily(regular));
 assertTrue(StyleConstants.isItalic(italic));
 assertTrue(StyleConstants.isBold(bold));
 assertTrue(StyleConstants.isBold(boldItalic));
 assertTrue(StyleConstants.isItalic(boldItalic));
 assertEquals(Color.BLUE, StyleConstants.getForeground(name));
 assertEquals(16, StyleConstants.getFontSize(name));
 assertEquals(Color.BLACK, StyleConstants.getForeground(system));
 assertEquals(0.0f, StyleConstants.getForeground(system).getAlpha(), 0.01f);
 assertEquals(14, StyleConstants.getFontSize(system));
 }

 @Test
 public void testChangeStyle() {
 // Set up the document with some styles
 DefaultStyledDocument doc = new DefaultStyledDocument();
 Style def = StyleContext.getDefaultStyleContext().getStyle("default");
 Style regular = doc.addStyle("regular", def);
 Style italic = doc.addStyle("italic", regular);
 Style bold = doc.addStyle("bold", regular);
 Style boldItalic = doc.addStyle("bold-italic", regular);
 Style name = doc.addStyle("name", regular);
 Style system = doc.addStyle("system", regular);

 // Set up the internalChatFrame with the document
 internalChatFrame.doc = doc;

 // Test changing the font size, color, bold, and italic for a style
 internalChatFrame.changeStyle(14, Color.RED, false, false, "name");
 assertEquals(Color.RED, StyleConstants.getForeground(internalChatFrame.doc.getStyle("name")));
 assertEquals((short) 14, StyleConstants.getFontSize(internalChatFrame.doc.getStyle("name")));

 // Test changing the background color for the JTextPane
 internalChatFrame.changeStyle(0, Color.GREEN, false, false, "system");
 assertEquals(Color.GREEN, internalChatFrame.showTxt.getBackground());
 }

 @Test
 public void testAddUser() {
 // Test adding a user to the list and the document
 internalChatFrame.addUser("Test User 1");
 List<String> userlist = Arrays.asList(internalChatFrame.getUserlist());
 assertEquals(1, userlist.size());
 assertEquals("Test User 1", userlist.get(0));
 assertTextEquals("Test User 1\n", internalChatFrame.showTxt.getText());

 // Test adding multiple users to the list and the document
 internalChatFrame.addUser("Test User 2");
 internalChatFrame.addUser("Test User 3");
 userlist = Arrays.asList(internalChatFrame.getUserlist());
 assertEquals(3, userlist.size());
 assertEquals("Test User 1", userlist.get(0));
 assertEquals("Test User 2", userlist.get(1));
 assertEquals("Test User 3", userlist.get(2));
 assertTextEquals("Test User 1\nTest User 2\nTest User 3\n", internalChatFrame.showTxt.getText());
 }

 @Test
 public void testDeleteUser() {
 // Test deleting a user from the list and the document
 internalChatFrame.addUser("Test User 1");
 internalChatFrame.addUser("Test User 2");
 internalChatFrame.deleteUser("Test User 1");
 List<String> userlist = Arrays.asList(internalChatFrame.getUserlist());
 assertEquals(1, userlist.size());
 assertEquals("Test User 2", userlist.get(0));
 assertTextEquals("Test User 2\n", internalChatFrame.showTxt.getText());

 // Test deleting the last user from the list and the document
 internalChatFrame.deleteUser("Test User 2");
 userlist = Arrays.asList(internalChatFrame.getUserlist());
 assertEquals(0, userlist.size());
 assertTextEquals("", internalChatFrame.showTxt.getText());
 }

 @Test
 public void testGetUserlist() {
 // Test getting the userlist from the InternalChatFrame
 internalChatFrame.addUser("Test User 1");
 internalChatFrame.addUser("Test User 2");
 List<String> userlist = internalChatFrame.getUserlist();
 assertEquals(2, userlist.size());
 assertEquals("Test User 1", userlist.get(0));
 assertEquals("Test User 2", userlist.get(1));
 }

 // Test handling user selection in the JList
 @Test
 public void testListSelection() {
 internalChatFrame.addUser("Test User 1");
 internalChatFrame.addUser("Test User 2");
 internalChatFrame.pack();
 internalChatFrame.setVisible(true);

 ListSelectionListener listSelectionListener = mock(ListSelectionListener.class);
 internalChatFrame.list.addListSelectionListener(listSelectionListener);

 internalChatFrame.list.setSelectedIndex(0);
 verify(listSelectionListener).valueChanged(argThat((ListSelectionEvent e) -> e.getFirstIndex() == 0 && e.getLastIndex() == 0));

 internalChatFrame.list.setSelectedIndex(1);
 verify(listSelectionListener).valueChanged(argThat((ListSelectionEvent e) -> e.getFirstIndex() == 1 && e.getLastIndex() == 1));
 }

 // Test handling the "sendInput" action event in the JTextField
 @Test
 public void testSendInput() {
 internalChatFrame.addUser("Test User 1");
 internalChatFrame.addUser("Test User 2");
 internalChatFrame.pack();
 internalChatFrame.setVisible(true);
 internalChatFrame.txtInput.setText("Hello Test User 2");
 internalChatFrame.txtInput.doClick();
 internalChatFrame.txtInput.dispatchEvent(new ActionEvent(internalChatFrame.txtInput, ActionEvent.ACTION_PERFORMED, "sendInput"));
 assertTextEquals("Test User 1: Hello Test User 2\n", internalChatFrame.showTxt.getText());
 }

 // Test handling the "chooseColor" action event in the JPopupMenu
 @Test
 public void testChooseColor() {
 internalChatFrame.addUser("Test User 1");
 internalChatFrame.pack();
 internalChatFrame.setVisible(true);

 JColorChooser colorChooser = mock(JColorChooser.class);
 JDialog dialog = mock(JDialog.class);
 when(colorChooser.createDialog(internalChatFrame, "Choose Color for user Test User 1", true, JFrame.DEFAULT_OPTIONS)).thenReturn(dialog);
 when(dialog.showDialog(internalChatFrame, "Choose Color for user Test User 1")).thenReturn(JColorChooser.APPROVE_OPTION);
 Color newColor = Color.RED;
 when(colorChooser.getColor()).thenReturn(newColor);

 internalChatFrame.listPop.show(internalChatFrame.list, 0, 0);
 internalChatFrame.listPop.getComponent(0).doClick();
 verify(internalChatFrame).changeStyle(12, newColor, false, false, "name");
 }

 // Test handling the "bgColor" action event in the JPopupMenu
 @Test
 public void testBgColor() {
 internalChatFrame.pack();
 internalChatFrame.setVisible(true);

 JColorChooser colorChooser = mock(JColorChooser.class);
 JDialog dialog = mock(JDialog.class);
 when(colorChooser.createDialog(internalChatFrame, "Choose Color for user Test User 1", true, JFrame.DEFAULT_OPTIONS)).thenReturn(dialog);
 when(dialog.showDialog(internalChatFrame, "Choose Color for user Test User 1")).thenReturn(JColorChooser.APPROVE_OPTION);
 Color newColor = Color.GREEN;
 when(colorChooser.getColor()).thenReturn(newColor);

 internalChatFrame.txtPop.show(internalChatFrame.showTxt, 0, 0);
 internalChatFrame.txtPop.getComponent(0).doClick();
 verify(internalChatFrame).changeStyle(0, newColor, false, false, "system");
 }
}