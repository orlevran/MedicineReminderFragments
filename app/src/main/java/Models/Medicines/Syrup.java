package Models.Medicines;

public class Syrup extends Medicine{
    private double dosage;

    public Syrup(String identifier, String name, int morningTaking, int afternoonTaking,
                 int eveningTaking, int nightTaking, double dosage) {
        super(identifier, name, morningTaking, afternoonTaking, eveningTaking, nightTaking);
        setType(Type.SYRUP);
        setDosage(dosage);
    }

    public double getDosage() {
        return this.dosage;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }
}
