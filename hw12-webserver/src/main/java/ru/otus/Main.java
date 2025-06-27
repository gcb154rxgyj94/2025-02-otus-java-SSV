package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.helpers.DbServicesUtils;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;
import ru.otus.webserver.ClientsWebServer;
import ru.otus.webserver.ClientsWebServerWithFilterBasedSecurity;

public class Main {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        DbServicesUtils.initDBConnection();

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(DbServicesUtils.getDbServiceUser());

        ClientsWebServer usersWebServer = new ClientsWebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT, authService, DbServicesUtils.getDbServiceClient(), gson, templateProcessor);
        usersWebServer.start();
        usersWebServer.join();
    }

}
