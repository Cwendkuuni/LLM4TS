import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
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

 private BeanConfiguration beanConfiguration;

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Mock
 private CloudMetadataClient cloudMetadataClient;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 beanConfiguration = new BeanConfiguration();
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 assertEquals(true, passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapper() throws Exception {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);

 Internationalizable internationalizable = mock(Internationalizable.class);
 when(internationalizable.getLocale()).thenReturn("en_US");
 when(internationalizable.getName("title")).thenReturn("Test Title");

 String json = objectMapper.writeValueAsString(internationalizable);
 assertNotNull(json);
 assertEquals("{\"name\":\"Test Title\",\"locale\":\"en_US\"}", json);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(httpFirewall);
 assertEquals(true, httpFirewall instanceof DefaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
 assertNotNull(client);
 assertEquals(true, client instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer serializer = beanConfiguration.cookieSerializer();
 assertNotNull(serializer);
 assertEquals("JSESSIONID", serializer.getCookieName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 assertNotNull(client);
 }

 @Test
 public void testUnsupportedException() {
 UnsupportedException exception = assertThrows(UnsupportedException.class,
 () -> cloudMetadataClient.listInstances());
 assertEquals("CloudMetadata not supported", exception.getMessage());
 }
}