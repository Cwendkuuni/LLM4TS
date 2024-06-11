import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;

public class FontChooserDialogTest {
    @Test
    public void testFontChooserDialogConstruction() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        assertNotNull(fontChooserDialog);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogNullComponent() {
        Component c = null;
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFontChooserDialogNullTitle() {
        Component c = new JFrame();
        String title = null;
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        try {
            new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        } catch (IllegalArgumentException iae) {
            fail("Expected IllegalArgumentException, but got " + iae.getClass().getName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFontChooserDialogEmptyTitle() {
        Component c = new JFrame();
        String title = "";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        try {
            new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        } catch (IllegalArgumentException iae) {
            fail("Expected IllegalArgumentException, but got " + iae.getClass().getName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFontChooserDialogFalseModalComponent() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = false;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        try {
            new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        } catch (IllegalArgumentException iae) {
            fail("Expected IllegalArgumentException, but got " + iae.getClass().getName());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogNullOkListener() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = null;
        ActionListener cancelListener = e -> {};
        try {
            new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        } catch (NullPointerException npe) {
            fail("Expected NullPointerException, but got " + npe.getClass().getName());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogNullCancelListener() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = null;
        try {
            new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        } catch (NullPointerException npe) {
            fail("Expected NullPointerException, but got " + npe.getClass().getName());
        }
    }

    @Test
    public void testSetVisibleTrue() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testSetVisibleFalse() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.setVisible(false);
        assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testReset() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.reset();
        assertEquals(fontChooserDialog.getInitialFont(), fontChooserPane.getFont());
    }

    @Test
    public void testSetVisibleTwiceTrue() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.setVisible(true);
        assertTrue(fontChooserDialog.isVisible());
    }

    @Test
    public void testSetVisibleTwiceFalse() {
        Component c = new JFrame();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> {};
        ActionListener cancelListener = e -> {};
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        fontChooserDialog.setVisible(true);
        fontChooserDialog.setVisible(false);
        fontChooserDialog.setVisible(false);
        assertFalse(fontChooserDialog.isVisible());
    }
}