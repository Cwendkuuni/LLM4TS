import static org.junit.Assert.assertNotNull;

public class MainMenuTest {
    // 

    @Test
    public void testMainMenuConstruction() {
        String menuString = "File,Edit,View";
        MainMenu menu = new MainMenu(null);

        String[] menuTokens = InspirentoUtilities.tokenize(menuString);
        String[] arr$;

        for (String menu : menuTokens) {
            JMenu jMenu = menu.createMenu(menu);
            assertNotNull(jMenu);
        }
    }

    // 
}