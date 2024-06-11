package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private Component c;
 private String title;
 private boolean modal;
 private JFontChooser chooserPane;
 private ActionListener okListener;
 private ActionListener cancelListener;
 private FontChooserDialog fontChooserDialog;

 @Before
 public void setUp() throws HeadlessException {
 c = new JFrame();
 title = "FontChooserDialogTest";
 modal = true;
 chooserPane = new JFontChooser();
 okListener = e -> {};
 cancelListener = e -> {};
 }

 @Test
 public void testFontChooserDialog() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 assertNotNull(fontChooserDialog.getRootPane());
 assertNotNull(fontChooserDialog.getContentPane());
 assertNotNull(fontChooserDialog.getModalExclusionType());
 assertSame(fontChooserDialog.getTitle(), title);
 assertSame(fontChooserDialog.getModal(), modal);
 assertSame(fontChooserDialog.getChooserPane(), chooserPane);
 fontChooserDialog.dispose();
 }

 @Test
 public void testSetVisible() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 assertTrue(fontChooserDialog.isVisible());
 assertSame(fontChooserDialog.getChooserPane().getFont(), fontChooserDialog.initialFont);
 fontChooserDialog.dispose();
 }

 @Test
 public void testReset() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.reset();
 assertNotSame(fontChooserDialog.getChooserPane().getFont(), fontChooserDialog.initialFont);
 assertSame(fontChooserDialog.getChooserPane().getFont(), Font.decode("Dialog"));
 fontChooserDialog.dispose();
 }

 @Test
 public void testOKButtonActionPerformed() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 ActionEvent actionEvent = new ActionEvent(fontChooserDialog, 0, "OK");
 okListener.actionPerformed(actionEvent);
 assertFalse(fontChooserDialog.isVisible());
 fontChooserDialog.dispose();
 }

 @Test
 public void testCancelButtonActionPerformed() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 ActionEvent actionEvent = new ActionEvent(fontChooserDialog, 0, "Cancel");
 cancelListener.actionPerformed(actionEvent);
 assertFalse(fontChooserDialog.isVisible());
 fontChooserDialog.dispose();
 }

 @Test
 public void testEscKeyActionPerformed() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.setFocusableWindowState(true);
 Robot robot = new Robot();
 robot.keyPress(KeyEvent.VK_ESCAPE);
 robot.keyRelease(KeyEvent.VK_ESCAPE);
 assertFalse(fontChooserDialog.isVisible());
 fontChooserDialog.dispose();
 }

 @Test
 public void testResetButtonActionPerformed() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.reset();
 ActionEvent actionEvent = new ActionEvent(fontChooserDialog, 0, "Reset");
 okListener.actionPerformed(actionEvent);
 assertFalse(fontChooserDialog.isVisible());
 fontChooserDialog.dispose();
 }

 @After
 public void tearDown() {
 c.dispose();
 }

}