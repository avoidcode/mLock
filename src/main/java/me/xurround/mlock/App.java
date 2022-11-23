package me.xurround.mlock;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.application.Application;
import me.xurround.mlock.layout.SceneManager;
import me.xurround.mlock.logic.DataManager;
import me.xurround.mlock.logic.crypto.loader.CryptoPasswordLoader;
import me.xurround.mlock.logic.prefs.BinaryPreferencesLoader;
import me.xurround.mlock.misc.enums.AppScene;
import me.xurround.mlock.settings.LocalizationManager;

import java.util.Objects;

public class App extends Application
{
    private SceneManager sceneManager;

    private DataManager dataManager;

    private LocalizationManager localizationManager;

    private static App instance;

    public App()
    {
        instance = this;
    }

    @Override
    public void start(Stage stage)
    {
        System.setProperty("prism.lcdtext", "false");
        dataManager = new DataManager(new BinaryPreferencesLoader(), new CryptoPasswordLoader());
        localizationManager = new LocalizationManager();
        localizationManager.setLanguage(getDataManager().getPreferences().getLanguage());
        sceneManager = new SceneManager(stage, 800, 500);
        if (getDataManager().getPreferences().isFirstRun())
            sceneManager.setLayout(AppScene.SPLASH);
        else
            sceneManager.setLayout(AppScene.LOGIN);
        stage.setTitle(getLocalizationManager().getLocalizedString("title_pass_manager"));
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("img/icon.png"))));
        stage.setResizable(false);
        stage.setOnCloseRequest(e ->
        {
            dataManager.savePreferences();
            dataManager.savePasswordStorage();
        });
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

    public static App getInstance()
    {
        return instance;
    }

    public DataManager getDataManager()
    {
        return dataManager;
    }

    public SceneManager getSceneManager()
    {
        return sceneManager;
    }

    public LocalizationManager getLocalizationManager()
    {
        return localizationManager;
    }
}