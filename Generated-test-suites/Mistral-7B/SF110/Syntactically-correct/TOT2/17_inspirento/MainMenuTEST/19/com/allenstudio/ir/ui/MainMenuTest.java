import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstructor() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
    }
}