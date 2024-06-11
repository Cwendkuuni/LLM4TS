import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
String encodedPassword = passwordEncoder.encode("test");
assertFalse(encodedPassword.equals("test"));
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = mock(ObjectMapper.class);
SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
when(objectMapper.canSerialize(Internationalizable.class)).thenReturn(true);
when(objectMapper.getSerializerProvider().findValueSerializer(Internationalizable.class))
.thenReturn(new I18nOutputSerializer());
JacksonFactory.unsafeJsonMapper().registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt));
JacksonFactory.unsafeJsonMapper().registerModule(JacksonModules.customOutputHandling(objectMapper));
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
defaultHttpFirewall.setAllowUrlEncodedSlash(true);
}

@Test
public void testCookieSerializer() {
CookieSerializer serializer = new DefaultCookieSerializer();
serializer.setCookieName("JSESSIONID");
}

@Test
public void testNullCloudMetadataClient() {
NullCloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
assertEquals(false, cloudMetadataClient.supportsTenantInstance());
assertEquals(false, cloudMetadataClient.needsOBTenantName());
assertEquals(false, cloudMetadataClient.needsSysTenantUser());
assertEquals(true, cloudMetadataClient.includeClusterNameForJdbcUsername());
assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
}

}