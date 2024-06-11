package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import org.junit.*;

public class FontChooserDialogTest {

 @BeforeClass
 static public void setup() {
 System.out.println("###Test Case: FontChooserDialogTest.java ###");
 }

 @AfterClass
 static public void teardown() {
 // System.out.println("###Test Case: FontChooserDialogTest.java ended. ###");
 }

 @Test
 public void testFontChooserDialog() {
 // Create a new instance of the FontChooserDialog class
 JFrame frame = new JFrame();
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog dialog = new FontChooserDialog(frame, "Font Dialog", true, chooserPane, okListener, cancelListener);

 // Test if the dialog is displayed correctly
 assertTrue(dialog.isVisible());
 assertEquals("Font Dialog", dialog.getTitle());

 // Test the OK button functionality
 dialog.getOkButton().doClick();
 assertFalse(dialog.isVisible());

 // Test the Cancel button functionality
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 assertFalse(dialog.isVisible());

 // Test the reset button functionality
 dialog.setVisible(true);
 dialog.getResetButton().doClick();
 assertNotEquals(dialog.getInitialFont(), dialog.getChooserPane().getFont());
 dialog.reset();
 assertEquals(dialog.getInitialFont(), dialog.getChooserPane().getFont());
 }

 @Test
 public void testSetVisible() {
 // Create a new instance of the FontChooserDialog class
 JFrame frame = new JFrame();
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog dialog = new FontChooserDialog(frame, "Font Dialog", true, chooserPane, okListener, cancelListener);

 // Test setting the dialog to be visible
 dialog.setVisible(true);
 assertTrue(dialog.isVisible());
 Font initialFont = dialog.getChooserPane().getFont();

 // Test setting the dialog to be invisible and getting the initial font back
 dialog.setVisible(false);
 assertFalse(dialog.isVisible());
 assertEquals(initialFont, dialog.getChooserPane().getFont());
 }
}