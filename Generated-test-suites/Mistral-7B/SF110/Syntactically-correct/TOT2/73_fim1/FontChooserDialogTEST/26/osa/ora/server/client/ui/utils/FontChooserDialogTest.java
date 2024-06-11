package osa.ora.server.client.ui.utils;

import org.junit.*;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        new FontChooserDialog(null, "Title", true, null, null, null);
    }

    @Test
    public void testSetVisible() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Title", true, null, null, null);
        dialog.setVisible(true);
        dialog.setVisible(false);
    }

    @Test
    public void testReset() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Title", true, null, null, null);
        dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 20));
        dialog.reset();
        assertEquals("Initial font not set correctly", dialog.chooserPane.getFont(), dialog.initialFont);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullComponent() {
        new FontChooserDialog(null, "Title", true, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithEmptyTitle() {
        new FontChooserDialog(null, "", true, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullChooserPane() {
        new FontChooserDialog(null, "Title", true, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullOkListener() {
        new FontChooserDialog(null, "Title", true, new JFontChooser(), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullCancelListener() {
        new FontChooserDialog(null, "Title", true, new JFontChooser(), null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    @Test
    public void testResetWithNullInitialFont() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Title", true, null, null, null);
        dialog.reset();
        dialog.chooserPane.setFont(null);
        dialog.reset();
        assertNull("Initial font not set to null", dialog.initialFont);
    }

    @Test
    public void testOkButtonActionPerformedWithNullOkListener() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Title", true, null, null, null);
        ActionEvent e = new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "OK");
        dialog.okButton.dispatchEvent(e);
        assertTrue("Dialog not hidden", dialog.isVisible());
    }

    @Test
    public void testCancelButtonActionPerformedWithNullCancelListener() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Title", true, null, null, null);
        ActionEvent e = new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "Cancel");
        dialog.cancelButton.dispatchEvent(e);
        assertTrue("Dialog not hidden", dialog.isVisible());
    }
}