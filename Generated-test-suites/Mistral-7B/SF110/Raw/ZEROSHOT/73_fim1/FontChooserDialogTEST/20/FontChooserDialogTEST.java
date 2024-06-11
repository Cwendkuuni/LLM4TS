package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private Component c;
 private JFrame frame;
 private JFontChooser chooserPane;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Before
 public void setUp() {
 c = mock(Component.class);
 frame = mock(JFrame.class);
 JOptionPane.setRootFrame(frame);
 chooserPane = mock(JFontChooser.class);
 okListener = mock(ActionListener.class);
 cancelListener = mock(ActionListener.class);
 dialog = new FontChooserDialog(c, "Test Title", true, chooserPane, okListener, cancelListener);
 MockitoAnnotations.initMocks(this);
 }

 @After
 public void tearDown() {
 dialog.dispose();
 }

 @Test
 public void testFontChooserDialogConstructor() {
 assertNotNull(dialog);
 assertSame(c, dialog.getParent());
 assertFalse(dialog.isFocusTraversalKeysEnabled());
 }

 @Test
 public void testSetVisible() {
 dialog.setVisible(true);
 assertSame(chooserPane.getFont(), dialog.initialFont);
 dialog.setVisible(false);
 }

 @Test
 public void testReset() {
 dialog.reset();
 assertSame(chooserPane.getFont(), initialFont);
 }

 @Test
 public void testOkButtonAction() {
 dialog.okButtonActionPerformed(mock(ActionEvent.class));
 verify(okListener).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testCancelButtonAction() {
 dialog.cancelButtonActionPerformed(mock(ActionEvent.class));
 verify(cancelListener).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testOkButtonActionWhenNoListener() {
 dialog.okButtonActionPerformed(mock(ActionEvent.class));
 verify(dialog).setVisible(false);
 }

 @Test
 public void testCancelKeyStroke() {
 final KeyEvent keyEvent = mock(KeyEvent.class);
 when(cancelButton.getInputMap(2).get(cancelKeyStroke)).thenReturn("cancel");
 dialog.cancelButtonActionPerformed(keyEvent);
 verify(cancelListener).actionPerformed(any(ActionEvent.class));
 }

 @Test(expected = NullPointerException.class)
 public void testNullComponent() {
 new FontChooserDialog(null, "Test Title", true, chooserPane, okListener, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 new FontChooserDialog(c, null, true, chooserPane, okListener, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testNullChooserPane() {
 new FontChooserDialog(c, "Test Title", true, null, okListener, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testNullOkListener() {
 new FontChooserDialog(c, "Test Title", true, chooserPane, null, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testNullCancelListener() {
 new FontChooserDialog(c, "Test Title", true, chooserPane, okListener, null);
 }
}