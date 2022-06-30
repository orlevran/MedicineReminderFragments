package Models.Medicines;

import java.io.Serializable;

public class Medicine implements Serializable {

    private final static int DEFAULT_TAKING = -1;
    private final static String EMPTY = "Empty";

    private String identifier, name;
    private Type type;
    private int nightTaking, morningTaking, afternoonTaking, eveningTaking;
    private boolean timeToTake;

    /*
     * morning: 360 - 659 (06:00 - 10:59)
     * afternoon 660 - 899 (11:00 - 14:59)
     * evening 900 - 1139 (15:00 - 18:59)
     * night : 1140 - 1439 (19:00 - 23:59) */

    public Medicine(String identifier){
        // Default constructor- empty cell
        setIdentifier(identifier);
        setToEmpty();
    }

    public Medicine(String identifier, String name, int morningTaking,
                    int afternoonTaking, int eveningTaking, int nightTaking) {
        setIdentifier(identifier);
        setName(name);
        setMorningTaking(morningTaking);
        setAfternoonTaking(afternoonTaking);
        setEveningTaking(eveningTaking);
        setNightTaking(nightTaking);
        setTimeToTake(false);
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getNightTaking() {
        return this.nightTaking;
    }

    public void setNightTaking(int nightTaking) {
        this.nightTaking = nightTaking;
    }

    public int getMorningTaking() {
        return this.morningTaking;
    }

    public void setMorningTaking(int morningTaking) {
        this.morningTaking = morningTaking;
    }

    public int getAfternoonTaking() {
        return this.afternoonTaking;
    }

    public void setAfternoonTaking(int afternoonTaking) {
        this.afternoonTaking = afternoonTaking;
    }

    public int getEveningTaking() {
        return this.eveningTaking;
    }

    public void setEveningTaking(int eveningTaking) {
        this.eveningTaking = eveningTaking;
    }

    public void setToEmpty() {
        setName(EMPTY);
        setType(Type.EMPTY);
        setNightTaking(DEFAULT_TAKING);
        setMorningTaking(DEFAULT_TAKING);
        setAfternoonTaking(DEFAULT_TAKING);
        setEveningTaking(DEFAULT_TAKING);
        setTimeToTake(false);
    }

    public boolean isTimeToTake() {
        return timeToTake;
    }

    public void setTimeToTake(boolean timeToTake) {
        this.timeToTake = timeToTake;
    }
}
