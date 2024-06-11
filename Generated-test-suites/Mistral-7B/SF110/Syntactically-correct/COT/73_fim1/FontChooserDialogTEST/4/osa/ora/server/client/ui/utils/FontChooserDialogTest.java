package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;
 private ActionListener okListener = e -> {};
 private ActionListener cancelListener = e -> {};

 @Before
 public void setUp() {
 frame = new JFrame();
 dialog = new FontChooserDialog(frame, "FontChooserDialog", true, new JFontChooser(), okListener, cancelListener);
 }

 // Test constructor
 @Test
 public void testFontChooserDialogConstructor() {
 assertNotNull(dialog);
 }

 // Test setVisible method
 @Test
 public void testSetVisible() {
 Font initialFont = dialog.chooserPane.getFont();
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertSame(initialFont, dialog.chooserPane.getFont());
 }

 // Test reset method
 @Test
 public void testReset() {
 dialog.chooserPane.setFont(new Font("Serif", Font.PLAIN, 12));
 dialog.reset();
 assertSame(dialog.initialFont, dialog.chooserPane.getFont());
 }

 // Test ok button action
 @Test
 public void testOkButtonAction() {
 dialog.okButton.doClick();
 assertFalse(dialog.isVisible());
 }

 // Test cancel button action
 @Test
 public void testCancelButtonAction() {
 dialog.cancelButton.doClick();
 assertFalse(dialog.isVisible());
 }

 // Test reset button action
 @Test
 public void testResetButtonAction() {
 dialog.resetButton.doClick();
 assertSame(dialog.initialFont, dialog.chooserPane.getFont());
 }

 // Test window closing
 @Test
 public void testWindowClosing() {
 dialog.addWindowListener(new FontChooserDialog.Closer());
 dialog.setVisible(true);
 dialog.windowClosing(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
 assertFalse(dialog.isVisible());
 }

 // Test component hidden
 @Test
 public void testComponentHidden() {
 dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 dialog.setVisible(true);
 dialog.dispose();
 assertEquals(0, Frame.getFrames().length);
 }

 // Edge case: null component
 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullComponent() {
 dialog = new FontChooserDialog(null, "FontChooserDialog", true, new JFontChooser(), okListener, cancelListener);
 }

 // Edge case: null title
 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullTitle() {
 dialog = new FontChooserDialog(frame, null, true, new JFontChooser(), okListener, cancelListener);
 }

 // Edge case: null ok listener
 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullOkListener() {
 dialog = new FontChooserDialog(frame, "FontChooserDialog", true, new JFontChooser(), null, cancelListener);
 }

 // Edge case: null cancel listener
 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullCancelListener() {
 dialog = new FontChooserDialog(frame, "FontChooserDialog", true, new JFontChooser(), okListener, null);
 }

 // Exception handling: IllegalArgumentException for null font chooser pane
 @Test(expected = IllegalArgumentException.class)
 public void testFontChooserDialogConstructorNullFontChooserPane() {
 dialog = new FontChooserDialog(frame, "FontChooserDialog", true, null, okListener, cancelListener);
 }
}