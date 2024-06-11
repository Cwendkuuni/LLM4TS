import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class AutomaticBeanTest {

 private AutomaticBean automaticBean;
 private Configuration configuration;

 @Before
 public void setUp() {
 automaticBean = new AutomaticBean();
 configuration = Mockito.mock(Configuration.class);
 }

 @Test
 public void testConfigureWithValidValues() throws Exception {
 // Given
 String[] attributeNames = new String[]{"attribute1", "attribute2"};
 Configuration config = new Configuration();
 config.addAttribute("attribute1", "value1");
 config.addAttribute("attribute2", "value2");
 config.setChildren(new Configuration[0]);

 // When
 automaticBean.configure(config);

 // Then
 Assert.assertSame(config, automaticBean.getConfiguration());
 Assert.assertEquals("value1", automaticBean.getConfiguration().getAttribute("attribute1"));
 Assert.assertEquals("value2", automaticBean.getConfiguration().getAttribute("attribute2"));

 // Verify BeanUtils.copyProperty was called correctly
 Mockito.verify(automaticBean).tryCopyProperty(automaticBean.getClass().getName(), "attribute1", "value1", true);
 Mockito.verify(automaticBean).tryCopyProperty(automaticBean.getClass().getName(), "attribute2", "value2", true);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithMissingAttribute() throws Exception {
 // Given
 String[] attributeNames = new String[]{"attribute1", "attribute2"};
 Configuration config = new Configuration();
 config.setChildren(new Configuration[0]);

 // When
 automaticBean.configure(config);

 // Then
 Assert.assertSame(config, automaticBean.getConfiguration());
 Assert.assertEquals(0, config.getAttributeNames().length);

 // Verify an exception is thrown when trying to copy a non-existent property
 Mockito.verifyNoInteractions(automaticBean);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidValueType() throws Exception {
 // Given
 String[] attributeNames = new String[]{"attribute1"};
 Configuration config = new Configuration();
 config.addAttribute("attribute1", new Object()[0]);
 config.setChildren(new Configuration[0]);

 // When
 automaticBean.configure(config);

 // Then
 Assert.assertSame(config, automaticBean.getConfiguration());
 Assert.assertEquals(0, config.getAttributeNames().length);

 // Verify an exception is thrown when trying to copy a property with an invalid value type
 Mockito.verifyNoInteractions(automaticBean);
 }

 @Test
 public void testContextualizeWithValidValues() throws Exception {
 // Given
 Context context = Mockito.mock(Context.class);
 context.put("attribute1", "value1");
 context.put("attribute2", "value2");

 // When
 automaticBean.contextualize(context);

 // Then
 Assert.assertEquals("value1", automaticBean.getConfiguration().getAttribute("attribute1"));
 Assert.assertEquals("value2", automaticBean.getConfiguration().getAttribute("attribute2"));
 }

}