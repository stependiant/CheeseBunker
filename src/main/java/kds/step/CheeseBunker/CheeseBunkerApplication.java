package kds.step.CheeseBunker;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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

		JDABuilder jdaBuilder = JDABuilder.createDefault(token)
				.enableIntents(
						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.MESSAGE_CONTENT,
						GatewayIntent.GUILD_MEMBERS
				)
				.addEventListeners(new MessageListener());

		return jdaBuilder.build();
	}
}
