package com.example.automaticbean;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private BeanUtilsBean beanUtils;

    @Before
    public void setup() {
        automaticBean = new AutomaticBean();
        beanUtils = new BeanUtilsBean();
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration config = new Configuration();
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullConfig() throws CheckstyleException {
        automaticBean.configure(null);
    }

    @Test
    public void testTryCopyPropertyWithValidValues() throws Exception {
        String moduleName = "test";
        String key = "testKey";
        Object value = "testValue";
        automaticBean.tryCopyProperty(moduleName, key, value, true);
        assertSame(value, PropertyUtils.getPropertyValue(automaticBean, key));
    }

    @Test
    public void testTryCopyPropertyWithNullValue() throws Exception {
        String moduleName = "test";
        String key = "testKey";
        automaticBean.tryCopyProperty(moduleName, key, null, true);
        assertNull(PropertyUtils.getPropertyValue(automaticBean, key));
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidKey() throws Exception {
        String moduleName = "test";
        String key = "testKey";
        Object value = "testValue";
        automaticBean.tryCopyProperty(moduleName, null, value, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidModuleName() throws Exception {
        String moduleName = null;
        String key = "testKey";
        Object value = "testValue";
        automaticBean.tryCopyProperty(moduleName, key, value, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidValueType() throws Exception {
        String moduleName = "test";
        String key = "testKey";
        Object value = new Object();
        automaticBean.tryCopyProperty(moduleName, key, value, true);
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = new Context();
        context.put("testKey", "testValue");
        automaticBean.contextualize(context);
        assertSame("testValue", PropertyUtils.getPropertyValue(automaticBean, "testKey"));
    }

    @Test
    public void testContextualizeWithEmptyContext() throws CheckstyleException {
        Context context = new Context();
        automaticBean.contextualize(context);
        assertNull(PropertyUtils.getPropertyValue(automaticBean, "nonExistentKey"));
    }

    @Test
    public void testGetConfiguration() {
        Configuration config = new Configuration();
        automaticBean.configure(config);
        assertSame(config, automaticBean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNullConfig() throws CheckstyleException {
        Configuration childConf = null;
        automaticBean.setupChild(childConf);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithValidConfig() throws CheckstyleException {
        Configuration childConf = new Configuration();
        automaticBean.setupChild(childConf);
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        String input = ",test1,test2, test3, test4 , test5";
        Class<?> type = String[].class;
        Object convertedValue = beanUtils.getConverter(type).convert(type, input);
        List<String> expectedList = Lists.newArrayList("test1", "test2", "test3", "test4", "test5");
        List<String> actualList = ListUtils.cast(convertedValue);
        assertArrayEquals(expectedList.toArray(new String[expectedList.size()]), actualList.toArray(new String[0]));
    }

}