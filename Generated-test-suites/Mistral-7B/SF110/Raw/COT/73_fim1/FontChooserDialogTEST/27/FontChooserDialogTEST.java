package osa.ora.server.client.ui.utils.test;

import org.junit.Assert;
import org.junit.Test;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import osa.ora.server.client.ui.utils.JFontChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FontChooserDialogTest {

    // Test constructor method with valid arguments
    @Test
    public void testFontChooserDialogWithValidArguments() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK button clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel button clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        Assert.assertNotNull(dialog);
    }

    // Test constructor method with null component
    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogWithNullComponent() {
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        new FontChooserDialog(null, title, modal, chooserPane, okListener, cancelListener);
    }

    // Test setVisible method
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
    }

    // Test reset method
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
        Font font = dialog.chooserPane.getFont();
        Assert.assertEquals(dialog.initialFont, font);
    }

    // Test windowClosing method of Closer class
    @Test
    public void testWindowClosing() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        dialog.addWindowListener(closer);
        dialog.setVisible(true);
        dialog.dispose();
        Assert.assertTrue(closer.windowClosing != null);
    }

    // Test componentHidden method of DisposeOnClose class
    @Test
    public void testComponentHidden() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        dialog.addComponentListener(disposeOnClose);
        dialog.setVisible(true);
        dialog.dispose();
        Assert.assertEquals(true, disposeOnClose.componentHiddenCalled);
    }

    // Test setActionCommand method with null actionCommand for okButton
    @Test(expected = NullPointerException.class)
    public void testSetActionCommandWithNullOkButtonActionCommand() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setActionCommand(null, okListener);
    }

    // Test setActionCommand method with non-null actionCommand for okButton
    @Test
    public void testSetActionCommandWithValidOkButtonActionCommand() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK button clicked with 'OK' action command");
        ActionListener cancelListener = e -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setActionCommand("OK", okListener);
        dialog.setVisible(true);
        dialog.okButton.doClick();
        Assert.assertEquals("OK", dialog.okButton.getActionCommand());
    }

    // Test setActionCommand method with null actionCommand for cancelButton
    @Test(expected = NullPointerException.class)
    public void testSetActionCommandWithNullCancelButtonActionCommand() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setActionCommand(null, cancelListener);
    }

    // Test setActionCommand method with non-null actionCommand for cancelButton
    @Test
    public void testSetActionCommandWithValidCancelButtonActionCommand() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK button clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel button clicked with 'CANCEL' action command");
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setActionCommand("CANCEL", cancelListener);
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        Assert.assertEquals("CANCEL", dialog.cancelButton.getActionCommand());
    }

    // Test setMnemonic method with negative mnemonic for resetButton
    @Test(expected = IllegalArgumentException.class)
    public void testSetMnemonicWithNegativeResetButtonMnemonic() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setMnemonic(-1, dialog.resetButton);
    }
}