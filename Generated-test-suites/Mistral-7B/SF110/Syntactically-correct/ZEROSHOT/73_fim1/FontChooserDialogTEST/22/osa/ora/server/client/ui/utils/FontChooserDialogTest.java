package osa.ora.server.client.ui.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import javax.swing.JFrame;
import javax.swing.JFontChooser;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;

public class FontChooserDialogTest {

 private JFontChooser chooserPane;
 private JFrame frame;

 @Before
 public void setUp() {
 chooserPane = new JFontChooser();
 frame = new JFrame();
 }

 @Test
 public void testFontChooserDialogConstruction() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", true, chooserPane, null, null);
 assertNotNull(dialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.setFont(initialFont);
 dialog.dispose();
 assertNotNull(dialog.initialFont);
 assertEquals(initialFont, dialog.initialFont);
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.dispose();
 assertNull(dialog.initialFont);
 }

 @Test
 public void testResetMethod() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", true, chooserPane, null, null);
 dialog.setVisible(true);
 dialog.reset();
 dialog.dispose();
 }

 @Test
 public void testOKButtonAction() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", true, chooserPane, e -> {}, e -> {});
 dialog.setVisible(true);
 dialog.dispose();
 }

 @Test(expected = NullPointerException.class)
 public void testNullComponentException() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, chooserPane, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitleException() {
 FontChooserDialog dialog = new FontChooserDialog(frame, null, true, chooserPane, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullModalException() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", false, chooserPane, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullChooserPaneException() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullOKListenerException() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", true, chooserPane, null, null);
 ActionListener okListener = null;
 dialog.setOKListener(okListener);
 }

 @Test(expected = NullPointerException.class)
 public void testNullCancelListenerException() {
 FontChooserDialog dialog = new FontChooserDialog(frame, "Test Dialog", true, chooserPane, null, null);
 ActionListener cancelListener = null;
 dialog.setCancelListener(cancelListener);
 }

 @After
 public void tearDown() {
 if (frame != null) {
 frame.dispose();
 }
 }

}