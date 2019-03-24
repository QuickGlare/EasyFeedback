package it.xquickglare.easyfeedback.database;

import it.xquickglare.easyfeedback.EasyFeedback;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SQLite extends MySQL {
    
    private final EasyFeedback plugin = EasyFeedback.getInstance();
    private final String fileName;
    
    public SQLite() {
        super();
        
        fileName = plugin.getConfig().getString("database.sqlite.name");
    }
    
    @Override
    public void connect() throws Exception {
        File file = new File(plugin.getDataFolder(), fileName);
        if(!file.exists())
            file.createNewFile();
        
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());

        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `feedbacks` (`uuid` TEXT, `feedback` TEXT);");
        statement.execute();
        statement.close();
    }
}
