package osa.ora.server.client.ui.utils.test;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.client.ui.utils.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;

 @Before
 public void setUp() {
 frame = new JFrame();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 }

 // Test constructor
 @Test
 public void testFontChooserDialogConstructor() {
 // Check if dialog is initialized correctly
 assertNotNull(dialog);
 }

 // Test setVisible method
 @Test
 public void testSetVisible() {
 // Check if initialFont is set correctly when dialog is shown
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 dialog.setVisible(false);

 // Check if initialFont is reset when dialog is hidden
 dialog.setVisible(true);
 dialog.reset();
 assertNull(dialog.initialFont);
 }

 // Test reset method
 @Test
 public void testReset() {
 // Set a font in chooserPane and check if it can be reset
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertSame(dialog.initialFont, dialog.chooserPane.getFont());
 }

 // Test exception handling when no ActionListeners are provided
 @Test(expected = NullPointerException.class)
 public void testNullOkListener() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.okButton.doClick();
 }

 // Test exception handling when no ActionListeners are provided for cancel button
 @Test(expected = NullPointerException.class)
 public void testNullCancelListener() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 }

 // Test exception handling when no InitialFont is provided
 @Test(expected = IllegalArgumentException.class)
 public void testNullInitialFont() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 dialog.chooserPane.setFont(null);
 dialog.setVisible(true);
 dialog.reset();
 }

 // Test exception handling when no OK button is added to the dialog
 @Test(expected = IllegalStateException.class)
 public void testNoOkButton() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.okButton.dispatchEvent(new MouseEvent(dialog, 1, 1, 1, System.currentTimeMillis(), new Point(1, 1), 1, false));
 dialog.dispose();
 }

 // Test exception handling when no Cancel button is added to the dialog
 @Test(expected = IllegalStateException.class)
 public void testNoCancelButton() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.cancelButton.dispatchEvent(new MouseEvent(dialog, 1, 1, 1, System.currentTimeMillis(), new Point(1, 1), 1, false));
 dialog.dispose();
 }

 // Test exception handling when OK and Cancel buttons have the same ActionCommand
 @Test(expected = IllegalArgumentException.class)
 public void testSameActionCommand() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 dialog.okButton.setActionCommand("cancel");
 dialog.cancelButton.setActionCommand("OK");
 dialog.setVisible(true);
 dialog.okButton.dispatchEvent(new ActionEvent(dialog.okButton, ActionEvent.ACTION_PERFORMED, "OK"));
 dialog.dispose();
 }

 // Test exception handling when attempting to set a null font in chooserPane
 @Test(expected = NullPointerException.class)
 public void testNullFontInChooserPane() {
 dialog.chooserPane.setFont(null);
 dialog.setVisible(true);
 dialog.reset();
 }

 // Test exception handling when attempting to set a font with a negative size
 @Test(expected = IllegalArgumentException.class)
 public void testNegativeFontSize() {
 Font font = new Font("Arial", Font.BOLD, -1);
 dialog.chooserPane.setFont(font);
 dialog.setVisible(true);
 dialog.reset();
 }

 // Test exception handling when attempting to set a font with a null name
 @Test(expected = NullPointerException.class)
 public void testNullFontName() {
 Font font = new Font(null, Font.BOLD, 12);
 dialog.chooserPane.setFont(font);
 dialog.setVisible(true);
 dialog.reset();
 }

 // Test exception handling when attempting to set a font with an invalid style
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFontStyle() {
 Font font = new Font("Arial", 123, Font.BOLD);
 dialog.chooserPane.setFont(font);
 dialog.setVisible(true);
 dialog.reset();
 }
}