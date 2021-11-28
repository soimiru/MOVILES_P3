package dadm.scaffold.engine;

public class PlayerAvatar {

    int index, idDrawable;
    String name;

    public PlayerAvatar(int i, int id, String n){
        this.index = i;
        this.idDrawable = id;
        this.name = n;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public void setIdDrawable(int idDrawable) {
        this.idDrawable = idDrawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
