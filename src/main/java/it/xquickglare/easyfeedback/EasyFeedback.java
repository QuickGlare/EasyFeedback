package it.xquickglare.easyfeedback;

import it.xquickglare.easyfeedback.commands.FeedbackCommand;
import it.xquickglare.easyfeedback.feedback.FeedbackManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyFeedback extends JavaPlugin {

    @Getter private static EasyFeedback instance;
    
    @Getter private FeedbackManager feedbackManager;
    
    @Override
    public void onEnable() { 
        instance = this;
        
        saveDefaultConfig();
        registerInstances();
        
        getCommand("feedback").setExecutor(new FeedbackCommand());
    }

    private void registerInstances() {
        feedbackManager = new FeedbackManager();
    }
    
    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path));
    }
}
