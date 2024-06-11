package osa.ora.server.client.ui.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private JFrame frame;
 private Component parentComponent;
 private FontChooserDialog dialog;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Before
 public void setUp() {
 frame = new JFrame();
 parentComponent = new JPanel();
 frame.getContentPane().add(parentComponent);
 okListener = e -> {};
 cancelListener = e -> {};
 }

 @After
 public void tearDown() {
 if (dialog != null) {
 dialog.dispose();
 }
 frame.dispose();
 }

 @Test
 public void testFontChooserDialogConstruction() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 assertNotNull(dialog);
 assertTrue(dialog.isModal());
 assertNotNull(dialog.getTitle());
 assertNull(dialog.getChooserPane());
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testFontChooserDialogConstructionWithChooserPane() {
 JFontChooser chooserPane = new JFontChooser();
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 assertNotNull(dialog);
 assertTrue(dialog.isModal());
 assertNotNull(dialog.getTitle());
 assertSame(dialog.getChooserPane(), chooserPane);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 JFontChooser chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 assertNotNull(dialog.getInitialFont());
 dialog.dispose();
}

 @Test
 public void testSetVisibleWithoutInitialFont() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 dialog.setVisible(true);
 assertNull(dialog.getInitialFont());
 dialog.dispose();
 }

 @Test
 public void testReset() {
 JFontChooser chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.reset();
 assertNull(dialog.chooserPane.getFont());
 dialog.dispose();
 }

 @Test
 public void testOKActionPerformed() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.okButton.doClick();
 assertTrue(okListener.wasInvoked());
 dialog.dispose();
 }

 @Test
 public void testCancelActionPerformed() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 assertTrue(cancelListener.wasInvoked());
 dialog.dispose();
 }

 @Test
 public void testResetActionPerformed() {
 JFontChooser chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.resetButton.doClick();
 assertNull(dialog.chooserPane.getFont());
 dialog.dispose();
 }
}