package osa.ora.server.client.ui.utils.test;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {
    @Test
    public void testFontChooserDialogConstruction() {
        // Given
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        // When
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Then
        assertEquals(title, dialog.getTitle());
        assertTrue(dialog.isModal());
        assertNotNull(dialog.getOkButton());
        assertNotNull(dialog.getCancelButton());
        assertNotNull(dialog.getResetButton());
        assertNotNull(dialog.getContentPane());
        assertNotNull(dialog.getChooserPane());
    }

    @Test
    public void testFontChooserDialogWithNullTitle() {
        // Given
        Component c = new JFrame();
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        // When & Then
        assertThrows(NullPointerException.class, () -> new FontChooserDialog(c, null, modal, chooserPane, okListener, cancelListener));
    }

    @Test
    public void testFontChooserDialogWithNullInitialFont() {
        // Given
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        // When
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Then
        dialog.reset();
        assertNull(dialog.chooserPane.getFont());
    }

    @Test
    public void testFontChooserDialogWithNullChooserPane() {
        // Given
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        // When & Then
        assertThrows(NullPointerException.class, () -> new FontChooserDialog(c, title, modal, null, okListener, cancelListener));
    }

    @Test
    public void testFontChooserDialogWithNullOkListener() {
        // Given
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener cancelListener = e -> {};

        // When
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, null, cancelListener);

        // Then
        assertNotNull(dialog.getOkButton());
        assertEquals(0, dialog.getOkButton().getListeners(ActionListener.class).length);
    }

    @Test
    public void testFontChooserDialogWithNullCancelListener() {
        // Given
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};

        // When
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, null);

        // Then
        assertNotNull(dialog.getCancelButton());
        assertEquals(0, dialog.getCancelButton().getListeners(ActionListener.class).length);
    }

    @Test
    public void testFontChooserDialogWithModalSetToFalse() {
        // Given
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        // When
        FontChooserDialog dialog = new FontChooserDialog(c, title, false, chooserPane, okListener, cancelListener);

        // Then
        assertFalse(dialog.isModal());
    }
}