/*
 * Copyright 2018 Bradley Steele
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.bradleysteele.lobby.sidebar;

import com.google.common.collect.Lists;
import me.bradleysteele.commons.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Bradley Steele
 */
public class PlayerSidebar {

    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();

    private final Player player;

    private String title;
    private final List<SidebarLine> lines = Lists.newArrayList();

    private final Scoreboard scoreboard;
    private final Objective objective;

    public PlayerSidebar(Player player, String title) {
        this.player = player;
        this.title = title;

        if (player.getScoreboard().equals(manager.getMainScoreboard())) {
            scoreboard = manager.getNewScoreboard();
        } else {
            scoreboard = player.getScoreboard();
        }

        objective = scoreboard.registerNewObjective("blobby_objective", "");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        setTitle(title);
    }

    /**
     * Updates each line of the player's sidebar.
     */
    public void update() {
        for (int i = 0; i < getLines().size(); i++) {
            SidebarLine line = getLinesReversed().get(i);
            String id = getKey(i);

            Team team = getTeam(id);
            team.setPrefix(line.getPrefix());
            team.setSuffix(line.getSuffix());

            objective.getScore(id).setScore(i);
        }

        player.setScoreboard(scoreboard);
    }

    /**
     * Clears the lines, does not update.
     */
    public void clear() {
        lines.clear();
    }

    /**
     * @return the sidebar title.
     */
    public String getTitle() {
        return Messages.colour(title);
    }

    /**
     * @return unmodifiable list of sidebar lines being displayed.
     */
    public List<SidebarLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    /**
     * @return reversed, unmodifiable list of sidebar lines
     *         being displayed.
     */
    public List<SidebarLine> getLinesReversed() {
        return Collections.unmodifiableList(Lists.reverse(lines));
    }

    private Team getTeam(String id) {
        Team team = scoreboard.getTeam(id);

        if (team == null) {
            team = scoreboard.registerNewTeam(id);
            team.addEntry(id);
        }

        return team;
    }

    /**
     * As chat colours are alphabetical and 'hidden',
     * they are used to order the sidebar.
     *
     * @param index line index.
     * @return key mapped to the line.
     */
    private String getKey(int index) {
        return ChatColor.values()[index].toString() + ChatColor.RESET;
    }

    /**
     * @param title the sidebar's title.
     */
    public void setTitle(String title) {
        objective.setDisplayName(Messages.colour(title));
    }

    /**
     * @param lines the lines to display.
     */
    public void setLines(List<SidebarLine> lines) {
        this.lines.clear();
        this.lines.addAll(lines);
    }

    /**
     * @param line the line to append.
     */
    public void addLine(SidebarLine line) {
        lines.add(line);
    }

    /**
     * @param line the line to append.
     */
    public void removeLine(SidebarLine line) {
        lines.remove(line);
    }
}