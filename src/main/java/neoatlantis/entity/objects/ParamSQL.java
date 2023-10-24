package neoatlantis.entity.objects;

import neoatlantis.utils.catalogs.objects.OrderMode;
import neoatlantis.entity.SimpleEntity;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ParamSQL {

    private String[] order;
    private OrderMode orderMode;
    private boolean filter;

    public ParamSQL() {
        this.order = new String[]{"1"};
        this.orderMode = OrderMode.ASC;
    }

    /**
     * @return the order
     */
    public String[] getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(String[] order) {
        this.order = order;
    }
    
    public void setOrder(String order){
        this.setOrder(new String[] {order});
    }

    /**
     * @return the orderMode
     */
    public OrderMode getOrderMode() {
        return orderMode;
    }

    /**
     * @param orderMode the orderMode to set
     */
    public void setOrderMode(OrderMode orderMode) {
        this.orderMode = orderMode;
    }

    /**
     * @return the filter
     */
    public boolean isFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public String getSort() {
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; this.getOrder() != null && i < this.getOrder().length; i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(this.getOrder()[i]).append(" ").append(this.getOrderMode());
        }

        return sb.toString();
    }
    
    public String obtieneInfo(int nivel){
        StringBuilder sb=new StringBuilder("");
        sb.append(SimpleEntity.generaPreNivel(nivel)).append("|--------------------------------------------------  SQL Params  -------------------------------------------------- ").append(System.getProperty("line.separator"));
        
        
        sb.append(SimpleEntity.generaPreNivel(nivel)).append("Order: {");
        for(int i=0; this.order!=null&&i<this.order.length; i++){
            if(i>0){
                sb.append(", ");
            }
            sb.append(this.order[i]);
        }
        sb.append("}").append(System.getProperty("line.separator"));
        sb.append(SimpleEntity.generaPreNivel(nivel)).append("OrderMode: ").append(this.orderMode).append(System.getProperty("line.separator"));
        sb.append(SimpleEntity.generaPreNivel(nivel)).append("Filter: ").append(this.filter).append(System.getProperty("line.separator"));
        sb.append(SimpleEntity.generaPreNivel(nivel)).append("|------------------------------------------------------------------------------------------------------------------ ").append(System.getProperty("line.separator"));
        
        return sb.toString();
    }
}
