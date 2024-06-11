import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class MainMenuTest {

    @Test
    public void testMainMenuConstructor() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        assertNotNull(mainMenu);
    }

    // Test for createMenu method here

    // Test for addShortcutAndIcon method here
}