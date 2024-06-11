package osa.ora.server.client.ui.utils.test;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private Font initialFont;
 private JFontChooser chooserPane;
 private FontChooserDialog dialog;

 @Before
 public void setUp() {
 initialFont = new Font("Arial", Font.PLAIN, 12);
 chooserPane = new JFontChooser();
 }

 @Test
 public void testFontChooserDialogConstructor() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, null, null);
 assertNotNull(dialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, null, null);
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 assertSame(dialog.initialFont, chooserPane.getFont());
 dialog.dispose();
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.dispose();
 }

 @Test(expected = NullPointerException.class)
 public void testNullComponentInConstructor() {
 dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitleInConstructor() {
 dialog = new FontChooserDialog(null, null, true, chooserPane, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullChooserPaneInConstructor() {
 dialog = new FontChooserDialog(null, "Test Title", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullOKListenerInConstructor() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, null, null);
 dialog.addOKListener(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullCancelListenerInConstructor() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, null, null);
 dialog.addCancelListener(null);
 }

 @Test
 public void testOkActionPerformed() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, e -> {}, e -> {});
 dialog.setVisible(true);
 dialog.okButton.doClick();
 dialog.dispose();
 }

 @Test
 public void testCancelActionPerformed() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, e -> {}, e -> {});
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 dialog.dispose();
 }

 @Test
 public void testResetActionPerformed() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, e -> {}, e -> {});
 dialog.setVisible(true);
 dialog.resetButton.doClick();
 dialog.dispose();
 }

 @Test
 public void testWindowClosing() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, e -> {}, e -> {});
 dialog.setVisible(true);
 MouseEvent me = new MouseEvent(dialog, 1, 1, 1, 1, 1, 1, false);
 dialog.getRootPane().processMouseEvent(me);
 assertTrue(dialog.isDisposed());
 }

 @Test
 public void testComponentHidden() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, e -> {}, e -> {});
 dialog.setVisible(true);
 dialog.addComponentListener(e -> {});
 dialog.getContentPane().setVisible(false);
 assertTrue(dialog.isDisposed());
 }

 @Test
 public void testInitialFontIsNull() {
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, e -> {}, e -> {});
 dialog.setVisible(true);
 dialog.reset();
 assertNull(chooserPane.getFont());
 dialog.dispose();
 }

 @Test
 public void testInitialFontIsNotnull() {
 initialFont = new Font("Arial", Font.BOLD, 14);
 dialog = new FontChooserDialog(null, "Test Title", true, chooserPane, e -> {}, e -> {});
 dialog.setVisible(true);
 dialog.reset();
 assertSame(initialFont, chooserPane.getFont());
 dialog.dispose();
 }

}