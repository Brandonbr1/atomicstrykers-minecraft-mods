package atomicstryker.infernalmobs.common.modifiers;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Configuration;

import atomicstryker.infernalmobs.common.InfernalMobsCore;

import java.util.ArrayList;
import java.util.List;

public class MM_Lifesteal extends MobModifier {

    private static final List<Class<?>> bannedClasses = new ArrayList<>();
    private static final String[] suffix = { "theVampire", "ofTransfusion", "theBloodsucker" };
    private static final String[] prefix = { "vampiric", "transfusing", "bloodsucking" };
    private static float lifestealMultiplier;

    public MM_Lifesteal(@Nullable MobModifier next) {
        super("Lifesteal", next);
    }

    @Override
    public float onAttack(EntityLivingBase entity, DamageSource source, float damage) {
        EntityLivingBase mob = (EntityLivingBase) source.getEntity();
        if (entity != null && mob.getHealth() < getActualMaxHealth(mob)) {
            InfernalMobsCore.instance()
                .setEntityHealthPastMax(mob, mob.getHealth() + (damage * lifestealMultiplier));
        }

        return super.onAttack(entity, source, damage);
    }

    @Override
    public Class<?>[] getBlackListMobClasses() {
        return bannedClasses.toArray(new Class<?>[0]);
    }

    @Override
    protected String[] getModNameSuffix() {
        return suffix;
    }

    @Override
    protected String[] getModNamePrefix() {
        return prefix;
    }


    public static class Loader extends ModifierLoader<MM_Lifesteal> {

        public Loader() {
            super(MM_Lifesteal.class);
        }

        @Override
        public MM_Lifesteal make(@Nullable MobModifier next) {
            return new MM_Lifesteal(next);
        }

        @Override
        public void loadConfig(Configuration config) {
            lifestealMultiplier = (float) config
                .get(
                    getModifierClassName(),
                    "lifestealMultiplier",
                    1.0D,
                    "Multiplies damage dealt, result is added to mob health")
                .getDouble(1.0D);

            String[] bannedClassString = config.getStringList("Disallowed Mob Classes", getModifierClassName(), new String[]{"net.minecraft.entity.monster.EntityCreeper"}, "Fully Qualified Mob classes which can not have this effect.");
            try {
                for (int i = 0; i < bannedClassString.length; i++) {
                    Class<?> clazz = Class.forName(bannedClassString[i]);
                    bannedClasses.add(clazz);
                }
            } catch (Exception e) {

            }
        }
    }
}
