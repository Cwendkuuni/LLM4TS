package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import javax.swing.*;
import org.junit.Test;

public class FontChooserDialogTest {

 FontChooserDialog dialog;
 JFrame frame;

 @Test
 public void testFontChooserDialog() {
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 assertNotNull(dialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 }

 @Test
 public void testResetFont() {
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.reset();
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(false);
 assertNull(dialog.initialFont);
 }

 @Test
 public void testActionPerformedOkListener() {
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.show();
 dialog.okButton.doClick();
 dialog.setVisible(false);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testActionPerformedCancelListener() {
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.show();
 dialog.cancelButton.doClick();
 dialog.setVisible(false);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testComponentHiddenDisposeListener() throws Exception {
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 frame = new JFrame();
 frame.add(dialog);
 dialog.show();
 dialog.addWindowListener(new FontChooserDialog.Closer());
 dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 dialog.cancelButton.doClick();
 assertTrue(frame.isDisposed());
 }

}