package com.appdynamics.ace.extension.rest.command.api;

import com.singularity.ee.controller.api.dto.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stefan.marx
 * Date: 22.10.13
 * Time: 01:54
 * To change this template use File | Settings | File Templates.
 */
public interface ISecurityService {

    public User getUser ( String userName) throws DataNotFoundException;


    public List<User> getAllUsers() throws DataNotFoundException;
    public List<User> findUSerByParent( String parentUserName) throws DataNotFoundException;

    public User createUser( User userData ) throws RestException;
    public User updateUser( User userData) throws RestException;
    public void deleteUser(String username) throws RestException;

}
