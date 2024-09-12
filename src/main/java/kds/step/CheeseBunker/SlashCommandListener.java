package kds.step.CheeseBunker;

import kds.step.CheeseBunker.commands.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SlashCommandListener extends ListenerAdapter {

    private final Map<String, Command> commandMap = new HashMap<>();

    public SlashCommandListener() {
        // registration all commands
        commandMap.put("createticketroom", new CreateTicketButtonCommand());
        commandMap.put("items", new ItemsCommand());
        commandMap.put("buyitem", new BuyItemCommand());
        commandMap.put("additem", new AddItemCommand());
        commandMap.put("removeitem", new RemoveItemCommand());
        commandMap.put("allmoneycheck", new AllMoneyCheckCommand());
        commandMap.put("usermoneyadd", new UserMoneyAddCommand());
        commandMap.put("usermoneycheck", new UserMoneyCheckCommand());
        commandMap.put("platformmoneyadd", new PlatformMoneyAddCommand());
        commandMap.put("platformmoneyremove", new PlatformMoneyRemoveCommand());
        commandMap.put("platformmoneycheck", new PlatformMoneyCheckCommand());
        commandMap.put("setup", new SetupCommand());

    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName();

        Command command = commandMap.get(commandName);
        if (command != null) {
            command.execute(event);
        } else {
            event.reply("Unknown command!").setEphemeral(true).queue();
        }

    }

    private void closeTicket(SlashCommandInteractionEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        channel.delete().queue();
        event.reply("Ticket closed and channel deleted.").setEphemeral(true).queue();
    }

    private String generatePayPalResponse(double amount) {
        double total = amount * 1.1;
        return "# Paypal#\n" +
                "Send the exact discussed price to the Paypal email linked below!\n" +
                "( Email: wesleytjhhh@gmail.com )\n\n" +
                "**Keep in mind for Paypal you pay 10% in Fee's.**\n" +
                "**Send as F&F**\n" +
                "**No Note**\n\n" +
                "If you don't follow the steps above, your payment will not be refunded and will be denied.\n\n" +
                "Your total included fee's will be: €" + String.format("%.2f", total);
    }

    private void createTicket(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (guild != null && member != null) {
            List<Category> categories = guild.getCategoriesByName("Tickets", true);

            if (categories.isEmpty()) {
                event.reply("Error: 'Tickets' category not found on this server.").setEphemeral(true).queue();
                return;
            }

            Category ticketCategory = categories.get(0);

            event.deferReply().setEphemeral(true).queue();

            ticketCategory.createTextChannel("ticket-" + member.getEffectiveName())
                    .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue((TextChannel channel) -> {
                        channel.sendMessage("Ticket created for " + member.getAsMention()).queue();
                        event.getHook().sendMessage("Ticket created: " + channel.getAsMention()).queue();
                    });
        } else {
            event.reply("Error: Unable to create a ticket.").setEphemeral(true).queue();
        }
    }

    private void setupTicketRoom(SlashCommandInteractionEvent event) {
        Channel channel = event.getOption("channel").getAsChannel();

        if (channel instanceof TextChannel textChannel) {

            textChannel.sendMessage("Click the button below to create a new ticket:")
                    .setActionRow(Button.primary("create-ticket", "Create Ticket"))
                    .queue();

            event.reply("Ticket setup message sent to " + textChannel.getAsMention()).setEphemeral(true).queue();
        } else {
            event.reply("Please select a valid text channel.").setEphemeral(true).queue();
        }
    }

}
