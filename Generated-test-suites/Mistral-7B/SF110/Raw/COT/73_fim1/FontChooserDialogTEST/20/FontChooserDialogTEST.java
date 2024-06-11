import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import javax.swing.JFrame;
import javax.swing.JFontChooser;
import java.awt.Font;

public class FontChooserDialogTest {

 private Font initialFont;
 private JFontChooser chooserPane;

 @Test
 public void testFontChooserDialogConstructor() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
 assertNotNull(fontChookerDialog);
 }

 @Test
 public void testSetVisibleMethod() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
 fontChooserDialog.setVisible(true);
 initialFont = fontChooserDialog.chooserPane.getFont();
 fontChooserDialog.setVisible(false);
 }

 @Test
 public void testResetMethod() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
 fontChooserDialog.setVisible(true);
 initialFont = fontChooserDialog.chooserPane.getFont();
 fontChooserDialog.reset();
 assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
 fontChooserDialog.setVisible(false);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullComponent() {
 new FontChooserDialog(null, "Test Title", true, new JFontChooser(), null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullTitle() {
 new FontChooserDialog(new JFrame(), null, true, new JFontChooser(), null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullChooserPane() {
 new FontChooserDialog(new JFrame(), "Test Title", true, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullOkListener() {
 new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testFontChooserDialogConstructorNullCancelListener() {
 new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
 }

 @Test
 public void testWindowEventWindowClosing() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
 fontChooserDialog.setVisible(true);
 final FontChooserDialog.Closer closer = fontChooserDialog.new Closer();
 fontChooserDialog.addWindowListener(closer);
 fontChooserDialog.dispatchEvent(new WindowEvent(fontChooserDialog, WindowEvent.WINDOW_CLOSING));
 assertFalse(fontChooserDialog.isVisible());
 }

 @Test
 public void testComponentEventComponentHidden() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
 fontChooserDialog.setVisible(true);
 final FontChooserDialog.DisposeOnClose disposeOnClose = fontChooserDialog.new DisposeOnClose();
 fontChooserDialog.addComponentListener(disposeOnClose);
 fontChooserDialog.dispatchEvent(new ComponentEvent(fontChooserDialog, ComponentEvent.COMPONENT_HIDDEN));
 assertFalse(fontChooserDialog.isDisplayable());
 }
}