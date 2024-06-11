package osa.ora.server.client.ui.utils;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;

public class FontChooserDialogTest {

 private JFrame parentFrame;
 private JComponent component;
 private JFontChooser chooserPane;
 private ActionListener okListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // handle OK button
 }
 };
 private ActionListener cancelListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // handle Cancel button
 }
 };

 @Test
 public void testFontChooserDialog_ValidInput() throws Exception {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 parentFrame = new JFrame();
 component = new JButton("Test Button");
 chooserPane = new JFontChooser();
 FontChooserDialog dialog = new FontChooserDialog(component, "Font Chooser", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 assertNotNull(dialog.getInitialFont());
 dialog.setVisible(false); // close dialog
 assertEquals(initialFont, chooserPane.getFont());
 }

 @Test
 public void testFontChooserDialog_OKButton() throws Exception {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 parentFrame = new JFrame();
 component = new JButton("Test Button");
 chooserPane = new JFontChooser();
 FontChooserDialog dialog = new FontChooserDialog(component, "Font Chooser", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.okButtonActionPerformed(); // simulate OK button press
 dialog.setVisible(false); // close dialog
 // assert that font has been changed in chooserPane
 Font newFont = chooserPane.getFont();
 // add assertions here based on the expected behavior of the new font
 dialog.reset();
 }

 @Test
 public void testFontChooserDialog_CancelButton() throws Exception {
 parentFrame = new JFrame();
 component = new JButton("Test Button");
 chooserPane = new JFontChooser();
 FontChooserDialog dialog = new FontChooserDialog(component, "Font Chooser", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.cancelButtonActionPerformed(); // simulate Cancel button press
 dialog.setVisible(false); // close dialog
 // no changes should have been made
 assertEquals(new Font("Arial", Font.BOLD, 12), chooserPane.getFont());
 }

 @Test
 public void testFontChooserDialog_ResetButton() throws Exception {
 parentFrame = new JFrame();
 component = new JButton("Test Button");
 chooserPane = new JFontChooser();
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 FontChooserDialog dialog = new FontChooserDialog(component, "Font Chooser", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.reset();
 assertEquals(initialFont, chooserPane.getFont());
 dialog.setVisible(false); // close dialog
 }
}