package ru.ange.jointbuy;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.ange.jointbuy.services.BotService;


import java.net.Authenticator;
import java.net.PasswordAuthentication;


public class App {

    private static String PROXY_HOST = "80.211.202.65";
    private static Integer PROXY_PORT = 12080;
    private static String PROXY_USER = "telegram";
    private static String PROXY_PASSWORD = "telegram";


    public static void main( String[] args ) {

        try {

            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
            BotService botService = ctx.getBean("botService", BotService.class);

            // Create the Authenticator that will return auth's parameters for proxy authentication
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
                }
            });

            ApiContextInitializer.init();

            // Create the TelegramBotsApi object to register your bots
            TelegramBotsApi botsApi = new TelegramBotsApi();

            // Set up Http proxy
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);

            //botsApi.registerBot(new JointBuyBot(botOptions, botService));
            botsApi.registerBot(new JointBuyCommandBot(botOptions, botService));
            //botsApi.registerBot(new TestBot3(botOptions, botService));




        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
