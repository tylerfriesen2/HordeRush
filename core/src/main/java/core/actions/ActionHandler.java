package core.actions;

import java.util.ArrayList;
import java.util.List;

public class ActionHandler {

    List<Action> queue = new ArrayList<>();

    private int index = 0;

    public ActionHandler() {

    }

    public boolean run() {
        if (index >= queue.size()) { return true; }
        if (queue.get(index).run()) {
            ++index;
            return index >= queue.size();
        }

        return false;
    }

    public void reset() {
        queue.clear();
        index = 0;
    }

    public int getIndex() {
        return index;
    }

    public void remove(int index) {
        if (index < 0 || index > queue.size() - 1) { return; }
        queue.remove(index);
    }

    public void addAction(Action action) {
        queue.add(action);
    }

    public void addAction(Action action, int index) {
        queue.add(index, action);
    }

    public void finish() {
        queue = queue.subList(0, index + 1);
    }

    public Action getCurrentAction() {
        return queue.get(index);
    }

    public int getSize() {
        return queue.size();
    }

}
