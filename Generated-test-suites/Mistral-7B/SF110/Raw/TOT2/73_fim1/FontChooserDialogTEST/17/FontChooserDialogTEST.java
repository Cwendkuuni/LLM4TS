import org.junit.Test;
import org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;

public class FontChooserDialogTest {
    @Test
    public void testFontChooserDialogConstructor() {
        FontChooserDialog dialog = new FontChooserDialog(null, "TestDialog", false, null, null, null);
        assertNotNull(dialog);
    }

    @Test
    public void testFontChooserDialogNullArguments() {
        try {
            new FontChooserDialog(null, null, false, null, null, null);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetVisibleTrue() {
        FontChooserDialog dialog = new FontChooserDialog(null, "TestDialog", false, null, null, null);
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
        dialog.setVisible(false);
    }

    @Test
    public void testSetVisibleFalse() {
        FontChooserDialog dialog = new FontChooserDialog(null, "TestDialog", false, null, null, null);
        dialog.setVisible(true);
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "TestDialog", false, null, null, null);
        dialog.setVisible(true);
        Font initialFont = dialog.chooserPane.getFont();
        dialog.reset();
        assertNotNull(dialog.initialFont);
        assertNotSame(dialog.chooserPane.getFont(), initialFont);
        dialog.setVisible(false);
    }

    @Test
    public void testOkButton() {
        FontChooserDialog dialog = new FontChooserDialog(null, "TestDialog", false, null, null, null);
        dialog.setVisible(true);
        dialog.okButton.doClick();
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testCancelButton() {
        FontChooserDialog dialog = new FontChooserDialog(null, "TestDialog", false, null, null, null);
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testEscapeKey() {
        FontChooserDialog dialog = new FontChooserDialog(null, "TestDialog", false, null, null, null);
        dialog.setVisible(true);
        dialog.getRootPane().requestFocus();
        dialog.getRootPane().dispatchEvent(new KeyEvent(FocusEvent.FOCUS_LOST, KeyEvent.VK_ESCAPE, 0, 0));
        assertFalse(dialog.isVisible());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOkListener() {
        new FontChooserDialog(null, "TestDialog", false, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullCancelListener() {
        new FontChooserDialog(null, "TestDialog", false, null, null, null);
    }

    @Test
    public void testModalDialog() {
        FontChooserDialog dialog1 = new FontChooserDialog(null, "TestDialog1", true, null, null, null);
        dialog1.setVisible(true);
        try {
            new FontChooserDialog(null, "TestDialog2", false, null, null, null).setVisible(true);
            fail("Should have thrown an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testNonModalDialog() {
        FontChooserDialog dialog1 = new FontChooserDialog(null, "TestDialog1", false, null, null, null);
        dialog1.setVisible(true);
        new FontChooserDialog(null, "TestDialog2", false, null, null, null).setVisible(true);
    }
}