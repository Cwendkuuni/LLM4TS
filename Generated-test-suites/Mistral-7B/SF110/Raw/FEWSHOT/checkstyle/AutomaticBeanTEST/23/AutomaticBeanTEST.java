package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        final Configuration config = mock(Configuration.class);
        final AutomaticBean bean = new AutomaticBean();

        doReturn(Arrays.asList("attribute1", "attribute2")).when(config).getAttributeNames();

        bean.configure(config);

        assertNotNull(bean.getConfiguration());
        assertEquals(2, bean.getConfiguration().size());

        final String[] attributeNames = bean.getConfiguration().getAttributeNames();
        assertEquals("attribute1", attributeNames[0]);
        assertEquals("attribute2", attributeNames[1]);
        assertEquals("attribute1", bean.getConfiguration().getAttribute("attribute1"));
        assertEquals("attribute2", bean.getConfiguration().getAttribute("attribute2"));

        final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, "configuration");
        assertNotNull(propertyDescriptor);
        assertEquals("configuration", propertyDescriptor.getName());
        assertEquals(Configuration.class, propertyDescriptor.getPropertyType());
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWrongType() throws Exception {
        final Configuration config = mock(Configuration.class);
        final AutomaticBean bean = new AutomaticBean();

        doReturn(new String[]{"attribute1", "attribute2"}).when(config).getAttributeNames();

        doThrow(new CheckstyleException("Test error")).when(config).getAttribute("testAttribute");

        bean.configure(config);

        final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, "configuration");
        assertNotNull(propertyDescriptor);
        assertEquals("configuration", propertyDescriptor.getName());
        assertEquals(Configuration.class, propertyDescriptor.getPropertyType());

        final Method method = MethodUtils.getAccessibleMethod(AutomaticBean.class, "tryCopyProperty", String.class, String.class, Object.class, boolean.class);
        method.invoke(bean, "moduleName", "testAttribute", new Object(), true);

        verify(config, times(1)).getAttribute("testAttribute");
        verify(config, times(1)).getAttributeNames();
    }

    @Test
    public void testContextualize() throws Exception {
        final Context context = mock(Context.class);
        final AutomaticBean bean = new AutomaticBean();

        doReturn(Arrays.asList("contextAttribute1", "contextAttribute2")).when(context).getAttributeNames();

        bean.contextualize(context);

        final Object configuration = bean.getConfiguration();
        assertNotNull(configuration);
        assertEquals(2, configuration.size());

        final String[] attributeNames = ((Configuration) configuration).getAttributeNames();
        assertEquals("contextAttribute1", attributeNames[0]);
        assertEquals("contextAttribute2", attributeNames[1]);
        assertEquals("contextAttribute1", configuration.get("contextAttribute1"));
        assertEquals("contextAttribute2", configuration.get("contextAttribute2"));

        final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, "configuration");
        assertNotNull(propertyDescriptor);
        assertEquals("configuration", propertyDescriptor.getName());
        assertEquals(Configuration.class, propertyDescriptor.getPropertyType());
    }

    @Test
    public void testCreateBeanUtilsBean() {
        final ConvertUtilsBean cub = new ConvertUtilsBean();
        final PropertyUtilsBean pub = new PropertyUtilsBean();

        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();

        assertEquals(cub, beanUtils.getConvertUtils());
        assertEquals(pub, beanUtils.getPropertyUtils());
    }

    @Test
    public void testTryCopyProperty() throws Exception {
        final Object target = new Object();
        final Configuration config = mock(Configuration.class);
        final AutomaticBean bean = new AutomaticBean();
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();

        doReturn(new PropertyDescriptor("configuration", target.getClass())).when(config).getPropertyDescriptor("configuration");

        bean.tryCopyProperty("moduleName", "configuration", config, true);

        assertNotNull(bean.getConfiguration());
        assertEquals(config, bean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyNullConfig() throws Exception {
        final Object target = new Object();
        final AutomaticBean bean = new AutomaticBean();

        bean.tryCopyProperty("moduleName", null, null, true);

        fail("An exception should have been thrown");
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyNullPropertyName() throws Exception {
        final Object target = new Object();
        final Configuration config = mock(Configuration.class);
        final AutomaticBean bean = new AutomaticBean();

        bean.tryCopyProperty(null, "configuration", config, true);

        fail("An exception should have been thrown");
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyNullValue() throws Exception {
        final Object target = new Object();
        final Configuration config = mock(Configuration.class);
        final AutomaticBean bean = new AutomaticBean();

        bean.tryCopyProperty("moduleName", "configuration", null, true);

        fail("An exception should have been thrown");
    }

    @Test
    public void testTryCopyPropertyValueTypeMismatch() throws Exception {
        final Object target = new Object();
        final Configuration config = mock(Configuration.class);
        final AutomaticBean bean = new AutomaticBean();

        doReturn(new PropertyDescriptor("configuration", target.getClass())).when(config).getPropertyDescriptor("configuration");

        try {
            bean.tryCopyProperty("moduleName", "configuration", new Object(), true);
            fail("An exception should have been thrown");
        } catch (RuntimeException e) {
            assertEquals("illegal value 'null' for property 'configuration' of module moduleName", e.getMessage());
        }
    }

    @Test
    public void testTryCopyPropertyValueConversion() throws Exception {
        final Object target = new Object();
        final Configuration config = mock(Configuration.class);
        final AutomaticBean bean = new AutomaticBean();
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();

        doReturn(new PropertyDescriptor("configuration", target.getClass())).when(config).getPropertyDescriptor("configuration");

        bean.tryCopyProperty("moduleName", "configuration", "123", true);

        assertEquals(123, ((Number) bean.getConfiguration().get("configuration")).intValue());
    }

    @Test
    public void testTryCopyPropertyValueConversionArray() throws Exception {
        final Object target = new Object();
        final Configuration config = mock(Configuration.class);
        final AutomaticBean bean = new AutomaticBean();
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();

        doReturn(new PropertyDescriptor("configuration", target.getClass()))
                .when(config).getPropertyDescriptor("configuration");

        bean.tryCopyProperty("moduleName", "configuration", new Object[]{"1", "2"}, true);

        final Configuration configuration = bean.getConfiguration();

        assertEquals(configuration.getClass(), Configuration.class);
        assertEquals(2, configuration.size());

        final Object[] configurationArray = ((Configuration) configuration).get("configuration");
        assertEquals(configurationArray.getClass(), Object[].class);
        assertEquals(2, configurationArray.length);

        assertEquals(1, ((Number) configurationArray[0]).intValue());
        assertEquals(2, ((Number) configurationArray[1]).intValue());
    }
}