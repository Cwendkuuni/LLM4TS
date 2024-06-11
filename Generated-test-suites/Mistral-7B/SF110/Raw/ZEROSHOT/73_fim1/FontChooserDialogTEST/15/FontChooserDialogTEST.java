package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnit4.class)
public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;

 @Before
 public void setUp() {
 JVMSecurityManager securityManager = System.getSecurityManager();
 if (securityManager != null) {
 System.setSecurityManager(null);
 }
 frame = new JFrame();
 }

 @After
 public void tearDown() {
 if (dialog != null) {
 dialog.dispose();
 }
 JVMSecurityManager securityManager = System.getSecurityManager();
 if (securityManager == null) {
 System.setSecurityManager(securityManager);
 }
 }

 @Test
 public void testFontChooserDialogConstruction() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertNull(dialog.getFont());
 }

 @Test
 public void testFontChooserDialogConstructionWithInitialFont() {
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null, initialFont);
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertEquals(initialFont, dialog.getFont());
 }

 @Test
 public void testFontChooserDialogConstructionWithOKListener() {
 ActionListener okListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // TODO add implementation
 }
 };
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, okListener, null);
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertNull(dialog.getFont());
 }

 @Test
 public void testFontChooserDialogConstructionWithCancelListener() {
 ActionListener cancelListener = new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // TODO add implementation
 }
 };
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, cancelListener);
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertNull(dialog.getFont());
 }

 @Test
 public void testFontChooserDialogConstructionWithFontChooser() {
 JFontChooser chooserPane = new JFontChooser();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, chooserPane, null, null);
 assertNotNull(dialog);
 assertEquals("Font Chooser Dialog", dialog.getTitle());
 assertTrue(dialog.isModal());
 assertSame(chooserPane, dialog.getChooserPane());
 assertNull(dialog.getFont());
 }

 @Test
 public void testSetVisible() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertTrue(dialog.isVisible());
 dialog.setVisible(true);
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testReset() {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertNull(dialog.getFont());
 }

 @Test
 public void testOKButtonAction() throws Exception {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.getOkButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testCancelButtonAction() throws Exception {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @Test
 public void testResetButtonAction() throws Exception {
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, null, null, null, new Font("Arial", Font.BOLD, 12));
 dialog.setVisible(true);
 dialog.getResetButton().doClick();
 assertNull(dialog.getFont());
 }
}