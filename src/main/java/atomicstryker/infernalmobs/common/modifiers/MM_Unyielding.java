package atomicstryker.infernalmobs.common.modifiers;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;

public class MM_Unyielding extends MobModifier {

    private static final String[] suffix = { "ofRelentlessness", "theUnYielding", "theUnstoppable" };
    private static final String[] prefix = { "relentless", "unyielding", "unstoppable" };

    public MM_Unyielding(@Nullable MobModifier next) {
        super("Unyielding", next);
    }

    @Override
    public float onHurt(EntityLivingBase mob, DamageSource source, float amount) {
        mob.getEntityAttribute(SharedMonsterAttributes.knockbackResistance)
            .setBaseValue(Double.MAX_VALUE);

        return super.onHurt(mob, source, amount);
    }

    @Override
    protected String[] getModNameSuffix() {
        return suffix;
    }

    @Override
    protected String[] getModNamePrefix() {
        return prefix;
    }

    public static class Loader extends ModifierLoader<MM_Unyielding> {

        public Loader() {
            super(MM_Unyielding.class);
        }

        @Override
        public MM_Unyielding make(@Nullable MobModifier next) {
            return new MM_Unyielding(next);
        }
    }
}
