package controller;

import model.Color;

public class GameConstants {
	public static final int USER_MODE = 0;
	public static final int SERVER_MODE = 1;
	public static Color THUNK_COLOR = Color.WHITE;
	public static Color USER_COLOR = Color.BLACK;
	public static Color SERVER_COLOR = Color.BLACK;
	public static final int MAX_PASSIVE_MOVES = 50;
	public static final int MAX_SEARCH_DEPTH = 6;
	public static final int BOARD_SIZE = 8;
	
	/* Параметри евристики */
	public static final boolean APEX = true;
	public static final boolean BACK = true;
	public static final boolean CENT = true;
	public static final boolean KCENT = true;
	public static final boolean MOB = true;
	public static final boolean POLE = true;
	public static final boolean PIECE_DIFFERENTIAL = true;
	
	/* "Цінність" клітинок на дошці */
	public static final int APEX_WEIGHT = 3;
	public static final int BACK_WEIGHT = 4;
	public static final int CENT_WEIGHT = 2;
	public static final int KCENT_WEIGHT = 2;
	public static final int MOB_WEIGHT = 1;
	public static final int POLE_WEIGHT = 3;
	public static final int PIECE_DIFFERENTIAL_WEIGHT = 4;
	
}
