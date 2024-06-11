import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.springframework.session.web.http.CookieSerializer;

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

public class BeanConfigurationTest {

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 private BeanConfiguration beanConfiguration;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 beanConfiguration = new BeanConfiguration();
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
 }

 @Test
 public void testObjectMapper() throws Exception {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);

 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());

 assertEquals(customOutputSerializer.getClass(), objectMapper.getSerializerProvider().getSerializerModifiers().get(0).getClass());
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 assertEquals(DefaultHttpFirewall.class, defaultHttpFirewall.getClass());
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 assertEquals(DefaultCookieSerializer.class, cookieSerializer.getClass());
 }

}

class NullCloudMetadataClientTest {

 private NullCloudMetadataClient nullCloudMetadataClient;

 @Before
 public void setup() {
 nullCloudMetadataClient = new NullCloudMetadataClient();
 }

 @Test
 public void testSupportsCloudMetadata() {
 assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testSupportsTenantInstance() {
 assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
 }

 @Test
 public void testNeedsOBTenantName() {
 assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
 }

 @Test
 public void testNeedsSysTenantUser() {
 assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
 }

 @Test
 public void testIncludeClusterNameForJdbcUsername() {
 assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 }

 @Test
 public void testOceanbaseAccessMode() {
 assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 }

 @Test
 public void testListInstances() {
 try {
 nullCloudMetadataClient.listInstances();
 } catch (UnsupportedException e) {
 assertEquals("CloudMetadata not supported", e.getMessage());
 }
 }

 @Test
 public void testListTenants() {
 try {
 nullCloudMetadataClient.listTenants("instanceId");
 } catch (UnsupportedException e) {
 assertEquals("CloudMetadata not supported", e.getMessage());
 }
 }

 @Test
 public void testGetTenant() {
 try {
 nullCloudMetadataClient.getTenant("instanceId", "tenantId");
 } catch (UnsupportedException e) {
 assertEquals("getTenant not supported in non-could", e.getMessage());
 }
 }

 @Test
 public void testListDatabaseUsers() {
 try {
 nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 } catch (UnsupportedException e) {
 assertEquals("CloudMetadata not supported", e.getMessage());
 }
 }

 @Test
 public void testGetTenantEndpoint() {
 OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 OceanBaseAccessMode accessMode = endpoint.getAccessMode();
 assertEquals(OceanBaseAccessMode.DIRECT, accessMode);
 }

 @Test
 public void testGetSysTenantUser() {
 try {
 nullCloudMetadataClient.getSysTenantUser("instanceId");
 } catch (UnsupportedException e) {
 assertEquals("CloudMetadata not supported", e.getMessage());
 }
 }
}

class CustomOutputSerializerTest {

 @Test
 public void testAddSerializer() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
 customOutputSerializer.addSerializer(Internationalizable.class, new I18nOutputSerializer());
 assertEquals(1, customOutputSerializer.getSerializers().size());
 }

}

class DefaultCookieSerializerTest {

 @Test
 public void testSetCookieName() {
 DefaultCookieSerializer serializer = new DefaultCookieSerializer();
 serializer.setCookieName("JSESSIONID");
 assertEquals("JSESSIONID", serializer.getCookieName());
 }

}