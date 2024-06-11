package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertyEditorManager.class, PropertyUtilsBean.class, BeanUtilsBean.class, AutomaticBean.class})
@SuppressStaticInitializationFor("org.apache.commons.beanutils.BeanUtilsBean")
public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertEquals(config, bean.getConfiguration());
    }

    @Test
    public void testConfigure_nullConfig() {
        AutomaticBean bean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> bean.configure(null));
    }

    @Test
    public void testTryCopyProperty_boolean() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "booleanProperty");
        bean.tryCopyProperty("testModule", "booleanProperty", true, false);
        assertTrue(bean.booleanProperty);
    }

    @Test
    public void testTryCopyProperty_boolean_nullValue() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "booleanProperty");
        bean.tryCopyProperty("testModule", "booleanProperty", null, false);
        assertFalse(bean.booleanProperty);
    }

    @Test
    public void testTryCopyProperty_boolean_invalidValue() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "booleanProperty");
        bean.tryCopyProperty("testModule", "booleanProperty", "invalidValue", false);
        assertFalse(bean.booleanProperty);
    }

    @Test
    public void testTryCopyProperty_boolean_recheck() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "booleanProperty");
        bean.tryCopyProperty("testModule", "booleanProperty", true, true);
        assertTrue(bean.booleanProperty);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_boolean_propertyNotFound() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "nonExistentProperty", true, true);
    }

    @Test
    public void testTryCopyProperty_int() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "intProperty", 42, false);
        assertEquals(42, bean.intProperty);
    }

    @Test
    public void testTryCopyProperty_int_nullValue() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "intProperty", null, false);
        assertEquals(0, bean.intProperty);
    }

    @Test
    public void testTryCopyProperty_int_invalidValue() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "intProperty", "invalidValue", false);
        assertEquals(0, bean.intProperty);
    }

    @Test
    public void testTryCopyProperty_int_recheck() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "intProperty");
        bean.tryCopyProperty("testModule", "intProperty", 42, true);
        assertEquals(42, bean.intProperty);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_int_propertyNotFound() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "nonExistentProperty", 42, true);
    }

    @Test
    public void testTryCopyProperty_stringArray() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "stringArrayProperty", new String[]{"foo", "bar"}, false);
        assertArrayEquals(new String[]{"foo", "bar"}, bean.stringArrayProperty);
    }

    @Test
    public void testTryCopyProperty_stringArray_nullValue() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "stringArrayProperty", null, false);
        assertNull(bean.stringArrayProperty);
    }

    @Test
    public void testTryCopyProperty_stringArray_invalidValue() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "stringArrayProperty", "invalidValue", false);
        assertNull(bean.stringArrayProperty);
    }

    @Test
    public void testTryCopyProperty_stringArray_recheck() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "stringArrayProperty");
        bean.tryCopyProperty("testModule", "stringArrayProperty", new String[]{"foo", "bar"}, true);
        assertArrayEquals(new String[]{"foo", "bar"}, bean.stringArrayProperty);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_stringArray_propertyNotFound() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "nonExistentProperty", new String[]{"foo", "bar"}, true);
    }

    @Test
    public void testContextualize() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        Configuration config = new Configuration();
        config.putAttribute("testAttribute", "testValue");
        bean.contextualize(config);
        assertEquals("testValue", bean.testAttribute);
    }

    @Test
    public void testContextualize_noAttributes() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(new Configuration());
        assertNull(bean.testAttribute);
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        // No implementation in the test
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_validChildConfig() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        Configuration childConf = new Configuration();
        bean.setupChild(childConf);
    }

    @Test
    public void testRelaxedStringArrayConverter() throws Exception {
        Configuration config = new Configuration();
        config.setAttribute("testAttribute", "value1,value2,value3");

        BeanUtilsBean beanUtilsBean = createBeanUtilsBean();
        Object value = beanUtilsBean.getEditor(String[].class).getValueFromString("value1,value2,value3");

        List<String> expectedValue = ListUtils.union(Arrays.asList("value1"), Arrays.asList("value2", "value3"));
        List<String> convertedValue = (List<String>)beanUtilsBean.getPropertyEditorSupport(AutomaticBean.class).getEditor(String[].class).getValueFromString(config.getAttribute("testAttribute"));

        assertEquals(expectedValue, convertedValue);
    }

    @Test
    public void testCreateBeanUtilsBean() {
        BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertSame(beanUtilsBean.getBeanUtils(), new BeanUtilsBean());
        assertSame(beanUtilsBean.getPropertyUtils(), new PropertyUtilsBean());
    }
}