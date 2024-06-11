package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        Component c = Mockito.mock(Component.class);
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = Mockito.mock(JFontChooser.class);
        ActionListener okListener = Mockito.mock(ActionListener.class);
        ActionListener cancelListener = Mockito.mock(ActionListener.class);

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertEquals(title, this.getDialogTitle(FontChooserDialog.this));
        assertEquals(modal, this.getDialogModalityType(FontChooserDialog.this) == Dialog.ModalType.MODELESS);
        assertEquals(chooserPane, FontChooserDialog.this.chooserPane);
        assertEquals(okListener, FontChooserDialog.this.okButton.getActionListeners()[0]);
        assertEquals(cancelListener, FontChooserDialog.this.cancelButton.getActionListeners()[0]);
    }

    @Test
    public void testSetVisible() {
        Component c = Mockito.mock(Component.class);
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = Mockito.mock(JFontChooser.class);
        ActionListener okListener = Mockito.mock(ActionListener.class);
        ActionListener cancelListener = Mockito.mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        assertEquals(chooserPane.getFont(), fontChooserDialog.initialFont);

        fontChooserDialog.reset();
        assertEquals(chooserPane.getFont(), fontChooserDialog.initialFont);
    }

    @Test
    public void testReset() {
        Component c = Mockito.mock(Component.class);
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = Mockito.mock(JFontChooser.class);
        ActionListener okListener = Mockito.mock(ActionListener.class);
        ActionListener cancelListener = Mockito.mock(ActionListener.class);

        Font initialFont = new Font("Arial", Font.BOLD, 12);
        when(chooserPane.getFont()).thenReturn(initialFont);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);

        fontChooserDialog.reset();
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFontChooserDialogConstructorNullComponent() {
        Component c = null;
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = Mockito.mock(JFontChooser.class);
        ActionListener okListener = Mockito.mock(ActionListener.class);
        ActionListener cancelListener = Mockito.mock(ActionListener.class);

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullTitle() {
        Component c = Mockito.mock(Component.class);
        String title = null;
        boolean modal = true;
        JFontChooser chooserPane = Mockito.mock(JFontChooser.class);
        ActionListener okListener = Mockito.mock(ActionListener.class);
        ActionListener cancelListener = Mockito.mock(ActionListener.class);

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testGetDialogTitle() {
        Component c = Mockito.mock(Component.class);
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = Mockito.mock(JFontChooser.class);
        ActionListener okListener = Mockito.mock(ActionListener.class);
        ActionListener cancelListener = Mockito.mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertEquals(title, this.getDialogTitle(fontChooserDialog));
    }

    @Test
    public void testGetDialogModalityType() {
        Component c = Mockito.mock(Component.class);
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = Mockito.mock(JFontChooser.class);
        ActionListener okListener = Mockito.mock(ActionListener.class);
        ActionListener cancelListener = Mockito.mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertEquals(modal, this.getDialogModalityType(fontChooserDialog) == Dialog.ModalType.MODELESS);
    }

    private String getDialogTitle(FontChooserDialog fontChooserDialog) {
        return fontChooserDialog.getTitle();
    }

    private Dialog.ModalType getDialogModalityType(Dialog dialog) {
        return dialog.getModalityType();
    }
}