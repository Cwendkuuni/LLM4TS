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
        ActionListener okListener = actionEvent -> {};
        ActionListener cancelListener = actionEvent -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.dispose();
    }

    @Test
    public void testSetVisibleAndGetInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = actionEvent -> {};
        ActionListener cancelListener = actionEvent -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
        dialog.dispose();
    }

    @Test
    public void testReset() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = actionEvent -> {};
        ActionListener cancelListener = actionEvent -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.reset();
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
        dialog.dispose();
    }

    @Test
    public void testOkButtonActionPerformed() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = actionEvent -> {};
        ActionListener cancelListener = actionEvent -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.okButton.doClick();
        dialog.dispose();
        assertTrue(okListener.isEnabled());
    }

    @Test
    public void testCancelButtonActionPerformed() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = actionEvent -> {};
        ActionListener cancelListener = actionEvent -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        dialog.dispose();
        assertTrue(cancelListener.isEnabled());
    }

    @Test
    public void testDefaultButton() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = actionEvent -> {};
        ActionListener cancelListener = actionEvent -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        assertEquals(dialog.okButton, dialog.getRootPane().getDefaultButton());
        dialog.dispose();
    }

    @Test
    public void testSetLocationRelativeTo() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = actionEvent -> {};
        ActionListener cancelListener = actionEvent -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        Point location = dialog.getLocation();
        dialog.dispose();
        // Replace the following with actual checks based on the expected location.
        assertTrue(location.x > 0 && location.y > 0);
    }

}