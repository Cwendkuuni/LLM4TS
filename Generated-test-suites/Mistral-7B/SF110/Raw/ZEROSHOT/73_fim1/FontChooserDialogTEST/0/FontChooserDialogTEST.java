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
 private FontChooserDialog fontChooserDialog;

 @Before
 public void setUp() {
 frame = new JFrame();
 frame.setVisible(true);
 parentComponent = new JButton();
 }

 @Test
 public void testFontChooserDialogConstructor() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, null, null, null);
 assertNotNull(fontChooserDialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, null, null, null);
 fontChooserDialog.setVisible(true);
 assertNotNull(fontChooserDialog.initialFont);
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, null, null, null);
 fontChooserDialog.setVisible(false);
 assertNull(fontChooserDialog.initialFont);
 }

 @Test
 public void testReset() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, null, null, null);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.reset();
 assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
 }

 @Test
 public void testOkButtonActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "OK");
 fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, null, null, null);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.okButton.dispatchEvent(actionEvent);
 assertFalse(fontChooserDialog.isVisible());
 }

 @Test
 public void testCancelButtonActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Cancel");
 fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, null, null, null);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.cancelButton.dispatchEvent(actionEvent);
 assertFalse(fontChooserDialog.isVisible());
 }

 @Test
 public void testResetButtonActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "Reset");
 fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, null, null, null);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.resetButton.dispatchEvent(actionEvent);
 assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
 }

 @After
 public void tearDown() {
 frame.dispose();
 parentComponent = null;
 fontChooserDialog = null;
 }
}