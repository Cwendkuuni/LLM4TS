package osa.ora.server.client.ui.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private JFrame frame;
 private Component parentComponent;
 private FontChooserDialog fontChooserDialog;

 @Before
 public void setUp() {
 parentComponent = new JFrame();
 frame = new JFrame();
 }

 @Test
 public void testFontChooserDialogCreation() {
 // Test creating a FontChooserDialog with valid arguments
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog fontDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 assertNotNull(fontDialog);
 }

 @Test
 public void testSetVisible() {
 // Test setting the dialog to be visible and getting the initial font
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog fontDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontDialog.setVisible(true);
 assertEquals(initialFont, fontDialog.chooserPane.getFont());
 }

 @Test
 public void testReset() {
 // Test resetting the dialog to its initial font
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog fontDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontDialog.setVisible(true);
 fontDialog.reset();
 assertEquals(initialFont, fontDialog.chooserPane.getFont());
 }

 @Test
 public void testOkButtonAction() {
 // Test the OK button action
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog fontDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontDialog.setVisible(true);
 fontDialog.okButton.doClick();
 assertFalse(fontDialog.isVisible());
 }

 @Test
 public void testCancelButtonAction() {
 // Test the Cancel button action
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog fontDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontDialog.setVisible(true);
 fontDialog.cancelButton.doClick();
 assertFalse(fontDialog.isVisible());
 }

 @Test
 public void testInitialFontWithNullValue() {
 // Test creating a FontChooserDialog with a null initial font
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog fontDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 assertEquals(null, fontDialog.initialFont);
 }

 @Test
 public void testInitialFontWithEmptyString() {
 // Test creating a FontChooserDialog with an empty string initial font
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog fontDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 assertEquals("", fontDialog.initialFont.getFamily());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialFontWithInvalidFont() {
 // Test creating a FontChooserDialog with an invalid font
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 Font initialFont = new Font("Invalid Font", Font.BOLD, 12);
 FontChooserDialog fontDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontDialog.initialFont = initialFont;
 fontDialog.setVisible(true);
 }

 @After
 public void tearDown() {
 parentComponent.dispose();
 frame.dispose();
 }
}