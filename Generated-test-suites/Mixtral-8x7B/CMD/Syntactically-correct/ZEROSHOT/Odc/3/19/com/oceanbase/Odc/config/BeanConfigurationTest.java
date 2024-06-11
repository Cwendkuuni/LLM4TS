import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BeanConfigurationTest {

@Autowired
private BeanConfiguration beanConfiguration;

@Before
public void setup() {
beanConfiguration = new BeanConfiguration();
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
assertNotNull(passwordEncoder);
assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = beanConfiguration.objectMapper(mock(SensitivePropertyHandler.class));
assertNotNull(objectMapper);
assertNotNull(objectMapper.getSerializerProvider());
assertNotNull(objectMapper.getDeserializationConfig());
assertNotNull(objectMapper.getSerializationConfig());
assertNotNull(objectMapper.getSerializerProvider().getLookupProvider());
assertNotNull(objectMapper.getDeserializationConfig().getBase());
assertNotNull(objectMapper.getSerializationConfig().getBase());
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
assertNotNull(defaultHttpFirewall);
assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
}

@Test
public void testCloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
assertNotNull(cloudMetadataClient);
assertFalse(cloudMetadataClient instanceof NullCloudMetadataClient);
}

@Test
public void testCookieSerializer() {
CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
assertNotNull(cookieSerializer);
assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
assertEquals("JSESSIONID", cookieSerializer.getCookieName());
}

@Test
public void testNullCloudMetadataClient() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertNotNull(nullCloudMetadataClient);
assertTrue(nullCloudMetadataClient instanceof NullCloudMetadataClient);
assertNull(nullCloudMetadataClient.listInstances());
assertNull(nullCloudMetadataClient.listTenants("instanceId"));
assertNull(nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
assertNull(nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
assertNull(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
assertNull(nullCloudMetadataClient.getSysTenantUser("instanceId"));
}

@Test
public void testNullCloudMetadataClientSupportsCloudMetadata() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
}

@Test
public void testNullCloudMetadataClientSupportsTenantInstance() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertFalse(nullCloudMetadataClient.supportsTenantInstance());
}

@Test
public void testNullCloudMetadataClientNeedsOBTenantName() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertFalse(nullCloudMetadataClient.needsOBTenantName());
}

@Test
public void testNullCloudMetadataClientNeedsSysTenantUser() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertFalse(nullCloudMetadataClient.needsSysTenantUser());
}

@Test
public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
}

@Test
public void testNullCloudMetadataClientOceanbaseAccessMode() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
}

@Test
public void testNullCloudMetadataClientListInstances() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
}

@Test
public void testNullCloudMetadataClientListTenants() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
}

@Test
public void testNullCloudMetadataClientGetTenant() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
}

@Test
public void testNullCloudMetadataClientListDatabaseUsers() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
}

@Test
public void testNullCloudMetadataClientGetTenantEndpoint() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
}

@Test
public void testNullCloudMetadataClientGetSysTenantUser() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
}

@Test
public void testCustomOutputSerializer() {
CustomOutputSerializer customOutputSerializer = JacksonModules.customOutputHandling(new I18nOutputSerializer());
assertNotNull(customOutputSerializer);
}

@Test
public void testSensitiveInputHandling() {
SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt));
assertNotNull(objectMapper);
}

}

@Configuration
class TestBeanConfiguration {

@Bean
public SensitivePropertyHandler sensitivePropertyHandler() {
return mock(SensitivePropertyHandler.class);
}

@Bean
public CloudMetadataClient cloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(false);
when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);
return cloudMetadataClient;
}

@Bean
public List<OBInstance> listInstances() {
List<OBInstance> instances = new ArrayList<>();
OBInstance instance = new OBInstance();
instance.setId("instanceId");
instances.add(instance);
return instances;
}

@Bean
public OBTenant getTenant() {
OBTenant tenant = new OBTenant();
tenant.setId("tenantId");
return tenant;
}

@Bean
public List<OBDatabaseUser> listDatabaseUsers() {
List<OBDatabaseUser> databaseUsers = new ArrayList<>();
OBDatabaseUser databaseUser = new OBDatabaseUser();
databaseUser.setName("databaseUserName");
databaseUsers.add(databaseUser);
return databaseUsers;
}

@Bean
public OBTenantEndpoint getTenantEndpoint() {
OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
tenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
return tenantEndpoint;
}

@Bean
public OBDatabaseUser getSysTenantUser() {
OBDatabaseUser databaseUser = new OBDatabaseUser();
databaseUser.setUsername("sysTenantUserName");
return databaseUser;
}

}

@Configuration
class TestBeanConfigurationWithCloudMetadataClient {

@Bean
public SensitivePropertyHandler sensitivePropertyHandler() {
return mock(SensitivePropertyHandler.class);
}

@Bean
public CloudMetadataClient cloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(true);
when(cloudMetadataClient.supportsTenantInstance()).thenReturn(true);
when(cloudMetadataClient.needsOBTenantName()).thenReturn(true);
when(cloudMetadataClient.needsSysTenantUser()).thenReturn(true);
when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);
return cloudMetadataClient;
}

@Bean
public List<OBInstance> listInstances() {
List<OBInstance> instances = new ArrayList<>();
OBInstance instance = new OBInstance();
instance.setId("instanceId");
instances.add(instance);
return instances;
}

@Bean
public OBTenant getTenant() {
OBTenant tenant = new OBTenant();
tenant.setId("tenantId");
return tenant;
}

@Bean
public List<OBDatabaseUser> listDatabaseUsers() {
List<OBDatabaseUser> databaseUsers = new ArrayList<>();
OBDatabaseUser databaseUser = new OBDatabaseUser();
databaseUser.setName("databaseUserName");
databaseUsers.add(databaseUser);
return databaseUsers;
}

@Bean
public OBTenantEndpoint getTenantEndpoint() {
OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
tenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
return tenantEndpoint;
}

@Bean
public OBDatabaseUser getSysTenantUser() {
OBDatabaseUser databaseUser = new OBDatabaseUser();
databaseUser.setUsername("sysTenantUserName");
return databaseUser;
}

}