package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.*;
import org.junit.experimental.categories.Category;

@Category(integration.class)
public class FontChooserDialogTest {

 private Component parentComponent;
 private JFrame frame;
 private ActionListener okListener;
 private ActionListener cancelListener;
 private JFontChooser chooserPane;

 @Before
 public void setUp() {
 parentComponent = new JFrame();
 frame = new JFrame();
 okListener = mock(ActionListener.class);
 cancelListener = mock(ActionListener.class);
 chooserPane = mock(JFontChooser.class);
 }

 @Test
 public void testFontChooserDialogCreation() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, okListener, cancelListener);
 assertNotNull(dialog);
 }

 @Test
 public void testDefaultButton() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, okListener, cancelListener);
 assertEquals("OK", dialog.getRootPane().getDefaultButton().getText());
 }

 @Test
 public void testCancelButtonLabel() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, okListener, cancelListener);
 assertEquals("Cancel", dialog.getButtonPane().getComponent(1).getText());
 }

 @Test
 public void testOkButtonActionPerformed() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, okListener, cancelListener);
 doCallRealMethod().when(dialog).actionPerformed(any(ActionEvent.class));
 dialog.getOkButton().doClick();
 verify(okListener).actionPerformed(any(ActionEvent.class));
 }

 @Test
 public void testCancelButtonActionPerformed() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, okListener, cancelListener);
 doCallRealMethod().when(dialog).actionPerformed(any(ActionEvent.class));
 dialog.getCancelButton().doClick();
 verifyNoMoreInteractions(okListener);
 }

 @Test
 public void testResetButtonActionPerformed() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, okListener, cancelListener);
 doCallRealMethod().when(dialog).actionPerformed(any(ActionEvent.class));
 dialog.getResetButton().doClick();
 assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
 }

 @Test
 public void testKeyEventOnCancelButton() {
 FontChooserDialog dialog = new FontChooserDialog(parentComponent, "Font Chooser", true, chooserPane, okListener, cancelListener);
 JButton cancelButton = dialog.getButtonPane().getComponent(1);
 cancelButton.requestFocusInWindow();
 KeyEvent keyEvent = new KeyEvent(cancelButton, 1, true, KeyEvent.VK_ESCAPE);
 dialog.dispatchEvent(keyEvent);
 verify(cancelListener).actionPerformed(any(ActionEvent.class));
 }
}