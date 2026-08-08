package eu.vexiron.server;

public final class Settings {

    private boolean dataSaving = true;

    public boolean isDataSaving() {
        return dataSaving;
    }

    public void setDataSaving(boolean dataSaving) {
        this.dataSaving = dataSaving;
    }
}