package core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import core.GameClass;
import core.actions.DelayAction;
import core.actions.RunnableAction;
import core.entities.*;
import core.entities.enemies.Enemy;
import core.entities.enemies.Runner;
import core.entities.enemies.Tank;
import core.entities.enemies.Walker;
import core.hud.HealthBar;
import core.hud.WeaponDisplay;
import core.items.*;
import core.projectiles.Projectile;
import core.ref.Ref;
import core.utils.Collisions;
import core.utils.Point;
import core.weapons.*;

import java.util.ArrayList;

public class GameScreen implements Screen, InputProcessor {

    GameClass core;

    OrthographicCamera cam;

    // Player variables
    Player player;
    boolean left = false, right = false, up = false, down = false, leftmousedown = false, leftmouseup = true, rightmousedown = false, sprint = false;
    float xvel = 0, yvel = 0, maxxvel = 1, maxyvel = 1, accel = 0.2f, decel = 1.2f;
    Vector3 mousePosition = new Vector3(), playerPosition = new Vector3();
    Ref.Direction playerDirection = Ref.Direction.DOWN;
    long lastDamage = 0;

    // Weapon variables
    Weapon[] weapons = new RangedWeapon[5];
    Weapon equippedWeapon;
    int currentWeapon = 0, nextWeapon = 0, availableWeapons = 0;
    boolean canFire = true, canHit = true;
    long leftmousedowntime = 0, rightmousedowntime = 0;

    // HUD
    Sprite cursor;

    HealthBar healthBar;
    WeaponDisplay weaponDisplay;

    ArrayList<Projectile> projectiles = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<>();

    boolean renderHitBoxes = false, renderMouseTrace = false, cursorCatched = true;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final int CAMERA_BATCH = 0, HUD_BATCH = 1;

    public GameScreen(GameClass core) {
        this.core = core;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, 640, 480);
        cam.update();

        core.getViewport().setCamera(cam);

        if (GameClass.getAssetManager().isLoaded(GameClass.getAssets().get("pistolcursor"))) {
            cursor = new Sprite((GameClass.getAssetManager().get(GameClass.getAssets().get("pistolcursor"), Texture.class)));
        } else {
            cursor = new Sprite(new Texture(GameClass.getAssets().get("pistolcursor")));
        }

        player = new Player();
        weapons[0] = new Pistol();
        weapons[1] = new Shotgun();
        weapons[2] = new Rifle();
        weapons[3] = new Bow();
        equippedWeapon = weapons[currentWeapon];

        for (Weapon weapon : weapons) {
            if (weapon != null) {
                ++availableWeapons;
            }
        }

