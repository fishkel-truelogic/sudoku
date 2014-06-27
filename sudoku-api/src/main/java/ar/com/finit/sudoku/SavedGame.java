package ar.com.finit.sudoku;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author leo
 */
public class SavedGame {

	private int[][] matrix;
	private int[][] matrix2;
	private byte centiseconds = 0;
    private byte seconds = 0;
    private short minutes = 0;
	public int[][] getMatrix() {
		return matrix;
	}
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}
	public int[][] getMatrix2() {
		return matrix2;
	}
	public void setMatrix2(int[][] matrix2) {
		this.matrix2 = matrix2;
	}
	public byte getCentiseconds() {
		return centiseconds;
	}
	public void setCentiseconds(byte centiseconds) {
		this.centiseconds = centiseconds;
	}
	public byte getSeconds() {
		return seconds;
	}
	public void setSeconds(byte seconds) {
		this.seconds = seconds;
	}
	public short getMinutes() {
		return minutes;
	}
	public void setMinutes(short minutes) {
		this.minutes = minutes;
	}
    
    public static SavedGame parseJson(String json) {
    	Gson gson = new Gson();
    	Type type = new TypeToken<SavedGame>(){}.getType();
    	return gson.fromJson(json, type); 
    }
	
}
