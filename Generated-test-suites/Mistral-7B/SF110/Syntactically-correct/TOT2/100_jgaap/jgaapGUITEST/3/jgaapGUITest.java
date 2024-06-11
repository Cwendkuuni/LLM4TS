import org.junit.Assert;
import org.junit.Test;

public class jgaapGUITest {

    @Test
    public void testConstructor() {
        jgaapGUI gui = new jgaapGUI();
        Assert.assertNotNull(gui.driver);
        Assert.assertEquals(new BorderLayout(), gui.getLayout());
    }
}