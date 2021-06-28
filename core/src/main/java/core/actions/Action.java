package core.actions;

public abstract class Action {

    protected boolean run = false;

    public Action() {}

    public abstract boolean run();

    public boolean isRun() {
        return run;
    }
}
