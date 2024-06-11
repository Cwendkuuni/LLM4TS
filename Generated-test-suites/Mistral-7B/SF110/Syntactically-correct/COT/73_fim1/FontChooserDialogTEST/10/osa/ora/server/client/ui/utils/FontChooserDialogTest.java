package osa.ora.server.client.ui.utils.test;

import org.junit.Assert;
import org.junit.Test;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import osa.ora.server.client.ui.utils.JFontChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FontChooserDialogTest {

    // Test constructor method with all arguments
    @Test
    public void testFontChooserDialogConstruction() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    }

    // Test method: setVisible(boolean visible)
    @Test
    public void testSetVisible() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        Assert.assertNotNull(dialog.initialFont);
        dialog.setVisible(false);
        dialog.setVisible(true);
        dialog.reset();
        Assert.assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    // Test method: reset()
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
        Assert.assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    // Test exception handling: null component
    @Test(expected = IllegalArgumentException.class)
    public void testNullComponent() {
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(null, title, modal, chooserPane, okListener, cancelListener);
    }

    // Test exception handling: null title
    @Test(expected = NullPointerException.class)
    public void testNullTitle() {
        Component c = new JFrame();
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, null, modal, chooserPane, okListener, cancelListener);
    }

    // Test exception handling: null chooserPane
    @Test(expected = NullPointerException.class)
    public void testNullChooserPane() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, title, modal, null, okListener, cancelListener);
    }

    // Test exception handling: null okListener
    @Test
    public void testNullOkListener() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, title, modal, chooserPane, null, cancelListener);
    }

    // Test exception handling: null cancelListener
    @Test
    public void testNullCancelListener() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};

        new FontChooserDialog(c, title, modal, chooserPane, okListener, null);
    }

    // Test exception handling: null okButtonActionListener
    @Test
    public void testNullOkButtonActionListener() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, null, cancelListener);
        ActionEvent event = new ActionEvent(new JButton("OK"), ActionEvent.ACTION_PERFORMED, "OK");

        try {
            dialog.okButton.dispatchEvent(event);
        } catch (Exception ex) {
            Assert.fail("Unexpected exception: " + ex.getMessage());
        }
    }

    // Test exception handling: null cancelButtonActionListener
    @Test
    public void testNullCancelButtonActionListener() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, null);
        ActionEvent event = new ActionEvent(new JButton("Cancel"), ActionEvent.ACTION_PERFORMED, "Cancel");

        try {
            dialog.cancelButton.dispatchEvent(event);
        } catch (Exception ex) {
            Assert.fail("Unexpected exception: " + ex.getMessage());
        }
    }
}