import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

@Before
public void setup() {
CustomOutputSerializer customOutputSerializer = mock(CustomOutputSerializer.class);
when(customOutputSerializer.addSerializer(I18nOutputSerializer.class, new I18nOutputSerializer()))
.thenReturn(customOutputSerializer);
ObjectMapper objectMapper = mock(ObjectMapper.class);
when(objectMapper.registerModule(JacksonModules.sensitiveInputHandling(mock(SensitivePropertyHandler.class)::decrypt)))
.thenReturn(objectMapper);
when(objectMapper.registerModule(customOutputSerializer))
.thenReturn(objectMapper);
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
assertEquals(true, passwordEncoder.matches("password", "$2a$10$cLCsRjVZsCzrO1TmgbF9He66ZKdDQ8f3jWQvhZMU1JGxWtN3rOyRu"));
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = new ObjectMapper();
assertEquals(objectMapper, JacksonFactory.unsafeJsonMapper().registerModule(JacksonModules.sensitiveInputHandling(mock(SensitivePropertyHandler.class)::decrypt))
.registerModule(JacksonModules.customOutputHandling(mock(CustomOutputSerializer.class))));
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
defaultHttpFirewall.setAllowUrlEncodedSlash(true);
assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
}

@Test
public void testCookieSerializer() {
CookieSerializer cookieSerializer = new DefaultCookieSerializer();
cookieSerializer.setCookieName("JSESSIONID");
assertEquals("JSESSIONID", cookieSerializer.getCookieName());
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

@Test
public void testListInstances() {
NullCloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
List<OBInstance> instances = new ArrayList<>();
when(cloudMetadataClient.listInstances()).thenReturn(instances);
assertEquals(instances, cloudMetadataClient.listInstances());
}

@Test
public void testListTenants() {
String instanceId = "testInstanceId";
NullCloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
List<OBTenant> tenants = new ArrayList<>();
when(cloudMetadataClient.listTenants(instanceId)).thenReturn(tenants);
assertEquals(tenants, cloudMetadataClient.listTenants(instanceId));
}

@Test
public void testGetTenant() {
String instanceId = "testInstanceId";
String tenantId = "testTenantId";
NullCloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
OBTenant tenant = mock(OBTenant.class);
when(cloudMetadataClient.getTenant(instanceId, tenantId)).thenReturn(tenant);
assertEquals(tenant, cloudMetadataClient.getTenant(instanceId, tenantId));
}

@Test
public void testListDatabaseUsers() {
String instanceId = "testInstanceId";
String tenantId = "testTenantId";
NullCloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
List<OBDatabaseUser> databaseUsers = new ArrayList<>();
when(cloudMetadataClient.listDatabaseUsers(instanceId, tenantId)).thenReturn(databaseUsers);
assertEquals(databaseUsers, cloudMetadataClient.listDatabaseUsers(instanceId, tenantId));
}

@Test
public void testGetTenantEndpoint() {
String instanceId = "testInstanceId";
String tenantId = "testTenantId";
NullCloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
OBTenantEndpoint endpoint = new OBTenantEndpoint();
endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
when(cloudMetadataClient.getTenantEndpoint(instanceId, tenantId)).thenReturn(endpoint);
assertEquals(endpoint, cloudMetadataClient.getTenantEndpoint(instanceId, tenantId));
}

@Test
public void testGetSysTenantUser() {
String instanceId = "testInstanceId";
NullCloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
when(cloudMetadataClient.getSysTenantUser(instanceId)).thenThrow(new UnsupportedException("getTenant not supported in non-cloud"));
assertNull(cloudMetadataClient.getSysTenantUser(instanceId));
}
}