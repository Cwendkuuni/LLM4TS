package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;
 private JFontChooser chooserPane;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Before
 public void setUp() {
 frame = new JFrame();
 chooserPane = new JFontChooser();
 okListener = e -> {};
 cancelListener = e -> {};
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 }

 @Test
 public void testConstructor() {
 // Test that a new FontChooserDialog is created
 assertNotNull(dialog);
 }

 @Test
 public void testSetVisible() {
 // Test that the initial font is null before the dialog is shown
 assertNull(dialog.getChooserPane().getFont());
 dialog.setVisible(true);
 // Test that the initial font is saved when the dialog is hidden
 dialog.setVisible(false);
 Font initialFont = dialog.getChooserPane().getFont();
 // Test that the initial font is set back when reset is called
 dialog.reset();
 assertEquals(initialFont, dialog.getChooserPane().getFont());
 }

 @Test
 public void testOkButtonAction() {
 // Test that the OK button hides the dialog
 dialog.setVisible(true);
 dialog.getOkButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testCancelButtonAction() {
 // Test that the Cancel button hides the dialog
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testResetButtonAction() {
 // Test that the Reset button sets the font to the initial font
 dialog.setVisible(true);
 dialog.getResetButton().doClick();
 assertEquals(dialog.getInitialFont(), dialog.getChooserPane().getFont());
 }

 @Test
 public void testKeyEventForCancelButton() {
 // Test that pressing ESC key behaves like clicking Cancel button
 dialog.setVisible(true);
 dialog.getRootPane().dispatchEvent(new KeyEvent(dialog, KeyEvent.KEY_PRESSED, 0, '\u001b', 0, 0, false));
 assertFalse(dialog.isVisible());
 }
}