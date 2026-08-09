package eu.vexiron.rank;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.minestom.server.scoreboard.TeamManager;

import java.util.EnumMap;
import java.util.Map;

final class RankTeams {

    private final Map<Rank, Team> teams = new EnumMap<>(Rank.class);

    RankTeams() {
        TeamManager manager = MinecraftServer.getTeamManager();
        for (Rank rank : Rank.values()) {
            String name = String.format("%03d_%s", 999 - rank.weight(), rank.name().toLowerCase());
            teams.put(rank, new TeamBuilder(name, manager)
                    .updatePrefix(rank.prefix())
                    .build());
        }
    }

    void sync(Player player) {
        teams.values().forEach(team -> player.sendPacket(team.createTeamsCreationPacket()));
    }

    void ensure(String username, Rank rank) {
        Team team = teams.get(rank);
        if (!team.getMembers().contains(username)) {
            team.addMember(username);
        }
    }

    void move(String username, Rank from, Rank to) {
        if (from == to) return;

        Team oldTeam = teams.get(from);
        if (oldTeam != null && oldTeam.getMembers().contains(username)) {
            oldTeam.removeMember(username);
        }

        Team newTeam = teams.get(to);
        if (newTeam != null && !newTeam.getMembers().contains(username)) {
            newTeam.addMember(username);
        }
    }
}