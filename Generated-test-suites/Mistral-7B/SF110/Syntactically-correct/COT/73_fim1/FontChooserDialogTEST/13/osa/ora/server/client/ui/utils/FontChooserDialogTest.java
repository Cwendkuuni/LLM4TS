package osa.ora.server.client.ui.utils.test;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private Font initialFont;
 private JFontChooser chooserPane;

 @Test
 public void testFontChooserDialogConstructor() {
     Component c = new JFrame();
     String title = "Font Chooser Dialog";
     boolean modal = true;
     JFontChooser mockChooserPane = new JFontChooser();
     ActionListener okListener = e -> {};
     ActionListener cancelListener = e -> {};

     FontChooserDialog dialog = new FontChooserDialog(c, title, modal, mockChooserPane, okListener, cancelListener);

     assertNotNull(dialog);
     assertEquals(title, dialog.getTitle());
     assertTrue(dialog.isModal());
     assertEquals(mockChooserPane, dialog.getChooserPane());
     assertNull(dialog.getInitialFont());
     assertEquals(0, dialog.getWidth());
     assertEquals(0, dialog.getHeight());
 }

 @Test
 public void testSetVisible() {
     Component c = new JFrame();
     String title = "Font Chooser Dialog";
     boolean modal = true;
     JFontChooser chooserPane = new JFontChooser();
     ActionListener okListener = e -> {};
     ActionListener cancelListener = e -> {};

     FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

     dialog.setVisible(true);
     initialFont = dialog.getChooserPane().getFont();
     dialog.setVisible(false);

     assertNotNull(initialFont);
 }

 @Test
 public void testReset() {
     Component c = new JFrame();
     String title = "Font Chooser Dialog";
     boolean modal = true;
     JFontChooser chooserPane = new JFontChooser();
     ActionListener okListener = e -> {};
     ActionListener cancelListener = e -> {};

     FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
     dialog.setVisible(true);
     dialog.reset();

     assertEquals(initialFont, dialog.getChooserPane().getFont());
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullComponent() {
     String title = "Font Chooser Dialog";
     boolean modal = true;
     JFontChooser chooserPane = new JFontChooser();
     ActionListener okListener = e -> {};
     ActionListener cancelListener = e -> {};

     new FontChooserDialog(null, title, modal, chooserPane, okListener, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullTitle() {
     Component c = new JFrame();
     boolean modal = true;
     JFontChooser chooserPane = new JFontChooser();
     ActionListener okListener = e -> {};
     ActionListener cancelListener = e -> {};

     new FontChooserDialog(c, null, modal, chooserPane, okListener, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullChooserPane() {
     Component c = new JFrame();
     String title = "Font Chooser Dialog";
     boolean modal = true;
     ActionListener okListener = e -> {};
     ActionListener cancelListener = e -> {};

     new FontChooserDialog(c, title, modal, null, okListener, cancelListener);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullOkListener() {
     Component c = new JFrame();
     String title = "Font Chooser Dialog";
     boolean modal = true;
     JFontChooser chooserPane = new JFontChooser();
     ActionListener cancelListener = e -> {};

     new FontChooserDialog(c, title, modal, chooserPane, null, cancelListener);
 }

 @Test(expected = NullPointerException. class)
 public void testFontChooserDialogConstructorNullCancelListener() {
     Component c = new JFrame();
     String title = "Font Chooser Dialog";
     boolean modal = true;
     JFontChooser chooserPane = new JFontChooser();
     ActionListener okListener = e -> {};

     new FontChooserDialog(c, title, modal, chooserPane, okListener, null);
 }

}