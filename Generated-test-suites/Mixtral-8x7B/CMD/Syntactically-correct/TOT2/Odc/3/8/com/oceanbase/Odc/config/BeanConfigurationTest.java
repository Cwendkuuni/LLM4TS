import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
@ActiveProfiles("test")
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
 assertNotNull(passwordEncoder);
 assertTrue(passwordEncoder.encode("test").length() > 0);
 }

 @Test
 public void testObjectMapper() {
 assertNotNull(objectMapper);
 // Add custom serialization test
 Internationalizable i18n = new Internationalizable() {
 @Override
 public String getLocale() {
 return "en_US";
 }

 @Override
 public String getContent() {
 return "Hello World";
 }
 };
 Object value = objectMapper.valueToTree(i18n);
 assertEquals("{\"class\":\"com.oceanbase.odc.common.i18n.Internationalizable\",\"locale\":\"en_US\",\"content\":\"Hello World\"}",
 value.toString());
 }

 @Test
 public void testHttpFirewall() {
 HttpFirewall firewall = new DefaultHttpFirewall();
 firewall.setAllowUrlEncodedSlash(true);
 String urlWithSlash = "/test/%25";
 assertTrue(firewall.getAllowedHttpMethods(urlWithSlash).size() > 0);
 }

 @Test
 public void testCloudMetadataClient() {
 assertNotNull(cloudMetadataClient);
 assertFalse(cloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer serializer = new DefaultCookieSerializer();
 serializer.setCookieName("JSESSIONID");
 assertEquals("JSESSIONID", serializer.getCookieName());
 }

 @Test
 public void testSensitivePropertyHandler() {
 assertNotNull(sensitivePropertyHandler);
 // Add custom decryption test
 String encryptedValue = sensitivePropertyHandler.encrypt("test");
 assertEquals("test", sensitivePropertyHandler.decrypt(encryptedValue));
 }

}