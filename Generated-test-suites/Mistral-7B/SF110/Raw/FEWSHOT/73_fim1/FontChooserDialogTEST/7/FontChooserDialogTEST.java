package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import javax.swing.*;
import org.junit.Test;

public class FontChooserDialogTest {

 @Test
 public void testFontChooserDialog() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 assertNotNull(dialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 assertEquals(dialog.getChooserPane().getFont(), dialog.initialFont);
 dialog.setVisible(false);
 }

 @Test
 public void testReset() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.reset();
 assertEquals(dialog.getChooserPane().getFont(), dialog.initialFont);
 dialog.setVisible(false);
 }

 @Test
 public void testGetContentPane() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 Container contentPane = dialog.getContentPane();
 assertNotNull(contentPane);
 }

 @Test
 public void testSetLayoutOnContentPane() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 Container contentPane = dialog.getContentPane();
 assertTrue(contentPane instanceof BorderLayout);
 }

 @Test
 public void testAddComponentsToContentPane() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 Container contentPane = dialog.getContentPane();
 JFontChooser chooserPane = dialog.getChooserPane();
 JPanel buttonPane = dialog.getButtonPane();
 contentPane.add(chooserPane, "Center");
 contentPane.add(buttonPane, "South");
 assertEquals(dialog.getComponentCount(), 3);
 }

 @Test
 public void testSetLocationRelativeTo() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 Component c = new JFrame();
 dialog.setLocationRelativeTo(c);
 assertNotNull(dialog.getX(), dialog.getY());
 }

 @Test
 public void testGetRootPane() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 Container rootPane = dialog.getRootPane();
 assertNotNull(rootPane);
 }

 @Test
 public void testSetDefaultButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton okButton = dialog.getOkButton();
 dialog.getRootPane().setDefaultButton(okButton);
 assertTrue(okButton.isFocusTraversalKeysEnabled());
 }

 @Test
 public void testGetOkButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton okButton = dialog.getOkButton();
 assertNotNull(okButton);
 }

 @Test
 public void testSetActionCommandOnOkButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton okButton = dialog.getOkButton();
 okButton.setActionCommand("OK");
 assertEquals("OK", okButton.getActionCommand());
 }

 @Test
 public void testAddActionListenerOnOkButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton okButton = dialog.getOkButton();
 ActionListener listener = mock(ActionListener.class);
 okButton.addActionListener(listener.mock());
 verify(listener).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testAddActionListenerOnOkButtonToHideDialog() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton okButton = dialog.getOkButton();
 okButton.addActionListener(new ActionListener() {
 @Override public void actionPerformed(ActionEvent e) { dialog.setVisible(false); }
 });
 dialog.setVisible(true);
 assertTrue(dialog.isVisible());
 okButton.doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testGetCancelButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton cancelButton = dialog.getCancelButton();
 assertNotNull(cancelButton);
 }

 @Test
 public void testSetActionCommandOnCancelButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton cancelButton = dialog.getCancelButton();
 cancelButton.setActionCommand("CANCEL");
 assertEquals("CANCEL", cancelButton.getActionCommand());
 }

 @Test
 public void testAddActionListenerOnCancelButtonToHideDialog() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton cancelButton = dialog.getCancelButton();
 cancelButton.addActionListener(new ActionListener() {
 @Override public void actionPerformed(ActionEvent e) { dialog.setVisible(false); }
 });
 dialog.setVisible(true);
 assertTrue(dialog.isVisible());
 cancelButton.doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testGetResetButton() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton resetButton = dialog.getResetButton();
 assertNotNull(resetButton);
 }

 @Test
 public void testAddActionListenerOnResetButtonToResetFont() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 JButton resetButton = dialog.getResetButton();
 ActionListener listener = mock(ActionListener.class);
 resetButton.addActionListener(listener.mock());
 dialog.setVisible(true);
 dialog.reset();
 verify(listener).actionPerformed(any(ActionEvent.class));
 }
}