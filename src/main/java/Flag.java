public class Flag {

    public String name;
    public int landmass;
    public int zone;
    public int area;
    public int population;
    public int language;
    public int religion;
    public int bars;
    public int stripes;
    public int colours;
    public int red;
    public int green;
    public int blue;
    public int gold;
    public int white;
    public int black;
    public int orange;
    public String mainhue;
    public int circles;
    public int crosses;
    public int saltires;
    public int quarters;
    public int sunstars;
    public int crescent;
    public int triangle;
    public int icon;
    public int animate;
    public int text;
    public String topleft;
    public String botright;


    public Flag(String name, int landmass, int zone, int area, int population, int language, int religion, int bars, int stripes, int colours, int red, int green, int blue, int gold, int white, int black, int orange, String mainhue, int circles, int crosses, int saltires, int quarters, int sunstars, int crescent, int triangle, int icon, int animate, int text, String topleft, String botright) {
        this.name = name;
        this.landmass = landmass;
        this.zone = zone;
        this.area = area;
        this.population = population;
        this.language = language;
        this.religion = religion;
        this.bars = bars;
        this.stripes = stripes;
        this.colours = colours;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.gold = gold;
        this.white = white;
        this.black = black;
        this.orange = orange;
        this.mainhue = mainhue;
        this.circles = circles;
        this.crosses = crosses;
        this.saltires = saltires;
        this.quarters = quarters;
        this.sunstars = sunstars;
        this.crescent = crescent;
        this.triangle = triangle;
        this.icon = icon;
        this.animate = animate;
        this.text = text;
        this.topleft = topleft;
        this.botright = botright;
    }

    public String toString(){
        return this.name;
    }


}
