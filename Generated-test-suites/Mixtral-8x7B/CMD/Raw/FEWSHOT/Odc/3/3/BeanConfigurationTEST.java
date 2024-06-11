import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

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

public class BeanConfigurationTest {

 private SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 private CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
 String encodedPassword = passwordEncoder.encode("test");
 assertNull(encodedPassword);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer())));
 assertNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewall() {
 DefaultHttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
 defaultHttpFirewall.setAllowUrlEncodedSlash(true);
 assertNull(defaultHttpFirewall);
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
 when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
 when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
 when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
 when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
 when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);
 when(cloudMetadataClient.listInstances()).thenThrow(new UnsupportedException("CloudMetadata not supported"));
 when(cloudMetadataClient.listTenants(Objects.requireNonNull("instanceId")))
 .thenThrow(new UnsupportedException("CloudMetadata not supported"));
 when(cloudMetadataClient.getTenant(Objects.requireNonNull("instanceId"),
 Objects.requireNonNull("tenantId")))
 .thenThrow(new UnsupportedException("getTenant not supported in non-could"));
 when(cloudMetadataClient.listDatabaseUsers(Objects.requireNonNull("instanceId"),
 Objects.requireNonNull("tenantId")))
 .thenThrow(new UnsupportedException("CloudMetadata not supported"));
 when(cloudMetadataClient.getTenantEndpoint(Objects.requireNonNull("instanceId"),
 Objects.requireNonNull("tenantId")))
 .thenReturn(new OBTenantEndpoint());
 when(cloudMetadataClient.getSysTenantUser(Objects.requireNonNull("instanceId")))
 .thenThrow(new UnsupportedException("CloudMetadata not supported"));
 assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
 assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
 assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
 assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
 assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = new DefaultCookieSerializer();
 cookieSerializer.setCookieName("JSESSIONID");
 assertNull(cookieSerializer);
 }
}