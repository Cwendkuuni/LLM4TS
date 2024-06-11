package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstruction() {
        Component c = new JFrame();
        FontChooserDialog dialog = new FontChooserDialog(c, "Test", true, null, null, null);
        assertNotNull(dialog);
    }

    @Test
    public void testOkButtonActionPerformed() {
        Component c = new JFrame();
        ActionListener okListener = mock(ActionListener.class);
        FontChooserDialog dialog = new FontChooserDialog(c, "Test", true, null, okListener, null);
        dialog.setVisible(true);
        dialog.okButton.doClick();
        verify(okListener).actionPerformed(argThat(isA(ActionEvent.class)));
        dialog.setVisible(false);
    }

    @Test
    public void testCancelButtonActionPerformed() {
        Component c = new JFrame();
        ActionListener cancelListener = mock(ActionListener.class);
        FontChooserDialog dialog = new FontChooserDialog(c, "Test", true, null, null, cancelListener);
        dialog.setVisible(true);
        dialog.cancelButton.doClick();
        verify(cancelListener).actionPerformed(argThat(isA(ActionEvent.class)));
        dialog.setVisible(false);
    }

    @Test
    public void testResetButtonActionPerformed() {
        Component c = new JFrame();
        FontChooserDialog dialog = new FontChooserDialog(c, "Test", true, null, null, null);
        dialog.setVisible(true);
        dialog.resetButton.doClick();
        assertNotNull(dialog.chooserPane);
        assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
        dialog.reset();
        assertNull(dialog.initialFont);
        assertNotNull(dialog.chooserPane.getFont());
        dialog.setVisible(false);
    }

    @Test
    public void testSetVisible() {
        Component c = new JFrame();
        FontChooserDialog dialog = new FontChooserDialog(c, "Test", true, null, null, null);
        dialog.setVisible(true);
        dialog.setVisible(false);
    }
}