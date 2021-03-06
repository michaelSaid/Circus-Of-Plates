package eg.edu.alexu.csd.oop.game.cs01.ObjectPool;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import eg.edu.alexu.csd.oop.game.GameObject;
import eg.edu.alexu.csd.oop.game.cs01.Difficulty.GameDifficulty;
import eg.edu.alexu.csd.oop.game.cs01.Logger4J.OurLogger;
import eg.edu.alexu.csd.oop.game.cs01.ModeFactory.GameMode;
import eg.edu.alexu.csd.oop.game.cs01.Strategy.MovableX;
import eg.edu.alexu.csd.oop.game.cs01.Strategy.MovableY;
import eg.edu.alexu.csd.oop.game.cs01.objects.AbstractFallenObject;

public class FallenObjectsGenerator {

	private static FallenObjectsGenerator instance;
	private final int MAX_FALLEN_OBJECTS = 150;
	private Map<String, GameObject> map;
	private static GameDifficulty difficulty;
	private GameObject fallenObject;
	private static GameMode mode;
	private ArrayList<GameObject> pool;
	private ArrayList<GameObject> used;

	private FallenObjectsGenerator(GameMode mode, GameDifficulty difficulty) {
		FallenObjectsGenerator.mode = mode;
		this.map = mode.getMapMovable();
		FallenObjectsGenerator.difficulty = difficulty;
		pool = new ArrayList<GameObject>(MAX_FALLEN_OBJECTS);
		used = new ArrayList<GameObject>(MAX_FALLEN_OBJECTS);
		for (int i = 0; i < MAX_FALLEN_OBJECTS; i++) {
			Random random = new Random();
			// 2 is the number of the extra objects.
			int r = random.nextInt(difficulty.getColorsOfFallenObjects() + 2);
			if (i % 14 < 8 || (i % 14 > 8 && i % 14 < 13)) {
				r = (r + 2) % difficulty.getColorsOfFallenObjects() + 2;
			} else if (i % 8 == 8) {
				r = 0;
			} else if (i % 8 == 13) {
				r = 1;
			}
			// System.out.println(r + 1);
				try {
					pool.add((GameObject) ((AbstractFallenObject)map.get(Integer.toString(r))).clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					OurLogger.error(getClass(), e.getMessage());
				}
		}
	}

	public Map<String,GameObject> getMap() {
		return map;
	}

	public static FallenObjectsGenerator getInstance(GameMode mode, GameDifficulty difficulty) {
		if (instance == null) {
			instance = new FallenObjectsGenerator(mode, difficulty);
		}
		if (FallenObjectsGenerator.mode != mode || FallenObjectsGenerator.difficulty != difficulty) {
			instance = new FallenObjectsGenerator(mode, difficulty);
		}
		return instance;
	}

	public static FallenObjectsGenerator getInstance() {
		return instance;
	}

	public GameObject getNewObject() {
		try {
			Random random = new Random();
			int r = random.nextInt(pool.size());
			fallenObject = pool.get(r);
			pool.remove(r);
			fallenObject.setX(random.nextInt(mode.getConstant().get(0).getWidth() - 35));
			fallenObject.setY(random.nextInt(30));
			used.add(fallenObject);
			return fallenObject;
		} catch (Exception e) {
			OurLogger.error(getClass(), e.getMessage());
		}
		return null;
	}

	public void releaseObject(GameObject oldObject) {
		System.out.println("used= " + used.size());
		System.out.println("pool= " + pool.size());
		used.remove(oldObject);
		oldObject.setX(0);
		oldObject.setY(0);
		((AbstractFallenObject) oldObject).setMovableX(new MovableX());
		((AbstractFallenObject) oldObject).setMovableY(new MovableY());
		pool.add(oldObject);
	}

	public void clear() {
		instance = null;
	}
}
