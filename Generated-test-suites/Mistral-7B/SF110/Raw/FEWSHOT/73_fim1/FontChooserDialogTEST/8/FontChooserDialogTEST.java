package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        verify(c).getFrameForComponent(eq(c));
        verifyNoMoreInteractions(c);
        verify(chooserPane).setFont(any());
        verifyNoMoreInteractions(chooserPane);
        verify(okListener).actionPerformed(any(ActionEvent.class));
        verifyNoMoreInteractions(okListener);
        verify(cancelListener).actionPerformed(any(ActionEvent.class));
        verifyNoMoreInteractions(cancelListener);
    }

    @Test
    public void testSetVisible() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        fontChooserDialog.setVisible(true);

        verify(fontChooserDialog).setVisible(true);
        verify(chooserPane).getFont();
        verify(okListener).actionPerformed(any(ActionEvent.class));
    }

    @Test
    public void testReset() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        Font initialFont = new Font("Arial", Font.BOLD, 12);

        when(chooserPane.getFont()).thenReturn(initialFont);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        fontChooserDialog.reset();

        verify(chooserPane).setFont(eq(initialFont));
    }

    @Test
    public void testCloseDialog() {
        Component c = mock(Component.class);
        String title = "Font Dialog";
        boolean modal = true;
        JFontChooser chooserPane = mock(JFontChooser.class);
        ActionListener okListener = mock(ActionListener.class);
        ActionListener cancelListener = mock(ActionListener.class);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        fontChooserDialog.dispose();

        verify(fontChooserDialog).setVisible(false);
        verify(fontChooserDialog).dispose();
    }
}