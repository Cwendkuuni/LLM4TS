package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    private FontChooserDialog dialog;
    private JFrame frame;
    private JFontChooser chooserPane;

    @Before
    public void setUp() {
        frame = new JFrame();
        chooserPane = new JFontChooser();
        dialog = new FontChooserDialog(frame, "Test Title", true, chooserPane, null, null);
    }

    // Test constructor
    @Test
    public void testFontChooserDialogConstructor() {
        assertNotNull(dialog);
    }

    // Test setVisible method
    @Test
    public void testSetVisible() {
        dialog.setVisible(true);
        assertNotNull(dialog.initialFont);
        dialog.setVisible(false);
    }

    // Test reset method
    @Test
    public void testReset() {
        dialog.reset();
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    // Test window closing
    @Test(expected = WindowEvent.WINDOW_CLOSING.class)
    public void testWindowClosing() throws WindowEvent {
        dialog.addWindowListener(new FontChooserDialog.Closer());
        dialog.setVisible(true);
        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
    }

    // Test component hidden
    @Test
    public void testComponentHidden() {
        dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
        dialog.setVisible(true);
        dialog.dispatchEvent(new ComponentEvent(dialog, ComponentEvent.COMPONENT_HIDDEN));
        assertTrue(dialog.isDisposed());
    }

    // Test ok button action
    @Test
    public void testOkButtonAction() {
        dialog.setVisible(true);
        dialog.getRootPane().getKeyboardFocusManager().focusNextComponent(dialog.getRootPane());
        dialog.okButton.doClick();
        assertFalse(dialog.isVisible());
    }

    // Test cancel button action
    @Test
    public void testCancelButtonAction() {
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        assertFalse(dialog.isVisible());
    }

    // Test reset button action
    @Test
    public void testResetButtonAction() {
        dialog.setVisible(true);
        dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 24));
        dialog.resetButton.doClick();
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    // Test null arguments in constructor
    @Test(expected = NullPointerException.class)
    public void testNullArgumentsConstructor() {
        new FontChooserDialog(null, null, true, null, null, null);
    }

    // Test null chooserPane argument in constructor
    @Test(expected = NullPointerException.class)
    public void testNullChooserPaneConstructor() {
        new FontChooserDialog(frame, "Test Title", true, null, null, null);
    }

    // Test null okListener argument in constructor
    @Test
    public void testNullOkListenerConstructor() {
        ActionListener nullListener = null;
        dialog = new FontChooserDialog(frame, "Test Title", true, chooserPane, nullListener, null);
        assertNotNull(dialog.okButton.getActionListeners());
        assertEquals(1, dialog.okButton.getActionListeners().length);
        assertNotNull(dialog.okButton.getActionListeners()[0]);
        assertNotEquals(nullListener, dialog.okButton.getActionListeners()[0]);
    }

    // Test null cancelListener argument in constructor
    @Test
    public void testNullCancelListenerConstructor() {
        ActionListener nullListener = null;
        dialog = new FontChooserDialog(frame, "Test Title", true, chooserPane, null, nullListener);
        assertNotNull(dialog.cancelButton.getActionListeners());
        assertEquals(1, dialog.cancelButton.getActionListeners().length);
        assertNotNull(dialog.cancelButton.getActionListeners()[0]);
        assertNotEquals(nullListener, dialog.cancelButton.getActionListeners()[0]);
    }
}