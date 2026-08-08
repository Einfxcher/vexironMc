package eu.vexiron.world;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class HubInstance {

    private final InstanceContainer instance;

    public HubInstance() {
        this.instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.instance.setChunkSupplier(LightingChunk::new);
        this.instance.setGenerator(unit -> {
            unit.modifier().fillHeight(63, 64, Block.GRAY_STAINED_GLASS);
        });

        List<CompletableFuture<Chunk>> futures = new ArrayList<>();
        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                futures.add(instance.loadChunk(x, z));
            }
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> instance.setBlock(0, 63, 0, Block.BEDROCK));
    }

    public InstanceContainer get() {
        return instance;
    }
}