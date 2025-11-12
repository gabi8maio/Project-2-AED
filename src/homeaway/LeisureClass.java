/** Authors:
 *  Gabriel Oliveira 70886 gdm.oliveira@campus.fct.unl.pt
 *  Diogo Figueiredo 70764 dam.figueiredo@campus.fct.unl.pt
 */
package homeaway;

public class LeisureClass extends ServicesClassAbstract implements Leisure {

    private static final long serialVersionUID = 0L;

    private final double price;



    public LeisureClass(long latitude, long longitude, double price, int value, String serviceName){
        super(latitude,longitude,value, serviceName);
        this.price = price * (1-((double) value /100));
    }

    @Override
    public String getServiceType() {
        return TypesOfService.LEISURE.toString();
    }

    @Override
    public double getPrice (){
        return this.price;
    }


}
