package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.exceptions.ConversionException;
import org.apache.commons.beanutils.exceptions.InvocationTargetException;
import org.apache.commons.beanutils.exceptions.NoSuchMethodException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

public class AutomaticBeanTest {

 private static final String MODULE_NAME = "AutomaticBean";
 private static final String CONFIG_KEY_BOOL = "bool";
 private static final boolean CONFIG_BOOL_VALUE = true;
 private static final String CONFIG_KEY_BOOLEAN_ARRAY = "booleanArray";
 private static final boolean[] CONFIG_BOOLEAN_ARRAY_VALUE = { true, false };
 private static final String CONFIG_KEY_BYTE = "byte";
 private static final byte CONFIG_BYTE_VALUE = 1;
 private static final String CONFIG_KEY_CHAR = "char";
 private static final char CONFIG_CHAR_VALUE = 'a';
 private static final String CONFIG_KEY_DOUBLE = "double";
 private static final double CONFIG_DOUBLE_VALUE = 1.2;
 private static final String CONFIG_KEY_FLOAT = "float";
 private static final float CONFIG_FLOAT_VALUE = 1.2f;
 private static final String CONFIG_KEY_INT = "int";
 private static final int CONFIG_INT_VALUE = 1;
 private static final String CONFIG_KEY_LONG = "long";
 private static final long CONFIG_LONG_VALUE = 1L;
 private static final String CONFIG_KEY_SHORT = "short";
 private static final short CONFIG_SHORT_VALUE = 1;
 private static final String CONFIG_KEY_STRING_ARRAY = "stringArray";
 private static final String[] CONFIG_STRING_ARRAY_VALUE = { "one", "two" };
 private static final String CONFIG_KEY_INVALID = "invalid";
 private static final Object CONFIG_INVALID_VALUE = new Object();
 private AutomaticBean automaticBean;
 private Configuration configuration;
 private Configuration childConfiguration;
 private ConvertUtilsBean cub;
 private PropertyUtilsBean pbu;
 private BeanUtilsBean beanUtils;

 @Before
 public void setup() throws Exception {
 configuration = Mockito.spy(new Configuration());
 childConfiguration = Mockito.spy(new Configuration());
 cub = new ConvertUtilsBean();
 pbu = new PropertyUtilsBean();
 beanUtils = new BeanUtilsBean(cub, pbu);
 automaticBean = new AutomaticBean();
 }

 @Test
 public void testConfigure() throws Exception {
 configureAutomaticBean(configuration);

 verify(configuration).getAttributeNames();
 verifyNoMoreInteractions(configuration);

 final String[] attributeNames = configuration.getAttributeNames();
 for (String key : attributeNames) {
 if (CONFIG_KEY_BOOL.equals(key)) {
 assertEquals(CONFIG_BOOL_VALUE, automaticBean.getConfiguration().getAttribute(key));
 } else if (CONFIG_KEY_BOOLEAN_ARRAY.equals(key)) {
 assertEquals(CONFIG_BOOLEAN_ARRAY_VALUE, automaticBean.getConfiguration().getAttributes(key));
 } else if (CONFIG_KEY_BYTE.equals(key)) {
 assertEquals(CONFIG_BYTE_VALUE, automaticBean.getConfiguration().getAttribute(key).byteValue());
 } else if (CONFIG_KEY_CHAR.equals(key)) {
 assertEquals(CONFIG_CHAR_VALUE, automaticBean.getConfiguration().getAttribute(key).charValue());
 } else if (CONFIG_KEY_DOUBLE.equals(key)) {
 assertEquals(CONFIG_DOUBLE_VALUE, automaticBean.getConfiguration().getAttribute(key).doubleValue(), 0.0);
 } else if (CONFIG_KEY_FLOAT.equals(key)) {
 assertEquals(CONFIG_FLOAT_VALUE, automaticBean.getConfiguration().getAttribute(key).floatValue(), 0.0f);
 } else if (CONFIG_KEY_INT.equals(key)) {
 assertEquals(CONFIG_INT_VALUE, automaticBean.getConfiguration().getAttribute(key).intValue());
 } else if (CONFIG_KEY_LONG.equals(key)) {
 assertEquals(CONFIG_LONG_VALUE, automaticBean.getConfiguration().getAttribute(key).longValue());
 } else if (CONFIG_KEY_SHORT.equals(key)) {
 assertEquals(CONFIG_SHORT_VALUE, automaticBean.getConfiguration().getAttribute(key).shortValue());
 } else if (CONFIG_KEY_STRING_ARRAY.equals(key)) {
 assertEquals(Arrays.asList(CONFIG_STRING_ARRAY_VALUE), Arrays.asList(automaticBean.getConfiguration().getAttributes(key)));
 } else {
 fail("Unexpected key: " + key);
 }
 }

 automaticBean.finishLocalSetup();
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidType() throws Exception {
 configureAutomaticBean(configuration);
 try {
 automaticBean.tryCopyProperty(MODULE_NAME, CONFIG_KEY_INVALID, CONFIG_INVALID_VALUE, true);
 fail("Should throw exception");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidValueType() throws Exception {
 configureAutomaticBean(configuration);
 try {
 automaticBean.tryCopyProperty(MODULE_NAME, CONFIG_KEY_BOOL, CONFIG_INVALID_VALUE, true);
 fail("Should throw exception");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidValue() throws Exception {
 configureAutomaticBean(configuration);
 try {
 automaticBean.tryCopyProperty(MODULE_NAME, CONFIG_KEY_BOOL, "invalid", true);
 fail("Should throw exception");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = ConversionException.class)
 public void testConfigureWithInvalidBooleanArrayValue() throws Exception {
 configureAutomaticBean(configuration);
 try {
 automaticBean.tryCopyProperty(MODULE_NAME, CONFIG_KEY_BOOLEAN_ARRAY, new Object[] { "invalid" }, true);
 fail("Should throw exception");
 } catch (ConversionException e) {
 // Expected
 }
 }

 @Test
 public void testContextualize() throws Exception {
 contextualizeAutomaticBean(configuration);

 verify(configuration).getAttributeNames();
 verifyNoMoreInteractions(configuration);

 final Collection<String> attributes = configuration.getAttributeNames();
 for (String key : attributes) {
 final Object value = configuration.get(key);
 automaticBean.tryCopyProperty(MODULE_NAME, key, value, false);
 }

 verify(automaticBean).finishLocalSetup();
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithInvalidType() throws Exception {
 contextualizeAutomaticBean(configuration);
 try {
 automaticBean.tryCopyProperty(MODULE_NAME, CONFIG_KEY_INVALID, CONFIG_INVALID_VALUE, false);
 fail("Should throw exception");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithInvalidValueType() throws Exception {
 contextualizeAutomaticBean(configuration);
 try {
 automaticBean.tryCopyProperty(MODULE_NAME, CONFIG_KEY_BOOL, new Object() {}, false);
 fail("Should throw exception");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithInvalidValue() throws Exception {
 contextualizeAutomaticBean(configuration);
 try {
 automaticBean.tryCopyProperty(MODULE_NAME, CONFIG_KEY_BOOL, "invalid", false);
 fail("Should throw exception");
 } catch (CheckstyleException e) {
 // Expected
 }
 }

 private void configureAutomaticBean(Configuration config) throws Exception {
 automaticBean.configure(config);
 }

 private void contextualizeAutomaticBean(Configuration config) throws Exception {
 automaticBean.contextualize(config);
 }
}

class TestHelper {

 @SuppressWarnings("unchecked")
 static <T> List<T> asList(T... values) {
 return Collections.unmodifiableList((List<T>)Lists.newArrayList(values));
 }
}