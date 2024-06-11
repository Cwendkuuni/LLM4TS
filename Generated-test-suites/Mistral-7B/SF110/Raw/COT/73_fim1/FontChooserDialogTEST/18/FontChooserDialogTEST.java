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
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        assertNotNull(dialog);
        dialog.setVisible(true); // Test visibility after constructor call
        dialog.setVisible(false); // Close dialog
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

        // Test setting dialog to visible
        dialog.setVisible(true);
        assertTrue(dialog.isVisible());
        dialog.setVisible(false); // Close dialog

        // Test setting dialog to invisible
        dialog.setVisible(false);
        assertFalse(dialog.isVisible());
    }

    @Test
    public void testReset() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);

        dialog.setVisible(true); // Open dialog
        dialog.reset(); // Reset font
        assertEquals(dialog.chooserPane.getFont(), dialog.initialFont); // Check font has been reset to initial font
        dialog.setVisible(false); // Close dialog
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFontChooserDialogConstructorNullComponent() {
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        new FontChooserDialog(null, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullTitle() {
        Component c = new JFrame();
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        new FontChooserDialog(c, null, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullModal() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        new FontChooserDialog(c, title, null, chooserPane, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullChooserPane() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        new FontChooserDialog(c, title, modal, null, okListener, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullOkListener() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        new FontChooserDialog(c, title, modal, chooserPane, null, cancelListener);
    }

    @Test(expected = NullPointerException.class)
    public void testFontChooserDialogConstructorNullCancelListener() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        new FontChooserDialog(c, title, modal, chooserPane, okListener, null);
    }

    @Test(expected = HeadlessException.class)
    public void testFontChooserDialogHeadless() {
        Component c = new JLabel(); // Headless environment
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
    }

    @Test(expected = IllegalStateException.class)
    public void testFontChooserDialogSetVisibleTwice() {
        Component c = new JFrame();
        String title = "Font Chooser Dialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = e -> System.out.println("OK clicked");
        ActionListener cancelListener = e -> System.out.println("Cancel clicked");
        FontChooserDialog dialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        dialog.setVisible(true);
        dialog.setVisible(true);
    }
}