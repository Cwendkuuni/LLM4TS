package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;
 private JFontChooser chooserPane;

 @Before
 public void setUp() {
 JVMArguments.limitMemory(50 * 1024 * 1024); // Limit JVM memory to 50MB for test
 frame = new JFrame();
 chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Do something when OK button is clicked
 }
 }, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Do something when Cancel button is clicked
 }
 });
 }

 @Test
 public void testConstructor() {
 // Test constructor with valid arguments
 assertNotNull(dialog);
 assertNotNull(dialog.chooserPane);
 // Test constructor with null arguments
 try {
 FontChooserDialog dialog2 = new FontChooserDialog(null, null, true, null, null, null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testSetVisible() {
 // Test setting dialog as visible and getting initial font
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 // Test setting dialog as invisible and resetting font
 dialog.reset();
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertNotSame(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testOkButtonAction() {
 // Test setting OK button action listener
 ActionEvent okEvent = new ActionEvent(dialog.okButton, ActionEvent.ACTION_PERFORMED, "OK");
 dialog.okButton.dispatchEvent(okEvent);
 // Test dialog disappears after OK button is clicked
 assertTrue(dialog.isVisible());
 dialog.okButton.dispatchEvent(okEvent);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testCancelButtonAction() {
 // Test setting Cancel button action listener
 ActionEvent cancelEvent = new ActionEvent(dialog.cancelButton, ActionEvent.ACTION_PERFORMED, "Cancel");
 dialog.cancelButton.dispatchEvent(cancelEvent);
 // Test dialog disappears after Cancel button is clicked
 assertTrue(dialog.isVisible());
 dialog.cancelButton.dispatchEvent(cancelEvent);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testResetButtonAction() {
 // Test setting Reset button action listener
 dialog.resetButton.doClick();
 assertNotSame(dialog.initialFont, dialog.chooserPane.getFont());
 dialog.reset();
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }
}