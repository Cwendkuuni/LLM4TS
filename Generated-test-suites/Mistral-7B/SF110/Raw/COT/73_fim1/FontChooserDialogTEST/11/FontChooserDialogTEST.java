import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import java.awt.Font;

public class FontChooserDialogTest {

 private Font initialFont;
 private JFontChooser chooserPane;

 @Test
 public void testFontChooserDialogConstructor() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 assertNotNull(dialog);
 }

 @Test
 public void testSetVisibleMethod() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 initialFont = dialog.chooserPane.getFont();
 dialog.setVisible(true);
 assertNotNull(dialog.initialFont);
 dialog.setVisible(false);
 }

 @Test
 public void testResetMethod() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertEquals(initialFont, dialog.chooserPane.getFont());
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogNullComponent() {
 new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 }

 @Test
 public void testFontChooserDialogWithNullOkListener() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.okButton.doClick();
 dialog.setVisible(false);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogNullCancelListener() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 dialog.setVisible(true);
 dialog.cancelButton.doClick();
 dialog.setVisible(false);
 }

 @Test
 public void testFontChooserDialogWithInvalidFont() {
 FontChooserDialog dialog = new FontChooserDialog(null, "Test Dialog", true, null, null, null);
 dialog.chooserPane.setFont(null);
 dialog.setVisible(true);
 dialog.okButton.doClick();
 dialog.setVisible(false);
 assertEquals(null, dialog.initialFont);
 }

}