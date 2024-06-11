package osa.ora.server.client.ui.utils.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;
 private ActionEvent okEvent;
 private ActionEvent cancelEvent;

 @Before
 public void setUp() {
 JFontChooser chooserPane = new JFontChooser();
 frame = new JFrame();
 okEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "OK");
 cancelEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "CANCEL");
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Handle OK button action
 }
 }, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Handle CANCEL button action
 }
 });
 dialog.addWindowListener(new FontChooserDialog.Closer());
 dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 }

 @Test
 public void testInitialFont() {
 Font initialFont = dialog.getInitialFont();
 assertNotNull(initialFont);
}

 @Test
 public void testSetVisible() {
 dialog.setVisible(true);
 dialog.setVisible(false);
}

 @Test
 public void testReset() {
 dialog.reset();
 Font font = dialog.chooserPane.getFont();
 assertEquals(dialog.initialFont, font);
}

 @Test
 public void testOKButtonAction() {
 dialog.chooserPane.setFont(new Font("Serif", Font.BOLD, 20));
 JButton okButton = dialog.findButton("OK");
 okButton.doClick();
 assertFalse(dialog.isVisible());
}

 @Test
 public void testCancelButtonAction() {
 dialog.chooserPane.setFont(new Font("Serif", Font.BOLD, 20));
 JButton cancelButton = dialog.findButton("CANCEL");
 cancelButton.doClick();
 assertFalse(dialog.isVisible());
}

 @Test(expected = NullPointerException.class)
 public void testNullInitialComponent() {
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 fail("Expected NullPointerException");
}

 @Test(expected = NullPointerException.class)
 public void testNullOkListener() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 fail("Expected NullPointerException");
}

 @Test(expected = NullPointerException.class)
 public void testNullCancelListener() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, null, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 }
 });
 dialog.setVisible(true);
 fail("Expected NullPointerException");
}
}