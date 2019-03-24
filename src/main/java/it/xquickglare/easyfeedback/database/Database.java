package it.xquickglare.easyfeedback.database;

import java.util.UUID;

public interface Database {
    
    void connect() throws Exception;
    void disconnect() throws Exception;
    
    void addFeedback(UUID uuid, String id) throws Exception;
    boolean hasFeedback(UUID uuid) throws Exception;
    
}
