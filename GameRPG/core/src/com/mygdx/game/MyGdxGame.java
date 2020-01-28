package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	/*
	=== Идеи ===
	1. + Движение по пикселям
	2. Преграды
	3. Анимация
	4. Снаряды
	5. + Хаотичное движение монстра
	6. + Преследование героя
	7. Аптечки, Монеты, Зелья (все что можно поднять)
	8. Параметры героям и монстрам(ХП, мана, крит шанс, скорость)
	9. Система уровней игры
	10. Опыт герою
	11. +Оружие
	12. Босс
	13. +Драка с монстрами
	14. +Полоска здоровья
	15. +Привязать логику послоски к ХП героя
	16. +Перенос на вектора
	17. Инвенталь
	18. +Отображение ткста
	19. Хот бар
	20. Камера
	21.!!!!!!!!!Вылетает ошибка при оверкапе экспы
	 */


	private SpriteBatch batch;
	private GameScreen gameScreen;

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.gameScreen = new GameScreen(batch);
		this.gameScreen.create();

	}

	@Override
	public void render () {
	gameScreen.render();
	}


	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
