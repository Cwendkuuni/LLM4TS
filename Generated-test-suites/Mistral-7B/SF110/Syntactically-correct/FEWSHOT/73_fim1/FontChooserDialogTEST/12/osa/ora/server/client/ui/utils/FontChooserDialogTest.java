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
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertNotNull(dialog);
        assertEquals(title, dialog.getTitle());
        assertTrue(dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());
        assertNull(dialog.getInitialFont());
    }

    @Test
    public void testSetAndGetInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        Font initialFont = new Font("Arial", Font.BOLD, 12);
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setInitialFont(initialFont);

        assertEquals(initialFont, dialog.getInitialFont());
    }

    @Test
    public void testSetAndResetFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        Font initialFont = new Font("Arial", Font.BOLD, 12);
        dialog.setInitialFont(initialFont);

        dialog.setVisible(true);
        assertEquals(initialFont, dialog.getChooserPane().getFont());

        dialog.reset();
        assertNull(dialog.getChooserPane().getFont());
    }

    @Test
    public void testButtonActions() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);

        dialog.getOkButton().doClick();
        assertTrue(dialog.isVisible());
        dialog.dispose();

        dialog.getCancelButton().doClick();
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testKeyActions() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);

        dialog.getRootPane().dispatchEvent(new KeyEvent(dialog, 1, 1, KeyEvent.VK_ESCAPE, 0));
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testCloseAndDispose() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        WindowCloser closer = spy(new FontChooserDialog.Closer());
        dialog.addWindowListener(closer);

        dialog.setVisible(true);
        dialog.dispose();

        verify(closer).windowClosing(any(WindowEvent.class));
    }

    @Test
    public void testComponentHiddenAndDispose() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = arg -> {};
        ActionListener cancelListener = arg -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        DisposeOnClose disposeOnClose = spy(new FontChooserDialog.DisposeOnClose());
        dialog.addComponentListener(disposeOnClose);

        dialog.setVisible(true);
        dialog.hide();

        verify(disposeOnClose).componentHidden(any(ComponentEvent.class));
        dialog.dispose();

        assertTrue(dialog.isDisposed());
    }
}