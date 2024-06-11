import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import javax.swing.JFontChooser;

public class FontChooserDialogTest {

 // Test constructor
 @Test
 public void testFontChooserDialogConstructor() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 assertNotNull(fontChooserDialog);
 }

 // Test setVisible method with null Component
 @Test(expected = NullPointerException.class)
 public void testSetVisibleWithNullComponent() {
 new FontChooserDialog(null, "Font Chooser Dialog", true, new JFontChooser(), null, null).setVisible(true);
 }

 // Test setVisible method with valid Component
 @Test
 public void testSetVisibleWithValidComponent() {
 Component c = new JFrame();
 FontChooserDialog fontChooserDialog = new FontChooserDialog(c, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 fontChooserDialog.setVisible(true);
 }

 // Test reset method
 @Test
 public void testReset() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.reset();
 assertNotNull(fontChooserDialog.chooserPane);
 assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
 }

 // Test edge case: Call reset method before setting visible
 @Test
 public void testResetBeforeSetVisible() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 fontChooserDialog.reset();
 fontChooserDialog.setVisible(true);
 }

 // Test exception handling: Null ActionListener
 @Test(expected = NullPointerException.class)
 public void testNullOkListener() {
 new FontChooserDialog(null, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 }

 // Test exception handling: Null CancelListener
 @Test(expected = NullPointerException.class)
 public void testNullCancelListener() {
 new FontChooserDialog(null, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 }

 // Test edge case: Empty ButtonPane
 @Test
 public void testEmptyButtonPane() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Font Chooser Dialog", true, new JFontChooser(), null, null);
 JPanel buttonPane = fontChooserDialog.getContentPane().getComponent(1);
 assertTrue(buttonPane.getComponentCount() > 0);
 }

 // Test exception handling: Null initialFont
 @Test(expected = NullPointerException.class)
 public void testNullInitialFont() {
 new FontChooserDialog(null, "Font Chooser Dialog", true, new JFontChooser(), null, null).reset();
 }

}