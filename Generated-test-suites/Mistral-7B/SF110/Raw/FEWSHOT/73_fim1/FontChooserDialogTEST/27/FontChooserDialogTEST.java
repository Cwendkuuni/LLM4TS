package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import javax.swing.*;
import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private JFrame frame;
 private FontChooserDialog dialog;

 @Before
 public void setUp() {
 JVMArguments.limitMaxHeapSize(10 * 1024 * 1024); // Set maximum heap size to 10MB for test
 frame = new JFrame();
 }

 @Test
 public void testFontChooserDialogCreation() {
 // Test constructor with no arguments
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 assertNotNull(dialog);
 dialog.setVisible(true);
 dialog.dispose();

 // Test constructor with JFontChooser and ActionListeners
 ActionListener okListener = e -> System.out.println("OK clicked");
 ActionListener cancelListener = e -> System.out.println("Cancel clicked");
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, new JFontChooser(), okListener, cancelListener);
 assertNotNull(dialog);
 dialog.setVisible(true);
 dialog.dispose();
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> System.out.println("OK clicked");
 ActionListener cancelListener = e -> System.out.println("Cancel clicked");
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);

 // Test that the initial font is saved
 Font initialFont = dialog.getInitialFont();
 assertEquals("Arial", initialFont.getFamily());
 dialog.dispose();
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> System.out.println("OK clicked");
 ActionListener cancelListener = e -> System.out.println("Cancel clicked");
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.dispose();

 // Test that no initial font is saved
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.dispose();

 Font initialFont = dialog.getInitialFont();
 assertNull(initialFont);
 }

 @Test
 public void testReset() {
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> System.out.println("OK clicked");
 ActionListener cancelListener = e -> System.out.println("Cancel clicked");
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.reset();
 dialog.dispose();

 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.setFont(new Font("Serif", Font.BOLD, 12));
 dialog.reset();
 dialog.dispose();

 Font initialFont = dialog.getInitialFont();
 assertEquals("Arial", initialFont.getFamily());
 }
}