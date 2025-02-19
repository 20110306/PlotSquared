/*
 *       _____  _       _    _____                                _
 *      |  __ \| |     | |  / ____|                              | |
 *      | |__) | | ___ | |_| (___   __ _ _   _  __ _ _ __ ___  __| |
 *      |  ___/| |/ _ \| __|\___ \ / _` | | | |/ _` | '__/ _ \/ _` |
 *      | |    | | (_) | |_ ____) | (_| | |_| | (_| | | |  __/ (_| |
 *      |_|    |_|\___/ \__|_____/ \__, |\__,_|\__,_|_|  \___|\__,_|
 *                                    | |
 *                                    |_|
 *            PlotSquared plot management system for Minecraft
 *               Copyright (C) 2014 - 2022 IntellectualSites
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.plotsquared.core.plot.comment;

import com.plotsquared.core.database.DBFunc;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.Permissions;
import com.plotsquared.core.util.task.RunnableVal;

import java.util.List;

public abstract class CommentInbox {

    @Override
    public abstract String toString();

    /**
     * @param plot   the plot's inbox to read
     * @param player the player trying to read the comment
     * @return the inbox, otherwise {@code false} false
     */
    public boolean canRead(Plot plot, PlotPlayer<?> player) {
        if (Permissions.hasPermission(player, "plots.inbox.read." + this, true)) {
            return plot.isOwner(player.getUUID()) || Permissions
                    .hasPermission(player, "plots.inbox.read." + this + ".other", true);
        }
        return false;
    }

    /**
     * @param plot   the plot's inbox to write to
     * @param player the player trying to write the comment
     * @return true if the player can write a comment on the plot
     */
    public boolean canWrite(Plot plot, PlotPlayer<?> player) {
        if (plot == null) {
            return Permissions.hasPermission(player, "plots.inbox.write." + this, true);
        }
        return Permissions.hasPermission(player, "plots.inbox.write." + this, true) && (
                plot.isOwner(player.getUUID()) || Permissions
                        .hasPermission(player, "plots.inbox.write." + this + ".other", true));
    }

    /**
     * @param plot   the plot's inbox to write to
     * @param player the player trying to modify the inbox
     * @return true if the player can write a comment on the plot
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
    public boolean canModify(Plot plot, PlotPlayer<?> player) {
        if (Permissions.hasPermission(player, "plots.inbox.modify." + this, true)) {
            return plot.isOwner(player.getUUID()) || Permissions
                    .hasPermission(player, "plots.inbox.modify." + this + ".other", true);
        }
        return false;
    }

    /**
     * <br>
     * The `whenDone` parameter should be executed when it's done fetching the comments.
     * The value should be set to List of comments
     *
     * @param plot     plot
     * @param whenDone task to run when comments are obtained
     * @return success or not
     */
    public abstract boolean getComments(Plot plot, RunnableVal<List<PlotComment>> whenDone);

    /**
     * @param plot    plot
     * @param comment the comment to add
     * @return success or not
     */
    public abstract boolean addComment(Plot plot, PlotComment comment);

    /**
     * @param plot    plot
     * @param comment the comment to remove
     */
    public void removeComment(Plot plot, PlotComment comment) {
        DBFunc.removeComment(plot, comment);
    }

    /**
     * @param plot plot
     */
    public void clearInbox(Plot plot) {
        DBFunc.clearInbox(plot, toString());
    }

}
