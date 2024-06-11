import org.junit.Test;
import static org.junit.Assert.*;

import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.ui.MainMenuTest;
import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

    @Test
    public void testMainMenuCreation() {
        // Test if MainMenu constructor initializes correctly
        String menuString = "File*Open>File*Save>File*Save As>File*Exit>";
        MainMenu menu = new MainMenu(new InspirentoMediator());

        String[] menus = menu.getMenuNames();
        int expectedSize = 4;
        assertEquals(expectedSize, menus.length);

        for (int i = 0; i < menus.length; i++) {
            String expectedMenuName = menuString.split(">")[i];
            String actualMenuName = menus[i];
            assertEquals(expectedMenuName, actualMenuName);
        }
    }

    @Test
    public void testCreateMenu() {
        // Test if createMenu method creates JMenu correctly
        String menuString = "File*Open>File*Save>File*Save As>File*Exit>";
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenu actualMenu = menu.createMenu("File");

        String[] menuItems = actualMenu.getMenuComponents();
        int expectedSize = 4;
        assertEquals(expectedSize, menuItems.length);

        for (int i = 0; i < menuItems.length; i++) {
            String expectedMenuName = menuString.split(">")[i];
            String expectedItemName = "";
            if (!expectedMenuName.endsWith(">")) {
                int lastIndex = expectedMenuName.lastIndexOf('*');
                expectedItemName = expectedMenuName.substring(lastIndex + 1);
            }
            JMenuItem actualMenuItem = (JMenuItem) menuItems[i];
            String actualMenuItemName = actualMenuItem.getText();

            String expectedMenuItemName = UIResources.getString(expectedItemName + "Label");
            assertEquals(expectedMenuItemName, actualMenuItemName);
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test if addShortcutAndIcon method sets JMenuItem accelerator and icon correctly
        MainMenu menu = new MainMenu(new InspirentoMediator());

        JMenuItem fileOpenMenuItem = new JMenuItem("Open");
        menu.addShortcutAndIcon(fileOpenMenuItem, "FileOpen");

        String shortcut = "Ctrl+O";
        KeyStroke expectedAccelerator = KeyStroke.getKeyStroke(shortcut);
        assertEquals(expectedAccelerator, fileOpenMenuItem.getAccelerator());

        String iconPath = "icon/fileopen.png";
        URL expectedIconURL = MainMenuTest.class.getResource(iconPath);
        ImageIcon expectedIcon = new ImageIcon(expectedIconURL);
        assertEquals(expectedIcon, fileOpenMenuItem.getIcon());
    }
}