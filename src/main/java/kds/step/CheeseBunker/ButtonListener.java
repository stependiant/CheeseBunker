package kds.step.CheeseBunker;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;

@Component
public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        // Проверяем, что кнопка - это "create-ticket"
        if (event.getComponentId().equals("create-ticket")) {
            createTicket(event);
        }
    }

    private void createTicket(ButtonInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (guild != null && member != null) {
            List<Category> categories = guild.getCategoriesByName("Tickets", true);

            if (categories.isEmpty()) {
                event.reply("Error: 'Tickets' category not found on this server.").setEphemeral(true).queue();
                return;
            }

            Category ticketCategory = categories.get(0);

            ticketCategory.createTextChannel("ticket-" + member.getEffectiveName())
                    .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue((TextChannel channel) -> {
                        channel.sendMessage("Ticket created for " + member.getAsMention()).queue();
                        event.reply("Ticket created: " + channel.getAsMention()).setEphemeral(true).queue();

                        System.out.println("Ticket channel created: " + channel.getName() + " (ID: " + channel.getId() + ")");
                    });
        } else {
            event.reply("Error: Unable to create a ticket.").setEphemeral(true).queue();
        }
    }

}
