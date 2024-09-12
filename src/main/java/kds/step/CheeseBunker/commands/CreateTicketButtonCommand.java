package kds.step.CheeseBunker.commands;

import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class CreateTicketButtonCommand implements Command {

    @Override
    public void execute(SlashCommandInteractionEvent event) {
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
