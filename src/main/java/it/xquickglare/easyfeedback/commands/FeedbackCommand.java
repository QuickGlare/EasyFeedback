package it.xquickglare.easyfeedback.commands;

import it.xquickglare.easyfeedback.EasyFeedback;
import it.xquickglare.easyfeedback.feedback.FeedbackType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedbackCommand implements CommandExecutor {
    
    private final EasyFeedback plugin = EasyFeedback.getInstance();
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessage("messages.onlyPlayer"));
            return true; 
        }
        
        Player player = (Player) sender;
        
        if(args.length != 0) {
            String id = args[0];
            FeedbackType type = plugin.getFeedbackManager().getType(id);
            if(type == null) {
                player.sendMessage(plugin.getMessage("messages.feedback.noExist"));
                return true;
            }
            
            if(plugin.getFeedbackManager().hasFeedback(player.getUniqueId())){
                player.sendMessage(plugin.getMessage("messages.feedback.alreadyDone"));
                return true;
            }
            
            if(plugin.getFeedbackManager().addFeedback(player.getUniqueId(), type))
                player.sendMessage(plugin.getMessage("messages.feedback.done"));
            else 
                player.sendMessage(plugin.getMessage("messages.feedback.error"));
            
            return true;
        }
        
        player.sendMessage(plugin.getMessage("messages.feedback.header"));
        for (FeedbackType type : plugin.getFeedbackManager().getTypes()) {
            TextComponent component = new TextComponent(type.getName());
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/feedback " + type.getId()));
        
            player.spigot().sendMessage(component);
        }
        return true;
    }
}
