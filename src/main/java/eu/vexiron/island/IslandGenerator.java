package eu.vexiron.island;

import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;

import java.util.concurrent.ThreadLocalRandom;

public final class IslandGenerator {

    private static final int CENTER_Y = 63;
    private static final int RADIUS = 14;
    private static final int RADIUS_SQ = RADIUS * RADIUS;
    private static final int INNER_RADIUS_SQ = (RADIUS - 1) * (RADIUS - 1);

    private IslandGenerator() {}

    public static void generate(InstanceContainer instance, IslandType type) {
        switch (type) {
            case SKY -> generateSky(instance);
            case OCEAN -> generateOcean(instance);
            case NETHER -> generateNether(instance);
            case STONE -> generateStone(instance);
        }
        placeCenterBedrock(instance);
        placeOakTree(instance);
    }

    // ─── Sky ─────────────────────────────────────────────

    private static void generateSky(InstanceContainer instance) {
        fillDisc(instance, CENTER_Y, Block.GRASS_BLOCK);
        fillDisc(instance, CENTER_Y - 1, Block.DIRT);
        fillDisc(instance, CENTER_Y - 2, Block.DIRT);
        fillDisc(instance, CENTER_Y - 3, Block.STONE);
    }

    // ─── Ocean ───────────────────────────────────────────

    private static void generateOcean(InstanceContainer instance) {
        instance.setGenerator(unit -> {
            unit.modifier().fillHeight(-64, -63, Block.BEDROCK);
            unit.modifier().fillHeight(-63, 320, Block.WATER);
        });

        buildGlassDome(instance);
        fillDisc(instance, CENTER_Y, Block.SAND);
        fillDisc(instance, CENTER_Y - 1, Block.SAND);
        fillDisc(instance, CENTER_Y - 2, Block.SAND);
        fillDisc(instance, CENTER_Y - 3, Block.SANDSTONE);
    }

    // ─── Nether ──────────────────────────────────────────

    private static void generateNether(InstanceContainer instance) {
        instance.setGenerator(unit ->
                unit.modifier().fillHeight(0, 64, Block.LAVA)
        );

        fillDisc(instance, CENTER_Y, Block.NETHERRACK);
        fillDisc(instance, CENTER_Y - 1, Block.NETHERRACK);
        fillDisc(instance, CENTER_Y - 2, Block.NETHERRACK);
        fillDisc(instance, CENTER_Y - 3, Block.BLACKSTONE);
    }

    // ─── Stone ───────────────────────────────────────────

    private static void generateStone(InstanceContainer instance) {
        instance.setGenerator(unit -> {
            unit.modifier().fillHeight(-64, -63, Block.BEDROCK);
            unit.modifier().fillHeight(-63, 319, Block.STONE);
            unit.modifier().fillHeight(319, 320, Block.BEDROCK);
        });

        carveDome(instance);
        fillDisc(instance, CENTER_Y, Block.STONE);
    }

    // ─── Helpers ─────────────────────────────────────────

    private static void fillDisc(InstanceContainer instance, int y, Block block) {
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int z = -RADIUS; z <= RADIUS; z++) {
                if (x * x + z * z > RADIUS_SQ) continue;
                instance.setBlock(x, y, z, block);
            }
        }
    }

    private static void buildGlassDome(InstanceContainer instance) {
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = 0; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    int dist = x * x + y * y + z * z;
                    if (dist > RADIUS_SQ) continue;
                    Block block = (dist >= INNER_RADIUS_SQ) ? Block.GLASS : Block.AIR;
                    instance.setBlock(x, CENTER_Y + y, z, block);
                }
            }
        }
    }

    private static void carveDome(InstanceContainer instance) {
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = 0; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    if (x * x + y * y + z * z > RADIUS_SQ) continue;
                    instance.setBlock(x, CENTER_Y + y, z, Block.AIR);
                }
            }
        }
    }

    private static void placeCenterBedrock(InstanceContainer instance) {
        instance.setBlock(0, CENTER_Y, 0, Block.BEDROCK);
    }

    private static void placeOakTree(InstanceContainer instance) {
        final int x = 0, y = 64, z = 5;
        final ThreadLocalRandom random = ThreadLocalRandom.current();

        instance.setBlock(x, y - 1, z, Block.DIRT);

        for (int i = 0; i < 6; i++) {
            instance.setBlock(x, y + i, z, Block.OAK_LOG);
        }

        for (int ly = 3; ly <= 4; ly++) {
            for (int lx = -2; lx <= 2; lx++) {
                for (int lz = -2; lz <= 2; lz++) {
                    if (lx == 0 && lz == 0) continue;
                    if (Math.abs(lx) == 2 && Math.abs(lz) == 2 && random.nextBoolean()) continue;
                    instance.setBlock(x + lx, y + ly, z + lz, Block.OAK_LEAVES);
                }
            }
        }

        for (int lx = -1; lx <= 1; lx++) {
            for (int lz = -1; lz <= 1; lz++) {
                if (lx == 0 && lz == 0) continue;
                instance.setBlock(x + lx, y + 5, z + lz, Block.OAK_LEAVES);
            }
        }

        instance.setBlock(x, y + 6, z, Block.OAK_LEAVES);
        instance.setBlock(x + 1, y + 6, z, Block.OAK_LEAVES);
        instance.setBlock(x - 1, y + 6, z, Block.OAK_LEAVES);
        instance.setBlock(x, y + 6, z + 1, Block.OAK_LEAVES);
        instance.setBlock(x, y + 6, z - 1, Block.OAK_LEAVES);
    }
}