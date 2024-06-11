package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FontChooserDialogTest {

    @Mock
    private Component c;

    @Mock
    private JFontChooser chooserPane;

    @Mock
    private ActionListener okListener;

    @Mock
    private ActionListener cancelListener;

    @InjectMocks
    private FontChooserDialog dialog;

    @Test
    public void testFontChooserDialogConstruction() {
        dialog = new FontChooserDialog(c, "Test Title", true, chooserPane, okListener, cancelListener);
        assertTrue(dialog.isModal());
        assertEquals("Test Title", dialog.getTitle());
        assertEquals(c, dialog.getParent());
        assertEquals(okListener, dialog.getOkButton().getActionListeners()[0]);
        assertNull(dialog.getOkButton().getActionCommand());
        assertNull(dialog.getCancelButton().getActionListeners());
        assertNull(dialog.getCancelButton().getActionCommand());
        assertEquals(0, dialog.getButtons().length);
    }

    @Test
    public void testOkButtonActionListener() {
        dialog = new FontChooserDialog(c, "Test Title", true, chooserPane, okListener, cancelListener);
        dialog.getOkButton().doClick();
        verify(okListener).actionPerformed(any(ActionEvent.class));
        verify(dialog).setVisible(false);
    }

    @Test
    public void testCancelButtonActionListener() {
        dialog = new FontChooserDialog(c, "Test Title", true, chooserPane, okListener, cancelListener);
        dialog.getCancelButton().doClick();
        verify(cancelListener).actionPerformed(any(ActionEvent.class));
        verify(dialog).setVisible(false);
    }

    @Test
    public void testDefaultButton() {
        dialog = new FontChooserDialog(c, "Test Title", true, chooserPane, okListener, cancelListener);
        dialog.getOkButton().doClick();
        assertEquals(ActionEvent.ACTION_PERFORMED, dialog.getOkButton().getLastFocusedComponent().getActionCommand());
    }

    @Test
    public void testSetFont() {
        Font initialFont = new Font("Arial", Font.BOLD, 12);
        dialog = new FontChooserDialog(c, "Test Title", true, chooserPane, okListener, cancelListener);
        dialog.chooserPane.setFont(initialFont);
        dialog.setVisible(true);
        dialog.reset();
        assertEquals(initialFont, dialog.chooserPane.getFont());
    }

    @Test
    public void testSetVisible() {
        dialog = new FontChooserDialog(c, "Test Title", true, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.setVisible(false);
        verify(dialog).dispose();
    }
}