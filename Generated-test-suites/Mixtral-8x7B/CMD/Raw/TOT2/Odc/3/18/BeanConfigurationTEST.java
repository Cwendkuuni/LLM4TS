import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 @InjectMocks
 private BeanConfiguration beanConfiguration;

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 String encodedPassword = passwordEncoder.encode("testPassword");
 assertNotNull(encodedPassword);
 }

 @Test
 public void testObjectMapper() throws Exception {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
 String jsonString = "{\"key\": \"value\"}";
 ObjectMapper.DefaultTyping typing = ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT;
 Object obj = objectMapper.readValue(jsonString, typing);
 assertNotNull(obj);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(httpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 assertEquals("JSESSIONID", cookieSerializer.getCookieName());
 }

}