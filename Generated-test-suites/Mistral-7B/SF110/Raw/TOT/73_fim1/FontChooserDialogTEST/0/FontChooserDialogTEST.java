package osa.ora.server.client.ui.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.event.*;

public class FontChooserDialogTest
{
    private FontChooserDialog dialog;
    private JFrame frame;
    private JFontChooser chooserPane;

    @Before
    public void setUp()
    {
        frame = new JFrame();
        chooserPane = new JFontChooser();
        dialog = new FontChooserDialog(frame, "Font Chooser", true, chooserPane, null, null);
    }

    @Test
    public void testDefaultFont()
    {
        dialog.setVisible(true);
        assertEquals("Default font should be set initially", DialogFont.getDialogFont(), dialog.chooserPane.getFont());
        dialog.setVisible(false);
    }

    @Test
    public void testOkButton()
    {
        dialog.setVisible(true);
        Font newFont = new Font("Arial", Font.BOLD, 14);
        dialog.chooserPane.setFont(newFont);
        ActionEvent okEvent = new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "OK");
        dialog.okButton.dispatchEvent(okEvent);
        dialog.setVisible(false);
        assertNotEquals("Font should not be the default font after OK button press", DialogFont.getDialogFont(), chooserPane.getFont());
    }

    @Test
    public void testCancelButton()
    {
        dialog.setVisible(true);
        ActionEvent cancelEvent = new ActionEvent(dialog, ActionEvent.ACTION_PERFORMED, "Cancel");
        dialog.cancelButton.dispatchEvent(cancelEvent);
        dialog.setVisible(false);
        assertEquals("Font should remain the same after Cancel button press", DialogFont.getDialogFont(), chooserPane.getFont());
    }

    @Test(expected = NullPointerException.class)
    public void testNullOkListener()
    {
        dialog = new FontChooserDialog(frame, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.okButton.doClick();
        fail("NullOKListener should have thrown a NullPointerException");
    }

    @Test(expected = NullPointerException.class)
    public void testNullCancelListener()
    {
        dialog = new FontChooserDialog(frame, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        fail("NullCancelListener should have thrown a NullPointerException");
    }

    @Test
    public void testResetButton()
    {
        dialog.setVisible(true);
        Font newFont = new Font("Arial", Font.BOLD, 14);
        dialog.chooserPane.setFont(newFont);
        dialog.resetButton.doClick();
        dialog.setVisible(false);
        assertEquals("Font should be reset to default after Reset button press", DialogFont.getDialogFont(), chooserPane.getFont());
    }

    @Test
    public void testKeyEventCancel()
    {
        dialog = new FontChooserDialog(frame, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        KeyEvent keyEvent = new KeyEvent(dialog, KeyEvent.KEY_PRESSED, 0, '\u001b', false);
        dialog.dispatchEvent(keyEvent);
        dialog.setVisible(false);
        assertEquals("Font should remain the same after ESC key press", DialogFont.getDialogFont(), chooserPane.getFont());
    }
}