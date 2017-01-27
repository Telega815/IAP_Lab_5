import java.util.ArrayList;

class Lang{
    public String setDot;
    public String removeDots;
    public String tryToConnect;
    
    public Lang(String s1, String s2, String s3){
        setDot = s1;
        removeDots = s2;
        tryToConnect = s3;
    }
}

public class Localization {
    public static ArrayList<Lang> Langs = new ArrayList<>();
    
    public Localization(){
        Langs.add(new Lang("setDot", "removeDots", "tryToConnect"));
        Langs.add(new Lang("Поставить точку", "Удалить все точки", "Восстановить соединение"));
        Langs.add(new Lang("Gesetzt", "Entfernen Sie Punkte","verbinden"));
    }
}
