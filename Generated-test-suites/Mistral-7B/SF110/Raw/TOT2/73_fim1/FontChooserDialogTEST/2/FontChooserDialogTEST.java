package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Test;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        Component component = new JFrame(); // or any other Component
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {}; // or any other ActionListener
        ActionListener cancelListener = e -> {}; // or any other ActionListener

        FontChooserDialog fontChooserDialog = new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);

        assertNotNull(fontChooserDialog);
    }

    @Test
    public void testSetVisible() {
        Component component = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);

        // Test when the dialog is made visible
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());

        // Test when the dialog is made invisible
        fontChooserDialog.setVisible(false);
        assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        Component component = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);

        fontChooserDialog.setVisible(true);
        assertEquals(fontChooserDialog.getInitialFont(), fontChooserPane.getFont());

        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.getInitialFont(), fontChooserPane.getFont());
    }

    @Test
    public void testFontChooserDialogNullComponent() {
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        try {
            FontChooserDialog fontChooserDialog = new FontChooserDialog(null, title, modal, chooserPane, okListener, cancelListener);
            fail("Expected IllegalArgumentException with null component");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFontChooserDialogEmptyTitle() {
        Component component = new JFrame();
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        try {
            FontChooserDialog fontChooserDialog = new FontChooserDialog(component, "", modal, chooserPane, okListener, cancelListener);
            fail("Expected IllegalArgumentException with empty title");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFontChooserDialogNullInitialFont() {
        Component component = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(component, title, modal, chooserPane, okListener, cancelListener);

        fontChooserDialog.setVisible(true);
        JFontChooser chooserPaneActual = fontChooserDialog.getChooserPane();
        assertEquals(fontChooserDialog.getInitialFont(), chooserPaneActual.getFont());

        fontChooserDialog.reset();
        chooserPaneActual = fontChooserDialog.getChooserPane();
        assertEquals(null, chooserPaneActual.getFont());
    }
}