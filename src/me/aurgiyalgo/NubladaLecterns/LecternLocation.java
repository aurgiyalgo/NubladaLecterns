package me.aurgiyalgo.NubladaLecterns;

public class LecternLocation {

    public double x, y, z;
    public float yaw, pitch;
    public String name;
    public String world;

    public LecternLocation(double x, double y, double z, float yaw, float pitch, String name, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.name = name;
        this.world = world;
    }
}
