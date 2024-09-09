package kds.step.CheeseBunker;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;

@Component
public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (event.getName().equals("pp")) {
            int amount = event.getOption("money").getAsInt();
            String response = generatePayPalResponse(amount);
            event.reply(response).queue();
        }

        if (event.getName().equals("ticket")) {
            createTicket(event);
        }

        if (event.getName().equals("close")) {
            closeTicket(event);
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

}
