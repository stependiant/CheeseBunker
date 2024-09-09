package kds.step.CheeseBunker;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        System.out.println("Message received: " + event.getMessage().getContentRaw());

        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();

        if (message.startsWith("!pp")) {
            try {
                String[] parts = message.split(" ");
                if (parts.length > 1) {
                    double amount = Double.parseDouble(parts[1]);
                    double total = amount * 1.1;

                    String response = "# Paypal#\n" +
                            "Send the exact discussed price to the Paypal email linked below!\n" +
                            "( Email: wesleytjhhh@gmail.com )\n\n" +
                            "**Keep in mind for Paypal you pay 10% in Fee's.**\n" +
                            "**Send as F&F**\n" +
                            "**No Note**\n\n" +
                            "If you don't follow the steps above, your payment will not be refunded and will be denied.\n\n" +
                            "Your total included fee's will be: €" + String.format("%.2f", total);
                    channel.sendMessage(response).queue();
                } else {
                    channel.sendMessage("Please provide a valid amount. Usage: !pp <amount>").queue();
                }
            } catch (NumberFormatException e) {
                channel.sendMessage("Invalid amount format. Usage: !pp <amount>").queue();
            }
        }
    }
}
