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
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Check that the title is set correctly
        assertEquals(title, dialog.getTitle());

        // Check that the modal flag is set correctly
        assertTrue(dialog.isModal());

        // Check that the OK button has the correct label
        JButton okButton = dialog.getRootPane().getDefaultButton();
        assertEquals("OK", okButton.getText());

        // Check that the cancel button exists
        JButton cancelButton = dialog.getRootPane().findComponentAt(dialog.getWidth() - 75, dialog.getHeight() - 25);
        assertNotNull(cancelButton);
        assertEquals("Cancel", cancelButton.getText());

        // Check that the reset button exists
        JButton resetButton = dialog.getRootPane().findComponentAt(dialog.getWidth() - 100, dialog.getHeight() - 25);
        assertNotNull(resetButton);
        assertEquals("Reset", resetButton.getText());

        // Check that the chooser pane is added to the dialog
        JComponent chooserComponent = dialog.getContentPane().getComponent(0);
        assertEquals(chooserPane, chooserComponent);

        dialog.dispose();
    }

    @Test
    public void testFontChooserDialogSetFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Set the initial font to a known value
        dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));

        // Set the dialog visible to get the initial font
        dialog.setVisible(true);

        // Reset the font
        dialog.reset();

        // Check that the font has been reset to the initial font
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());

        dialog.dispose();
    }

    @Test
    public void testFontChooserDialogSetVisible() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Set the dialog visible and check that the initial font is saved
        dialog.setVisible(true);
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());

        dialog.dispose();
    }

    @Test
    public void testFontChooserDialogEventHandling() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        booleanmodal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Simulate OK button click
        JButton okButton = dialog.getRootPane().getDefaultButton();
        okButton.doClick();
        assertTrue(okListener.actionPerformedHasBeenCalled());

        dialog.dispose();

        // Simulate cancel button click
        JButton cancelButton = dialog.getRootPane().findComponentAt(dialog.getWidth() - 75, dialog.getHeight() - 25);
        cancelButton.doClick();
        assertTrue(cancelListener.actionPerformedHasBeenCalled());

        dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Simulate OK button press with key event
        dialog.getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke("pressed ALT"), "ok");
        dialog.getRootPane().getActionMap().put("ok", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assertEquals("OK", e.getActionCommand());
                dialog.dispose();
            }
        });
        dialog.getRootPane().getRootPane().requestFocus();
        dialog.getRootPane().getInputMap(2).get(KeyStroke.getKeyStroke("pressed ALT")).invoke();
        assertTrue(okListener.actionPerformedHasBeenCalled());
    }

    @Test
    public void testFontChooserDialogDisposeOnClose() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Register dispose on close listeners
        dialog.addWindowListener(new FontChooserDialog.Closer());
        dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());

        // Set the dialog visible and check that it is disposed when hidden
        dialog.setVisible(true);
        assertEquals(false, dialog.isVisible());
        assertTrue(dialog.isDisposed());

        dialog.dispose();
    }

    @Test
    public void testFontChooserDialogReset() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        // Set the font chooser pane font to a known value
        dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));

        // Reset the font
        dialog.reset();

        // Check that the font has been reset to the default font
        assertEquals(chooserPane.getDefaultFont(), dialog.chooserPane.getFont());

        dialog.dispose();
    }
}