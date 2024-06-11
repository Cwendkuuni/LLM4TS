package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private JFrame frame;
 private Component testComponent;
 private JFontChooser chooserPane;
 private FontChooserDialog dialog;

 @Before
 public void setUp() {
 frame = new JFrame();
 testComponent = new JButton("Test Button");
 chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, chooserPane, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Handle OK button click
 }
 }, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Handle Cancel button click
 }
 });
 }

 @Test
 public void testSetVisible() {
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 dialog.setVisible(false);
 dialog.setVisible(true);
 dialog.setVisible(false);
 // Test setVisible multiple times
 dialog.setVisible(true);
 assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
 dialog.reset();
 dialog.setVisible(true);
 assertNotEquals(dialog.chooserPane.getFont(), dialog.initialFont);
 }

 @Test
 public void testReset() {
 dialog.reset();
 assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
 dialog.setVisible(true);
 dialog.reset();
 assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
 }

 @Test
 public void testActionListeners() {
 dialog.setVisible(true);
 dialog.okButtonPressed();
 dialog.cancelButtonPressed();
 dialog.resetButtonPressed();
 dialog.setVisible(false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullComponent() {
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.setVisible(false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullOkListener() {
 dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.setVisible(false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullCancelListener() {
 dialog = new FontChooserDialog(testComponent, "Font Chooser Dialog", true, chooserPane, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Handle OK button click
 }
 }, null);
 dialog.setVisible(true);
 dialog.setVisible(false);
 }
}