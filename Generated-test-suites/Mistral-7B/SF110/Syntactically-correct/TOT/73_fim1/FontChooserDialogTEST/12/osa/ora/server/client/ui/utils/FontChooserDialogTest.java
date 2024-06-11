package osa.ora.server.client.ui.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Before
 public void setUp() {
 JFrame frame = new JFrame();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, new JFontChooser(), okListener, cancelListener);
 }

 @Test
 public void testConstructor() {
 // Test that the constructor initializes the dialog with the correct title and modal flag
 Assert.assertEquals("Font Chooser Dialog", dialog.getTitle());
 Assert.assertTrue(dialog.isModal());

 // Test that the okay and cancel buttons are initialized with the correct labels
 JButton okButton = (JButton)dialog.getContentPane().getComponent(0);
 Assert.assertEquals(UIManager.getString("ColorChooser.okText"), okButton.getText());

 JButton cancelButton = (JButton)dialog.getContentPane().getComponent(1);
 Assert.assertEquals(UIManager.getString("ColorChooser.cancelText"), cancelButton.getText());
 }

 @Test
 public void testSetVisible() {
 // Test that the initial font is null when the dialog is first shown
 Assert.assertNull(dialog.chooserPane.getFont());
 dialog.setVisible(true);
 // Test that the initial font is saved when the dialog is hidden
 dialog.setVisible(false);
 Assert.assertNotNull(dialog.initialFont);
 }

 @Test
 public void testReset() {
 // Test that the dialog's font is reset to the initial font when the reset button is clicked
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 14));
 dialog.reset();
 Assert.assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testOkButton() {
 okListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Test that the okay button sets the dialog to be hidden when clicked
 dialog.setVisible(false);
 }
 };
 dialog.okButton.addActionListener(okListener);
 dialog.setVisible(true);
 // Test that the okay button sets the font chooser pane's font to the user's selection when clicked
 dialog.chooserPane.setFont(new Font("Calibri", Font.BOLD, 16));
 dialog.okButton.doClick();
 Assert.assertEquals(new Font("Calibri", Font.BOLD, 16), dialog.chooserPane.getFont());
 }

 @Test
 public void testCancelButton() {
 cancelListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Test that the cancel button sets the dialog to be hidden when clicked
 dialog.setVisible(false);
 }
 };
 dialog.cancelButton.addActionListener(cancelListener);
 dialog.setVisible(true);
 // Test that the cancel button does not change the font chooser pane's font when clicked
 dialog.cancelButton.doClick();
 Assert.assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testEscKey() {
 cancelListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 }
 };
 dialog.cancelButton.addActionListener(cancelListener);
 dialog.setVisible(true);
 dialog.getContentPane().requestFocus();
 dialog.getContentPane().dispatchEvent(new KeyEvent(dialog.getContentPane(), KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ESCAPE, 0));
 Assert.assertFalse(dialog.isVisible());
 }

 @After
 public void tearDown() {
 if (dialog != null) {
 dialog.dispose();
 }
 }
}