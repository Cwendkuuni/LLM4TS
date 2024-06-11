import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@RunWith(SpringBootTest.class)
@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

@Autowired
private BeanConfiguration beanConfiguration;

@Autowired
private ObjectMapper objectMapper;

@Autowired
private PasswordEncoder passwordEncoder;

@Autowired
private CloudMetadataClient cloudMetadataClient;

@Autowired
private CookieSerializer cookieSerializer;

@InjectMocks
private BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testPasswordEncoder() {
String password = "test1234";
PasswordEncoder encoder = beanConfiguration.passwordEncoder();
String encodedPassword = encoder.encode(password);
assertNotNull(encodedPassword);
assertTrue(encoder.matches(password, encodedPassword));
}

@Test
public void testObjectMapper() {
ObjectMapper mapper = beanConfiguration.objectMapper(null);
assertNotNull(mapper);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
assertNotNull(firewall);
assertTrue(firewall instanceof DefaultHttpFirewall);
}

@Test
public void testCloudMetadataClient() {
CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
assertNotNull(client);
assertTrue(client instanceof BeanConfiguration.NullCloudMetadataClient);
}

@Test
public void testCookieSerializer() {
CookieSerializer serializer = beanConfiguration.cookieSerializer();
assertNotNull(serializer);
assertEquals("JSESSIONID", serializer.getCookieName());
}

@Test
public void testNullCloudMetadataClient() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
assertNotNull(client);
}

@Test
public void testNullCloudMetadataClientSupportsCloudMetadata() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
assertFalse(client.supportsCloudMetadata());
}

@Test
public void testNullCloudMetadataClientSupportsTenantInstance() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
assertFalse(client.supportsTenantInstance());
}

@Test
public void testNullCloudMetadataClientNeedsOBTenantName() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
assertFalse(client.needsOBTenantName());
}

@Test
public void testNullCloudMetadataClientNeedsSysTenantUser() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
assertFalse(client.needsSysTenantUser());
}

@Test
public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
assertTrue(client.includeClusterNameForJdbcUsername());
}

@Test
public void testNullCloudMetadataClientOceanbaseAccessMode() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
}

@Test
public void testNullCloudMetadataClientListInstances() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
List<OBInstance> instances = new ArrayList<>();
assertThrows(UnsupportedException.class, () -> client.listInstances());
}

@Test
public void testNullCloudMetadataClientListTenants() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
List<OBTenant> tenants = new ArrayList<>();
assertThrows(UnsupportedException.class, () -> client.listTenants("instanceId"));
}

@Test
public void testNullCloudMetadataClientGetTenant() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
OBTenant tenant = new OBTenant();
assertThrows(UnsupportedException.class, () -> client.getTenant("instanceId", "tenantId"));
}

@Test
public void testNullCloudMetadataClientListDatabaseUsers() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
List<OBDatabaseUser> users = new ArrayList<>();
assertThrows(UnsupportedException.class, () -> client.listDatabaseUsers("instanceId", "tenantId"));
}

@Test
public void testNullCloudMetadataClientGetTenantEndpoint() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
OBTenantEndpoint endpoint = new OBTenantEndpoint();
assertEquals(OceanBaseAccessMode.DIRECT, client.getTenantEndpoint("instanceId", "tenantId").getAccessMode());
}

@Test
public void testNullCloudMetadataClientGetSysTenantUser() {
BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.nullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> client.getSysTenantUser("instanceId"));
}
}