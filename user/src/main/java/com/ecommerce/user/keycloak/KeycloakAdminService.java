package com.ecommerce.user.keycloak;

import com.ecommerce.user.dto.CreateUserRequest;
import com.ecommerce.user.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final KeycloakAdminProperties props;

    public String getAdminAccessToken() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", props.getClientId());
        params.add("client_secret", props.getClientSecret());
        params.add("grant_type", "client_credentials");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<>(params, headers);

        String url = props.getServerUrl()
                + "/realms/" + props.getRealm()
                + "/protocol/openid-connect/token";

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, entity, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null
                || response.getBody().get("access_token") == null) {
            throw new RuntimeException("Failed to obtain Keycloak admin token");
        }

        return response.getBody().get("access_token").toString();
    }

    public String createUser(String token, CreateUserRequest req) {
        HttpHeaders headers = baseHeaders(token);

        Map<String, Object> payload = new HashMap<>();
        payload.put("username", req.getUsername());
        payload.put("email", req.getEmail());
        payload.put("enabled", true);
        payload.put("firstName", req.getFirstName());
        payload.put("lastName", req.getLastName());

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", req.getPassword());
        credentials.put("temporary", false);

        payload.put("credentials", List.of(credentials));

        String url = adminUsersUrl();

        ResponseEntity<Void> response = restTemplate.postForEntity(
                url,
                new HttpEntity<>(payload, headers),
                Void.class
        );

        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new RuntimeException("Failed to create user in Keycloak");
        }

        URI location = response.getHeaders().getLocation();
        if (location == null) {
            throw new RuntimeException("Keycloak did not return user location");
        }

        return location.getPath()
                       .substring(location.getPath().lastIndexOf("/") + 1);
    }

    public void updateUser(String token, String keycloakUserId, UpdateUserRequest req) {
        HttpHeaders headers = baseHeaders(token);

        Map<String, Object> payload = new HashMap<>();
        // username нельзя менять
        payload.put("email", req.getEmail());
        payload.put("firstName", req.getFirstName());
        payload.put("lastName", req.getLastName());

        restTemplate.exchange(
                adminUsersUrl(keycloakUserId),
                HttpMethod.PUT,
                new HttpEntity<>(payload, headers),
                Void.class
        );
    }

    public void updatePassword(String token, String keycloakUserId, String password) {
        HttpHeaders headers = baseHeaders(token);

        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "password");
        payload.put("value", password);
        payload.put("temporary", false);

        restTemplate.exchange(
                adminUsersUrl(keycloakUserId) + "/reset-password",
                HttpMethod.PUT,
                new HttpEntity<>(payload, headers),
                Void.class
        );
    }

    public void deleteUser(String token, String keycloakUserId) {
        HttpHeaders headers = baseHeaders(token);

        restTemplate.exchange(
                adminUsersUrl(keycloakUserId),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );
    }

    private HttpHeaders baseHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    private String adminUsersUrl() {
        return props.getServerUrl()
                + "/admin/realms/"
                + props.getRealm()
                + "/users";
    }

    private String adminUsersUrl(String userId) {
        return adminUsersUrl() + "/" + userId;
    }
}
