package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import javax.swing.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialog() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertNotNull(dialog);
        assertEquals(title, dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());
        assertNull(dialog.getInitialFont());

        dialog.pack();
        dialog.setVisible(true);
        dialog.dispose();
    }

    @Test
    public void testSetVisible() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.pack();

        dialog.setVisible(true);
        assertNotNull(dialog.getInitialFont());

        dialog.reset();
        assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
    }

    @Test
    public void testReset() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.pack();
        dialog.setVisible(true);

        dialog.reset();
        assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
    }

    @Test
    public void testActionPerformedOk() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.pack();
        dialog.setVisible(true);

        dialog.getOkButton().doClick();
        assertTrue(dialog.isVisible());
    }

    @Test
    public void testActionPerformedCancel() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.pack();
        dialog.setVisible(true);

        dialog.getCancelButton().doClick();
        assertFalse(dialog.isVisible());
    }
}