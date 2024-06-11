package com.example.automaticbean;

import static org.junit.Assert.*;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AutomaticBean.class, BeanUtils.class, PropertyUtils.class, ConvertUtilsBean.class})
public class AutomaticBeanTest {

    private static final Configuration TEST_CONFIGURATION = new Configuration();

    @Test
    public void testConfigure() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        automaticBean.configure(config);
        assertEquals(config, automaticBean.getConfiguration());
        final String[] attributeNames = config.getAttributeNames();
        for (final String key : attributeNames) {
            final Object value = config.getAttribute(key);
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), key, value, true);
            final String getterName = "get" + Character.toUpperCase(key.charAt(0)) + key.substring(1);
            final Method getMethod = automaticBean.getClass().getMethod(GetterUtils.getGetterName(getterName));
            final Object actualValue = getMethod.invoke(automaticBean);
            assertEquals(value, actualValue);
        }

        final Configuration childConfig = new Configuration();
        automaticBean.configure(childConfig);
        assertThrows(CheckstyleException.class, () -> automaticBean.setupChild(childConfig));
    }

    @Test
    public void testContextualize() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Context context = Mockito.mock(Context.class);
        PowerMockito.whenNew(Context.class).withAnyArguments().thenReturn(context);
        final Collection<String> attributes = Arrays.asList("attribute1", "attribute2");
        PowerMockito.when(context.getAttributeNames()).thenReturn(attributes.iterator());
        PowerMockito.when(context.get("attribute1")).thenReturn("value1");
        PowerMockito.when(context.get("attribute2")).thenReturn("value2");

        automaticBean.contextualize(context);

        final String[] attributeNames = automaticBean.getConfiguration().getAttributeNames();
        assertEquals(2, attributeNames.length);
        assertEquals("attribute1", attributeNames[0]);
        assertEquals("attribute2", attributeNames[1]);

        final Object attribute1Value = automaticBean.getConfiguration().getAttribute("attribute1");
        assertEquals("value1", attribute1Value);

        final Object attribute2Value = automaticBean.getConfiguration().getAttribute("attribute2");
        assertEquals("value2", attribute2Value);
    }

    @Test
    public void testTryCopyProperty_String() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("testString", "testValue");
        automaticBean.configure(config);
        automaticBean.tryCopyProperty("test", "testString", "testValue", true);

        final String testString = (String) PropertyUtils.getProperty(automaticBean, "testString");
        assertEquals("testValue", testString);
    }

    @Test
    public void testTryCopyProperty_Boolean() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("testBoolean", true);
        automaticBean.configure(config);
        automaticBean.tryCopyProperty("test", "testBoolean", true, true);

        final boolean testBoolean = (boolean) PropertyUtils.getProperty(automaticBean, "testBoolean");
        assertTrue(testBoolean);
    }

    @Test
    public void testTryCopyProperty_Integer() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("testInteger", 10);
        automaticBean.configure(config);
        automaticBean.tryCopyProperty("test", "testInteger", 10, true);

        final int testInteger = (int) PropertyUtils.getProperty(automaticBean, "testInteger");
        assertEquals(10, testInteger);
    }

    @Test
    public void testTryCopyProperty_ArrayOfBoolean() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        final Object testBooleanArrayObject = new Object[] {true, false};
        config.setAttribute("testBooleanArray", testBooleanArrayObject);
        automaticBean.configure(config);
        automaticBean.tryCopyProperty("test", "testBooleanArray", testBooleanArrayObject, true);

        final boolean[] testBooleanArray = (boolean[]) PropertyUtils.getProperty(automaticBean, "testBooleanArray");
        assertArrayEquals(new boolean[] {true, false}, testBooleanArray);
    }

    @Test
    public void testTryCopyProperty_ExceptionIfPropertyNotFound() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        automaticBean.configure(config);
        final String notExistingProperty = "notExistingProperty";
        assertThrows(CheckstyleException.class, () -> automaticBean.tryCopyProperty("test", notExistingProperty, null, true));
    }

    @Test
    public void testTryCopyProperty_IllegalValue() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("testIllegalValue", "not-a-boolean");
        automaticBean.configure(config);
        assertThrows(RuntimeException.class, () -> automaticBean.tryCopyProperty("test", "testIllegalValue", "not-a-boolean", true));
    }

    @Test
    public void testTryCopyProperty_IllegalAccess() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("testIllegalAccess", new byte[0]);
        PowerMockito.mockStatic(PropertyUtils.class);
        PowerMockito.when(PropertyUtils.getPropertyDescriptor(automaticBean, "testIllegalAccess")).thenReturn(null);
        automaticBean.configure(config);
        assertThrows(InvocationTargetException.class, () -> automaticBean.tryCopyProperty("test", "testIllegalAccess", new byte[0], true));
    }

    @Test
    public void testTryCopyProperty_ConversionException() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("testConversionException", "not-a-number");
        automaticBean.configure(config);
        assertThrows(ConversionException.class, () -> automaticBean.tryCopyProperty("test", "testConversionException", "not-a-number", true));
    }

    @Test
    public void testCreateBeanUtilsBean() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final ConvertUtilsBean cub = automaticBean.createBeanUtilsBean();
        assertNotNull(cub);
        final BeanUtilsBean bub = (BeanUtilsBean) cub.getInnerBean();
        assertNotNull(bub);
    }

    private static Method GetterUtils_getGetterName(String getterName) throws NoSuchMethodException {
        final String getterMethodName = "get" + Character.toUpperCase(getterName.charAt(0)) + getterName.substring(1);
        final Class<?> clazz = GetterUtils.class;
        final Method getterMethod = clazz.getMethod("getterName", String.class);
        return getterMethod.invoke(null, getterMethodName);
    }
}