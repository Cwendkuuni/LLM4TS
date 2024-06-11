import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import javax.swing.JFontChooser;

public class FontChooserDialogTest {

 private Font initialFont;
 private JFontChooser chooserPane;

 @Test
 public void testFontChooserDialogConstructor() {
 FontChooserDialog fontDialog = new FontChooserDialog(null, "Font Dialog", true, new JFontChooser(), null, null);
 assertNotNull(fontDialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 initialFont = new Font("Arial", Font.PLAIN, 12);
 FontChooserDialog fontDialog = new FontChooserDialog(null, "Font Dialog", true, new JFontChooser(), null, null);
 fontDialog.setVisible(true);
 assertTrue(fontDialog.isVisible());
 assertSame(initialFont, fontDialog.chooserPane.getFont());
 }

 @Test
 public void testOkButtonActionPerformed() {
 FontChooserDialog fontDialog = new FontChooserDialog(null, "Font Dialog", true, new JFontChooser(), null, null);
 fontDialog.setVisible(true);
 fontDialog.okButton.doClick();
 assertFalse(fontDialog.isVisible());
 }

 @Test
 public void testCancelButtonActionPerformed() {
 FontChooserDialog fontDialog = new FontChooserDialog(null, "Font Dialog", true, new JFontChooser(), null, null);
 fontDialog.setVisible(true);
 fontDialog.cancelButton.doClick();
 assertFalse(fontDialog.isVisible());
 }

 @Test
 public void testResetButtonActionPerformed() {
 FontChooserDialog fontDialog = new FontChooserDialog(null, "Font Dialog", true, new JFontChooser(), null, null);
 fontDialog.setVisible(true);
 fontDialog.resetButton.doClick();
 assertSame(initialFont, fontDialog.chooserPane.getFont());
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullComponent() {
 new FontChooserDialog(null, "Font Dialog", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullTitle() {
 new FontChooserDialog(null, null, true, new JFontChooser(), null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullOkListener() {
 new FontChooserDialog(null, "Font Dialog", true, new JFontChooser(), null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullCancelListener() {
 new FontChooserDialog(null, "Font Dialog", true, new JFontChooser(), null, null);
 }

 @Test
 public void testSetVisibleWithNullVisible() {
 final FontChooserDialog fontDialog = new FontChooserDialog(null, "Font Dialog", true, new JFontChooser(), null, null);
 fontDialog.setVisible(null);
 assertFalse(fontDialog.isVisible());
 }

 @Test
 public void testResetIfDialogIsNotVisible() {
 FontChooserDialog fontDialog = new FontChooserDialog(null, "Font Dialog", true, new JFontChooser(), null, null);
 fontDialog.reset();
 assertSame(initialFont, fontDialog.chooserPane.getFont());
 fontDialog.setVisible(true);
 fontDialog.reset();
 assertNotSame(initialFont, fontDialog.chooserPane.getFont());
 }
}