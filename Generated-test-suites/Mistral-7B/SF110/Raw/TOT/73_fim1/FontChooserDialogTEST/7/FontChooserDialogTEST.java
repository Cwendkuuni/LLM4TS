package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;
 private JFontChooser chooserPane;

 @Before
 public void setUp() {
 final Component c = new JFrame();
 dialog = new FontChooserDialog(c, "Font Chooser Dialog", true, chooserPane, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on ok button click
 }
 }, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on cancel button click
 }
 });
 frame = (JFrame) dialog.getRootPane().getParent();
 }

 @Test
 public void testInitialFont() {
 Font initialFont = dialog.getInitialFont();
 assertNotNull(initialFont);
}

 @Test
 public void testSetVisible() {
 dialog.setVisible(true);
 Font savedFont = dialog.chooserPane.getFont();
 dialog.setVisible(false);
 dialog.reset();
 assertNotSame(savedFont, dialog.chooserPane.getFont());
}

 @Test
 public void testOkButtonAction() {
 dialog.setVisible(true);
 dialog.okButton.doClick();
 dialog.setVisible(false);
 // Assert that the dialog has been closed
 assertEquals(0, frame.getWidth());
 assertEquals(0, frame.getHeight());
}

 @Test
 public void testCancelButtonAction() {
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 dialog.setVisible(false);
 // Assert that the dialog has been closed
 assertEquals(0, frame.getWidth());
 assertEquals(0, frame.getHeight());
}

 @Test(expected = NullPointerException.class)
 public void testNullComponent() {
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, chooserPane, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on ok button click
 }
 }, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on cancel button click
 }
 });
}

 @Test(expected = IllegalArgumentException.class)
 public void testNullFrame() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on ok button click
 }
 }, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on cancel button click
 }
 });
 frame = null;
 dialog.setVisible(true);
 dialog.setVisible(false);
}

 @Test(expected = IllegalArgumentException.class)
 public void testNullChooserPane() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on ok button click
 }
 }, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on cancel button click
 }
 });
 chooserPane = null;
 dialog.setVisible(true);
 dialog.setVisible(false);
}

 @Test
 public void testOkListener() {
 ActionListener okListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on ok button click
 }
 };
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on cancel button click
 }
 });
 dialog.setVisible(true);
 dialog.okButton.doClick();
 dialog.setVisible(false);
 okListener.actionPerformed(new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "OK"));
 // Assert that the okListener has been called
 assertTrue(okListener.isEnabled());
}

 @Test
 public void testCancelListener() {
 ActionListener cancelListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on cancel button click
 }
 };
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // do something on ok button click
 }
 }, cancelListener);
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 dialog.setVisible(false);
 cancelListener.actionPerformed(new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "CANCEL"));
 // Assert that the cancelListener has been called
 assertTrue(cancelListener.isEnabled());
}

 @Test
 public void testResetButtonAction() {
 dialog.setVisible(true);
 dialog.resetButton.doClick();
 dialog.setVisible(false);
 Font resetFont = dialog.chooserPane.getFont();
 dialog.setVisible(true);
 dialog.chooserPane.setFont(new Font("Serif", Font.BOLD, 12));
 dialog.resetButton.doClick();
 dialog.setVisible(false);
 assertEquals(resetFont, dialog.chooserPane.getFont());
}

 @Test
 public void testKeyEventOnCancelButton() {
 dialog.setVisible(true);
 dialog.focusLost(new FocusEvent(dialog, FocusEvent.FOCUS_LOST, FocusEvent.FOCUS_IN, new KeyEvent(dialog, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ESCAPE)));
 dialog.setVisible(false);
 // Assert that the dialog has been closed
 assertEquals(0, frame.getWidth());
 assertEquals(0, frame.getHeight());
}
}