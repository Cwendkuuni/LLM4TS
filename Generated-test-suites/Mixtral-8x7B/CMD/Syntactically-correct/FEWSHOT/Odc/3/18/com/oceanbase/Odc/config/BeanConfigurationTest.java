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
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

@Before
public void setup() {
// Initialize any necessary objects or mocks here
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
String encodedPassword = passwordEncoder.encode("testPassword");
assertNotNull(encodedPassword);
}

@Test
public void testObjectMapper() {
CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
.addSerializer(Internationalizable.class, new I18nOutputSerializer());
ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
.registerModule(JacksonModules.sensitiveInputHandling(s -> {}))
.registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
assertNotNull(objectMapper);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
defaultHttpFirewall.setAllowUrlEncodedSlash(true);
assertNotNull(defaultHttpFirewall);
}

@Test
public void testCloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
assertNotNull(cloudMetadataClient);
}

@Test
public void testCookieSerializer() {
CookieSerializer serializer = new DefaultCookieSerializer();
serializer.setCookieName("JSESSIONID");
assertNotNull(serializer);
}

@Test
public void testNullCloudMetadataClient() {
NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
assertNotNull(nullCloudMetadataClient);
assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
assertNotNull(endpoint);
assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
}

@Test
public void testListInstances() {
CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
when(cloudMetadataClient.listInstances()).thenReturn(List.of(new OBInstance()));
assertNotNull(cloudMetadataClient.listInstances());
}

@Test
public void testListTenants() {
CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
when(cloudMetadataClient.listTenants("instanceId")).thenReturn(List.of(new OBTenant()));
assertNotNull(cloudMetadataClient.listTenants("instanceId"));
}

@Test
public void testGetTenant() {
CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
when(cloudMetadataClient.getTenant("instanceId", "tenantId"))
.thenReturn(new OBTenant());
assertNotNull(cloudMetadataClient.getTenant("instanceId", "tenantId"));
}

@Test
public void testListDatabaseUsers() {
CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
when(cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"))
.thenReturn(List.of(new OBDatabaseUser()));
assertNotNull(cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
}

@Test
public void testGetSysTenantUser() {
CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
when(cloudMetadataClient.getSysTenantUser("instanceId"))
.thenReturn(new OBDatabaseUser());
assertNotNull(cloudMetadataClient.getSysTenantUser("instanceId"));
}
}