import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class FontChooserDialogTest {

    @Test
    public void testFontChooserDialog() {
        JFrame frame = new JFrame();
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(frame, "Test Dialog", true, chooserPane, okListener, cancelListener);

        // Check if dialog is displayed and components are added
        assertTrue(fontChooserDialog.isDisplayable());
        assertEquals(2, fontChooserDialog.getContentPane().getComponentCount());

        // Test for null Component parameter
        Component nullComponent = null;
        try {
            new FontChooserDialog(nullComponent, "Test Dialog", true, chooserPane, okListener, cancelListener);
            fail("Exception should be thrown");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test for empty title string
        String emptyTitle = "";
        try {
            new FontChooserDialog(frame, emptyTitle, true, chooserPane, okListener, cancelListener);
            fail("Exception should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for missing okListener and cancelListener
        try {
            new FontChooserDialog(frame, "Test Dialog", true, chooserPane, null, null);
            fail("Exception should be thrown");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test for Modal dialog with non-null Component parent not being a JFrame
        JDialog nonJFrameComponent = new JDialog(new JLabel(), "Test Dialog", false);
        try {
            new FontChooserDialog(nonJFrameComponent, "Test Dialog", true, chooserPane, okListener, cancelListener);
            fail("Exception should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for chooserPane being null
        try {
            new FontChooserDialog(frame, "Test Dialog", true, null, okListener, cancelListener);
            fail("Exception should be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}