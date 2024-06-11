package dsachat.client.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

 private MainFrame parent;
 private InternalChatFrame chatFrame;

 @Before
 public void setUp() {
 parent = new MainFrame();
 chatFrame = new InternalChatFrame(parent);
 }

 @Test
 public void testCreateGUI() {
 // Check if components are added correctly
 assertNotNull(chatFrame.getList());
 assertNotNull(chatFrame.getShowTxt());
 assertNotNull(chatFrame.getTxtInput());

 // Check if listeners are added correctly
 assertTrue(chatFrame.getList().hasFocusableComponent(0));
 assertTrue(chatFrame.getShowTxt().hasFocusableComponent(0));
 assertTrue(chatFrame.getTxtInput().hasFocusableComponent(0));
 }

 @Test
 public void testAddText() {
 // Create test data
 String line = "You: Hello World";

 // Add test data to chat frame
 chatFrame.addText(line);

 // Check if text was added correctly
 assertEquals(line, chatFrame.getShowTxt().getText().substring(0, chatFrame.getShowTxt().getCaretPosition()));
 }

 @Test
 public void testAddStylesToDocument() {
 // Create test data
 StyledDocument doc = chatFrame.getShowTxt().getStyledDocument();

 // Test default style
 assertEquals("regular", doc.getStyleNames()[0]);

 // Test italic style
 Style italic = doc.getStyle("italic");
 assertEquals(false, italic.isBold());
 assertEquals(true, italic.isItalic());

 // Test bold style
 Style bold = doc.getStyle("bold");
 assertEquals(true, bold.isBold());
 assertEquals(false, bold.isItalic());

 // Test bold-italic style
 Style boldItalic = doc.getStyle("bold-italic");
 assertEquals(true, boldItalic.isBold());
 assertEquals(true, boldItalic.isItalic());
 }

 @Test
 public void testActionPerformed() {
 // Create test data
 String name = "TestUser";
 String expectedCommand = "chooseColor";

 // Simulate action event for "Change Color" menu item
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, expectedCommand));

 // Check if style was changed
 Style style = chatFrame.getDoc().getStyle(name);
 assertTrue(style.getBackground() instanceof Color);
 }

 @Test
 public void testMouseClicked() {
 // Create test data
 MouseEvent event = new MouseEvent(chatFrame.getShowTxt(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false);

 // Simulate mouse click event
 chatFrame.maybeShowPopup(event);

 // Check if popup menu was shown
 assertTrue(chatFrame.getListPop().isVisible());
 }
}