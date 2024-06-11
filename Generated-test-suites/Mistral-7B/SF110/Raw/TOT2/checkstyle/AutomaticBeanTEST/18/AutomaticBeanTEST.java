import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertEquals(config, bean.getConfiguration());
    }
}