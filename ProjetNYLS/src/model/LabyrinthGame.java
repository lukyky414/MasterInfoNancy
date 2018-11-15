package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dao_Txt.MapTxtDAO;
import engine.Cmd;
import engine.Game;
import exceptions.CorruptDataException;
import model.entity.*;
import model.plateau.Map;
import model.plateau.Square;

/**
 * @author Horatiu Cirstea, Vincent Thomas
 *
 *         Version avec personnage qui peut se deplacer. A completer dans les
 *         versions suivantes.
 * 
 */
public class LabyrinthGame implements engine.Game {
	private static int nbLevel = 3;
	private int level =0 ;
	private GameState state;

	/**
	 * constructeur avec fichier source pour le help
	 * 
	 */

	private Map map;

	private Hero hero;
	/*private Goblin goblin;
	private Ghost ghost;*/
	private ArrayList<Movable> entites;

	public LabyrinthGame(String source) throws CorruptDataException {
		BufferedReader helpReader;
		try {
			helpReader = new BufferedReader(new FileReader(source));
			String ligne;
			while ((ligne = helpReader.readLine()) != null) {
				System.out.println(ligne);
			}
			helpReader.close();
		} catch (IOException e) {
			System.out.println("Help not available");
		}

		map = MapTxtDAO.getInstance().load(level);
		//System.out.println(map.toString());

		state = GameState.RUN;
		loadEntity();
	}

	private void loadEntity(){
		entites = new ArrayList<Movable>();
		this.hero = null;
		Entity ent;
		for(Square sq : map){
			ent = sq.getEntity();
			if(ent != null){
				entites.add((Movable) ent);
				if(ent instanceof Hero)
					this.hero = (Hero) ent;
			}
		}
	}

	public Map getMap(){
		return map;
	}
	
	/**
	 * load the next level, if there is none, reload the current one
	 * @throws CorruptDataException
	 */
	
	public void loadNextLevel() throws CorruptDataException{
		if(level<nbLevel){
			level++;
		}
		map = MapTxtDAO.getInstance().load(level);
		loadEntity();
	}

	@Override
	public Hero getHero(){
		return hero;
	}

	/**
	 * faire evoluer le jeu avec la commande actuelle.
	 * Est executee toutes les 50ms
	 * 
	 * @param cmd
	 */
	@Override
	public void evolve(Cmd cmd) {
		if(state==GameState.RUN){
			for(Movable e : entites){
				e.evolve(cmd);
				e.move();
			}
		}
		
	}

	/**
	 * verifier si le jeu est fini
	 */
	@Override
	public boolean isFinished() {
		return map.isFinished();
	}
	
	public ArrayList<Movable> getEntities(){
		return entites;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}
}
