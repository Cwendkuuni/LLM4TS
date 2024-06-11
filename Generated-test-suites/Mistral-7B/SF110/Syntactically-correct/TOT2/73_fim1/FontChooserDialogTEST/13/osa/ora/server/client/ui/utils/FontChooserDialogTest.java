package osa.ora.server.client.ui.utils.test;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class TestFontChooserDialog extends JFrame {
    private static final long serialVersionUID = 1L;

    @Test
    public void testFontChooserDialogConstructor() {
        JFrame frame = new JFrame();
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {}; // Dummy implementation
        ActionListener cancelListener = e -> {}; // Dummy implementation
        FontChooserDialog dialog = new FontChooserDialog(frame, "Test Title", true, chooserPane, okListener, cancelListener);
        assertTrue(dialog instanceof JDialog);
    }

    @Test
    public void testSetVisible() {
        JFrame frame = new JFrame();
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {}; // Dummy implementation
        ActionListener cancelListener = e -> {}; // Dummy implementation
        FontChooserDialog dialog = new FontChooserDialog(frame, "Test Title", true, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
    }

    @Test
    public void testNullComponent() {
        ActionListener okListener = e -> {}; // Dummy implementation
        ActionListener cancelListener = e -> {}; // Dummy implementation
        try {
            new FontChooserDialog(null, "Test Title", true, new JFontChooser(), okListener, cancelListener);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyTitle() {
        ActionListener okListener = e -> {}; // Dummy implementation
        ActionListener cancelListener = e -> {}; // Dummy implementation
        try {
            new FontChooserDialog(new JFrame(), "", true, new JFontChooser(), okListener, cancelListener);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testModalFalse() {
        JFrame frame = new JFrame();
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {}; // Dummy implementation
        ActionListener cancelListener = e -> {}; // Dummy implementation
        try {
            new FontChooserDialog(frame, "Test Title", false, chooserPane, okListener, cancelListener).setVisible(true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNullJFontChooser() {
        ActionListener okListener = e -> {}; // Dummy implementation
        ActionListener cancelListener = e -> {}; // Dummy implementation
        try {
            new FontChooserDialog(new JFrame(), "Test Title", true, null, okListener, cancelListener);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testReset() {
        JFrame frame = new JFrame();
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {}; // Dummy implementation
        ActionListener cancelListener = e -> {}; // Dummy implementation
        FontChooserDialog dialog = new FontChooserDialog(frame, "Test Title", true, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.reset();
        assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
    }
}