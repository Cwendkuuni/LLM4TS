import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
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

 // Test for passwordEncoder() method
 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 assertEquals(true, passwordEncoder instanceof BCryptPasswordEncoder);
 }

 // Test for objectMapper() method with valid sensitivePropertyHandler
 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 assertEquals(customOutputSerializer, objectMapper.getSerializerProvider().getNullValueSerializer());
 }

 // Test for objectMapper() method with null sensitivePropertyHandler
 @Test
 public void testObjectMapperWithNullSensitivePropertyHandler() {
 assertThrows(NullPointerException.class, () -> beanConfiguration.objectMapper(null));
 }

 // Test for defaultHttpFirewall() method
 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 assertEquals(true, defaultHttpFirewall instanceof DefaultHttpFirewall);
 ((DefaultHttpFirewall) defaultHttpFirewall).setAllowUrlEncodedSlash(true);
 assertEquals(true, ((DefaultHttpFirewall) defaultHttpFirewall).getAllowUrlEncodedSlash());
 }

 // Test for cloudMetadataClient() method
 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 assertEquals(true, cloudMetadataClient instanceof NullCloudMetadataClient);
 }

 // Test for cookieSerializer() method
 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 assertEquals("JSESSIONID", cookieSerializer.getCookieName());
 }

}

// Test for NullCloudMetadataClient class
public class NullCloudMetadataClientTest {

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
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
 }

 @Test
 public void testListTenants() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants(anyString()));
 }

 @Test
 public void testGetTenant() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant(anyString(), anyString()));
 }

 @Test
 public void testListDatabaseUsers() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers(anyString(), anyString()));
 }

 @Test
 public void testGetTenantEndpoint() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint(anyString(), anyString());
 assertNotNull(endpoint);
 assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
 }

 @Test
 public void testGetSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser(anyString()));
 }

}