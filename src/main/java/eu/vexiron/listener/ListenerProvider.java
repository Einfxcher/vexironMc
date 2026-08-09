package eu.vexiron.listener;

import eu.vexiron.rank.RankProvider;
import eu.vexiron.world.InstanceProvider;

public class ListenerProvider {

    public ListenerProvider(InstanceProvider instances, RankProvider ranks) {
        new PlayerListener(instances);
        new ChatListener(ranks.manager());
    }
}