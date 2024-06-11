import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, chooserPane, okListener, cancelListener);
        assertNotNull(dialog);
    }

    @Test
    public void testSetVisibleTrue() {
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
    }

    @Test
    public void testSetVisibleFalse() {
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
        dialog.setVisible(true);
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
        dialog.setVisible(true);
        dialog.chooserPane.setFont(new Font("Serif", Font.BOLD, 14));
        dialog.reset();
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogNullComponent() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, new JFontChooser(), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogNullTitle() {
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), null, true, new JFontChooser(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFontChooserDialogModalFalseAndListenerNull() {
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", false, new JFontChooser(), null, null);
    }

    @Test
    public void testFontChooserDialogListenersNull() {
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
        dialog.setVisible(true);
        dialog.setVisible(false);
    }

    @Test
    public void testFontChooserDialogOkButtonActionPerformed() {
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.okButton.doClick();
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testFontChooserDialogCancelButtonActionPerformed() {
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testFontChooserDialogResetButtonActionPerformed() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.reset();
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    @Test
    public void testFontChooserDialogKeyListenerCancelKey() {
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.dispose();
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ESCAPE);
        assertFalse(dialog.isDisplayable());
    }
}