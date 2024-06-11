import static org.junit.Assert.*;

import javax.swing.*;

import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {
    private MainMenu mainMenu;
    private InspirentoMediator mediator;

    @Before
    public void setUp() {
        mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testMainMenuConstruction() {
        assertNotNull(mainMenu);
    }

    // ... other test cases
}