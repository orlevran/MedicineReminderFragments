package Models.Medicines;

public class Pill extends Medicine{
    private int initialAmount, currentAmount, dosage;

    public Pill(String identifier, String name, int morningTaking,
                int afternoonTaking, int eveningTaking, int nightTaking,
                int initialAmount, int currentAmount, int dosage) {
        super(identifier, name, morningTaking, afternoonTaking, eveningTaking, nightTaking);
        setType(Type.PILL);
        setInitialAmount(initialAmount);
        setCurrentAmount(currentAmount);
        setDosage(dosage);
    }

    public int getInitialAmount() {
        return this.initialAmount;
    }

    public void setInitialAmount(int initialAmount) {
        this.initialAmount = initialAmount;
    }

    public int getCurrentAmount() {
        return this.currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int getDosage() {
        return this.dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }
}
