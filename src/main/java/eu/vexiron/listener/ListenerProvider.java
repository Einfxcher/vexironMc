package eu.vexiron.listener;

import eu.vexiron.world.InstanceProvider;

public class ListenerProvider {
    public ListenerProvider(InstanceProvider instances) {
        new PlayerListener(instances);
    }
}