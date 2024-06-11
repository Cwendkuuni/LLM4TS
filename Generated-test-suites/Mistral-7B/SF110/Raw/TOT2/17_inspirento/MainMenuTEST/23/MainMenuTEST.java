import static org.junit.Assert.*;
import com.allenstudio.ir.ui.*;
import org.junit.Test;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        assertNotNull(menu);
    }

    // Test createMenu method here...

    // Test addShortcutAndIcon method here...
}