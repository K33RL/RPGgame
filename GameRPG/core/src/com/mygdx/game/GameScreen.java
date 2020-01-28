package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Items.Weapon;
import com.mygdx.game.characters.GameCharacter;
import com.mygdx.game.characters.Ghost;
import com.mygdx.game.characters.Hero;
import com.mygdx.game.characters.Monster;

import java.awt.event.KeyListener;
import java.util.*;

public class GameScreen {
    private Hero hero;
    private BitmapFont font20;
    private Map map;
    private ItemsEmitter itemsEmitter;
    private SpriteBatch batch;
    private Stage stage;
    private Music music;
    private TextEmitter textEmitter;
    private boolean paused = false;
    private Sound sound;
    private float spawnTimer;


    public TextEmitter getTextEmitter() {
        return textEmitter;
    }

    private List<GameCharacter> allCharacters;
    private List<Monster> allMonsters;

    public List<Monster> getAllMonsters() {
        return allMonsters;
    }

    private Comparator<GameCharacter> drawOrderComparator;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public Hero getHero() {
        return hero;
    }


    public void create() {
        map = new Map();
        textEmitter = new TextEmitter();
        music = Gdx.audio.newMusic(Gdx.files.internal("bc.mp3"));
        music.setLooping(true);
        music.setVolume(0.01f);
        music.play();


//        sound = Gdx.audio.newSound(Gdx.files.internal("sound_4.wav"));
//        sound.play();

        allCharacters = new ArrayList<>();
        allMonsters = new ArrayList<>();
        hero = new Hero(this);
        itemsEmitter = new ItemsEmitter();
        allCharacters.addAll(Arrays.asList(
                hero,
                new Monster(this),
//                new Ghost(this),
                new Monster(this),
//                new Ghost(this),
                new Monster(this),
                new Monster(this),
                new Monster(this)
//                new Ghost(this)
        ));
        for (int i = 0; i < allCharacters.size(); i++) {
            if (allCharacters.get(i) instanceof Monster) {
                allMonsters.add(((Monster) allCharacters.get(i)));
            }

        }

        font20 = new BitmapFont(Gdx.files.internal("Nov.fnt"));
        stage = new Stage();

        Skin skin = new Skin();
        skin.add("simpleButton", new Texture("simpleButton.png"));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font20;

        TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        TextButton exitButton = new TextButton("Exit", textButtonStyle);
        pauseButton.addListener(new ClickListener() {
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        paused = !paused;
                                    }
                                }

        );
        exitButton.addListener(new ClickListener() {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       Gdx.app.exit();
                                   }
                               }

        );
        Group menuGroup = new Group();
        menuGroup.addActor(pauseButton);
        menuGroup.addActor(exitButton);
        exitButton.setPosition(160, 0);
        menuGroup.setPosition(960, 680);
        menuGroup.setVisible(true);


        stage.addActor(menuGroup);
        Gdx.input.setInputProcessor(stage);


        drawOrderComparator = new Comparator<GameCharacter>() {
            @Override
            public int compare(GameCharacter o1, GameCharacter o2) {
                return (int) (o2.getPosition().y - o1.getPosition().y);
            }
        };

    }

    public Map getMap() {
        return map;
    }

    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        map.render(batch);
        Collections.sort(allCharacters, drawOrderComparator);
        for (int i = 0; i < allCharacters.size(); i++) {
            allCharacters.get(i).render(batch, font20);
        }
        itemsEmitter.render(batch);
        textEmitter.render(batch, font20);
        hero.renderHUD(batch, font20);
        batch.end();
        stage.draw();

    }

    public void update(float dt) {
        if (!paused) {
            spawnTimer += dt;
            if (spawnTimer > 3.0f) {
                Monster monster = new Monster(this);
                allCharacters.add(monster);
                allMonsters.add(monster);
                spawnTimer = 0.0f;
            }
            for (int i = 0; i < allCharacters.size(); i++) {
                allCharacters.get(i).update(dt);
            }
            for (int i = 0; i < allMonsters.size(); i++) {
                Monster currentMonster = allMonsters.get(i);
                if (!currentMonster.isAlive()) {
                    allMonsters.remove(currentMonster);
                    allCharacters.remove(currentMonster);
                    itemsEmitter.generateRandomItem(currentMonster.getPosition().x, currentMonster.getPosition().y, 5, 0.6f);
                    hero.killMonster(currentMonster);
                }
            }

//            for (int i = 0; i < allGhosts.size(); i++) {
//                Ghost currentGhost = allGhosts.get(i);
//                if (!currentGhost.isAlive()) {
//                    allGhosts.remove(currentGhost);
//                    allCharacters.remove(currentGhost);
//                    itemsEmitter.generateRandomItem(currentGhost.getPosition().x, currentGhost.getPosition().y, 5, 0.6f);
//                    hero.killGhost(currentGhost);
//                }
//            }

            for (int i = 0; i < itemsEmitter.getItems().length; i++) {
                Item it = itemsEmitter.getItems()[i];
                if (it.isActive()) {
                    float dst = hero.getPosition().dst(it.getPosition());
                    if (dst < 24.0f) {
                        hero.useItem(it);
                    }
                }
            }
            textEmitter.update(dt);
            itemsEmitter.update(dt);
        }
        stage.act(dt);
    }
}
