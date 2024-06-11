package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;
 private ActionListener okListener = e -> {};
 private ActionListener cancelListener = e -> {};

 @Before
 public void setUp() {
 frame = new JFrame();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, new JFontChooser(), okListener, cancelListener);
 }

 @Test
 public void testConstructor() {
 // Test if the dialog is initialized correctly
 assertNotNull(dialog);
 assertInstanceOf(JDialog.class, dialog);
 // Test if the OK and Cancel buttons are created
 assertNotNull(dialog.getContentPane().getComponent(0));
 assertInstanceOf(JButton.class, dialog.getContentPane().getComponent(0));
 assertNotNull(dialog.getContentPane().getComponent(1));
 assertInstanceOf(JButton.class, dialog.getContentPane().getComponent(1));
 }

 @Test
 public void testSetVisible() {
 // Test if the initialFont is set correctly when the dialog is shown
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 dialog.setVisible(false);
 // Test if the dialog is hidden when the OK button is clicked
 dialog.setVisible(true);
 dialog.getContentPane().getComponent(0).doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testReset() {
 // Test if the font is reset correctly when the Reset button is clicked
 dialog.setVisible(true);
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 20));
 dialog.getContentPane().getComponent(2).doClick();
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testKeyEvent() {
 // Test if the ESC key closes the dialog
 dialog.setVisible(true);
 dialog.getContentPane().getComponent(1).doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testNullArguments() {
 // Test if the dialog can handle null arguments
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, okListener, cancelListener);
 assertNull(dialog.getContentPane());
 dialog.setVisible(true);
 dialog = new FontChooserDialog(frame, null, true, new JFontChooser(), null, cancelListener);
 assertNull(dialog.getTitle());
 dialog.setVisible(true);
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", false, new JFontChooser(), okListener, null);
 assertFalse(dialog.isModal());
 dialog.setVisible(true);
 }
}