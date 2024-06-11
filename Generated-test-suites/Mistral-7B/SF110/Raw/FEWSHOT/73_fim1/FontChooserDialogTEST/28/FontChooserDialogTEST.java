package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 FontChooserDialog dialog;
 JFontChooser chooserPane;

 @Before
 public void setUp() {
 chooserPane = mock(JFontChooser.class);
 dialog = new FontChooserDialog(null, "Font Dialog", true, chooserPane, null, null);
 }

 @Test
 public void testSetVisibleTrueInitializesFont() {
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 }

 @Test
 public void testSetVisibleFalseDoesNotChangeFont() {
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertSame(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testResetSetsFontToInitialFont() {
 dialog.setVisible(true);
 dialog.setVisible(false);
 dialog.reset();
 assertSame(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testOkListenerClosesDialog() {
 final ActionListener okListener = mock(ActionListener.class);
 dialog.setVisible(true);
 dialog.okButton.doClick();
 verify(okListener).actionPerformed(dialog.okButton);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testCancelListenerClosesDialog() {
 final ActionListener cancelListener = mock(ActionListener.class);
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 verify(cancelListener).actionPerformed(dialog.cancelButton);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testOkButtonDefaultActionPerformsReset() {
 dialog.setVisible(true);
 dialog.okButton.doClick();
 dialog.reset();
 assertSame(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testCancelButtonDefaultActionDoesNothing() {
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 assertSame(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testCancelKeyStrokeClosesDialog() {
 dialog.setVisible(true);
 dialog.cancelKeyAction.actionPerformed(null);
 assertFalse(dialog.isVisible());
 }
}