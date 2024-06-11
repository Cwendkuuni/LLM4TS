package com.example.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

public class AutomaticBeanTest {

 private AutomaticBean automaticBean;
 private BeanUtilsBean beanUtilsBean;
 private Configuration configuration;

 @Before
 public void setUp() {
 automaticBean = new AutomaticBean();
 beanUtilsBean = Mockito.mock(BeanUtilsBean.class);
 configuration = new Configuration();
 }

 @Test
 public void testConfigure() throws Exception {
 final String[] attributeNames = new String[]{"attribute1", "attribute2"};
 configuration.setAttributes(Arrays.asList(attributeNames));
 when(beanUtilsBean.copyProperty(automaticBean, "attribute1", "value1")).thenReturn(null);
 automaticBean.configure(configuration);
 verify(beanUtilsBean, times(2)).copyProperty(automaticBean, "attribute1", anyObject());
 verify(beanUtilsBean, times(1)).copyProperty(automaticBean, "attribute2", anyObject());
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithNullConfiguration() throws Exception {
 automaticBean.configure(null);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidAttributeName() throws Exception {
 configuration.setAttributes(Arrays.asList("invalidAttributeName"));
 automaticBean.configure(configuration);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithNullAttributeValue() throws Exception {
 configuration.setAttributes(Arrays.asList(new String[]{"attribute1", null}));
 automaticBean.configure(configuration);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidType() throws Exception {
 configuration.setAttributes(Arrays.asList(new String[]{"attribute1", "invalidType"}));
 automaticBean.configure(configuration);
 }

 @Test
 public void testTryCopyPropertyWithExistingProperty() throws Exception {
 automaticBean.tryCopyProperty("moduleName", "existingProperty", "value", true);
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithNonExistingProperty() throws Exception {
 automaticBean.tryCopyProperty("moduleName", "nonExistingProperty", "value", true);
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithInvalidType() throws Exception {
 automaticBean.tryCopyProperty("moduleName", "invalidProperty", new Object(){}, true);
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithNullValue() throws Exception {
 automaticBean.tryCopyProperty("moduleName", "property", null, true);
 }

 @Test(expected = CheckstyleException.class)
 public void testTryCopyPropertyWithInvalidValueType() throws Exception {
 automaticBean.tryCopyProperty("moduleName", "property", new Object(){}, true);
 }

 @Test
 public void testContextualize() throws Exception {
 final Map<String, Object> context = new HashMap<>();
 context.put("attribute1", "value1");
 context.put("attribute2", "value2");
 automaticBean.contextualize(context);
 assertEquals("value1", automaticBean.getConfiguration().getAttribute("attribute1"));
 assertEquals("value2", automaticBean.getConfiguration().getAttribute("attribute2"));
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithNullContext() throws Exception {
 automaticBean.contextualize(null);
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithInvalidAttributeName() throws Exception {
 final Map<String, Object> context = new HashMap<>();
 context.put("invalidAttributeName", "value");
 automaticBean.contextualize(context);
 }

 @Test
 public void testGetConfiguration() throws Exception {
 assertNotNull(automaticBean.getConfiguration());
 }

 @Test
 public void testFinishLocalSetup() throws Exception {
 automaticBean.finishLocalSetup();
 }

 @Test(expected = CheckstyleException.class)
 public void testSetupChildWithNullChildConfiguration() throws Exception {
 automaticBean.setupChild(null);
 }

 @Test(expected = CheckstyleException.class)
 public void testSetupChildWithValidChildConfiguration() throws Exception {
 automaticBean.setupChild(new Configuration());
 }

 @Test
 public void testCreateBeanUtilsBean() {
 final BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
 assertNotNull(beanUtilsBean);
 }

 @Test
 public void testTryCopyPropertyUsingPropertyDescriptor() throws Exception {
 final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "configuration");
 automaticBean.tryCopyProperty("moduleName", "configuration", propertyDescriptor.getReadMethod().invoke(automaticBean), true);
 assertEquals(configuration, automaticBean.getConfiguration());
 }

 @Test
 public void testTryCopyPropertyUsingReflection() throws Exception {
 final Field configurationField = AutomaticBean.class.getDeclaredField("configuration");
 configurationField.setAccessible(true);
 automaticBean.tryCopyProperty("moduleName", "configuration", configurationField.get(automaticBean), true);
 assertEquals(configuration, automaticBean.getConfiguration());
 }

 @Test
 public void testCopyPropertiesUsingBeanUtils() throws Exception {
 final Configuration configurationToCopy = new Configuration();
 configurationToCopy.setAttributeNames(Arrays.asList(new String[]{"attribute1", "attribute2"}));
 configurationToCopy.setAttribute("attribute1", "value1");
 configurationToCopy.setAttribute("attribute2", Arrays.asList(new String[]{"value2.1", "value2.2"}));
 automaticBean.configure(configurationToCopy);
 assertEquals("value1", automaticBean.getConfiguration().getAttribute("attribute1"));
 List<String> attribute2 = (List<String>)automaticBean.getConfiguration().getAttribute("attribute2");
 assertEquals(2, attribute2.size());
 assertEquals("value2.1", attribute2.get(0));
 assertEquals("value2.2", attribute2.get(1));
 }

 @Test
 public void testCopyPropertiesUsingPropertyUtils() throws Exception {
 final Configuration configurationToCopy = new Configuration();
 configurationToCopy.setAttributeNames(Arrays.asList(new String[]{"attribute1", "attribute2"}));
 configurationToCopy.setAttribute("attribute1", "value1");
 configurationToCopy.setAttribute("attribute2", Arrays.asList(new String[]{"value2.1", "value2.2"}));
 MethodUtils.setProperty(automaticBean, "configuration", configurationToCopy);
 assertEquals("value1", automaticBean.getConfiguration().getAttribute("attribute1"));
 List<String> attribute2 = (List<String>)MethodUtils.invokeMethod(automaticBean, "getConfiguration", new Class[0]).getAttribute("attribute2");
 assertEquals(2, attribute2.size());
 assertEquals("value2.1", attribute2.get(0));
 assertEquals("value2.2", attribute2.get(1));
 }

 @Test
 public void testCopyPropertiesUsingBeanUtilsWithConverters() throws Exception {
 final Configuration configurationToCopy = new Configuration();
 configurationToCopy.setAttributeNames(Arrays.asList(new String[]{"attribute1", "attribute2", "attribute3"}));
 configurationToCopy.setAttribute("attribute1", "true");
 configurationToCopy.setAttribute("attribute2", new byte[]{1, 2});
 configurationToCopy.setAttribute("attribute3", Arrays.asList(new String[]{"value3.1", "value3.2"}));
 automaticBean.configure(configurationToCopy);
 assertTrue(automaticBean.getConfiguration().getAttribute("attribute1"));
 assertArrayEquals(new byte[]{1, 2}, (byte[])automaticBean.getConfiguration().getAttribute("attribute2"));
 List<String> attribute3 = (List<String>)automaticBean.getConfiguration().getAttribute("attribute3");
 assertEquals(2, attribute3.size());
 assertEquals("value3.1", attribute3.get(0));
 assertEquals("value3.2", attribute3.get(1));
 }

 @Test
 public void testCopyPropertiesUsingPropertyUtilsWithConverters() throws Exception {
 final Configuration configurationToCopy = new Configuration();
 configurationToCopy.setAttributeNames(Arrays.asList(new String[]{"attribute1", "attribute2", "attribute3"}));
 configurationToCopy.setAttribute("attribute1", "true");
 configurationToCopy.setAttribute("attribute2", new byte[]{1, 2});
 configurationToCopy.setAttribute("attribute3", Arrays.asList(new String[]{"value3.1", "value3.2"}));
 MethodUtils.setProperty(automaticBean, "configuration", configurationToCopy);
 assertTrue(automaticBean.getConfiguration().getAttribute("attribute1"));
 assertArrayEquals(new byte[]{1, 2}, (byte[])MethodUtils.invokeMethod(automaticBean, "getConfiguration", new Class[0]).getAttribute("attribute2"));
 List<String> attribute3 = (List<String>)MethodUtils.invokeMethod(automaticBean, "getConfiguration", new Class[0]).getAttribute("attribute3");
 assertEquals(2, attribute3.size());
 assertEquals("value3.1", attribute3.get(0));
 assertEquals("value3.2", attribute3.get(1));
 }

 @Test
 public void testConvertUsingBeanUtilsConverter() throws Exception {
 final Configuration configurationToCopy = new Configuration();
 configurationToCopy.setAttribute("attribute", Arrays.asList(new Object[]{"1", "2"}));
 final Converter converter = new BooleanConverter();
 automaticBean.configure(configurationToCopy);
 List<Boolean> booleanAttribute = (List<Boolean>)automaticBean.getConfiguration().getAttribute("attribute");
 assertEquals(2, booleanAttribute.size());
 assertTrue(booleanAttribute.get(0));
 assertTrue(booleanAttribute.get(1));
 }

 @Test
 public void testConvertUsingBeanUtilsCustomConverter() throws Exception {
 final Configuration configurationToCopy = new Configuration();
 configurationToCopy.setAttribute("attribute", Arrays.asList(new Object[]{"1", "2"}));
 final Converter converter = new CustomConverter();
 automaticBean.configure(configurationToCopy);
 List<Integer> integerAttribute = (List<Integer>)automaticBean.getConfiguration().getAttribute("attribute");
 assertEquals(2, integerAttribute.size());
 assertEquals(1, integerAttribute.get(0).intValue());
 assertEquals(2, integerAttribute.get(1).intValue());
 }

 @Test
 public void testConvertUsingPropertyUtilsConverter() throws Exception {
 final Configuration configurationToCopy = new Configuration();
 configurationToCopy.setAttribute("attribute", Arrays.asList(new Object[]{"1", "2"}));
 automaticBean.configure(configurationToCopy);
 List<Integer> integerAttribute = (List<Integer>)automaticBean.getConfiguration().getAttribute("attribute");
 assertEquals(2, integerAttribute.size());
 assertEquals(1, integerAttribute.get(0).intValue());
 assertEquals(2, integerAttribute.get(1).intValue());
 }

 @Test
 public void testConvertUsingPropertyUtilsCustomConverter() throws Exception {
 final Configuration configurationToCopy = new Configuration();
 configurationToCopy.setAttribute("attribute", Arrays.asList(new Object[]{"1", "2"}));
 MethodUtils.setProperty(automaticBean, "configuration", configurationToCopy);
 List<Integer> integerAttribute = (List<Integer>)MethodUtils.invokeMethod(automaticBean, "getConfiguration", new Class[0]).getAttribute("attribute");
 assertEquals(2, integerAttribute.size());
 assertEquals(1, integerAttribute.get(0).intValue());
 assertEquals(2, integerAttribute.get(1).intValue());
 }

 @Test
 public void testGetConfigurationInChildConfiguration() throws Exception {
 final Configuration childConfiguration = new Configuration();
 automaticBean.configure(new Configuration());
 automaticBean.configure(childConfiguration);
 assertEquals(automaticBean.getConfiguration(), childConfiguration.getConfiguration());
 }

 @Test
 public void testGetConfigurationInRootConfiguration() throws Exception {
 Configuration rootConfiguration = new Configuration();
 automaticBean.configure(rootConfiguration);
 assertEquals(rootConfiguration, automaticBean.getConfiguration());
 }

}

class CustomConverter implements Converter {
 @Override
 public Object convert(final Class type, final Object value) {
 return ArrayUtils.toObject(ListUtils.toArray(Lists.newArrayList(value), Integer.TYPE), Integer.class);
 }
}