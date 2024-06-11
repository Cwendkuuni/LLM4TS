package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private Component parentComponent;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Before
 public void setUp() {
 JFontChooser chooserPane = mock(JFontChooser.class);
 parentComponent = new JFrame();
 okListener = mock(ActionListener.class);
 cancelListener = mock(ActionListener.class);
 dialog = new FontChooserDialog(parentComponent, "Font Dialog", true, chooserPane, okListener, cancelListener);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 dialog.setVisible(true);
 verify(chooserPane, times(1)).setFont(initialFont);
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 dialog.setVisible(true);
 verify(chooserPane, never()).setFont(any(Font.class));
 }

 @Test
 public void testCancelButton() {
 dialog.setVisible(true);
 dialog.cancelButtonClicked();
 verify(okListener, never()).actionPerformed(any(ActionEvent.class));
 verify(cancelListener, times(1)).actionPerformed(any(ActionEvent.class));
 verify(dialog, times(1)).dispose();
 }

 @Test
 public void testOKButton() {
 dialog.setVisible(true);
 Font chosenFont = new Font("Times New Roman", Font.ITALIC, 14);
 dialog.okButtonClicked(new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "OK"));
 verify(chooserPane, times(1)).getFont();
 verify(okListener, times(1)).actionPerformed(any(ActionEvent.class));
 verify(dialog, times(1)).dispose();
 }

 @Test
 public void testSetFontOnChooserPane() {
 Font newFont = new Font("Serif", Font.BOLD, 16);
 dialog.chooserPane.setFont(newFont);
 dialog.setVisible(true);
 verify(dialog.chooserPane, times(1)).getFont();
 assertEquals(newFont, dialog.initialFont);
 }

 @Test
 public void testResetButton() {
 dialog.setVisible(true);
 dialog.resetButtonClicked();
 verify(chooserPane, times(1)).setFont(dialog.initialFont);
 }
}