import static org.junit.Assert.*;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.core.InspirentoMediator;

public class MainMenuTest {
    @Test
    public void testMainMenuConstruction() {
        String expectedMenuString = UIResources.getString("menu");
        String[] menus = InspirentoUtilities.tokenize(expectedMenuString);
        InspirentoMediator mediator = new InspirentoMediator(); // Assuming this class can be instantiated
        MainMenu mainMenu = new MainMenu(mediator);

        JMenu[] actualMenus = mainMenu.getMenus();
        assertEquals(menus.length, actualMenus.length);
        for (int i = 0; i < menus.length; i++) {
            String menuName = menus[i];
            JMenu actualMenu = actualMenus[i];
            String expectedLabel = UIResources.getString(menuName + "Label");
            assertEquals(expectedLabel, actualMenu.getName());
        }
    }
}