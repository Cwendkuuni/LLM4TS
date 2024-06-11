import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import static org.junit.Assert.assertNotNull;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new Configuration();
        automaticBean.configure(configuration);
        assertNotNull(automaticBean.getConfiguration());
    }
}