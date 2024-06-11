import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

@Mock
private SensitivePropertyHandler sensitivePropertyHandler;

@InjectMocks
private BeanConfiguration beanConfiguration;

private ObjectMapper objectMapper;

@Before
public void setup() {
objectMapper = JacksonFactory.unsafeJsonMapper()
.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
.registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
.addSerializer(Internationalizable.class, new I18nOutputSerializer())));
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
assertNotNull(passwordEncoder);
assertEquals(true, passwordEncoder instanceof BCryptPasswordEncoder);
}

@Test
public void testObjectMapper() {
assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
assertNotNull(firewall);
assertEquals(true, firewall instanceof DefaultHttpFirewall);
}

@Test
public void testCloudMetadataClient() {
assertNotNull(beanConfiguration.cloudMetadataClient());
assertEquals(true, beanConfiguration.cloudMetadataClient() instanceof NullCloudMetadataClient);
}

@Test
public void testCookieSerializer() {
CookieSerializer serializer = beanConfiguration.cookieSerializer();
assertNotNull(serializer);
assertEquals("JSESSIONID", serializer.getCookieName());
}

@Test
public void testNullCloudMetadataClient() {
NullCloudMetadataClient client = beanConfiguration.cloudMetadataClient();
assertEquals(false, client.supportsCloudMetadata());
assertEquals(false, client.supportsTenantInstance());
assertEquals(false, client.needsOBTenantName());
assertEquals(false, client.needsSysTenantUser());
assertEquals(true, client.includeClusterNameForJdbcUsername());
assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
assertThrows(UnsupportedException.class, client::listInstances);
assertThrows(UnsupportedException.class, client::listTenants("instanceId"));
assertThrows(UnsupportedException.class, client::getTenant("instanceId", "tenantId"));
assertThrows(UnsupportedException.class, client::listDatabaseUsers("instanceId", "tenantId"));
assertNotNull(client.getTenantEndpoint("instanceId", "tenantId"));
assertThrows(UnsupportedException.class, client::getSysTenantUser("instanceId"));
}

}