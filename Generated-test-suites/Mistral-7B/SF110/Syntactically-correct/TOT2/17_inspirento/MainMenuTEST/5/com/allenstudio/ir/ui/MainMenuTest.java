import org.junit.Assert;
import org.junit.Test;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

    @Test
    public void testCreateMenu() {
        MainMenu menu = new MainMenu(null);
        String menuLabel = "File";
        String expectedMenuLabel = "File";

        JMenu actualMenu = menu.createMenu(menuLabel);
        Assert.assertEquals(expectedMenuLabel, actualMenu.getText());
    }

    // Edge case testing for empty menus
    @Test
    public void testCreateMenuEmptyMenu() {
        MainMenu menu = new MainMenu(null);
        String menuLabel = "";
        JMenu actualMenu = menu.createMenu(menuLabel);
        Assert.assertNull(actualMenu);
    }

    // Exception handling testing for missing menu labels
    @Test(expected = MissingResourceException.class)
    public void testCreateMenuMissingMenuLabel() {
        MainMenu menu = new MainMenu(null);
        String menuLabel = "missingMenuLabel";
        menu.createMenu(menuLabel);
    }
}