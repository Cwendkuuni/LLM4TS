package osa.ora.server.client.ui.utils.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import osa.ora.server.client.ui.utils.FontChooserDialog;
import osa.ora.server.client.ui.utils.FontChooserDialog.Closer;
import osa.ora.server.client.ui.utils.FontChooserDialog.DisposeOnClose;

@RunWith(org.mockito.jUnit.MockitoJUnitRunner.class)
public class FontChooserDialogTest {

 // Test constructor
 @Test
 public void testFontChooserDialogConstructor() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 assertNotNull(dialog);
 }

 // Test setVisible method
 @Test
 public void testSetVisible() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 dialog.setVisible(true);
 assertNotNull(dialog.getInitialFont());
 dialog.setVisible(false);
}

 // Test reset method
 @Test
 public void testReset() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.reset();
 assertSame(dialog.getInitialFont(), dialog.chooserPane.getFont());
 dialog.setVisible(false);
 }

 // Test OK button action
 @Test
 public void testOkButtonAction() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Do something on OK button click
 }
 });
 dialog.setVisible(true);
 dialog.getOkButton().doClick();
 dialog.setVisible(false);
 assertTrue(dialog.isClosed());
 }

 // Test Cancel button action
 @Test
 public void testCancelButtonAction() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Do something on Cancel button click
 }
 });
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 dialog.setVisible(false);
 assertFalse(dialog.isClosed());
 }

 // Test reset button action
 @Test
 public void testResetButtonAction() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.getResetButton().doClick();
 assertSame(dialog.getInitialFont(), dialog.chooserPane.getFont());
 dialog.setVisible(false);
 }

 // Test exception when null component passed in constructor
 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullComponent() {
 new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 }

 // Test exception when null title passed in constructor
 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullTitle() {
 new FontChooserDialog(new JFrame(), null, true, null, null, null);
 }

 // Test exception when OK listener is null
 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullOkListener() {
 new FontChooserDialog(new JFrame(), "Test Dialog", true, null, null, null);
 }

 // Test exception when cancel listener is null
 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullCancelListener() {
 new FontChooserDialog(new JFrame(), "Test Dialog", true, null, null, null, null);
 }

 // Test exception when chooserPane is null
 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullChooserPane() {
 new FontChooserDialog(new JFrame(), "Test Dialog", true, null, null, null, null);
 }

 // Test exception when okListener and cancelListener are both null
 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullListeners() {
 new FontChooserDialog(new JFrame(), "Test Dialog", true, null, null, null, null, null);
 }

 // Test exception when title is empty string
 @Test(expected = IllegalArgumentException.class)
 public void testFontChooserDialogEmptyTitle() {
 new FontChooserDialog(new JFrame(), "", true, null, null, null);
 }

 // Test exception when title is too long
 @Test(expected = IllegalArgumentException.class)
 public void testFontChooserDialogLongTitle() {
 String longTitle = "A".repeat(101);
 new FontChooserDialog(new JFrame(), longTitle, true, null, null, null);
 }
}