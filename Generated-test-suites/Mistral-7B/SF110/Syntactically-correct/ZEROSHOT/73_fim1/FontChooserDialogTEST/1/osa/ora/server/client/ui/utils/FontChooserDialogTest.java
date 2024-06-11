package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private Component testComponent;
 private JFrame testFrame;

 @Before
 public void setup() {
 testComponent = new JButton("Test Button");
 testFrame = new JFrame("Test Frame");
 testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 }

 @After
 public void teardown() {
 testFrame.dispose();
 }

 @Test
 public void testFontChooserDialogCreation() {
 FontChooserDialog dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, null, null, null);
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertNull(dialog.getFont());

 JButton okButton = dialog.getRootPane().getDefaultButton();
 assertEquals("OK", okButton.getText());
 JButton cancelButton = dialog.getRootPane().getComponent(1);
 assertEquals("Cancel", cancelButton.getText());
 JButton resetButton = dialog.getRootPane().getComponent(2);
 assertEquals("Reset", resetButton.getText());

 assertNotNull(dialog.getContentPane());
 assertNotNull(dialog.chooserPane);
 }

 @Test
 public void testFontChooserDialogWithListeners() {
 ActionListener okListener = ae -> System.out.println("OK button clicked");
 ActionListener cancelListener = ae -> System.out.println("Cancel button clicked");

 FontChooserDialog dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.setVisible(false);

 assertEquals("OK button clicked", System.out.getHistory(0).toString());
 assertEquals("Cancel button clicked", System.out.getHistory(1).toString());
 }

 @Test
 public void testFontChooserDialogWithInitialFont() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 JFontChooser chooserPane = new JFontChooser();
 chooserPane.setFont(initialFont);

 FontChooserDialog dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.setVisible(false);

 assertEquals(initialFont, dialog.getInitialFont());
 }

 @Test
 public void testResetFont() {
 FontChooserDialog dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.reset();
 dialog.setVisible(false);

 assertEquals(dialog.getInitialFont(), dialog.getFont());
 }
}