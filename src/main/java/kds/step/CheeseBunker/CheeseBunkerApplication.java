package kds.step.CheeseBunker;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class CheeseBunkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheeseBunkerApplication.class, args);
	}

	@Bean
	public JDA jda() throws LoginException, IOException, JSONException {

		String fromConfig = new String(Files.readAllBytes(Paths.get("src/main/resources/config.json")));
		JSONObject config = new JSONObject(fromConfig);
		String token = config.getString("discordBotToken");

		JDA jda = JDABuilder.createDefault(token)
				.enableIntents(
						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.MESSAGE_CONTENT,
						GatewayIntent.GUILD_MEMBERS
				)
				.addEventListeners(new SlashCommandListener())
				.build();

		jda.updateCommands()
				.addCommands(
						Commands.slash("ticket", "Create a private room for the user and support")
								.setDescription("Creates a private room (text channel) for communication between the user and support team"),

						Commands.slash("createticketroom", "Set up a room for ticket creation")
								.addOption(OptionType.CHANNEL, "channel", "The channel where the ticket button will be sent", true)
								.setDescription("Creates a message with a button that allows users to create support rooms"),

						Commands.slash("items", "List all available items for purchase")
								.setDescription("Displays a list of all available items for purchase, including their description and payment options"),

						Commands.slash("buyitem", "Purchase an item")
								.addOption(OptionType.STRING, "item", "The name of the item to purchase", true)
								.addOption(OptionType.STRING, "platform", "The payment platform to use (PayPal, crypto, etc.)", true)
								.setDescription("Allows the user to purchase a selected item using a specified payment platform. Notifies support"),

						Commands.slash("additem", "Add a new item to the store")
								.addOption(OptionType.STRING, "name", "The name of the item", true)
								.addOption(OptionType.STRING, "description", "The description of the item", true)
								.setDescription("Allows administrators to add a new item to the store with a name and description"),

						Commands.slash("removeitem", "Remove an item from the store")
								.addOption(OptionType.STRING, "name", "The name of the item to remove", true)
								.setDescription("Allows administrators to remove an item from the store by name"),

						Commands.slash("allmoneycheck", "Check total money spent by all users")
								.setDescription("Displays the total amount of money spent by all users through the bot"),

						Commands.slash("usermoneyadd", "Add money to a user's total spending")
								.addOption(OptionType.USER, "user", "The user to add money for", true)
								.addOption(OptionType.INTEGER, "amount", "The amount to add", true)
								.setDescription("Allows an administrator to manually add an amount to a user's total spending"),

						Commands.slash("usermoneycheck", "Check a user's total spending")
								.addOption(OptionType.USER, "user", "The user to check spending for", true)
								.setDescription("Displays the total amount of money spent by a specific user"),

						Commands.slash("platformmoneyremove", "Remove money from a specific platform")
								.addOption(OptionType.STRING, "platform", "The platform to remove money from (e.g. PayPal, crypto)", true)
								.addOption(OptionType.INTEGER, "amount", "The amount to remove", true)
								.setDescription("Allows an administrator to remove a specific amount of money from a platform's balance"),

						Commands.slash("platformmoneyadd", "Add money to a platform")
								.addOption(OptionType.STRING, "platform", "The platform to add money to (e.g. PayPal, crypto)", true)
								.addOption(OptionType.INTEGER, "amount", "The amount to add", true)
								.setDescription("Increases the balance of a specified platform by a certain amount"),

						Commands.slash("platformmoneycheck", "Check the balance of a platform")
								.setDescription("Displays the current balance of all available platforms (e.g. PayPal, crypto)"),

						Commands.slash("setup", "Setup the bot")
								.addOption(OptionType.CHANNEL, "log_channel", "The channel to log transactions", true)
								.setDescription("Initial bot setup, allowing the administrator to define settings like the log channel and roles")
				).queue();

		return jda;
	}

}
