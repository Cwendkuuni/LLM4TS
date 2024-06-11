import static org.junit.Assert.*;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.core.InspirentoMediator;

public class MainMenuTest {
    

    @Test
    public void testMainMenuConstruction() {
        String menuString = "File*Open>NewWindow*SaveAs>Save>Save As*Preferences";
        InspirentoMediator mockMediator = mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mockMediator);

        // Check that the correct number of menus and menu items were created
        assertEquals(5, menu.getMenuCount());
        JMenu fileMenu = (JMenu) menu.getComponent(0);
        assertEquals(4, fileMenu.getMenuComponentCount());
        JMenu openMenu = (JMenu) fileMenu.getComponent(0);
        assertEquals(1, openMenu.getMenuComponentCount());
        JMenuItem newWindowItem = (JMenuItem) openMenu.getComponent(0);
        assertEquals("Open", newWindowItem.getText());
        JMenu saveMenu = (JMenu) fileMenu.getComponent(1);
        assertEquals(2, saveMenu.getMenuComponentCount());
        JMenuItem saveAsItem = (JMenuItem) saveMenu.getComponent(1);
        assertEquals("Save As", saveAsItem.getText());
        JMenu preferencesMenu = (JMenu) menu.getComponent(4);
        assertEquals(1, preferencesMenu.getMenuComponentCount());

        // Check that mnemonics and shortcuts are set correctly
        JMenuItem openItem = (JMenuItem) openMenu.getComponent(1);
        assertEquals('O', openItem.getMnemonic());
        assertEquals("Ctrl+O", openItem.getAccelerator().toString());
        JMenuItem saveAsItem = (JMenuItem) saveMenu.getComponent(1);
        assertEquals('S', saveAsItem.getMnemonic());
        assertEquals("Ctrl+S", saveAsItem.getAccelerator().toString());

        // Check that icons are set correctly (assuming that the icons are available in the classpath)
        JMenuItem saveItem = (JMenuItem) saveMenu.getComponent(0);
        assertNotNull(saveItem.getIcon());

        // Check that menus and items are added to the JMenuBar correctly
        JMenuBar menuBar = (JMenuBar) menu.getParent();
        assertEquals(1, menuBar.getComponentCount());
        assertEquals(menu, menuBar.getComponent(0));
    }

    
}