        weaponDisplay = new WeaponDisplay(Gdx.graphics.getWidth() - 4, 46);
        healthBar = new HealthBar(Gdx.graphics.getWidth() - 4, 8);
        Gdx.input.setCursorCatched(cursorCatched);
    }

    public void pollInput() {
        // Get mouse position
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        playerPosition.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);

        if (leftmousedown) {
            playerAttack(mousePosition.x, mousePosition.y);
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
                    playerDirection = Ref.Direction.LEFT;
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
                    playerDirection = Ref.Direction.RIGHT;
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
                    playerDirection = Ref.Direction.DOWN;
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
                    playerDirection = Ref.Direction.UP;
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
        // Do actions in the action queue
        if (GameClass.getActionHandler().run()) {
            // Do things while the action queue is running
        }

        // Update positions
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        mousePosition = core.getViewport().unproject(mousePosition);

        // Update HUD
        healthBar.update(player);
        weaponDisplay.update(equippedWeapon);
        cursor.setPosition(mousePosition.x - cursor.getWidth() / 2, mousePosition.y - cursor.getHeight() / 2);

        // Move player
        player.update(playerDirection, xvel, yvel);

        // Update camera
        cam.position.set(playerPosition.x, playerPosition.y, 0);
        cam.update();

        // Change weapons
        if (equippedWeapon instanceof RangedWeapon) {
            RangedWeapon w = (RangedWeapon) equippedWeapon;
            if (!w.isReloading()) {
                if (currentWeapon != nextWeapon) {
                    currentWeapon = nextWeapon;
                    equippedWeapon = weapons[currentWeapon];
                }
            }
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
                if (Intersector.overlaps(e1.getCollisionRectangle(), e2.getCollisionRectangle()) && e1 != e2) {
                    Collisions.twoWaySeparation(e1, e2);
                }
            }
        }

        // Detect collisions between enemies and projectiles
        for (int j = enemies.size() - 1; j >= 0; --j) {
            Enemy e = enemies.get(j);
            for (int i = projectiles.size() - 1; i >= 0; --i) {
                Projectile p = projectiles.get(i);
                if (Intersector.overlaps(p.getBoundingRectangle(), e.getCollisionRectangle())) {
                    if (e.getHealth() > 0) {
                        if (e.getHealth() >= p.getDamage()) {
                            projectiles.remove(p);
                        }

                        e.damage(p.getDamage());
                        p.setDamage(e.getHealth() + p.getDamage());
                    }
                }
            }
        }

        // Detect collisions between enemies and the player
        for (int i = enemies.size() - 1; i >= 0; --i) {
            Enemy e = enemies.get(i);
            if (Intersector.overlaps(e.getCollisionRectangle(), player.getCollisionRectangle())) {
                Collisions.oneWaySeparation(player, e);
                if (System.currentTimeMillis() - lastDamage >= 1000) {
                    lastDamage = System.currentTimeMillis();

                    GameClass.getActionHandler().addAction(new RunnableAction(() -> player.damage(e.getDamage())));

                    GameClass.getActionHandler().addAction(new DelayAction(0.5f));

                    GameClass.getActionHandler().addAction(new RunnableAction(() -> player.getSprite().setColor(Color.WHITE)));
                }
            }
        }

        // Detect collisions between items and the player
        for (int i = items.size() - 1; i >= 0; --i) {
            Item e = items.get(i);
            if (Intersector.overlaps(e.getBoundingRectangle(), player.getCollisionRectangle())) {
                if (e instanceof PistolAmmo) {
                    for (Weapon weapon : weapons) {
                        if (weapon instanceof Pistol) {
                            Pistol p = (Pistol) weapon;
                            p.setRounds(p.getRounds() + e.getQuantity());
                        }
                    }
                }

                if (e instanceof ShotgunAmmo) {
                    for (Weapon weapon : weapons) {
                        if (weapon instanceof Shotgun) {
                            Shotgun s = (Shotgun) weapon;
                            s.setRounds(s.getRounds() + e.getQuantity());
                        }
                    }
                }

                if (e instanceof RifleAmmo) {
                    for (Weapon weapon : weapons) {
                        if (weapon instanceof Rifle) {
                            Rifle r = (Rifle) weapon;
                            r.setRounds(r.getRounds() + e.getQuantity());
                        }
                    }
                }

                if (e instanceof HealthPack) {
                    player.setHealth(player.getHealth() + e.getQuantity());
                }

                items.remove(e);
            }
        }

        // Spawn enemies
        spawnEnemies();
    }

    public void draw() {
        // Draw stuff affected by the camera
        core.getBatch(CAMERA_BATCH).setProjectionMatrix(cam.combined);
        core.getBatch(CAMERA_BATCH).begin();
        for (Item i : items) {
            i.draw(core.getBatch(CAMERA_BATCH));
        }
        player.draw(core.getBatch(CAMERA_BATCH));
        for (Projectile p : projectiles) {
            p.draw(core.getBatch(CAMERA_BATCH));
        }
        for (Enemy e : enemies) {
            e.draw(core.getBatch(CAMERA_BATCH));
        }
        cursor.draw(core.getBatch(CAMERA_BATCH));
        core.getBatch(CAMERA_BATCH).end();

        // Draw shapes
        if (renderHitBoxes || renderMouseTrace) {
            shapeRenderer.setProjectionMatrix(cam.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            if (renderMouseTrace) {
                shapeRenderer.line(new Vector3(mousePosition.x, mousePosition.y, mousePosition.z), playerPosition);
            }

            if (renderHitBoxes) {
                shapeRenderer.rect(player.getDamageRectangle().getX(), player.getDamageRectangle().getY(), player.getDamageRectangle().getWidth(), player.getDamageRectangle().getHeight());

                for (Enemy e : enemies) {
                    shapeRenderer.rect(e.getCollisionRectangle().getX(), e.getCollisionRectangle().getY(), e.getCollisionRectangle().getWidth(), e.getCollisionRectangle().getHeight());
                }

                for (Projectile p : projectiles) {
                    shapeRenderer.rect(p.getBoundingRectangle().getX(), p.getBoundingRectangle().getY(), p.getBoundingRectangle().getWidth(), p.getBoundingRectangle().getHeight());
                }
            }
            shapeRenderer.end();
        }

        // Draw HUD stuff
        core.getBatch(HUD_BATCH).begin();
        healthBar.draw(core.getBatch(HUD_BATCH));
        weaponDisplay.draw(core.getBatch(HUD_BATCH));
        core.getBatch(HUD_BATCH).end();
    }

    /**
     * Player performs an attack at the specified x and y coordinates (projected)
     * @param x projected x coordinate of attack
     * @param y projected y coordinate of attack
     */
    public void playerAttack(float x, float y) {
        Vector3 position = core.getViewport().unproject(new Vector3(x, y, 0));
        if (equippedWeapon instanceof RangedWeapon) {
            RangedWeapon w = (RangedWeapon) equippedWeapon;

            if (System.currentTimeMillis() - leftmousedowntime > w.getCooldown()) {
                leftmousedowntime = System.currentTimeMillis();

                if (w.isSemi()) {
                    if (leftmouseup) {
                        canFire = true;
                        leftmouseup = false;
                    } else {
                        canFire = false;
                    }
                } else {
                    canFire = true;
                }
            }

            if (!canFire) {
                return;
            }

            if (w.getAmmo() == 0 && !w.isReloading()) {
                w.reload();
                return;
            }

            canFire = false;
            float anglex = position.x - playerPosition.x, angley = position.y - playerPosition.y;
            float theta = (float) Math.atan2(angley, anglex);
            w.fire(projectiles, theta, playerPosition.x, playerPosition.y, player.getRadius() / 2);
        }
    }

    boolean spawning = true;
    long lastSpawn = System.currentTimeMillis();

    /**
     * Temporary method to spawn enemies
     */
    public void spawnEnemies() {
        if (spawning) {
            if (System.currentTimeMillis() - lastSpawn > 7500) {
                float spawnx = player.getX() + (float) (Math.floor(Math.random() * 700 - 350)), spawny = player.getY() + (float) (Math.floor(Math.random() * 520 - 260));
                if (Point.distance(new Point(spawnx, spawny), new Point(player.getX(), player.getY())) < 330) {
                    return;
                }
                lastSpawn = System.currentTimeMillis();
                spawnHorde(spawnx, spawny);
            }
        }
    }

    public void spawnHorde(float x, float y) {
        int size = (int) Math.floor(Math.random() * 10 + 5);
        for (int i = 0; i < size; ++i) {
            GameClass.getActionHandler().addAction(new RunnableAction(() -> {
                float spawnx = x + (float) (Math.floor(Math.random() * 20 - 10)), spawny = y + (float) (Math.floor(Math.random() * 20 - 10));
                int spawnType = (int) Math.floor(Math.random() * 5);
                switch (spawnType) {
                    case 0:
                        Tank t = new Tank(spawnx, spawny);
                        enemies.add(t);
                        break;
                    case 1:
                        Runner r = new Runner(spawnx, spawny);
                        enemies.add(r);
                        break;
                    case 2:
                    default:
                        Walker w = new Walker(spawnx, spawny);
                        enemies.add(w);
                        break;
                }
            }));
            GameClass.getActionHandler().addAction(new DelayAction(0.1f));
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
                case 4:
                    HealthPack h = new HealthPack(MathUtils.clamp(amount, 3, 5));
                    h.setPosition(x, y);
                    items.add(h);
                    break;
                default:
                    break;
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
    }

    @Override
    public void resume() {
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
                if (equippedWeapon instanceof RangedWeapon) {
                    if (!((RangedWeapon) equippedWeapon).isReloading()) {
                        ((RangedWeapon) equippedWeapon).reload();
                    }
                }
                break;
            case Input.Keys.SHIFT_LEFT:
                sprint = true;
                break;
            case Input.Keys.F1:
                cursorCatched = !cursorCatched;
                Gdx.input.setCursorCatched(cursorCatched);
                break;
            case Input.Keys.F2:
                renderMouseTrace = !renderMouseTrace;
                break;
            case Input.Keys.F3:
                renderHitBoxes = !renderHitBoxes;
                break;
            case Input.Keys.F4:
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
                if (leftmousedown) {
                    leftmouseup = true;
                }
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
        if (amountY > 0) {
            nextWeapon = currentWeapon + 1;
            nextWeapon = nextWeapon >= availableWeapons ? nextWeapon - availableWeapons : nextWeapon;
        } else if (amountY < 0) {
            nextWeapon = currentWeapon - 1;
            nextWeapon = nextWeapon < 0 ? nextWeapon + availableWeapons : nextWeapon;
        }
        return false;
    }
}