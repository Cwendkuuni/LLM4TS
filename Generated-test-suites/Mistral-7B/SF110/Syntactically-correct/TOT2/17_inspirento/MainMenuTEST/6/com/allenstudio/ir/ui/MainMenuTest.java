import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testConstructor() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        assertNotNull(mainMenu);
    }
}