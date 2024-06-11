import static org.junit.Assert.*;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.core.InspirentoMediator;

public class MainMenuTest {
    

    @Test
    public void testMainMenuConstruction() {
        String menuString = "File*Open>Save*Save As>Preferences*Help";
        String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
        assertEquals("Number of menus created does not match expected", 1, menus.length);
        JMenuBar menuBar = new MainMenu(new InspirentoMediator(new Object()));
        assertEquals("Menu bar created is not instance of MainMenu", MainMenu.class, menuBar.getClass());
        JMenu fileMenu = (JMenu) menuBar.getComponent(0);
        String[] fileMenuItems = fileMenu.getMenuComponents();
        assertEquals("Number of items in File menu does not match expected", 3, fileMenuItems.length);
        assertEquals("First item in File menu does not match expected label", "Open", fileMenuItems[0].getText());
        assertEquals("Second item in File menu does not match expected label", "Save", fileMenuItems[1].getText());
        assertEquals("Third item in File menu does not match expected label", "Save As", fileMenuItems[2].getText());
        assertTrue("File menu does not have a checkbox item for Preferences", fileMenu.getComponentCount() > 3);
        JCheckBoxMenuItem preferencesMenuItem = (JCheckBoxMenuItem) fileMenu.getComponent(3);
        assertTrue("Preferences item does not have a label", preferencesMenuItem.getText().length() > 0);
        
    }
    
    // Other test cases for individual methods can be added here
}