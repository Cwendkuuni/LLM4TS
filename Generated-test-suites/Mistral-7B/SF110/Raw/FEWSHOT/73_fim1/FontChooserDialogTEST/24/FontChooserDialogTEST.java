package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstruction() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertEquals(title, dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());
        assertNull(dialog.getInitialFont());
        assertEquals(2, dialog.getButtonPane().getComponentCount());
    }

    @Test
    public void testSetVisibleWithInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        Font initialFont = new Font("Arial", Font.BOLD, 12);

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);

        assertEquals(initialFont, dialog.getInitialFont());
    }

    @Test
    public void testSetVisibleWithoutInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);

        assertNull(dialog.getInitialFont());
    }

    @Test
    public void testReset() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        Font initialFont = new Font("Arial", Font.BOLD, 12);

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);

        dialog.reset();

        assertEquals(initialFont, dialog.getChooserPane().getFont());
    }

    @Test
    public void testWindowEvents() throws Exception {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);

        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        dialog.addWindowListener(closer);

        dialog.dispose();

        assertTrue(closer.windowClosed);
    }

    @Test
    public void testComponentEvents() throws Exception {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);

        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        dialog.addComponentListener(disposeOnClose);

        dialog.dispose();

        assertTrue(disposeOnClose.componentHidden);
    }
}