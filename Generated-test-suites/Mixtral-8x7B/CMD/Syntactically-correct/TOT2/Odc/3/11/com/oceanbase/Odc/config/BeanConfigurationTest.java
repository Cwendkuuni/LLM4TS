import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
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

 @Autowired
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Test
 public void testPasswordEncoder() {
 // Test password encoding with BCryptPasswordEncoder
 String rawPassword = "test123";
 String encodedPassword = passwordEncoder.encode(rawPassword);
 assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
 }

 @Test
 public void testObjectMapper() throws Exception {
 // Test serialization and deserialization with ObjectMapper
 Internationalizable i18nObj = new Internationalizable() {
 @Override
 public String getLocale() {
 return "en_US";
 }

 @Override
 public String getContent() {
 return "Hello World!";
 }
 };

 String serializedObj = objectMapper.writeValueAsString(i18nObj);
 Internationalizable deserializedObj = objectMapper.readValue(serializedObj, Internationalizable.class);
 assertEquals(i18nObj.getLocale(), deserializedObj.getLocale());
 assertEquals(i18nObj.getContent(), deserializedObj.getContent());
 }

 @Test
 public void testHttpFirewall() {
 // Test that URL encoded slash is allowed
 String urlWithEncodedSlash = "http://example.com/test%2Ftest";
 assertTrue(httpFirewall.getAllowedHttpMethods().stream().anyMatch(urlWithEncodedSlash::startsWith));
 }

 @Test
 public void testNullCloudMetadataClient() {
 // Test NullCloudMetadataClient behavior
 assertFalse(cloudMetadataClient.supportsCloudMetadata());
 assertFalse(cloudMetadataClient.supportsTenantInstance());
 assertFalse(cloudMetadataClient.needsOBTenantName());
 assertFalse(cloudMetadataClient.needsSysTenantUser());
 assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());

 List<OBInstance> instances = cloudMetadataClient.listInstances();
 assertTrue(instances instanceof ArrayList);
 assertTrue(instances.isEmpty());

 List<OBTenant> tenants = cloudMetadataClient.listTenants("invalidInstanceId");
 assertTrue(tenants instanceof ArrayList);
 assertTrue(tenants.isEmpty());

 OBTenant tenant = cloudMetadataClient.getTenant("invalidInstanceId", "invalidTenantId");
 assertNull(tenant);

 List<OBDatabaseUser> databaseUsers = cloudMetadataClient.listDatabaseUsers("invalidInstanceId", "invalidTenantId");
 assertTrue(databaseUsers instanceof ArrayList);
 assertTrue(databaseUsers.isEmpty());

 OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint("invalidInstanceId", "invalidTenantId");
 assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());

 OBDatabaseUser sysTenantUser = cloudMetadataClient.getSysTenantUser("invalidInstanceId");
 assertNull(sysTenantUser);
 }

 @Test
 public void testCookieSerializer() {
 assertEquals("JSESSIONID", cookieSerializer.getCookieName());
 }

 @Test(expected = UnsupportedException.class)
 public void testSensitivePropertyHandler() {
 // Test that UnsupportedException is thrown when trying to decrypt
 sensitivePropertyHandler.decrypt("invalidEncryptedValue");
 }
}