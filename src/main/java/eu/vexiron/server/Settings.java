package eu.vexiron.server;

public final class Settings {

    private static boolean dataSaving = true;

    Settings() {}

    public static boolean isDataSaving() {
        return dataSaving;
    }

    public static void setDataSaving(boolean enabled) {
        dataSaving = enabled;
    }
}