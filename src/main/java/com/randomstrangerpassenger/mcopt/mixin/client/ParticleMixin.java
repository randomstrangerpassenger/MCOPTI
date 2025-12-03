package com.randomstrangerpassenger.mcopt.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
<<<<<<< HEAD
import org.spongepowered.asm.mixin.Shadow;
=======
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
import org.spongepowered.asm.mixin.Unique;

@Mixin(Particle.class)
public abstract class ParticleMixin implements MCOptCulledParticle {
<<<<<<< HEAD
    @Shadow
    protected double x;
    @Shadow
    protected double y;
    @Shadow
    protected double z;

    @Unique
    private boolean mcopt$culled = false;

    @Override
    public double mcopt$distanceSquared(Vec3 camera) {
        double dx = this.x - camera.x;
        double dy = this.y - camera.y;
        double dz = this.z - camera.z;
=======
    @Unique private boolean mcopt$culled = false;

    @Override
    public double mcopt$distanceSquared(Vec3 camera) {
        Particle self = (Particle) (Object) this;
        double dx = self.getX() - camera.x;
        double dy = self.getY() - camera.y;
        double dz = self.getZ() - camera.z;
>>>>>>> 1da28dde83262df0df1d55168e914749d22a9de0
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public void mcopt$setCulled(boolean culled) {
        this.mcopt$culled = culled;
    }

    @Override
    public boolean mcopt$isCulled() {
        return mcopt$culled;
    }
}
