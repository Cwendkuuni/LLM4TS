package osa.ora.server.client.ui.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FontChooserDialogTest {

 private JFrame frame;
 private Component parentComponent;
 private FontChooserDialog fontChooserDialog;
 private ActionListener okListener;
 private ActionListener cancelListener;

 @Before
 public void setUp() {
 frame = new JFrame();
 parentComponent = new JButton();
 okListener = e -> {};
 cancelListener = e -> {};
 }

 @Test
 public void testFontChooserDialogConstruction() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 assertNotNull(fontChooserDialog);
 }

 @Test
 public void testFontChooserDialogConstructionWithJFontChooser() {
 JFontChooser chooserPane = new JFontChooser();
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 assertNotNull(fontChooserDialog);
 assertSame(chooserPane, fontChooserDialog.chooserPane);
 }

 @Test
 public void testSetVisible() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 assertFalse(fontChooserDialog.isVisible());
 fontChooserDialog.setVisible(true);
 assertTrue(fontChooserDialog.isVisible());
 }

 @Test
 public void testSetFont() {
 JFontChooser chooserPane = new JFontChooser();
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 fontChooserDialog.setVisible(true);
 assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
 }

 @Test
 public void testReset() {
 JFontChooser chooserPane = new JFontChooser();
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 Font initialFont = fontChooserDialog.chooserPane.getFont();
 fontChooserDialog.reset();
 assertNotSame(initialFont, fontChooserDialog.chooserPane.getFont());
 }

 @Test
 public void testActionPerformedWhenOkButtonIsClicked() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "OK");
 okListener.actionPerformed(actionEvent);
 verify(fontChooserDialog, times(1)).setVisible(false);
 }

 @Test
 public void testActionPerformedWhenCancelButtonIsClicked() {
 fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, null, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "CANCEL");
 cancelListener.actionPerformed(actionEvent);
 verify(fontChooserDialog, times(1)).setVisible(false);
 }

 @Test
 public void testActionPerformedWhenResetButtonIsClicked() {
 JFontChooser chooserPane = new JFontChooser();
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 fontChooserDialog.setVisible(true);
 Font initialFont = fontChooserDialog.chooserPane.getFont();
 ActionEvent actionEvent = new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "RESET");
 fontChooserDialog.reset();
 assertNotSame(initialFont, fontChooserDialog.chooserPane.getFont());
 }

 @After
 public void tearDown() {
 if (fontChooserDialog != null) {
 fontChooserDialog.dispose();
 }
 frame.dispose();
 }

}