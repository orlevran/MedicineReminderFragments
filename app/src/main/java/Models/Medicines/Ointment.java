package Models.Medicines;

public class Ointment extends Medicine{
    public Ointment(String identifier, String name, int morningTaking, int afternoonTaking, int eveningTaking, int nightTaking) {
        super(identifier, name, morningTaking, afternoonTaking, eveningTaking, nightTaking);
        setType(Type.OINTMENT);
    }
}
