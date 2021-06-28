package core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import core.GameClass;
import core.entities.Enemy;
import core.entities.Player;
import core.entities.Walker;
import core.hud.WeaponDisplay;
import core.items.Item;
import core.items.PistolAmmo;
import core.items.RifleAmmo;
import core.items.ShotgunAmmo;
import core.projectiles.Projectile;
import core.utils.Collisions;
import core.utils.Point;
import core.weapons.*;

import java.util.ArrayList;

public class GameScreen implements Screen, InputProcessor {

    GameClass core;

    OrthographicCamera cam;

    // Player variables
    Player player;
    boolean left = false, right = false, up = false, down = false, leftmousedown = false, leftmouseup = false, rightmousedown = false, sprint = false;
    float xvel = 0, yvel = 0, maxxvel = 1, maxyvel = 1, accel = 0.2f, decel = 1.2f;
    Vector3 mousePosition = new Vector3();

    // Weapon variables
    RangedWeapon[] rangedWeapons = new RangedWeapon[10];
    MeleeWeapon meleeWeapon;
    RangedWeapon rangedWeapon;
    int currentWeapon = 0, nextWeapon = 0;
    boolean canFire = true, canHit = true;
    long leftmousedowntime = 0, rightmousedowntime = 0;

    // HUD
    WeaponDisplay weaponDisplay;

    ArrayList<Projectile> projectiles = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<>();

    public GameScreen(GameClass core) {
        this.core = core;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, 640, 480);
        cam.update();

        core.getViewport().setCamera(cam);

        player = new Player();
        rangedWeapons[0] = new Pistol();
        rangedWeapons[1] = new Shotgun();
        rangedWeapons[2] = new Rifle();
        rangedWeapon = rangedWeapons[currentWeapon];

