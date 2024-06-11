import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {
   @Test
   public void testConfigure() throws Exception {
      Configuration config = new Configuration();
      config.addAttribute("testAttribute", "testValue");
      AutomaticBean bean = new AutomaticBean();
      bean.configure(config);
      assertNotNull(bean.getConfiguration());
      assertNotNull(bean.getConfiguration().getAttribute("testAttribute"));
      assertNotNull(bean.getConfiguration().getAttribute("testAttribute").getValue());
      assertEquals("testValue", bean.getConfiguration().getAttribute("testAttribute").getValue());
   }
}