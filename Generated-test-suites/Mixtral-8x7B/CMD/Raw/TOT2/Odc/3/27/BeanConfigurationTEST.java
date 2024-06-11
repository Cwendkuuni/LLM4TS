import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
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
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(httpFirewall);
 assertTrue(httpFirewall instanceof DefaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 assertEquals("JSESSIONID", cookieSerializer.getCookieName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
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
 String instanceId = "instanceId";
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants(instanceId));
 }

 @Test
 public void testNullCloudMetadataClientGetTenant() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant(instanceId, tenantId));
 }

 @Test
 public void testNullCloudMetadataClientListDatabaseUsers() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers(instanceId, tenantId));
 }

 @Test
 public void testNullCloudMetadataClientGetTenantEndpoint() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint(instanceId, tenantId);
 assertNotNull(endpoint);
 assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
 }

 @Test
 public void testNullCloudMetadataClientGetSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 String instanceId = "instanceId";
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser(instanceId));
 }

 @Test
 public void testCustomOutputSerializer() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
 assertNotNull(customOutputSerializer);
 }

 @Test
 public void testCustomOutputSerializerAddSerializer() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
 customOutputSerializer.addSerializer(Internationalizable.class, new I18nOutputSerializer());
 assertNotNull(customOutputSerializer);
 }

 @Test
 public void testUnsafeJsonMapper() {
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper();
 assertNotNull(objectMapper);
 }

 @Test
 public void testRegisterModule() {
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper();
 ObjectMapper registeredMapper = objectMapper.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt));
 assertNotNull(registeredMapper);
 assertNotSame(objectMapper, registeredMapper);
 }

 @Test
 public void testRegisterModule2() {
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper();
 ObjectMapper registeredMapper = objectMapper.registerModule(JacksonModules.customOutputHandling(customOutputSerializer()));
 assertNotNull(registeredMapper);
 assertNotSame(objectMapper, registeredMapper);
 }

 @Test
 public void testCustomOutputHandling() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
 customOutputSerializer.addSerializer(Internationalizable.class, new I18nOutputSerializer());
 ObjectMapper registeredMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
 assertNotNull(registeredMapper);
 }

 @Test
 public void testSensitiveInputHandling() {
 ObjectMapper registeredMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt));
 assertNotNull(registeredMapper);
 }

 private CustomOutputSerializer customOutputSerializer() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
 customOutputSerializer.addSerializer(Internationalizable.class, new I18nOutputSerializer());
 return customOutputSerializer;
 }

 @Test
 public void testListInstances() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 List<OBInstance> instances = new ArrayList<>();
 instances.add(new OBInstance());
 when(cloudMetadataClient.listInstances()).thenReturn(instances);
 List<OBInstance> result = cloudMetadataClient.listInstances();
 assertNotNull(result);
 assertEquals(1, result.size());
 }

 @Test
 public void testListTenants() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 String instanceId = "instanceId";
 List<OBTenant> tenants = new ArrayList<>();
 tenants.add(new OBTenant());
 when(cloudMetadataClient.listTenants(instanceId)).thenReturn(tenants);
 List<OBTenant> result = cloudMetadataClient.listTenants(instanceId);
 assertNotNull(result);
 assertEquals(1, result.size());
 }

 @Test
 public void testGetTenant() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 OBTenant tenant = new OBTenant();
 when(cloudMetadataClient.getTenant(instanceId, tenantId)).thenReturn(tenant);
 OBTenant result = cloudMetadataClient.getTenant(instanceId, tenantId);
 assertNotNull(result);
 }

 @Test
 public void testListDatabaseUsers() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 List<OBDatabaseUser> databaseUsers = new ArrayList<>();
 databaseUsers.add(new OBDatabaseUser());
 when(cloudMetadataClient.listDatabaseUsers(instanceId, tenantId)).thenReturn(databaseUsers);
 List<OBDatabaseUser> result = cloudMetadataClient.listDatabaseUsers(instanceId, tenantId);
 assertNotNull(result);
 assertEquals(1, result.size());
 }

 @Test
 public void testGetSysTenantUser() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 String instanceId = "instanceId";
 OBDatabaseUser sysTenantUser = new OBDatabaseUser();
 when(cloudMetadataClient.getSysTenantUser(instanceId)).thenReturn(sysTenantUser);
 OBDatabaseUser result = cloudMetadataClient.getSysTenantUser(instanceId);
 assertNotNull(result);
 }
}