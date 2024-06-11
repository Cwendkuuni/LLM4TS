import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FontChooserDialogTest {

 private JFrame frame;
 private Component parentComponent;
 private FontChooserDialog fontChooserDialog;

 @Before
 public void setUp() {
 parentComponent = new JFrame();
 frame = new JFrame();
 }

 @Test
 public void testFontChooserDialogConstruction() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, new JFontChooser(), null, null);
 Assert.assertNotNull(fontChooserDialog);
 }

 @Test
 public void testSetVisibleWithInitialFont() {
 JFontChooser chooserPane = new JFontChooser();
 Font initialFont = new Font("Arial", Font.PLAIN, 12);
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, chooserPane, null, null);
 fontChooserDialog.setVisible(true);
 Assert.assertEquals(initialFont, fontChooserDialog.getInitialFont());
 }

 @Test
 public void testSetVisibleWithoutInitialFont() {
 JFontChooser chooserPane = new JFontChooser();
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, chooserPane, null, null);
 fontChooserDialog.setVisible(false);
 Assert.assertNull(fontChooserDialog.getInitialFont());
 }

 @Test
 public void testOkButtonActionPerformed() {
 JFontChooser chooserPane = new JFontChooser();
 ActionListener okListener = e -> System.out.println("OK button clicked");
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, chooserPane, okListener, null);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.getOkButton().doClick();
 }

 @Test
 public void testCancelButtonActionPerformed() {
 JFontChooser chooserPane = new JFontChooser();
 ActionListener cancelListener = e -> System.out.println("Cancel button clicked");
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, chooserPane, null, cancelListener);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.getCancelButton().doClick();
 }

 @Test
 public void testResetButtonActionPerformed() {
 JFontChooser chooserPane = new JFontChooser();
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, chooserPane, null, null);
 fontChooserDialog.setVisible(true);
 fontChooserDialog.getResetButton().doClick();
 }

 @Test
 public void testWindowClosing() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, new JFontChooser(), null, null);
 fontChooserDialog.setVisible(true);
 FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
 fontChooserDialog.addWindowListener(closer);
 KeyEvent keyEvent = new KeyEvent(fontChooserDialog, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ESCAPE, 0);
 fontChooserDialog.dispatchEvent(keyEvent);
 Assert.assertEquals(true, closer.wasInvoked());
 }

 @Test
 public void testComponentHidden() {
 FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, "Test Font Chooser Dialog", true, new JFontChooser(), null, null);
 fontChooserDialog.setVisible(true);
 FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
 fontChooserDialog.addComponentListener(disposeOnClose);
 fontChooserDialog.dispose();
 Assert.assertEquals(true, disposeOnClose.wasInvoked());
 }

 @After
 public void tearDown() {
 if (parentComponent != null) {
 parentComponent.dispose();
 }
 if (frame != null) {
 frame.dispose();
 }
 }

}