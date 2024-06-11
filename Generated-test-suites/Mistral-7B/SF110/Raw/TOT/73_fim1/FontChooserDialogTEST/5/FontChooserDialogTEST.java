package osa.ora.server.client.ui.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FontChooserDialogTest {

 private JFrame parentFrame;
 private JComponent component;
 private JFontChooser chooserPane;
 private FontChooserDialog dialog;

 @Before
 public void setUp() {
 parentFrame = new JFrame();
 parentFrame.setVisible(true);
 component = new JComponent();
 chooserPane = new JFontChooser();
 }

 @Test
 public void testFontChooserDialog() {
 // Test constructor with valid arguments
 dialog = new FontChooserDialog(parentFrame, "Font Dialog", true, chooserPane, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 Assert.assertEquals("OK", e.getActionCommand());
 }
 }, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 Assert.assertEquals("Cancel", e.getActionCommand());
 }
 });
 Assert.assertEquals("Font Dialog", dialog.getTitle());
 Assert.assertNotNull(dialog.getOkButton());
 Assert.assertNotNull(dialog.getCancelButton());
 dialog.setVisible(true);
 dialog.dispose();

 // Test constructor with null okListener and cancelListener
 dialog = new FontChooserDialog(parentFrame, "Font Dialog", true, chooserPane, null, null);
 Assert.assertEquals("Font Dialog", dialog.getTitle());
 Assert.assertNotNull(dialog.getOkButton());
 Assert.assertNotNull(dialog.getCancelButton());
 dialog.setVisible(true);
 dialog.dispose();

 // Test setVisible method
 dialog = new FontChooserDialog(parentFrame, "Font Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 Assert.assertNotNull(dialog.getInitialFont());
 dialog.dispose();

 // Test reset method
 dialog = new FontChooserDialog(parentFrame, "Font Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.reset();
 Assert.assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
 dialog.dispose();

 // Test okButton actionPerformed
 dialog = new FontChooserDialog(parentFrame, "Font Dialog", true, chooserPane, e -> {
 Assert.assertEquals("OK", e.getActionCommand());
 }, null);
 dialog.setVisible(true);
 dialog.getOkButton().doClick();
 dialog.dispose();

 // Test cancelButton actionPerformed
 dialog = new FontChooserDialog(parentFrame, "Font Dialog", true, chooserPane, null, e -> {
 Assert.assertEquals("Cancel", e.getActionCommand());
 });
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 dialog.dispose();

 // Test okButton ActionEvent
 dialog = new FontChooserDialog(parentFrame, "Font Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 ActionEvent okEvent = new ActionEvent(dialog.getOkButton(), ActionEvent.ACTION_PERFORMED, "OK");
 dialog.getOkButton().dispatchEvent(okEvent);
 dialog.dispose();

 // Test cancelButton ActionEvent
 dialog = new FontChooserDialog(parentFrame, "Font Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 ActionEvent cancelEvent = new ActionEvent(dialog.getCancelButton(), ActionEvent.ACTION_PERFORMED, "Cancel");
 dialog.getCancelButton().dispatchEvent(cancelEvent);
 dialog.dispose();

 // Test okButton KeyStroke
 dialog = new FontChooserDialog(parentFrame, "Font Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.getRootPane().dispatchEvent(new KeyEvent(dialog.getOkButton(), KeyEvent.KEY_PRESSED, 0, 0, true, KeyEvent.VK_ENTER, ''));
 dialog.dispose();

 // Test cancelButton KeyStroke
 dialog = new FontChooserDialog(parentFrame, "Font Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.getRootPane().dispatchEvent(new KeyEvent(dialog.getCancelButton(), KeyEvent.KEY_PRESSED, 0, 0, true, KeyEvent.VK_ESCAPE, ''));
 dialog.dispose();
 }
}