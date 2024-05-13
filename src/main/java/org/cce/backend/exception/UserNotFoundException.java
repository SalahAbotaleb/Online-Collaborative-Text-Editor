package org.cce.backend.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username){
        super("User with username + "+username+" not found");
    }
    public UserNotFoundException(){
        super("User not found");
    }
}
