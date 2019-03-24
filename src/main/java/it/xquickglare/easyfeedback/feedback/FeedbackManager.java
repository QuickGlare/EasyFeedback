package it.xquickglare.easyfeedback.feedback;

import it.xquickglare.easyfeedback.EasyFeedback;
import it.xquickglare.easyfeedback.database.Database;
import it.xquickglare.easyfeedback.database.MySQL;
import it.xquickglare.easyfeedback.database.SQLite;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class FeedbackManager {
    
    private final EasyFeedback plugin = EasyFeedback.getInstance();
    
    private Database database;
    @Getter private List<FeedbackType> types;
    
    public FeedbackManager() {
        load();
    }
    
    public FeedbackType getType(String id) {
        for (FeedbackType type : types) {
            if(type.getId().equals(id))
                return type;
        }
        
        return null;
    }
    
    public boolean hasFeedback(UUID uuid) {
        try {
            return database.hasFeedback(uuid);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while doing an operation on database.", e);
            return false;
        }
    }
    
    public boolean addFeedback(UUID uuid, FeedbackType type) {
        try {
            database.addFeedback(uuid, type.getId());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while adding a feedback.", e);
            return false;
        }
        return true;
    }
    
    private void load() {
        switch (plugin.getConfig().getString("database.type").toUpperCase()) {
            case "MYSQL":
                database = new MySQL();
                break;
                
            case "SQLITE":
                database = new SQLite();
                break;
                
            default:
                plugin.getLogger().log(Level.SEVERE, "The database type was not recognized.");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
        }

        try {
            database.connect();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred while connecting to the database.", e);
            return;
        }
        
        types = new ArrayList<>();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("feedbackTypes");
        for (String key : section.getKeys(false))
            types.add(new FeedbackType(key, plugin.getMessage("feedbackTypes." + key)));
    }
}
