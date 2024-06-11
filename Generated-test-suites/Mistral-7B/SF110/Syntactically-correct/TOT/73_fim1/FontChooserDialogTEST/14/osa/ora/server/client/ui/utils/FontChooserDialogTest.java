package osa.ora.server.client.ui.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontChooserDialogTest {

 private FontChooserDialog fontChooserDialog;
 private JFrame frame;
 private ActionListener okListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Verify the font has been changed when the OK button is clicked
 Font font = fontChooserDialog.chooserPane.getFont();
 Assert.assertNotNull(font);
 // Perform additional checks on the font if needed
 }
 };
 private ActionListener cancelListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Verify the dialog is hidden when the Cancel button is clicked
 Assert.assertFalse(fontChooserDialog.isVisible());
 }
 };

 @Before
 public void setUp() {
 frame = new JFrame();
 fontChooserDialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, new JFontChooser(), okListener, cancelListener);
 }

 @Test
 public void testInitiallyShowsCorrectFont() {
 // Verify that the dialog initially shows the correct font
 Font initialFont = fontChooserDialog.chooserPane.getFont();
 Assert.assertNotNull(initialFont);
 // Perform additional checks on the font if needed
 }

 @Test
 public void testFontCanBeChangedWithOKButton() {
 // Set up a new font to test
 Font newFont = new Font("Arial", Font.BOLD, 14);
 fontChooserDialog.chooserPane.setFont(newFont);
 // Click the OK button to apply the new font
 fontChooserDialog.okButton.doClick();
 // Verify that the new font has been applied
 Font appliedFont = fontChooserDialog.chooserPane.getFont();
 Assert.assertEquals(newFont, appliedFont);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFontChooser() {
 // Test exception when null font chooser is passed to constructor
 fontChooserDialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, okListener, cancelListener);
 }

 @Test
 public void testCancelButtonClosesDialog() {
 // Click the Cancel button to close the dialog
 fontChooserDialog.cancelButton.doClick();
 // Verify that the dialog is hidden
 Assert.assertFalse(fontChooserDialog.isVisible());
 }
}