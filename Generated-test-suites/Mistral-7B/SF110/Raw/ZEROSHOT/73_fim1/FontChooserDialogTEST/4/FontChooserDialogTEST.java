package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;
 private JFontChooser chooserPane;

 @Before
 public void setup() {
 frame = new JFrame();
 chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, (ActionEvent e) -> {}, (ActionEvent e) -> {});
 }

 @Test
 public void testConstructor() {
 // Test that the dialog is initialized with the correct components
 assertNotNull(dialog.getContentPane());
 assertInstanceOf(JPanel.class, dialog.getContentPane());
 assertNotNull(dialog.getRootPane());
 assertInstanceOf(JDialog.class, dialog.getRootPane());
 assertNotNull(dialog.getTitle());
 assertNotNull(dialog.getOkButton());
 assertNotNull(dialog.getCancelButton());
 assertNotNull(dialog.getResetButton());
 assertInstanceOf(JButton.class, dialog.getOkButton());
 assertInstanceOf(JButton.class, dialog.getCancelButton());
 assertInstanceOf(JButton.class, dialog.getResetButton());
 // Test that the dialog is initially hidden
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testSetVisible() {
 // Test that the dialog becomes visible when setVisible(true) is called
 dialog.setVisible(true);
 assertTrue(dialog.isVisible());
 // Test that the initial font is saved when the dialog becomes visible
 dialog.setVisible(true);
 assertEquals(chooserPane.getFont(), dialog.initialFont);
 // Test that the dialog hides when setVisible(false) is called
 dialog.setVisible(false);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testReset() {
 // Test that the dialog resets the font to the initial font when the reset button is clicked
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 14));
 dialog.reset();
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testActionPerformedOk() {
 // Test that the OK button dismisses the dialog
 dialog.setVisible(true);
 dialog.okButton.doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testActionPerformedCancel() {
 // Test that the Cancel button dismisses the dialog
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testActionPerformedReset() {
 // Test that the Reset button resets the font to the initial font
 dialog.setVisible(true);
 dialog.resetButton.doClick();
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testKeyEventCancel() {
 // Test that the Escape key dismisses the dialog
 dialog.setVisible(true);
 Robot robot = new Robot();
 robot.keyPress(KeyEvent.VK_ESCAPE);
 robot.keyRelease(KeyEvent.VK_ESCAPE);
 assertFalse(dialog.isVisible());
 }
}