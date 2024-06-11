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
 private FontChooserDialog fontChooserDialog;

 @Before
 public void setUp() {
 parentComponent = new JFrame();
 frame = new JFrame();
 }

 @Test
 public void testFontChooserDialogConstruction() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 assertNotNull(dialog);
 assertTrue(dialog.isModal());
 }

 @Test
 public void testFontChooserDialogSetContent() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 Container contentPane = dialog.getContentPane();
 JComponent chooserPane = dialog.getChooserPane();
 assertEquals(BorderLayout.CENTER, contentPane.getLayout());
 assertEquals(chooserPane, contentPane.getComponent(0));
 }

 @Test
 public void testFontChooserDialogSetButtons() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 JComponent buttonPane = dialog.getButtonPane();
 assertEquals(FlowLayout.LEFT, buttonPane.getLayout());
 assertNotNull(dialog.getOKButton());
 assertNotNull(dialog.getCancelButton());
 assertNotNull(dialog.getResetButton());
 }

 @Test
 public void testFontChooserDialogSetListeners() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 ActionListener okListener = mock(ActionListener.class);
 ActionListener cancelListener = mock(ActionListener.class);
 dialog.setOKListener(okListener);
 dialog.setCancelListener(cancelListener);
 verify(okListener, times(0)).actionPerformed(any(ActionEvent.class));
 verify(cancelListener, times(0)).actionPerformed(any(ActionEvent.class));
 dialog.getOKButton().doClick();
 verify(okListener, times(1)).actionPerformed(any(ActionEvent.class));
 verify(cancelListener, times(1)).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testFontChooserDialogSetFont() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 dialog.setFont(initialFont);
 assertEquals(initialFont, dialog.getChooserPane().getFont());
 }

 @Test
 public void testFontChooserDialogGetInitialFont() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 dialog.setVisible(true);
 Font initialFont = dialog.getChooserPane().getFont();
 dialog.dispose();
 assertEquals(initialFont, dialog.getInitialFont());
 }

 @Test
 public void testFontChooserDialogReset() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 dialog.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertEquals(dialog.getInitialFont(), dialog.getChooserPane().getFont());
 }

 @After
 public void tearDown() {
 if (frame != null) {
 frame.dispose();
 }
 if (parentComponent != null) {
 parentComponent.dispose();
 }
 }

}