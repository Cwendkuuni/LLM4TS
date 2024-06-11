import org.junit.Test;
import static org.junit.Assert.*;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.core.InspirentoMediator;
import java.awt.event.KeyEvent;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        // Create an instance of the MainMenu class with a valid mediator object
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu() {
        // Create an instance of the MainMenu class with a valid mediator object
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);

        // Test creating a menu with a simple label
        String menuLabel = "File";
        JMenu expectedMenu = new JMenu(menuLabel);
        JMenu actualMenu = menu.createMenu(menuLabel);
        assertEquals(expectedMenu, actualMenu);

        // Test creating a menu with a complex label (contains a mnemonic character)
        String menuLabelWithMnemonic = "File&";
        JMenu expectedMenuWithMnemonic = new JMenu(menuLabelWithMnemonic.substring(0, menuLabelWithMnemonic.length() - 1));
        expectedMenuWithMnemonic.setMnemonic(menuLabelWithMnemonic.charAt(menuLabelWithMnemonic.length() - 1));
        JMenu actualMenuWithMnemonic = menu.createMenu(menuLabelWithMnemonic);
        assertEquals(expectedMenuWithMnemonic, actualMenuWithMnemonic);
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Create an instance of the MainMenu class with a valid mediator object
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);

        // Test adding a shortcut and icon to a JMenuItem
        JMenuItem item = new JMenuItem();
        String key = "testKey";
        menu.addShortcutAndIcon(item, key);
        assertNotNull(item.getAccelerator());
        assertEquals(KeyEvent.VK_T, item.getAccelerator().getKeyCode());
        assertEquals(KeyEvent.CTRL_MASK, item.getAccelerator().getModifiersEx());
    }
}