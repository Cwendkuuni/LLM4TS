import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

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
 // Test basic functionality
 String rawPassword = "test123";
 String encodedPassword = passwordEncoder.encode(rawPassword);
 assertNotNull(encodedPassword);
 boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
 assertTrue(matches);
 }

 @Test
 public void testObjectMapper() throws Exception {
 // Test basic functionality
 String json = "{\"message\":\"Hello, World!\"}";
 ObjectMapper mapper = new ObjectMapper();
 Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
 assertEquals("Hello, World!", map.get("message"));

 // Test custom serializer
 @JsonSerialize(using = I18nOutputSerializer.class)
 Internationalizable i18n = new Internationalizable() {
 @Override
 public String getLocale() {
 return "en-US";
 }

 @Override
 public String getContent() {
 return "Hello, World!";
 }
 };
 String i18nJson = objectMapper.writeValueAsString(i18n);
 assertTrue(i18nJson.contains("I18nOutputSerializer"));

 // Test sensitive input handling
 String encrypted = sensitivePropertyHandler.encrypt("test");
 String decrypted = sensitivePropertyHandler.decrypt(encrypted);
 assertEquals("test", decrypted);
 }

 @Test
 public void testHttpFirewall() {
 // Test basic functionality
 assertEquals(true, httpFirewall.getAllowUrlEncodedSlash());

 // Test exception handling
 try {
 httpFirewall.setAllowUrlEncodedSlash(false);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testCloudMetadataClient() {
 // Test basic functionality and exception handling
 NullCloudMetadataClient client = (NullCloudMetadataClient) cloudMetadataClient;
 assertEquals(false, client.supportsCloudMetadata());
 assertEquals(false, client.supportsTenantInstance());
 assertEquals(false, client.needsOBTenantName());
 assertEquals(false, client.needsSysTenantUser());
 assertEquals(true, client.includeClusterNameForJdbcUsername());
 assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());

 assertThrows(UnsupportedException.class, () -> client.listInstances());
 assertThrows(UnsupportedException.class, () -> client.listTenants("instanceId"));
 assertThrows(UnsupportedException.class, () -> client.getTenant("instanceId", "tenantId"));
 assertThrows(UnsupportedException.class, () -> client.listDatabaseUsers("instanceId", "tenantId"));
 assertThrows(UnsupportedException.class, () -> client.getTenantEndpoint("instanceId", "tenantId"));
 assertThrows(UnsupportedException.class, () -> client.getSysTenantUser("instanceId"));
 }

 @Test
 public void testCookieSerializer() {
 // Test basic functionality
 CookieSerializer serializer = new DefaultCookieSerializer();
 serializer.setCookieName("JSESSIONID");
 assertEquals("JSESSIONID", serializer.getCookieName());
 }
}