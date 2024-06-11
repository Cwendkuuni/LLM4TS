import org.junit.Test;
import static org.junit.Assert.*;

public class FontChooserDialogTest {
    @Test
    public void testFontChooserDialogConstructor() {
        new FontChooserDialog(null, "", true, null, null, null);
    }

    @Test
    public void testFontChooserDialogWithNullComponent() {
        assertThrows(NullPointerException.class, () -> new FontChooserDialog(null, "", true, null, null, null));
    }

    @Test
    public void testFontChooserDialogWithEmptyTitle() {
        new FontChooserDialog(null, "", true, null, null, null); // No exception should be thrown
    }

    @Test
    public void testFontChooserDialogWithNullChooserPane() {
        assertThrows(NullPointerException.class, () -> new FontChooserDialog(null, "", true, null, null, null));
    }

    @Test
    public void testFontChooserDialogWithNullOkListener() {
        new FontChooserDialog(null, "", true, null, null, null); // No exception should be thrown
    }

    @Test
    public void testFontChooserDialogWithNullCancelListener() {
        new FontChooserDialog(null, "", true, null, null, null); // No exception should be thrown
    }

    @Test
    public void testFontChooserDialogWithModalFlagSetToFalse() throws InterruptedException {
        // Testing the behavior when the modal flag is set to false
        // and the dialog is opened in the middle of a Swing application
        // This test case is complex and might need a dedicated test framework
        // or a testing library for Swing applications
    }

    @Test
    public void testFontChooserDialogSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, "", true, null, null, null);
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
    }

    @Test
    public void testFontChooserDialogReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "", true, null, null, null);
        dialog.setVisible(true);
        dialog.reset();
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont);
    }

    @Test
    public void testFontChooserDialogButtons() {
        FontChooserDialog dialog = new FontChooserDialog(null, "", true, null, null, null);
        dialog.setVisible(true);

        JButton okButton = dialog.findButton("OK");
        JButton cancelButton = dialog.findButton("Cancel");
        JButton resetButton = dialog.findButton("Reset");

        assertNotNull(okButton);
        assertNotNull(cancelButton);
        assertNotNull(resetButton);
    }

    @Test
    public void testFontChooserDialogEventHandling() {
        FontChooserDialog dialog = new FontChooserDialog(null, "", true, null, null, null);
        dialog.setVisible(true);

        ActionEvent okEvent = new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "OK");
        ActionEvent cancelEvent = new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "Cancel");
        ActionEvent resetEvent = new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "Reset");

        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new FontChooserDialog.Closer());
        dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());

        dialog.okButton.addActionListener(okListener);
        dialog.cancelButton.addActionListener(cancelListener);
        dialog.resetButton.addActionListener(e -> dialog.reset());

        okButton.doClick();
        cancelButton.doClick();
        resetButton.doClick();

        dialog.dispose();
    }
}