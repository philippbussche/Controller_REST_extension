package com.appdynamics.ace.extension.rest.command;

import com.appdynamics.ace.extension.rest.command.api.DataNotFoundException;
import com.appdynamics.ace.extension.rest.command.api.RestException;
import com.appdynamics.ace.extension.rest.util.BeanLocator;
import com.singularity.ee.controller.api.dto.Group;
import com.singularity.ee.controller.api.dto.User;
import com.singularity.ee.controller.api.exceptions.ServerException;
import com.singularity.ee.controller.api.services.model.IAccountManager;
import com.singularity.ee.controller.api.services.model.IGroupManager;
import com.singularity.ee.controller.api.services.model.IUserManager;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stefan.marx
 * Date: 22.10.13
 * Time: 02:07
 * To change this template use File | Settings | File Templates.
 */

@Path("aceSecurityService")
public class SecurityRestService {


    private final IUserManager _userManager;
    private final IAccountManager _accountManager;
    private final IGroupManager _groupManager;

    public SecurityRestService () {
        _userManager = BeanLocator.getInstance().getGlobalBeanInstance(IUserManager.class);
        _accountManager = BeanLocator.getInstance().getGlobalBeanInstance(IAccountManager.class);
        _groupManager = BeanLocator.getInstance().getGlobalBeanInstance(IGroupManager.class);
    }

    @GET
    @Path("user/{username}")
    @Produces(MediaType.APPLICATION_JSON)
   public User getUser( @PathParam("username") String userName) throws DataNotFoundException {

        try {
            User[] users = _userManager.getAllUsers();

            for (User user : users) {
                if (user.getProviderUniqueName().equals(userName)) return user;

            }

            throw new DataNotFoundException("User "+userName+" not found. ("+_accountManager.getCurrentAccount().getName()+")");
        } catch (ServerException e) {
            throw new DataNotFoundException("Couldn't find Userdata : "+e.getMessage());
        }

    }

    @GET
    @Path("getAllUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() throws DataNotFoundException {
        try {
            ArrayList<User> list = new ArrayList<User>();
            Collections.addAll(list,_userManager.getAllUsers());

            return list;
        } catch (ServerException e) {
            throw new DataNotFoundException("Userdata not found :"+e.getMessage());
        }
    }


    public List<User> findUSerByParent(String parentUserName) throws DataNotFoundException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @POST
    @Path ("user")
    @Consumes (MediaType.APPLICATION_JSON)
    public User createUser(User userData ) throws RestException {

        try {
            return _userManager.createUser(userData);
        } catch (ServerException e) {
            throw new RestException(e.getMessage());
        }

    }

    @PUT
    @Path ("user")
    @Consumes (MediaType.APPLICATION_JSON)
    public User updateUser(User userData) throws RestException {
        try {

            return _userManager.updateUser(userData);
        } catch (ServerException e) {
            throw new RestException(e.getMessage());
        }
    }


    @DELETE
    @Path("user/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteUser( @PathParam("username") String userName) throws RestException {

        try {
            User u = getUser(userName);
            _userManager.deleteUser(u.getId());



        } catch (ServerException e) {
            throw new RestException("Couldn't delete User ("+userName+") : "+e.getMessage());
        }

    }


    @GET
    @Path("userGroups/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> getGroupsOnUser ( @PathParam("username") String userName) throws RestException {

        try {
            User u = getUser(userName);
            Group[] groups = _userManager.getGroupsForUser(u.getId());

            ArrayList<Group> erg = new ArrayList<Group>();

            Collections.addAll(erg,groups);

            return erg;


        } catch (ServerException e) {
            throw new RestException("Couldn't delete User ("+userName+") : "+e.getMessage());
        }

    }

    @POST
    @Path ("userGroups")
    @Produces (MediaType.APPLICATION_JSON)
    @Consumes (MediaType.APPLICATION_JSON)
    public Group createGroup(Group group) {
        try {
            return _groupManager.createGroup(group);
        } catch (ServerException e) {
            throw new RestException(e.getMessage());
        }
    }

    @DELETE
    @Path("userGroups/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteGroupsOnUser ( @PathParam("username") String userName) throws RestException {

        try {
            User u = getUser(userName);
            Group[] groups = _userManager.getGroupsForUser(u.getId());

            for(Group g : groups) {
                _userManager.removeGroupMembership(u.getId(),g);
            }
        } catch (ServerException e) {
            throw new RestException("Couldn't delete User ("+userName+") : "+e.getMessage());
        }

    }

    @PUT
    @Path("userGroups/{username}/{groupname}")
    @Produces(MediaType.APPLICATION_JSON)
    public void addGroupMembership ( @PathParam("username") String userName,@PathParam("groupname") String groupName) throws RestException {

        try {
            User u = getUser(userName);

            Group[] groups = _groupManager.getAllGroups();
            for (Group g : groups) {
                System.out.println("Tries :"+g.getName() + "  WITH "+groupName);
                if (g.getName().equals(groupName)) {
                    _userManager.addGroupMembership(u.getId(),g);
                    return;
                }
            }

            throw new RestException("Couldn't find Group "+groupName);


        } catch (ServerException e) {
            throw new RestException("Couldn't delete User ("+userName+") : "+e.getMessage());
        }

    }

}
