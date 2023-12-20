package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.config.KeycloakProperties;
import com.cydeo.service.KeycloakService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.Arrays.asList;
import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;

@Service
public class KeycloakServiceImpl implements KeycloakService {

    private final KeycloakProperties keycloakProperties;

    public KeycloakServiceImpl(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    @Override
    public Response userCreate(UserDTO userDTO) {

        // filling user info in keycloak application

        // setting password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credential.setValue(userDTO.getPassWord());


        // setting other field
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(userDTO.getUserName());
        keycloakUser.setFirstName(userDTO.getFirstName());
        keycloakUser.setLastName(userDTO.getLastName());
        keycloakUser.setEmail(userDTO.getUserName());
        keycloakUser.setCredentials(asList(credential));
        keycloakUser.setEmailVerified(true);
        keycloakUser.setEnabled(true);


        // open instance
        Keycloak keycloak = getKeycloakInstance();

        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        // Create Keycloak user
        Response result = usersResource.create(keycloakUser);

        String userId = getCreatedId(result);
        ClientRepresentation appClient = realmResource.clients()
                .findByClientId(keycloakProperties.getClientId()).get(0);

        // getting the role from user, matching that role in keycloak and assigning it to user
        RoleRepresentation userClientRole = realmResource.clients().get(appClient.getId()) //
                .roles().get(userDTO.getRole().getDescription()).toRepresentation();

        realmResource.users().get(userId).roles().clientLevel(appClient.getId())
                .add(List.of(userClientRole));


        // close the instance
        keycloak.close();
        return result;
    }

    @Override
    public void delete(String userName) {

        // open an instance
        Keycloak keycloak = getKeycloakInstance();

        // go to realm
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        // get all users
        UsersResource usersResource = realmResource.users();

        // find the user using username
        List<UserRepresentation> userRepresentations = usersResource.search(userName);

        // when user is found, get its id
        String uid = userRepresentations.get(0).getId();

        // delete user
        usersResource.delete(uid);

        // close the instance
        keycloak.close();
    }

    // opening an instance
    private Keycloak getKeycloakInstance(){
        // passing keycloak value from application.properties file
        return Keycloak.getInstance(keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getMasterRealm(), keycloakProperties.getMasterUser()
                , keycloakProperties.getMasterUserPswd(), keycloakProperties.getMasterClient());
    }
}
