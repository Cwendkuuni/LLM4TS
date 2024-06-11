import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

 private JFrame frame;
 private FontChooserDialog dialog;
 private ActionListener okListener = e -> { /* do nothing */ };
 private ActionListener cancelListener = e -> { /* do nothing */ };

 @Before
 public void setUp() {
 frame = new JFrame();
 dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, new JFontChooser(), okListener, cancelListener);
 }

 @Test
 public void testConstructor() {
 assertNotNull(dialog);
 assertNotNull(dialog.chooserPane);
 assertEquals("OK", dialog.getOkButton().getText());
 assertEquals("Cancel", dialog.getCancelButton().getText());
 assertNull(dialog.getInitialFont());
 }

 @Test
 public void testSetVisible() {
 dialog.setVisible(true);
 assertNotNull(dialog.getInitialFont());
 dialog.setVisible(false);
 dialog.setVisible(true);
 assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
 }

 @Test
 public void testReset() {
 dialog.setVisible(true);
 dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
 dialog.reset();
 assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
 }

 @Test
 public void testActionPerformedOnOKButton() {
 dialog.setVisible(true);
 dialog.getOkButton().doClick();
 assertTrue(dialog.isVisible());
 }

 @Test
 public void testActionPerformedOnCancelButton() {
 dialog.setVisible(true);
 dialog.getCancelButton().doClick();
 assertFalse(dialog.isVisible());
 }

 @After
 public void tearDown() {
 if (dialog != null) {
 dialog.dispose();
 }
 if (frame != null) {
 frame.dispose();
 }
 }
}