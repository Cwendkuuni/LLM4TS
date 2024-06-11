package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

        assertEquals(title, dialog.getTitle());
        assertEquals(modal, dialog.isModal());
        assertEquals(chooserPane, dialog.getChooserPane());
        assertNull(dialog.getInitialFont());

        dialog.setVisible(true);
        dialog.dispose();
    }

    @Test
    public void testSetVisibleAndGetInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        dialog.dispose();

        JFrame frame = new JFrame();
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        dialog = new FontChooserDialog(frame, title, modal, chooserPane, okListener, cancelListener) {
            @Override
            public void setVisible(boolean visible) {
                if (visible) {
                    super.setVisible(visible);
                    this.initialFont = this.chooserPane.getFont();
                }
            }
        };

        dialog.setVisible(true);
        assertEquals(initialFont, dialog.getInitialFont());
        dialog.dispose();
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

        dialog.setVisible(true);
        dialog.reset();
        dialog.dispose();
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

        MouseEvent mouseEvent = new MouseEvent(dialog, 0, System.currentTimeMillis(), 0, 0, 0, 1, false);
        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
        dialog.dispatchEvent(mouseEvent);
        dialog.dispose();

        assertFalse(dialog.isVisible());
        assertTrue(dialog.isDisposed());
    }
}