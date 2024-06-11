package osa.ora.server.client.ui.utils;

import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogCreation() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        assertNotNull(dialog);
        assertEquals(title, dialog.getTitle());
        assertEquals(modal, dialog.isModal());
        assertNotNull(dialog.getContentPane());
    }

    @Test
    public void testSetVisible() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true);
        assertNotNull(dialog.getInitialFont());

        dialog.setVisible(false);
        dialog.reset();
        assertEquals(dialog.getInitialFont(), dialog.chooserPane.getFont());
    }

    @Test
    public void testReset() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        Font initialFont = new Font("Arial", Font.BOLD, 12);
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener) {
            @Override
            public void setVisible(boolean visible) {
                if (visible) {
                    super.setVisible(visible);
                    this.initialFont = this.chooserPane.getFont();
                }
            }
        };
        dialog.setVisible(true);

        dialog.reset();
        assertEquals(initialFont, dialog.chooserPane.getFont());
    }

    @Test
    public void testActionListeners() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);

        dialog.getOkButton().doClick();
        assertTrue(okListener.isEnabled());
        okListener.actionPerformed(null);
        assertFalse(dialog.isVisible());

        dialog.getCancelButton().doClick();
        assertTrue(cancelListener.isEnabled());
        cancelListener.actionPerformed(null);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testKeyboardListeners() throws Exception {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ESCAPE);
        assertTrue(cancelListener.isEnabled());
        cancelListener.actionPerformed(null);
        assertFalse(dialog.isVisible());
    }
}