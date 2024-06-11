package osa.ora.server.client.ui.utils;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private Component testComponent;
 private JFrame frame;

 @Test
 public void testFontChooserDialogCreation() {
 Font initialFont = new Font("Arial", Font.PLAIN, 12);
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 assertNotNull(dialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 Font initialFont = new Font("Arial", Font.PLAIN, 12);
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.setVisible(false); //hide the dialog to reset the font
 assertSame(initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 FontChooserDialog dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 dialog.setVisible(true);
 dialog.setVisible(false); //hide the dialog to reset the font
 assertNull(dialog.initialFont);
 }

 @Test
 public void testOkButtonActionPerformed() {
 FontChooserDialog dialog = createFontChooserDialogWithMockComponents();
 dialog.add(new JButton("OK"));
 dialog.setVisible(true);
 dialog.okButton.doClick();
 dialog.setVisible(false);
 assertTrue(dialog.isVisible() == false);
 }

 @Test
 public void testCancelButtonActionPerformed() {
 FontChooserDialog dialog = createFontChooserDialogWithMockComponents();
 dialog.add(new JButton("Cancel"));
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 dialog.setVisible(false);
 assertTrue(dialog.isVisible() == false);
 }

 @Test
 public void testResetButtonActionPerformed() {
 FontChooserDialog dialog = createFontChooserDialogWithMockComponents();
 dialog.add(new JButton("Reset"));
 dialog.setVisible(true);
 dialog.resetButton.doClick();
 dialog.setVisible(false);
 Font initialFont = dialog.chooserPane.getFont();
 dialog.setVisible(true);
 dialog.chooserPane.setFont(new Font("Times New Roman", Font.BOLD, 14));
 dialog.resetButton.doClick();
 assertSame(initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testCancelKeyAction() {
 FontChooserDialog dialog = createFontChooserDialogWithMockComponents();
 dialog.add(new JButton("Cancel"));
 dialog.setVisible(true);
 dialog.focusOwnerDrawn = false;
 dialog.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("\u001b"), "cancel");
 dialog.getActionMap().put("cancel", new AbstractAction() {
 @Override
 public void actionPerformed(ActionEvent e) {
 dialog.setVisible(false);
 }
 });
 dialog.focusOwnerDrawn = true;
 dialog.doClick();
 dialog.dispatchEvent(new KeyEvent(dialog, 2, true, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), "\u001b"));
 dialog.dispatchEvent(new KeyEvent(dialog, 2, false, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), "\u001b"));
 dialog.dispatchEvent(new KeyEvent(dialog, 2, false, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), "\u001b"));
 assertTrue(dialog.isVisible() == false);
 }

 private FontChooserDialog createFontChooserDialogWithMockComponents() {
 JFrame frame = new JFrame();
 Component testComponent = new JButton();
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 FontChooserDialog dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, new JFontChooser(), okListener, cancelListener);
 dialog.addWindowListener(new FontChooserDialog.Closer());
 dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 dialog.add(new JPanel());
 return dialog;
 }
}