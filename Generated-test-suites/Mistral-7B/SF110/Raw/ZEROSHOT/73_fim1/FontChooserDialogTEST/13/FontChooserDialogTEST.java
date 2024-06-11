package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private Component parentComponent;
 private ActionListener okListener;
 private ActionListener cancelListener;
 private JFontChooser chooserPane;
 private JButton okButton;
 private JButton cancelButton;
 private JButton resetButton;

 @Before
 public void setUp() {
 parentComponent = mock(Component.class);
 okListener = mock(ActionListener.class);
 cancelListener = mock(ActionListener.class);
 chooserPane = mock(JFontChooser.class);
 dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 okButton = dialog.getRootPane().findComponent("OK");
 cancelButton = dialog.getRootPane().findComponent("Cancel");
 resetButton = dialog.getRootPane().findComponent("Reset");
 }

 @Test
 public void testFontChooserDialogConstruction() {
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertNull(dialog.getFont());
 }

 @Test
 public void testSetVisibleInitialFont() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertNotNull(dialog.initialFont);
 assertEquals(initialFont, dialog.initialFont);
 }

 @Test
 public void testSetVisibleNoInitialFont() {
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertNull(dialog.initialFont);
 }

 @Test
 public void testReset() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 dialog.setFont(initialFont);
 dialog.reset();
 assertNull(dialog.getFont());
 }

 @Test
 public void testOkButtonActionPerformed() {
 verify(okListener, atLeastOnce()).actionPerformed(mock(ActionEvent.class));
 verify(dialog, times(1)).setVisible(false);
 okButton.doClick();
 }

 @Test
 public void testCancelButtonActionPerformed() {
 verify(cancelListener, atLeastOnce()).actionPerformed(mock(ActionEvent.class));
 verify(dialog, times(1)).setVisible(false);
 cancelButton.doClick();
 }

 @Test
 public void testResetButtonActionPerformed() {
 verify(dialog, times(1)).reset();
 resetButton.doClick();
 }
}