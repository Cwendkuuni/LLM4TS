package com.example.automaticbean;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyDescriptor;
import java.beans.PropertyUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configuration;
    private Context context;
    private BeanUtilsBean beanUtils;
    private PropertyUtilsBean propertyUtils;
    private Converter relaxedStringArrayConverter;

    @Before
    public void init() {
        configuration = Mockito.mock(Configuration.class);
        context = Mockito.mock(Context.class);
        beanUtils = new BeanUtilsBean();
        propertyUtils = new PropertyUtilsBean();
        relaxedStringArrayConverter = new RelaxedStringArrayConverter();
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure() throws Exception {
        // Given: An instance of AutomaticBean and configuration object

        // When: Configure method is called with a valid configuration
        automaticBean.configure(configuration);

        // Then: The configuration property is set in the AutomaticBean instance
        assertSame(configuration, automaticBean.getConfiguration());

        String[] attributeNames = configuration.getAttributeNames();
        for (String key : attributeNames) {
            Object value = configuration.getAttribute(key);
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), key, value, true);
            PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
            assertSame(value, descriptor.getReadMethod().invoke(automaticBean));
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullConfiguration() throws Exception {
        // Given: An instance of AutomaticBean and null configuration object

        // When: Configure method is called
        automaticBean.configure(null);

        // Then: A CheckstyleException is thrown
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidPropertyName() throws Exception {
        // Given: An instance of AutomaticBean and a configuration object with an invalid property name

        String invalidPropertyName = "invalidProperty";
        Configuration invalidConfig = Mockito.mock(Configuration.class);
        Mockito.when(invalidConfig.getAttributeNames()).thenReturn(new String[]{invalidPropertyName});
        Mockito.when(invalidConfig.getAttribute(invalidPropertyName)).thenReturn("invalidValue");

        // When: tryCopyProperty method is called with an invalid property name
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), invalidPropertyName, "invalidValue", true);

        // Then: A CheckstyleException is thrown
    }

    @Test
    public void testTryCopyPropertyWithExistingProperty() throws Exception {
        // Given: An instance of AutomaticBean, a configuration object with an existing property, and a value

        String existingPropertyName = "existingProperty";
        Object existingValue = Arrays.asList(1, 2, 3);
        Configuration existingConfig = Mockito.mock(Configuration.class);
        Mockito.when(existingConfig.getAttributeNames()).thenReturn(new String[]{existingPropertyName});
        Mockito.when(existingConfig.getAttribute(existingPropertyName)).thenReturn(existingValue);

        // When: tryCopyProperty method is called with an existing property
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), existingPropertyName, existingValue, true);

        // Then: The property is set in the AutomaticBean instance
        List<Object> actualPropertyValue = (List<Object>)AutomaticBean.class.getField("property").get(automaticBean);
        assertSame(existingValue, actualPropertyValue);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidValueType() throws Exception {
        // Given: An instance of AutomaticBean, a configuration object with an invalid value type, and a value

        String invalidPropertyName = "invalidProperty";
        Object invalidValue = new Object();
        Configuration invalidConfig = Mockito.mock(Configuration.class);
        Mockito.when(invalidConfig.getAttributeNames()).thenReturn(new String[]{invalidPropertyName});
        Mockito.when(invalidConfig.getAttribute(invalidPropertyName)).thenReturn(invalidValue);

        // When: tryCopyProperty method is called with an invalid value type
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), invalidPropertyName, invalidValue, true);

        // Then: A CheckstyleException is thrown
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithIllegalAccess() throws Exception {
        // Given: An instance of AutomaticBean, a configuration object with an illegal access property, and a value

        String illegalAccessPropertyName = "illegalAccessProperty";
        Object illegalAccessValue = new Object();
        Configuration illegalAccessConfig = Mockito.mock(Configuration.class);
        Mockito.when(illegalAccessConfig.getAttributeNames()).thenReturn(new String[]{illegalAccessPropertyName});
        Mockito.when(illegalAccessConfig.getAttribute(illegalAccessPropertyName)).thenReturn(illegalAccessValue);

        // When: Reflection is used to access a private property in AutomaticBean
        Method privatePropertyMethod = AutomaticBean.class.getDeclaredMethod("set" + StringUtils.capitalize(illegalAccessPropertyName));
        privatePropertyMethod.setAccessible(true);

        // When: tryCopyProperty method is called with an illegal access property
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), illegalAccessPropertyName, illegalAccessValue, true);

        // Then: A CheckstyleException is thrown
    }

    @Test
    public void testTryCopyPropertyWithBooleanProperty() throws Exception {
        // Given: An instance of AutomaticBean and a configuration object with a boolean property

        String booleanPropertyName = "booleanProperty";
        boolean booleanValue = true;
        Configuration booleanConfig = Mockito.mock(Configuration.class);
        Mockito.when(booleanConfig.getAttributeNames()).thenReturn(new String[]{booleanPropertyName});
        Mockito.when(booleanConfig.getAttribute(booleanPropertyName)).thenReturn(booleanValue);

        // When: tryCopyProperty method is called with a boolean property
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), booleanPropertyName, booleanValue, true);

        // Then: The boolean property is set in the AutomaticBean instance
        boolean actualBooleanValue = ((Boolean)AutomaticBean.class.getField("booleanProperty").get(automaticBean)).booleanValue();
        assertTrue(actualBooleanValue);
    }

    @Test
    public void testTryCopyPropertyWithArrayProperty() throws Exception {
        // Given: An instance of AutomaticBean and a configuration object with an array property

        String arrayPropertyName = "arrayProperty";
        Object arrayValue = new int[]{1, 2, 3};
        Configuration arrayConfig = Mockito.mock(Configuration.class);
        Mockito.when(arrayConfig.getAttributeNames()).thenReturn(new String[]{arrayPropertyName});
        Mockito.when(arrayConfig.getAttribute(arrayPropertyName)).thenReturn(arrayValue);

        // When: tryCopyProperty method is called with an array property
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), arrayPropertyName, arrayValue, true);

        // Then: The array property is set in the AutomaticBean instance
        Object actualArrayValue = AutomaticBean.class.getField("arrayProperty").get(automaticBean);
        assertArrayEquals((Object[])arrayValue, actualArrayValue);
    }

    @Test
    public void testContextualize() throws Exception {
        // Given: An instance of AutomaticBean and a context object with attributes

        String attributeName = "attribute";
        Object attributeValue = new Object();
        context.put(attributeName, attributeValue);

        // When: contextualize method is called
        automaticBean.contextualize(context);

        // Then: The attribute is copied to the AutomaticBean instance
        Object actualAttributeValue = AutomaticBean.class.getField("attribute").get(automaticBean);
        assertSame(attributeValue, actualAttributeValue);
    }

    @Test
    public void testGetConfiguration() throws Exception {
        // Given: An instance of AutomaticBean

        // When: getConfiguration method is called
        Configuration actualConfiguration = automaticBean.getConfiguration();

        // Then: The configuration object is returned
        assertSame(configuration, actualConfiguration);
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        // Given: An instance of AutomaticBean

        // When: finishLocalSetup method is called
        automaticBean.finishLocalSetup();

        // Then: No exception is thrown
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNullChild() throws Exception {
        // Given: An instance of AutomaticBean and a null child configuration object

        // When: setupChild method is called
        automaticBean.setupChild(null);

        // Then: A CheckstyleException is thrown
    }

    @Test
    public void testTryCopyPropertyWithNullValue() throws Exception {
        // Given: An instance of AutomaticBean and a configuration object with a null value property

        String nullPropertyName = "nullProperty";
        Configuration nullConfig = Mockito.mock(Configuration.class);
        Mockito.when(nullConfig.getAttributeNames()).thenReturn(new String[]{nullPropertyName});
        Mockito.when(nullConfig.getAttribute(nullPropertyName)).thenReturn(null);

        // When: tryCopyProperty method is called with a null value property
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), nullPropertyName, null, true);

        // Then: No exception is thrown
    }

    @Test
    public void testCreateBeanUtilsBean() throws Exception {
        // Given: An instance of AutomaticBean

        // When: createBeanUtilsBean method is called
        BeanUtilsBean actualBeanUtilsBean = automaticBean.createBeanUtilsBean();

        // Then: A BeanUtilsBean instance is returned
        assertSame(beanUtils, actualBeanUtilsBean);
    }
}