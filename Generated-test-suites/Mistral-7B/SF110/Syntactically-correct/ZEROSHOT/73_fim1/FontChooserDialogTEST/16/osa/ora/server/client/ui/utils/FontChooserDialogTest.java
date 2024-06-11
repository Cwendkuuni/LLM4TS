package osa.ora.server.client.ui.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;

public class FontChooserDialogTest {

 @Test
 public void testFontChooserDialogConstructor() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 assertNotNull(fontChooserDialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 JFontChooser mockChooserPane = mock(JFontChooser.class);
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, mockChooserPane, null, null);
 fontChooserDialog.setVisible(true);
 assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 JFontChooser mockChooserPane = mock(JFontChooser.class);
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, mockChooserPane, null, null);
 fontChooserDialog.setVisible(true);
 assertNull(fontChooserDialog.initialFont);
 }

 @Test
 public void testReset() {
 JFontChooser mockChooserPane = mock(JFontChooser.class);
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, mockChooserPane, null, null);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.setFont(new Font("Verdana", Font.ITALIC, 14));
 fontChooserDialog.reset();
 assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
 }

 @Test
 public void testOkButtonAction() {
 JFontChooser mockChooserPane = mock(JFontChooser.class);
 ActionListener okListener = mock(ActionListener.class);
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, mockChooserPane, okListener, null);
 fontChooserDialog.setVisible(true);
 verify(okListener).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testCancelButtonAction() {
 JFontChooser mockChooserPane = mock(JFontChooser.class);
 ActionListener cancelListener = mock(ActionListener.class);
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, mockChooserPane, null, cancelListener);
 fontChooserDialog.setVisible(true);
 verify(cancelListener).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testResetButtonAction() {
 JFontChooser mockChooserPane = mock(JFontChooser.class);
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, mockChooserPane, null, null);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.reset();
 assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
 }
}