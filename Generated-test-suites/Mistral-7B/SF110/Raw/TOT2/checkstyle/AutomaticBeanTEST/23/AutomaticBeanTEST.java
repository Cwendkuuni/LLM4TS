import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {
    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
    }
}