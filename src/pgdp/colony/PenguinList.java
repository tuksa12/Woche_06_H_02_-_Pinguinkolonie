package pgdp.colony;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PenguinList implements Iterable<Penguin> {
    protected List<Penguin> list = new ArrayList<>();

    public boolean add(Penguin penguin) {
        return list.add(penguin);
    }

    public Penguin get(int index) {
        return list.get(index);
    }

    public boolean remove(Penguin penguin) {
        return list.remove(penguin);
    }

    public Penguin remove(int index) {
        return list.remove(index);
    }

    public int size() {
        return list.size();
    }

    public Iterator<Penguin> iterator() {
        return list.iterator();
    }
}
