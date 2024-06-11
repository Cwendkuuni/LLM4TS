package osa.ora.server.client.ui.utils.test;

import org.junit.Assert;
import org.junit.Test;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import osa.ora.server.client.ui.utils.JFontChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        Assert.assertNotNull(dialog);
        Assert.assertNotNull(dialog.chooserPane);
        Assert.assertEquals(BorderLayout.CENTER, dialog.getContentPane().getLayout());
    }

    @Test
    public void testSetVisibleMethod() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        Assert.assertNotNull(dialog.initialFont);

        dialog.setVisible(false);
        Assert.assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
    }

    @Test
    public void testResetMethod() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        Font initialFont = chooserPane.getFont();
        dialog.reset();
        Assert.assertEquals(initialFont, dialog.chooserPane.getFont());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFontChooserDialogConstructorWithNullComponent() {
        String title = "FontChooserDialog";
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
        String title = "FontChooserDialog";
        boolean modal = true;
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, title, modal, null, okListener, cancelListener);
    }

    @Test
    public void testFontChooserDialogWithNullOkListener() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, null, cancelListener);

        Assert.assertNotNull(dialog);
        Assert.assertNotNull(dialog.chooserPane);
        Assert.assertEquals(BorderLayout.CENTER, dialog.getContentPane().getLayout());
    }

    @Test
    public void testFontChooserDialogWithNullCancelListener() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, null);

        Assert.assertNotNull(dialog);
        Assert.assertNotNull(dialog.chooserPane);
        Assert.assertEquals(BorderLayout.CENTER, dialog.getContentPane().getLayout());
    }

    @Test
    public void testFontChooserDialogWithEmptyActionCommands() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, new ActionListener[]{}, new ActionListener[]{});

        Assert.assertNotNull(dialog);
        Assert.assertNotNull(dialog.chooserPane);
        Assert.assertEquals(BorderLayout.CENTER, dialog.getContentPane().getLayout());
    }
}