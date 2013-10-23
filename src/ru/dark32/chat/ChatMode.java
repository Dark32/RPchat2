package ru.dark32.chat;

public enum ChatMode {

		GLOBAL(0, "$", 'g' ),
		WORLD(1, ">", 'w' ),
		SHOUT(2, "!", 's' ),
		LOCAL(3, "-", 'l' ),
		WHISPER(4, "#", 'v' ),
		PM(5, "@", 'p' ),
		CHANCE(6, "*", 'c' ),
		BROADCAST(7, "~", 'b' );
	final private int		id;
	final private String	ch;
	final private char		sign;

	private ChatMode(int i, String s, char a ){
		id = i;
		ch = s;
		sign = a;
	}

	public int getModeId() {
		return id;
	}

	public String getFirstLetter() {
		return ch;
	}

	public char getFirstChar() {
		return ch.charAt(0);
	}

	public char getSign() {
		return sign;
	}

	public String getSignS() {
		return String.valueOf(sign);
	}
}
