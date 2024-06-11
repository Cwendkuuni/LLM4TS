package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;
 private JFontChooser chooserPane;

 @Before
 public void setUp() {
 frame = new JFrame();
 chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, e -> {}, e -> {});
 }

 @Test
 public void testDialogConstruction() {
 assertNotNull(dialog);
 assertSame(frame, dialog.getParent());
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertSame(chooserPane, dialog.getChooserPane());
 assertNull(dialog.getInitialFont());
 }

 @Test
 public void testSetInitialFont() {
 Font initialFont = new Font("Arial", Font.BOLD | Font.ITALIC, 12);
 dialog.setInitialFont(initialFont);
 assertSame(initialFont, dialog.getInitialFont());
 }

 @Test
 public void testGetInitialFont() {
 Font initialFont = new Font("Arial", Font.BOLD | Font.ITALIC, 12);
 dialog.setInitialFont(initialFont);
 assertSame(initialFont, dialog.getInitialFont());
 }

 @Test
 public void testSetVisible() {
 dialog.setVisible(true);
 assertTrue(dialog.isVisible());
 assertNotNull(dialog.getInitialFont());
 dialog.setVisible(false);
 assertFalse(dialog.isVisible());
 assertSame(dialog.getInitialFont(), dialog.getChooserPane().getFont());
 }

 @Test
 public void testReset() {
 dialog.setVisible(true);
 dialog.reset();
 assertSame(dialog.getInitialFont(), dialog.getChooserPane().getFont());
 dialog.setVisible(false);
 }

 @Test
 public void testOkButtonActionPerformed() {
 dialog.setVisible(true);
 dialog.getOkButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testCancelButtonActionPerformed() {
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testResetButtonActionPerformed() {
 dialog.setVisible(true);
 dialog.getResetButton().doClick();
 assertSame(dialog.getInitialFont(), dialog.getChooserPane().getFont());
 dialog.setVisible(false);
 }

 @Test
 public void testKeyEventHandling() throws KeyEvent {
 dialog.setVisible(true);
 Robot robot = new Robot();
 robot.setAutoDelay(100);
 KeyEvent keyEvent = new KeyEvent(dialog, KeyEvent.KEY_PRESSED, 0, '\u001b');
 dialog.getCancelButton().getInputMap(2).invoke(keyEvent, null);
 assertFalse(dialog.isVisible());
 }
}