import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.ModelTestUtil;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

public class BeanConfigurationTest {

 private CloudMetadataClient mockCloudMetadataClient;
 private SensitivePropertyHandler mockSensitiveHandler;

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
 String password = "testPassword";
 String encodedPassword = passwordEncoder.encode(password);
 assertNotNull(encodedPassword);
 assertTrue(passwordEncoder.matches(password, encodedPassword));
 }

 @Test
 public void testObjectMapper() throws Exception {
 ObjectMapper objectMapper = new BeanConfiguration().objectMapper(mockSensitiveHandler);
 CustomOutputSerializer customOutputSerializer = objectMapper.getSerializationConfig().getAdditionalSerializers()
 .findValueSerializer(CustomOutputSerializer.class).getSerializerProvider().getAdditionalSerializers()
 .findValueSerializer(CustomOutputSerializer.class);
 assertNotNull(customOutputSerializer);
 assertNotNull(customOutputSerializer.findSerializerInstance(Internationalizable.class));
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall firewall = new BeanConfiguration().defaultHttpFirewall();
 assertNotNull(firewall);
 assertTrue(firewall.getAllowedUrlEncodedSlash());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
 assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
 assertFalse(nullCloudMetadataClient.supportsTenantInstance());
 assertFalse(nullCloudMetadataClient.needsOBTenantName());
 assertFalse(nullCloudMetadataClient.needsSysTenantUser());
 assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer serializer = new BeanConfiguration().cookieSerializer();
 assertNotNull(serializer);
 assertEquals("JSESSIONID", serializer.getCookieName());
 }

 @Test
 public void testUnsupportedException() {
 CloudMetadataClient mockClient = mock(CloudMetadataClient.class);
 when(mockClient.listInstances()).thenThrow(new UnsupportedException("test"));
 assertThrows(UnsupportedException.class, () -> mockClient.listInstances());
 }

 @Test
 public void testGetTenant() {
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 OBTenantEndpoint endpoint = ModelTestUtil.createOBTenantEndpoint();
 endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 when(mockCloudMetadataClient.getTenant(anyString(), anyString())).thenAnswer(new Answer<OBTenant>() {
 public OBTenant answer(InvocationOnMock invocation) {
 return ModelTestUtil.createOBTenant(instanceId, tenantId, endpoint);
 }
 });
 OBTenant tenant = mockCloudMetadataClient.getTenant(instanceId, tenantId);
 assertNotNull(tenant);
 assertEquals(instanceId, tenant.getInstanceId());
 assertEquals(tenantId, tenant.getTenantId());
 assertEquals(endpoint.getAccessMode(), tenant.getAccessMode());
 }

 @Test
 public void testListDatabaseUsers() {
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 List<OBDatabaseUser> databaseUsers = new ArrayList<>();
 databaseUsers.add(ModelTestUtil.createOBDatabaseUser());
 when(mockCloudMetadataClient.listDatabaseUsers(anyString(), anyString())).thenReturn(databaseUsers);
 List<OBDatabaseUser> users = mockCloudMetadataClient.listDatabaseUsers(instanceId, tenantId);
 assertEquals(databaseUsers, users);
 }

 private CloudMetadataClient createMockCloudMetadataClient() {
 if (mockCloudMetadataClient == null) {
 mockCloudMetadataClient = mock(CloudMetadataClient.class);
 when(mockCloudMetadataClient.supportsCloudMetadata()).thenReturn(true);
 when(mockCloudMetadataClient.supportsTenantInstance()).thenReturn(true);
 when(mockCloudMetadataClient.needsOBTenantName()).thenReturn(true);
 when(mockCloudMetadataClient.needsSysTenantUser()).thenReturn(true);
 when(mockCloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
 when(mockCloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);
 when(mockCloudMetadataClient.listInstances())
 .thenAnswer(new Answer<List<OBInstance>>() {
 public List<OBInstance> answer(InvocationOnMock invocation) {
 return ModelTestUtil.createOBInstanceList();
 }
 });
 when(mockCloudMetadataClient.listTenants(anyString()))
 .thenAnswer(new Answer<List<OBTenant>>() {
 public List<OBTenant> answer(InvocationOnMock invocation) {
 return ModelTestUtil.createOBTenantList();
 }
 });
 when(mockCloudMetadataClient.getTenant(anyString(), anyString()))
 .thenAnswer(new Answer<OBTenant>() {
 public OBTenant answer(InvocationOnMock invocation) {
 return ModelTestUtil.createOBTenant();
 }
 });
 when(mockCloudMetadataClient.listDatabaseUsers(anyString(), anyString()))
 .thenAnswer(new Answer<List<OBDatabaseUser>>() {
 public List<OBDatabaseUser> answer(InvocationOnMock invocation) {
 return ModelTestUtil.createOBDatabaseUserList();
 }
 });
 when(mockCloudMetadataClient.getTenantEndpoint(anyString(), anyString()))
 .thenAnswer(new Answer<OBTenantEndpoint>() {
 public OBTenantEndpoint answer(InvocationOnMock invocation) {
 return ModelTestUtil.createOBTenantEndpoint();
 }
 });
 when(mockCloudMetadataClient.getSysTenantUser(anyString()))
 .thenAnswer(new Answer<OBDatabaseUser>() {
 public OBDatabaseUser answer(InvocationOnMock invocation) {
 return ModelTestUtil.createOBDatabaseUser();
 }
 });

 mockSensitiveHandler = mock(SensitivePropertyHandler.class);
 when(mockSensitiveHandler.decrypt(anyString())).thenReturn("decrypted");

 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
 customOutputSerializer.addSerializer(Internationalizable.class, new I18nOutputSerializer());
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(mockSensitiveHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
 when(mockCloudMetadataClient.getObjectMapper()).thenReturn(objectMapper);
 }
 return mockCloudMetadataClient;
 }
}