package ru.ange.jointbuy;


import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.ApiContextInitializer;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.ange.jointbuy.bot.JointBuyAbilityBot;
import ru.ange.jointbuy.services.BotService;
import ru.ange.jointbuy.test.ChannelHandlers;


import java.net.Authenticator;
import java.net.PasswordAuthentication;


public class App {

    private static String PROXY_HOST = "81.2.248.110";
    private static Integer PROXY_PORT = 11080;
    private static String PROXY_USER = "soksuser";
    private static String PROXY_PASSWORD = "FV1Pg1q-N_Ev";

    public static void main( String[] args ) {

        try {

            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml" );
            BotService botService = ctx.getBean("botService", BotService.class );

            // Create the Authenticator that will return auth's parameters for proxy authentication
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication( PROXY_USER, PROXY_PASSWORD.toCharArray() );
                }
            });

            ApiContextInitializer.init();

            // Set up Http proxy
            DefaultBotOptions botOptions = ApiContext.getInstance( DefaultBotOptions.class );
            botOptions.setProxyType( DefaultBotOptions.ProxyType.SOCKS5 );
            botOptions.setProxyHost( PROXY_HOST );
            botOptions.setProxyPort( PROXY_PORT );

            // Create bot instance
            JointBuyAbilityBot jointBuyBot = new JointBuyAbilityBot( botOptions, botService );

            // Create the TelegramBotsApi object to register bot
            TelegramBotsApi botsApi = new TelegramBotsApi();
            botsApi.registerBot( jointBuyBot );


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
