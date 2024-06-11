package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private Component parentComponent;

 @Before
 public void setUp() {
 parentComponent = new JFrame();
 dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, null, null);
 }

 @After
 public void tearDown() {
 dialog.dispose();
 parentComponent.dispose();
 }

 @Test
 public void testFontChooserDialogConstructor() {
 // Basic functionality test
 dialog.setVisible(true);
 dialog.pack();
 dialog.setVisible(false);

 // Test for null arguments
 new AssertionError("Should throw an exception for null Component argument") {
 @Override
 public void run() {
 try {
 new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }
 }.run();

 new AssertionError("Should throw an exception for null ActionListener arguments") {
 @Override
 public void run() {
 try {
 new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, null, null, null, null);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }
 }.run();
 }

 @Test
 public void testSetVisible() {
 // Basic functionality test
 dialog.setVisible(true);
 dialog.setVisible(false);

 // Test for initialFont is set when dialog is shown
 JFontChooser chooserPane = dialog.chooserPane;
 Font initialFont = chooserPane.getFont();
 dialog.setVisible(true);
 assertEquals(initialFont, chooserPane.getFont());
 dialog.setVisible(false);
 }

 @Test
 public void testReset() {
 // Basic functionality test
 dialog.setVisible(true);
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test(expected = IllegalStateException.class)
 public void testWindowEvents() {
 dialog.addWindowListener(new FontChooserDialog.Closer());
 dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 dialog.setVisible(true);
 dialog.dispose();
 dialog.setVisible(true);
 }
}