package osa.ora.server.client.ui.utils.test;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import osa.ora.server.client.ui.utils.FontChooserDialog;
import osa.ora.server.client.ui.utils.FontChooserDialog.Closer;
import osa.ora.server.client.ui.utils.FontChooserDialog.DisposeOnClose;

public class FontChooserDialogTest {

 private JFrame frame;
 private FontChooserDialog dialog;

 @Before
 public void setUp() {
 JOptionPane.setRootFrame(null);
 frame = new JFrame();
 dialog = new FontChooserDialog(frame, "FontChooserDialog Test", true, null, null, null);
 dialog.setVisible(true);
 }

 @After
 public void tearDown() {
 dialog.dispose();
 frame.dispose();
 }

 @Test
 public void testFontChooserDialogConstructor() {
 // Check if the dialog is created without any exceptions
 assertNotNull(dialog);
 }

 @Test
 public void testSetVisible() {
 Font initialFont = null;
 dialog.setVisible(true);
 assertNotNull(dialog.chooserPane);
 initialFont = dialog.chooserPane.getFont();
 dialog.setVisible(false);
 dialog.setVisible(true);
 assertNotEquals(initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testReset() {
 Font initialFont = null;
 dialog.setVisible(true);
 initialFont = dialog.chooserPane.getFont();
 dialog.reset();
 assertSame(initialFont, dialog.chooserPane.getFont());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFontChooserDialogConstructorNullComponent() {
 new FontChooserDialog(null, "Title", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullTitle() {
 new FontChooserDialog(frame, null, true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullChooserPane() {
 new FontChooserDialog(frame, "Title", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullOkListener() {
 ActionListener okListener = null;
 new FontChooserDialog(frame, "Title", true, null, okListener, null);
 }

 @Test
 public void testOkButtonAction() {
 dialog.setVisible(true);
 dialog.getOKButton().doClick();
 assertTrue(dialog.isVisible());
 dialog.dispose();
 }

 @Test(expected = HeadlessException.class)
 public void testOkButtonActionHeadlessEnvironment() {
 System.setProperty("java.awt.headless", "true");
 dialog.setVisible(true);
 dialog.getOKButton().doClick();
 System.setProperty("java.awt.headless", "false");
 }

 @Test
 public void testCancelButtonAction() {
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test(expected = HeadlessException.class)
 public void testCancelButtonActionHeadlessEnvironment() {
 System.setProperty("java.awt.headless", "true");
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 System.setProperty("java.awt.headless", "false");
 }

 @Test
 public void testResetButtonAction() {
 dialog.setVisible(true);
 dialog.getResetButton().doClick();
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testWindowClosing() {
 dialog.addWindowListener(new Closer());
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertTrue(dialog.isDisposed());
 }

 @Test
 public void testComponentHidden() {
 dialog.addComponentListener(new DisposeOnClose());
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertTrue(dialog.isDisposed());
 }
}