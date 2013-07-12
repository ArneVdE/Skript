/*
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * Copyright 2011, 2012 Peter Güttinger
 * 
 */

package ch.njol.skript.hooks.chat.expressions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.hooks.chat.ChatHook;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@SuppressWarnings("serial")
public class ExprPrefixSuffix extends SimplePropertyExpression<Player, String> {
	static {
		register(ExprPrefixSuffix.class, String.class, "(1¦prefix|2¦suffix)", "players");
	}
	
	private boolean prefix;
	
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		prefix = parseResult.mark == 1;
		return super.init(exprs, matchedPattern, isDelayed, parseResult);
	}
	
	@Override
	public String convert(final Player p) {
		return prefix ? ChatHook.chat.getPlayerPrefix(p) : ChatHook.chat.getPlayerSuffix(p);
	}
	
	@Override
	protected String getPropertyName() {
		return prefix ? "prefix" : "suffix";
	}
	
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
	
	@Override
	public Class<?>[] acceptChange(final ChangeMode mode) {
		if (mode == ChangeMode.SET)
			return new Class[] {String.class};
		return null;
	}
	
	@Override
	public void change(final Event e, final Object delta, final ChangeMode mode) {
		for (final Player p : getExpr().getArray(e)) {
			if (prefix)
				ChatHook.chat.setPlayerPrefix(p, (String) delta);
			else
				ChatHook.chat.setPlayerSuffix(p, (String) delta);
		}
	}
	
}