import org.junit.Test;
import static org.junit.Assert.*;
import com.allenstudio.ir.ui.MainMenu;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        String menuName = "TestMenu";
        JMenu testMenu = menu.createMenu(menuName);
        assertNotNull(testMenu);
    }

    @Test
    public void testAddShortcutAndIcon() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        String key = "TestKey";
        JMenuItem testItem = new JMenuItem();
        menu.addShortcutAndIcon(testItem, key);
        assertTrue(testItem.isAcceleratorAvailable());
    }
}