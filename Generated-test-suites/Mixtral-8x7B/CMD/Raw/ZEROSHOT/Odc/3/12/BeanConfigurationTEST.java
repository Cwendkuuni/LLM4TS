import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class BeanConfigurationTest {

 @Before
 public void setUp() {
 AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
 context.register(BeanConfiguration.class);
 context.refresh();
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
 Assert.assertNotNull(passwordEncoder);
 Assert.assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
 Assert.assertNotNull(objectMapper);
 }

 @Test
 public void testHttpFirewall() {
 HttpFirewall httpFirewall = context.getBean(HttpFirewall.class);
 Assert.assertNotNull(httpFirewall);
 Assert.assertTrue(httpFirewall instanceof DefaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 Assert.assertNotNull(cloudMetadataClient);
 Assert.assertTrue(cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = context.getBean(CookieSerializer.class);
 Assert.assertNotNull(cookieSerializer);
 Assert.assertEquals("JSESSIONID", cookieSerializer.getCookieName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 Assert.assertNotNull(nullCloudMetadataClient);
 }

 @Test
 public void testSensitivePropertyHandler() {
 SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 ObjectMapper objectMapper = context.getBean(ObjectMapper.class, sensitivePropertyHandler);
 Assert.assertNotNull(objectMapper);
 }

 @Test
 public void testCustomOutputSerializer() {
 ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
 CustomOutputSerializer customOutputSerializer = objectMapper.getSerializationConfig().getSerializerProvider().getNullValueSerializer();
 Assert.assertNotNull(customOutputSerializer);
 }

 @Test
 public void testListInstances() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 List<OBInstance> instances = cloudMetadataClient.listInstances();
 Assert.assertNotNull(instances);
 Assert.assertTrue(instances.isEmpty());
 }

 @Test
 public void testListTenants() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 String instanceId = "testInstanceId";
 List<OBTenant> tenants = cloudMetadataClient.listTenants(instanceId);
 Assert.assertNotNull(tenants);
 Assert.assertTrue(tenants.isEmpty());
 }

 @Test
 public void testGetTenant() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 String instanceId = "testInstanceId";
 String tenantId = "testTenantId";
 OBTenant tenant = cloudMetadataClient.getTenant(instanceId, tenantId);
 Assert.assertNotNull(tenant);
 }

 @Test
 public void testListDatabaseUsers() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 String instanceId = "testInstanceId";
 String tenantId = "testTenantId";
 List<OBDatabaseUser> databaseUsers = cloudMetadataClient.listDatabaseUsers(instanceId, tenantId);
 Assert.assertNotNull(databaseUsers);
 Assert.assertTrue(databaseUsers.isEmpty());
 }

 @Test
 public void testGetTenantEndpoint() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 String instanceId = "testInstanceId";
 String tenantId = "testTenantId";
 OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint(instanceId, tenantId);
 Assert.assertNotNull(endpoint);
 }

 @Test
 public void testGetSysTenantUser() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 String instanceId = "testInstanceId";
 OBDatabaseUser sysTenantUser = cloudMetadataClient.getSysTenantUser(instanceId);
 Assert.assertNull(sysTenantUser);
 }

 @Test
 public void testOceanbaseAccessMode() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 OceanBaseAccessMode oceanBaseAccessMode = cloudMetadataClient.oceanbaseAccessMode();
 Assert.assertEquals(OceanBaseAccessMode.DIRECT, oceanBaseAccessMode);
 }

 @Test
 public void testIncludeClusterNameForJdbcUsername() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 boolean includeClusterNameForJdbcUsername = cloudMetadataClient.includeClusterNameForJdbcUsername();
 Assert.assertTrue(includeClusterNameForJdbcUsername);
 }

 @Test
 public void testNeedsSysTenantUser() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 boolean needsSysTenantUser = cloudMetadataClient.needsSysTenantUser();
 Assert.assertFalse(needsSysTenantUser);
 }

 @Test
 public void testNeedsOBTenantName() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 boolean needsOBTenantName = cloudMetadataClient.needsOBTenantName();
 Assert.assertFalse(needsOBTenantName);
 }

 @Test
 public void testSupportsTenantInstance() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 boolean supportsTenantInstance = cloudMetadataClient.supportsTenantInstance();
 Assert.assertFalse(supportsTenantInstance);
 }

 @Test
 public void testSupportsCloudMetadata() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 boolean supportsCloudMetadata = cloudMetadataClient.supportsCloudMetadata();
 Assert.assertFalse(supportsCloudMetadata);
 }
}