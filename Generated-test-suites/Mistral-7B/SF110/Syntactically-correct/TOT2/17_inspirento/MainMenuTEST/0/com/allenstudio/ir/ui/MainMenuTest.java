import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {
    @Test
    public void testMainMenuConstructor() {
        InspirentoMediator mediator = new InspirentoMediator(); // create valid object
        MainMenu mainMenu = new MainMenu(mediator);
        assertNotNull(mainMenu); // basic functionality check
    }

    @Test(expected = NullPointerException.class)
    public void testMainMenuWithNullMediator() {
        MainMenu mainMenu = new MainMenu(null);
    }
}