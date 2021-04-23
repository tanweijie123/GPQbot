package config;

import com.jagrosh.jdautilities.command.CommandClient;

import java.util.logging.Logger;

public class Settings {
    public static CommandClient botCommand;
    public static final String BOT_MOD_NAME = "NyanBot Mod";
    public static final String VERSION = "0.0.0"; //sync it with build.gradle
    public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
}
