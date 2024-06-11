package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest
{
 private FontChooserDialog dialog;
 private JFrame frame;

 @Before
 public void setUp()
 {
 frame = new JFrame();
 JButton button = new JButton("Show Dialog");
 button.addActionListener(e -> {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, new JFontChooser(), null, null, null);
 dialog.setVisible(true);
 });
 frame.getContentPane().add(button, 10, 10);
 frame.setSize(300, 300);
 frame.setVisible(true);
 }

 @Test
 public void testFontChooserDialogCreation()
 {
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertNotNull(dialog.getChooserPane());
 }

 @Test
 public void testSetVisibleWithInitialFont()
 {
 Font initialFont = new Font("Arial", Font.PLAIN, 12);
 dialog.setVisible(true);
 assertEquals(initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testSetVisibleWithoutInitialFont()
 {
 dialog.setVisible(true);
 assertNull(dialog.initialFont);
 }

 @Test
 public void testOkButtonAction()
 {
 JFontChooser chooserPane = dialog.chooserPane;
 Font oldFont = chooserPane.getFont();
 dialog.okButton.doClick();
 assertNotSame(oldFont, chooserPane.getFont());
 }

 @Test
 public void testCancelButtonAction()
 {
 dialog.cancelButton.doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testResetButtonAction()
 {
 dialog.resetButton.doClick();
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testWindowClosing()
 {
 dialog.addWindowListener(new FontChooserDialog.Closer());
 dialog.setVisible(true);
 dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testComponentHidden()
 {
 dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 dialog.setVisible(true);
 dialog.dispatchEvent(new ComponentEvent(dialog, ComponentEvent.COMPONENT_HIDDEN));
 assertTrue(dialog.isDisposed());
 }
}