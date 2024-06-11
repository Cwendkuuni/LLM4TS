import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;
 private SensitivePropertyHandler sensitivePropertyHandler;
 private CloudMetadataClient cloudMetadataClient;

 @Before
 public void setup() {
 beanConfiguration = new BeanConfiguration();
 sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 cloudMetadataClient = mock(CloudMetadataClient.class);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapper() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertEquals(JacksonFactory.unsafeJsonMapper(), objectMapper.getFactory());
 assertTrue(objectMapper.getSerializers().contains(customOutputSerializer));
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
 assertTrue(defaultHttpFirewall.getAllowedUrlEncodedSlash());
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
 assertTrue(client instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer serializer = beanConfiguration.cookieSerializer();
 assertTrue(serializer instanceof DefaultCookieSerializer);
 assertEquals("JSESSIONID", serializer.getCookieName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 assertTrue(client instanceof NullCloudMetadataClient);
 }

 @Test
 public void testNullCloudMetadataClientSupportsCloudMetadata() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 assertFalse(client.supportsCloudMetadata());
 }

 @Test
 public void testNullCloudMetadataClientSupportsTenantInstance() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 assertFalse(client.supportsTenantInstance());
 }

 @Test
 public void testNullCloudMetadataClientNeedsOBTenantName() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 assertFalse(client.needsOBTenantName());
 }

 @Test
 public void testNullCloudMetadataClientNeedsSysTenantUser() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 assertFalse(client.needsSysTenantUser());
 }

 @Test
 public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 assertTrue(client.includeClusterNameForJdbcUsername());
 }

 @Test
 public void testNullCloudMetadataClientOceanbaseAccessMode() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 OceanBaseAccessMode mode = client.oceanbaseAccessMode();
 assertEquals(OceanBaseAccessMode.DIRECT, mode);
 }

 @Test
 public void testNullCloudMetadataClientListInstances() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 try {
 client.listInstances();
 fail("Expected UnsupportedException");
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testNullCloudMetadataClientListTenants() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 try {
 client.listTenants("instanceId");
 fail("Expected UnsupportedException");
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testNullCloudMetadataClientGetTenant() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 try {
 client.getTenant("instanceId", "tenantId");
 fail("Expected UnsupportedException");
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testNullCloudMetadataClientListDatabaseUsers() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 try {
 client.listDatabaseUsers("instanceId", "tenantId");
 fail("Expected UnsupportedException");
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testNullCloudMetadataClientGetTenantEndpoint() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 OBTenantEndpoint endpoint = client.getTenantEndpoint("instanceId", "tenantId");
 assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
 }

 @Test
 public void testNullCloudMetadataClientGetSysTenantUser() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 try {
 client.getSysTenantUser("instanceId");
 fail("Expected UnsupportedException");
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testNullCloudMetadataClientIsInstanceRegistered() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(true);
 when(cloudMetadataClient.listInstances()).thenReturn(Arrays.asList(new OBInstance()));
 assertFalse(client.isInstanceRegistered());
 when(cloudMetadataClient.listInstances()).thenReturn(Arrays.asList());
 assertTrue(client.isInstanceRegistered());
 }

 @Test
 public void testNullCloudMetadataClientRegisterInstance() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 assertTrue(client.registerInstance(new OBInstance()));
 }

 @Test
 public void testNullCloudMetadataClientUnregisterInstance() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 assertTrue(client.unregisterInstance("instanceId"));
 }
}