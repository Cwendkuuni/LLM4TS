import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;
import javax.swing.*;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        assertNotNull(fontChooserDialog);
    }

    @Test
    public void testSetVisibleWithInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        Font initialFont = new Font("Arial", Font.BOLD, 12);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        fontChooserDialog.setVisible(true);
        assertEquals(initialFont, fontChooserDialog.chooserPane.getFont());
    }

    @Test
    public void testSetVisibleWithoutInitialFont() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogNullComponent() {
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        Component c = null;

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogNullTitle() {
        Component c = new JFrame();
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        new FontChooserDialog(c, null, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = HeadlessException.class)
    public void testFontChooserDialogHeadless() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        UIManager.put("Awt.Component.systemLookAndFeel.installedLookAndFeelID", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        System.setProperty("java.awt.headless", "true");

        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test
    public void testReset() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};

        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.initialFont, fontChooserDialog.chooserPane.getFont());
    }
}