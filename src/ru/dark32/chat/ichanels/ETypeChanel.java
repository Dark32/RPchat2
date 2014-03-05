package ru.dark32.chat.ichanels;

import java.util.Locale;

import ru.dark32.chat.chanels.BaseChanel;
import ru.dark32.chat.chanels.BroadChanel;
import ru.dark32.chat.chanels.ChanceChanel;
import ru.dark32.chat.chanels.ItemChanel;
import ru.dark32.chat.chanels.PersonalMessageChanel;
import ru.dark32.chat.chanels.RangeChanel;
import ru.dark32.chat.chanels.RangeItemChanel;
import ru.dark32.chat.chanels.RangeRequisiteItemChanel;

/**
 * @author Andrew
 * 
 */

@SuppressWarnings("deprecation" )
public enum ETypeChanel {
		BASE {
			@Override
			public IChanel setChanel(final String name ) {
				return new BaseChanel(name);
			}
		},
		RANGE {
			@Override
			public IChanel setChanel(final String name ) {
				return new RangeChanel(name);
			}
		},
		ITEM {
			@Override
			public IChanel setChanel(final String name ) {
				return new ItemChanel(name);
			}
		},
		PM {
			@Override
			public IChanel setChanel(final String name ) {
				return new PersonalMessageChanel(name);
			}
		},
		RANGE_ITEM {
			@Override
			public IChanel setChanel(final String name ) {
				return new RangeItemChanel(name);
			}
		},
		REQUISITE {
			@Override
			public IChanel setChanel(final String name ) {
				return new RangeRequisiteItemChanel(name);
			}
		},
		CHANCE {
			@Override
			public IChanel setChanel(final String name ) {
				return new ChanceChanel(name);
			}
		},
		BROAD() {
			@Override
			public IChanel setChanel(final String name ) {
				return new BroadChanel(name);
			}
		},
		NONE {
			@Override
			public IChanel setChanel(final String name ) {
				return null;
			}
		};
	public static ETypeChanel get(final String type ) {
		ETypeChanel result = ETypeChanel.valueOf(type.toUpperCase(Locale.US));
		return result != null ? result : NONE;
	}
	public abstract IChanel setChanel(final String name );
}
