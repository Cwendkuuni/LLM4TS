package osa.ora.server.client.ui.utils.test;

import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.*;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(new JFrame()), null, null);
        assertNotNull(new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(new JFrame()), null, null).getContentPane());
    }

    @Test
    public void testSetVisible() {
        final FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(new JFrame()), null, null);
        dialog.setVisible(true);
        assertNotNull(dialog.getContentPane());
        dialog.setVisible(false);
        assertNull(dialog.getContentPane());
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testReset() {
        final FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(new JFrame()), null, null);
        dialog.chooserPane.setFont(new Font("Test Font", Font.BOLD, 12));
        dialog.reset();
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    // Add test cases for other methods and edge cases

    @Test
    public void testOkButton() {
        // Add test case for ok button functionality
    }

    @Test
    public void testCancelButton() {
        // Add test case for cancel button functionality
    }

    @Test
    public void testInputMap() {
        // Add test case for input map functionality
    }

    @Test
    public void testActionMap() {
        // Add test case for action map functionality
    }

    @Test
    public void testComponentHidden() {
        // Add test case for componentHidden event
    }

    @Test
    public void testWindowClosing() {
        // Add test case for windowClosing event
    }
}