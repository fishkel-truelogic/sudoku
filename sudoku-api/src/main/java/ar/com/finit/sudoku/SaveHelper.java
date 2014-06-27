package ar.com.finit.sudoku;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author leo
 */
public class SaveHelper {

	public static void saveGame(String json) {
		BufferedWriter writer = null;
        try {
            //create a temporary file
            String filename = System.getProperty( "user.home" ) + File.separator + "Mis documentos" + File.separator + "sudoku_saved_game.txt";
            File file = new File(filename);

            // This will output the full path where the file will be written to...
            System.out.println(file.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(file));
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
		
	}

	public static boolean isSaved() {
		File f = new File (System.getProperty( "user.home" ) + File.separator + "Mis documentos" + File.separator + "sudoku_saved_game.txt");
		return f.exists();
	}

	public static SavedGame getSavedGame() {
		String json = "";
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(System.getProperty( "user.home" ) + File.separator + "Mis documentos" + File.separator + "sudoku_saved_game.txt"));
 
			while ((sCurrentLine = br.readLine()) != null) {
				json += sCurrentLine;
			}
 
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		return SavedGame.parseJson(json);
	}

}
