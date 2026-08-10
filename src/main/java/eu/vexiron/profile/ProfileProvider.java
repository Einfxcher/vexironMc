package eu.vexiron.profile;

import eu.vexiron.database.Database;

public final class ProfileProvider {

    private final ProfileManager manager;

    public ProfileProvider(Database database) {
        ProfileRepository repository = new ProfileRepository(database);
        this.manager = new ProfileManager(repository);
        new ProfileListener(manager);
    }

    public ProfileManager manager() {
        return manager;
    }
}