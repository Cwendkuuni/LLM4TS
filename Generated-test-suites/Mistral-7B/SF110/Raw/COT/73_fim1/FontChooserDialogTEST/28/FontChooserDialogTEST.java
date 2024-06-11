package osa.ora.server.client.ui.utils.test;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import osa.ora.server.client.ui.utils.FontChooserDialog;

public class FontChooserDialogTest {

 private JFrame frame;
 private FontChooserDialog fontChooserDialog;

 @Before
 public void setUp() {
 JVMArguments.currentVM().add(-Djava.awt.headless="false");
 frame = new JFrame();
 fontChooserDialog = new FontChooserDialog(frame, "FontChooserDialog", true, null, null, null);
 fontChooserDialog.setVisible(true);
 }

 @After
 public void tearDown() {
 if (fontChooserDialog != null) {
 fontChooserDialog.dispose();
 }
 if (frame != null) {
 frame.dispose();
 }
 }

 @Test
 public void testFontChooserDialogConstructor() {
 // Check if the FontChooserDialog is initialized correctly
 assertEquals("FontChooserDialog title is not set correctly", "FontChooserDialog", fontChooserDialog.getTitle());
 assertTrue("FontChooserDialog is modal", fontChooserDialog.isModal());
 assertNotNull("chooserPane is null", fontChooserDialog.chooserPane);
 assertNull("okListener is not null", fontChooserDialog.getOkListener());
 assertNull("cancelListener is not null", fontChooserDialog.getCancelListener());
 }

 @Test
 public void testSetVisible() {
 // Check if the initialFont is set correctly when the dialog is shown
 fontChooserDialog.setVisible(true);
 assertTrue("Initial font is not set correctly", fontChooserDialog.initialFont != null);

 // Check if the dialog hides when the OK button is clicked
 fontChooserDialog.getOkButton().doClick();
 assertFalse("Dialog is still visible", fontChooserDialog.isVisible());
 }

 @Test
 public void testReset() {
 // Check if the font is reset correctly
 fontChooserDialog.reset();
 assertEquals("Font is not reset to initial font", fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
 }

 @Test(expected = NullPointerException.class)
 public void testNullComponentInConstructor() {
 fontChooserDialog = new FontChooserDialog(null, "FontChooserDialog", true, null, null, null);
 fail("Should throw NullPointerException for null Component");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTitleInConstructor() {
 fontChooserDialog = new FontChooserDialog(frame, "", true, null, null, null);
 fail("Should throw IllegalArgumentException for empty title");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullOkListenerInConstructor() {
 fontChooserDialog = new FontChooserDialog(frame, "FontChooserDialog", true, null, null, null);
 fontChooserDialog.getOkButton().doClick();
 fail("Should throw IllegalArgumentException for null okListener when OK button is clicked");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCancelListenerInConstructor() {
 fontChooserDialog = new FontChooserDialog(frame, "FontChooserDialog", true, null, null, null);
 fontChooserDialog.getCancelButton().doClick();
 fail("Should throw IllegalArgumentException for null cancelListener when Cancel button is clicked");
 }

 @Test
 public void testEscKeyPress() {
 Robot robot = new Robot();
 fontChooserDialog.setVisible(true);
 robot.keyPress(KeyEvent.VK_ESCAPE);
 assertFalse("Dialog is still visible", fontChooserDialog.isVisible());
 }

 @Test
 public void testOkButtonActionPerformed() {
 fontChooserDialog.getOkButton().doClick();
 assertTrue("Dialog is still visible", fontChooserDialog.isVisible());
 }

 @Test
 public void testCancelButtonActionPerformed() {
 fontChooserDialog.getCancelButton().doClick();
 assertFalse("Dialog is still visible", fontChooserDialog.isVisible());
 }

 @Test
 public void testResetButtonActionPerformed() {
 fontChooserDialog.reset();
 assertNotNull("The initialFont is null", fontChooserDialog.initialFont);
 fontChooserDialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 fontChooserDialog.reset();
 assertEquals("The reset font is not the initial font", fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
 }
}