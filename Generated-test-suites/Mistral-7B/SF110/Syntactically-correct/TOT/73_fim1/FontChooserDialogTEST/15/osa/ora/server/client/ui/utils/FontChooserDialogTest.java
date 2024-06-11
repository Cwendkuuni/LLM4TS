package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JButton okButton;
 private JButton cancelButton;
 private JFontChooser chooserPane;

 @Before
 public void setUp() {
 JFrame frame = new JFrame();
 chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, null, null);
 okButton = (JButton)dialog.getContentPane().getComponent(0);
 cancelButton = (JButton)dialog.getContentPane().getComponent(1);
 }

 @Test
 public void testInitialFontIsNotNull() {
 assertNotNull(dialog.initialFont);
 }

 @Test
 public void testOkButtonSetsDialogVisibleToFalseOnClick() {
 dialog.setVisible(true);
 okButton.doClick();
 assertTrue(dialog.isVisible() == false);
 }

 @Test
 public void testCancelButtonSetsDialogVisibleToFalseOnClick() {
 dialog.setVisible(true);
 cancelButton.doClick();
 assertTrue(dialog.isVisible() == false);
 }

 @Test
 public void testInitialFontIsSetToChooserPane() {
 dialog.setVisible(true);
 Font initialFont = dialog.chooserPane.getFont();
 dialog.setVisible(false);
 dialog.setVisible(true);
 Font newFont = dialog.chooserPane.getFont();
 assertNotSame(initialFont, newFont);
 dialog.reset();
 Font resetFont = dialog.chooserPane.getFont();
 assertEquals(initialFont, resetFont);
 }

 @Test(expected = IllegalStateException.class)
 public void testExceptionWhenNullComponentPassed() {
 new FontChooserDialog(null, "Title", true, chooserPane, null, null);
 fail("No Exception thrown when null Component passed.");
 }

 @Test(expected = NullPointerException.class)
 public void testExceptionWhenNullActionListenerPassed() {
 ActionListener okListener = null;
 ActionListener cancelListener = null;
 new FontChooserDialog(new JFrame(), "Title", true, chooserPane, okListener, cancelListener);
 fail("No NullPointerException thrown when null ActionListener passed.");
 }
}