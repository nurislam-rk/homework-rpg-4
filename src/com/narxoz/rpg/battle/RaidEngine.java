package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {

    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(
            CombatNode teamA,
            CombatNode teamB,
            Skill teamASkill,
            Skill teamBSkill) {

        if (teamA == null || teamB == null)
            throw new IllegalArgumentException("Teams cannot be null");

        if (teamASkill == null || teamBSkill == null)
            throw new IllegalArgumentException("Skills cannot be null");

        RaidResult result = new RaidResult();

        int rounds = 0;
        int maxRounds = 50;

        result.addLine("Raid begins!");
        result.addLine(teamA.getName() + " vs " + teamB.getName());

        while (teamA.isAlive() && teamB.isAlive() && rounds < maxRounds) {

            rounds++;
            result.addLine("\n--- Round " + rounds + " ---");

            if (teamA.isAlive()) {

                boolean crit = random.nextInt(100) < 10;

                result.addLine(teamA.getName() + " casts " +
                        teamASkill.getSkillName() +
                        " (" + teamASkill.getEffectName() + ")" +
                        (crit ? " CRITICAL!" : ""));

                teamASkill.cast(teamB);

                result.addLine(teamB.getName() +
                        " HP remaining: " + teamB.getHealth());
            }

            if (teamB.isAlive()) {

                boolean crit = random.nextInt(100) < 10;

                result.addLine(teamB.getName() + " casts " +
                        teamBSkill.getSkillName() +
                        " (" + teamBSkill.getEffectName() + ")" +
                        (crit ? " CRITICAL!" : ""));

                teamBSkill.cast(teamA);

                result.addLine(teamA.getName() +
                        " HP remaining: " + teamA.getHealth());
            }
        }

        result.setRounds(rounds);

        if (teamA.isAlive() && !teamB.isAlive())
            result.setWinner(teamA.getName());
        else if (teamB.isAlive() && !teamA.isAlive())
            result.setWinner(teamB.getName());
        else
            result.setWinner("Draw");

        result.addLine("\nBattle finished!");
        result.addLine("Winner: " + result.getWinner());

        return result;
    }
}