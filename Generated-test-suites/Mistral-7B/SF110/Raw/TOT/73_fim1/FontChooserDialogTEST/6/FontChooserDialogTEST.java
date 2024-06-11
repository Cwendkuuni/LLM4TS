package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private JFrame parentFrame; // Mock or real parent frame for testing
 private FontChooserDialog dialog;
 private ActionListener okListener = e -> {}; // Dummy OK listener
 private ActionListener cancelListener = e -> {}; // Dummy CANCEL listener

 @Before
 public void setUp() {
 JFrame frame = new JFrame();
 parentFrame = frame;
 }

 @Test
 public void testFontChooserDialogCreation() {
 // Testing if a FontChooserDialog can be created with correct arguments
 dialog = new FontChooserDialog(parentFrame, "Test Title", true, new JFontChooser(), okListener, cancelListener);
 assertNotNull(dialog);
 }

 @Test
 public void testFontChooserDialogSetVisibleTrue() {
 // Testing if the initial font is correctly set when the dialog is shown
 dialog = new FontChooserDialog(parentFrame, "Test Title", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
 }

 @Test
 public void testFontChooserDialogSetVisibleFalse() {
 // Testing if the dialog is hidden when setVisible(false) is called
 dialog = new FontChooserDialog(parentFrame, "Test Title", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testFontChooserDialogOkButton() {
 // Testing if the OK button is disabled when the dialog is shown
 dialog = new FontChooserDialog(parentFrame, "Test Title", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 assertFalse(dialog.okButton.isEnabled());

 // Testing if the OK button is enabled when a font is selected
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 14));
 assertTrue(dialog.okButton.isEnabled());

 // Testing if the dialog is hidden when the OK button is pressed
 dialog.okButton.doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testFontChooserDialogCancelButton() {
 // Testing if the Cancel button is always enabled
 dialog = new FontChooserDialog(parentFrame, "Test Title", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 assertTrue(dialog.cancelButton.isEnabled());

 // Testing if the dialog is hidden when the Cancel button is pressed
 dialog.cancelButton.doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testFontChooserDialogResetButton() {
 // Testing if the initial font is correctly reset when the reset button is pressed
 dialog = new FontChooserDialog(parentFrame, "Test Title", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 15));
 dialog.resetButton.doClick();
 assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
 }
}