        weaponDisplay = new WeaponDisplay(Gdx.graphics.getWidth() - 4, 16);
    }

    public void pollInput() {
        // Get mouse position
        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        if (leftmousedown) {
            playerFire(cam.unproject(mousePosition));
        }

        if (sprint) {
            maxxvel = 2f;
            maxyvel = 2f;
        } else {
            maxxvel = 1f;
            maxyvel = 1f;
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
        // Do actions
        if (core.getActionHandler().run()) {

        }

        // Update HUD
        weaponDisplay.update(rangedWeapon);

        // Move player
        player.update(xvel, yvel);

        // Update camera
        cam.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
        cam.update();

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
            if (e.isDisposable()) {
                enemies.remove(e);
                dropLoot(e.getX(), e.getY());
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
                    Collisions.twoWaySeparation(e1, e2);
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

        // Detect collisions between enemies and the player
        for (int i = enemies.size() - 1; i >= 0; --i) {
            Enemy e = enemies.get(i);
            if (Intersector.overlaps(e.getBoundingRectangle(), player.getBoundingRectangle())) {
                Collisions.oneWaySeparation(player, e);
            }
        }

        // Detect collisions between items and the player
        for (int i = items.size() - 1; i >= 0; --i) {
            Item e = items.get(i);
            if (Intersector.overlaps(e.getBoundingRectangle(), player.getBoundingRectangle())) {
                if (e instanceof PistolAmmo) {
                    for (int j = 0; j < rangedWeapons.length; ++j) {
                        if (rangedWeapons[j] instanceof Pistol) {
                            rangedWeapons[j].setRounds(rangedWeapons[j].getRounds() + e.getQuantity());
                        }
                    }
                }

                if (e instanceof ShotgunAmmo) {
                    for (int j = 0; j < rangedWeapons.length; ++j) {
                        if (rangedWeapons[j] instanceof Shotgun) {
                            rangedWeapons[j].setRounds(rangedWeapons[j].getRounds() + e.getQuantity());
                        }
                    }
                }

                if (e instanceof RifleAmmo) {
                    for (int j = 0; j < rangedWeapons.length; ++j) {
                        if (rangedWeapons[j] instanceof Rifle) {
                            rangedWeapons[j].setRounds(rangedWeapons[j].getRounds() + e.getQuantity());
                        }
                    }
                }

                items.remove(e);
            }
        }

        // Spawn enemies
        spawnEnemies();
    }

    public void draw() {
        core.getBatch(0).setProjectionMatrix(cam.combined);
        core.getBatch(0).begin();
        for (Item i : items) {
            i.draw(core.getBatch(0));
        }
        for (Projectile p : projectiles) {
            p.draw(core.getBatch(0));
        }
        player.draw(core.getBatch(0));
        for (Enemy e : enemies) {
            e.draw(core.getBatch(0));
        }
        core.getBatch(0).end();

        core.getBatch(1).begin();
        weaponDisplay.draw(core.getBatch(1));
        core.getBatch(1).end();
    }

    public void playerFire(Vector3 position) {
        if (System.currentTimeMillis() - leftmousedowntime > rangedWeapon.getCooldown()) {
            leftmousedowntime = System.currentTimeMillis();

            if (rangedWeapon.isSemi()) {
                if (leftmouseup) {
                    canFire = true;
                }
            } else {
                canFire = true;
            }
        }

        if (!canFire) {
            return;
        }

        if (rangedWeapon.getAmmo() == 0) {
            rangedWeapon.reload();
            return;
        }

        canFire = false;
        float playerx = player.getX() + player.getWidth() / 2, playery = player.getY() + player.getHeight() / 2;
        float anglex = position.x + 2 - playerx, angley = position.y + 2 - playery;
        float theta = (float) (Math.atan2(angley, anglex));
        rangedWeapon.fire(projectiles, theta, playerx, playery, 8);
    }

    boolean spawning = true;
    long lastSpawn = System.currentTimeMillis();

    /**
     * Temporary method to spawn enemies
     */
    public void spawnEnemies() {
        if (spawning) {
            if (System.currentTimeMillis() - lastSpawn > 5000) {
                float spawnx = player.getX() + (float) (Math.floor(Math.random() * 300 - 150)), spawny = player.getY() + (float) (Math.floor(Math.random() * 300 - 150));
                if (Point.distance(new Point(spawnx, spawny), new Point(player.getX(), player.getY())) < 100) {
                    return;
                }
                lastSpawn = System.currentTimeMillis();
                spawnGroup(spawnx, spawny);
            }
        }
    }

    public void spawnGroup(float x, float y) {
        int size = (int) Math.floor(Math.random() * 5 + 5);
        for (int i = 0; i < size; ++i) {
            Walker w = new Walker(x + (float) (Math.floor(Math.random() * 20 - 10)), y + (float) (Math.floor(Math.random() * 20 - 10)));
            enemies.add(w);
        }
    }

    public void dropLoot(float x, float y) {
        int type = (int) Math.floor(Math.random() * 5);
        if (type > 0) {
            int amount = (int) Math.floor(Math.random() * 5) + 1;
            switch (type) {
                case 1:
                    PistolAmmo p = new PistolAmmo(amount);
                    p.setPosition(x, y);
                    items.add(p);
                    break;
                case 2:
                    ShotgunAmmo s = new ShotgunAmmo(amount);
                    s.setPosition(x, y);
                    items.add(s);
                    break;
                case 3:
                    RifleAmmo r = new RifleAmmo(amount);
                    r.setPosition(x, y);
                    items.add(r);
                    break;
                default: break;
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        pollInput();
        update(delta);
        draw();
    }

    @Override
    public void resize(int width, int height) {
        core.getViewport().update(width, height);
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
            case Input.Keys.R:
                rangedWeapon.reload();
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
            case Input.Keys.NUM_3:
                nextWeapon = 2;
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
                leftmouseup = false;
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
                if (leftmousedown) { leftmouseup = true; }
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
        cam.zoom += amountY / 10;
        return false;
    }
}