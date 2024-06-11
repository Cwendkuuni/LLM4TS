package osa.ora.server.client.ui.utils.test;

import org.junit.Test;
import org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import javax.swing.*;
import java.awt.*;

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
        assertNotNull(fontChooserDialog);
    }

    @Test
    public void testSetVisibleMethod() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Test default font
        fontChooserDialog.setVisible(true);
        assertEquals("Default font should be set when dialog is shown", Font.decode("Dialog"), fontChooserDialog.getChooserPane().getFont());

        // Test custom font
        Font customFont = new Font("Arial", Font.BOLD, 12);
        fontChooserDialog.getChooserPane().setFont(customFont);
        fontChooserDialog.setVisible(false);
        fontChooserDialog.setVisible(true);
        assertEquals("Custom font should be restored when dialog is shown again", customFont, fontChooserDialog.getChooserPane().getFont());
    }

    @Test
    public void testResetMethod() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        Font initialFont = fontChooserDialog.getChooserPane().getFont();
        fontChooserDialog.reset();
        assertEquals("Font should be reset to initial font", initialFont, fontChooserDialog.getChooserPane().getFont());
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorWithNullComponent() {
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(null, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorWithNullTitle() {
        Component c = new JFrame();
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, null, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorWithNullChooserPane() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, title, modal, null, okListener, cancelListener);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFontChooserDialogConstructorWithEmptyActionListeners() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = null;
        ActionListener cancelListener = null;

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    }
}