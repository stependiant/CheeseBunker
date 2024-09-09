package kds.step.CheeseBunker;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

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
            event.reply("Create ticket...").queue();
        }

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
}
