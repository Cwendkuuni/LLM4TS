package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFrame frame;

 @Before
 public void setUp() {
 final Component c = new JFrame();
 frame = new JFrame();
 dialog = new FontChooserDialog(c, "Test Dialog", true, null, null, null);
 }

 @Test
 public void testFontChooserDialogConstructor() {
 assertNotNull(dialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 dialog.setVisible(false);
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 dialog.setVisible(false);
 dialog.setVisible(true);
 assertNull(dialog.initialFont);
 dialog.setVisible(false);
 }

 @Test
 public void testReset() {
 dialog.setVisible(true);
 dialog.initialFont = new Font("Arial", Font.BOLD, 12);
 dialog.reset();
 assertNotNull(dialog.initialFont);
 assertFalse(dialog.chooserPane.getFont().isBold());
 dialog.setVisible(false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullComponentInConstructor() {
 dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitleInConstructor() {
 dialog = new FontChooserDialog(frame, null, true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullModalFlagInConstructor() {
 dialog = new FontChooserDialog(frame, "Test Dialog", false, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullChooserPaneInConstructor() {
 dialog = new FontChooserDialog(frame, "Test Dialog", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullOKListenerInConstructor() {
 dialog = new FontChooserDialog(frame, "Test Dialog", true, new JFontChooser(), null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullCancelListenerInConstructor() {
 dialog = new FontChooserDialog(frame, "Test Dialog", true, new JFontChooser(), null, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Empty implementation
 }
 });
 }

 @Test
 public void testOKButtonAction() {
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertTrue(dialog.isVisible()); // Check if dialog can be shown again
 }

 @Test
 public void testCancelButtonAction() {
 dialog.setVisible(true);
 dialog.setVisible(false);
 assertTrue(dialog.isVisible()); // Check if dialog can be shown again
 }

 @Test
 public void testResetButtonAction() {
 dialog.setVisible(true);
 dialog.initialFont = new Font("Arial", Font.BOLD, 12);
 dialog.reset();
 assertNotNull(dialog.initialFont);
 assertFalse(dialog.chooserPane.getFont().isBold());
 dialog.setVisible(false);
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 assertFalse(dialog.chooserPane.getFont().isBold());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeComponentXInSetLocationRelativeTo() {
 final Component c = new JFrame();
 dialog = new FontChooserDialog(c, "Test Dialog", true, null, null, null);
 dialog.setLocationRelativeTo(new Point(-10, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeComponentYInSetLocationRelativeTo() {
 final Component c = new JFrame();
 dialog = new FontChooserDialog(c, "Test Dialog", true, null, null, null);
 dialog.setLocationRelativeTo(new Point(0, -10));
 }

 @Test
 public void testOkButtonKeyBindings() {
 final Container contentPane = dialog.getContentPane();
 final JButton okButton = (JButton)contentPane.getComponent(0);
 final KeyStroke okKeyStroke = KeyStroke.getKeyStroke("repressed VK_ENTER");
 final ActionMap actionMap = okButton.getActionMap();
 final Object okAction = actionMap.get("OK");
 final ActionEvent okEvent = new ActionEvent(okButton, ActionEvent.ACTION_PERFORMED, "OK");
 assertEquals(okAction, okEvent.getSource());
 }

 @Test
 public void testCancelButtonKeyBindings() {
 final Container contentPane = dialog.getContentPane();
 final JButton cancelButton = (JButton)contentPane.getComponent(1);
 final KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke("repressed VK_ESCAPE");
 final ActionMap actionMap = cancelButton.getActionMap();
 final Object cancelAction = actionMap.get("cancel");
 final ActionEvent cancelEvent = new ActionEvent(cancelButton, ActionEvent.ACTION_PERFORMED, "cancel");
 assertEquals(cancelAction, cancelEvent.getSource());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullOKActionListener() {
 dialog.setVisible(true);
 dialog.addOKListener(null);
 dialog.setVisible(false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCancelActionListener() {
 dialog.setVisible(true);
 dialog.addCancelListener(null);
 dialog.setVisible(false);
 }

 @Test
 public void testCloseDialogOnWindowClosing() {
 final FrameCloser closer = spy(new FontChooserDialog.Closer());
 dialog.addWindowListener(closer);
 dialog.setVisible(true);
 dialog.dispose();
 verify(closer).windowClosing(argThat(equalTo(dialog)));
 }

 @Test
 public void testDisposeDialogOnComponentHidden() {
 final FrameDisposeOnClose disposeOnClose = spy(new FontChooserDialog.DisposeOnClose());
 dialog.addComponentListener(disposeOnClose);
 dialog.setVisible(true);
 dialog.dispose();
 verify(disposeOnClose).componentHidden(argThat(equalTo(dialog)));
 }
}