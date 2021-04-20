package IDATT2106.team6.Gidd.models;

import javax.persistence.*;

@Entity
@Table(name = "ACTIVITY_EQUIPMENT")
public class ActivityEquipment {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activityId")
    private Activity activity;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "equipmentId")
    private Equipment equipment;
    private int bringerId;

    public ActivityEquipment(){}

    public ActivityEquipment(Activity activity, Equipment equipment){
        this.activity = activity;
        this.equipment = equipment;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getId() {
        return id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getBringerId() {
        return bringerId;
    }

    public void setBringerId(int bringerId) {
        this.bringerId = bringerId;
    }


    @Override
    public String toString() {
        return  "{" +
            "\n   \"equipmentId\": " + equipment.getEquipmentId() +
            ",\n   \"description\": \"" + equipment.getDescription() + "\"" +
            ",\n   \"bringerID\": " + this.getBringerId() +
            "\n}";
    }
}
