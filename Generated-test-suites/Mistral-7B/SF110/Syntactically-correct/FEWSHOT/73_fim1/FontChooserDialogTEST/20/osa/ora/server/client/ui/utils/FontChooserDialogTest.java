package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {
    @Test
    public void testFontChooserDialogConstructor() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        assertTrue(fontChooserDialog.isModal());
        assertEquals(title, fontChooserDialog.getTitle());
        assertNotNull(fontChooserDialog.getContentPane());
        assertNotNull(fontChooserDialog.getRootPane());
    }

    @Test
    public void testSetVisibleWithInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        assertTrue(fontChooserDialog.isModal());
        assertEquals(title, fontChooserDialog.getTitle());
        assertNotNull(fontChooserDialog.getContentPane());
        assertNotNull(fontChooserDialog.getRootPane());

        fontChooserDialog.setVisible(true);
        assertEquals(chooserPane.getFont(), fontChooserDialog.initialFont);
    }

    @Test
    public void testSetVisibleWithoutInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        assertTrue(fontChooserDialog.isModal());
        assertEquals(title, fontChooserDialog.getTitle());
        assertNotNull(fontChooserDialog.getContentPane());
        assertNotNull(fontChooserDialog.getRootPane());

        fontChooserDialog.setVisible(false);
        assertNull(fontChooserDialog.initialFont);
    }

    @Test
    public void testReset() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        assertTrue(fontChooserDialog.isModal());
        assertEquals(title, fontChooserDialog.getTitle());
        assertNotNull(fontChooserDialog.getContentPane());
        assertNotNull(fontChooserDialog.getRootPane());

        Font initialFont = new Font("Arial", Font.BOLD, 12);
        chooserPane.setFont(initialFont);
        fontChooserDialog.setVisible(true);
        assertEquals(initialFont, fontChooserDialog.initialFont);

        fontChooserDialog.reset();
        assertEquals(initialFont, chooserPane.getFont());
    }
}