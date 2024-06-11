package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstruction() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertSame(c, fontChooserDialog.getParent());
        assertEquals(title, fontChooserDialog.getTitle());
        assertEquals(modal, fontChooserDialog.isModal());
        assertSame(chooserPane, fontChooserDialog.getChooserPane());
        assertTrue(fontChooserDialog.isFocusTraversable());
    }

    @Test
    public void testSetVisibleWithInitialFont() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);
        Font initialFont = new Font("Arial", Font.BOLD, 12);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        fontChooserDialog.setVisible(true);
        verify(chooserPane).setFont(initialFont);
    }

    @Test
    public void testSetVisibleWithoutInitialFont() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        fontChooserDialog.setVisible(true);
        verify(chooserPane, never()).setFont(any());
    }

    @Test
    public void testReset() {
        FontChooserDialog fontChooserDialog = new FontChooserDialog(mock(Component.class), "Font Dialog", true, mock(JFontChooser.class), mock(ActionListener.class), mock(ActionListener.class));
        Font initialFont = new Font("Arial", Font.BOLD, 12);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.reset();
        verify(fontChooserDialog.getChooserPane()).setFont(initialFont);
    }

    @Test
    public void testOkButtonAction() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        ActionEvent actionEvent = mock(ActionEvent.class);
        okListener.actionPerformed(actionEvent);

        verify(fontChooserDialog).setVisible(false);
    }

    @Test
    public void testCancelButtonAction() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        ActionEvent actionEvent = mock(ActionEvent.class);
        cancelListener.actionPerformed(actionEvent);

        verify(fontChooserDialog).setVisible(false);
    }

    @Test
    public void testResetButtonAction() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);
        Font initialFont = new Font("Arial", Font.BOLD, 12);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);

        ActionEvent actionEvent = mock(ActionEvent.class);
        fontChooserDialog.getChooserPane().setFont(new Font("Verdana", Font.ITALIC, 14));
        fontChooserDialog.resetButton.doClick();

        verify(chooserPane).setFont(initialFont);
    }
}