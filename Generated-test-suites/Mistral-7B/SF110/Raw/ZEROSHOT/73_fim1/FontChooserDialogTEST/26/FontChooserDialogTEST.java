package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Test;

public class FontChooserDialogTest {

 private Component testComponent;
 private FontChooserDialog fontChooserDialog;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Test
 public void testFontChooserDialogConstruction() {
  testComponent = new JFrame();
  okListener = e -> {};
  cancelListener = e -> {};

  fontChooserDialog = new FontChooserDialog(testComponent, "FontChooserDialog", true, new JFontChooser(), okListener, cancelListener);

  assertTrue(fontChooserDialog.isModal());
  assertEquals("FontChooserDialog", fontChooserDialog.getTitle());
  assertNotNull(fontChooserDialog.getOkButton());
  assertNotNull(fontChooserDialog.getCancelButton());
 }

 @Test
 public void testFontChooserDialogSetVisible() {
  testComponent = new JFrame();
  okListener = e -> {};
  cancelListener = e -> {};

  fontChooserDialog = new FontChooserDialog(testComponent, "FontChooserDialog", true, new JFontChooser(), okListener, cancelListener);

  // Initial font should be null
  assertNull(fontChooserDialog.getInitialFont());

  fontChooserDialog.setVisible(true);

  // After setVisible, initial font should be set
  assertTrue(fontChooserDialog.isVisible());
  assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.chooserPane.getFont());

  fontChooserDialog.reset();

  // After reset, initial font should be set back to the original value
  assertEquals(null, fontChooserDialog.getInitialFont());
  assertNotEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.chooserPane.getFont());
 }

 @Test
 public void testFontChooserDialogActionListeners() {
  testComponent = new JFrame();
  okListener = e -> {};
  cancelListener = e -> {};

  fontChooserDialog = new FontChooserDialog(testComponent, "FontChooserDialog", true, new JFontChooser(), okListener, cancelListener);

  // Test OK button listener
  ActionEvent okEvent = new ActionEvent(fontChooserDialog.getOkButton(), ActionEvent.ACTION_PERFORMED, "OK");
  okListener.actionPerformed(okEvent);
  assertTrue(fontChooserDialog.isVisible());

  // Test Cancel button listener
  ActionEvent cancelEvent = new ActionEvent(fontChooserDialog.getCancelButton(), ActionEvent.ACTION_PERFORMED, "Cancel");
  cancelListener.actionPerformed(cancelEvent);
  assertFalse(fontChooserDialog.isVisible());
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogNullComponents() {
  JFrame testFrame = null;
  okListener = e -> {};
  cancelListener = e -> {};

  try {
   fontChooserDialog = new FontChooserDialog(testFrame, "FontChooserDialog", true, null, okListener, cancelListener);
  } catch (NullPointerException e) {
   assertEquals("testComponent is null", e.getMessage());
  }
 }

 @Test
 public void testFontChooserDialogCustomListeners() {
  testComponent = new JFrame();
  okListener = e -> {};
  FontChooserDialog fontChooserDialog = new FontChooserDialog(testComponent, "FontChooserDialog", true, new JFontChooser(), okListener, null);

  ActionEvent okEvent = new ActionEvent(fontChooserDialog.getOkButton(), ActionEvent.ACTION_PERFORMED, "OK");
  ActionEvent cancelEvent = new ActionEvent(fontChooserDialog.getCancelButton(), ActionEvent.ACTION_PERFORMED, "Cancel");

  ActionListener customOkListener = e -> System.out.println("OK button clicked");
  ActionListener customCancelListener = e -> System.out.println("Cancel button clicked");

  fontChooserDialog.setOkListener(customOkListener);
  fontChooserDialog.setCancelListener(customCancelListener);

  fontChooserDialog.setVisible(true);

  okEvent = new ActionEvent(fontChooserDialog.getOkButton(), ActionEvent.ACTION_PERFORMED, "OK");
  okListener.actionPerformed(okEvent);
  assertTrue(fontChooserDialog.isVisible());
  System.out.println("OK button clicked");

  cancelEvent = new ActionEvent(fontChooserDialog.getCancelButton(), ActionEvent.ACTION_PERFORMED, "Cancel");
  cancelListener.actionPerformed(cancelEvent);
  assertFalse(fontChooserDialog.isVisible());
  System.out.println("Cancel button clicked");

  fontChooserDialog.dispose();
 }

}