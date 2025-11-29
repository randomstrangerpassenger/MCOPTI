package com.randomstrangerpassenger.mcopt.config;

import net.neoforged.fml.config.ModConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class McoptCommonConfig {
    public static final BeeFixSettings BEE_FIX;
    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        BEE_FIX = new BeeFixSettings(builder);
        SPEC = builder.build();
    }

    public static class BeeFixSettings {
        public final ForgeConfigSpec.BooleanValue enableBeeStuckFix;
        public final ForgeConfigSpec.IntValue stuckTimeoutTicks;
        public final ForgeConfigSpec.IntValue relinkCooldownTicks;
        public final ForgeConfigSpec.IntValue failedHiveSearchPenalty;

        private BeeFixSettings(ForgeConfigSpec.Builder builder) {
            builder.comment("꿀벌 둥지 귀환 안정화를 위한 설정").push("bee_fix");

            enableBeeStuckFix = builder
                .comment("꿀벌이 둥지로 돌아가지 못하고 멈춰 있는 상황을 자동으로 복구합니다.")
                .translation("mcopt.config.bee_fix.enable")
                .define("enableBeeStuckFix", true);

            stuckTimeoutTicks = builder
                .comment("동일한 둥지를 향해 진행하지 못한 상태로 몇 틱 동안 감지될 때 문제로 간주할지 결정합니다.")
                .translation("mcopt.config.bee_fix.stuck_timeout")
                .defineInRange("stuckTimeoutTicks", 200, 40, 1200);

            relinkCooldownTicks = builder
                .comment("둥지 좌표를 잃어버린 뒤 다시 찾기 시작하기 전에 적용할 최소 쿨타임." )
                .translation("mcopt.config.bee_fix.relink_cooldown")
                .defineInRange("relinkCooldownTicks", 40, 0, 600);

            failedHiveSearchPenalty = builder
                .comment("잘못된 둥지에 매달렸을 때 탐색 재시도를 늦추기 위한 추가 페널티." )
                .translation("mcopt.config.bee_fix.search_penalty")
                .defineInRange("failedHiveSearchPenalty", 1200, 0, 2400);

            builder.pop();
        }
    }

    public static void refresh(ModConfig config) {
        if (config.getSpec() == SPEC) {
            config.save();
        }
    }
}
