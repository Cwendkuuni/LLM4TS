package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private Component c;
 private String title;
 private boolean modal;
 private JFontChooser chooserPane;
 private ActionListener okListener;
 private ActionListener cancelListener;
 private FontChooserDialog fontChooserDialog;

 @Before
 public void setUp() {
 c = new JFrame();
 title = "FontChooserDialogTest";
 modal = true;
 chooserPane = new JFontChooser();
 okListener = e -> {};
 cancelListener = e -> {};
 }

 @Test
 public void testFontChooserDialogConstruction() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 assertNotNull(fontChooserDialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 assertNotNull(fontChooserDialog.getInitialFont());
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(false);
 assertNull(fontChooserDialog.getInitialFont());
 }

 @Test
 public void testReset() {
 fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.reset();
 assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.chooserPane.getFont());
 }

 @Test
 public void testOkButtonAction() {
 fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.getOkButton().doClick();
 assertFalse(fontChooserDialog.isVisible());
 }

 @Test
 public void testCancelButtonAction() {
 fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.getCancelButton().doClick();
 assertFalse(fontChooserDialog.isVisible());
 }

 @Test
 public void testOkButtonActionWithOkListener() {
 ActionListener okListenerSpy = e -> {
 System.out.println("Ok button clicked!");
 };
 fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListenerSpy, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.getOkButton().doClick();
 assertTrue(okListenerSpy.isCalled());
 }

 @Test
 public void testCancelButtonActionWithCancelListener() {
 ActionListener cancelListenerSpy = e -> {
 System.out.println("Cancel button clicked!");
 };
 fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListenerSpy);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.getCancelButton().doClick();
 assertTrue(cancelListenerSpy.isCalled());
 }
}