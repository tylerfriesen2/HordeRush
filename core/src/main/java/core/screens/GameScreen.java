package core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Timer;
import core.GameClass;
import core.entities.Enemy;
import core.entities.Player;
import core.entities.Zombie;
import core.projectiles.Projectile;
import core.utils.Collisions;
import core.weapons.*;

import java.util.ArrayList;

public class GameScreen implements Screen, InputProcessor {

    GameClass core;

    // Player variables
    Player player;
    boolean left = false, right = false, up = false, down = false, leftmousedown = false, rightmousedown = false, sprint = false;
    float xvel = 0, yvel = 0, maxxvel = 1, maxyvel = 1, accel = 0.2f, decel = 1.2f;

    // Weapon variables
    RangedWeapon[] rangedWeapons = new RangedWeapon[10];
    MeleeWeapon meleeWeapon;
    RangedWeapon rangedWeapon;
    int currentWeapon = 0, nextWeapon = 0;
    boolean canFire = true, canHit = true;
    long leftmousedowntime = 0, rightmousedowntime = 0;

    ArrayList<Projectile> projectiles = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();

    public GameScreen(GameClass core) {
        this.core = core;
        player = new Player();
        rangedWeapons[0] = new Pistol();
        rangedWeapons[1] = new Shotgun();
        rangedWeapon = rangedWeapons[currentWeapon];
    }

    public void pollInput() {
        if (sprint) {
            maxxvel = 1.5f; maxyvel = 1.5f;
        } else {
            maxxvel = 1f; maxyvel = 1f;
        }

        if (!left && !right) {
            if (Math.abs(xvel) > 0.1f) {
                xvel /= decel;
            } else {
                xvel = 0;
            }
        } else {
            if (left) {
                if (!right) {
                    if (Math.abs(xvel) < maxxvel) {
                        xvel -= accel;
                    } else {
                        xvel = -maxxvel;
                    }
                } else {
                    if (Math.abs(xvel) > 0.1f) {
                        xvel /= decel;
                    } else {
                        xvel = 0;
                    }
                }
            }

            if (right) {
                if (!left) {
                    if (Math.abs(xvel) < maxxvel) {
                        xvel += accel;
                    } else {
                        xvel = maxxvel;
                    }
                } else {
                    if (Math.abs(xvel) > 0.1f) {
                        xvel /= decel;
                    } else {
                        xvel = 0;
                    }
                }
            }
        }

        if (!up && !down) {
            if (Math.abs(yvel) > 0.1f) {
                yvel /= decel;
            } else {
                yvel = 0;
            }
        } else {
            if (down) {
                if (!up) {
                    if (Math.abs(yvel) < maxyvel) {
                        yvel -= accel;
                    } else {
                        yvel = -maxyvel;
                    }
                } else {
                    if (Math.abs(yvel) > 0.1f) {
                        yvel /= decel;
                    } else {
                        yvel = 0;
                    }
                }
            }

            if (up) {
                if (!down) {
                    if (Math.abs(yvel) < maxyvel) {
                        yvel += accel;
                    } else {
                        yvel = maxyvel;
                    }
                } else {
                    if (Math.abs(yvel) > 0.1f) {
                        yvel /= decel;
                    } else {
                        yvel = 0;
                    }
                }
            }
        }
    }

