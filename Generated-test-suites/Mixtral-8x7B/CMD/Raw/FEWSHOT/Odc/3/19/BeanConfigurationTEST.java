import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 private BeanConfiguration beanConfiguration;

 @Before
 public void setup() {
 beanConfiguration = new BeanConfiguration();
 ReflectionTestUtils.setField(beanConfiguration, "sensitivePropertyHandler", sensitivePropertyHandler);
 }

 @Test
 public void testPasswordEncoderBean() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapperBean() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewallBean() {
 HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
 assertTrue(httpFirewall instanceof DefaultHttpFirewall);
 DefaultHttpFirewall defaultHttpFirewall = (DefaultHttpFirewall) httpFirewall;
 assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
 }

 @Test
 public void testCloudMetadataClientBean() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializerBean() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
 DefaultCookieSerializer defaultCookieSerializer = (DefaultCookieSerializer) cookieSerializer;
 assertEquals("JSESSIONID", defaultCookieSerializer.getCookieName());
 }

 @Test
 public void testCustomOutputSerializer() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
 customOutputSerializer.addSerializer(Internationalizable.class, new I18nOutputSerializer());

 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

 String json = "{\"key\":\"value\"}";
 Internationalizable internationalizable = new Internationalizable() {
 @Override
 public String getName() {
 return "test";
 }
 };
 ObjectMapper.DefaultTyping typing = ObjectMapper.DefaultTyping.NON_FINAL;
 when(sensitivePropertyHandler.decrypt("value")).thenReturn("decryptedValue");

 // Test serialization
 String serializedJson = objectMapper.writeValueAsString(json);
 assertEquals(json, serializedJson);

 String serializedInternationalizable = objectMapper.writeValueAsString(internationalizable);
 assertTrue(serializedInternationalizable.contains("test"));
 }
}