package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FontChooserDialogTest {

 private JFrame parentFrame;
 private Component component;
 private ActionListener okListener;
 private ActionListener cancelListener;
 private FontChooserDialog dialog;

 @Before
 public void setup() {
 parentFrame = Mockito.mock(JFrame.class);
 component = Mockito.mock(Component.class);
 okListener = Mockito.mock(ActionListener.class);
 cancelListener = Mockito.mock(ActionListener.class);
 dialog = new FontChooserDialog(parentFrame, "Font Chooser", true, new JFontChooser(), okListener, cancelListener);
 }

 @Test
 public void testConstructorSetsUpDialogWithInitialFontAndListeners() {
 Font expectedInitialFont = new Font("Arial", Font.BOLD, 12);
 when(component.getFont()).thenReturn(expectedInitialFont);
 dialog.setVisible(true);
 assertEquals(expectedInitialFont, dialog.chooserPane.getFont());
 verify(okListener, times(1)).actionPerformed(any(ActionEvent.class));
 verify(cancelListener, times(1)).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testOKButtonClosesDialog() {
 dialog.okButton.doClick();
 verify(parentFrame, times(1)).setVisible(false);
 }

 @Test
 public void testCancelButtonClosesDialog() {
 dialog.cancelButton.doClick();
 verify(parentFrame, times(1)).setVisible(false);
 }

 @Test
 public void testResetButtonResetsFont() {
 dialog.resetButton.doClick();
 Font expectedFont = new Font("Arial", Font.PLAIN, 11);
 when(component.getFont()).thenReturn(expectedFont);
 dialog.setVisible(true);
 dialog.reset();
 assertEquals(expectedFont, dialog.chooserPane.getFont());
 }
}