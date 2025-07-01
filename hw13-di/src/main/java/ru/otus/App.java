package ru.otus;

import ru.otus.appcontainer.AppComponentsContainerImpl;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.config.AppConfig;
import ru.otus.config.AppConfig1;
import ru.otus.config.AppConfig2;
import ru.otus.services.GameProcessor;
import ru.otus.services.GameProcessorImpl;

public class App {

    public static void main(String[] args) throws Exception {
        AppComponentsContainer container1 = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);
        AppComponentsContainer container2 = new AppComponentsContainerImpl("ru.otus.config");
        AppComponentsContainer container3 = new AppComponentsContainerImpl(AppConfig.class);

        GameProcessor gameProcessor1 = container1.getAppComponent(GameProcessor.class);
        GameProcessor gameProcessor2 = container1.getAppComponent(GameProcessorImpl.class);
        GameProcessor gameProcessor3 = container1.getAppComponent("gameProcessor");

        gameProcessor1 = container2.getAppComponent(GameProcessor.class);
        gameProcessor2 = container2.getAppComponent(GameProcessorImpl.class);
        gameProcessor3 = container2.getAppComponent("gameProcessor");

        gameProcessor1 = container3.getAppComponent(GameProcessor.class);
        gameProcessor2 = container3.getAppComponent(GameProcessorImpl.class);
        gameProcessor3 = container3.getAppComponent("gameProcessor");

        gameProcessor1.startGame();
    }
}
