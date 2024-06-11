package osa.ora.server.client.ui.utils;import org.junit.Before;import org.junit.Test;import javax.swing.JFontChooser;import javax.swing.SwingUtilities;

public class FontChooserDialogTest {

 private FontChooserDialog dialog;
 private JFontChooser chooser;

 @Before
 public void setUp() {
 chooser = new JFontChooser();
 dialog = new FontChooserDialog(null, "Font Chooser Dialog", true, chooser, null, null);
 }

 @Test
 public void testConstruction() {
 assertNotNull(dialog);
 assertNotNull(dialog.chooserPane);
 }

 @Test
 public void testSetVisible() {
 Font initialFont = dialog.chooserPane.getFont();
 dialog.setVisible(true);
 assertNotNull(initialFont);
 dialog.setVisible(false);
 }

 @Test
 public void testReset() {
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertNotNull(dialog.initialFont);
 assertSame(dialog.chooserPane.getFont(), dialog.initialFont);
 }

 @Test
 public void testOKButton() {
 Object initialValue = dialog.chooserPane.getFont();
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 dialog.okButton.doClick();
 }
 });
 assertNotNull(initialValue);
 assertNotSame(dialog.chooserPane.getFont(), initialValue);
 }

 @Test
 public void testCancelButton() {
 Object initialValue = dialog.chooserPane.getFont();
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 dialog.cancelButton.doClick();
 }
 });
 assertSame(dialog.chooserPane.getFont(), initialValue);
 }

 @Test
 public void testResetButton() {
 dialog.chooserPane.setFont(new Font("Serif", Font.ITALIC, 14));
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 dialog.resetButton.doClick();
 }
 });
 assertSame(dialog.chooserPane.getFont(), dialog.initialFont);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullComponent() {
 new FontChooserDialog(null, "Font Chooser Dialog", true, chooser, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTitle() {
 new FontChooserDialog(null, null, true, chooser, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullModalFlag() {
 new FontChooserDialog(null, "Font Chooser Dialog", false, chooser, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullChooserPane() {
 new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
 }
}