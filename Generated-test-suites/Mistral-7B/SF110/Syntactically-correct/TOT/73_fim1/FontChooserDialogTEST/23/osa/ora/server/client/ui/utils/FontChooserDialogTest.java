package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private Component parentComponent;
 private ActionListener okListener;
 private ActionListener cancelListener;
 private JFontChooser chooserPane;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 parentComponent = mock(Component.class);
 okListener = mock(ActionListener.class);
 cancelListener = mock(ActionListener.class);
 chooserPane = mock(JFontChooser.class);

 dialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 }

 @Test
 public void testFontChooserDialogCreation() {
 assertNotNull(dialog);
 assertEquals("Test Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertNotNull(dialog.getContentPane());
 }

 @Test
 public void testSetVisible() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 dialog.setVisible(true);
 verify(chooserPane).setFont(initialFont);
 dialog.setVisible(false);
 verify(dialog).dispose();
 }

 @Test
 public void testOkButtonAction() {
 dialog.setVisible(true);
 dialog.okButton.doClick();
 verify(okListener).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testCancelButtonAction() {
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 verify(cancelListener).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testResetButtonAction() {
 dialog.setVisible(true);
 dialog.resetButton.doClick();
 verify(chooserPane).setFont(initialFont);
 }

 @Test
 public void testEscKeyAction() {
 dialog.setVisible(true);
 dialog.dispatchEvent(new KeyEvent(dialog, KeyEvent.KEY_PRESSED, 0, '\u001b'));
 verify(dialog).dispose();
 }
}