package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.swing.JComponent;
import javax.swing.JFontChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.junit.Before;
import org.junit.Test;

public class FontChooserDialogTest {

 private JFrame frame;
 private JComponent parentComponent;
 private FontChooserDialog dialog;
 private JFontChooser chooserPane;

 @Before
 public void setUp() {
 frame = mock(JFrame.class);
 parentComponent = mock(JComponent.class);
 chooserPane = mock(JFontChooser.class);
 }

 @Test
 public void testFontChooserDialogCreation() {
 // Given
 String title = "Font Chooser Dialog";
 boolean modal = true;
 ActionListener okListener = mock(ActionListener.class);
 ActionListener cancelListener = mock(ActionListener.class);

 // When
 dialog = new FontChooserDialog(parentComponent, title, modal, chooserPane, okListener, cancelListener);

 // Then
 assertNotNull(dialog);
 assertEquals(title, dialog.getTitle());
 assertNotNull(dialog.getContentPane());
 assertEquals("Center", dialog.getContentPane().getLayout().getName());
 assertNull(dialog.getInitialFont());
 }

 @Test
 public void testFontChooserDialogSetVisible() {
 // Given
 String title = "Font Chooser Dialog";
 boolean modal = true;
 ActionListener okListener = mock(ActionListener.class);
 ActionListener cancelListener = mock(ActionListener.class);
 JFontChooser chooserPane = mock(JFontChooser.class);
 dialog = new FontChooserDialog(parentComponent, title, modal, chooserPane, okListener, cancelListener);
 Font initialFont = new Font("Arial", Font.BOLD, 12);

 // When
 dialog.setVisible(true);
 dialog.setVisible(false);

 // Then
 verify(dialog).reset();
 assertNotNull(dialog.getInitialFont());
 assertEquals(initialFont, dialog.getInitialFont());
 }

 @Test
 public void testFontChooserDialogReset() {
 // Given
 String title = "Font Chooser Dialog";
 boolean modal = true;
 ActionListener okListener = mock(ActionListener.class);
 ActionListener cancelListener = mock(ActionListener.class);
 JFontChooser chooserPane = mock(JFontChooser.class);
 dialog = new FontChooserDialog(parentComponent, title, modal, chooserPane, okListener, cancelListener);
 Font initialFont = new Font("Arial", Font.BOLD, 12);
 dialog.setVisible(true);
 dialog.setVisible(false);

 // When
 dialog.reset();

 // Then
 verify(chooserPane).setFont(initialFont);
 }

 @Test
 public void testFontChooserDialogOKButtonAction() {
 // Given
 String title = "Font Chooser Dialog";
 boolean modal = true;
 ActionListener okListener = mock(ActionListener.class);
 ActionListener cancelListener = mock(ActionListener.class);
 JFontChooser chooserPane = mock(JFontChooser.class);
 dialog = new FontChooserDialog(parentComponent, title, modal, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);

 // When
 dialog.getOkButton().doClick();

 // Then
 verify(okListener).actionPerformed(mock(ActionEvent.class));
 verify(dialog).dispose();
 }

 @Test
 public void testFontChooserDialogCancelButtonAction() {
 // Given
 String title = "Font Chooser Dialog";
 boolean modal = true;
 ActionListener okListener = mock(ActionListener.class);
 ActionListener cancelListener = mock(ActionListener.class);
 JFontChooser chooserPane = mock(JFontChooser.class);
 dialog = new FontChooserDialog(parentComponent, title, modal, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);

 // When
 dialog.getCancelButton().doClick();

 // Then
 verify(cancelListener).actionPerformed(mock(ActionEvent.class));
 verify(dialog).dispose();
 }

 @Test
 public void testFontChooserDialogEscKeyAction() {
 // Given
 String title = "Font Chooser Dialog";
 boolean modal = true;
 ActionListener okListener = mock(ActionListener.class);
 ActionListener cancelListener = mock(ActionListener.class);
 JFontChooser chooserPane = mock(JFontChooser.class);
 dialog = new FontChooserDialog(parentComponent, title, modal, chooserPane, okListener, cancelListener);
 dialog.setVisible(true);

 // When
 dialog.getContentPane().requestFocus();
 dialog.getContentPane().dispatchEvent(new KeyEvent(dialog, 27, true, false));

 // Then
 verify(cancelListener).actionPerformed(mock(ActionEvent.class));
 verify(dialog).dispose();
 }
}