package com.example.automaticbean;

import static org.junit.Assert.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AutomaticBean.class, BeanUtils.class, PropertyUtils.class})
public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();

        PowerMockito.mockStatic(BeanUtils.class);
        PowerMockito.mockStatic(PropertyUtils.class);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class))).thenReturn(Mockito.mock(BeanUtilsBean.class));
        Mockito.when(PropertyUtils.getPropertyDescriptor(automaticBean, "configuration")).thenReturn(propertyDescriptor("configuration", Configuration.class));

        automaticBean.configure(config);

        Mockito.verify(automaticBean).setConfiguration(config);
        Mockito.verify(BeanUtils.class).copyProperty(automaticBean, "configuration", config);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidProperty() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();

        PowerMockito.mockStatic(BeanUtils.class);
        PowerMockito.mockStatic(PropertyUtils.class);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class))).thenReturn(Mockito.mock(BeanUtilsBean.class));
        Mockito.when(PropertyUtils.getPropertyDescriptor(automaticBean, "nonExistentProperty")).thenReturn(null);

        automaticBean.configure(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidValueBoolean() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();

        PowerMockito.mockStatic(BeanUtils.class);
        PowerMockito.mockStatic(PropertyUtils.class);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class))).thenReturn(Mockito.mock(BeanUtilsBean.class));
        Mockito.when(PropertyUtils.getPropertyDescriptor(automaticBean, "booleanProperty")).thenReturn(propertyDescriptor("booleanProperty", Boolean.class));

        automaticBean.configure(config);

        Mockito.verify(BeanUtils.class).copyProperty(automaticBean, "booleanProperty", (Object)false);
        Mockito.verify(automaticBean).setBooleanProperty((boolean)false);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class)))
                .thenThrow(new NoSuchMethodException("copyProperty"));

        automaticBean.configure(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidValueString() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();

        PowerMockito.mockStatic(BeanUtils.class);
        PowerMockito.mockStatic(PropertyUtils.class);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class))).thenReturn(Mockito.mock(BeanUtilsBean.class));
        Mockito.when(PropertyUtils.getPropertyDescriptor(automaticBean, "stringProperty")).thenReturn(propertyDescriptor("stringProperty", String.class));

        automaticBean.configure(config);

        Mockito.verify(BeanUtils.class).copyProperty(automaticBean, "stringProperty", "invalid value");
        Mockito.verify(automaticBean).setStringProperty("invalid value");

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class)))
                .thenThrow(new InvocationTargetException(new RuntimeException("copyProperty")));

        automaticBean.configure(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidValueList() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();

        PowerMockito.mockStatic(BeanUtils.class);
        PowerMockito.mockStatic(PropertyUtils.class);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class))).thenReturn(Mockito.mock(BeanUtilsBean.class));
        Mockito.when(PropertyUtils.getPropertyDescriptor(automaticBean, "listProperty")).thenReturn(propertyDescriptor("listProperty", List.class));

        automaticBean.configure(config);

        Mockito.verify(BeanUtils.class).copyProperty(automaticBean, "listProperty", ListUtils.emptyList());
        Mockito.verify(automaticBean).setListProperty(ListUtils.emptyList());

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class)))
                .thenThrow(new InvocationTargetException(new RuntimeException("copyProperty")));

        automaticBean.configure(config);
    }

    @Test
    public void testContextualize() throws Exception {
        Context context = Mockito.mock(Context.class);
        AutomaticBean automaticBean = new AutomaticBean();

        PowerMockito.mockStatic(BeanUtils.class);
        PowerMockito.mockStatic(PropertyUtils.class);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class))).thenReturn(Mockito.mock(BeanUtilsBean.class));
        Mockito.when(context.getAttributeNames()).thenReturn(Arrays.asList("booleanProperty", "stringProperty", "listProperty"));

        automaticBean.contextualize(context);

        Mockito.verify(BeanUtils.class).copyProperty(automaticBean, "booleanProperty", Mockito.eq(true));
        Mockito.verify(BeanUtils.class).copyProperty(automaticBean, "stringProperty", Mockito.eq("string value"));
        Mockito.verify(BeanUtils.class).copyProperty(automaticBean, "listProperty", Mockito.eq(Arrays.asList("list item 1", "list item 2")));
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeInvalidValueBoolean() throws Exception {
        Context context = Mockito.mock(Context.class);
        AutomaticBean automaticBean = new AutomaticBean();

        PowerMockito.mockStatic(BeanUtils.class);
        PowerMockito.mockStatic(PropertyUtils.class);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class))).thenReturn(Mockito.mock(BeanUtilsBean.class));
        Mockito.when(context.getAttribute("booleanProperty")).thenReturn((Object)false);

        automaticBean.contextualize(context);

        Mockito.verify(BeanUtils.class).copyProperty(automaticBean, "booleanProperty", (Object)false);
        Mockito.verify(automaticBean).setBooleanProperty((boolean)false);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class)))
                .thenThrow(new ConversionException("boolean"));

        automaticBean.contextualize(context);
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeInvalidValueString() throws Exception {
        Context context = Mockito.mock(Context.class);
        AutomaticBean automaticBean = new AutomaticBean();

        PowerMockito.mockStatic(BeanUtils.class);
        PowerMockito.mockStatic(PropertyUtils.class);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class))).thenReturn(Mockito.mock(BeanUtilsBean.class));
        Mockito.when(context.getAttribute("stringProperty")).thenReturn("invalid value");

        automaticBean.contextualize(context);

        Mockito.verify(BeanUtils.class).copyProperty(automaticBean, "stringProperty", "invalid value");
        Mockito.verify(automaticBean).setStringProperty("invalid value");

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class)))
                .thenThrow(new InvocationTargetException(new RuntimeException("copyProperty")));

        automaticBean.contextualize(context);
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeInvalidValueList() throws Exception {
        Context context = Mockito.mock(Context.class);
        AutomaticBean automaticBean = new AutomaticBean();

        PowerMockito.mockStatic(BeanUtils.class);
        PowerMockito.mockStatic(PropertyUtils.class);

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class))).thenReturn(Mockito.mock(BeanUtilsBean.class));
        Mockito.when(context.getAttribute("listProperty"))
                .thenReturn(Arrays.asList("list item 1", "list item 2", "list item 3"));

        automaticBean.contextualize(context);

        Mockito.verify(BeanUtils.class).copyProperty(automaticBean, "listProperty", Arrays.asList("list item 1", "list item 2", "list item 3"));
        Mockito.verify(automaticBean).setListProperty(Arrays.asList("list item 1", "list item 2", "list item 3"));

        Mockito.when(BeanUtils.newBeanUtilsBean(Mockito.any(ConvertUtilsBean.class), Mockito.any(PropertyUtilsBean.class)))
                .thenThrow(new InvocationTargetException(new RuntimeException("copyProperty")));

        automaticBean.contextualize(context);
    }

    private PropertyDescriptor propertyDescriptor(String name, Class clazz) {
        return Mockito.mock(PropertyDescriptor.class);
    }
}