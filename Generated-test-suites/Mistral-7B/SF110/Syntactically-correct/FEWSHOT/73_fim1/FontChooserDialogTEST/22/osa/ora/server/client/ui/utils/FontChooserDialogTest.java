package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import javax.swing.*;
import org.junit.Test;

public class FontChooserDialogTest {

 @Test
 public void testFontChooserDialogConstruction() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 assertNotNull(dialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(false);
 assertNull(dialog.initialFont);
 }

 @Test
 public void testReset() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.reset();
 assertNotNull(dialog.chooserPane);
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testActionPerformedWithOkButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, action -> {}, action -> {});
 dialog.setVisible(true);
 verify(dialog).setVisible(false);
 }

 @Test
 public void testActionPerformedWithCancelButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, action -> {}, action -> {});
 dialog.setVisible(true);
 verify(dialog).setVisible(false);
 }

 @Test
 public void testActionPerformedWithResetButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertNotNull(dialog.chooserPane);
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testJButtonMnemonic() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton resetButton = dialog.getContentPane().getComponents()[2];
 int mnemonic = UIManager.getInt("ColorChooser.resetMnemonic");
 assertEquals(mnemonic, resetButton.getMnemonic());
 }

 @Test
 public void testKeyStrokeForCancelButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton cancelButton = dialog.getContentPane().getComponents()[1];
 final KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke('\u001b');
 ActionMap actionMap = cancelButton.getActionMap();
 assertEquals(cancelKeyStroke, actionMap.get(cancelButton.getActionCommands()[0]));
 }

}