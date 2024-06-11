package osa.ora.server.client.ui.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;
 private JFontChooser chooserPane;

 @Before
 public void setUp() {
 frame = new JFrame();
 chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, null, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // handle OK button click
 }
 }, null);
 }

 @Test
 public void testInitialFont() {
 Font initialFont = dialog.getInitialFont();
 Assert.assertNotNull(initialFont);
 }

 @Test
 public void testSetVisible() {
 Font initialFont = dialog.getInitialFont();
 dialog.setVisible(true);
 dialog.setVisible(false);
 Assert.assertEquals(initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testReset() {
 dialog.reset();
 Assert.assertNotEquals(dialog.initialFont, dialog.chooserPane.getFont());
 dialog.setVisible(true);
 dialog.reset();
 Assert.assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testOKButton() {
 dialog.setVisible(true);
 dialog.getOkButton().doClick();
 dialog.setVisible(false);
 // check if OK action listener was invoked
 }

 @Test
 public void testCancelButton() {
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 dialog.setVisible(false);
 // check if Cancel action listener was invoked
 }

 @Test
 public void testEscKey() throws ActionEvent {
 Robot robot = new Robot();
 dialog.setVisible(true);
 dialog.toFront();
 robot.keyPress(KeyEvent.VK_ESCAPE);
 dialog.setVisible(false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullOKListener() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, null, null, null);
 dialog.setVisible(true);
 dialog.setVisible(false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullCancelListener() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, null, null, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // handle Cancel button click
 }
 });
 dialog.setVisible(true);
 dialog.setVisible(false);
 }
}