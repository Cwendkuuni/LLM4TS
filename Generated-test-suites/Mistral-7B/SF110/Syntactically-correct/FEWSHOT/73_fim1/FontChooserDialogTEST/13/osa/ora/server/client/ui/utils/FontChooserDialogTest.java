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

        assertEquals(title, dialog.getTitle());
        assertTrue(dialog.isModal());
        assertSame(chooserPane, dialog.getChooserPane());
        assertNotNull(dialog.getOkButton());
        assertNotNull(dialog.getCancelButton());
        assertNotNull(dialog.getResetButton());
        assertEquals(okListener, dialog.getOkButton().getActionListeners()[0]);
        assertEquals(cancelListener, dialog.getCancelButton().getActionListeners()[0]);
    }

    @Test
    public void testSetVisible() {
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

        // Test reset method
        dialog.reset();
        JFontChooser chooser = dialog.getChooserPane();
        assertEquals(dialog.initialFont, chooser.getFont());
    }

    @Test
    public void testInputMapAndActionMap() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        JButton cancelButton = dialog.getCancelButton();
        KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke('\u001b');

        InputMap inputMap = cancelButton.getInputMap(2);
        ActionMap actionMap = cancelButton.getActionMap();

        assertEquals("cancel", inputMap.get(cancelKeyStroke));
        assertSame(cancelKeyAction, actionMap.get("cancel"));
    }
}