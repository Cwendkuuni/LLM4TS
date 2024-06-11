package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
 frame.setVisible(true);
 parentComponent = new JButton();
 okListener = e -> {};
 cancelListener = e -> {};
 }

 @Test
 public void testFontChooserDialogCreation() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Test Dialog", true, new JFontChooser(), okListener, cancelListener);
 assertNotNull(dialog);
 assertEquals(true, dialog.isModal());
 assertNotNull(dialog.getContentPane());
 assertNotNull(dialog.getRootPane());
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 dialog = new FontChooserDialog(parentComponent, "Test Dialog", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertNotNull(dialog.initialFont);
 assertEquals(initialFont, dialog.initialFont);
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 dialog = new FontChooserDialog(parentComponent, "Test Dialog", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertNull(dialog.initialFont);
 }

 @Test
 public void testReset() {
 JFontChooser chooser = new JFontChooser();
 Font font = new Font("Arial", Font.BOLD, 12);
 chooser.setFont(font);
 dialog = new FontChooserDialog(parentComponent, "Test Dialog", true, chooser, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.reset();
 assertNotNull(dialog.initialFont);
 assertEquals(font, dialog.chooserPane.getFont());
 }

 @Test
 public void testOkButtonAction() {
 dialog = new FontChooserDialog(parentComponent, "Test Dialog", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 dialog.okButton.doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testCancelButtonAction() {
 dialog = new FontChooserDialog(parentComponent, "Test Dialog", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testOkKeyEvent() {
 dialog = new FontChooserDialog(parentComponent, "Test Dialog", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 Robot robot = new Robot();
 robot.keyPress(KeyEvent.VK_ENTER);
 robot.keyRelease(KeyEvent.VK_ENTER);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testCancelKeyEvent() {
 dialog = new FontChooserDialog(parentComponent, "Test Dialog", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 Robot robot = new Robot();
 robot.keyPress(KeyEvent.VK_ESCAPE);
 robot.keyRelease(KeyEvent.VK_ESCAPE);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testDisposeOnClose() {
 dialog = new FontChooserDialog(parentComponent, "Test Dialog", true, new JFontChooser(), okListener, cancelListener);
 dialog.setVisible(true);
 dialog.addWindowListener(new FontChooserDialog.Closer());
 dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 dialog.dispose();
 assertTrue(dialog.isDisposed());
 }
}