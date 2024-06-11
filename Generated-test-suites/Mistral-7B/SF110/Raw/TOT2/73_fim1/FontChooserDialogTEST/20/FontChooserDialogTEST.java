package osa.ora.server.client.ui.utils.tests;

import org.junit.*;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.*;

public class FontChooserDialogTest {
    JFrame frame;
    JFontChooser chooserPane;
    ActionListener okListener = e -> {};
    ActionListener cancelListener = e -> {};

    @Before
    public void setUp() {
        frame = new JFrame();
        chooserPane = new JFontChooser();
    }

    @Test
    public void testFontChooserDialogConstructor() {
        // Test case for the constructor
        new FontChooserDialog(frame, "Test", true, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testSetVisible() {
        // Test case for the setVisible method
        FontChooserDialog dialog = new FontChooserDialog(frame, "Test", true, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.setVisible(false);
    }

    @Test
    public void testReset() {
        // Test case for the reset method
        FontChooserDialog dialog = new FontChooserDialog(frame, "Test", true, chooserPane, okListener, cancelListener);
        dialog.reset();
    }
}