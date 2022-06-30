package Models.Medicines;

public class Drops extends Medicine{
    public int dosage;

    public Drops(String identifier, String name, int morningTaking, int afternoonTaking,
                 int eveningTaking, int nightTaking, int dosage) {
        super(identifier, name, morningTaking, afternoonTaking, eveningTaking, nightTaking);
        setType(Type.DROPS);
        setDosage(dosage);
    }

    public int getDosage() {
        return this.dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }
}
