package W6RedScare;

public enum ResultWithIntInfo {
    VALUE {
        private int value;

        public ResultWithIntInfo setValue(int value) {
            this.value = value;
            return this;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public boolean isHard() {
            return false;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    },

    HARD {
        @Override
        public ResultWithIntInfo setValue(int value) {
            throw new UnsupportedOperationException("HARD doesn't have a value.");
        }

        @Override
        public int getValue() {
            throw new UnsupportedOperationException("HARD doesn't have a value.");
        }

        @Override
        public boolean isHard() {
            return true;
        }

        @Override
        public String toString() {
            return "HARD";
        }
    };

    // Abstract methods to be implemented by each enum constant
    public abstract int getValue();
    public abstract ResultWithIntInfo setValue(int value);
    public abstract boolean isHard();
}
