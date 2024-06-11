import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);

        // Check if configuration properties have been set
        String expectedPropertyValue = "expectedValue";
        config.setProperty(AutomaticBean.class.getSimpleName(), "propertyKey", expectedPropertyValue);
        automaticBean.configure(config);
        assertEquals(expectedPropertyValue, automaticBean.getConfiguration().getProperty("propertyKey"));
    }
}