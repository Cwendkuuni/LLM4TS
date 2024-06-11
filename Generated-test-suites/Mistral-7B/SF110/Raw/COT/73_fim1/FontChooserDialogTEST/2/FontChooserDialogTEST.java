package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import org.junit.*;

public class FontChooserDialogTest {

 private Font initialFont;
 private JFontChooser chooserPane;
 private FontChooserDialog dialog;

 @Before
 public void setUp() {
 initialFont = new Font("Arial", Font.PLAIN, 12);
 chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, chooserPane, null, null);
 }

 @After
 public void tearDown() {
 dialog.dispose();
 }

 // Test for constructor
 @Test
 public void testFontChooserDialogConstructor() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, chooserPane, null, null);
 assertNotNull(fontChooserDialog);
 }

 // Test for setVisible method
 @Test
 public void testSetVisible() {
 dialog.setVisible(true);
 assertNotNull(dialog.getInitialFont());
 dialog.setVisible(false);
 dialog.setVisible(true);
 assertSame(initialFont, dialog.getInitialFont());
 }

 // Test for reset method
 @Test
 public void testReset() {
 dialog.setVisible(true);
 dialog.reset();
 assertNotSame(initialFont, dialog.getInitialFont());
 dialog.setVisible(false);
 dialog.setVisible(true);
 dialog.reset();
 assertSame(initialFont, dialog.getInitialFont());
 }

 // Test for window closing event
 @Test
 public void testWindowClosing() {
 dialog.addWindowListener(new FontChooserDialog.Closer());
 dialog.setVisible(true);
 dialog.dispose();
}

 // Test for component hidden event
 @Test
 public void testComponentHidden() {
 dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
 dialog.setVisible(true);
 assertTrue(dialog.isDisplayable());
 dialog.dispose();
 assertFalse(dialog.isDisplayable());
 }

 // Test for null component in constructor
 @Test(expected = IllegalArgumentException.class)
 public void testNullComponentInConstructor() {
 new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 }

 // Test for null title in constructor
 @Test(expected = NullPointerException.class)
 public void testNullTitleInConstructor() {
 new FontChooserDialog(null, null, true, chooserPane, null, null);
 }

 // Test for null chooserPane in constructor
 @Test(expected = NullPointerException.class)
 public void testNullChooserPaneInConstructor() {
 new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 }

 // Test for null okListener in constructor
 @Test
 public void testNullOkListenerInConstructor() {
 ActionListener okListener = null;
 new FontChooserDialog(null, "Font Chooser Dialog", true, chooserPane, okListener, null);
 }

 // Test for null cancelListener in constructor
 @Test
 public void testNullCancelListenerInConstructor() {
 ActionListener cancelListener = null;
 new FontChooserDialog(null, "Font Chooser Dialog", true, chooserPane, null, cancelListener);
 }
}