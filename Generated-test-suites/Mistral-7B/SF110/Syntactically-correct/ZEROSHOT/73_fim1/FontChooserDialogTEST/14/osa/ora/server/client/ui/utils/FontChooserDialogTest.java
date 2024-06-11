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
 private ActionEvent actionEvent;
 private ActionListener okListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // TODO add assertions or actions here
 }
 };
 private ActionListener cancelListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // TODO add assertions or actions here
 }
 };

 @Before
 public void setUp() {
 JFrame frame = new JFrame();
 dialog = new FontChooserDialog(frame, "FontChooserDialogTest", true, null, okListener, cancelListener);
 actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "OK");
 }

 @After
 public void tearDown() {
 dialog.dispose();
 }

 @Test
 public void testFontChooserDialogConstruction() {
 assertNotNull(dialog);
 assertEquals("FontChooserDialogTest", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertNull(dialog.getFont());
 }

 @Test
 public void testSetFont() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 JFontChooser chooserPane = new JFontChooser();
 chooserPane.setFont(initialFont);
 dialog = new FontChooserDialog(new JFrame(), "FontChooserDialogTest", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.setFont(new Font("Verdana", Font.ITALIC, 14));
 assertEquals("Verdana", dialog.getFont().getName());
 assertFalse(dialog.isFontBold());
 assertTrue(dialog.isFontItalic());
 dialog.reset();
 assertEquals(initialFont, dialog.getFont());
 }

 @Test
 public void testOkButtonActionPerformed() {
 dialog.setVisible(true);
 dialog.okButtonActionPerformed(actionEvent);
 assertTrue(dialog.isVisible());
 dialog.dispose();
 }

 @Test
 public void testCancelButtonActionPerformed() {
 dialog.setVisible(true);
 dialog.cancelButtonActionPerformed(actionEvent);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testResetButtonActionPerformed() {
 dialog.setVisible(true);
 dialog.resetButtonActionPerformed(actionEvent);
 assertEquals(dialog.initialFont, dialog.getFont());
 dialog.dispose();
 }
}