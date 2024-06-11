package com.example.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
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
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class AutomaticBeanTest {

    private static final BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean(), new PropertyUtilsBean());
    private static final AutomaticBean automaticBean = new AutomaticBean();
    private static final Configuration configuration = Mockito.mock(Configuration.class);
    private static final Context context = Mockito.mock(Context.class);

    @Test
    public void testConfigure() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        Configuration childConfig = Mockito.mock(Configuration.class);

        automaticBean.configure(config);

        Mockito.verify(config, Mockito.times(1)).setAttribute(ArgumentMatchers.anyString(), ArgumentMatchers.any());
        Mockito.verify(config, Mockito.times(1)).getChildren();

        automaticBean.configure(config);
        automaticBean.configure(childConfig);

        Mockito.verify(config, Mockito.times(1)).setAttribute(ArgumentMatchers.anyString(), ArgumentMatchers.any());
        Mockito.verify(childConfig, Mockito.times(0)).setAttribute(ArgumentMatchers.anyString(), ArgumentMatchers.any());
        Mockito.verify(config, Mockito.times(1)).getChildren();
        Mockito.verify(childConfig, Mockito.times(0)).getChildren();
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullConfig() throws Exception {
        automaticBean.configure(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullChildConfig() throws Exception {
        automaticBean.configure(configuration);
        automaticBean.configure(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithInvalidChildConfigType() throws Exception {
        Configuration invalidChildConfig = Mockito.mock(Integer.class);
        automaticBean.configure(configuration);
        automaticBean.configure(invalidChildConfig);
    }

    @Test
    public void testTryCopyPropertyWithNullValue() throws Exception {
        String key = "testKey";
        String moduleName = "testModule";

        automaticBean.tryCopyProperty(moduleName, key, null, true);
    }

    @Test
    public void testTryCopyPropertyWithNonExistentProperty() throws Exception {
        String key = "nonExistentProperty";
        String moduleName = "testModule";

        automaticBean.tryCopyProperty(moduleName, key, "value", true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        String key = "testKey";
        String moduleName = "testModule";
        Object invalidValue = new Object();

        automaticBean.tryCopyProperty(moduleName, key, invalidValue, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullBeanUtils() throws Exception {
        String key = "testKey";
        String moduleName = "testModule";
        Object value = new Boolean(true);

        AutomaticBean automaticBeanWithNullBeanUtils = new AutomaticBean() {
            private static final BeanUtilsBean beanUtilsBean = null;

            @Override
            private static BeanUtilsBean createBeanUtilsBean() {
                return null;
            }
        };

        automaticBeanWithNullBeanUtils.tryCopyProperty(moduleName, key, value, true);
    }

    @Test
    public void testTryCopyProperty() throws Exception {
        String key = "testKey";
        String moduleName = "testModule";
        Object value = new Boolean(true);

        automaticBean.tryCopyProperty(moduleName, key, value, true);

        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
        assertEquals(Boolean.TRUE, propertyDescriptor.getReadMethod().invoke(automaticBean));
    }

    @Test
    public void testContextualize() throws Exception {
        String key = "testKey";
        String value = "testValue";

        automaticBean.contextualize(context);
        automaticBean.contextualize(context);

        Mockito.verify(context, Mockito.times(1)).getAttributeNames();
        Mockito.verify(context, Mockito.times(1)).get(key);

        automaticBean.contextualize(context);
        Mockito.verify(context, Mockito.times(1)).getAttributeNames();
        Mockito.verify(context, Mockito.times(0)).get(ArgumentMatchers.anyString());
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeWithNullContext() throws Exception {
        automaticBean.contextualize(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeWithNullKey() throws Exception {
        automaticBean.contextualize(context);
        automaticBean.contextualize(context);
        automaticBean.contextualize(context);

        Mockito.verify(context, Mockito.times(2)).getAttributeNames();
        Mockito.verify(context, Mockito.times(1)).get(ArgumentMatchers.anyString());

        automaticBean.contextualize(context);
        Mockito.verify(context, Mockito.times(3)).getAttributeNames();
        Mockito.verify(context, Mockito.times(1)).get(ArgumentMatchers.anyString());

        automaticBean.contextualize(context);
        Mockito.verify(context, Mockito.times(3)).getAttributeNames();
        Mockito.verify(context, Mockito.times(1)).get(ArgumentMatchers.anyString());

        automaticBean.contextualize(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullKey() throws Exception {
        String nullKey = null;
        String moduleName = "testModule";
        Object value = new Boolean(true);

        automaticBean.tryCopyProperty(moduleName, nullKey, value, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullValue() throws Exception {
        String key = "testKey";
        String moduleName = "testModule";
        Object nullValue = null;

        automaticBean.tryCopyProperty(moduleName, key, nullValue, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullBean() throws Exception {
        String key = "testKey";
        String moduleName = "testModule";
        Object value = new Boolean(true);

        AutomaticBean automaticBeanWithNullBean = new AutomaticBean() {
            @Override
            protected Configuration getConfiguration() {
                return null;
            }
        };

        automaticBeanWithNullBean.tryCopyProperty(moduleName, key, value, true);
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        // empty implementation
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNullChildConfig() throws Exception {
        automaticBean.setupChild(null);
    }

    @Test
    public void testGetConfiguration() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        automaticBean.configure(config);

        assertEquals(config, automaticBean.getConfiguration());
    }

    @Test
    public void testSetupChild() throws Exception {
        Configuration childConfig = Mockito.mock(Configuration.class);

        automaticBean.setupChild(childConfig);

        Mockito.verify(childConfig, Mockito.times(1)).getName();
        Mockito.verify(childConfig, Mockito.times(1)).getClass();
        Mockito.verify(childConfig, Mockito.times(1)).isAllowedAsChild(automaticBean.getClass().getName());
        Mockito.verify(childConfig, Mockito.times(0)).getAttributeNames();
        Mockito.verify(childConfig, Mockito.times(0)).get(ArgumentMatchers.anyString());
    }

}