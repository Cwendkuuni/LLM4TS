package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import org.junit.Test;

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
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Check if the FontChooserDialog has the correct title
        assertEquals(title, dialog.getTitle());

        // Check if the FontChooserDialog is modal
        assertTrue(dialog.isModal());

        // Check if the OK button is present
        JButton okButton = dialog.getRootPane().findComponentAt(dialog.getRootPane().getHeight() / 2, dialog.getRootPane().getWidth() / 2);
        assertNotNull(okButton);
        assertEquals("OK", okButton.getText());

        // Check if the Cancel button is present
        JButton cancelButton = dialog.getRootPane().findComponentAt(dialog.getRootPane().getHeight() / 2, dialog.getRootPane().getWidth() / 2 - 100);
        assertNotNull(cancelButton);
        assertEquals("Cancel", cancelButton.getText());
    }

    @Test
    public void testFontChooserDialogSetVisible() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        assertTrue(dialog.isVisible());

        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testFontChooserDialogSetFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        Font initialFont = dialog.chooserPane.getFont();
        dialog.setVisible(true);

        Font newFont = new Font("Arial", Font.BOLD, 14);
        dialog.chooserPane.setFont(newFont);

        dialog.setVisible(false);
        assertEquals(newFont, dialog.chooserPane.getFont());
        assertEquals(initialFont, dialog.initialFont);
    }

    @Test
    public void testFontChooserDialogOkButtonAction() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);

        JButton okButton = dialog.getRootPane().findComponentAt(dialog.getRootPane().getHeight() / 2, dialog.getRootPane().getWidth() / 2);
        okButton.doClick();

        dialog.setVisible(false);
        assertTrue(okListener.actionPerformed(new ActionEvent(dialog, 0, "OK")));
    }

    @Test
    public void testFontChooserDialogCancelButtonAction() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);

        JButton cancelButton = dialog.getRootPane().findComponentAt(dialog.getRootPane().getHeight() / 2, dialog.getRootPane().getWidth() / 2 - 100);
        cancelButton.doClick();

        dialog.setVisible(false);
        assertTrue(cancelListener.actionPerformed(new ActionEvent(dialog, 0, "Cancel")));
    }

    @Test
    public void testFontChooserDialogResetButtonAction() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);

        JButton resetButton = dialog.getRootPane().findComponentAt(dialog.getRootPane().getHeight() / 2, dialog.getRootPane().getWidth() / 2 - 200);
        resetButton.doClick();

        dialog.setVisible(false);
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }
}