package com.appdynamics.ace.extension.rest.client;

import com.singularity.ee.controller.api.dto.Group;
import com.singularity.ee.controller.api.dto.User;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stefan.marx
 * Date: 22.10.13
 * Time: 02:52
 * To change this template use File | Settings | File Templates.
 */
public class SecurityServiceClient {
    private final WebResource _restClient;

    public SecurityServiceClient(AceRestClient aceClient) {
        _restClient = aceClient.createClient().path("aceSecurityService");
    }

    public List<User> getAllUsers() throws RestClientException{

        ClientResponse response = _restClient.path("getAllUsers").accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.getEntity(new GenericType<List<User>>(){});
        }
        else throw new RestClientException(response);
    }

    public User  getUser(String username) throws RestClientException {
        ClientResponse response = _restClient.path("user")
                .path(username)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.getEntity(User.class);
        }
        else throw new RestClientException(response);

    }

    public User createUser(User src) throws RestClientException {
        ClientResponse response = _restClient.path("user")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, src);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.getEntity(User.class);
        }
        else throw new RestClientException(response);


    }

    public User updateUser(User src) throws RestClientException {
        ClientResponse response = _restClient.path("user")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .put(ClientResponse.class, src);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.getEntity(User.class);
        }
        else throw new RestClientException(response);


    }

    public void deleteUser(String username) throws RestClientException {
        ClientResponse response = _restClient.path("user")
                .path(username)
                .accept(MediaType.APPLICATION_JSON)
                .delete(ClientResponse.class);

        if (response.getStatus() >= 300 ) {

           throw new RestClientException(response);
        }
    }

    public List<Group> getGroupsForUser(String username) throws RestClientException{

        ClientResponse response = _restClient.path("userGroups")
                .path(username)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.getEntity(new GenericType<List<Group>>(){});
        }
        else throw new RestClientException(response);
    }

    public void clearGroupsForUser(String username) throws RestClientException{

        ClientResponse response = _restClient.path("userGroups")
                .path(username)
                .accept(MediaType.APPLICATION_JSON)
                .delete(ClientResponse.class);
        if (response.getStatus() >=300) {
            throw new RestClientException(response);
        }
    }


    public void assignUserToGroup(String username, String groupname) throws RestClientException {
        ClientResponse response = _restClient.path("userGroups")
                .path(username)
                .path(groupname)
                .accept(MediaType.APPLICATION_JSON)
                .put(ClientResponse.class);
        if (response.getStatus() >=300) {
            throw new RestClientException(response);
        }
    }

    public Group createGroup(Group g) throws RestClientException {
        ClientResponse response = _restClient.path("userGroups")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, g);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.getEntity(Group.class);
        }
        else throw new RestClientException(response);
    }
}
