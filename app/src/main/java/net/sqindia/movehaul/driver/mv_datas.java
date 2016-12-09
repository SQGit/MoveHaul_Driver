package net.sqindia.movehaul.driver;

/**
 * Created by SQINDIA on 12/9/2016.
 */

public class MV_Datas {

    String driver_id;
    String booking_id;
    String job_id;
    String customer_id;
    String pickup;
    String drop;
    String goods_type;
    String time;
    String date;
    String desc;



    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getDrop() {
        return drop;
    }

    public String getJob_id() {
        return job_id;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public String getPickup() {
        return pickup;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public String getTime() {
        return time;
    }
}
