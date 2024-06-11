package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private Component parentComponent;
 private ActionListener okListener;
 private ActionListener cancelListener;
 private Font initialFont;
 private JFontChooser chooserPane;
 private FontChooserDialog fontChooserDialog;

 @Before
 public void setUp() {
 parentComponent = new JFrame();
 okListener = e -> {};
 cancelListener = e -> {};
 chooserPane = new JFontChooser();
 initialFont = new Font("Arial", Font.BOLD, 12);
 }

 @After
 public void tearDown() {
 if (fontChooserDialog != null) {
 fontChooserDialog.dispose();
 }
 }

 @Test
 public void testFontChooserDialogConstruction() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 assertTrue(fontChooserDialog.isVisible());
 assertEquals("Font Chooser Dialog", fontChooserDialog.getTitle());
 }

 @Test
 public void testOkButtonAction() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.okButtonActionPerformed();
 assertFalse(fontChooserDialog.isVisible());
 }

 @Test
 public void testCancelButtonAction() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.cancelButtonActionPerformed();
 assertFalse(fontChooserDialog.isVisible());
 }

 @Test
 public void testResetButtonAction() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 chooserPane.setFont(new Font("Verdana", Font.ITALIC, 14));
 fontChooserDialog.resetButtonActionPerformed();
 assertEquals(initialFont, chooserPane.getFont());
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructionWithNullParentComponent() {
 fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructionWithNullTitle() {
 fontChooserDialog = new FontChooserDialog(parentComponent, null, true, chooserPane, okListener, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructionWithNullChooserPane() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructionWithNullOKListener() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, null, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructionWithNullCancelListener() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, null);
 }

 @Test
 public void testFontChooserDialogSetVisibleTrue() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 assertTrue(fontChooserDialog.isVisible());
 }

 @Test
 public void testFontChooserDialogSetVisibleFalse() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.setVisible(false);
 assertFalse(fontChooserDialog.isVisible());
 }

 @Test
 public void testFontChooserDialogReset() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 chooserPane.setFont(new Font("Verdana", Font.BOLD, 14));
 fontChooserDialog.reset();
 assertEquals(initialFont, chooserPane.getFont());
 }
}