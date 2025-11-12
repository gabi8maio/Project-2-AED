/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;


import java.io.Serializable;

public abstract class StudentsClassAbstract implements Students, Serializable {

    private final String name;
    private final String type;
    private final String country;
    private Services lodging;
    private Services placeNow;

    private static final long serialVersionUID = 0L;

    public StudentsClassAbstract(String type, String name, String country, Services lodging){
        this.type = type;
        this.name = name;
        this.country = country;
        this.lodging = lodging;
        this.placeNow = lodging;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Students o1){
        return this.getName().compareTo(o1.getName());
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setPlaceHome(Services newPlace) {
        lodging = newPlace;
    }

    @Override
    public void setPlaceGo(Services newPlace) {
        placeNow = newPlace;
    }

    @Override
    public  Services getPlaceHome() {
        return lodging;
    }

    @Override
    public  Services getPlaceNow() {
        return placeNow;
    }

}
