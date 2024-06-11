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
 private Component parentComponent;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Before
 public void setUp() {
 parentComponent = new JFrame();
 okListener = e -> {};
 cancelListener = e -> {};
 dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), okListener, cancelListener);
 }

 @After
 public void tearDown() {
 if (dialog != null) {
 dialog.dispose();
 }
 }

 @Test
 public void testFontChooserDialogConstruction() {
 // Test that the FontChooserDialog is constructed correctly
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertNotNull(dialog.getOkButton());
 assertNotNull(dialog.getCancelButton());
 assertNotNull(dialog.getResetButton());
 assertNotNull(dialog.getChooserPane());
 assertNull(dialog.getInitialFont());
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 // Test that the initialFont is set correctly when the dialog is made visible
 dialog.setVisible(true);
 assertNotNull(dialog.getInitialFont());
 dialog.setVisible(false);
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 // Test that the initialFont is not set when the dialog is made visible without an initial font
 dialog.setVisible(true);
 assertNull(dialog.getInitialFont());
 dialog.setVisible(false);
 }

 @Test
 public void testReset() {
 // Test that the font is reset to the initial font when the reset button is pressed
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertSame(dialog.getInitialFont(), dialog.chooserPane.getFont());
 }

 @Test
 public void testOkButtonAction() {
 // Test that the ok button dismisses the dialog
 dialog.addWindowListener(new FontChooserDialog.Closer());
 dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 dialog.getOkButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testCancelButtonAction() {
 // Test that the cancel button dismisses the dialog
 dialog.addWindowListener(new FontChooserDialog.Closer());
 dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 dialog.getCancelButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testInitialFontSetOnOkButtonPress() {
 // Test that the initialFont is set to the selected font when the ok button is pressed
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 dialog.chooserPane.setFont(initialFont);
 dialog.getOkButton().doClick();
 assertEquals(initialFont, dialog.getInitialFont());
 }
}