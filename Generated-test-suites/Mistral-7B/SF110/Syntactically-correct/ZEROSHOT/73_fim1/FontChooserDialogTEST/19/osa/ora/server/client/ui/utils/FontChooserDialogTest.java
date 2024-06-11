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
 private ActionListener okListener;
 private ActionListener cancelListener;
 private FontChooserDialog fontChooserDialog;

 @Before
 public void setUp() {
 frame = new JFrame();
 parentComponent = new JLabel();
 frame.add(parentComponent);
 okListener = e -> {};
 cancelListener = e -> {};
 }

 @Test
 public void testFontChooserDialogConstruction() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 assertNotNull(fontChooserDialog);
 assertTrue(fontChooserDialog.isModal());
 }

 @Test
 public void testFontChooserDialogConstructionWithChooserPane() {
 JFontChooser chooserPane = new JFontChooser();
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 assertNotNull(fontChooserDialog);
 assertSame(chooserPane, fontChooserDialog.chooserPane);
 }

 @Test
 public void testSetVisible() {
 Font font = new Font("Arial", Font.BOLD, 12);
 JFontChooser chooserPane = new JFontChooser();
 chooserPane.setFont(font);
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 assertTrue(fontChooserDialog.isVisible());
 fontChooserDialog.setVisible(false);
 assertFalse(fontChooserDialog.isVisible());
 }

 @Test
 public void testReset() {
 JFontChooser chooserPane = new JFontChooser();
 chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.reset();
 assertSame(new Font("Arial", Font.BOLD, 12), fontChooserDialog.chooserPane.getFont());
 }

 @Test
 public void testActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "OK");
 okListener = e -> System.out.println("OK button clicked");
 cancelListener = e -> System.out.println("Cancel button clicked");
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 fontChooserDialog.setVisible(true);

 okButtonClicked:
 for (int i = 0; i < 10; i++) {
 JButton okButton = fontChooserDialog.getRootPane().getDefaultButton();
 okButton.doClick();
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 if (okListener.actionPerformed != null) {
 System.out.println("OK button clicked " + (i + 1) + " times");
 continue okButtonClicked;
 }
 if (cancelListener.actionPerformed != null) {
 System.out.println("Cancel button clicked " + (i + 1) + " times");
 fail("Cancel button should not have been clicked");
 }
 }
}

 @After
 public void tearDown() {
 frame.dispose();
 parentComponent = null;
 okListener = null;
 cancelListener = null;
 fontChooserDialog = null;
 }

}