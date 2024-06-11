import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

 private AutomaticBean automaticBean;
 private Configuration configuration;

 @Before
 public void setup() {
 automaticBean = new AutomaticBean();
 configuration = new Configuration();
 }

 @Test
 public void testConfigure() throws Exception {
 // testing configuration with valid properties
 configuration.addProperty("property1", "value1");
 configuration.addProperty("property2", "value2");
 automaticBean.configure(configuration);
 assertNotNull(automaticBean.getConfiguration());
 assertNotNull(automaticBean.getConfiguration().getAttribute("property1"));
 assertNotNull(automaticBean.getConfiguration().getAttribute("property2"));
 assertSame(value1, automaticBean.getConfiguration().getAttribute("property1").getValue());
 assertSame(value2, automaticBean.getConfiguration(). Attribute("property2").getValue());

 // testing configuration with missing property
 configuration.clearProperties();
 configuration.addProperty("property1", "value1");
 automaticBean.configure(configuration);
 assertNotNull(automaticBean.getConfiguration());
 assertNull(automaticBean.getConfiguration().getAttribute("property2"));

 // testing configuration with invalid property type
 configuration.clearProperties();
 configuration.addProperty("property1", "value1");
 configuration.addProperty("property2", new Object[]{1, 2, 3});
 automaticBean.configure(configuration);
 assertNotNull(automaticBean.getConfiguration());
 assertNull(automaticBean.getConfiguration().getAttribute("property2"));

 // testing configuration with empty list
 configuration.clearProperties();
 configuration.addProperty("property1", "value1");
 configuration.addProperty("property2", Lists.newArrayList());
 automaticBean.configure(configuration);
 assertNotNull(automaticBean.getConfiguration());
 assertNull(automaticBean.getConfiguration().getAttribute("property2"));
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidProperty() throws Exception {
 configuration.clearProperties();
 configuration.addProperty("property1", "value1");
 configuration.addProperty("property2", 123);
 automaticBean.configure(configuration);
 fail("Should throw CheckstyleException");
 }

 @Test
 public void testContextualize() throws Exception {
 // testing contextualize with valid properties
 automaticBean.contextualize(configuration);
 assertNotNull(automaticBean.getConfiguration());
 assertNotNull(automaticBean.getConfiguration().getAttribute("property1"));
 assertNotNull(automaticBean.getConfiguration().getAttribute("property2"));
 assertSame(value1, automaticBean.getConfiguration().getAttribute("property1").getValue());
 assertSame(value2, automaticBean.getConfiguration(). Attribute("property2").getValue());

 // testing contextualize with missing property
 automaticBean.contextualize(new Configuration());
 assertNotNull(automaticBean.getConfiguration());
 assertNull(automaticBean.getConfiguration().getAttribute("property2"));
 }
}