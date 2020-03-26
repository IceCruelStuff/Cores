package nycuro.database.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileSkyblock {

    public String name;
    public int job;
    public int kills;
    public int deaths;
    public long cooldown;
    public double experience;
    public int level;
    public double necesary;
    public long time;
    public double dollars;

    public ProfileSkyblock(String name, int job, int kills, int deaths, long cooldown, double experience, int level, double necesary, long time, double dollars) {
        this.name = name;
        this.job = job;
        this.kills = kills;
        this.deaths = deaths;
        this.cooldown = cooldown;
        this.experience = experience;
        this.level = level;
        this.necesary = necesary;
        this.time = time;
        this.dollars = dollars;
    }
}
