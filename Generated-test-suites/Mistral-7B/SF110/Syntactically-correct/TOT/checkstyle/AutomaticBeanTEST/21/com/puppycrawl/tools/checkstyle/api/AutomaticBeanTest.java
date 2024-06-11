import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConverterRegistry;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.list.ArrayListList;
import org.apache.commons.collections4.list.ListUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

 private static final String CONFIG_KEY_BOOL = "bool";
 private static final boolean CONFIG_VALUE_BOOL = true;
 private static final String CONFIG_KEY_STRING = "string";
 private static final String CONFIG_VALUE_STRING = "test";
 private static final String CONFIG_KEY_ARRAY_BOOL = "arrayBool";
 private static final boolean[] CONFIG_VALUE_ARRAY_BOOL = {true, false};
 private static final String CONFIG_KEY_ARRAY_STRING = "arrayString";
 private static final String[] CONFIG_VALUE_ARRAY_STRING = {"test1", "test2"};

 @Mock
 private Configuration configMock;

 @InjectMocks
 private AutomaticBean automaticBean;

 @Before
 public void setUp() {
 ConverterRegistry registry = new ConverterRegistry();
 registry.register(new BooleanConverter(), Boolean.TYPE, Boolean.class);
 registry.register(new BooleanConverter(), boolean[].class, boolean.class);
 registry.register(new ArrayConverter(boolean[].class, new BooleanConverter()), boolean[].class);
 registry.register(new StringConverter(), String.class, String[].class);
 registry.register(new RelaxedStringArrayConverter(), String[].class);
 BeanUtils.setPropertyUtilsBean(AutomaticBeanTest.class, new PropertyUtilsBean(registry));
 }

 @Test
 public void testConfigureWithSingleProperty() throws CheckstyleException {
 setupConfig(CONFIG_KEY_BOOL, CONFIG_VALUE_BOOL);
 automaticBean.configure(configMock);
 assertBoolean(CONFIG_KEY_BOOL, automaticBean.getConfiguration().getAttribute(CONFIG_KEY_BOOL));
 }

 @Test
 public void testConfigureWithArrayProperty() throws CheckstyleException {
 setupConfig(CONFIG_KEY_ARRAY_BOOL, CONFIG_VALUE_ARRAY_BOOL);
 automaticBean.configure(configMock);
 assertBooleanArray(CONFIG_KEY_ARRAY_BOOL, automaticBean.getConfiguration().getAttribute(CONFIG_KEY_ARRAY_BOOL));
 }

 @Test
 public void testConfigureWithStringProperty() throws CheckstyleException {
 setupConfig(CONFIG_KEY_STRING, CONFIG_VALUE_STRING);
 automaticBean.configure(configMock);
 assertString(CONFIG_KEY_STRING, automaticBean.getConfiguration().getAttribute(CONFIG_KEY_STRING));
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidProperty() throws CheckstyleException {
 setupConfig("invalidProperty", 1);
 automaticBean.configure(configMock);
 }

 @Test(expected = CheckstyleException.class)
 public void testConfigureWithInvalidTypeProperty() throws CheckstyleException {
 setupConfig("invalidTypeProperty", "test");
 automaticBean.configure(configMock);
 }

 @Test
 public void testContextualizeWithSingleProperty() throws CheckstyleException {
 setupContext(CONFIG_KEY_BOOL, CONFIG_VALUE_BOOL);
 automaticBean.contextualize(new HashMap<String, Object>() {{
 put(CONFIG_KEY_BOOL, CONFIG_VALUE_BOOL);
 }});
 assertBoolean(CONFIG_KEY_BOOL, automaticBean.getConfiguration().getAttribute(CONFIG_KEY_BOOL));
 }

 @Test
 public void testContextualizeWithArrayProperty() throws CheckstyleException {
 setupContext(CONFIG_KEY_ARRAY_BOOL, CONFIG_VALUE_ARRAY_BOOL);
 automaticBean.contextualize(new HashedMap<String, Object>() {{
 put(CONFIG_KEY_ARRAY_BOOL, CONFIG_VALUE_ARRAY_BOOL);
 }});
 assertBooleanArray(CONFIG_KEY_ARRAY_BOOL, automaticBean.getConfiguration().getAttribute(CONFIG_KEY_ARRAY_BOOL));
 }

 @Test
 public void testContextualizeWithStringProperty() throws CheckstyleException {
 setupContext(CONFIG_KEY_STRING, CONFIG_VALUE_STRING);
 automaticBean.contextualize(new HashMap<String, Object>() {{
 put(CONFIG_KEY_STRING, CONFIG_VALUE_STRING);
 }});
 assertString(CONFIG_KEY_STRING, automaticBean.getConfiguration().getAttribute(CONFIG_KEY_STRING));
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithInvalidProperty() throws CheckstyleException {
 setupContext("invalidProperty", 1);
 automaticBean.contextualize(new HashMap<String, Object>() {{
 put(CONFIG_KEY_BOOL, CONFIG_VALUE_BOOL);
 }});
 }

 @Test(expected = CheckstyleException.class)
 public void testContextualizeWithInvalidTypeProperty() throws CheckstyleException {
 setupContext("invalidTypeProperty", "test");
 automaticBean.contextualize(new HashMap<String, Object>() {{
 put(CONFIG_KEY_BOOL, CONFIG_VALUE_BOOL);
 }});
 }

 private void setupConfig(String key, Object value) {
 Mockito.when(configMock.getAttributeNames()).thenReturn(new String[] {key});
 Mockito.when(configMock.getAttribute(key)).thenReturn(value);
 }

 private void setupContext(String key, Object value) {
 Mockito.when(configMock.getAttributeNames()).thenReturn(new String[] {key});
 Mockito.when(configMock.get(key)).thenReturn(value);
 }

 private boolean getBoolean(Object obj, String propertyName) throws InvocationTargetException, IntrospectionException, IllegalAccessException {
 PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(obj, propertyName);
 return (boolean) descriptor.getReadMethod().invoke(obj);
 }

 private boolean[] getBooleanArray(Object obj, String propertyName) throws InvocationTargetException, IntrospectionException, IllegalAccessException {
 PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(obj, propertyName);
 return (boolean[]) descriptor.getReadMethod().invoke(obj);
 }

 private String getString(Object obj, String propertyName) throws InvocationTargetException, IntrospectionException, IllegalAccessException {
 PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(obj, propertyName);
 return (String) descriptor.getReadMethod().invoke(obj);
 }

 private String[] getStringArray(Object obj, String propertyName) throws InvocationTargetException, IntrospectionException, IllegalAccessException {
 PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(obj, propertyName);
 return (String[]) descriptor.getReadMethod().invoke(obj);
 }

 private void assertBoolean(String propertyName, boolean expectedValue) {
 boolean actualValue = getBoolean(automaticBean, propertyName);
 assertEquals(propertyName, expectedValue, actualValue);
 }

 private void assertBooleanArray(String propertyName, boolean[] expectedValues) {
 boolean[] actualValues = getBooleanArray(automaticBean, propertyName);
 assertArrayEquals(propertyName, expectedValues, actualValues);
 }

 private void assertString(String propertyName, String expectedValue) {
 String actualValue = getString(automaticBean, propertyName);
 assertEquals(propertyName, expectedValue, actualValue);
 }

 private void assertStringArray(String propertyName, String[] expectedValues) {
 String[] actualValues = getStringArray(automaticBean, propertyName);
 assertArrayEquals(propertyName, expectedValues, actualValues);
 }

 private void assertEquals(String propertyName, Object expectedValue, Object actualValue) {
 if (expectedValue instanceof Boolean && actualValue instanceof Boolean) {
 assertEquals(propertyName, expectedValue.booleanValue(), actualValue.booleanValue());
 } else if (expectedValue instanceof boolean[]) {
 assertArrayEquals(propertyName, expectedValue, (boolean[]) actualValue);
 } else if (expectedValue instanceof String[]) {
 assertArrayEquals(propertyName, expectedValue, (String[]) actualValue);
 } else if (expectedValue != null) {
 assertEquals(propertyName, expectedValue, actualValue);
 } else {
 assertNull(propertyName, actualValue);
 }
 }

 private void assertArrayEquals(String propertyName, Object expectedValues, Object actualValues) {
 if (expectedValues instanceof boolean[]) {
 assertArrayEquals(propertyName, expectedValues, (boolean[]) actualValues);
 } else if (expectedValues != null) {
 assertArrayEquals(propertyName, expectedValues, (Object[]) actualValues);
 } else {
 assertNull(propertyName, actualValues);
 }
 }
}