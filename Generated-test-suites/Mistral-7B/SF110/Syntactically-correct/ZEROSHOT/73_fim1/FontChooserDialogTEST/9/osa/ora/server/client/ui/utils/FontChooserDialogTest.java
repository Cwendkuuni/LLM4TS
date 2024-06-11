package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private Component parentFrame;
 private JFontChooser chooserPane;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Before
 public void setUp() {
 parentFrame = new JFrame();
 chooserPane = new JFontChooser();
 okListener = e -> {};
 cancelListener = e -> {};
 dialog = new FontChooserDialog(parentFrame, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 }

 @Test
 public void testConstructor() {
 // Check if the dialog is initialized correctly
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertNotNull(dialog.getOkButton());
 assertNotNull(dialog.getCancelButton());
 assertNotNull(dialog.getResetButton());
 // Check if the chooserPane is added to the dialog
 assertTrue(dialog.getContentPane().getComponentCount() > 0);
 assertEquals(dialog.getContentPane().getComponent(0), dialog.chooserPane);
 }

 @Test
 public void testSetVisible() {
 // Test setting the dialog to visible and checking the initialFont
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 dialog.setVisible(false);
 // Test setting the dialog to visible again and checking the reset method
 dialog.setVisible(true);
 dialog.reset();
 assertNotNull(dialog.initialFont);
 assertNotSame(dialog.chooserPane.getFont(), dialog.initialFont);
 dialog.setVisible(false);
 }

 @Test
 public void testReset() {
 // Test resetting the dialog to the initialFont
 dialog.setVisible(true);
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertSame(dialog.chooserPane.getFont(), dialog.initialFont);
 dialog.setVisible(false);
 }

 @Test
 public void testOkButtonActionPerformed() {
 // Test that the ok button hides the dialog when clicked
 dialog.setVisible(true);
 dialog.getOkButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testCancelButtonActionPerformed() {
 // Test that the cancel button hides the dialog when clicked
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testOkButtonDefaultButton() {
 // Test that the ok button is the default button
 dialog.setVisible(true);
 FocusTraversalPolicy focusTraversalPolicy = dialog.getRootPane().getFocusTraversalPolicy();
 assertEquals(dialog.getOkButton(), focusTraversalPolicy.getComponent(FocusTraversalPolicy.FIRST_COMPONENT));
 }

 @After
 public void tearDown() {
 parentFrame.dispose();
 }

}