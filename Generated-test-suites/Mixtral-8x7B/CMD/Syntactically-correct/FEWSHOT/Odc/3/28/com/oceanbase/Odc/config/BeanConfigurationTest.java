import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 @InjectMocks
 private BeanConfiguration beanConfiguration;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 assertEquals(JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer))
 .getClass(), objectMapper.getClass());
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertEquals(DefaultHttpFirewall.class, defaultHttpFirewall.getClass());
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertEquals(DefaultCookieSerializer.class, cookieSerializer.getClass());
 }

}

class NullCloudMetadataClientTest {

 @Test
 public void testSupportsCloudMetadata() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testSupportsTenantInstance() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
 }

 @Test
 public void testNeedsOBTenantName() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
 }

 @Test
 public void testNeedsSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
 }

 @Test
 public void testIncludeClusterNameForJdbcUsername() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 }

 @Test
 public void testOceanbaseAccessMode() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 }

 @Test
 public void testListInstances() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 try {
 nullCloudMetadataClient.listInstances();
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testListTenants() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 String instanceId = "instanceId";
 try {
 nullCloudMetadataClient.listTenants(instanceId);
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testGetTenant() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 try {
 nullCloudMetadataClient.getTenant(instanceId, tenantId);
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testListDatabaseUsers() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 try {
 nullCloudMetadataClient.listDatabaseUsers(instanceId, tenantId);
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testGetTenantEndpoint() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint(instanceId, tenantId);
 OceanBaseAccessMode accessMode = endpoint.getAccessMode();
 assertEquals(OceanBaseAccessMode.DIRECT, accessMode);
 }

 @Test
 public void testGetSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 String instanceId = "instanceId";
 try {
 nullCloudMetadataClient.getSysTenantUser(instanceId);
 } catch (UnsupportedException e) {
 // expected
 }
 }

}