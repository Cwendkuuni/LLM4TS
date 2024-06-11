import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
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
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;
 private SensitivePropertyHandler sensitivePropertyHandler;
 private ObjectMapper objectMapper;
 private CloudMetadataClient cloudMetadataClient;

 @Before
 public void setup() {
 beanConfiguration = new BeanConfiguration();
 sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 ReflectionTestUtils.setField(beanConfiguration, "sensitivePropertyHandler", sensitivePropertyHandler);
 objectMapper = mock(ObjectMapper.class);
 ReflectionTestUtils.setField(beanConfiguration, "objectMapper", objectMapper);
 cloudMetadataClient = mock(CloudMetadataClient.class);
 ReflectionTestUtils.setField(beanConfiguration, "cloudMetadataClient", cloudMetadataClient);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper mapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(mapper);
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 assertEquals(customOutputSerializer, ReflectionTestUtils.getField(mapper, "customOutputSerializer"));
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(httpFirewall);
 assertTrue(httpFirewall instanceof DefaultHttpFirewall);
 DefaultHttpFirewall defaultHttpFirewall = (DefaultHttpFirewall) httpFirewall;
 assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
 assertNotNull(client);
 assertTrue(client instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer serializer = beanConfiguration.cookieSerializer();
 assertNotNull(serializer);
 assertTrue(serializer instanceof DefaultCookieSerializer);
 DefaultCookieSerializer defaultCookieSerializer = (DefaultCookieSerializer) serializer;
 assertEquals("JSESSIONID", defaultCookieSerializer.getCookieName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 assertNotNull(client);
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
 assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
 }

 @Test
 public void testNullCloudMetadataClientListInstances() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 try {
 client.listInstances();
 fail("Expected UnsupportedException to be thrown");
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testNullCloudMetadataClientListTenants() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 try {
 client.listTenants("instanceId");
 fail("Expected UnsupportedException to be thrown");
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testNullCloudMetadataClientGetTenant() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 try {
 client.getTenant("instanceId", "tenantId");
 fail("Expected UnsupportedException to be thrown");
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Test
 public void testNullCloudMetadataClientListDatabaseUsers() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 try {
 client.listDatabaseUsers("instanceId", "tenantId");
 fail("Expected UnsupportedException to be thrown");
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
 fail("Expected UnsupportedException to be thrown");
 } catch (UnsupportedException e) {
 // expected
 }
 }

 @Configuration
 @ConditionalOnMissingBean
 static class TestBeanConfiguration {

 @Bean
 public SensitivePropertyHandler sensitivePropertyHandler() {
 return mock(SensitivePropertyHandler.class);
 }

 @Bean
 public CloudMetadataClient cloudMetadataClient() {
 return mock(CloudMetadataClient.class);
 }

 @Bean
 public ObjectMapper objectMapper(SensitivePropertyHandler sensitivePropertyHandler) {
 return mock(ObjectMapper.class);
 }

 @Bean
 public BCryptPasswordEncoder passwordEncoder() {
 return new BCryptPasswordEncoder();
 }

 @Bean
 public HttpFirewall defaultHttpFirewall() {
 return mock(DefaultHttpFirewall.class);
 }

 @Bean
 @Primary
 public DefaultCookieSerializer cookieSerializer() {
 return new DefaultCookieSerializer();
 }
 }

 @Test
 public void testNullCloudMetadataClientInit() {
 NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
 client.init();
 }
}