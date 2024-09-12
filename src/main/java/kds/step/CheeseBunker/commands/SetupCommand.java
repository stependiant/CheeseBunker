package kds.step.CheeseBunker.commands;

import kds.step.CheeseBunker.dataBase.BotSettingsRepository;
import kds.step.CheeseBunker.dataBase.BotSettings;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupCommand implements Command{

    @Autowired
    private BotSettingsRepository botSettingsRepository;

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // Проверяем, является ли пользователь администратором
        if (!event.getMember().hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR)) {
            event.reply("You don't have permission to execute this command!").setEphemeral(true).queue();
            return;
        }

        // Получаем выбранный канал и роль из опций команды
        TextChannel selectedLogChannel = event.getOption("log_channel").getAsChannel().asTextChannel();
        Role selectedSupportRole = event.getOption("support_role").getAsRole();

        // Сохраняем лог-канал и роль администратора в базе данных
        BotSettings settings = new BotSettings();
        settings.setLogChannelId(selectedLogChannel.getId());
        settings.setSupportRoleId(selectedSupportRole.getId());
        botSettingsRepository.save(settings);

        // Подтверждаем успешную установку настроек
        event.reply("Log channel set to: " + selectedLogChannel.getAsMention() + "\nSupport role set to: " + selectedSupportRole.getAsMention()).queue();
    }
}
