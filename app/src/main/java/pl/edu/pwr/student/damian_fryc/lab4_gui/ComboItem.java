package pl.edu.pwr.student.damian_fryc.lab4_gui;

public class ComboItem {
    public int id;
    public String name;
    public ComboItem(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}