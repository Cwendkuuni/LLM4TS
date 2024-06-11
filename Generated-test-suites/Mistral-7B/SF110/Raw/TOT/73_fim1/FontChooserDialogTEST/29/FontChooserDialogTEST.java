package osa.ora.server.client.ui.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private JFrame frame;
 private JDialog dialog;
 private ActionListener okListener = e -> {};
 private ActionListener cancelListener = e -> {};

 @Before
 public void setUp() {
 frame = new JFrame();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, new JFontChooser(), okListener, cancelListener);
 }

 @Test
 public void testConstructor() {
 // Test if the dialog is initialized correctly
 assertTrue(dialog instanceof JDialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertFalse(dialog.isVisible());

 // Test if the OK and Cancel buttons are initialized correctly
 JButton okButton = (JButton)dialog.getRootPane().getComponent(0);
 JButton cancelButton = (JButton)dialog.getRootPane().getComponent(1);
 assertEquals("OK", okButton.getText());
 assertEquals("Cancel", cancelButton.getText());
 }

 @Test
 public void testSetVisible() {
 // Test if the initialFont is set correctly when the dialog is shown
 dialog.setVisible(true);
 dialog.setVisible(false);
 JFontChooser chooserPane = dialog.chooserPane;
 assertEquals(chooserPane.getFont(), dialog.initialFont);
 }

 @Test
 public void testReset() {
 // Test if the font is reset correctly
 dialog.reset();
 JFontChooser chooserPane = dialog.chooserPane;
 assertEquals(dialog.initialFont, chooserPane.getFont());
 }

 @Test
 public void testOKButtonAction() {
 // Test if the OK button closes the dialog
 dialog.setVisible(true);
 okButtonAction(dialog.okButton);
 assertTrue(dialog.isVisible());
 okButtonAction(dialog.okButton);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testCancelButtonAction() {
 // Test if the Cancel button closes the dialog
 dialog.setVisible(true);
 cancelButtonAction(dialog.cancelButton);
 assertTrue(dialog.isVisible());
 cancelButtonAction(dialog.cancelButton);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testResetButtonAction() {
 // Test if the Reset button resets the font
 dialog.setVisible(true);
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 resetButtonAction(dialog.resetButton);
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testKeyEventForCancelButton() {
 // Test if the Cancel button responds to the Escape key
 dialog.setVisible(true);
 dialog.pack();
 Robot robot = new Robot();
 robot.keyPress(KeyEvent.VK_ESCAPE);
 assertTrue(dialog.isVisible());
 robot.keyRelease(KeyEvent.VK_ESCAPE);
 cancelButtonAction(dialog.cancelButton);
 assertFalse(dialog.isVisible());
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 private void okButtonAction(JButton button) {
 button.doClick();
 }

 private void cancelButtonAction(JButton button) {
 button.doClick();
 }

 private void resetButtonAction(JButton button) {
 button.doClick();
 }
}