import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {
    private MainMenu mainMenu;
    private InspirentoMediator mediator;

    @Before
    public void setup() {
        mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testMainMenuConstruction() {
        // Test that menus are added to the JMenuBar
        String[] expectedMenus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
        int i = 0;
        JMenu menu;
        while ((menu = mainMenu.getComponent(i++)) != null) {
            assertEquals(expectedMenus[i - 1], menu.getText());
        }

        // Test that no exception is thrown when creating menus
        try {
            String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
            for (String menu : menus) {
               createMenu(mainMenu, menu);
            }
        } catch (MissingResourceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private void createMenu(JMenuBar menuBar, String menuName) {
        JMenu menu = menuBar.getComponent(getIndexOfMenu(menuName, menuBar));
        assertEquals(menuName.substring(0, menuName.length() - 2), menu.getText());
        String[] items = InspirentoUtilities.tokenize(UIResources.getString(menuName));
        int j = 0;
        JMenuItem item;
        while ((item = menu.getComponent(j++)) != null) {
            if (item instanceof JCheckBoxMenuItem) {
                JCheckBoxMenuItem checkboxItem = (JCheckBoxMenuItem) item;
                String itemKey = menuName + items[j];
                assertEquals(items[j].substring(1), checkboxItem.getText());
                assertFalse(checkboxItem.isSelected());
                checkboxItem.doClick();
                assertTrue(checkboxItem.isSelected());
                checkboxItem.doClick();
                assertFalse(checkboxItem.isSelected());
            } else {
                JMenuItem menuItem = (JMenuItem) item;
                String itemKey = menuName + items[j];
                assertEquals(items[j], menuItem.getText());
                menuItem.doClick();
                // TODO: Implement test for appropriate action when menu item is clicked
            }
        }
    }

    private int getIndexOfMenu(String menuName, JMenuBar menuBar) {
        int index = 0;
        JMenu menu;
        while ((menu = (JMenu) menuBar.getComponent(index++)) != null) {
            if (menu.getText().equals(menuName.substring(0, menuName.length() - 2))) {
                return index;
            }
        }
        fail("Menu not found: " + menuName);
        return -1;
    }
}