package it.xquickglare.easyfeedback.database;

import it.xquickglare.easyfeedback.EasyFeedback;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class MySQL implements Database {

    private final String hostname;
    private final String username;
    private final String password;
    private final String database;
    private final int port;
    
    protected Connection connection;
    
    public MySQL() {
        FileConfiguration config = EasyFeedback.getInstance().getConfig();
        
        hostname = config.getString("database.mysql.hostname");
        username = config.getString("database.mysql.username");
        password = config.getString("database.mysql.password");
        database = config.getString("database.mysql.database");
        port = config.getInt("database.mysql.port");
    }
    
    @Override
    public void connect() throws Exception {
        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?user=" + username;
        if(password != null && !password.isEmpty())
            url += "&password=" + password;
        
        connection = DriverManager.getConnection(url);

        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `feedbacks` (`uuid` varchar(36) NOT NULL, `feedback` varchar(50) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
        statement.execute();
        statement.close();
    }

    @Override
    public void disconnect() throws Exception {
        connection.close();
    }

    @Override
    public void addFeedback(UUID uuid, String id) throws Exception {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO feedbacks (uuid, feedback) VALUES (?, ?);");
        statement.setString(1, uuid.toString());
        statement.setString(2, id);
        
        statement.execute();
        statement.close();
    }

    @Override
    public boolean hasFeedback(UUID uuid) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM feedbacks WHERE uuid = ?;");
        statement.setString(1, uuid.toString());

        ResultSet result = statement.executeQuery();
        boolean has = result.next();
        
        result.close();
        statement.close();
        return has;
    }
}
