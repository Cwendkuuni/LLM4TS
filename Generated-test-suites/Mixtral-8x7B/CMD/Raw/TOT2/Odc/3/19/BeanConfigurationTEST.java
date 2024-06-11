import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private PasswordEncoder passwordEncoder;

 @Autowired
 private ObjectMapper objectMapper;

 @Autowired
 private HttpFirewall httpFirewall;

 @Autowired
 private CloudMetadataClient cloudMetadataClient;

 @Autowired
 private CookieSerializer cookieSerializer;

 @Test
 public void testPasswordEncoder() {
 assertNotNull(passwordEncoder);
 String encodedPassword = passwordEncoder.encode("test123");
 assertNotNull(encodedPassword);
 }

 @Test
 public void testObjectMapper() throws Exception {
 // Test custom serializer
 Internationalizable i18n = new Internationalizable() {
 };
 String serialized = objectMapper.writeValueAsString(i18n);
 assertNotNull(serialized);

 // Test sensitive input handling
 String input = "{\"encryptedValue\":\"test_encrypted_value\"}";
 ObjectMapper jsonMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(input::toString));
 String decrypted = jsonMapper.readValue(input, String.class);
 assertEquals("test_encrypted_value", decrypted);
 }

 @Test
 public void testHttpFirewall() {
 httpFirewall.setAllowUrlEncodedSlash(true);
 assertEquals(true, httpFirewall.getAllowedUrlEncodedSlash());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
 assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testCloudMetadataClient() {
 assertThrows(UnsupportedException.class, () -> {
 cloudMetadataClient.listInstances();
 });
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer serializer = new DefaultCookieSerializer();
 serializer.setCookieName("JSESSIONID");
 assertEquals("JSESSIONID", serializer.getCookieName());
 }

 @Test
 public void testListInstances() {
 List<OBInstance> instances = new ArrayList<>();
 instances.add(new OBInstance());
 assertEquals(instances, cloudMetadataClient.listInstances());
 }

 @Test
 public void testListTenants() {
 List<OBTenant> tenants = new ArrayList<>();
 tenants.add(new OBTenant());
 assertEquals(tenants, cloudMetadataClient.listTenants("instanceId"));
 }

 @Test
 public void testGetTenant() {
 OBTenant tenant = new OBTenant();
 tenant.setTenantId("tenantId");
 assertEquals(tenant, cloudMetadataClient.getTenant("instanceId", "tenantId"));
 }

 @Test
 public void testListDatabaseUsers() {
 List<OBDatabaseUser> users = new ArrayList<>();
 users.add(new OBDatabaseUser());
 assertEquals(users, cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
 }

 @Test
 public void testGetTenantEndpoint() {
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 assertEquals(endpoint, cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
 }

 @Test
 public void testGetSysTenantUser() {
 OBDatabaseUser user = new OBDatabaseUser();
 assertEquals(user, cloudMetadataClient.getSysTenantUser("instanceId"));
 }
}