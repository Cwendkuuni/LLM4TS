import static org.junit.Assert.*;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.beanutils.PropertyDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configuration;
    private Context context;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        configuration = Mockito.mock(Configuration.class);
        context = Mockito.mock(Context.class);
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);
        automaticBean.contextualize(context);
    }

    // Test method: configure(Configuration config)
    @Test
    public void testConfigure() {
        assertNotNull(automaticBean.getConfiguration());
        assertEquals(configuration, automaticBean.getConfiguration());
    }

    // Test method: tryCopyProperty(String moduleName, String key, Object value, boolean recheck)

    @Test
    public void testTryCopyProperty_ValidStringValue() throws Exception {
        final String key = "testKey";
        final String value = "testValue";
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, value, false);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
        final Object actualValue = descriptor.getReadMethod().invoke(automaticBean);
        assertEquals(value, actualValue);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_NullValue() throws Exception {
        final String key = "testKey";
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, null, false);
        fail("Expecting CheckstyleException");
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_InvalidProperty() throws Exception {
        final String key = "doesNotExist";
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, "testValue", false);
        fail("Expecting CheckstyleException");
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_InvalidType() throws Exception {
        final String key = "testIntegerKey";
        final Object value = 123;
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, value, false);
        fail("Expecting CheckstyleException");
    }

    @Test
    public void testTryCopyProperty_RecheckValidStringValue() throws Exception {
        final String key = "testKey";
        final String originalValue = "originalValue";
        final String value = "testValue";
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, originalValue, true);
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, value, false);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
        final Object actualValue = descriptor.getReadMethod().invoke(automaticBean);
        assertEquals(value, actualValue);
    }

    // Test method: contextualize(Context context)
    @Test
    public void testContextualize() {
        final String key = "testKey";
        final String value = "testValue";
        Mockito.when(context.get(key)).thenReturn(value);
        automaticBean.contextualize(context);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
        final Object actualValue = descriptor.getReadMethod().invoke(automaticBean);
        assertEquals(value, actualValue);
    }

    // Test method: getConfiguration()
    @Test
    public void testGetConfiguration() {
        assertNotNull(automaticBean.getConfiguration());
    }

    // Test method: finishLocalSetup()
    @Test (expected = CheckstyleException.class)
    public void testFinishLocalSetup_InvalidChild() throws Exception {
        final Configuration childConf = Mockito.mock(Configuration.class);
        automaticBean.finishLocalSetup();
        automaticBean.setupChild(childConf);
        fail("Expecting CheckstyleException");
    }

    // Test method: setupChild(Configuration childConf)
    @Test (expected = CheckstyleException.class)
    public void testSetupChild_NullChild() throws Exception {
        automaticBean.setupChild(null);
        fail("Expecting CheckstyleException");
    }

    // Test method: setupChild(Configuration childConf) with valid child
    @Test
    public void testSetupChild_ValidChild() throws Exception {
        final Configuration childConf = Mockito.mock(Configuration.class);
        automaticBean.setupChild(childConf);
        // No exception is thrown, so the test is passed
    }

    // Test method: RelaxedStringArrayConverter
    @Test
    public void testRelaxedStringArrayConverter() {
        final Converter converter = new RelaxedStringArrayConverter();
        final Class type = String[].class;
        final Object value = "test,value,one,two";
        final Object convertedValue = converter.convert(type, value);
        assertArrayEquals(new String[]{"test", "value", "one", "two"}, convertedValue);
    }
}