import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {
    private AutomaticBean automaticBean;
    private Configuration config;

    @Before
    public void setup() {
        config = new Configuration();
        config.addAttribute("moduleName", "TestModule");
        automaticBean = new AutomaticBean();
        automaticBean.configure(config);
    }

    @Test
    public void testConfigureBooleanProperty() {
        config.addAttribute("TestBooleanProperty", true);
        automaticBean.configure(config);
        assertTrue(automaticBean.getConfiguration().getBoolean("TestBooleanProperty"));
    }

    @Test
    public void testConfigureStringArrayProperty() {
        config.addAttribute("TestStringArrayProperty", "Value1, Value2");
        automaticBean.configure(config);
        assertArrayEquals(new String[]{"Value1", "Value2"}, automaticBean.getConfiguration().getStringArray("TestStringArrayProperty"));
    }

    @Test
    public void testConfigureInvalidProperty() {
        config.addAttribute("TestInvalidProperty", new Object());
        try {
            automaticBean.configure(config);
            fail("Should have thrown an exception");
        } catch (CheckstyleException e) {
            assertTrue(e.getMessage().contains("illegal value"));
        }
    }

    @Test
    public void testContextualizeBooleanProperty() {
        config.put("TestContextualizeBooleanProperty", true);
        automaticBean.contextualize(config);
        assertTrue(automaticBean.getConfiguration().getBoolean("TestContextualizeBooleanProperty"));
    }

    @Test
    public void testContextualizeStringArrayProperty() {
        config.put("TestContextualizeStringArrayProperty", "Value3, Value4");
        automaticBean.contextualize(config);
        assertArrayEquals(new String[]{"Value3", "Value4"}, automaticBean.getConfiguration().getStringArray("TestContextualizeStringArrayProperty"));
    }

    @Test
    public void testContextualizeInvalidProperty() {
        config.put("TestContextualizeInvalidProperty", new Object());
        try {
            automaticBean.contextualize(config);
            fail("Should have thrown an exception");
        } catch (CheckstyleException e) {
            assertTrue(e.getMessage().contains("illegal value"));
        }
    }
}