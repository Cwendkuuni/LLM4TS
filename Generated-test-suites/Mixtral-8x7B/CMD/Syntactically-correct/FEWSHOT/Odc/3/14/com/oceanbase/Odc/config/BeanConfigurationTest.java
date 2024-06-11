import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

@Before
public void setup() {
SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
when(sensitivePropertyHandler.decrypt("encryptedValue"))
.thenReturn("decryptedValue");
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
String encodedPassword = passwordEncoder.encode("password");
assertNotNull(encodedPassword);
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = new ObjectMapper(null, new CustomOutputSerializer());
assertNotNull(objectMapper);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
defaultHttpFirewall.setAllowUrlEncodedSlash(true);
assertNotNull(defaultHttpFirewall);
}

@Test
public void testNullCloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
assertEquals(false, cloudMetadataClient.supportsTenantInstance());
assertEquals(false, cloudMetadataClient.needsOBTenantName());
assertEquals(false, cloudMetadataClient.needsSysTenantUser());
assertEquals(true, cloudMetadataClient.includeClusterNameForJdbcUsername());
assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
assertThrows(UnsupportedException.class, cloudMetadataClient::listInstances);
assertThrows(UnsupportedException.class, cloudMetadataClient::listTenants("instanceId"));
assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("instanceId", "tenantId"));
assertThrows(UnsupportedException.class, cloudMetadataClient::listDatabaseUsers);
assertEquals(new OBTenantEndpoint(), cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
assertThrows(UnsupportedException.class, cloudMetadataClient::getSysTenantUser);
}

@Test
public void testCookieSerializer() {
CookieSerializer serializer = new DefaultCookieSerializer();
serializer.setCookieName("JSESSIONID");
assertNotNull(serializer);
}

@Configuration
static class TestConfiguration {

@Bean
public SensitivePropertyHandler sensitivePropertyHandler() {
return mock(SensitivePropertyHandler.class);
}

@Bean
public PasswordEncoder passwordEncoder() {
return new BCryptPasswordEncoder();
}

@Bean
public ObjectMapper objectMapper(SensitivePropertyHandler sensitivePropertyHandler) {
CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
.addSerializer(Internationalizable.class, new I18nOutputSerializer());
return JacksonFactory.unsafeJsonMapper()
.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
.registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
}

@Bean
public HttpFirewall defaultHttpFirewall() {
DefaultHttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
defaultHttpFirewall.setAllowUrlEncodedSlash(true);
return defaultHttpFirewall;
}

@Bean
@ConditionalOnMissingBean
public CloudMetadataClient cloudMetadataClient() {
return new NullCloudMetadataClient();
}

@Bean
@Primary
public CookieSerializer cookieSerializer() {
DefaultCookieSerializer serializer = new DefaultCookieSerializer();
serializer.setCookieName("JSESSIONID");
return serializer;
}
}
}