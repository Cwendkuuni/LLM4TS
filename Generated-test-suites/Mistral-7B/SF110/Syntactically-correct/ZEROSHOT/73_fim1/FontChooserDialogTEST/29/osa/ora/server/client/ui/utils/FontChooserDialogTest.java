package osa.ora.server.client.ui.utils;

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
 private JFontChooser chooserPane;

 @Before
 public void setUp() {
 parentComponent = new JFrame();
 frame = new JFrame();
 chooserPane = new JFontChooser();
 }

 @Test
 public void testFontChooserDialogConstruction() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, null, null);
 assertNotNull(fontChooserDialog.getContentPane());
 assertNotNull(fontChooserDialog.getOkButton());
 assertNotNull(fontChooserDialog.getCancelButton());
 assertNotNull(fontChooserDialog.getResetButton());
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, null, null);
 dialog.setVisible(true);
 assertNotNull(dialog.getInitialFont());
 dialog.setVisible(false);
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, null, null);
 dialog.setVisible(false);
 dialog.setVisible(true);
 assertNull(dialog.getInitialFont());
 }

 @Test
 public void testReset() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.reset();
 assertEquals(dialog.getInitialFont(), chooserPane.getFont());
 }

 @Test
 public void testOkButtonAction() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, e -> {}, e -> {});
 dialog.setVisible(true);
 dialog.getOkButton().doClick();
 assertTrue(dialog.isVisible());
 dialog.dispose();
 }

 @Test
 public void testCancelButtonAction() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, e -> {}, e -> {});
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testResetButtonAction() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.getResetButton().doClick();
 assertEquals(dialog.getInitialFont(), chooserPane.getFont());
 dialog.dispose();
 }

 @Test(expected = NullPointerException.class)
 public void testNullParentComponent() {
 dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 dialog = new FontChooserDialog(parentComponent, null, true, chooserPane, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullModal() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser", (boolean)null, chooserPane, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullChooserPane() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullOkListener() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, null, null);
 dialog.getOkButton().removeActionListener(dialog.getOkButtonListener());
 dialog.getOkButton().addActionListener(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullCancelListener() {
 dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, null, null);
 dialog.getCancelButton().removeActionListener(dialog.getCancelButtonListener());
 dialog.getCancelButton().addActionListener(null);
 }
}