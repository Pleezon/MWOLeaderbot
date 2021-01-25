package utils;

import org.json.JSONObject;

/**
 * @author Pleezon & B4CKF1SH
 */
public class MatchPlayer {

    private final String username;
    private final boolean isSpectator;
    private String team = null;
    private final int healthPercentage;
    private final int kills;
    private final int killsMostDamage;
    private final int assists;
    private final int componentsDestroyed;
    private final int matchScore;
    private final int damage;
    private final int teamDamage;

    protected MatchPlayer(JSONObject json) {
        username = json.getString("Username");
        isSpectator = json.getBoolean("IsSpectator");

        if (!isSpectator)
            team = json.getString("Team").replace("2", "B").replace("1", "A");

        healthPercentage = json.getInt("HealthPercentage");
        kills = json.getInt("Kills");
        killsMostDamage = json.getInt("KillsMostDamage");
        assists = json.getInt("Assists");
        componentsDestroyed = json.getInt("ComponentsDestroyed");
        matchScore = json.getInt("MatchScore");
        damage = json.getInt("Damage");
        teamDamage = json.getInt("TeamDamage");
    }

    public String getUsername() {
        return username;
    }

    public boolean isSpectator() {
        return isSpectator;
    }

    public String getTeam() {
        return team;
    }

    public int getHealthPercentage() {
        return healthPercentage;
    }

    public int getKills() {
        return kills;
    }

    public int getKillsMostDamage() {
        return killsMostDamage;
    }

    public int getAssists() {
        return assists;
    }

    public int getComponentsDestroyed() {
        return componentsDestroyed;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public int getDamage() {
        return damage;
    }

    public int getTeamDamage() {
        return teamDamage;
    }
}