    public void update(float delta) {
        // Move player
        player.update(xvel, yvel);

        // Change weapons
        if (currentWeapon != nextWeapon) {
            currentWeapon = nextWeapon;
            rangedWeapon = rangedWeapons[currentWeapon];
        }

        // Update projectiles
        for (int i = projectiles.size() - 1; i >= 0; --i) {
            Projectile p = projectiles.get(i);
            if (!p.isAlive()) {
                projectiles.remove(p);
            } else {
                p.update();
            }
        }

        // Update enemies
        for (int i = enemies.size() - 1; i >= 0; --i) {
            Enemy e = enemies.get(i);
            if (!e.isAlive()) {
                enemies.remove(e);
            } else {
                e.update(delta, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
            }
        }

        // Detect collisions between enemies and other enemies
        for (int i = enemies.size() - 1; i >= 0; --i) {
            for (int j = enemies.size() - 1; j >= 0; --j) {
                Enemy e1 = enemies.get(i), e2 = enemies.get(j);
                if (e1 == e2) {
                    continue;
                } else if (Intersector.overlaps(e1.getBoundingRectangle(), e2.getBoundingRectangle())) {
                    Collisions.separateBoth(e1, e2);
                }
            }
        }

        // Detect collisions between enemies and bullets
        for (int i = projectiles.size() - 1; i >= 0; --i) {
            Projectile p = projectiles.get(i);
            for (int j = enemies.size() - 1; j >= 0; --j) {
                Enemy e = enemies.get(j);
                if (Intersector.overlaps(p.getBoundingRectangle(), e.getBoundingRectangle())) {
                    e.damage(p.getDamage());
                    if (e.getHealth() >= 0) {
                        projectiles.remove(p);
                    }
                }
            }
        }

        // Spawn enemies
        spawnEnemies();
    }

    public void draw() {
        core.getBatch().begin();
        player.draw(core.getBatch());
        for (Projectile p : projectiles) {
            p.draw(core.getBatch());
        }
        for (Enemy e : enemies) {
            e.draw(core.getBatch());
        }
        core.getBatch().end();
    }

    public void playerFire(float mousex, float mousey) {
        if (System.currentTimeMillis() - leftmousedowntime > rangedWeapon.getCooldown()) {
            leftmousedowntime = System.currentTimeMillis();
            canFire = true;
        }

        if (!canFire) { return; }

        canFire = false;
        float playerx = player.getX() + player.getWidth() / 2, playery = player.getY() + player.getHeight() / 2;
        float anglex = mousex - playerx, angley = mousey - playery;
        float theta = (float) (Math.atan2(angley, anglex));
        float spawnx = playerx + (float) (16 * Math.cos(theta)), spawny = playery + (float) (16 * Math.sin(theta));
        rangedWeapon.fire(projectiles, theta, playerx, playery, 8);
    }

    boolean spawning = true;
    long lastSpawn = System.currentTimeMillis() + 2000;

    /**
     * Temporary method to spawn enemies
     */
    public void spawnEnemies() {
        if (spawning) {
            if (System.currentTimeMillis() - lastSpawn > 5000) {
                lastSpawn = System.currentTimeMillis();
                float spawnx = (float) (Math.floor(Math.random() * 608 + 16)), spawny = (float) (Math.floor(Math.random() * 448 + 16));
                spawnGroup(spawnx, spawny);
            }
        }
    }

    public void spawnGroup(float x, float y) {
        int size = (int) Math.floor(Math.random() * 5 + 2);
        for (int i = 0; i < size; ++i) {
            Zombie z = new Zombie(x, y);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    enemies.add(z);
                }
            }, 0.5f);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        pollInput();
        update(delta);
        draw();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                left = true;
                break;
            case Input.Keys.D:
                right = true;
                break;
            case Input.Keys.W:
                up = true;
                break;
            case Input.Keys.S:
                down = true;
                break;
            case Input.Keys.SHIFT_LEFT:
                sprint = true;
                break;
            case Input.Keys.NUM_1:
                nextWeapon = 0;
                break;
            case Input.Keys.NUM_2:
                nextWeapon = 1;
                break;
            case Input.Keys.BACKSPACE:
                spawning = !spawning;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                left = false;
                break;
            case Input.Keys.D:
                right = false;
                break;
            case Input.Keys.W:
                up = false;
                break;
            case Input.Keys.S:
                down = false;
                break;
            case Input.Keys.SHIFT_LEFT:
                sprint = false;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Input.Buttons.LEFT:
                leftmousedown = true;
                playerFire(screenX, Gdx.graphics.getHeight() - screenY);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Input.Buttons.LEFT:
                leftmousedown = false;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}