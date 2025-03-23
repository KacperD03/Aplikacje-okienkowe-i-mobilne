package com.example.projekt3okienka;

public enum EmployeeCondition {
    OBECNY {
        @Override
        public String toString() {
            return "obecny";
        }
    },
    DELEGACJA {
        @Override
        public String toString() {
            return "delegacja";
        }
    },
    CHORY {
        @Override
        public String toString() {
            return "chory";
        }
    },
    NIEOBECNY {
        @Override
        public String toString() {
            return "nieobecny";
        }
    }
}
