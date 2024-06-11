package osa.ora.server.client.ui.utils.test;

import org.junit.Assert;
import org.junit.Test;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import osa.ora.server.client.ui.utils.JFontChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    }

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
    }

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

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullComponent() {
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(null, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullTitle() {
        Component c = new JFrame();
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, null, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullModal() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, title, null, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullChooserPane() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, title, modal, null, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullOkListener() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, title, modal, chooserPane, null, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullCancelListener() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};

        new FontChooserDialog(c, title, modal, chooserPane, okListener, null);
    }

    @Test
    public void testActionPerformedOkButton() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.okButton.doClick();
        dialog.dispose();
        Assert.assertEquals("OK clicked", okListener.actionPerformed(new ActionEvent(dialog, 1, "OK")));
    }

    @Test
    public void testActionPerformedCancelButton() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        dialog.dispose();
        Assert.assertEquals("Cancel clicked", cancelListener.actionPerformed(new ActionEvent(dialog, 1, "Cancel")));
    }

    @Test
    public void testActionPerformedCancelKey() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.getRootPane().dispatchEvent(new KeyEvent(dialog, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ESCAPE));
        dialog.dispose();
        Assert.assertEquals("Cancel clicked", cancelListener.actionPerformed(new ActionEvent(dialog, 1, "cancel")));
    }

    @Test
    public void testActionPerformedOkButtonWithInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {
            Font selectedFont = chooserPane.getFont();
            Assert.assertNotNull(selectedFont);
            Assert.assertEquals("Arial", selectedFont.getName());
            Assert.assertEquals(12, (int)selectedFont.getSize());
        };
        ActionListener cancelListener = e -> {};

        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.initFont = initialFont;
        dialog.setVisible(true);
        dialog.okButton.doClick();
        dialog.dispose();
    }

    @Test
    public void testActionPerformedOkButtonWithNullInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.initialFont = null;
        dialog.setVisible(true);
        dialog.okButton.doClick();
        dialog.dispose();
    }

    @Test
    public void testComponentHidden() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener) {
            @Override
            protected void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                dispose();
            }
        }.setVisible(true);
    }
}