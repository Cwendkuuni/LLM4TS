package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

    @InjectMocks
    private AutomaticBean automaticBean;
    private static Configuration configuration;

    @Test
    public void testConfigure() throws Exception {
        configuration = Mockito.mock(Configuration.class);

        automaticBean.configure(configuration);

        assertSame(configuration, automaticBean.getConfiguration());
    }

    @Test
    public void testCopyPropertyBoolean() throws Exception {
        configuration.setBoolean("booleanProperty", true);

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "booleanProperty", true, false);

        assertTrue(automaticBean.getBooleanProperty());
    }

    @Test
    public void testCopyPropertyNull() throws Exception {
        configuration.setProperty("stringProperty", null);

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "stringProperty", null, false);

        assertNull(automaticBean.getStringProperty());
    }

    @Test
    public void testCopyPropertyInvalidType() throws Exception {
        configuration.setProperty("floatProperty", "123");

        assertThrows(IllegalArgumentException.class, () -> {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "floatProperty", "123", false);
        });
    }

    @Test
    public void testCopyPropertyInvalidValue() throws Exception {
        configuration.setProperty("integerProperty", "abc");

        assertThrows(ConversionException.class, () -> {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "integerProperty", "abc", false);
        });
    }

    @Test
    public void testCopyPropertyExistingProperty() throws Exception {
        configuration.setProperty("existingProperty", "existingValue");
        automaticBean.setExistingProperty("overwrittenValue");

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "existingProperty", "newValue", false);

        assertEquals("newValue", automaticBean.getExistingProperty());
    }

    @Test
    public void testCopyPropertyWithRecheck() throws Exception {
        configuration.setProperty("existingProperty", "existingValue");
        automaticBean.setExistingProperty("overwrittenValue");

        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "existingProperty");

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "existingProperty", "newValue", true);

        assertSame(propertyDescriptor, PropertyUtils.getPropertyDescriptor(automaticBean, "existingProperty"));
        assertEquals("newValue", automaticBean.getExistingProperty());
    }

    @Test
    public void testTryCopyPropertyNullConfig() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            automaticBean.configure(null);
        });
    }

    @Test
    public void testTryCopyPropertyNullAttribute() throws Exception {
        automaticBean.configure(configuration);

        assertThrows(NullPointerException.class, () -> {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), null, new Object(), false);
        });
    }

    @Test
    public void testTryCopyPropertyEmptyConfig() throws Exception {
        configuration.clear();

        automaticBean.configure(configuration);

        assertFalse(automaticBean.hasConfiguration());
    }

    @Test
    public void testTryCopyPropertyEmptyAttribute() throws Exception {
        automaticBean.configure(configuration);

        assertThrows(IllegalArgumentException.class, () -> {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "", new Object(), false);
        });
    }

    @Test
    public void testTryCopyPropertyUnknownAttribute() throws Exception {
        configuration.setProperty("unknownProperty", "unknownValue");

        automaticBean.configure(configuration);

        assertThrows(IllegalArgumentException.class, () -> {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "unknownProperty", new Object(), false);
        });
    }

    @Test
    public void testContextualize() {
        configuration.putAttribute("stringAttribute", "stringValue");
        configuration.putAttribute("integerAttribute", 123);
        configuration.putAttribute("booleanAttribute", true);
        configuration.putAttribute("listAttribute", Arrays.asList("listValue1", "listValue2"));

        automaticBean.contextualize(configuration);

        assertEquals("stringValue", automaticBean.getStringAttribute());
        assertEquals(123, automaticBean.getIntegerAttribute());
        assertTrue(automaticBean.getBooleanAttribute());
        assertArrayEquals(new String[]{"listValue1", "listValue2"}, automaticBean.getListAttribute());
    }

    @Test
    public void testContextualizeNullContext() {
        assertThrows(NullPointerException.class, () -> {
            automaticBean.contextualize(null);
        });
    }

    @Test
    public void testContextualizeEmptyContext() {
        automaticBean.contextualize(Collections.emptyMap());

        assertNull(automaticBean.getStringAttribute());
        assertNull(automaticBean.getIntegerAttribute());
        assertNull(automaticBean.getBooleanAttribute());
        assertNull(automaticBean.getListAttribute());
    }

    @Test
    public void testSetupChild() throws Exception {
        Configuration childConfig = Mockito.mock(Configuration.class);

        automaticBean.setupChild(childConfig);

        assertThrows(IllegalArgumentException.class, () -> {
            automaticBean.setupChild(childConfig);
        });
    }

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}