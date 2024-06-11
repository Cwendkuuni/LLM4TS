import static org.junit.Assert.*;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.ui.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {
    private MainMenu menu;
    private InspirentoMediator mediator;

    @Before
    public void setup() {
        mediator = new InspirentoMediator();
        menu = new MainMenu(mediator);
    }

    @Test
    public void testMainMenuConstruction() {
        // Test initialization
        assertNotNull(menu);
        // Test menu items creation
        String[] arr$;
        String[] menus = arr$ = UIResources.getString("menu").split("\\s+");
        for (String menu : arr$) {
            JMenu menuComponent = menu.getComponent(0);
            assertEquals(menu, menuComponent.getText());
            // Test menu items creation for submenus
            if (menu.endsWith(">")) {
                String[] subMenuItems = menu.substring(0, menu.length() - 1).split("\\s+");
                assertNotNull(menuComponent.getMenuComponent(0));
                JMenu subMenu = (JMenu) menuComponent.getMenuComponent(0);
                for (String item : subMenuItems) {
                    JMenu testSubMenu = menu.createMenu(item);
                    assertEquals(item, testSubMenu.getText());
                }
            }
            // Test menu items creation for non-submenus
            else {
                int index = -1;
                for (Component component : menuComponent.getComponents()) {
                    if (component instanceof JMenuItem && ((JMenuItem) component).getText().equals("Exit")) {
                        index = menuComponent.getComponentCount() - 1;
                        break;
                    }
                }
                assertEquals(Menu.class.getDeclaredMethod("getComponent", int.class), menuComponent.getClass().getMethod("getComponent", int.class));
                JMenuItem item = (JMenuItem) menuComponent.getComponent(index);
                assertNotNull(item);
            }
        }
    }
}