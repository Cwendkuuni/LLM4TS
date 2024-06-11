import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 @InjectMocks
 private BeanConfiguration beanConfiguration;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
 }

 @Test
 public void testObjectMapper() throws Exception {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);

 // Verify the custom serializer is registered
 CustomOutputSerializer customOutputSerializer =
 jacksonObjectMapper().getSerializationConfig().getAdditionalSerializers().get(CustomOutputSerializer.class);
 assertNotNull(customOutputSerializer);

 List<String> testList = new ArrayList<>();
 testList.add("test1");
 testList.add("test2");

 ObjectMapper jsonMapper = new ObjectMapper();
 String jsonString = jsonMapper.writeValueAsString(testList);

 List<String> deserializedList = objectMapper.readValue(jsonString, List.class);
 assertEquals(testList, deserializedList);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(httpFirewall);
 assertEquals(DefaultHttpFirewall.class, httpFirewall.getClass());
 ((DefaultHttpFirewall) httpFirewall).setAllowUrlEncodedSlash(true);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 assertEquals("JSESSIONID", cookieSerializer.getCookieName());
 }

 private ObjectMapper jacksonObjectMapper() {
 return JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer())));
 }

}