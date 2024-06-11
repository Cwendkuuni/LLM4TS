package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;
 private JFontChooser chooserPane;

 @Before
 public void setUp() {
 frame = new JFrame();
 chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, null, null);
 }

 @Test
 public void testConstructor() {
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertNotNull(dialog.getContentPane());
 assertNotNull(dialog.getButtonPane());
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testSetVisible() {
 Font initialFont = null;
 dialog.setVisible(true);
 dialog.setVisible(false);
 dialog.setVisible(true);
 initialFont = dialog.chooserPane.getFont();
 dialog.setVisible(false);
 dialog.reset();
 assertNotNull(initialFont);
 assertEquals(initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testReset() {
 dialog.setVisible(true);
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertNotNull(dialog.initialFont);
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testOKButtonActionPerformed() {
 dialog.setVisible(true);
 dialog.getOkButton().doClick();
 assertTrue(dialog.isVisible());
 }

 @Test
 public void testCancelButtonActionPerformed() {
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testSetDefaultButton() {
 dialog.setVisible(true);
 assertSame(dialog.getOkButton(), dialog.getRootPane().getDefaultButton());
 }

 @Test(expected = NullPointerException.class)
 public void testNullComponent() {
 new FontChooserDialog(null, "Font Chooser Dialog", true, chooserPane, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 new FontChooserDialog(frame, null, true, chooserPane, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullChooserPane() {
 new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullOKListener() {
 ActionListener okListener = null;
 new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, null, okListener);
 }

 @Test(expected = NullPointerException.class)
 public void testNullCancelListener() {
 ActionListener cancelListener = null;
 new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, null, cancelListener);
 }

}