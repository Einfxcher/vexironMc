package eu.vexiron.command.commands;

import eu.vexiron.command.Feedback;
import eu.vexiron.rank.Rank;
import eu.vexiron.rank.RankManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

public class RankCommand extends Command {

    public RankCommand(RankManager ranks) {
        super("rank");

        setCondition((sender, cmd) -> ranks.hasRank(sender, Rank.ADMIN));

        ArgumentWord playerArg = ArgumentType.Word("player");
        ArgumentEnum<Rank> rankArg = ArgumentType.Enum("rank", Rank.class)
                .setFormat(ArgumentEnum.Format.UPPER_CASED);

        addSyntax((sender, ctx) -> {
            String username = ctx.get(playerArg);
            Rank rank = ctx.get(rankArg);

            Player target = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(username);
            if (target == null) {
                Feedback.error(sender, "Player not found: " + username);
                return;
            }

            ranks.setRank(target, rank);
            Feedback.success(sender, "Set " + username + "'s rank to " + rank.name());
        }, playerArg, rankArg);
    }
}