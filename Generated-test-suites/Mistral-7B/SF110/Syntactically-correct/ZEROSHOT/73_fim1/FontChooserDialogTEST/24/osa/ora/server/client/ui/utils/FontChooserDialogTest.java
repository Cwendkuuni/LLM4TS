package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private JFrame frame;
 private FontChooserDialog dialog;

 @Before
 public void setUp() {
 JVMArguments.setSecurityManager(new SecurityManager());
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(300, 200);
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testFontChooserDialogConstruction() {
 // Test constructor with all arguments
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 JFontChooser chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertSame(chooserPane, dialog.getChooserPane());
 assertNull(dialog.getOkListener());
 assertNull(dialog.getCancelListener());
 }

 @Test
 public void testFontChooserDialogConstructionWithoutOkListener() {
 // Test constructor without okListener argument
 ActionListener cancelListener = e -> {};
 JFontChooser chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, cancelListener);
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertSame(chooserPane, dialog.getChooserPane());
 assertNull(dialog.getOkListener());
 }

 @Test
 public void testFontChooserDialogConstructionWithoutCancelListener() {
 // Test constructor without cancelListener argument
 ActionListener okListener = e -> {};
 JFontChooser chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener);
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertSame(chooserPane, dialog.getChooserPane());
 assertNull(dialog.getCancelListener());
 }

 @Test
 public void testFontChooserDialogConstructionWithoutBothListeners() {
 // Test constructor without okListener and cancelListener arguments
 JFontChooser chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane);
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertSame(chooserPane, dialog.getChooserPane());
 assertNull(dialog.getOkListener());
 assertNull(dialog.getCancelListener());
 }

 @Test
 public void testSetVisible() {
 // Test initialFont is set correctly when dialog is made visible
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 JFontChooser chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.setVisible(false);
 dialog.setVisible(true);
 assertEquals(dialog.getChooserPane().getFont(), dialog.initialFont);
 }

 @Test
 public void testReset() {
 // Test that dialog font is reset to initialFont
 ActionListener okListener = e -> {};
 ActionListener cancelListener = e -> {};
 JFontChooser chooserPane = new JFontChooser();
 Font initialFont = new Font("Arial", Font.PLAIN, 12);
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);
 dialog.reset();
 assertSame(dialog.getChooserPane().getFont(), initialFont);
 }

 @Test
 public void testWindowEvents() {
 // Test closing and hiding events
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, new JFontChooser());
 dialog.setVisible(true);
 MouseEvent mouseEvent = new MouseEvent(new Object(), 1, 1, 1, 1, 1, 1, false);
 dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
 dialog.dispatchEvent(new ComponentEvent(dialog, ComponentEvent.COMPONENT_HIDDEN));
 assertTrue(dialog.isDisposed());
 }
}