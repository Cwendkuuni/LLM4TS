import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.collections4.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

 private AutomaticBean automaticBean;
 private Configuration configuration;
 private ConvertUtilsBean convertUtilsBean;
 private BeanUtilsBean beanUtilsBean;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 configuration = mock(Configuration.class);
 convertUtilsBean = new ConvertUtilsBean();
 beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
 automaticBean = new AutomaticBean() {
 @Override
 public void configure(Configuration config) throws CheckstyleException {
 super.configure(config);
 }
 @Override
 public void contextualize(Context context) throws CheckstyleException {
 super.contextualize(context);
 }
 @Override
 protected void finishLocalSetup() throws CheckstyleException {
 }
 @Override
 protected void setupChild(Configuration childConf) throws CheckstyleException {
 super.setupChild(childConf);
 }
 };
 }

 @Test
 public void testConfigure() throws CheckstyleException {
 // Given
 final String[] attributeNames = new String[]{"attribute1", "attribute2"};
 when(configuration.getAttributeNames()).thenReturn(attributeNames);
 final String attributeValue1 = "value1";
 final String attributeValue2 = "value2";
 when(configuration.getAttribute("attribute1")).thenReturn(attributeValue1);
 when(configuration.getAttribute("attribute2")).thenReturn(attributeValue2);

 // When
 automaticBean.configure(configuration);

 // Then
 assertEquals(attributeValue1, automaticBean.getConfiguration().getAttribute("attribute1"));
 assertEquals(attributeValue2, automaticBean.getConfiguration().getAttribute("attribute2"));

 verify(automaticBean, times(2)).tryCopyProperty(anyString(), anyString(), anyObject(), anyBoolean());
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithNullConfiguration() {
 automaticBean.configure(null);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidAttributeName() {
 final Configuration invalidConfig = mock(Configuration.class);
 when(invalidConfig.getAttributeNames()).thenReturn(new String[0]);
 when(invalidConfig.getAttribute("nonExistentAttribute")).thenReturn("value");
 automaticBean.configure(invalidConfig);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidAttributeValue() {
 final Configuration invalidConfig = mock(Configuration.class);
 when(invalidConfig.getAttributeNames()).thenReturn(new String[0]);
 when(invalidConfig.getAttribute("attribute1")).thenReturn("invalidValue");
 automaticBean.configure(invalidConfig);
 }

 @Test
 public void testContextualize() throws CheckstyleException {
 // Given
 final Map<String, Object> contextAttributes = new HashMap<>();
 contextAttributes.put("attribute1", "value1");
 contextAttributes.put("attribute2", "value2");

 // When
 automaticBean.contextualize(contextAttributes);

 // Then
 assertEquals("value1", automaticBean.getConfiguration().getAttribute("attribute1"));
 assertEquals("value2", automaticBean.getConfiguration().getAttribute("attribute2"));
 }

 @Test
 public void testContextualizeWithEmptyContext() throws CheckstyleException {
 automaticBean.contextualize(Collections.emptyMap());
 assertNull(automaticBean.getConfiguration());
 }

 @Test
 public void testTryCopyProperty() throws CheckstyleException {
 // Given
 final String moduleName = "moduleName";
 final String key = "key";
 final Object value = "value";
 final PropertyDescriptor descriptor = mock(PropertyDescriptor.class);
 final Method setter = mock(Method.class);
 final BeanUtilsBean beanUtils = mock(BeanUtilsBean.class);

 when(beanUtils.copyProperty(any(Object.class), eq(key), any(Object.class))).thenThrow(new IllegalAccessException());
 when(beanUtils.copyProperty(any(Object.class), eq(key), anyObject())).thenReturn(null);
 when(beanUtils.getBeanUtils()).thenReturn(beanUtils);
 when(beanUtils.getPropertyDescriptor(any(Class.class), eq(key))).thenReturn(descriptor);
 when(descriptor.getWriteMethod()).thenReturn(setter);
 when(setter.invoke(any(), any())).thenThrow(new IllegalArgumentException());

 // When
 automaticBean.tryCopyProperty(moduleName, key, value, true);

 // Then
 assertNull(automaticBean.getConfiguration());
 verify(beanUtils, times(1)).copyProperty(any(Object.class), eq(key), anyObject());
 verify(beanUtils, times(1)).getPropertyDescriptor(any(Class.class), eq(key));
 verify(beanUtils, times(1)).getBeanUtils();
 }

 @Test
 public void testTryCopyPropertyWithExistingProperty() throws CheckstyleException {
 // Given
 final String moduleName = "moduleName";
 final String key = "key";
 final Object value = "value";
 final PropertyDescriptor descriptor = mock(PropertyDescriptor.class);
 when(beanUtilsBean.getPropertyDescriptor(AutomaticBean.class, key)).thenReturn(descriptor);
 when(descriptor.getWriteMethod()).thenReturn(doNothing());

 // When
 automaticBean.tryCopyProperty(moduleName, key, value, true);

 // Then
 assertEquals(value, automaticBean.getConfiguration().getAttribute(key));
 }

 @Test
 public void testTryCopyPropertyWithNullValue() throws CheckstyleException {
 // Given
 final String moduleName = "moduleName";
 final String key = "key";
 final Object value = null;

 // When
 automaticBean.tryCopyProperty(moduleName, key, value, true);

 // Then
 assertNull(automaticBean.getConfiguration());
 }

 @Test
 public void testTryCopyPropertyWithEmptyValue() throws CheckstyleException {
 // Given
 final String moduleName = "moduleName";
 final String key = "key";
 final Object value = "";

 // When
 automaticBean.tryCopyProperty(moduleName, key, value, true);

 // Then
 assertNull(automaticBean.getConfiguration());
 }

 @Test
 public void testTryCopyPropertyWithInvalidValue() throws CheckstyleException {
 // Given
 final String moduleName = "moduleName";
 final String key = "key";
 final Object value = 123;

 // When
 automaticBean.tryCopyProperty(moduleName, key, value, true);

 // Then
 assertNull(automaticBean.getConfiguration());
 }

 @Test
 public void testCopyPropertyWithRecheck() throws CheckstyleException {
 // Given
 final String moduleName = "moduleName";
 final String key = "key";
 final Object value = "value";
 final PropertyDescriptor descriptor = mock(PropertyDescriptor.class);
 when(beanUtilsBean.getPropertyDescriptor(AutomaticBean.class, key)).thenReturn(descriptor);
 when(descriptor.getWriteMethod()).thenReturn(doNothing());

 // When
 automaticBean.tryCopyProperty(moduleName, key, value, true);
 automaticBean.tryCopyProperty(moduleName, key, value, true);

 // Then
 assertEquals(value, automaticBean.getConfiguration().getAttribute(key));
 }

 @Test
 public void testTryCopyPropertyWithNonExistentProperty() throws CheckstyleException {
 // Given
 final String moduleName = "moduleName";
 final String key = "nonExistentProperty";
 final Object value = "value";

 // When
 automaticBean.tryCopyProperty(moduleName, key, value, true);

 // Then
 assertNull(automaticBean.getConfiguration());
 }

 @Test
 public void testSetupChild() throws CheckstyleException {
 // Given
 final Configuration childConfig = mock(Configuration.class);

 // When
 automaticBean.setupChild(childConfig);

 // Then
 verify(automaticBean, times(1)).setupChild(childConfig);
 }

 @Test(expected = CheckstyleException.class)
 public void testSetupChildWithNullChildConfig() throws CheckstyleException {
 automaticBean.setupChild(null);
 }

 @Test(expected = CheckstyleException.class)
 public void testSetupChildWithInvalidChildConfigName() throws CheckstyleException {
 final Configuration invalidChildConfig = mock(Configuration.class);
 automaticBean.setupChild(invalidChildConfig);
 }
}