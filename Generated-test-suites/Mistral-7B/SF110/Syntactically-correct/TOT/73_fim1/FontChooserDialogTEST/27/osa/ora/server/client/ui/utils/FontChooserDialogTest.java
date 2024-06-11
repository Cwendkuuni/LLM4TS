package osa.ora.server.client.ui.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FontChooserDialogTest {

    private FontChooserDialog dialog;
    private JFrame frame;
    private ActionListener okListener = e -> {};
    private ActionListener cancelListener = e -> {};

    @Before
    public void setUp() {
        frame = new JFrame();
        dialog = new FontChooserDialog(frame, "Font Chooser Dialog", true, new JFontChooser(), okListener, cancelListener);
    }

    @Test
    public void testFontChooserDialogCreation() {
        Assert.assertNotNull(dialog);
        Assert.assertNotNull(dialog.getContentPane());
        Assert.assertNotNull(dialog.chooserPane);
    }

    @Test
    public void testSetVisible() {
        dialog.setVisible(true);
        Assert.assertNotNull(dialog.initialFont);
    }

    @Test
    public void testReset() {
        dialog.setVisible(true);
        dialog.reset();
        Assert.assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    @Test
    public void testOKButtonAction() {
        dialog.setVisible(true);
        dialog.okButton.doClick();
        Assert.assertFalse(dialog.isVisible());
    }

    @Test
    public void testCancelButtonAction() {
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        Assert.assertFalse(dialog.isVisible());
    }

    @Test
    public void testInitialFontIsNull() {
        dialog.initialFont = null;
        dialog.setVisible(true);
        dialog.reset();
        Assert.assertNull(dialog.chooserPane.getFont());
    }

    @Test(expected = NullPointerException.class)
    public void testNullComponent() {
        new FontChooserDialog(null, "Title", true, new JFontChooser(), okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTitle() {
        new FontChooserDialog(frame, null, true, new JFontChooser(), okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testNullOkListener() {
        new FontChooserDialog(frame, "Title", true, new JFontChooser(), null, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCancelListener() {
        new FontChooserDialog(frame, "Title", true, new JFontChooser(), okListener, null);
    }
}