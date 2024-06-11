package osa.ora.server.client.ui.utils.test;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static org.junit.Assert.*;

public class FontChooserDialogTest {

    private JFrame frame;
    private Component c;
    private FontChooserDialog fontChooserDialog;

    @Before
    public void setUp() {
        frame = new JFrame();
        c = new JButton();
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, "Font Chooser Dialog", true, null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("OK button clicked");
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel button clicked");
            }
        });
    }

    @Test
    public void testFontChooserDialogConstructor() {
        assertNotNull(fontChooserDialog);
        assertEquals("Font Chooser Dialog", fontChooserDialog.getTitle());
        assertTrue(fontChooserDialog.isModal());
        assertNotNull(fontChooserDialog.getOkButton());
        assertNotNull(fontChooserDialog.getCancelButton());
    }

    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.chooserPane.getFont());
        fontChooserDialog.setVisible(false);
    }

    @Test
    public void testReset() {
        fontChooserDialog.setVisible(true);
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.chooserPane.getFont());
        fontChooserDialog.setVisible(false);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogNullComponent() {
        new FontChooserDialog(null, "Font Chooser Dialog", true, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogNullListener() {
        new FontChooserDialog(c, "Font Chooser Dialog", true, null, null, null);
    }

    @Test
    public void testActionPerformedOkButton() {
        fontChooserDialog.getOkButton().doClick();
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testActionPerformedCancelButton() {
        fontChooserDialog.getCancelButton().doClick();
        assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testActionPerformedResetButton() {
        fontChooserDialog.setVisible(true);
        fontChooserDialog.getResetButton().doClick();
        assertEquals(fontChooserDialog.getInitialFont(), fontChooserDialog.chooserPane.getFont());
        fontChooserDialog.setVisible(false);
    }

    @Test
    public void testWindowClosing() {
        fontChooserDialog.addWindowListener(new FontChooserDialog.Closer());
        fontChooserDialog.setVisible(true);
        WindowEvent windowEvent = new WindowEvent(fontChooserDialog, WindowEvent.WINDOW_CLOSING);
        fontChooserDialog.dispatchEvent(windowEvent);
        assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testComponentHidden() {
        fontChooserDialog.addComponentListener(new FontChooserDialog.DisposeOnClose());
        fontChooserDialog.setVisible(true);
        ComponentEvent componentEvent = new ComponentEvent(fontChooserDialog, ComponentEvent.COMPONENT_HIDDEN);
        fontChooserDialog.dispatchEvent(componentEvent);
        assertFalse(fontChooserDialog.isDisplayable());
    }
}