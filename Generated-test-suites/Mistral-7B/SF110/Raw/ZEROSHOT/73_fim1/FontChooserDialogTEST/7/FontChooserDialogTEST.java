package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.swing.JFontChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private JFontChooser chooserPane;
 private JFrame frame;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Before
 public void setUp() {
 chooserPane = mock(JFontChooser.class);
 frame = mock(JFrame.class);
 okListener = mock(ActionListener.class);
 cancelListener = mock(ActionListener.class);
 }

 @Test
 public void testFontChooserDialogConstruction() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Title", true, chooserPane, okListener, cancelListener);
 assertEquals("Title", dialog.getTitle());
 assertFalse(dialog.isModal());
 assertSame(chooserPane, dialog.getChooserPane());
 assertNull(dialog.getInitialFont());
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 FontChooserDialog dialog = new FontChooserDialog(frame, "Title", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.setFont(initialFont);
 dialog.setVisible(false);
 dialog.reset();
 assertEquals(initialFont, dialog.getInitialFont());
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Title", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.reset();
 assertNull(dialog.getInitialFont());
 }

 @Test
 public void testOkButtonAction() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Title", true, chooserPane, okListener, cancelListener);
 dialog.pack();
 dialog.setVisible(true);
 verify(okListener).actionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testCancelButtonAction() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Title", true, chooserPane, okListener, cancelListener);
 dialog.pack();
 dialog.setVisible(true);
 verify(cancelListener).actionPerformed(mock(ActionEvent.class));
 }

 @Test
 public void testResetButtonAction() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Title", true, chooserPane, okListener, cancelListener);
 dialog.pack();
 dialog.setVisible(true);
 dialog.reset();
 assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
 }
}