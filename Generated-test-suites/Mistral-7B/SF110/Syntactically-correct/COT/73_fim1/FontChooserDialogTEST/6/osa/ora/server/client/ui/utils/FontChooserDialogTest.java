package osa.ora.server.client.ui.utils.test;

import org.junit.Assert;
import org.junit.Test;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import osa.ora.server.client.ui.utils.JFontChooser;

import java.awt.*;
import java.awt.event.ActionEvent;

public class FontChooserDialogTest {

    // Test constructor
    @Test
    public void testFontChooserDialogConstructor() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        Assert.assertNotNull(dialog);
    }

    // Test setVisible method with initialFont set
    @Test
    public void testSetVisibleWithInitialFontSet() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        Assert.assertNotNull(dialog.initialFont);
    }

    // Test reset method
    @Test
    public void testResetMethod() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.reset();
        Assert.assertEquals(dialog.initialFont, chooserPane.getFont());
    }

    // Test setVisible method with initialFont not set
    @Test(expected = IllegalStateException.class)
    public void testSetVisibleWithoutInitialFont() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.setVisible(false);
        dialog.reset(); // reset should throw an exception if initialFont is not set
    }

    // Test okButton actionPerformed with no okListener
    @Test
    public void testOkButtonActionPerformedNoListener() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.okButton.doClick();
        Assert.assertFalse(dialog.isVisible());
    }

    // Test okButton actionPerformed with okListener
    @Test
    public void testOkButtonActionPerformedWithListener() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, e -> {
        }, null);
        dialog.setVisible(true);
        dialog.okButton.doClick();
        Assert.assertFalse(dialog.isVisible());
    }

    // Test cancelButton actionPerformed with no cancelListener
    @Test
    public void testCancelButtonActionPerformedNoListener() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        Assert.assertFalse(dialog.isVisible());
    }

    // Test cancelButton actionPerformed with cancelListener
    @Test
    public void testCancelButtonActionPerformedWithListener() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, e -> {
        }, e -> {
        });
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        Assert.assertFalse(dialog.isVisible());
    }

    // Test resetButton actionPerformed
    @Test
    public void testResetButtonActionPerformed() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.resetButton.doClick();
        Assert.assertEquals(dialog.initialFont, chooserPane.getFont());
    }

    // Test windowClosing with no windowListener
    @Test(expected = IllegalStateException.class)
    public void testWindowClosingWithoutWindowListener() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.getRootPane().getFrame().dispatchEvent(new WindowEvent(dialog.getRootPane().getFrame(), WindowEvent.WINDOW_CLOSING));
    }

    // Test windowClosing with windowListener
    @Test
    public void testWindowClosingWithWindowListener() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.addWindowListener(new FontChooserDialog.Closer());
        dialog.getRootPane().getFrame().dispatchEvent(new WindowEvent(dialog.getRootPane().getFrame(), WindowEvent.WINDOW_CLOSING));
        Assert.assertFalse(dialog.isVisible());
    }

    // Test componentHidden with no componentListener
    @Test(expected = IllegalStateException.class)
    public void testComponentHiddenWithoutComponentListener() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.getRootPane().getFrame().dispatchEvent(new ComponentEvent(dialog.getRootPane(), ComponentEvent.COMPONENT_HIDDEN));
    }

    // Test componentHidden with componentListener
    @Test
    public void testComponentHiddenWithComponentListener() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setVisible(true);
        dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
        dialog.getRootPane().getFrame().dispatchEvent(new ComponentEvent(dialog.getRootPane(), ComponentEvent.COMPONENT_HIDDEN));
        Assert.assertFalse(dialog.isVisible());
    }

    // Test if component is focusable
    @Test
    public void testIsFocusable() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        Assert.assertTrue(dialog.isFocusable());
    }

    // Test setFont method with null font
    @Test(expected = IllegalArgumentException.class)
    public void testSetFontWithNullFont() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        dialog.setFont(null);
    }

    // Test setFont method with valid font
    @Test
    public void testSetFontWithValidFont() {
        JFontChooser chooserPane = new JFontChooser();
        FontChooserDialog dialog = new FontChooserDialog(null, "Font Chooser", true, chooserPane, null, null);
        Font font = new Font("Arial", Font.PLAIN, 12);
        dialog.setFont(font);
        Assert.assertEquals(font, chooserPane.getFont());
    }
}